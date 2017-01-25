package view;

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
    private static ResourceBundle bundle = ResourceBundle.getBundle("view.ExceptionAlert");

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
