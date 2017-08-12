package com.github.vatbub.common.view.reporting;

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


import com.github.vatbub.common.core.logging.FOKLogger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class ReportingDialogUploadProgress {
    private static ReportingDialogUploadProgress currentInstance;
    private static Stage stage;
    @FXML
    private Label statusLabel;

    public ReportingDialogUploadProgress() {
        currentInstance = this;
    }

    public static void show() {
        FOKLogger.info(ReportingDialog.class.getName(), "Showing the ReportingDialogUploadProgress...");
        stage = new Stage();
        Parent root;
        try {
            String details = "";

            root = FXMLLoader.load(ReportingDialogUploadProgress.class.getResource("ReportingDialogUploadProgress.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(ReportingDialogUploadProgress.class.getResource("ReportingDialog.css").toExternalForm());

            stage.setMinWidth(scene.getRoot().minWidth(0) + 70);
            stage.setMinHeight(scene.getRoot().minHeight(0) + 70);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e2) {
            FOKLogger.log(ReportingDialog.class.getName(), Level.SEVERE, "An error occurred", e2);
        }
    }

    public static void hide() {
        stage.hide();
    }

    public static Label getStatusLabel() {
        return currentInstance.statusLabel;
    }
}
