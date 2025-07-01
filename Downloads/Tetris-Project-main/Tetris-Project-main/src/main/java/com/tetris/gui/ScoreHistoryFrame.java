package com.tetris.gui;

import com.tetris.dao.ScoreDAO;
import com.tetris.model.Score;
import com.tetris.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScoreHistoryFrame extends JFrame {

    public ScoreHistoryFrame(User user) {
        setTitle("Historique des scores - " + user.getUsername());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Titre
        JLabel titleLabel = new JLabel("Historique des scores de " + user.getUsername(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Table des scores
        String[] columnNames = {"Score", "Niveau", "Lignes", "Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable scoreTable = new JTable(tableModel);
        scoreTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        scoreTable.setRowHeight(24);

        try {
            ScoreDAO scoreDAO = new ScoreDAO();
            List<Score> scores = scoreDAO.getScoresByUser(user.getId());

            if (scores.isEmpty()) {
                tableModel.addRow(new Object[]{"Aucun score", "", "", ""});
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                for (Score s : scores) {
                    tableModel.addRow(new Object[]{
                        s.getScore(),
                        s.getLevel(),
                        s.getLinesCleared(),
                        s.getDateTime().format(formatter)
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des scores.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Bouton fermer
        JButton closeButton = new JButton("Fermer");
        closeButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
