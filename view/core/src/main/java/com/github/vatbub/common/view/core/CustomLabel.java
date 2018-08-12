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


import javafx.scene.control.Label;

public class CustomLabel extends Label {

    private String permText = "";

    public CustomLabel() {
        super();
    }

    public CustomLabel(String arg0) {
        super(arg0);
    }

    /**
     * Sets a temporary text. Use {@link #resetText()} to reset the text to its
     * current value.
     *
     * @param text The temporary text to set.
     */
    public void setTemporaryText(String text) {
        if (permText.equals("")) {
            permText = this.getText();
        }
        this.setText(text);
    }

    /**
     * Resets the text of this label to its value prior to calling
     * {@link #setTemporaryText(String)}
     */
    public void resetText() {
        if (!permText.equals("")) {
            this.setText(permText);
        }
        permText = "";
    }

}
