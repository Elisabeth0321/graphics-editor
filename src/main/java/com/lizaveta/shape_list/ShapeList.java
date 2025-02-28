package com.lizaveta.shape_list;

import com.lizaveta.shapes.BaseShape;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ShapeList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
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

    public boolean isEmpty() {
        return shapes.isEmpty();
    }

    public BaseShape removeLast() {
        return shapes.remove(shapes.size() - 1);
    }

}
