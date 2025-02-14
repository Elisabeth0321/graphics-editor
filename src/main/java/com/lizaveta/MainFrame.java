package com.lizaveta;

import com.lizaveta.shape_list.ShapeList;
import com.lizaveta.utils.ControlPanel;
import com.lizaveta.utils.DrawingPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Graphic Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ShapeList shapeList = new ShapeList();
        DrawingPanel drawingPanel = new DrawingPanel(shapeList);
        ControlPanel controlPanel = new ControlPanel(drawingPanel, shapeList);

        add(controlPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

}
