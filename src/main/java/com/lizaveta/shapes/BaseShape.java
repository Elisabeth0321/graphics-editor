package com.lizaveta.shapes;

import java.awt.*;

public abstract class BaseShape {

    private double x1, y1, x2, y2;
    private Color color = Color.BLACK;

    public BaseShape(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public abstract void draw(Graphics g);
}