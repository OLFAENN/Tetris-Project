package com.tetris.utils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
    private static AnimationManager instance;
    private List<Animation> animations;
    
    private AnimationManager() {
        animations = new ArrayList<>();
    }
    
    public static AnimationManager getInstance() {
        if (instance == null) {
            instance = new AnimationManager();
        }
        return instance;
    }
    
    public void addAnimation(Animation animation) {
        animations.add(animation);
    }
    
    public void update() {
        animations.removeIf(Animation::isFinished);
        for (Animation animation : animations) {
            animation.update();
        }
    }
    
    public void render(Graphics2D g2d) {
        for (Animation animation : animations) {
            animation.render(g2d);
        }
    }
    
    // Animation de ligne qui disparaît
    public void createLineClearAnimation(int row, int col, int width) {
        for (int i = 0; i < width; i++) {
            LineClearParticle particle = new LineClearParticle(
                col + i, row, 
                (Math.random() - 0.5) * 4, 
                -Math.random() * 3 - 1,
                30 + (int)(Math.random() * 30)
            );
            addAnimation(particle);
        }
    }
    
    // Animation de score
    public void createScoreAnimation(int x, int y, int score) {
        ScoreAnimation scoreAnim = new ScoreAnimation(x, y, score);
        addAnimation(scoreAnim);
    }
    
    // Animation de niveau
    public void createLevelUpAnimation() {
        LevelUpAnimation levelAnim = new LevelUpAnimation();
        addAnimation(levelAnim);
    }
    
    // Classe de base pour les animations
    public static abstract class Animation {
        protected int duration;
        protected int currentTime;
        protected boolean finished;
        
        public Animation(int duration) {
            this.duration = duration;
            this.currentTime = 0;
            this.finished = false;
        }
        
        public void update() {
            currentTime++;
            if (currentTime >= duration) {
                finished = true;
            }
        }
        
        public abstract void render(Graphics2D g2d);
        
        public boolean isFinished() {
            return finished;
        }
        
        protected float getProgress() {
            return (float) currentTime / duration;
        }
    }
    
    // Particule pour l'animation de ligne
    public static class LineClearParticle extends Animation {
        private double x, y;
        private double vx, vy;
        private Color color;
        private int size;
        
        public LineClearParticle(double x, double y, double vx, double vy, int duration) {
            super(duration);
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = new Color(255, 255, 255, 255);
            this.size = 4;
        }
        
        @Override
        public void update() {
            super.update();
            x += vx;
            y += vy;
            vy += 0.1; // Gravité
            
            // Fade out
            int alpha = (int)(255 * (1 - getProgress()));
            color = new Color(255, 255, 255, alpha);
        }
        
        @Override
        public void render(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval((int)x * 25 + 50, (int)y * 25 + 50, size, size);
        }
    }
    
    // Animation de score
    public static class ScoreAnimation extends Animation {
        private int x, y;
        private int score;
        private Color color;
        
        public ScoreAnimation(int x, int y, int score) {
            super(60);
            this.x = x;
            this.y = y;
            this.score = score;
            this.color = new Color(52, 152, 219, 255);
        }
        
        @Override
        public void update() {
            super.update();
            y -= 1; // Monte vers le haut
            
            // Fade out
            int alpha = (int)(255 * (1 - getProgress()));
            color = new Color(52, 152, 219, alpha);
        }
        
        @Override
        public void render(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("+" + score, x, y);
        }
    }
    
    // Animation de niveau
    public static class LevelUpAnimation extends Animation {
        private int x, y;
        private String text;
        
        public LevelUpAnimation() {
            super(120);
            this.x = 200;
            this.y = 300;
            this.text = "NIVEAU SUPÉRIEUR !";
        }
        
        @Override
        public void update() {
            super.update();
            // Animation de pulsation
        }
        
        @Override
        public void render(Graphics2D g2d) {
            float progress = getProgress();
            if (progress < 0.5f) {
                // Apparition
                int alpha = (int)(255 * (progress * 2));
                g2d.setColor(new Color(46, 204, 113, alpha));
            } else {
                // Disparition
                int alpha = (int)(255 * ((1 - progress) * 2));
                g2d.setColor(new Color(46, 204, 113, alpha));
            }
            
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, x - textWidth / 2, y);
        }
    }
    
    // Animation spéciale "AMAZING" pour 3 lignes
    public static class AmazingAnimation extends Animation {
        private int x, y;
        private String text;
        private Color[] colors;
        private int colorIndex;
        
        public AmazingAnimation() {
            super(180); // Plus longue que les autres animations
            this.x = 200;
            this.y = 250;
            this.text = "AMAZING !!!";
            this.colors = new Color[]{
                new Color(255, 0, 0),    // Rouge
                new Color(255, 165, 0),  // Orange
                new Color(255, 255, 0),  // Jaune
                new Color(0, 255, 0),    // Vert
                new Color(0, 0, 255),    // Bleu
                new Color(128, 0, 128)   // Violet
            };
            this.colorIndex = 0;
        }
        
        @Override
        public void update() {
            super.update();
            // Changer de couleur toutes les 10 frames
            if (currentTime % 10 == 0) {
                colorIndex = (colorIndex + 1) % colors.length;
            }
        }
        
        @Override
        public void render(Graphics2D g2d) {
            float progress = getProgress();
            
            // Animation d'apparition avec effet de zoom
            float scale = 1.0f;
            if (progress < 0.3f) {
                // Zoom in
                scale = 1.0f + (progress / 0.3f) * 0.5f;
            } else if (progress > 0.7f) {
                // Zoom out
                scale = 1.5f - ((progress - 0.7f) / 0.3f) * 0.5f;
            } else {
                // Taille normale
                scale = 1.5f;
            }
            
            // Sauvegarder la transformation actuelle
            AffineTransform originalTransform = g2d.getTransform();
            
            // Appliquer la transformation de zoom
            g2d.translate(x, y);
            g2d.scale(scale, scale);
            g2d.translate(-x, -y);
            
            // Couleur arc-en-ciel
            Color currentColor = colors[colorIndex];
            
            // Fond avec effet de lueur
            g2d.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 50));
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2d.drawString(text, x - textWidth / 2 - 2, y + 2);
            g2d.drawString(text, x - textWidth / 2 + 2, y - 2);
            g2d.drawString(text, x - textWidth / 2, y + 2);
            g2d.drawString(text, x - textWidth / 2, y - 2);
            
            // Texte principal
            int alpha = (int)(255 * (1 - Math.abs(progress - 0.5) * 2));
            g2d.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), alpha));
            g2d.drawString(text, x - textWidth / 2, y);
            
            // Restaurer la transformation
            g2d.setTransform(originalTransform);
            
            // Ajouter des particules d'étoiles
            if (progress < 0.8f) {
                for (int i = 0; i < 5; i++) {
                    int starX = x + (int)((Math.random() - 0.5) * 200);
                    int starY = y + (int)((Math.random() - 0.5) * 100);
                    g2d.setColor(new Color(255, 255, 255, (int)(255 * (1 - progress))));
                    g2d.fillOval(starX, starY, 3, 3);
                }
            }
        }
    }
} 