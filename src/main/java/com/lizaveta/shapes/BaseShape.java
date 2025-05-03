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

    protected double x1, y1, x2, y2;
    protected int sides, thickness;
    protected Color color, borderColor;

    public abstract void draw(Graphics g);
}