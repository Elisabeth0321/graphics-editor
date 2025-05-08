package com.lizaveta.plugin;

import com.lizaveta.utils.IShapeCreator;

import java.util.Map;

public interface IShapePlugin {
    Map<String, IShapeCreator> getShapes();
}