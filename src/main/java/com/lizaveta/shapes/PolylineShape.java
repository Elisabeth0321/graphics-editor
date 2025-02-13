package com.lizaveta.shapes;

import java.awt.*;

public class PolylineShape extends BaseShape {

    public PolylineShape(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());

        int[] xPoints = {
                (int) getX1(),
                (int) getX2(),
                (int) ((getX1() + getX2()) / 2),
                (int) getX1()
        };

        int[] yPoints = {
                (int) getY1(),
                (int) getY2(),
                (int) (getY1() - 20),
                (int) getY1()
        };

        g.drawPolyline(xPoints, yPoints, xPoints.length);
    }
}