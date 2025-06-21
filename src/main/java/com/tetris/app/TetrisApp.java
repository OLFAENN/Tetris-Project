package com.tetris.app;

import com.tetris.gui.MainMenuFrame;
import com.tetris.dao.DatabaseConnection;

import javax.swing.*;

public class TetrisApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Essayer d'initialiser la base de données
                DatabaseConnection.getInstance().initializeDatabase();
                System.out.println("Base de données connectée avec succès");
            } catch (Exception e) {
                System.out.println("Erreur de connexion à la base de données: " + e.getMessage());
                System.out.println("L'application fonctionnera en mode hors ligne");
                
                // Afficher un message d'information
                JOptionPane.showMessageDialog(null, 
                    "Impossible de se connecter à la base de données MySQL.\n" +
                    "L'application fonctionnera en mode hors ligne.\n\n" +
                    "Pour activer la sauvegarde des scores, assurez-vous que :\n" +
                    "• MySQL est installé et démarré\n" +
                    "• La base de données 'tetris_db' existe\n" +
                    "• Les identifiants sont corrects dans DatabaseConnection.java", 
                    "Mode hors ligne", JOptionPane.WARNING_MESSAGE);
            }
            
            // Lancer le menu principal
            MainMenuFrame mainMenu = new MainMenuFrame();
            mainMenu.setVisible(true);
        });
    }
}
