package com.tetris.gui;

import com.tetris.dao.UserDAO;
import com.tetris.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private UserDAO userDAO;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        this.userDAO = new UserDAO();
        setTitle("Connexion - Tetris Cosmos");
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

        // Panneau glassmorphism centré
        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fond semi-transparent simple
                g2.setColor(new Color(80, 44, 227 , 80));               
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Bordure simple
                g2.setStroke(new BasicStroke(1f));
                g2.setColor(new Color(80, 44, 227 , 60));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setPreferredSize(new Dimension(500, 420));
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Titre stylé
        JLabel titleLabel = new JLabel("Ravi de vous voir !");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Champ texte avec placeholder
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

        // Champ mot de passe avec placeholder
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

        // Bouton Se connecter
        JButton loginButton = new StylishButton("Se connecter");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        loginButton.setMaximumSize(new Dimension(220, 50));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setForeground(Color.WHITE);

        // Lien Créer un compte
        JLabel registerLabel = new JLabel("Créer un compte");
        registerLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        registerLabel.setForeground(Color.WHITE);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerLabel.setFont(registerLabel.getFont().deriveFont(Font.BOLD, 15f));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                registerLabel.setFont(registerLabel.getFont().deriveFont(Font.PLAIN, 15f));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                openRegisterFrame();
            }
        });

        // Lien Continuer en mode invité
        JLabel guestLabel = new JLabel("Continuer en mode invité");
        guestLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        guestLabel.setForeground(Color.WHITE);
        guestLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guestLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        guestLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        guestLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                guestLabel.setFont(guestLabel.getFont().deriveFont(Font.BOLD, 15f));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                guestLabel.setFont(guestLabel.getFont().deriveFont(Font.PLAIN, 15f));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ouvre la nouvelle fenêtre GuestFrame
                GuestFrame guestFrame = new GuestFrame();
                guestFrame.setVisible(true);
                LoginFrame.this.dispose();
            }
        });

        // Lien retour au menu principal
        JLabel backToMenuLabel = new JLabel("Retour au menu principal");
        backToMenuLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        backToMenuLabel.setForeground(Color.WHITE);
        backToMenuLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backToMenuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToMenuLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        backToMenuLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backToMenuLabel.setFont(backToMenuLabel.getFont().deriveFont(Font.BOLD, 15f));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                backToMenuLabel.setFont(backToMenuLabel.getFont().deriveFont(Font.PLAIN, 15f));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                // Retour au menu principal
                MainMenuFrame mainMenuFrame = new MainMenuFrame();
                mainMenuFrame.setVisible(true);
                LoginFrame.this.dispose();
            }
        });

        // Espacement
        glassPanel.add(titleLabel);
        glassPanel.add(Box.createVerticalStrut(20));
        glassPanel.add(usernameField);
        glassPanel.add(Box.createVerticalStrut(15));
        glassPanel.add(passwordField);
        glassPanel.add(Box.createVerticalStrut(30));
        glassPanel.add(loginButton);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(registerLabel);
        glassPanel.add(guestLabel);
        glassPanel.add(backToMenuLabel);

        // Centrage du panneau glass
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(glassPanel, gbc);

        setContentPane(mainPanel);
    }

    // Champ texte avec placeholder
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
    // Champ mot de passe avec placeholder
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

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JLabel label = new JLabel("<html><div style='text-align:center; color:#c0392b; font-size:14px;'><b>Veuillez remplir tous les champs.</b></div></html>");
            label.setFont(new Font("Arial", Font.BOLD, 16));
            JOptionPane.showMessageDialog(this, label, "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            User user = userDAO.authenticateUser(username, password);
            if (user != null) {
                openGameFrame(user);
            } else {
                JLabel label = new JLabel("<html><div style='text-align:center; color:#c0392b; font-size:14px;'><b>Nom d'utilisateur ou mot de passe incorrect.</b></div></html>");
                label.setFont(new Font("Arial", Font.BOLD, 16));
                JOptionPane.showMessageDialog(this, label, "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JLabel label = new JLabel("<html><div style='text-align:center; color:#c0392b; font-size:14px;'><b>Erreur de connexion à la base de données.</b></div></html>");
            label.setFont(new Font("Arial", Font.BOLD, 16));
            JOptionPane.showMessageDialog(this, label, "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleGuestLogin() {
        String guestName = JOptionPane.showInputDialog(this, "Entrez votre nom d'utilisateur:", "Continuer en mode invité", JOptionPane.QUESTION_MESSAGE);
        if (guestName != null && !guestName.trim().isEmpty()) {
            try {
                User guestUser = userDAO.createGuestUser(guestName.trim());
                openGameFrame(guestUser);
            } catch (SQLException ex) {
                JLabel label = new JLabel("<html><div style='text-align:center; color:#c0392b; font-size:14px;'><b>Erreur lors de la création du compte invité.</b></div></html>");
                label.setFont(new Font("Arial", Font.BOLD, 16));
                JOptionPane.showMessageDialog(this, label, "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void openRegisterFrame() {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        this.dispose();
    }

    private void openGameFrame(User user) {
        PostLoginFrame postLoginFrame = new PostLoginFrame(user);
        postLoginFrame.setVisible(true);
        this.dispose();
    }

    // Bouton stylé identique à celui du menu d'accueil
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

    // --- NOUVELLE FENÊTRE APRÈS CONNEXION ---
    class UserHomeFrame extends JFrame {
        private final User user;
        public UserHomeFrame(User user) {
            this.user = user;
            setTitle("Bienvenue, " + user.getUsername());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);
            setResizable(false);
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
            mainPanel.setOpaque(false);
            mainPanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.anchor = GridBagConstraints.CENTER;

            // Panneau horizontal pour les deux boutons
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
                UserScoresFrame scoresFrame = new UserScoresFrame(user);
                scoresFrame.setVisible(true);
                dispose();
            });
            buttonPanel.add(playBtn);
            buttonPanel.add(scoresBtn);
            mainPanel.add(buttonPanel, gbc);

            // Lien retour centré sous les boutons
            gbc.gridy = 1;
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
    }

    // --- PAGE SCORES UTILISATEUR (SQUELETTE) ---
    class UserScoresFrame extends JFrame {
        public UserScoresFrame(User user) {
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

            JLabel label = new JLabel("Ici s'afficheront les scores de l'utilisateur.", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 22));
            label.setForeground(Color.WHITE);
            mainPanel.add(label, gbc);

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
                    new UserHomeFrame(user).setVisible(true);
                    dispose();
                }
            });
            mainPanel.add(retourLabel, gbc);

            setContentPane(mainPanel);
        }
    }
} 