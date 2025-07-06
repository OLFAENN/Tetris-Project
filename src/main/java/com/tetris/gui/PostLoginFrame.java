package com.tetris.gui;

import com.tetris.model.User;
import javax.swing.*;
import java.awt.*;

public class PostLoginFrame extends JFrame {
    private final User user;
    public PostLoginFrame(User user) {
        this.user = user;
        setTitle("Bienvenue, " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setupUI();
    }
    private void setupUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            ImageIcon gif = new ImageIcon("ressources/img/back.gif");
            {
                new Timer(40, e -> repaint()).start();
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(gif.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Message de bienvenue centré
        JLabel welcomeLabel = new JLabel("Bienvenue, " + user.getUsername() + " :)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(welcomeLabel, gbc);

        // Panneau horizontal pour les deux boutons
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setOpaque(false);
        JButton playBtn = new StylishButton("Jouer");
        playBtn.setPreferredSize(new Dimension(220, 60));
        playBtn.addActionListener(e -> {
            GameFrame gameFrame = new GameFrame(user);
            gameFrame.setVisible(true);
            dispose();
        });
        JButton scoresBtn = new StylishButton("Voir mes scores");
        scoresBtn.setPreferredSize(new Dimension(220, 60));
        scoresBtn.addActionListener(e -> {
            ScoreHistoryFrame scoresFrame = new ScoreHistoryFrame(user);
            scoresFrame.setVisible(true);
            dispose();
        });
        buttonPanel.add(playBtn);
        buttonPanel.add(scoresBtn);
        mainPanel.add(buttonPanel, gbc);

        // Lien retour centré sous les boutons
        gbc.gridy = 2;
        gbc.insets = new Insets(40, 0, 0, 0);
        JLabel retourLabel = new JLabel("<html><u>Retour</u></html>");
        retourLabel.setFont(new Font("Arial", Font.BOLD, 16));
        retourLabel.setForeground(Color.WHITE);
        retourLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        retourLabel.setHorizontalAlignment(SwingConstants.CENTER);
        retourLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        mainPanel.add(retourLabel, gbc);

        setContentPane(mainPanel);
    }
    // StylishButton identique à LoginFrame
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
            setBackground(new Color(155, 89, 182));
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
            if (hover) {
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRoundRect(6, 8, w - 12, h - 8, 40, 40);
            } else {
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillRoundRect(6, 10, w - 12, h - 8, 40, 40);
            }
            Color color1 = hover ? new Color(200, 120, 255) : new Color(155, 89, 182);
            Color color2 = hover ? new Color(230, 180, 255) : new Color(187, 102, 255);
            GradientPaint gradient = new GradientPaint(0, 0, color1, 0, h, color2);
            g2.setPaint(gradient);
            g2.fillRoundRect(0, 0, w, h - 4, 40, 40);
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(255, 255, 255, 180));
            g2.drawRoundRect(0, 0, w - 1, h - 5, 40, 40);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getAscent();
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), (w - textWidth) / 2, (h + textHeight) / 2 - 8);
            g2.dispose();
        }
    }
} 