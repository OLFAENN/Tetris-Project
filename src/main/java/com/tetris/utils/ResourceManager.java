package com.tetris.utils;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static ResourceManager instance;
    private Map<String, BufferedImage> images;
    private Map<String, Clip> sounds;
    
    private ResourceManager() {
        images = new HashMap<>();
        sounds = new HashMap<>();
        loadResources();
    }
    
    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }
    
    private void loadResources() {
        // Charger les images des tétrominos
        loadTetrominoImages();
        // Charger les images d'interface
        loadUIImages();
        // Charger les sons
        loadSounds();
    }
    
    private void loadTetrominoImages() {
        // Créer des images colorées pour chaque type de tétromino
        createTetrominoImage("I", new Color(0, 240, 240));    // Cyan
        createTetrominoImage("O", new Color(240, 240, 0));    // Jaune
        createTetrominoImage("T", new Color(160, 0, 240));    // Magenta
        createTetrominoImage("S", new Color(0, 240, 0));      // Vert
        createTetrominoImage("Z", new Color(240, 0, 0));      // Rouge
        createTetrominoImage("J", new Color(0, 0, 240));      // Bleu
        createTetrominoImage("L", new Color(240, 160, 0));    // Orange
    }
    
    private void createTetrominoImage(String type, Color color) {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fond avec effet de dégradé
        GradientPaint gradient = new GradientPaint(
            0, 0, color.brighter(),
            30, 30, color.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(2, 2, 26, 26, 5, 5);
        
        // Bordure
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(2, 2, 26, 26, 5, 5);
        
        // Effet de brillance
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillRoundRect(4, 4, 8, 8, 3, 3);
        
        g2d.dispose();
        images.put("tetromino_" + type, image);
    }
    
    private void loadUIImages() {
        // Créer des images pour l'interface
        createButtonImage("button_normal", new Color(52, 152, 219));
        createButtonImage("button_hover", new Color(72, 172, 239));
        createButtonImage("button_pressed", new Color(32, 132, 199));
        
        // Image de fond pour le menu
        createBackgroundImage();
    }
    
    private void createButtonImage(String name, Color color) {
        BufferedImage image = new BufferedImage(200, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Fond avec dégradé
        GradientPaint gradient = new GradientPaint(
            0, 0, color.brighter(),
            0, 50, color.darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRoundRect(0, 0, 200, 50, 10, 10);
        
        // Bordure
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(0, 0, 200, 50, 10, 10);
        
        g2d.dispose();
        images.put(name, image);
    }
    
    private void createBackgroundImage() {
        BufferedImage image = new BufferedImage(400, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Dégradé de fond
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(240, 240, 245),
            0, 600, new Color(250, 250, 255)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 400, 600);
        
        // Motif subtil
        g2d.setColor(new Color(245, 245, 250));
        for (int i = 0; i < 400; i += 20) {
            for (int j = 0; j < 600; j += 20) {
                g2d.drawOval(i, j, 2, 2);
            }
        }
        
        g2d.dispose();
        images.put("background", image);
    }
    
    private void loadSounds() {
        // Créer des sons simples programmatiquement
        createSound("move", 440, 100);      // Son de mouvement
        createSound("rotate", 660, 100);    // Son de rotation
        createSound("line_clear", 880, 200); // Son de ligne complétée
        createSound("game_over", 220, 500);  // Son de fin de jeu
        createSound("amazing", 523, 300);    // Son spécial pour 3 lignes (Do majeur)
    }
    
    private void createSound(String name, int frequency, int duration) {
        try {
            // Créer un son simple
            AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
            byte[] data = new byte[duration * 44]; // 44 bytes par milliseconde
            
            if (name.equals("amazing")) {
                // Son spécial "Amazing" avec plusieurs notes
                createAmazingSound(data, format);
            } else {
                // Sons normaux avec une seule fréquence
                for (int i = 0; i < data.length; i += 2) {
                    short sample = (short) (Math.sin(2 * Math.PI * frequency * i / 44100) * 32767);
                    data[i] = (byte) (sample & 0xFF);
                    data[i + 1] = (byte) ((sample >> 8) & 0xFF);
                }
            }
            
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, data.length / 2);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            
            sounds.put(name, clip);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du son " + name + ": " + e.getMessage());
        }
    }
    
    private void createAmazingSound(byte[] data, AudioFormat format) {
        // Créer un son "Amazing" avec une progression de notes
        int sampleRate = 44100;
        int duration = data.length / 44; // durée en millisecondes
        
        // Notes pour "Amazing" (Do, Mi, Sol, Do aigu)
        int[] frequencies = {523, 659, 784, 1047}; // Do4, Mi4, Sol4, Do5
        int noteDuration = duration / frequencies.length;
        
        for (int noteIndex = 0; noteIndex < frequencies.length; noteIndex++) {
            int startSample = noteIndex * noteDuration * 44 / 1000;
            int endSample = (noteIndex + 1) * noteDuration * 44 / 1000;
            int frequency = frequencies[noteIndex];
            
            for (int i = startSample; i < endSample && i < data.length - 1; i += 2) {
                // Effet de fade in/out pour chaque note
                double fadeIn = Math.min(1.0, (i - startSample) / 1000.0);
                double fadeOut = Math.min(1.0, (endSample - i) / 1000.0);
                double volume = Math.min(fadeIn, fadeOut);
                
                short sample = (short) (Math.sin(2 * Math.PI * frequency * i / sampleRate) * 32767 * volume);
                data[i] = (byte) (sample & 0xFF);
                data[i + 1] = (byte) ((sample >> 8) & 0xFF);
            }
        }
    }
    
    public BufferedImage getImage(String name) {
        return images.get(name);
    }
    
    public void playSound(String name) {
        Clip clip = sounds.get(name);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void stopSound(String name) {
        Clip clip = sounds.get(name);
        if (clip != null) {
            clip.stop();
        }
    }
    
    public void stopAllSounds() {
        for (Clip clip : sounds.values()) {
            clip.stop();
        }
    }
} 