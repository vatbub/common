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


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
public class ReportingDialog {
    private static URL defaultGitReportsURL = null;
    private static Stage stage;
    private static Scene scene;
    private static String windowTitle;
    private static URL finalURL;
    private static Throwable e;


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

    public void show(String userName, String repoName) {
        String windowTitle = null;
        show(windowTitle, userName, repoName, null);
    }

    public void show(String userName, String repoName, Throwable e) {
        show(defaultGitReportsURL, userName, repoName, e);
    }

    public void show(URL gitReportsBaseURL, String userName, String repoName) {
        show(gitReportsBaseURL, userName, repoName, null);
    }

    public void show(URL gitReportsBaseURL, String userName, String repoName, Throwable e) {
        String windowTitle = null;
        show(windowTitle, gitReportsBaseURL, userName, repoName, e);
    }

    public void show(String windowTitle, String userName, String repoName) {
        show(windowTitle, userName, repoName, null);
    }

    public void show(String windowTitle, String userName, String repoName, Throwable e) {
        show(windowTitle, defaultGitReportsURL, userName, repoName, e);
    }

    public void show(String windowTitle, URL gitReportsBaseURL, String userName, String repoName) {
        show(windowTitle, gitReportsBaseURL, userName, repoName, null);
    }

    public void show(String windowTitle, URL gitReportsBaseURL, String userName, String repoName, Throwable e) {
        stage = new Stage();
        Parent root;
        try {
            String finalURLString = gitReportsBaseURL.toString() + "/issue/" + userName + "/" + repoName + "/";
            if (e!=null){
                // set the details value
                String details = "\n\n------------------------------\nStacktrace is:\n" + ExceptionUtils.getFullStackTrace(e) + "\n------------------------------";
                finalURLString = finalURLString + "?details=" + URLEncoder.encode(details, "UTF-8");
            }
            FOKLogger.info(ReportingDialog.class.getName(), "Final reporting window url is " + finalURLString);
            finalURL = new URL(finalURLString);

            root = FXMLLoader.load(UpdateAvailableDialog.class.getResource("/view/reporting/ReportingDialog.fxml"));
            scene = new Scene(root);

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
        if (this.windowTitle != null) {
            stage.setTitle(windowTitle);
        } else {
            // bind the window title to the web page title
            webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                stage.setTitle(getWebPageTitle(webView.getEngine()));
            });
        }

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
