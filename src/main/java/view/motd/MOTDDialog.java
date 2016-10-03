/**
 * Sample Skeleton for 'MOTDDialog.fxml' Controller Class
 */

package view.motd;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.rometools.rome.feed.synd.SyndContent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import logging.FOKLogger;
import view.updateAvailableDialog.UpdateAvailableDialog;

public class MOTDDialog {

	private static Stage stage;
	private ResourceBundle bundle = ResourceBundle.getBundle("view.motd.MOTDDialog");
	FOKLogger log = new FOKLogger(MOTDDialog.class.getName());
	private static MOTD motd;

	@Deprecated
	public MOTDDialog() {

	}
	
	public MOTDDialog(MOTD motd){
		this(motd, motd.getFeedTitle());
	}

	public MOTDDialog(MOTD motd, String windowTitle) {
		MOTDDialog.motd = motd;
		this.show(windowTitle);
	}

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
		try {
			Desktop.getDesktop().browse(new URI(motd.getEntry().getUri()));
			hide();
		} catch (IOException | URISyntaxException e) {
			log.getLogger().log(Level.SEVERE, "An error occurred", e);
		}
	}

	@FXML
	void closeButtonOnAction(ActionEvent event) {
		hide();
	}

	private void show(String windowTitle) {
		stage = new Stage();
		Parent root;
		try {
			root = FXMLLoader.load(UpdateAvailableDialog.class.getResource("/view/motd/MOTDDialog.fxml"), bundle);
			Scene scene = new Scene(root);
			stage.setAlwaysOnTop(true);
			stage.getIcons().add(new Image(new URL(motd.getImage().getUrl()).openStream()));
			// scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Set the window title
			stage.setTitle(windowTitle);

			stage.setMinWidth(scene.getRoot().minWidth(0) + 70);
			stage.setMinHeight(scene.getRoot().minHeight(0) + 70);

			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			log.getLogger().log(Level.SEVERE, "An error occurred", e);
		}
	}

	private void hide() {
		try {
			motd.markAsRead();
			stage.hide();
		} catch (ClassNotFoundException | IOException e) {
			log.getLogger().log(Level.SEVERE, "An error occurred", e);
		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is
			// complete
	void initialize() {
		assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'MOTDDialog.fxml'.";
		assert rssWebView != null : "fx:id=\"rssWebView\" was not injected: check your FXML file 'MOTDDialog.fxml'.";
		assert openWebpageButton != null : "fx:id=\"openWebpageButton\" was not injected: check your FXML file 'MOTDDialog.fxml'.";

		// Get the motd content
		String content = "";
		for (SyndContent str : motd.getEntry().getContents()) {
			if (str.getValue() != null) {
				content = content + str.getValue();
			}
		}

		if (content.contains("<span id=\"more")) {
			// We've got a read more link so stop parsing the message
			// and change the button caption to imply that there is more
			// to read
			content = content.substring(0, content.indexOf("<span id=\"more"));
			openWebpageButton.setText(bundle.getString("readMoreLink"));
		}

		System.out.println(content);

		rssWebView.getEngine().loadContent(content);
	}
}
