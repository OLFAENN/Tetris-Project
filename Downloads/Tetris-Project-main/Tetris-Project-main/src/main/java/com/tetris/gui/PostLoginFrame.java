package com.tetris.gui;

import com.tetris.model.User;

import javax.swing.*;
import java.awt.*;

public class PostLoginFrame extends JFrame {

    public PostLoginFrame(User user) {
        setTitle("Bienvenue " + user.getUsername());
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal vertical
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // marges autour

        // Message de bienvenue
        JLabel welcomeLabel = new JLabel("Bonjour " + user.getUsername() + " :)", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Espacement
        Component verticalSpace = Box.createVerticalStrut(40);

        // Boutons
        JButton playButton = new JButton("Jouer");
        JButton historyButton = new JButton("Historique des scores");

        playButton.setMaximumSize(new Dimension(200, 40));
        historyButton.setMaximumSize(new Dimension(200, 40));

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Action: Jouer
        playButton.addActionListener(e -> {
            new GameFrame(user).setVisible(true);
            dispose(); // ferme cette fenêtre
        });

        // Action: Historique des scores
        historyButton.addActionListener(e -> {
            new ScoreHistoryFrame(user); // ouvre ScoreHistoryFrame
        });
        
        // Bouton Se déconnecter
        JButton logoutButton = new JButton("Se déconnecter");
        logoutButton.setMaximumSize(new Dimension(200, 40));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
     // Action: Se déconnecter
        logoutButton.addActionListener(e -> {
            dispose(); // ferme la fenêtre actuelle
            new LoginFrame().setVisible(true); // rouvre la fenêtre de connexion
        });


        // Ajout au panel
        mainPanel.add(welcomeLabel);
        mainPanel.add(verticalSpace);
        mainPanel.add(playButton);
        mainPanel.add(Box.createVerticalStrut(15)); // espace entre les boutons
        mainPanel.add(historyButton);
        
        mainPanel.add(Box.createVerticalStrut(15)); // espace entre les boutons
        mainPanel.add(logoutButton);


        add(mainPanel);
        setVisible(true);
    }
}
