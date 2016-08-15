package common;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Control;
import javafx.util.Duration;
import logging.FOKLogger;

public class Animations {
	
	public static FOKLogger log = new FOKLogger(Animations.class.getName());

	public static void disableControl(Control c, boolean noAnimation) {
		if (noAnimation) {
			disableControl(c, Duration.millis(0.1));
		} else {
			disableControl(c);
		}
	}

	public static void disableControl(Control c) {
		if (!c.isDisabled()) {
			disableControl(c, Duration.millis(50));
		}
	}

	private static void disableControl(Control c, Duration dur) {
		log.getLogger().info("Disabling control " + c.toString() + ", dur = " + dur.toMillis());
		ScaleTransition st = new ScaleTransition(dur, c);
		st.setByX(-0.5);
		st.setByY(0.0);
		st.setCycleCount(1);
		st.setAutoReverse(false);

		st.play();

		c.setDisable(true);
	}

	public static void enableControl(Control c, boolean noAnimation) {
		if (noAnimation) {
			enableControl(c, Duration.millis(0.1));
		} else {
			enableControl(c);
		}
	}

	public static void enableControl(Control c) {
		if (!c.isDisabled()) {
			enableControl(c, Duration.millis(50));
		}
	}

	private static void enableControl(Control c, Duration dur) {
		log.getLogger().info("Enabling control " + c.toString() + ", dur = " + dur.toMillis());
		ScaleTransition st = new ScaleTransition(dur, c);
		st.setByX(0.5);
		st.setByY(0.0);
		st.setCycleCount(1);
		st.setAutoReverse(false);

		st.play();

		c.setDisable(true);
	}

}
