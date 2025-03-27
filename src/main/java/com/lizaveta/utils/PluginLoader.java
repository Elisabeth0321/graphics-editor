package com.lizaveta.utils;

import com.lizaveta.shapes.BaseShape;
import lombok.Getter;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.function.BiFunction;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

public class PluginLoader {
    @Getter
    private final HashMap<String, BiFunction<Double, Double, BaseShape>> shapeFactory = new HashMap<>();

    private final JFrame parent;
    private final ControlPanel controlPanel;

    public PluginLoader(JFrame parent, ControlPanel controlPanel) {
        this.parent = parent;
        this.controlPanel = controlPanel;
    }

    public String loadPlugin() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите JAR-плагин");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JAR файлы (*.jar)", "jar"));

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                URL jarURL = file.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL}, getClass().getClassLoader());

                // Допустим, в JAR есть класс "CustomShape"
                String className = "com.plugin.shapes.CustomShape";
                Class<?> shapeClass = classLoader.loadClass(className);

                if (BaseShape.class.isAssignableFrom(shapeClass)) {
                    Constructor<?> constructor = shapeClass.getConstructor(double.class, double.class, double.class, double.class, int.class, Color.class);
                    shapeFactory.put(file.getName().replace(".jar", ""), (x2, y2) -> {
                        try {
                            return (BaseShape) constructor.newInstance(0, 0, x2, y2, 2, Color.RED);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });

                    JOptionPane.showMessageDialog(parent, "Плагин успешно загружен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                    return file.getName().replace(".jar", "");
                } else {
                    JOptionPane.showMessageDialog(parent, "Ошибка: Класс не является фигурой!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Ошибка загрузки плагина!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    public JMenuItem createPlaginMenu() {
        JMenuItem pluginItem = new JMenuItem("Загрузить плагин...");
        pluginItem.addActionListener(e -> {
            String pluginName = loadPlugin();
            if (pluginName != null) {
                controlPanel.shapeFactory.putAll(getShapeFactory());
                controlPanel.shapeSelector.addItem(pluginName);
            }
        });
        return pluginItem;
    }

//    public JMenu createPlaginMenu() {
//        JMenu pluginMenu = new JMenu("Загрузить плагин...");
//        pluginMenu.addActionListener(e -> {
//            String pluginName = loadPlugin();
//            if (pluginName != null) {
//                controlPanel.shapeFactory.putAll(getShapeFactory());
//                controlPanel.shapeSelector.addItem(pluginName);
//            }
//        });
//        return pluginMenu;
//    }

}