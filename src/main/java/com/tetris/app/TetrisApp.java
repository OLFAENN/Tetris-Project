package com.tetris.app;

import com.tetris.gui.MainMenuFrame;
import com.tetris.dao.DatabaseConnection;

import javax.swing.*;

public class TetrisApp {
    public static void main(String[] args) {
        // Vérifier si c'est un test de connexion
        if (args.length > 0 && args[0].equals("--test-connection")) {
            testDatabaseConnection();
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                // Essayer d'initialiser la base de données
                DatabaseConnection dbConnection = DatabaseConnection.getInstance();
                
                // Tester la connexion
                if (dbConnection.testConnection()) {
                    dbConnection.initializeDatabase();
                    System.out.println("Base de données connectée avec succès");
                } else {
                    throw new Exception("Impossible de se connecter à la base de données");
                }
                
            } catch (Exception e) {
                System.out.println("Erreur de connexion à la base de données: " + e.getMessage());
                System.out.println("L'application fonctionnera en mode hors ligne");
                
                // Afficher un message d'information
                int choice = JOptionPane.showConfirmDialog(null, 
                    "Impossible de se connecter à la base de données.\n\n" +
                    "Voulez-vous continuer en mode hors ligne ?\n" +
                    "(Les scores ne seront pas sauvegardés)", 
                    "Erreur de connexion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (choice == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
            
            // Lancer le menu principal
            MainMenuFrame mainMenu = new MainMenuFrame();
            mainMenu.setVisible(true);
        });
    }
    
    private static void testDatabaseConnection() {
        System.out.println("Test de connexion à la base de données...");
        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            if (dbConnection.testConnection()) {
                System.out.println("✓ Connexion réussie!");
                System.out.println("La base de données est accessible.");
                System.exit(0);
            } else {
                System.out.println("✗ Échec de la connexion!");
                System.out.println("Vérifiez la configuration dans DatabaseConnection.java");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("✗ Erreur lors du test: " + e.getMessage());
            System.exit(1);
        }
    }
}
