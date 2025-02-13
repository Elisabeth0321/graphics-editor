package com.lizaveta;

import com.lizaveta.shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

    private final ShapeList shapeList = new ShapeList();
    private double startX, startY;

    public MainFrame() {
        setTitle("Graphic Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DrawingPanel drawingPanel = new DrawingPanel();
        drawingPanel.setBackground(Color.WHITE);

        JComboBox<String> shapeSelector = new JComboBox<>(new String[]{
                "Отрезок", "Прямоугольник", "Эллипс", "Многоугольник", "Ломаная"
        });

        JButton clearButton = new JButton("Очистить");
        clearButton.addActionListener(e -> {
            drawingPanel.clear();
        });

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
                BaseShape shape = switch (shapeSelector.getSelectedItem().toString()) {
                    case "Отрезок" -> new LineShape(startX, startY, endX, endY);
                    case "Прямоугольник" -> new RectangleShape(startX, startY, endX, endY);
                    case "Эллипс" -> new EllipseShape(startX, startY, endX, endY);
                    case "Многоугольник" -> new PolygonShape(startX, startY, endX, endY);
                    case "Ломаная" -> new PolylineShape(startX, startY, endX, endY);
                    default -> null;
                };
                if (shape != null) {
                    shapeList.addShape(shape);
                    drawingPanel.repaint();
                }
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(shapeSelector);
        controlPanel.add(clearButton);

        add(controlPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            shapeList.drawAll(g);
        }

        public void clear() {
            shapeList.clear();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}