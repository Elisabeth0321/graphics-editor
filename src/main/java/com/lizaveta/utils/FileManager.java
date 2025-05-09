package com.lizaveta.utils;

import com.lizaveta.panels.ControlPanel;
import com.lizaveta.panels.DrawingPanel;
import com.lizaveta.shape_list.ShapeList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class FileManager {

    private final JFrame parent;
    private final ShapeList shapeList;
    private final DrawingPanel drawingPanel;
    private final ControlPanel controlPanel;

    public FileManager(JFrame parent, ShapeList shapeList, DrawingPanel drawingPanel, ControlPanel controlPanel) {
        this.parent = parent;
        this.shapeList = shapeList;
        this.drawingPanel = drawingPanel;
        this.controlPanel = controlPanel;
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Открыть файл");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Файлы данных (*.dat)", "dat"));

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    try {
                        ClassLoader pluginCL = PluginLoader.getPluginClassLoader();
                        if (pluginCL != null) {
                            return Class.forName(desc.getName(), false, pluginCL);
                        }
                    } catch (ClassNotFoundException e) {
                    }
                    return super.resolveClass(desc);
                }
            }) {
                ShapeList loadedShapes = (ShapeList) ois.readObject();
                shapeList.getShapes().clear();
                shapeList.getShapes().addAll(loadedShapes.getShapes());
                drawingPanel.repaint();
                controlPanel.updateButtonsState();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Ошибка при открытии файла!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить файл");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Файлы данных (*.dat)", "dat"));

        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!file.getName().endsWith(".dat")) {
                file = new File(file.getAbsolutePath() + ".dat");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(shapeList);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Ошибка при сохранении файла!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(parent, "Вы действительно хотите выйти?", "Выход", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Файл");

        JMenuItem openItem = new JMenuItem("Открыть");
        openItem.addActionListener(e -> openFile());

        JMenuItem saveItem = new JMenuItem("Сохранить");
        saveItem.addActionListener(e -> saveFile());

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(e -> exitApplication());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        return fileMenu;
    }
}