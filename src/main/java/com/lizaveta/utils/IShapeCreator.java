package com.lizaveta.utils;

import com.lizaveta.shapes.BaseShape;
import java.awt.Color;

@FunctionalInterface
public interface IShapeCreator {
    BaseShape create(double x1, double y1, double x2, double y2, int sides, int thickness, Color color, Color borderColor);
} 