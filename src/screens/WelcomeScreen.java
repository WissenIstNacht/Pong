package screens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import improvedStructure.Game;
import improvedStructure.GameState;

/**
	 * Panel on which the welcome screen is drawn.
	 * 
	 * This class provide a canvas by extending a JPanel class. This screen
	 * contains a welcome msg and buttons to sart the game modes.
	 * 
	 * @author WissenIstNacht
	 */
public class WelcomeScreen extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3732454082115626205L;

	private Game game;
	
	private JButton b_singlePlayer, b_twoPlayer;
	private JButton b_settings;

	private JLabel lbl_welcome;

	/**
	 * Initializes Welcome screen by adding layout and various components
	 */
	public WelcomeScreen(Game g) {
		game = g;
		setLayout(new GridLayout(2,1));
		
		JPanel top = new JPanel();
		top.setBackground(Color.DARK_GRAY);
		top.setLayout(new BorderLayout());
		
		lbl_welcome = new JLabel("Welcome to Pong!", SwingConstants.CENTER);
		lbl_welcome.setForeground(Color.WHITE);
		lbl_welcome.setFont(new Font("Verdana", Font.BOLD, 40));
		top.add(lbl_welcome, BorderLayout.CENTER);
		
		JPanel bot = new JPanel();
		
		int noRow = 6;
		int noCol = 5;
		bot.setLayout(new GridLayout(noRow, noCol, 25, 5));
		bot.setBackground(Color.DARK_GRAY);
		b_singlePlayer = new JButton("Single Player");
		b_singlePlayer.addActionListener(this);
		b_twoPlayer = new JButton("Two Player");
		b_twoPlayer.addActionListener(this);
		b_settings = new JButton("Settings");
		b_settings.setEnabled(false);

		// Add invisible buttons to fill layout.
		for (int i = 0; i < noRow * noCol; i++) {
			JButton b_fill = new JButton("Some" + i);

			switch (i) {
			case 6:
				bot.add(b_singlePlayer);
				break;
			case 8:
				bot.add(b_twoPlayer);
				break;
			case 17:
				bot.add(b_settings);
				break;
			default:
				b_fill.setVisible(false);
				b_fill.setEnabled(false);
				bot.add(b_fill);
				break;
			}
		}
		add(top);
		add(bot);
		setVisible(true);
	}

	/**
	 * Overwrite function inherited by JPanel
	 * 
	 * This is used to draw the welcome message on the panel. (This is done
	 * because the layout created for the buttons on this panel does no
	 * longer allow to place a label.
	 * 
	 * @param g
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana", Font.BOLD, 50));
		String welcomeMsg = "Welcome to Pong!";

		int xPos = (getWidth() - g.getFontMetrics().stringWidth(welcomeMsg))
				/ 2;
		int yPos = getHeight() / 3 - 50 / 2;
		g.drawString("Welcome to Pong!", xPos, yPos);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(b_singlePlayer)) {
			game.welcomeToRunning(true);
			game.state = GameState.Running;
		} else if (e.getSource().equals(b_twoPlayer)) {
			game.welcomeToRunning(false);
			game.state = GameState.Running;
		} else if (e.getSource().equals(b_settings)) {
			// Not sure if we should have settings.
			game.state = GameState.Settings;
		}

	}

}