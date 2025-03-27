package com.lizaveta.shapes;

import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class PolylineShape extends BaseShape {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Point> points = new ArrayList<>();

    public PolylineShape(double x1, double y1, int thickness, Color color) {
        super(x1, y1, x1, y1, thickness, color, null);
        addPoint(x1, y1);
    }

    public PolylineShape(PolylineShape other) {
        super(other.getX1(), other.getY1(), other.getX1(), other.getY1(), other.thickness, other.getColor(), null);
        this.points.addAll(other.points);
    }

    public void addPoint(double x, double y) {
        points.add(new Point((int) x, (int) y));
    }

    @Override
    public void draw(Graphics g) {
        if (points.size() > 1) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(getColor());
            g2.setStroke(new BasicStroke(thickness));

            int[] xPoints = points.stream().mapToInt(p -> p.x).toArray();
            int[] yPoints = points.stream().mapToInt(p -> p.y).toArray();
            g2.drawPolyline(xPoints, yPoints, points.size());
        }
    }
}