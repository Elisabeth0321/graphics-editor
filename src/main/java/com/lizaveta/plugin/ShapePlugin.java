package com.lizaveta.plugin;

import com.lizaveta.utils.ShapeCreator;

import java.util.Map;

public interface ShapePlugin {
    Map<String, ShapeCreator> getShapes();
}