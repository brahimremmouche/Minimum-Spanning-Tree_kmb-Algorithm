/*
 * Copyright (C) 2019 dell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mst;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author brahim
 */
public class Info extends StackPane {

    public Info(String text, double x, double y, int width, int height) {
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setOpacity(0.9);
        Rectangle r = new Rectangle(width, height);
        r.setFill(Color.BISQUE);
        r.setStroke(Color.BROWN);
        r.setStrokeWidth(2);
        r.setStyle("-fx-arc-width: 15; -fx-arc-height: 15;");
        Text t = new Text(text);
        this.getChildren().addAll(r,t);
    }

    
}
