package com.lizaveta.shapes;

import java.awt.*;
import java.io.Serial;

public class RectangleShape extends BaseShape {

    @Serial
    private static final long serialVersionUID = 1L;

    public RectangleShape(double x1, double y1, double x2, double y2, int thickness, Color color) {
        super(x1, y1, x2, y2, thickness, color);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(getColor());

        int x = (int) Math.min(getX1(), getX2());
        int y = (int) Math.min(getY1(), getY2());
        int width = (int) Math.abs(getX2() - getX1());
        int height = (int) Math.abs(getY2() - getY1());

        g2d.setColor(getColor());
        g2d.fillRect(x, y, width, height);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRect(x, y, width, height);
    }
}