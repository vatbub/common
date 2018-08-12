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


import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * A {@link Button} that can display a control text (E. g. {@code Cancel}) and a
 * progress text at the same time. The results are like so:<br>
 * <ul>
 * <li>If you set a controlText (let's say {@code Start operation}) but no
 * progressText: {@code Start operation}</li>
 * <li>If you set a progressText (let's say {@code Download running}) but no
 * controlText: {@code Cancel}</li>
 * <li>If you set a controlText (let's say {@code Cancel}) and a progressText
 * (let's say {@code Download running}): {@code Download running - Cancel}</li>
 * </ul>
 *
 * @author frede
 */

public class ProgressButton extends Button {

    private String controlText = "";
    private String progressText = "";

    /**
     * Creates a button with an empty string for its label.
     */
    public ProgressButton() {
        super();
    }

    /**
     * Creates a button with the specified text as its label.
     *
     * @param text A text string for its label.
     */
    public ProgressButton(String text) {
        super(text);
    }

    /**
     * Creates a button with the specified text and icon for its label.
     *
     * @param text    A text string for its label.
     * @param graphic The icon for its label.
     */
    public ProgressButton(String text, Node graphic) {
        super(text, graphic);
    }

    public String getControlText() {
        return this.controlText;
    }

    public void setControlText(String controlText) {
        this.controlText = controlText;
        updateText();
    }

    public String getProgressText() {
        return this.progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
        updateText();
    }

    private void updateText() {
        String finalText;

        if (!this.progressText.equals("")) {
            finalText = this.progressText;
        } else {
            finalText = "";
        }

        if (!this.progressText.equals("") && !this.controlText.equals("")) {
            // Add -
            finalText = finalText + " - ";
        }

        // add the controlText
        finalText = finalText + controlText;

        super.setText(finalText);
    }
}
