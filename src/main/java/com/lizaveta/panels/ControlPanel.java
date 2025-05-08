package com.lizaveta.panels;

import com.lizaveta.shapes.*;
import com.lizaveta.utils.IShapeCreator;
import com.lizaveta.utils.UndoRedoManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Objects;

public class ControlPanel extends JPanel {

    public final JComboBox<String> shapeSelector;
    private final JComboBox<Integer> sidesComboBox;
    private final JSpinner lineThicknessSpinner;
    private final JButton undoButton, redoButton, clearButton, colorButton, borderColorButton;

    private final UndoRedoManager undoRedoManager;

    private double startX, startY;
    private Color selectedColor = Color.BLACK;
    private Color selectedBorderColor = Color.BLACK;
    private PolylineShape currentPolyline = null;

    public final HashMap<String, IShapeCreator> shapeFactory;

    public ControlPanel(DrawingPanel drawingPanel, UndoRedoManager undoRedoManager) {
        this.undoRedoManager = undoRedoManager;
        setLayout(new FlowLayout());

        shapeSelector = new JComboBox<>(new String[]{
                "Отрезок", "Прямоугольник", "Эллипс", "Многоугольник", "Ломаная"
        });

        Integer[] sides = {3, 4, 5, 6, 7, 8, 9, 10};
        sidesComboBox = new JComboBox<>(sides);
        lineThicknessSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        
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
        undoButton = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/undo.png"))));
        redoButton = new JButton(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/redo.png"))));

        clearButton.addActionListener(e -> {
            currentPolyline = null;

            undoRedoManager.clearList();
            undoRedoManager.clearUndoStack();

            drawingPanel.clear();
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
        shapeFactory.put("Отрезок", (x1, y1, x2, y2, sidesParam, thickness, color, borderColor) ->
                new LineShape(x1, y1, x2, y2, thickness, color));

        shapeFactory.put("Прямоугольник", (x1, y1, x2, y2, sidesParam, thickness, color, borderColor) ->
                new RectangleShape(x1, y1, x2, y2, thickness, color, borderColor));

        shapeFactory.put("Эллипс", (x1, y1, x2, y2, sidesParam, thickness, color, borderColor) ->
                new EllipseShape(x1, y1, x2, y2, thickness, color, borderColor));

        shapeFactory.put("Многоугольник", (x1, y1, x2, y2, sidesParam, thickness, color, borderColor) ->
                new PolygonShape(x1, y1, x2, y2, sidesParam, thickness, color, borderColor));

        shapeFactory.put("Ломаная", (x1, y1, x2, y2, sidesParam, thickness, color, borderColor) ->
                new PolylineShape(x2, y2, thickness, color));

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if ("Ломаная".equals(Objects.requireNonNull(shapeSelector.getSelectedItem()).toString())) {
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
                if (!"Ломаная".equals(Objects.requireNonNull(shapeSelector.getSelectedItem()).toString())) {
                    double endX = e.getX();
                    double endY = e.getY();
                    BaseShape shape = createShape(endX, endY);

                    shape.setColor(selectedColor);
                    undoRedoManager.addShape(shape);
                    updateButtonsState();
                    drawingPanel.clearPreview();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if ("Ломаная".equals(Objects.requireNonNull(shapeSelector.getSelectedItem()).toString())
                        && e.getClickCount() == 2) {
                    currentPolyline = null;
                }
            }
        });

        drawingPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double endX = e.getX();
                double endY = e.getY();

                if ("Ломаная".equals(Objects.requireNonNull(shapeSelector.getSelectedItem()).toString())
                        && currentPolyline != null) {
                    PolylineShape previewPolyline = new PolylineShape(currentPolyline);
                    previewPolyline.addPoint(endX, endY);
                    drawingPanel.setPreviewShape(previewPolyline);
                } else {
                    BaseShape previewShape = createShape(endX, endY);
                    previewShape.setColor(selectedColor);
                    drawingPanel.setPreviewShape(previewShape);
                }
                drawingPanel.repaint();
            }
        });
    }

    private BaseShape createShape(double x2, double y2) {
        undoRedoManager.clearUndoStack();
        double x1 = startX;
        double y1 = startY;
        int sides = (sidesComboBox.getSelectedItem() != null) ? (int) sidesComboBox.getSelectedItem() : 3;
        int thickness = (int) lineThicknessSpinner.getValue();
        Color color = selectedColor;
        Color borderColor = selectedBorderColor;
        return shapeFactory.getOrDefault(Objects.requireNonNull(shapeSelector.getSelectedItem()).toString(),
                (a, b, c, d, e, f, g, h) -> null)
                .create(x1, y1, x2, y2, sides, thickness, color, borderColor);
    }

    public void updateButtonsState() {
        undoButton.setEnabled(!undoRedoManager.shapeList.isEmpty());
        redoButton.setEnabled(!undoRedoManager.undoStack.isEmpty());
        clearButton.setEnabled(!(undoRedoManager.shapeList.isEmpty() && undoRedoManager.undoStack.isEmpty()));
    }

}