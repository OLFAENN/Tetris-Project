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

    public RegisterFrame() {
        this.userDAO = new UserDAO();
        setTitle("Créer un compte - Tetris Cosmos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setupUI();
    }

    private void setupUI() {
        // Panel principal avec fond GIF animé
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

        // Panneau central semi-transparent
        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(80, 44, 227, 80));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(new Color(80, 44, 227, 80));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setPreferredSize(new Dimension(500, 500));
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Titre stylé
        JLabel titleLabel = new JLabel("Créer un compte");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Champs
        usernameField = new PlaceholderTextField("Entrez votre nom d'utilisateur");
        usernameField.setPreferredSize(new Dimension(250, 40));
        usernameField.setMaximumSize(new Dimension(350, 40));
        usernameField.setBackground(new Color(255, 255, 255, 220));
        usernameField.setCaretColor(new Color(60, 60, 60));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 255), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new PlaceholderPasswordField("Entrez votre mot de passe");
        passwordField.setPreferredSize(new Dimension(250, 40));
        passwordField.setMaximumSize(new Dimension(350, 40));
        passwordField.setBackground(new Color(255, 255, 255, 220));
        passwordField.setCaretColor(new Color(60, 60, 60));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 255), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmPasswordField = new PlaceholderPasswordField("Confirmez votre mot de passe");
        confirmPasswordField.setPreferredSize(new Dimension(250, 40));
        confirmPasswordField.setMaximumSize(new Dimension(350, 40));
        confirmPasswordField.setBackground(new Color(255, 255, 255, 220));
        confirmPasswordField.setCaretColor(new Color(60, 60, 60));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 255), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = new PlaceholderTextField("Entrez votre adresse email");
        emailField.setPreferredSize(new Dimension(250, 40));
        emailField.setMaximumSize(new Dimension(350, 40));
        emailField.setBackground(new Color(255, 255, 255, 220));
        emailField.setCaretColor(new Color(60, 60, 60));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 150, 255), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bouton Créer le compte
        JButton registerButton = new StylishButton("Créer le compte");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> handleRegister());
        registerButton.setMaximumSize(new Dimension(220, 50));
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setForeground(Color.WHITE);

        // Lien retour à la connexion
        JLabel backLabel = new JLabel("Retour à la connexion");
        backLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        backLabel.setForeground(Color.WHITE);
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backLabel.setFont(backLabel.getFont().deriveFont(Font.BOLD, 15f));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backLabel.setFont(backLabel.getFont().deriveFont(Font.PLAIN, 15f));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                goBack();
            }
        });

        // Espacement et ajout au panel
        glassPanel.add(titleLabel);
        glassPanel.add(Box.createVerticalStrut(20));
        glassPanel.add(usernameField);
        glassPanel.add(Box.createVerticalStrut(15));
        glassPanel.add(passwordField);
        glassPanel.add(Box.createVerticalStrut(15));
        glassPanel.add(confirmPasswordField);
        glassPanel.add(Box.createVerticalStrut(15));
        glassPanel.add(emailField);
        glassPanel.add(Box.createVerticalStrut(30));
        glassPanel.add(registerButton);
        glassPanel.add(Box.createVerticalStrut(15));
        glassPanel.add(backLabel);

        // Centrage du panneau glass
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(glassPanel, gbc);

        setContentPane(mainPanel);
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = emailField.getText();
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showThemedError("Veuillez remplir tous les champs.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showThemedError("Les mots de passe ne correspondent pas.");
            return;
        }
        try {
            if (userDAO.usernameExists(username)) {
                showThemedError("Ce nom d'utilisateur existe déjà.");
                return;
            }
            userDAO.createUser(username, password, email);
            showThemedSuccess("Compte créé avec succès !");
            goBack();
        } catch (SQLException ex) {
            showThemedError("Erreur lors de la création du compte: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void goBack() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }

    // --- Placeholders ---
    private static class PlaceholderTextField extends JTextField {
        private String placeholder;
        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setForeground(new Color(60, 60, 60));
            setFont(new Font("Arial", Font.PLAIN, 16));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setFont(getFont());
                g2.setColor(new Color(150, 150, 150, 180));
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 4, getHeight() / 2 + getFont().getSize() / 2 - 2);
                g2.dispose();
            }
        }
    }
    private static class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;
        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setForeground(new Color(60, 60, 60));
            setFont(new Font("Arial", Font.PLAIN, 16));
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getPassword().length == 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setFont(getFont());
                g2.setColor(new Color(150, 150, 150, 180));
                Insets insets = getInsets();
                g2.drawString(placeholder, insets.left + 4, getHeight() / 2 + getFont().getSize() / 2 - 2);
                g2.dispose();
            }
        }
    }

    // --- StylishButton (identique à LoginFrame) ---
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

    // --- ThemedDialog pour messages d'erreur/succès ---
    private void showThemedError(String message) {
        JLabel label = new JLabel("<html><div style='text-align:center; color:#c0392b; font-size:14px;'><b>" + message + "</b></div></html>");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        JOptionPane.showMessageDialog(this, label, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    private void showThemedSuccess(String message) {
        JLabel label = new JLabel("<html><div style='text-align:center; color:#27ae60; font-size:14px;'><b>" + message + "</b></div></html>");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        JOptionPane.showMessageDialog(this, label, "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
} 