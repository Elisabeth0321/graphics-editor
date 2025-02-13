package com.lizaveta.shapes;

import java.awt.*;

public class PolygonShape extends BaseShape {

    public PolygonShape(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());

        int[] polyX = {(int) getX1(), (int) getX2(), (int) ((getX1() + getX2()) / 2)};
        int[] polyY = {(int) getY2(), (int) getY2(), (int) getY1()};

        g.fillPolygon(polyX, polyY, polyX.length);
    }
}
