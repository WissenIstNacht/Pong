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
public class Game implements KeyListener {

	private JFrame window;
	private JMenuBar jmb;

	// Different JPanels for different game screens
	private JPanel pnl_welcome, pnl_play, pnl_gameover;

	private Ball b;
	private Player p1, p2;

	private boolean exit;
	private boolean singlePlayer;

	private GameState state;

	public Game() {
		state = GameState.Welcome;

		/* Init Gui Components */
		pnl_welcome = new WelcomeScreen();
		pnl_play = new PlayScreen();
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
		window.addKeyListener(this);
		window.setVisible(true);
	}

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

	/**
	 * Decides in each frame whether the game is over.
	 * 
	 * @return true iff the game over condition is met
	 */
	private boolean detectGO() {
		return b.xPos <= 0 || b.xPos >= pnl_play.getWidth();
	}

	/**
	 * Transitions between Welcome and Running state of game.
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
		pnl_welcome.removeKeyListener(Game.this);
		window.remove(pnl_welcome);

		// add GS screen and listeners
		window.add(pnl_play, BorderLayout.CENTER);
		pnl_play.requestFocusInWindow();
		pnl_play.addKeyListener(Game.this);
		window.setVisible(true);

		// instantiate game objects
		p1 = new Player(pnl_play.getWidth(), pnl_play.getHeight(), 0);
		if (single) {
			singlePlayer = true;
			p2 = new Bot(pnl_play.getWidth(), pnl_play.getHeight());
		} else {
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
		pnl_play.removeKeyListener(Game.this);
		window.remove(pnl_play);

		// add GO screen and listeners
		window.add(pnl_gameover, BorderLayout.CENTER);
		pnl_gameover.requestFocusInWindow();
		pnl_gameover.addKeyListener(Game.this);
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
		pnl_gameover.removeKeyListener(Game.this);
		window.remove(pnl_gameover);

		// add GO screen and listeners
		window.add(pnl_welcome, BorderLayout.CENTER);
		pnl_welcome.requestFocusInWindow();
		pnl_welcome.addKeyListener(Game.this);
		window.setVisible(true);

		// once everything's set draw on GO screen canvas
		pnl_welcome.repaint();
	}
	
	private enum GameState {
		Welcome, Running, Paused, Settings, Over;
	}

	/*******************************************************************************/
	/* *LISTENERS* */
	/*******************************************************************************/
	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_W:
			p1.setMovement(-1);
			break;
		case KeyEvent.VK_S:
			p1.setMovement(1);
			break;
		case KeyEvent.VK_UP:
			p2.setMovement(-1);
			break;
		case KeyEvent.VK_DOWN:
			p2.setMovement(1);
			break;
		case KeyEvent.VK_P:
			if (state == GameState.Running)
				state = GameState.Paused;
			else if (state == GameState.Paused)
				state = GameState.Running;
			break;
		default:
			break;
		}
	}

	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_W:
			p1.setMovement(0);
			break;
		case KeyEvent.VK_S:
			p1.setMovement(0);
			break;
		case KeyEvent.VK_UP:
			p2.setMovement(0);
			break;
		case KeyEvent.VK_DOWN:
			p2.setMovement(0);
			break;
		default:
			break;
		}
	}

	public void keyTyped(KeyEvent ke) {
	}

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
	/* *INNER CLASS GameSCREEN* */
	/*******************************************************************************/

	/**
	 * Panel on which the actual playing is drawn.
	 * 
	 * This class's only job is to call the various object's draw methods (ball,
	 * player, etc) and pass them the graphics object g. In order to do so the
	 * class extends JPanel, which allows to override the paint component
	 * method.
	 * 
	 * @author WissenIstNacht
	 */
	class PlayScreen extends JPanel {
		private static final long serialVersionUID = 6698027035721355452L;
		
		JLabel lbl_paused;

		public PlayScreen() {
			setBackground(Color.BLACK);
			setLayout(new GridBagLayout());
			
			lbl_paused = new JLabel("PAUSED");
			lbl_paused.setFont(new Font("Verdana", Font.BOLD, 50));
			lbl_paused.setForeground(Color.WHITE);	// This is actually font color.
			lbl_paused.setVisible(false);
			add(lbl_paused);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(Color.BLACK);

			// Deal with Paused/unpaused game
			if (state == GameState.Paused) {
				lbl_paused.setVisible(true);
				setBackground(Color.DARK_GRAY);
				g.setColor(Color.LIGHT_GRAY);
			} else {
				lbl_paused.setVisible(false);
				g.setColor(Color.WHITE);
			}

			// draw game objects
			p1.draw(g);
			p2.draw(g);
			b.draw(g);
		}
	}

	/*******************************************************************************/
	/* *INNER CLASS WelcomeSCREEN* */
	/*******************************************************************************/

	/**
	 * Panel on which the welcome screen is drawn.
	 * 
	 * This class provide a canvas by extending a JPanel class. This screen
	 * contains a welcome msg and buttons to sart the game modes.
	 * 
	 * @author WissenIstNacht
	 */
	class WelcomeScreen extends JPanel implements ActionListener {
		private static final long serialVersionUID = 3732454082115626205L;

		private JButton b_singlePlayer, b_twoPlayer;
		private JButton b_settings;
		private JButton b_fill;

		private JLabel lbl_welcome;

		/**
		 * Initializes Welcome screen by adding layout and various components
		 */
		public WelcomeScreen() {
			setBackground(Color.DARK_GRAY);
			/* Layout */
			int noRow = 7;
			int noCol = 5;
			setLayout(new GridLayout(noRow, noCol, 0, 20));

			/* Components */
			b_singlePlayer = new JButton("Single Player");
			b_singlePlayer.addActionListener(this);
			b_twoPlayer = new JButton("Two Player");
			b_twoPlayer.addActionListener(this);
			b_settings = new JButton("Settings");
			b_settings.addActionListener(this);

			// Add invisible buttons to fill layout.
			for (int i = 0; i < noRow * noCol; i++) {
				b_fill = new JButton("Some" + i);

				switch (i) {
				case 17:
					add(b_singlePlayer);
					break;
				case 22:
					add(b_twoPlayer);
					break;
				case 27:
					add(b_settings);
					break;
				default:
					b_fill.setVisible(false);
					b_fill.setEnabled(false);
					add(b_fill);
					break;
				}
			}
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
				welcomeToRunning(true);
				state = GameState.Running;
			} else if (e.getSource().equals(b_twoPlayer)) {
				welcomeToRunning(false);
				state = GameState.Running;
			} else if (e.getSource().equals(b_settings)) {
				// Not sure if we should have settings.
				state = GameState.Settings;
			}

		}

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
