package com.lizaveta;

import com.lizaveta.panels.ControlPanel;
import com.lizaveta.panels.DrawingPanel;
import com.lizaveta.shape_list.ShapeList;
import com.lizaveta.utils.*;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Рисовалка");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(Objects.requireNonNull(MainFrame.class.getResource("/images/main_icon.png"))).getImage());
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        ShapeList shapeList = new ShapeList();
        DrawingPanel drawingPanel = new DrawingPanel(shapeList);
        UndoRedoManager undoRedoManager = new UndoRedoManager(shapeList);
        ControlPanel controlPanel = new ControlPanel(drawingPanel, undoRedoManager);
        FileManager fileManager = new FileManager(this, shapeList, drawingPanel, controlPanel);
        PluginLoader pluginLoader = new PluginLoader(this, controlPanel);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileManager.createFileMenu());
        menuBar.add(pluginLoader.createPluginMenu());

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
