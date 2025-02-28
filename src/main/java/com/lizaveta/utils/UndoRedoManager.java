package com.lizaveta.utils;

import com.lizaveta.shape_list.ShapeList;
import com.lizaveta.shapes.BaseShape;

import javax.swing.*;
import java.util.Stack;

public class UndoRedoManager {
    final ShapeList shapeList;
    final Stack<BaseShape> undoStack = new Stack<>();

    public UndoRedoManager(ShapeList shapeList) {
        this.shapeList = shapeList;
    }

    public void addShape(BaseShape shape) {
        shapeList.addShape(shape);
    }

    public void undo() {
        if (!shapeList.isEmpty()) {
            BaseShape lastShape = shapeList.removeLast();
            undoStack.push(lastShape);
        }
    }

    public void redo() {
        if (!undoStack.isEmpty()) {
            BaseShape shape = undoStack.pop();
            shapeList.addShape(shape);
        }
    }

    public void clear() {
        shapeList.clear();
        undoStack.clear();
    }
}