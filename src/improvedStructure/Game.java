package improvedStructure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import screens.*;

/** This is an implementation of the classic Pong game.
 * 
 * The game at the moment provides a game mode for single and two player. The game
 * starts with a welcome screen where the modes can be chosen. After a player loses
 * the game displays a game over screen. From there a player can go back to the
 * welcome screen. 
 * 
 * @author WissenIstNacht
 * @date 18.05.2019
 */
public class Game {

	private JFrame window;
	private JMenuBar jmb;

	// Different JPanels for different game screens
	private JPanel pnl_welcome, pnl_play, pnl_gameover;

	public Ball b;
	public Player p1;
	public Player p2;

	private boolean exit;
	private boolean singlePlayer;

	public GameState state;

	
	public Game() {
		state = GameState.Welcome;

		/* Init Gui Components */
		pnl_welcome = new WelcomeScreen(this);
		pnl_play = new PlayScreen(this);
		pnl_gameover = new GOScreen();

		jmb = new JMenuBar();
		jmb.setPreferredSize(new Dimension(800, 20));

		/* Init Frame */
		window = new JFrame("Pong");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		window.setSize(800, 600);

		window.add(jmb, BorderLayout.NORTH);
		window.add(pnl_welcome, BorderLayout.CENTER);
//		window.addKeyListener(this);
		window.setVisible(true);
	}

	
	/*******************************************************************************/
			/* Game Methods */
	/*******************************************************************************/
	
	/** This is the game loop.
	 * 
	 * This function runs until a player presses escape or the window is closed.
	 * 
	 * The two main purposes of this function is to keep the game running at a certain
	 * rate (set by sending the thread to sleep) and to update all the game elements
	 * as well as issuing redraw commands when the game is in Running state.
	 * 
	 */
	private void run() {
		while (!exit) {
			switch (state) {
			case Running:
				// update game state
				b.playerCollide(p1);
				b.playerCollide(p2);
				b.wallCollide(pnl_play.getHeight());

				b.update();
				p1.update();
				if (singlePlayer)
					((Bot) p2).update(b);
				else
					p2.update();

				// transition to GO screen if game over.
				if (detectGO()) {
					runningToOver();
					state = GameState.Over;
				}

				// update canvas
				pnl_play.repaint();
				break;
			case Paused:
				pnl_play.repaint();
				break;
			default:
				break;
			}

			// halt loop for some time. This leads to 50 FPS.
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

		
	/** Decides in each frame whether the game is over.
	 * 
	 * @return true iff the game over condition is met
	 */
	private boolean detectGO() {
		return b.xPos <= 0 || b.xPos >= pnl_play.getWidth();
	}

	
	/** Transitions between Welcome and Running state of game.
	 * 
	 * This happens when a button in the welcome screen fires. I.e., when a game
	 * mode has been chosen. Transitioning from Welcome to Running means
	 * replacing the panel and their listeners. Also, game objects are
	 * instantiated depending on game mode.
	 * 
	 * @param single - True iff game mode is single player.
	 */
	public void welcomeToRunning(boolean single) {
		// remove game screen and listeners
		window.remove(pnl_welcome);

		// add GS screen and listeners
		window.add(pnl_play, BorderLayout.CENTER);
		pnl_play.requestFocusInWindow();
		window.setVisible(true);

		// instantiate game objects
		p1 = new Player(pnl_play.getWidth(), pnl_play.getHeight(), 0);
		if (single) {
			singlePlayer = true;
			p2 = new Bot(pnl_play.getWidth(), pnl_play.getHeight());
		} else {
			singlePlayer = false;
			p2 = new Player(pnl_play.getWidth(), pnl_play.getHeight(), 1);
		}
		b = new Ball(pnl_play.getWidth() / 2, pnl_play.getHeight() / 2);

		// once everything's set draw on GO screen canvas
		pnl_play.repaint();
	}

	
	/** Transitions between Running and gameover state.
	 * 
	 * This happens when GO is detected. Transitioning from Running to GO means
	 * replacing the panel and their listeners.
	 */
	public void runningToOver() {
		// remove game screen and listeners
		window.remove(pnl_play);

		// add GO screen and listeners
		window.add(pnl_gameover, BorderLayout.CENTER);
		pnl_gameover.requestFocusInWindow();
		window.setVisible(true);

		// once everything's set draw on GO screen canvas
		pnl_gameover.repaint();
	}

	
	/** Transitions between GO and Welcome state of game.
	 * 
	 * This happens when GO is detected. Transitioning from Running to GO means
	 * replacing the panel and their listeners.
	 */
	public void overToWelcome() {
		// remove game screen and listeners
		window.remove(pnl_gameover);

		// add GO screen and listeners
		window.add(pnl_welcome, BorderLayout.CENTER);
		pnl_welcome.requestFocusInWindow();
		window.setVisible(true);

		// once everything's set draw on GO screen canvas
		pnl_welcome.repaint();
	}
	
	
	/*******************************************************************************/
	/*******************************************************************************/
	/* *MAIN* */
	/*******************************************************************************/

	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}

		/* Create and start game. Everything else is handled by game object */
		Game g = new Game();
		g.run();
	}



	/*******************************************************************************/
	/* *INNER CLASS GameOverScreen* */
	/*******************************************************************************/

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
	class GOScreen extends JPanel implements ActionListener {
		private static final long serialVersionUID = 3732454082115626205L;

		//TODO: Implement back to game directly.
		JButton b_back, b_fill;
		
		/**
		 * Initializes GO screen by adding layout and various components
		 */
		public GOScreen() {
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
				overToWelcome();
				state = GameState.Welcome;
			}
		}
	}
}
