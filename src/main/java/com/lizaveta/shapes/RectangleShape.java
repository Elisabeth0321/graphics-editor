package com.lizaveta.shapes;

import java.awt.*;
import java.io.Serial;

public class RectangleShape extends BaseShape {

    @Serial
    private static final long serialVersionUID = 1L;

    public RectangleShape(double x1, double y1, double x2, double y2, int thickness, Color color, Color borderColor) {
        super(x1, y1, x2, y2, 0, thickness, color, borderColor);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int x = (int) Math.min(x1, x2);
        int y = (int) Math.min(y1, y2);
        int width = (int) Math.abs(x2 - x1);
        int height = (int) Math.abs(y2 - y1);

        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);

        g2d.setColor(borderColor);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRect(x, y, width, height);
    }
}