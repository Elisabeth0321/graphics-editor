package com.lizaveta.shapes;

import java.awt.*;
import java.io.Serial;

public class LineShape extends BaseShape {

    @Serial
    private static final long serialVersionUID = 1L;

    public LineShape(double x1, double y1, double x2, double y2, int thickness, Color color) {
        super(x1, y1, x2, y2, 0, thickness, color, null);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        g2.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }
}