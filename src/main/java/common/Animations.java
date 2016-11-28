package common;

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
