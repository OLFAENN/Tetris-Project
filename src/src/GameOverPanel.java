package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameOverPanel extends JPanel {
	private JButton btnRetourAccueil;

    public GameOverPanel(int score, Runnable retourAccueilCallback) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Titre Game Over
        JLabel lblGameOver = new JLabel("GAME OVER");
        lblGameOver.setForeground(Color.RED);
        lblGameOver.setFont(new Font("Arial", Font.BOLD, 48));
        lblGameOver.setHorizontalAlignment(SwingConstants.CENTER);
        lblGameOver.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        add(lblGameOver, BorderLayout.NORTH);

        // Affiche le score
        JLabel lblScore = new JLabel("Votre score : " + score);
        lblScore.setForeground(Color.WHITE);
        lblScore.setFont(new Font("Arial", Font.PLAIN, 28));
        lblScore.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblScore, BorderLayout.CENTER);

        // Bouton Retour Accueil
        btnRetourAccueil = new JButton("Retour à l'accueil");
        btnRetourAccueil.setFont(new Font("Arial", Font.BOLD, 18));
        btnRetourAccueil.setBackground(new Color(50, 50, 50));
        btnRetourAccueil.setForeground(Color.WHITE);
        btnRetourAccueil.setFocusPainted(false);
        btnRetourAccueil.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        btnRetourAccueil.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRetourAccueil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (retourAccueilCallback != null) {
                    retourAccueilCallback.run();
                }
            }
        });

        JPanel panelBouton = new JPanel();
        panelBouton.setBackground(Color.BLACK);
        panelBouton.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        panelBouton.add(btnRetourAccueil);
        add(panelBouton, BorderLayout.SOUTH);
    }
}
