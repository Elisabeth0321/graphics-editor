package com.lizaveta;

import com.lizaveta.shape_list.ShapeList;
import com.lizaveta.utils.ControlPanel;
import com.lizaveta.utils.DrawingPanel;
import com.lizaveta.utils.FileManager;
import com.lizaveta.utils.UndoRedoManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Рисовалка");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(MainFrame.class.getResource("/images/main_icon.png")).getImage());
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ShapeList shapeList = new ShapeList();
        DrawingPanel drawingPanel = new DrawingPanel(shapeList);
        UndoRedoManager undoRedoManager = new UndoRedoManager(shapeList);
        ControlPanel controlPanel = new ControlPanel(drawingPanel, shapeList, undoRedoManager);
        FileManager fileManager = new FileManager(this, shapeList, drawingPanel, controlPanel);

        setJMenuBar(fileManager.createMenuBar());
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
