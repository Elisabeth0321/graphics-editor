package com.lizaveta.shapes;

import java.awt.*;

public class EllipseShape extends BaseShape {

    public EllipseShape(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());

        g.fillOval((int) Math.min(getX1(), getX2()), (int) Math.min(getY1(), getY2()),
                (int) Math.abs(getX2() - getX1()), (int) Math.abs(getY2() - getY1()));
    }
}