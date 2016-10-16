package common;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import logging.FOKLogger;

public class Animations {

	private static FOKLogger log = new FOKLogger(Animations.class.getName());
	private static final Duration defaultDuration = Duration.millis(300);

	public static void disableControl(Node c, boolean noAnimation) {
		if (noAnimation) {
			disableControl(c, Duration.millis(0.1));
		} else {
			disableControl(c);
		}
	}

	public static void disableControl(Node c) {
		if (!c.isDisabled()) {
			disableControl(c, defaultDuration);
		}
	}

	private static void disableControl(Node c, Duration dur) {
		log.getLogger().info("Disabling control " + c.toString() + ", dur = " + dur.toMillis());
		doFadeTransition(c, dur, 0.0);

		c.setDisable(true);
		c.setVisible(false);
	}

	public static void enableControl(Node c, boolean noAnimation) {
		if (noAnimation) {
			enableControl(c, Duration.millis(0.1));
		} else {
			enableControl(c);
		}
	}

	public static void enableControl(Node c) {
		if (!c.isDisabled()) {
			enableControl(c, defaultDuration);
		}
	}

	private static void enableControl(Node c, Duration dur) {
		log.getLogger().info("Enabling control " + c.toString() + ", dur = " + dur.toMillis());
		c.setVisible(true);
		doFadeTransition(c, dur, 1.0);

		c.setDisable(false);
	}

	private static void doFadeTransition(Node c, Duration dur, double toValue) {
		FadeTransition fade = new FadeTransition(dur, c);
		fade.setFromValue(c.getOpacity());
		fade.setToValue(toValue);
		fade.play();
	}

}
