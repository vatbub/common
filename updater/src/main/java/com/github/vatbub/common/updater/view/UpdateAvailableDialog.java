package com.github.vatbub.common.updater.view;

/*-
 * #%L
 * FOKProjects Common
 * %%
 * Copyright (C) 2016 Frederik Kammel
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

import com.github.vatbub.common.core.Common;
import com.github.vatbub.common.updater.UpdateChecker;
import com.github.vatbub.common.updater.UpdateInfo;
import com.github.vatbub.common.updater.UpdateProgressDialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class UpdateAvailableDialog implements UpdateProgressDialog {

    private static Stage stage;
    private static String messageText;
    private static UpdateInfo updateInfo;
    private static Thread downloadThread = new Thread();
    private final ResourceBundle bundle = ResourceBundle.getBundle("com.github.vatbub.common.updater.view.AlertDialog");
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="updateProgressBar"
    private ProgressBar updateProgressBar; // Value injected by FXMLLoader
    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader
    @FXML // fx:id="detailsLabel"
    private Label detailsLabel; // Value injected by FXMLLoader
    @FXML // fx:id="messageLabel"
    private Label messageLabel; // Value injected by FXMLLoader
    @FXML // fx:id="okButton"
    private Button okButton; // Value injected by FXMLLoader
    @FXML // fx:id="updateProgressAnimation"
    private ProgressIndicator updateProgressAnimation; // Value injected by
    @FXML // fx:id="updateProgressText"
    private Label updateProgressText; // Value injected by FXMLLoader

    /**
     * Constructor stub necessary for FXML, please use
     * {@link #UpdateAvailableDialog(UpdateInfo update)}.
     */
    @Deprecated
    public UpdateAvailableDialog() {

    }
    // FXMLLoader

    /**
     * Constructs a new UpdateAvailableDialog and presents the specified update
     * info to the user.
     *
     * @param update The info about the update do install.
     */
    public UpdateAvailableDialog(UpdateInfo update) {
        show(update);
    }

    // Handler for Button[fx:id="cancelButton"] onAction
    @FXML
    void ignoreButtonOnAction(ActionEvent event) {
        if (!downloadThread.isAlive()) {
            // ignore this update
            this.hide();
            UpdateChecker.ignoreUpdate(updateInfo.toVersion);
        } else {
            // Cancel download
            UpdateChecker.cancelDownloadAndLaunch(this);
        }
    }

    // Handler for Button[fx:id="okButton"] onAction
    @FXML
    void okButtonOnAction(ActionEvent event) {
        UpdateAvailableDialog t = this;

        okButton.setDisable(true);
        cancelButton.setText(bundle.getString("button.cancel.cancelDownload"));

        downloadThread = new Thread(() -> {
            try {
                boolean res = UpdateChecker.downloadAndInstallUpdate(updateInfo, t);
                if (res) {
                    t.hide();
                }
            } catch (IllegalStateException | IOException e) {
                showErrorMessage(e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
        downloadThread.start();
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is
        // complete
    void initialize() {
        assert cancelButton != null : "fx:id=\"cancelButton\" was not injected: check your FXML file 'AlertDialog.fxml'.";
        assert detailsLabel != null : "fx:id=\"detailsLabel\" was not injected: check your FXML file 'AlertDialog.fxml'.";
        assert messageLabel != null : "fx:id=\"messageLabel\" was not injected: check your FXML file 'AlertDialog.fxml'.";
        assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'AlertDialog.fxml'.";
        assert updateProgressAnimation != null : "fx:id=\"updateProgressAnimation\" was not injected: check your FXML file 'AlertDialog.fxml'.";
        assert updateProgressBar != null : "fx:id=\"updateProgressBar\" was not injected: check your FXML file 'AlertDialog.fxml'.";
        assert updateProgressText != null : "fx:id=\"updateProgressText\" was not injected: check your FXML file 'AlertDialog.fxml'.";
        // Initialize your logic here: all @FXML variables will have been
        // injected
        if (updateInfo.showAlert) {
            // an update is available, show its info
            detailsLabel.setText(messageText);
        } else {
            // No update is available, show a corresponding message
            detailsLabel.setText("");
            messageLabel.setText(bundle.getString("label.noUpdate"));
            okButton.setDisable(true);
            cancelButton.setDisable(true);

        }
        updateProgressAnimation.setVisible(false);
        updateProgressText.setVisible(false);
    }

    private void show(UpdateInfo update) {
        stage = new Stage();
        Parent root;
        try {

            if (update != null) {
                if (update.fileSizeInMB != -1) {
                    messageText = "Filesize: " + (Math.round(update.fileSizeInMB * 100)) / 100.0
                            + " MB, Version to download: " + update.toVersion.toString();
                } else {
                    // File sie unknown, see Javadoc of UpdateInfo
                    messageText = "Filesize: unknown, Version to download: " + update.toVersion.toString();
                }
            }
            updateInfo = update;

            root = FXMLLoader.load(UpdateAvailableDialog.class.getResource("AlertDialog.fxml"), bundle);
            Scene scene = new Scene(root);
            // scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            // Set the window title and put the app name in it
            stage.setTitle(bundle.getString("window.Title").replace("{appName}", Common.getInstance().getAppName()));

            stage.setMinWidth(scene.getRoot().minWidth(0) + 70);
            stage.setMinHeight(scene.getRoot().minHeight(0) + 70);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void hide() {
        Platform.runLater(() -> stage.hide());
    }

    @Override
    public void preparePhaseStarted() {
        Platform.runLater(() -> {
            updateProgressAnimation.setVisible(true);
            updateProgressText.setVisible(true);
            updateProgressText.setText(bundle.getString("progress.preparing"));
        });
    }

    @Override
    public void downloadStarted() {
        Platform.runLater(() -> {
            updateProgressText.setText(bundle.getString("progress.downloading"));
            updateProgressBar.setVisible(true);
            updateProgressBar.setProgress(-1);
        });
    }

    @Override
    public void downloadProgressChanged(double kilobytesDownloaded, double totalFileSizeInKB) {
        Platform.runLater(() -> updateProgressBar.setProgress(kilobytesDownloaded / totalFileSizeInKB));
    }

    @Override
    public void installStarted() {
        Platform.runLater(() -> updateProgressText.setText(bundle.getString("progress.installing")));
    }

    @Override
    public void launchStarted() {
        Platform.runLater(() -> updateProgressText.setText(bundle.getString("progress.launching")));
    }

    @Override
    public void cancelRequested() {
        Platform.runLater(() -> updateProgressText.setText(bundle.getString("progress.cancelRequested")));
    }

    @Override
    public void operationCanceled() {
        Platform.runLater(() -> {
            updateProgressAnimation.setVisible(false);
            updateProgressText.setVisible(false);
            okButton.setText(bundle.getString("button.ok"));
            okButton.setDisable(false);
            cancelButton.setText(bundle.getString("button.cancel"));
            updateProgressBar.setVisible(false);
        });
    }

    public void showErrorMessage(String message) {
        Platform.runLater(() -> {
            detailsLabel.setText("An error occurred:\n" + message);
            updateProgressAnimation.setVisible(false);
            updateProgressText.setVisible(false);
            okButton.setDisable(false);
            okButton.setText(bundle.getString("button.ok.retry"));
            updateProgressBar.setVisible(false);
        });
    }
}
