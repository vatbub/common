package view.tagarea;

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


import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * A gui element that can let a user select tags, similar to <a href="https://github.com/goxr3plus/JavaFX-TagsBar">this Project</a>
 */
@SuppressWarnings("unused")
public class TagArea extends FlowPane {
    private TextField textField = new TextField();

    @SuppressWarnings("unused")
    public TagArea() {
        this.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        getStyleClass().setAll("tag-area");
        textField.setMinSize(60, 30);

        textField.setOnAction(event -> {
            this.addTag(textField.getText());
            textField.clear();
        });
        textField.setOnKeyReleased(event -> {
            System.out.println("[Pressed] Pressed Key: " + event.getCode() + ", Character is: " + event.getCharacter());
            if (event.getCode() == KeyCode.BACK_SPACE && textField.getText().length() == 0) {
                List<Tag> tags = this.getTags();
                if (tags.size() != 0) {
                    Tag lastTag = tags.get(tags.size() - 1);
                    removeTag(lastTag);
                    textField.setText(lastTag.getValue());
                    textField.positionCaret(lastTag.getValue().length());
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.COMMA) {
                System.out.println("TextField value is: " + textField.getText());
                addTag(textField.getText().replaceAll(",", ""));
                textField.clear();
                event.consume();
            }
        });
        this.getChildren().add(textField);
    }

    @SuppressWarnings("unused")
    public TextField getTextField() {
        return textField;
    }

    /**
     * Returns a list of all tags that are currently in the tag area. The list is a copy of the actual list so modifications of the returned list will not influence the tag area
     *
     * @return A list of all tags that are currently in the tag area.
     */
    public List<Tag> getTags() {
        List<Tag> res = new ArrayList<>();

        for (Node node : this.getChildren()) {
            if (node instanceof Tag) {
                res.add((Tag) node);
            }
        }

        return res;
    }

    /**
     * Removes all tags from this tag area
     */
    @SuppressWarnings("unused")
    public void clearAllTags() {
        this.getChildren().removeAll(getTags());
    }

    public void addTag(String tag) {
        addTag(new Tag(tag));
    }

    public void addTag(Tag tag) {
        if (!this.getChildren().contains(tag)) {
            this.getChildren().add(tag);
            textField.toFront();
        }
    }

    public boolean removeTag(Tag tag) {
        return this.getChildren().remove(tag);
    }

    /**
     * A tag in the tagbar.
     *
     * @author SuperGoliath
     */
    public class Tag extends HBox {

        private Label textLabel = new Label();
        private Label iconLabel = new Label(null, new ImageView(new Image(getClass().getResourceAsStream("x.png"))));

        // Constructor
        public Tag(String tag) {
            getStyleClass().add("tag");

            // drag detected
            setOnDragDetected(event -> {

				/* allow copy transfer mode */
                Dragboard db = startDragAndDrop(TransferMode.MOVE);

				/* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString("#c" + getValue());

                db.setDragView(snapshot(null, new WritableImage((int) getWidth(), (int) getHeight())), getWidth() / 2,
                        0);

                db.setContent(content);

                event.consume();
            });

            // drag over
            setOnDragOver((event) -> {
                /*
                 * data is dragged over the target accept it only if it is not
				 * dragged from the same imageView and if it has a string data
				 */
                if (event.getGestureSource() != this && event.getDragboard().hasString())
                    event.acceptTransferModes(TransferMode.MOVE);

                event.consume();
            });

            // drag dropped
            setOnDragDropped(event -> {

                boolean sucess = false;
                if (event.getDragboard().hasString() && event.getDragboard().getString().startsWith("#c")) {
                    String currentTag = getValue();
                    setValue(event.getDragboard().getString().replace("#c", ""));
                    ((Tag) event.getGestureSource()).setValue(currentTag);
                    sucess = true;
                }

                event.setDropCompleted(sucess);
                event.consume();
            });

            // drag done
            setOnDragDone(Event::consume);

            // textLabel
            textLabel.getStyleClass().add("label");
            textLabel.setText(tag);
            // textLabel.setMinWidth(getValue().length() * 6);

            // iconLabel
            iconLabel.setOnMouseReleased(r -> this.getChildren().remove(this));

            getChildren().addAll(textLabel, iconLabel);
        }

        @Override
        public boolean equals(Object obj) {
            //noinspection SimplifiableIfStatement
            if (obj instanceof Tag) {
                return ((Tag) obj).getValue().equalsIgnoreCase(this.getValue());
            } else {
                return false;
            }
        }

        /**
         * Get the current value of the tag
         *
         * @return the current value of the tag
         */
        public String getValue() {
            return textLabel.getText();
        }

        /**
         * Sets the value of this tag
         *
         * @param text The value to set
         */
        public void setValue(String text) {
            textLabel.setText(text);
        }

    }
}
