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

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

/**
 *
 * @author brahim
 */
public class Circle extends javafx.scene.shape.Circle {
    
    private final String id;
    private Info info;
    private boolean isVisible = false;
    
    double x;
    double y;
    
    public Circle(Object id, double d, double d1, double d2, Paint paint, Paint stroke, double width) {
        super(d, d1, d2, paint);
        this.setStroke(stroke);
        this.setStrokeWidth(width);   
        this.id = String.valueOf(id);
        this.setOnMouseClicked((event) -> {
            if (!isVisible) {
                x = d+10;
                y = d1+5;
                double w = ((Pane)((Group)this.getParent()).getParent()).getWidth();
                double h = ((Pane)((Group)this.getParent()).getParent()).getHeight();
                if ( x + 20+this.id.length()*10 > w ) x -= (40+this.id.length()*10);
                if ( y + 25 > h ) y -= 30;
                this.info = new Info(this.id, x, y, 20+this.id.length()*10, 20);
                ((Group)((Circle)event.getSource()).getParent()).getChildren().add(info);
                isVisible = true;
            }
        });
        this.setOnMouseExited((event) -> {
            if (isVisible) {
                ((Group)((Circle)event.getSource()).getParent()).getChildren().remove(info);
                this.info = null;
                isVisible = false;
            }
        });
    }
    
}
