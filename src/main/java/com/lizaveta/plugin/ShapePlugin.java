package com.lizaveta.plugin;

import com.lizaveta.shapes.BaseShape;

import java.util.Map;
import java.util.function.BiFunction;

public interface ShapePlugin {
    Map<String, BiFunction<Double, Double, BaseShape>> getShapes();
}