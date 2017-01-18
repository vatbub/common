package view.reporting;

/*-
 * #%L
 * FOKProjects Common
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import common.AWSS3Utils;
import common.Common;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressBar;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logging.FOKLogger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import view.updateAvailableDialog.UpdateAvailableDialog;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

/**
 * A web view that shows a reporting page of <a href="https://gitreports.com/">GitReports</a>, a website to submit anonymous GitHub issues.<br>
 * <b>Please note:</b> This class is <i>*NOT*</i> thread safe.
 *
 * @author frede
 * @since 0.0.22
 */
@SuppressWarnings({"SameParameterValue", "ConstantConditions"})
public class ReportingDialog {
    private static final String s3BucketName = "vatbubissuelogs2";
    private static URL defaultGitReportsURL = null;
    private static Stage stage;
    private static String windowTitle;
    private static URL finalURL;

    static {
        try {
            defaultGitReportsURL = new URL("https://gitreports.com/");
        } catch (MalformedURLException e) {
            // will not happen
            e.printStackTrace();
        }
    }

    @FXML
    private WebView webView;
    @FXML
    private ProgressBar progressBar;

    public ReportingDialog() {

    }

    @SuppressWarnings("unused")
    public void show(String userName, String repoName) {
        show(userName, repoName, false);
    }

    public void show(String userName, String repoName, boolean includeLatestLogFile) {
        String windowTitle = null;
        show(windowTitle, userName, repoName, null, includeLatestLogFile);
    }

    @SuppressWarnings("unused")
    public void show(String userName, String repoName, Throwable e) {
        show(userName, repoName, e, false);
    }

    public void show(String userName, String repoName, Throwable e, boolean includeLatestLogFile) {
        show(defaultGitReportsURL, userName, repoName, e, includeLatestLogFile);
    }

    @SuppressWarnings("unused")
    public void show(URL gitReportsBaseURL, String userName, String repoName) {
        show(gitReportsBaseURL, userName, repoName, false);
    }

    public void show(URL gitReportsBaseURL, String userName, String repoName, boolean includeLatestLogFile) {
        show(gitReportsBaseURL, userName, repoName, null, includeLatestLogFile);
    }

    @SuppressWarnings("unused")
    public void show(URL gitReportsBaseURL, String userName, String repoName, Throwable e) {
        show(gitReportsBaseURL, userName, repoName, e, false);
    }

    public void show(URL gitReportsBaseURL, String userName, String repoName, Throwable e, boolean includeLatestLogFile) {
        String windowTitle = null;
        show(windowTitle, gitReportsBaseURL, userName, repoName, e, includeLatestLogFile);
    }

    @SuppressWarnings("unused")
    public void show(String windowTitle, String userName, String repoName) {
        show(windowTitle, userName, repoName, false);
    }

    public void show(String windowTitle, String userName, String repoName, boolean includeLatestLogFile) {
        show(windowTitle, userName, repoName, null, includeLatestLogFile);
    }

    @SuppressWarnings("unused")
    public void show(String windowTitle, String userName, String repoName, Throwable e) {
        show(windowTitle, userName, repoName, e, false);
    }

    public void show(String windowTitle, String userName, String repoName, Throwable e, boolean includeLatestLogFile) {
        show(windowTitle, defaultGitReportsURL, userName, repoName, e, includeLatestLogFile);
    }

    @SuppressWarnings("unused")
    public void show(String windowTitle, URL gitReportsBaseURL, String userName, String repoName) {
        show(windowTitle, gitReportsBaseURL, userName, repoName, false);
    }

    public void show(String windowTitle, URL gitReportsBaseURL, String userName, String repoName, boolean includeLatestLogFile) {
        show(windowTitle, gitReportsBaseURL, userName, repoName, null, includeLatestLogFile);
    }

    @SuppressWarnings("unused")
    public void show(String windowTitle, URL gitReportsBaseURL, String userName, String repoName, Throwable e) {
        show(windowTitle, gitReportsBaseURL, userName, repoName, e, false);
    }

    public void show(String windowTitle, URL gitReportsBaseURL, String userName, String repoName, Throwable e, boolean includeLatestLogFile) {
        FOKLogger.info(ReportingDialog.class.getName(), "Showing the ReportingDialog...");
        stage = new Stage();
        Parent root;
        try {
            String details = "";

            if (includeLatestLogFile) {
                String awsFileName = Common.getAppName() + "/" + FOKLogger.getLogFileName();
                // write the aws key to the details to find the log file again
                details = details + "\n\n------------------------------\nLog file aws key: " + awsFileName + "\n------------------------------";

                // upload the logs to s3
                Thread awsUploadThread = new Thread(() -> {
                    try {
                        AmazonS3Client s3Client = new AmazonS3Client(Common.getAWSCredentials());
                        if (!AWSS3Utils.doesBucketExist(s3Client, s3BucketName)) {
                            // create bucket
                            FOKLogger.info(ReportingDialog.class.getName(), "Creating aws s3 bucket " + s3BucketName);
                            s3Client.createBucket(s3BucketName);
                        }

                        if (!AWSS3Utils.keyExists(s3Client, s3BucketName, Common.getAppName())) {
                            FOKLogger.info(ReportingDialog.class.getName(), "Creating aws s3 folder " + Common.getAppName());
                            // Create folder
                            AWSS3Utils.createFolder(s3Client, s3BucketName, Common.getAppName());
                        }

                        // Upload the log file
                        FOKLogger.info(ReportingDialog.class.getName(), "Uploading log file to aws: " + awsFileName);
                        s3Client.putObject(new PutObjectRequest(s3BucketName, awsFileName, new File(FOKLogger.getLogFilePathAndName())));
                        FOKLogger.info(ReportingDialog.class.getName(), "Upload completed");
                    } catch (AmazonServiceException ase) {
                        FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "Caught AmazonServiceException which means that the request made it to S3, but was rejected with an error response", ase);
                    } catch (AmazonClientException ace) {
                        FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.", ace);
                    }
                });
                awsUploadThread.setName("awsUploadThread");
                awsUploadThread.start();
            }

            String finalURLString = gitReportsBaseURL.toString() + "/issue/" + userName + "/" + repoName + "/";

            if (e != null) {
                // set the details value
                details = details + "\n\n------------------------------\nStacktrace is:\n" + ExceptionUtils.getFullStackTrace(e) + "\n------------------------------";
            }

            finalURLString = finalURLString + "?details=" + URLEncoder.encode(details, "UTF-8");
            ReportingDialog.windowTitle = windowTitle;
            FOKLogger.info(ReportingDialog.class.getName(), "Final reporting window url is " + finalURLString);
            finalURL = new URL(finalURLString);

            root = FXMLLoader.load(UpdateAvailableDialog.class.getResource("/view/reporting/ReportingDialog.fxml"));
            Scene scene = new Scene(root);

            stage.setMinWidth(scene.getRoot().minWidth(0) + 70);
            stage.setMinHeight(scene.getRoot().minHeight(0) + 70);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e2) {
            FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "An error occurred", e2);
        }
    }

    @FXML
    void initialize() {
        webView.prefHeightProperty().addListener((observableValue, number, number2) -> stage.setHeight((double) number2 + 85));

        // set the window title
        if (windowTitle != null) {
            stage.setTitle(windowTitle);
        } else {
            // bind the window title to the web page title
            webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    stage.setTitle(getWebPageTitle(webView.getEngine()));
                } catch (NullPointerException e) {
                    // do nothing
                }
            });
        }

        webView.getEngine().getLoadWorker().progressProperty().addListener((observable, oldValue, newValue) -> {
            progressBar.setProgress(newValue.doubleValue());
            if (newValue.doubleValue() == 1.0) {
                progressBar.setVisible(false);
                progressBar.setPrefHeight(0);
            } else {
                progressBar.setVisible(true);
                // reset to use computed value
                progressBar.setPrefHeight(Control.USE_COMPUTED_SIZE);
            }
        });

        webView.getEngine().load(finalURL.toString());
    }

    private String getWebPageTitle(WebEngine webEngine) {
        Document doc = webEngine.getDocument();
        NodeList heads = doc.getElementsByTagName("head");
        String titleText = webEngine.getLocation(); // use location if page does not define a title
        if (heads.getLength() > 0) {
            Element head = (Element) heads.item(0);
            NodeList titles = head.getElementsByTagName("title");
            if (titles.getLength() > 0) {
                Node title = titles.item(0);
                titleText = title.getTextContent();
            }
        }
        return titleText;
    }
}
