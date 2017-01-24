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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.AWSS3Utils;
import common.Common;
import common.Internet;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logging.FOKLogger;
import reporting.GitHubIssue;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
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
    private static ResourceBundle bundle = ResourceBundle.getBundle("view.reporting.ReportingDialog");
    private static URL logInfoURL = null;
    private static URL defaultLogInfoURL;
    private static URL gitReportsBaseURL;

    static {
        try {
            defaultGitReportsURL = new URL("https://gitreports.com/");
            defaultLogInfoURL = new URL("https://github.com/vatbub/zorkClone/wiki/Getting-the-logs");
        } catch (MalformedURLException e) {
            // will not happen
            e.printStackTrace();
        }
    }

    private GitHubIssue gitHubIssue = new GitHubIssue();
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField name;

    @FXML
    private TextField mail;

    @FXML
    private TextField title;

    @FXML
    private TextArea message;

    @FXML
    private CheckBox uploadLogsCheckbox;

    @FXML
    private Button logInfoButton;

    @FXML
    private Button sendButton;

    @SuppressWarnings("unused")
    public ReportingDialog() {

    }

    @SuppressWarnings("unused")
    public ReportingDialog(URL logInfoURL) {
        ReportingDialog.logInfoURL = logInfoURL;
    }

    @SuppressWarnings("unused")
    public void show(String userName, String repoName) {
        String windowTitle = null;
        show(windowTitle, userName, repoName, null);
    }

    @SuppressWarnings("unused")
    public void show(String userName, String repoName, Throwable e) {
        show(defaultGitReportsURL, userName, repoName, e);
    }

    @SuppressWarnings("unused")
    public void show(URL gitReportsBaseURL, String userName, String repoName) {
        String windowTitle = null;
        show(windowTitle, gitReportsBaseURL, userName, repoName);
    }

    public void show(URL gitReportsBaseURL, String userName, String repoName, Throwable e) {
        String windowTitle = null;
        show(windowTitle, gitReportsBaseURL, userName, repoName, e);
    }

    @SuppressWarnings("unused")
    public void show(String windowTitle, String userName, String repoName) {
        show(windowTitle, defaultGitReportsURL, userName, repoName);
    }

    public void show(String windowTitle, String userName, String repoName, Throwable e) {
        show(windowTitle, defaultGitReportsURL, userName, repoName, e);
    }


    public void show(String windowTitle, URL gitReportsBaseURL, String userName, String repoName) {
        show(windowTitle, gitReportsBaseURL, userName, repoName, null);
    }

    public void show(String windowTitle, URL gitReportsBaseURL, String userName, String repoName, Throwable e) {
        FOKLogger.info(ReportingDialog.class.getName(), "Showing the ReportingDialog...");
        stage = new Stage();
        Parent root;
        try {
            gitHubIssue.setThrowable(e);
            gitHubIssue.setToRepo_Owner(userName);
            gitHubIssue.setToRepo_RepoName(repoName);
            ReportingDialog.gitReportsBaseURL = gitReportsBaseURL;

            root = FXMLLoader.load(ReportingDialog.class.getResource("/view/reporting/ReportingDialog.fxml"), bundle);
            Scene scene = new Scene(root);
            scene.setUserAgentStylesheet(ReportingDialog.class.getResource("/view/reporting/ReportingDialog.css").toString());

            stage.setMinWidth(scene.getRoot().minWidth(0) + 70);
            stage.setMinHeight(scene.getRoot().minHeight(0) + 70);

            stage.setScene(scene);
            stage.setTitle(windowTitle);
            stage.show();
        } catch (IOException e2) {
            FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "An error occurred", e2);
        }
    }

    @FXML
    void initialize() {
        anchorPane.prefHeightProperty().addListener((observableValue, number, number2) -> stage.setHeight((double) number2 + 85));
    }

    @FXML
    void logInfoButtonOnAction(ActionEvent event) {
        URL finalURL;
        if (logInfoURL == null) {
            finalURL = defaultLogInfoURL;
        } else {
            finalURL = logInfoURL;
        }

        try {
            Desktop.getDesktop().browse(finalURL.toURI());
        } catch (IOException | URISyntaxException e) {
            FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "An error occurred", e);
        }
    }

    @FXML
    void sendButtonOnAction(ActionEvent event) {
        if (validate(message) || validate(title)) {
            // something is not ok
            new Alert(Alert.AlertType.ERROR, "Please fill out all required fields").show();
            return;
        }

        // Everything ok
        // disable all of the controls
        anchorPane.setDisable(true);

        // show the progress dialog
        ReportingDialogUploadProgress.show();

        Thread issueUploadThread = new Thread(() -> {
            if (uploadLogsCheckbox.isSelected()) {
                // upload the logs to aws
                Platform.runLater(() -> ReportingDialogUploadProgress.getStatusLabel().setText(bundle.getString("uploadingLogs")));

                String awsFileName = Common.getAppName() + "/" + FOKLogger.getLogFileName();
                gitHubIssue.setLogLocation(awsFileName);

                // upload the logs to s3
                try {
                    AmazonS3Client s3Client = new AmazonS3Client(Common.getAWSCredentials());
                    if (!AWSS3Utils.doesBucketExist(s3Client, s3BucketName)) {
                        // create bucket
                        FOKLogger.info(ReportingDialog.class.getName(), "Creating aws s3 bucket " + s3BucketName);
                        s3Client.createBucket(s3BucketName);
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
            }

            // post the issue
            Platform.runLater(() -> ReportingDialogUploadProgress.getStatusLabel().setText(bundle.getString("uploadingIssue")));
            gitHubIssue.setBody(message.getText());
            gitHubIssue.setTitle(title.getText());
            gitHubIssue.setReporterEmail(mail.getText());
            gitHubIssue.setReporterName(name.getText());

            try {
                HttpURLConnection connection = (HttpURLConnection) gitReportsBaseURL.openConnection();

                // build the request body
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String query = gson.toJson(gitHubIssue);

                // request header
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Length", Integer.toString(query.length()));
                connection.getOutputStream().write(query.getBytes("UTF8"));
                connection.getOutputStream().flush();
                connection.getOutputStream().close();

                ReportingDialogUploadProgress.hide();
                stage.hide();

                // check the server response
                int responseCode = connection.getResponseCode();
                if (responseCode >= 400) {
                    // something went wrong
                    FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "Something went wrong when trying to upload the issue: " + responseCode + " " + Internet.getReasonForHTTPCode(responseCode));
                    new Alert(Alert.AlertType.ERROR, "Something went wrong when trying to upload the issue: " + responseCode + " " + Internet.getReasonForHTTPCode(responseCode)).show();
                } else {
                    // everything worked
                    new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("thanks")).show();
                }
            } catch (IOException e) {
                FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "An error occurred", e);
            }
        });
        issueUploadThread.setName("issueUploadThread");
        issueUploadThread.start();
    }

    /**
     * Validates the input of the given {@code TextField}
     *
     * @param tf The {@code TextField} to validate
     * @return {@code true} if {@code tf} is <b>NOT</b> ok, {@code false} if it <b>IS</b> ok.
     */
    private boolean validate(TextInputControl tf) {
        boolean res = tf.getText().trim().length() == 0;
        ObservableList<String> styleClass = tf.getStyleClass();
        if (res) {
            if (!styleClass.contains("error")) {
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            styleClass.removeAll(Collections.singleton("error"));
        }

        return res;
    }
}
