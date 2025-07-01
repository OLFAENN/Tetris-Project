package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AccueilTetris extends JFrame {
	public AccueilTetris() {
		setTitle("Tetris - Accueil");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal avec fond dégradé
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());

        // Titre
        JLabel titre = new JLabel("Bienvenue sur TETRIS");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titre.setForeground(Color.WHITE);
        titre.setHorizontalAlignment(SwingConstants.CENTER);
        titre.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        // Logo optionnel
        // JLabel logo = new JLabel(new ImageIcon("tetris_logo.png"));
        // logo.setHorizontalAlignment(SwingConstants.CENTER);
        // mainPanel.add(logo, BorderLayout.NORTH);

        // Boutons
        JButton btnJouer = createStyledButton("Jouer");
        JButton btnConnexion = createStyledButton("Se connecter");
        JButton btnCreerCompte = createStyledButton("Créer un compte");

        // Panneau boutons
        JPanel boutonPanel = new JPanel();
        boutonPanel.setOpaque(false);
        boutonPanel.setLayout(new GridLayout(3, 1, 15, 15));
        boutonPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80));
        boutonPanel.add(btnJouer);
        boutonPanel.add(btnConnexion);
        boutonPanel.add(btnCreerCompte);

        // Actions
        btnJouer.addActionListener(e -> lancerJeu());
        btnConnexion.addActionListener(e -> JOptionPane.showMessageDialog(this, "Connexion à implémenter."));
        btnCreerCompte.addActionListener(e -> JOptionPane.showMessageDialog(this, "Création de compte à implémenter."));

        mainPanel.add(titre, BorderLayout.NORTH);
        mainPanel.add(boutonPanel, BorderLayout.CENTER);
        add(mainPanel);

        setVisible(true);
    }
	 private JButton createStyledButton(String text) {
	        JButton button = new JButton(text);
	        button.setFocusPainted(false);
	        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
	        button.setBackground(new Color(0, 120, 215));
	        button.setForeground(Color.WHITE);
	        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	        
	        // Effet hover
	        button.addMouseListener(new MouseAdapter() {
	            public void mouseEntered(MouseEvent e) {
	                button.setBackground(new Color(0, 90, 180));
	            }

	            public void mouseExited(MouseEvent e) {
	                button.setBackground(new Color(0, 120, 215));
	            }
	        });

	        return button;
	    }

    private void lancerJeu() {
        dispose(); // Ferme la fenêtre d'accueil
        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GrilleTetris());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AccueilTetris());
    }
}
