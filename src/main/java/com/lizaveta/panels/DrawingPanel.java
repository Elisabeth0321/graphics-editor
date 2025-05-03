package com.lizaveta.panels;

import com.lizaveta.shape_list.ShapeList;
import com.lizaveta.shapes.BaseShape;

import javax.swing.*;
import java.awt.*;

public class DrawingPanel extends JPanel {

    private final ShapeList shapeList;
    private BaseShape previewShape;

    public DrawingPanel(ShapeList shapeList) {
        this.shapeList = shapeList;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        shapeList.drawAll(g);

        if (previewShape != null) {
            previewShape.draw(g);
        }
    }

    public void clear() {
        shapeList.clear();
        previewShape = null;
        repaint();
    }

    public void setPreviewShape(BaseShape shape) {
        this.previewShape = shape;
        repaint();
    }

    public void clearPreview() {
        this.previewShape = null;
        repaint();
    }

}

