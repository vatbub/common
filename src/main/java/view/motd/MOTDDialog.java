/**
 * Sample Skeleton for 'MOTDDialog.fxml' Controller Class
 */

package view.motd;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logging.FOKLogger;
import view.updateAvailableDialog.UpdateAvailableDialog;

public class MOTDDialog {

	private Stage stage;
	private ResourceBundle bundle = ResourceBundle.getBundle("view.motd.MOTDDialog");
	FOKLogger log = new FOKLogger(MOTDDialog.class.getName());

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="closeButton"
	private Button closeButton; // Value injected by FXMLLoader

	@FXML // fx:id="rssWebView"
	private WebView rssWebView; // Value injected by FXMLLoader

	@FXML // fx:id="openWebpageButton"
	private Button openWebpageButton; // Value injected by FXMLLoader

	@FXML
	void openWebpageButtonOnAction(ActionEvent event) {

	}

	@FXML
	void closeButtonOnAction(ActionEvent event) {

	}

	private void show() {
		stage = new Stage();
		Parent root;
		try {

			root = FXMLLoader.load(UpdateAvailableDialog.class.getResource("AlertDialog.fxml"), bundle);
			Scene scene = new Scene(root);
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Set the window title
			stage.setTitle("RSS Feed title");

			stage.setMinWidth(scene.getRoot().minWidth(0) + 70);
			stage.setMinHeight(scene.getRoot().minHeight(0) + 70);

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			log.getLogger().log(Level.SEVERE, "An error occurred", e);
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize() {
		assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'MOTDDialog.fxml'.";
		assert rssWebView != null : "fx:id=\"rssWebView\" was not injected: check your FXML file 'MOTDDialog.fxml'.";
		assert openWebpageButton != null : "fx:id=\"openWebpageButton\" was not injected: check your FXML file 'MOTDDialog.fxml'.";

	}
}
