package com.tetris.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("TETRIS COSMOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(new ImageOnlyPanel());
    }

    // Panel qui affiche l'image de fond (statique ou GIF), le titre et le bouton centré
    private class ImageOnlyPanel extends JPanel {
        private BufferedImage backgroundImg;
        private ImageIcon backgroundGif;
        private boolean isGif = false;
        private StylishButton letsGoBtn;
        private final String title = "TETRIS COSMOS";
        private Timer gifTimer;
        private final String[] possibleNames = {"ressources/img/back.gif", "ressources/img/back.jpeg", "ressources/img/back.jpg", "ressources/img/back.png"};

        public ImageOnlyPanel() {
            setLayout(null);
            loadBackground();
            setOpaque(false);
            if (isGif) {
                gifTimer = new Timer(40, e -> repaint());
                gifTimer.start();
            }
        }

        private void loadBackground() {
            for (String name : possibleNames) {
                File f = new File(name);
                if (f.exists()) {
                    if (name.endsWith(".gif")) {
                        backgroundGif = new ImageIcon(name);
                        isGif = true;
                    } else {
                        try {
                            backgroundImg = ImageIO.read(f);
                        } catch (Exception e) {
                            backgroundImg = null;
                        }
                    }
                    break;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (isGif && backgroundGif != null) {
                Image img = backgroundGif.getImage();
                g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            } else if (backgroundImg != null) {
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null);
            } else {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            // Titre centré verticalement
            drawTitleAndButton(g2d);
            g2d.dispose();
        }

        private void drawTitleAndButton(Graphics2D g2d) {
            String text = title;
            Font font = new Font("Arial", Font.BOLD, 60);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2;
            // Ombre légère
            g2d.setColor(new Color(0,0,0,120));
            g2d.drawString(text, x+3, y+4);
            // Texte principal
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, x, y);
            // Calculer la position du bouton sous le titre
            int btnWidth = 160;
            int btnHeight = 60;
            int btnX = (getWidth() - btnWidth) / 2;
            int btnY = y + textHeight + 40;
            if (letsGoBtn == null) {
                letsGoBtn = new StylishButton("Let's Go");
                letsGoBtn.setFont(new Font("Arial", Font.BOLD, 26));
                letsGoBtn.setForeground(Color.WHITE);
                letsGoBtn.setBackground(new Color(155, 89, 182));
                letsGoBtn.setFocusPainted(false);
                letsGoBtn.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
                letsGoBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                letsGoBtn.setBounds(btnX, btnY, btnWidth, btnHeight);
                letsGoBtn.addActionListener(e -> openLoginFrame());
                add(letsGoBtn);
            } else {
                letsGoBtn.setBounds(btnX, btnY, btnWidth, btnHeight);
            }
        }

        private void openLoginFrame() {
            if (gifTimer != null) gifTimer.stop();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            SwingUtilities.getWindowAncestor(this).dispose();
        }
    }

    // Bouton stylé avec dégradé mauve
    private static class StylishButton extends JButton {
        private boolean hover = false;
        public StylishButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setFont(new Font("Arial", Font.BOLD, 26));
            setForeground(Color.WHITE);
            setBackground(new Color(155, 89, 182)); // Mauve
            setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hover = true;
                    repaint();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            // Ombre portée
            if (hover) {
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRoundRect(6, 8, w - 12, h - 8, 40, 40);
            } else {
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRoundRect(6, 10, w - 12, h - 8, 40, 40);
            }
            // Dégradé mauve
            Color color1 = hover ? new Color(200, 120, 255) : new Color(155, 89, 182);   // haut
            Color color2 = hover ? new Color(230, 180, 255) : new Color(187, 102, 255);  // bas
            GradientPaint gradient = new GradientPaint(0, 0, color1, 0, h, color2);
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h - 4, 40, 40);
            // Bordure blanche fine
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 180));
            g2.drawRoundRect(0, 0, w - 1, h - 5, 40, 40);
            // Texte
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), (w - textWidth) / 2, (h + textHeight) / 2 - 8);
            g2.dispose();
        }
    }
} 