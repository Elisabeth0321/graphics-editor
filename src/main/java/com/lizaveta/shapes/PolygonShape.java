package com.lizaveta.shapes;

import java.awt.*;

public class PolygonShape extends BaseShape {
    private int sides;

    public PolygonShape(double x1, double y1, double x2, double y2, int sides) {
        super(x1, y1, x2, y2);
        this.sides = Math.max(3, Math.min(sides, 10)); // Ограничиваем от 3 до 10
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());

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

        g.fillPolygon(polyX, polyY, sides);
    }

    public void setSides(int sides) {
        this.sides = Math.max(3, Math.min(sides, 10));
    }
}