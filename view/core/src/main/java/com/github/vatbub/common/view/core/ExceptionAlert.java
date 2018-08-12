package com.github.vatbub.common.view.core;

/*-
 * #%L
 * FOKProjects Common
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
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


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ResourceBundle;

/**
 * A dialog that can show an exception
 */
public class ExceptionAlert extends Alert {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("com.github.vatbub.common.view.core.ExceptionAlert");

    public ExceptionAlert(Throwable e) {
        this(e, bundle.getString("defaultErrorText"));
    }

    public ExceptionAlert(Throwable e, String contentText, ButtonType... buttonTypes) {
        super(AlertType.ERROR, contentText, buttonTypes);

        String exceptionText = ExceptionUtils.getFullStackTrace(e);

        Label label = new Label(bundle.getString("stackTraceLabel"));

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        this.getDialogPane().setExpandableContent(expContent);
    }
}
