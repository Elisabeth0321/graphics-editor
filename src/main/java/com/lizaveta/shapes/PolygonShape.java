package com.lizaveta.shapes;

import java.awt.*;
import java.io.Serial;

public class PolygonShape extends BaseShape {

    @Serial
    private static final long serialVersionUID = 1L;

    private int sides;

    public PolygonShape(double x1, double y1, double x2, double y2, int sides, int thickness, Color color, Color borderColor) {
        super(x1, y1, x2, y2, thickness, color, borderColor);
        this.sides = Math.max(3, Math.min(sides, 10));
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int centerX = (int) ((getX1() + getX2()) / 2);
        int centerY = (int) ((getY1() + getY2()) / 2);
        int radius = (int) (Math.min(Math.abs(getX2() - getX1()), Math.abs(getY2() - getY1())) / 2);

        int[] polyX = new int[sides];
        int[] polyY = new int[sides];

        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            polyX[i] = centerX + (int) (radius * Math.cos(angle));
            polyY[i] = centerY + (int) (radius * Math.sin(angle));
        }

        g2d.setColor(getColor());
        g2d.fillPolygon(polyX, polyY, sides);

        g2d.setColor(getBorderColor());
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawPolygon(polyX, polyY, sides);
    }

}