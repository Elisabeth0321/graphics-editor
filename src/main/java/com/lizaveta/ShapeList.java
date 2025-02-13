package com.lizaveta;

import com.lizaveta.shapes.BaseShape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeList {

    private final List<BaseShape> shapes = new ArrayList<>();

    public void addShape(BaseShape shape) {
        shapes.add(shape);
    }

    public void drawAll(Graphics g) {
        for (BaseShape shape : shapes) {
            shape.draw(g);
        }
    }

    public void clear() {
        shapes.clear();
    }
}
