package view;

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
	 * @param text
	 *            The temporary text to set.
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
