package com.lizaveta.utils;

import com.lizaveta.shape_list.ShapeList;
import com.lizaveta.shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.function.BiFunction;

public class ControlPanel extends JPanel {

    final JComboBox<String> shapeSelector;
    private final JComboBox<Integer> sidesComboBox;
    private final JSpinner lineThicknessSpinner;
    private final JButton undoButton, redoButton, clearButton, colorButton, borderColorButton;

    private final UndoRedoManager undoRedoManager;
    private double startX, startY;
    private Color selectedColor = Color.BLACK;
    private Color selectedBorderColor = Color.BLACK;
    private PolylineShape currentPolyline = null;

    final HashMap<String, BiFunction<Double, Double, BaseShape>> shapeFactory;

    public ControlPanel(DrawingPanel drawingPanel, ShapeList shapeList, UndoRedoManager undoRedoManager) {
        this.undoRedoManager = undoRedoManager;
        setLayout(new FlowLayout());

        shapeSelector = new JComboBox<>(new String[]{
                "Отрезок", "Прямоугольник", "Эллипс", "Многоугольник", "Ломаная"
        });

        Integer[] sides = {3, 4, 5, 6, 7, 8, 9, 10};
        sidesComboBox = new JComboBox<>(sides);
        sidesComboBox.setEnabled(false);

        lineThicknessSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        shapeSelector.addActionListener(e -> {
            sidesComboBox.setEnabled("Многоугольник".equals(shapeSelector.getSelectedItem().toString()));
        });

        colorButton = new JButton("Выбрать цвет заливки");
        colorButton.addActionListener(e -> {
            selectedColor = JColorChooser.showDialog(this, "Выберите цвет", selectedColor);
            colorButton.setBackground(selectedColor);
        });

        borderColorButton = new JButton("Выбрать цвет границы");
        borderColorButton.addActionListener(e -> {
            selectedBorderColor = JColorChooser.showDialog(this, "Выберите цвет", selectedBorderColor);
            borderColorButton.setBackground(selectedBorderColor);
        });

        clearButton = new JButton("Очистить");
        undoButton = new JButton(new ImageIcon(getClass().getResource("/images/undo.png")));
        redoButton = new JButton(new ImageIcon(getClass().getResource("/images/redo.png")));

        clearButton.addActionListener(e -> {
            drawingPanel.clear();
            currentPolyline = null;

            undoRedoManager.clear();
            updateButtonsState();
        });

        undoButton.addActionListener(e -> {
            undoRedoManager.undo();
            updateButtonsState();
            drawingPanel.repaint();
        });

        redoButton.addActionListener(e -> {
            undoRedoManager.redo();
            updateButtonsState();
            drawingPanel.repaint();
        });

        add(undoButton);
        add(redoButton);
        add(new JLabel("Фигура:"));
        add(shapeSelector);
        add(new JLabel("Углы:"));
        add(sidesComboBox);
        add(new JLabel("Толщина линии:"));
        add(lineThicknessSpinner);
        add(colorButton);
        add(borderColorButton);
        add(clearButton);

        updateButtonsState();

        shapeFactory = new HashMap<>();
        shapeFactory.put("Отрезок", (x2, y2) ->
                new LineShape(startX, startY, x2, y2, (int) lineThicknessSpinner.getValue(), selectedColor));

        shapeFactory.put("Прямоугольник", (x2, y2) ->
                new RectangleShape(startX, startY, x2, y2, (int) lineThicknessSpinner.getValue(), selectedColor, selectedBorderColor));

        shapeFactory.put("Эллипс", (x2, y2) ->
                new EllipseShape(startX, startY, x2, y2, (int) lineThicknessSpinner.getValue(), selectedColor, selectedBorderColor));

        shapeFactory.put("Многоугольник", (x2, y2) ->
                new PolygonShape(startX, startY, x2, y2, (int) sidesComboBox.getSelectedItem(),
                        (int) lineThicknessSpinner.getValue(), selectedColor, selectedBorderColor));

        shapeFactory.put("Ломаная", (x2, y2) ->
                new PolylineShape(x2, y2, (int) lineThicknessSpinner.getValue(), selectedColor));

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if ("Ломаная".equals(shapeSelector.getSelectedItem().toString())) {
                    int thickness = (int) lineThicknessSpinner.getValue();

                    if (currentPolyline == null) {
                        currentPolyline = new PolylineShape(e.getX(), e.getY(), thickness, selectedColor);
                        currentPolyline.setColor(selectedColor);
                        undoRedoManager.addShape(currentPolyline);
                        updateButtonsState();
                    } else {
                        currentPolyline.addPoint(e.getX(), e.getY());
                    }
                    drawingPanel.repaint();
                } else {
                    startX = e.getX();
                    startY = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!"Ломаная".equals(shapeSelector.getSelectedItem().toString())) {
                    double endX = e.getX();
                    double endY = e.getY();
                    BaseShape shape = createShape(startX, startY, endX, endY);

                    shape.setColor(selectedColor);
                    undoRedoManager.addShape(shape);
                    updateButtonsState();
                    drawingPanel.clearPreview();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if ("Ломаная".equals(shapeSelector.getSelectedItem().toString()) && e.getClickCount() == 2) {
                    currentPolyline = null;
                }
            }
        });

        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double endX = e.getX();
                double endY = e.getY();

                if ("Ломаная".equals(shapeSelector.getSelectedItem().toString()) && currentPolyline != null) {
                    PolylineShape previewPolyline = new PolylineShape(currentPolyline);
                    previewPolyline.addPoint(endX, endY);
                    drawingPanel.setPreviewShape(previewPolyline);
                } else {
                    BaseShape previewShape = createShape(startX, startY, endX, endY);
                    previewShape.setColor(selectedColor);
                    drawingPanel.setPreviewShape(previewShape);
                }
                drawingPanel.repaint();
            }
        });
    }

    private BaseShape createShape(double x1, double y1, double x2, double y2) {
        return shapeFactory.getOrDefault(shapeSelector.getSelectedItem().toString(), (a, b) -> null).apply(x2, y2);
    }

    void updateButtonsState() {
        undoButton.setEnabled(!undoRedoManager.shapeList.isEmpty());
        redoButton.setEnabled(!undoRedoManager.undoStack.isEmpty());
        clearButton.setEnabled(!(undoRedoManager.shapeList.isEmpty() && undoRedoManager.undoStack.isEmpty()));
    }

}