package com.lizaveta.utils;

import com.lizaveta.shape_list.ShapeList;
import com.lizaveta.shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPanel extends JPanel {

    private final JComboBox<String> shapeSelector;

    private final JComboBox<Integer> sidesComboBox;

    private final DrawingPanel drawingPanel;

    private final ShapeList shapeList;

    private double startX, startY;

    public ControlPanel(DrawingPanel drawingPanel, ShapeList shapeList) {
        this.drawingPanel = drawingPanel;
        this.shapeList = shapeList;

        setLayout(new FlowLayout());

        shapeSelector = new JComboBox<>(new String[]{
                "Отрезок", "Прямоугольник", "Эллипс", "Многоугольник", "Ломаная"
        });

        Integer[] sides = {3, 4, 5, 6, 7, 8, 9, 10};
        sidesComboBox = new JComboBox<>(sides);
        sidesComboBox.setEnabled(false);

        shapeSelector.addActionListener(e -> {
            boolean isPolygon = "Многоугольник".equals(shapeSelector.getSelectedItem());
            sidesComboBox.setEnabled(isPolygon);
        });

        JButton clearButton = new JButton("Очистить");
        clearButton.addActionListener(e -> drawingPanel.clear());

        add(new JLabel("Фигура:"));
        add(shapeSelector);
        add(new JLabel("Углы:"));
        add(sidesComboBox);
        add(clearButton);

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                double endX = e.getX();
                double endY = e.getY();

                BaseShape shape = createShape(startX, startY, endX, endY);
                if (shape != null) {
                    shapeList.addShape(shape);
                    drawingPanel.clearPreview();
                }
            }
        });

        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double endX = e.getX();
                double endY = e.getY();

                BaseShape previewShape = createShape(startX, startY, endX, endY);
                drawingPanel.setPreviewShape(previewShape);
            }
        });
    }

    private BaseShape createShape(double x1, double y1, double x2, double y2) {
        return switch (shapeSelector.getSelectedItem().toString()) {
            case "Отрезок" -> new LineShape(x1, y1, x2, y2);
            case "Прямоугольник" -> new RectangleShape(x1, y1, x2, y2);
            case "Эллипс" -> new EllipseShape(x1, y1, x2, y2);
            case "Многоугольник" -> new PolygonShape(x1, y1, x2, y2, (int) sidesComboBox.getSelectedItem());
            case "Ломаная" -> new PolylineShape(x1, y1, x2, y2);
            default -> null;
        };
    }
}
