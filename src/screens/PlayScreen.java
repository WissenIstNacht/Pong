package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import improvedStructure.Game;
import improvedStructure.GameState;

/* Panel on which the actual playing is drawn.
 * 
 * This class's only job is to call the various object's draw methods (ball,
 * player, etc) and pass them the graphics object g. In order to do so the
 * class extends JPanel, which allows to override the paint component
 * method.
 * 
 * @author WissenIstNacht
 */
public class PlayScreen extends JPanel implements KeyListener{
	private static final long serialVersionUID = 6698027035721355452L;
	
	private Game game;
	
	JLabel lbl_paused;

	public PlayScreen(Game g) {
		game = g;
		setBackground(Color.BLACK);
		setLayout(new GridBagLayout());
		this.addKeyListener(this);
		
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
		if (game.state == GameState.Paused) {
			lbl_paused.setVisible(true);
			setBackground(Color.DARK_GRAY);
			g.setColor(Color.LIGHT_GRAY);
		} else {
			lbl_paused.setVisible(false);
			g.setColor(Color.WHITE);
		}

		// draw game objects
		game.p1.draw(g);
		game.p2.draw(g);
		game.b.draw(g);
	}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_W:
			game.p1.setMovement(-1);
			break;
		case KeyEvent.VK_S:
			game.p1.setMovement(1);
			break;
		case KeyEvent.VK_UP:
			game.p2.setMovement(-1);
			break;
		case KeyEvent.VK_DOWN:
			game.p2.setMovement(1);
			break;
		case KeyEvent.VK_P:
			if (game.state == GameState.Running)
				game.state = GameState.Paused;
			else if (game.state == GameState.Paused)
				game.state = GameState.Running;
			break;
		default:
			break;
		}
	}

	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_W:
			game.p1.setMovement(0);
			break;
		case KeyEvent.VK_S:
			game.p1.setMovement(0);
			break;
		case KeyEvent.VK_UP:
			game.p2.setMovement(0);
			break;
		case KeyEvent.VK_DOWN:
			game.p2.setMovement(0);
			break;
		default:
			break;
		}
	}

	public void keyTyped(KeyEvent ke) {
	}
}
