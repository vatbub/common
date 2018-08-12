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


import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.Collection;

/**
 * A normal javafx group that is able to get a child by its coordinates
 */
public class CustomGroup extends Group {
    public CustomGroup() {
        super();
    }

    public CustomGroup(Collection<Node> children) {
        super(children);
    }

    public CustomGroup(Node... children) {
        super(children);
    }

    /**
     * Gets the rectangle at the specified coordinates if there is one. The coordinates may even point in the middle of a rectangle to be found. Searches from the background to the foreground which means that rectangles behind other rectangles will be preferred.
     * <br>
     * <b>Only finds Rectangles, no other shapes</b>
     *
     * @param x The x-coordinate of the point to look for.
     * @param y The y-coordinate of the point to look for.
     * @return The rectangle that was found or {@code null} if none was found.
     */
    public Rectangle getRectangleByCoordinatesPreferBack(double x, double y) {
        for (Node node : this.getChildren()) {
            if (node instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) node;
                if (x >= rectangle.getX() && x <= (rectangle.getX() + rectangle.getWidth()) && y >= rectangle.getY() && y <= (rectangle.getY() + rectangle.getHeight())) {
                    // region is correct
                    return rectangle;
                }
            }
        }

        // nothing found, return null
        return null;
    }

    /**
     * Gets the rectangle at the specified coordinates if there is one. The coordinates may even point in the middle of a rectangle to be found. Searches from the foreground to the background which means that rectangles in front of other rectangles will be preferred.
     * <br>
     * <b>Only finds Rectangles, no other shapes</b>
     *
     * @param x The x-coordinate of the point to look for.
     * @param y The y-coordinate of the point to look for.
     * @return The rectangle that was found or {@code null} if none was found.
     */
    public Rectangle getRectangleByCoordinatesPreferFront(double x, double y) {
        for (int i = this.getChildren().size() - 1; i >= 0; i--) {
            Node node = this.getChildren().get(i);
            if (node instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) node;
                if (x >= rectangle.getX() && x <= (rectangle.getX() + rectangle.getWidth()) && y >= rectangle.getY() && y <= (rectangle.getY() + rectangle.getHeight())) {
                    // region is correct
                    return rectangle;
                }
            }
        }

        // nothing found, return null
        return null;
    }
}
