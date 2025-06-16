package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GradientPanel extends JPanel {
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = new Color(58, 123, 213);
        Color color2 = new Color(58, 96, 115);
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

}
