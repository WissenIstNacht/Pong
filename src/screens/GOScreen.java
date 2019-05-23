package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import improvedStructure.Game;
import improvedStructure.GameState;

/**
 * Panel on which the game is drawn.
 * 
 * This class provide a canvas by extending a JPanel class. This screen
 * contains a game over msg (winner, etc) and buttons to resart the game
 * modes.
 * 
 * @author WissenIstNacht
 *
 */
public class GOScreen extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3732454082115626205L;

	Game game;
	
	//TODO: Implement back to game directly.
	JButton b_back, b_fill;
	
	/**
	 * Initializes GO screen by adding layout and various components
	 * @param g 
	 */
	public GOScreen(Game g) {
		game = g;
		setBackground(Color.DARK_GRAY);
		/* Layout */
		int noRow = 7;
		int noCol = 5;
		setLayout(new GridLayout(noRow, noCol, 0, 20));

		/* Components */
		b_back = new JButton("Back to Welcome");
		b_back.addActionListener(this);

		// Add invisible buttons to fill layout.
		for (int i = 0; i < noRow * noCol; i++) {
			b_fill = new JButton("Some" + i);

			switch (i) {
			case 22:
				add(b_back);
				break;
			default:
				b_fill.setVisible(false);
				b_fill.setEnabled(false);
				add(b_fill);
				break;
			}
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana", Font.BOLD, 50));
		String gameOverMsg = "GAME OVER!";

		int xPos = (getWidth()
				- g.getFontMetrics().stringWidth(gameOverMsg)) / 2;
		int yPos = getHeight() / 3 - 50 / 2;
		g.drawString(gameOverMsg, xPos, yPos);

		g.setFont(new Font("Verdana", Font.BOLD, 30));
		String winner = "The winner is Player x";
		xPos = (getWidth() - g.getFontMetrics().stringWidth(winner)) / 2;
		yPos = getHeight() / 2 - 40 / 2;
		g.drawString(winner, xPos, yPos);
		// TODO add number of balls hit
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(b_back)) {
			System.out.println("HI");
			game.overToWelcome();
			game.state = GameState.Welcome;
		}
	}
}