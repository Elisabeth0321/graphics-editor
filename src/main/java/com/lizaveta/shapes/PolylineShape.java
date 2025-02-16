package com.lizaveta.shapes;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PolylineShape extends BaseShape {

    private final List<Point> points = new ArrayList<>();

    public PolylineShape(double x1, double y1) {
        super(x1, y1, x1, y1);
        addPoint(x1, y1);
    }

    public void addPoint(double x, double y) {
        points.add(new Point((int) x, (int) y));
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());

        if (points.size() > 1) {
            int[] xPoints = points.stream().mapToInt(p -> p.x).toArray();
            int[] yPoints = points.stream().mapToInt(p -> p.y).toArray();
            g.drawPolyline(xPoints, yPoints, points.size());
        }
    }
}

