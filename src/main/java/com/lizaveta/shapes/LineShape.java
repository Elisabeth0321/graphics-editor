package com.lizaveta.shapes;

import java.awt.*;

public class LineShape extends BaseShape {

    public LineShape(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());

        g.drawLine((int) getX1(), (int) getY1(), (int) getX2(), (int) getY2());
    }
}