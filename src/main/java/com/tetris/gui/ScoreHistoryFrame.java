package com.tetris.gui;

import com.tetris.model.User;
import javax.swing.*;
import java.awt.*;

public class ScoreHistoryFrame extends JFrame {
    public ScoreHistoryFrame(User user) {
        setTitle("Mes scores - " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setupUI(user);
    }
    private void setupUI(User user) {
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
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // --- Affichage des scores ---
        java.util.List<com.tetris.model.Score> scores = new java.util.ArrayList<>();
        try {
            com.tetris.dao.ScoreDAO scoreDAO = new com.tetris.dao.ScoreDAO();
            scores = scoreDAO.getScoresByUser(user.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] columns = {"Score", "Niveau", "Lignes", "Date"};
        String[][] data = new String[scores.size()][4];
        for (int i = 0; i < scores.size(); i++) {
            com.tetris.model.Score s = scores.get(i);
            data[i][0] = String.valueOf(s.getScore());
            data[i][1] = String.valueOf(s.getLevel());
            data[i][2] = String.valueOf(s.getLinesCleared());
            data[i][3] = s.getDateTime() != null ? s.getDateTime().toString() : "";
        }
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(28);
        table.setEnabled(false);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(155, 89, 182));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        mainPanel.add(scrollPane, gbc);

        // Lien retour centré en bas
        gbc.gridy = 1;
        gbc.insets = new Insets(60, 0, 0, 0);
        JLabel retourLabel = new JLabel("<html><u>Retour</u></html>");
        retourLabel.setFont(new Font("Arial", Font.BOLD, 16));
        retourLabel.setForeground(Color.WHITE);
        retourLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        retourLabel.setHorizontalAlignment(SwingConstants.CENTER);
        retourLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new PostLoginFrame(user).setVisible(true);
                dispose();
            }
        });
        mainPanel.add(retourLabel, gbc);

        setContentPane(mainPanel);
    }
} 