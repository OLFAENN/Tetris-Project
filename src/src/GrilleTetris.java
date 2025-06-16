package src;

import javax.swing.*; //importe les classes Swing comme `JPanel`, `JFrame`... utilisées pour créer une interface graphique.
import java.awt.*; //importe les classes AWT comme `Graphics`, `Color`, `Dimension`... utilisées pour dessiner et gérer les couleurs, tailles, etc.
import java.awt.event.*;
import java.util.Random;

//ce qui explique implements KeyListner
/*public interface KeyListener {
    void keyPressed(KeyEvent e);   // Quand une touche est enfoncée
    void keyReleased(KeyEvent e);  // Quand une touche est relâchée
    void keyTyped(KeyEvent e);     // Quand une touche "caractère" est tapée
}*/


public class GrilleTetris extends JPanel implements KeyListener{
	 private final int lignes = 20;
	    private final int colonnes = 10;
	    private final int tailleCase = 30;
	    
	    private int[][] grilleFixe = new int[lignes][colonnes]; // la grille avec les pièces fixées
	    private Color[][] couleursFixes = new Color[lignes][colonnes]; // couleurs des blocs fixés


	    private int score = 0;

	    private Tetromino pieceActive;
	    private int xPiece;
	    private int yPiece;

	    private Timer timer;
	    private final Random random = new Random();

	    public GrilleTetris() {
	        setPreferredSize(new Dimension(colonnes * tailleCase, lignes * tailleCase)); // attend un objet Dimension en paramètre new Dimension(int width, int height)
	        setBackground(Color.BLACK);//le fond du panneau sera noir
	        
	        nouvellePiece();
	        /*  toutes les 500ms=0.5s, Java exécute :
    si la pièce peut descendre :
        yPiece++ ;
    sinon :
        fixer la pièce
        vérifier les lignes complètes
        générer une nouvelle pièce
    puis on redessine l’écran*/
	        timer = new Timer(500, e -> {
	        	if (peutDescendre()) {
	                yPiece++;
	            } else {
	                fixerPiece();
	                verifierLignes();
	                nouvellePiece();
	            }
	            repaint();
	        });
	        
	        timer.start();
	        setFocusable(true);
	        addKeyListener(this);
	    }
	    private void nouvellePiece() {
	        pieceActive = genererPieceAleatoire();
	        xPiece = random.nextInt(colonnes - pieceActive.shape[0].length + 1); // position aléatoire sur X
	        yPiece = 0; //Tetrominos se trouve initiallement en haut de la grille
	        
	        // Si on ne peut pas placer la nouvelle pièce, jeu fini (optionnel)
	        if (!peutPlacer(pieceActive.shape, yPiece, xPiece)) {
	        	 gameOver(); // méthode deja créé
	        }
	    }

	    private Tetromino genererPieceAleatoire() {
	        int choix = random.nextInt(5); // 5 types
	        switch (choix) {
	            case 0: // carré
	                return new Tetromino(new int[][] {
	                        {1, 1},
	                        {1, 1}
	                }, Color.YELLOW);
	            case 1: // ligne
	                return new Tetromino(new int[][] {
	                        {1, 1, 1, 1}
	                }, Color.CYAN);
	            case 2: // forme T
	                return new Tetromino(new int[][] {
	                        {1, 1, 1},
	                        {0, 1, 0}
	                }, Color.MAGENTA);
	            case 3: // forme S
	                return new Tetromino(new int[][] {
	                        {0, 1, 1},
	                        {1, 1, 0}
	                }, Color.GREEN);
	            case 4: // forme Z
	                return new Tetromino(new int[][] {
	                        {1, 1, 0},
	                        {0, 1, 1}
	                }, Color.RED);
	            default:
	                return new Tetromino(new int[][] {
	                        {1}
	                }, Color.WHITE);
	        }
	        
	    }
	    
	    private boolean peutPlacer(int[][] shape, int y, int x) {
	        for (int i = 0; i < shape.length; i++) {
	            for (int j = 0; j < shape[i].length; j++) {
	                if (shape[i][j] == 1) {
	                	//Calcule la position réelle dans la grille de jeu où cette case irait
	                    int newY = y + i;
	                    int newX = x + j;
	                    if (newX < 0 || newX >= colonnes || newY < 0 || newY >= lignes) {
	                        return false; // hors grille
	                    }
	                    if (grilleFixe[newY][newX] != 0) {
	                        return false; // collision avec bloc fixe
	                    }
	                }
	            }
	        }
	        return true;
	    }
	    
	    private boolean peutDescendre() {
	        return peutPlacer(pieceActive.shape, yPiece + 1, xPiece);
	    }
	    
	    private void fixerPiece() {
	        for (int i = 0; i < pieceActive.shape.length; i++) {
	            for (int j = 0; j < pieceActive.shape[i].length; j++) {
	                if (pieceActive.shape[i][j] == 1) {
	                    int y = yPiece + i;
	                    int x = xPiece + j;
	                    grilleFixe[y][x] = 1;
	                    couleursFixes[y][x] = pieceActive.color;
	                }
	            }
	        }
	    }
	    
	    private void verifierLignes() {
	        for (int i = lignes - 1; i >= 0; i--) 
	        //puisque la grille commence a etre remplit du bas vers le haut
	        {
	            boolean ligneComplete = true;
	            for (int j = 0; j < colonnes; j++) {
	            	//Si un seul bloc n'est pas occupé la ligne n'est pas complete
	                if (grilleFixe[i][j] == 0) {
	                    ligneComplete = false;
	                    break;
	                }
	            }
	            if (ligneComplete) {
	                supprimerLigne(i);
	                score += 100; // 100 points par ligne supprimée
	                ajusterVitesse();// diminue l'intervalle pour accélérer la chute des pièces
	                i++; // vérifier la même ligne à nouveau après le décalage
	            }
	        }
	    }
	    
	    private void supprimerLigne(int ligne) {
	    	/*Elle copie chaque ligne supérieure (ligne i-1) vers la ligne en dessous (i).
Cela a pour effet de "faire tomber" toutes les lignes au-dessus de celle supprimée.*/
	        for (int i = ligne; i > 0; i--) {
	            grilleFixe[i] = grilleFixe[i-1].clone();
	            couleursFixes[i] = couleursFixes[i-1].clone();
	        }
	        // Ligne 0 remise à zéro
	        grilleFixe[0] = new int[colonnes];
	        couleursFixes[0] = new Color[colonnes];
	    }

	    
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        
	     // Dessine la grille et les blocs fixes
	        for (int i = 0; i < lignes; i++) {
	            for (int j = 0; j < colonnes; j++) {
	                if (grilleFixe[i][j] != 0) {
	                    g.setColor(couleursFixes[i][j]);
	                    int x = j * tailleCase;
	                    int y = i * tailleCase;
	                    g.fillRect(x, y, tailleCase, tailleCase);
	                    g.setColor(Color.BLACK);
	                    g.drawRect(x, y, tailleCase, tailleCase);
	                }
	            }
	        }
	        
	        // Dessine la pièce active
	        drawPiece(g, pieceActive, xPiece, yPiece);
	        

	        // Dessine la grille
	        g.setColor(Color.DARK_GRAY);
	        //Lignes horizontales (séparant les rangées)
	        for (int i = 0; i <= lignes; i++) {
	            g.drawLine(0, i * tailleCase, colonnes * tailleCase, i * tailleCase);
	        }
	     // Lignes verticales (séparant les colonnes)
	        for (int j = 0; j <= colonnes; j++) {
	            g.drawLine(j * tailleCase, 0, j * tailleCase, lignes * tailleCase);
	        }
	        
	       
	        // Dessine la pièce active
	        drawPiece(g, pieceActive, xPiece, yPiece);
	        
	        g.setColor(Color.WHITE);
	        g.setFont(new Font("Arial", Font.BOLD, 16));
	        g.drawString("Score: " + score, 10, 20);
	    }
	        
	        private void drawPiece(Graphics g, Tetromino t, int xOffset, int yOffset) {
	            g.setColor(t.color);
	            for (int i = 0; i < t.shape.length; i++) {
	                for (int j = 0; j < t.shape[i].length; j++) {
	                    if (t.shape[i][j] == 1) {
	                        int x = (xOffset + j) * tailleCase;
	                        int y = (yOffset + i) * tailleCase;
	                        g.fillRect(x, y, tailleCase, tailleCase);
	                        g.setColor(Color.BLACK);
	                        g.drawRect(x, y, tailleCase, tailleCase);
	                        g.setColor(t.color);
	                    }
	                }
	            }
	            
	            
	    }
	        // Méthode pour pivoter la matrice 90° dans le sens horaire
	        private int[][] rotation90(int[][] shape) {
	            int rows = shape.length;
	            int cols = shape[0].length;
	            int[][] rotated = new int[cols][rows];
	            for (int i = 0; i < rows; i++) {
	                for (int j = 0; j < cols; j++) {
	                    rotated[j][rows - 1 - i] = shape[i][j];
	                }
	            }
	            return rotated;
	        }
	        
	     // KeyListener pour gérer les mouvements et rotation
	        @Override
	        public void keyPressed(KeyEvent e) {
	            int key = e.getKeyCode();

	            switch (key) {
	                case KeyEvent.VK_LEFT:
	                    if (peutPlacer(pieceActive.shape, yPiece, xPiece - 1)) {
	                        xPiece--;
	                    }
	                    break;
	                case KeyEvent.VK_RIGHT:
	                    if (peutPlacer(pieceActive.shape, yPiece, xPiece + 1)) {
	                        xPiece++;
	                    }
	                    break;
	                case KeyEvent.VK_DOWN:
	                    if (peutPlacer(pieceActive.shape, yPiece + 1, xPiece)) {
	                        yPiece++;
	                    }
	                    break;
	                case KeyEvent.VK_UP: // rotation
	                    int[][] rotated = rotation90(pieceActive.shape);
	                    if (peutPlacer(rotated, yPiece, xPiece)) {
	                        pieceActive.shape = rotated;
	                    }
	                    break;
	            }
	            repaint();
	        }
/*choisi de ne pas utiliser ces deux méthodes (ce qui est courant en Tetris).
	je les laisses vides parce que seul keyPressed(...) nous intéresse*/
	        @Override
	        public void keyReleased(KeyEvent e) { }
	        @Override
	        public void keyTyped(KeyEvent e) { }
//C'est la fct main qu'on utilisais avant de créer une interface d'acceuil
	        
//	    public static void main(String[] args) {
//	    	 JFrame frame = new JFrame("Grille Tetris avec Pièces");
//	         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	         frame.add(new GrilleTetris());
//	         frame.pack();
//	         frame.setLocationRelativeTo(null);
//	         frame.setVisible(true);
//	    }
//
//	
	        private void gameOver() {
	            timer.stop(); // stop le timer cad le jeu arrete

	            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this); //Récupère la fenêtre principale
	            topFrame.getContentPane().removeAll();//Supprime tout le contenu actuel de la fenêtre

	            GameOverPanel gameOverPanel = new GameOverPanel(score, () -> {
	                topFrame.getContentPane().removeAll(); // Vide à nouveau la fenêtre
	                GrilleTetris nouvelleGrille = new GrilleTetris();// Crée une nouvelle grille de jeu
	                topFrame.add(nouvelleGrille); // L'ajoute à la fenêtre
	                topFrame.revalidate(); // Met à jour la fenêtre
	                nouvelleGrille.requestFocusInWindow();  // Redonne le focus clavier au jeu
	            });

	            topFrame.add(gameOverPanel);
	            topFrame.revalidate();
	            topFrame.repaint();
	        }
	        
	        private void ajusterVitesse() {
	        	if (score >= 400) {
	                timer.setDelay(200); // encore plus rapide
	            } else if (score >= 200) {
	                timer.setDelay(300); // vitesse intermédiaire
	            } else {
	                timer.setDelay(500); // vitesse de départ
	            }
	        }


}
