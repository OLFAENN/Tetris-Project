package com.tetris.gui;

import com.tetris.dao.UserDAO;
import com.tetris.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {
    private UserDAO userDAO;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterFrame() {
        this.userDAO = new UserDAO();
        
        setTitle("Tetris - Inscription");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 550);
        setLocationRelativeTo(null);
        
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Panneau principal avec fond uni
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Titre
        JLabel titleLabel = new JLabel("INSCRIPTION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        // Panneau de formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Username
        JLabel userLabel = new JLabel("Nom d'utilisateur");
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setForeground(new Color(70, 70, 70));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        usernameField = createCleanTextField();
        
        // Password
        JLabel passLabel = new JLabel("Mot de passe");
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passLabel.setForeground(new Color(70, 70, 70));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        
        passwordField = createCleanPasswordField();
        
        // Confirm Password
        JLabel confirmPassLabel = new JLabel("Confirmer le mot de passe");
        confirmPassLabel.setFont(new Font("Arial", Font.BOLD, 14));
        confirmPassLabel.setForeground(new Color(70, 70, 70));
        confirmPassLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmPassLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        
        confirmPasswordField = createCleanPasswordField();
        
        // Email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(new Color(70, 70, 70));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
        
        emailField = createCleanTextField();
        
        // Assembler le formulaire
        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmPassLabel);
        formPanel.add(confirmPasswordField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        
        // Panneau des boutons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        // Boutons principaux
        registerButton = createCleanButton("CRÉER LE COMPTE", new Color(46, 204, 113));
        backButton = createCleanButton("RETOUR", new Color(150, 150, 150));
        
        registerButton.addActionListener(e -> handleRegister());
        backButton.addActionListener(e -> goBack());
        
        // Ajouter les boutons avec espacement
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);
        
        // Assembler le panneau principal
        mainPanel.add(titleLabel);
        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private JTextField createCleanTextField() {
        JTextField field = new JTextField(20);
        field.setPreferredSize(new Dimension(250, 40));
        field.setMaximumSize(new Dimension(250, 40));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(50, 50, 50));
        field.setCaretColor(new Color(50, 50, 50));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createCleanPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setPreferredSize(new Dimension(250, 40));
        field.setMaximumSize(new Dimension(250, 40));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(50, 50, 50));
        field.setCaretColor(new Color(50, 50, 50));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JButton createCleanButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(250, 45));
        button.setMaximumSize(new Dimension(250, 45));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText();
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (userDAO.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Ce nom d'utilisateur existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            userDAO.createUser(username, password, email);
            JOptionPane.showMessageDialog(this, "Compte créé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            goBack();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void goBack() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
} 