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

/**
 * A default javaFx dialog to present a {@link MOTD} to the user using a WebView
 * and CSS.<br>
 * <b>Please note:</b> This class is <i>*NOT*</i> thread safe.
 * 
 * @author frede
 * @since 0.0.15
 *
 */
public class MOTDDialog {

	private static Stage stage;
	private ResourceBundle bundle = ResourceBundle.getBundle("view.motd.MOTDDialog");
	FOKLogger log = new FOKLogger(MOTDDialog.class.getName());
	private static MOTD motd;
	private static String css;
	public static final String defaultCss = "body {font: 14px/24px \"Source Sans Pro\", sans-serif; background: rgba(0,0,0,0.05);}\n	a {text-decoration: none; -webkit-transition: all 0.3s ease-in-out; -moz-transition: all 0.3s ease-in-out; -ms-transition: all 0.3s ease-in-out; -o-transition: all 0.3s ease-in-out; transition: all 0.3s ease-in-out;}\n	a:hover, a:focus {color: #443f3f; text-decoration: none; outline: 0; -webkit-transition: all 0.3s ease-in-out; -moz-transition: all 0.3s ease-in-out; -ms-transition: all 0.3s ease-in-out; -o-transition: all 0.3s ease-in-out; transition: all 0.3s ease-in-out;}\n	img {max-width: 100%; height: auto;}\n	strong {font-weight: 600;}\n	h1 { font: 52px/1.1 \"Raleway\", sans-serif;}\n	h2 { font: 42px/1.1 \"Raleway\", sans-serif;}\n	h3 { font: 32px/1.1 \"Raleway\", sans-serif;}\n	h4 { font: 25px/1.1 \"Raleway\", sans-serif;}\n	h5 { font: 20px/1.1 \"Raleway\", sans-serif;}\n	h6 { font: 18px/1\n	.1 \"Raleway\", sans-serif;}\n	h1, h2, h3, h4, h5, h6 {color: #443f3f; font-weight: 600; margin: 10px 0 24px;}\n	table {width: 100%;}\n	th,td {border: 1px solid #333; padding: 1px; text-align: center;}";
	private static Scene scene;

	@Deprecated
	/**
	 * Only exists for the FXMLLoader
	 */
	public MOTDDialog() {

	}

	/**
	 * Creates a new {@code MOTDDialog} with the default settings. The window
	 * title will be the feed title retreived using {@link MOTD#getFeedTitle()}
	 * 
	 * @param motd
	 *            The {@link MOTD} to display
	 */
	public MOTDDialog(MOTD motd) {
		this(motd, motd.getFeedTitle());
	}

	/**
	 * Creates a new {@code MOTDDialog} with a custom windowTitle.
	 * 
	 * @param motd
	 *            The {@link MOTD} to display
	 * @param windowTitle
	 *            The title of the javaFx stage
	 */
	public MOTDDialog(MOTD motd, String windowTitle) {
		this(motd, windowTitle, defaultCss);
	}

	/**
	 * Creates a new {@code MOTDDialog} with a custom windowTitle.
	 * 
	 * @param motd
	 *            The {@link MOTD} to display
	 * @param windowTitle
	 *            The title of the javaFx stage
	 * @param contentCss
	 *            The css style of the motd. <b>Please note: </b> The specified
	 *            style will be used as an inline css stylesheet.
	 */
	public MOTDDialog(MOTD motd, String windowTitle, String contentCss) {
		MOTDDialog.motd = motd;
		css = contentCss;
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
			scene = new Scene(root);
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

	/**
	 * Hides this {@code MOTDDialog} and marks its {@link MOTD} as read using
	 * {@link MOTD#markAsRead()}
	 */
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
		String content = "<head><style>" + css + "</style></head><body><div class=\"motdContent\">";
		for (SyndContent str : motd.getEntry().getContents()) {
			if (str.getValue() != null) {
				content = content + str.getValue();
			}
		}
		content = content + "</div></body>";

		if (content.contains("<span id=\"more")) {
			// We've got a read more link so stop parsing the message
			// and change the button caption to imply that there is more
			// to read
			content = content.substring(0, content.indexOf("<span id=\"more"));
			openWebpageButton.setText(bundle.getString("readMoreLink"));
		}

		log.getLogger().finest("MOTD content:\n" + content);

		rssWebView.getEngine().loadContent(content);
	}
}
