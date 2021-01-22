package hr.fer.oprpp1.hw08.jnotepadpp;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Helper class SeparatorPanel represents a separator panel
 * @author leokiparje
 *
 */

public class SeparatorPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /*
     * Color of the left part of separation
     */
    protected Color leftColor;
    
    /*
     * Color of the right part of separation
     */
    protected Color rightColor;

    /*
     * Basic constructor
     */
    public SeparatorPanel(Color leftColor, Color rightColor) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(leftColor);
        g.drawLine(0, 0, 0, getHeight());
        g.setColor(rightColor);
        g.drawLine(1, 0, 1, getHeight());
    }

}