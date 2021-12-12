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

import java.util.Collection;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author brahim
 */
public class GraphPane extends Pane{
    
    private final double SCALE_DELTA = 1.05;
    public final int MARGIN;
    private final Line line[] = new Line[4];
    private Group contents;
    
    private double startDragX;
    private double startDragY;
    
    
    public GraphPane(int margin) {
        this.MARGIN = margin;
        this.contents = new Group();
        this.setStyle("-fx-background-color: WHITE;");
        for (int i = 0; i < 4; i++) {
            line[i] = new Line();
            line[i].setStrokeWidth(1);
            line[i].setStroke(Color.BLACK);
            line[i].setStrokeLineCap(StrokeLineCap.BUTT);
            line[i].setOpacity(0.2);
        }
        Platform.runLater(() -> {
            this.getChildren().setAll(this.contents);
            this.drawBorder();
        });
        this.setOnScroll((ScrollEvent event) -> {
            event.consume();
            if (event.getDeltaY() == 0) {return;}
            double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
            contents.setScaleX(contents.getScaleX() * scaleFactor);
            contents.setScaleY(contents.getScaleY() * scaleFactor);
        });
        this.layoutBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldBounds, Bounds bounds) -> {
            setClip(new Rectangle(bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight()));
        });
        this.setOnMousePressed((event) -> {
            startDragX = event.getX();
            startDragY = event.getY();
        });
        this.setOnMouseDragged((event) -> {
            event.consume();
            contents.setLayoutX(contents.getLayoutX()-(startDragX-event.getX()));
            contents.setLayoutY(contents.getLayoutY()-(startDragY-event.getY()));
            startDragX = event.getX();
            startDragY = event.getY();
        });
    }

    public void drawGraph(Collection<? extends Node> c) {
        this.contents.getChildren().setAll(c);
        this.drawBorder();
    }
    
    public void addComponents(Collection<? extends Node> c) {
        this.contents.getChildren().addAll(c);
    }
    
    public void removeComponents(Collection<? extends Node> c) {
        this.contents.getChildren().removeAll(c);
    }
    
    public void drawBorder() {
        line[0].setStartX(3);line[0].setStartY(MARGIN);line[0].setEndX(this.getWidth()-3);line[0].setEndY(MARGIN);
        line[1].setStartX(3);line[1].setStartY(this.getHeight()-MARGIN);line[1].setEndX(this.getWidth()-3);line[1].setEndY(this.getHeight()-MARGIN);
        line[2].setStartX(MARGIN);line[2].setStartY(3);line[2].setEndX(MARGIN);line[2].setEndY(this.getHeight()-3);
        line[3].setStartX(this.getWidth()-MARGIN);line[3].setStartY(3);line[3].setEndX(this.getWidth()-MARGIN);line[3].setEndY(this.getHeight()-3);
        ObservableList<Node> lines = FXCollections.observableArrayList();
        lines.addAll(line[0],line[1],line[2],line[3]);
        if ( this.contents.getChildren().containsAll(lines) ) {
            this.contents.getChildren().removeAll(lines);
        }
        this.contents.getChildren().addAll(0, lines);
    }
}
