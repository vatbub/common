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
import common.internet.Internet;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import logging.FOKLogger;
import reporting.GitHubIssue;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static final ResourceBundle bundle = ResourceBundle.getBundle("view.reporting.ReportingDialog");
    /**
     * The color in which a required text box appears in case it is not filled in
     */
    private static final String errorColor = "#dd4444";
    /**
     * The default color of a text box
     */
    private static final String defaultColor = "inherit";
    @SuppressWarnings("CanBeFinal")
    private static URL defaultGitReportsURL = null;
    private static Stage stage;
    private static URL logInfoURL;
    private static Scene screenshotScene;
    @SuppressWarnings("CanBeFinal")
    private static URL defaultLogInfoURL;
    private static URL gitReportsBaseURL;
    private static GitHubIssue gitHubIssue;

    static {
        try {
            defaultGitReportsURL = new URL("https://vatbubgitreports.herokuapp.com/");
            defaultLogInfoURL = new URL("https://github.com/vatbub/zorkClone/wiki/Getting-the-logs");
        } catch (MalformedURLException e) {
            // will not happen
            e.printStackTrace();
        }
    }

    private boolean submitButtonWasPressed;
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

    @FXML
    private Button screenshotInfoButton;

    @FXML
    private CheckBox uploadScreenshot;

    @SuppressWarnings("unused")
    public ReportingDialog() {
    }

    @SuppressWarnings("unused")
    public ReportingDialog(URL logInfoURL) {
        this(logInfoURL, null);
    }

    @SuppressWarnings("unused")
    public ReportingDialog(Scene screenshotScene) {
        this(null, screenshotScene);
    }

    @SuppressWarnings("unused")
    public ReportingDialog(URL logInfoURL, Scene screenshotScene) {
        ReportingDialog.logInfoURL = logInfoURL;
        ReportingDialog.screenshotScene = screenshotScene;
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
            gitHubIssue = new GitHubIssue();
            gitHubIssue.setThrowable(e);
            gitHubIssue.setToRepo_Owner(userName);
            gitHubIssue.setToRepo_RepoName(repoName);
            ReportingDialog.gitReportsBaseURL = gitReportsBaseURL;

            root = FXMLLoader.load(ReportingDialog.class.getResource("/view/reporting/ReportingDialog.fxml"), bundle);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(this.getClass().getResource("/view/reporting/ReportingDialog.css").toExternalForm());

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
        // disable screenshot upload if no screenshotScene was given
        if (screenshotScene == null) {
            uploadScreenshot.setDisable(true);
            screenshotInfoButton.setDisable(true);
        }

        // check log upload if an exception is submitted
        if (gitHubIssue.getThrowable() != null) {
            uploadLogsCheckbox.setSelected(true);

            if (screenshotScene != null) {
                uploadScreenshot.setSelected(true);
            }
        }
    }

    @FXML
    void uploadLogsCheckboxOnACtion(ActionEvent event) {
        if (gitHubIssue.getThrowable() != null && !uploadLogsCheckbox.isSelected()) {
            (new Alert(Alert.AlertType.WARNING, bundle.getString("uploadLogsExceptionInfo"), ButtonType.OK)).show();
        }
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
    void screenshotInfoButtonOnAction(ActionEvent event) {
        // TODO Link to a privacy policy
    }

    @FXML
    void titleKeyReleased(InputEvent event) {
        if (submitButtonWasPressed) {
            validate(title);
        }
    }

    @FXML
    void messageKeyReleased(InputEvent event) {
        if (submitButtonWasPressed) {
            validate(message);
        }
    }

    @FXML
    void sendButtonOnAction(ActionEvent event) {
        submitButtonWasPressed = true;
        boolean validationErrors = false;
        if (validate(message)) {
            validationErrors = true;
        }
        if (validate(title)) {
            validationErrors = true;
        }
        if (validationErrors) {
            // something is not ok
            new Alert(Alert.AlertType.ERROR, bundle.getString("error")).show();
            return;
        }

        // Everything ok
        // disable all of the controls
        anchorPane.setDisable(true);

        // show the progress dialog
        ReportingDialogUploadProgress.show();

        // take a screenshot
        WritableImage imageTemp = null;
        if (uploadScreenshot.isSelected()) {
            imageTemp = screenshotScene.snapshot(null);
        }
        final WritableImage image = imageTemp;


        Thread issueUploadThread = new Thread(() -> {
            if (uploadLogsCheckbox.isSelected()) {
                // upload the logs to aws
                Platform.runLater(() -> ReportingDialogUploadProgress.getStatusLabel().setText(bundle.getString("uploadingLogs")));

                String awsFileName = Common.getAppName() + "/logs/" + FOKLogger.getLogFileName();
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

            if (uploadScreenshot.isSelected()) {
                try {
                    // save it to the disk
                    File screenshotFile = new File(Common.getAndCreateAppDataPath() + "screenshotForIssueUpload.png");
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", screenshotFile);

                    // upload the screenshot to aws
                    Platform.runLater(() -> ReportingDialogUploadProgress.getStatusLabel().setText(bundle.getString("uploadingScreenshot")));

                    String awsFileName = Common.getAppName() + "/screenshots/screenshot" + Common.getLaunchTimeStamp() + ".png";
                    gitHubIssue.setScreenshotLocation(awsFileName);

                    // upload the logs to s3
                    AmazonS3Client s3Client = new AmazonS3Client(Common.getAWSCredentials());
                    if (!AWSS3Utils.doesBucketExist(s3Client, s3BucketName)) {
                        // create bucket
                        FOKLogger.info(ReportingDialog.class.getName(), "Creating aws s3 bucket " + s3BucketName);
                        s3Client.createBucket(s3BucketName);
                    }

                    // Upload the screenshot file
                    FOKLogger.info(ReportingDialog.class.getName(), "Uploading screenshot to aws: " + awsFileName);
                    s3Client.putObject(new PutObjectRequest(s3BucketName, awsFileName, screenshotFile));
                    FOKLogger.info(ReportingDialog.class.getName(), "Upload completed");
                } catch (AmazonServiceException ase) {
                    FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "Caught AmazonServiceException which means that the request made it to S3, but was rejected with an error response", ase);
                } catch (AmazonClientException ace) {
                    FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.", ace);
                } catch (IOException e) {
                    FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "Could not write temporary screenshot file, screenshot will not be uploaded", e);
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
                connection.setRequestProperty("Content-Encoding", "UTF-8");
                connection.setRequestProperty("Content-Length", Integer.toString(query.length()));
                connection.setDoOutput(true);
                connection.getOutputStream().write(query.getBytes("UTF8"));
                connection.getOutputStream().flush();
                connection.getOutputStream().close();

                Platform.runLater(() -> {
                    ReportingDialogUploadProgress.hide();

                    stage.hide();

                    // check the server response
                    int responseCode = 0;
                    StringBuilder responseBody = new StringBuilder();
                    try {
                        responseCode = connection.getResponseCode();
                        BufferedReader br;
                        if (200 <= connection.getResponseCode() && connection.getResponseCode() <= 299) {
                            br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                        } else {
                            br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
                        }

                        String line;
                        while ((line = br.readLine()) != null) {
                            responseBody.append(line);
                        }
                    } catch (IOException e) {
                        FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "An error occurred", e);
                    }

                    FOKLogger.info(ReportingDialog.class.getName(), "Submitted GitHub issue, response code from VatbubGitReports-Server: " + responseCode);
                    FOKLogger.info(ReportingDialog.class.getName(), "Response from Server:\n" + responseBody);

                    if (responseCode >= 400) {
                        // something went wrong
                        FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "Something went wrong when trying to upload the issue: " + responseCode + " " + Internet.getReasonForHTTPCode(responseCode));
                        new Alert(Alert.AlertType.ERROR, "Something went wrong when trying to upload the issue: " + responseCode + " " + Internet.getReasonForHTTPCode(responseCode)).show();
                    } else {
                        // everything worked
                        new Alert(Alert.AlertType.CONFIRMATION, bundle.getString("thanks")).show();
                    }
                });
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
                tf.setStyle("-text-area-background: " + errorColor + ";");
                styleClass.add("error");
            }
        } else {
            // remove all occurrences:
            tf.setStyle("-text-area-background: " + defaultColor + ";");
            styleClass.removeAll(Collections.singleton("error"));
        }

        return res;
    }
}
