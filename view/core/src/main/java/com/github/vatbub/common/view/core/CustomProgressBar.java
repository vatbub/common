package com.github.vatbub.common.view.core;

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


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
     * @param progress The progress to set
     */
    public void setProgressAnimated(double progress) {
        Timeline timeline = new Timeline();

        KeyValue keyValue = new KeyValue(this.progressProperty(), progress);
        KeyFrame keyFrame = new KeyFrame(new Duration(400), keyValue);
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
    }

}
