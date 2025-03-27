package com.lizaveta.shapes;

import lombok.*;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseShape implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private double x1, y1, x2, y2;
    protected int thickness;
    private Color color = Color.BLACK;
    private Color borderColor = Color.BLACK;

    public abstract void draw(Graphics g);
}