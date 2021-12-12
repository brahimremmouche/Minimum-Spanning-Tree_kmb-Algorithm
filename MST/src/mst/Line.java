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

import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;

/**
 *
 * @author brahim
 */
public class Line extends javafx.scene.shape.Line {

    public Line(double d, double d1, double d2, double d3, Paint stroke, StrokeLineCap strokeLineCap, double width) {
        super(d, d1, d2, d3);
        this.setStroke(stroke);
        this.setStrokeLineCap(strokeLineCap);
        this.setStrokeWidth(width);
    }
    
}
