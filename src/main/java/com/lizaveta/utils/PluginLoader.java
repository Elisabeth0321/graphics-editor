package com.lizaveta.utils;

import com.lizaveta.panels.ControlPanel;
import com.lizaveta.plugin.ShapePlugin;
import lombok.Getter;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import javax.swing.*;

public class PluginLoader {
    @Getter
    private final HashMap<String, ShapeCreator> shapeFactory = new HashMap<>();

    private final JFrame parent;
    private final ControlPanel controlPanel;

    public PluginLoader(JFrame parent, ControlPanel controlPanel) {
        this.parent = parent;
        this.controlPanel = controlPanel;
    }

    public void loadPlugin() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите JAR-плагин");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JAR файлы (*.jar)", "jar"));
        fileChooser.setCurrentDirectory(new File("D:\\WORK\\4 СЕМЕСТР\\ООТПиСП"));

        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                URL jarURL = file.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL}, getClass().getClassLoader());

                java.util.jar.JarFile jarFile = new java.util.jar.JarFile(file);
                java.util.Enumeration<java.util.jar.JarEntry> entries = jarFile.entries();
                boolean found = false;
                while (entries.hasMoreElements()) {
                    java.util.jar.JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.endsWith(".class")) {
                        String className = name.replace('/', '.').substring(0, name.length() - 6);
                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            if (ShapePlugin.class.isAssignableFrom(clazz)) {
                                ShapePlugin plugin = (ShapePlugin) clazz.getDeclaredConstructor().newInstance();
                                var shapes = plugin.getShapes();
                                for (var entryShape : shapes.entrySet()) {
                                    String shapeName = entryShape.getKey();
                                    String pluginName = file.getName().replace(".jar", "");
                                    ShapeCreator factory = entryShape.getValue();
                                    controlPanel.shapeFactory.put(shapeName, factory);
                                    controlPanel.shapeSelector.addItem(shapeName);
                                }
                                found = true;
                            }
                        } catch (Throwable t) {
                            // Пропускаем классы, которые не ShapePlugin
                        }
                    }
                }
                jarFile.close();
                if (found) {
                    JOptionPane.showMessageDialog(parent, "Плагин(ы) успешно загружен(ы)!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(parent, "Не найдено ни одного ShapePlugin!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Ошибка загрузки плагина!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public JMenuItem createPluginMenu() {
        JMenuItem pluginItem = new JMenuItem("Загрузить плагин...");
        pluginItem.addActionListener(e -> {
            loadPlugin();
        });
        return pluginItem;
    }
}