package view;

import javafx.animation.*;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class CustomProgressBar extends ProgressBar {

	public CustomProgressBar() {
		super();
	}

	public CustomProgressBar(double arg0) {
		super(arg0);
	}

	/**
	 * Like the usual {@code setProgress}-method but adds a smooth animation
	 * 
	 * @param progress
	 *            The progress to set
	 */
	public void setProgressAnimated(double progress) {
		Timeline timeline = new Timeline();

		KeyValue keyValue = new KeyValue(this.progressProperty(), progress);
		KeyFrame keyFrame = new KeyFrame(new Duration(400), keyValue);
		timeline.getKeyFrames().add(keyFrame);

		timeline.play();
	}

}
