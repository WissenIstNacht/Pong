import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel implements KeyListener {
	
	public boolean refresh;
	public boolean gameOver;
	private static final long serialVersionUID = 8642494139633759629L;
	
	Player p1;
	Player p2;
	Ball b;
	
	
	public DrawingPanel(Ball b_, Player p1_, Player p2_){
		p1 = p1_;
		p2 = p2_;
		b = b_;
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
	}
	
	
	/** CONSTRUCTOR
	 * 
	 */
	public DrawingPanel() {
		setBackground(Color.BLACK);
	}
	
	protected void paintComponent(Graphics g) {
		Graphics g2 = g;
		super.paintComponent(g2);
		
		if(!b.playerCollision){
			int w = getWidth();
			int h = getHeight();
			
			b.updateBall(w, h);
			b.renderBall(g2);
			
			p1.renderPlayer(g2);
			p2.renderPlayer(g2);
		}else{
			String fontName = g2.getFont().getName();
			g2.setColor(Color.red.brighter());
			g2.setFont(new Font(fontName, Font.BOLD, 30));
			g2.drawString("GAME OVER", 325, 100);
			g2.drawString("To play another game press 'r'", 225, 150);
			
			b.renderBall(g2);
			
			p1.renderPlayer(g2);
			p2.renderPlayer(g2);
		}
	}

	
	/*******************************************************************************/
	/*               *Interfaces*                                           	   */
	/*******************************************************************************/

	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		
		switch (key) {
		case 'w':
			p1.moveDown();
			break;
		case 's':
			p1.moveUp(getHeight());
			break;
		case 'i':
			p2.moveDown();
			break;
		case 'k':
			p2.moveUp(getHeight());
			break;
		default:
			break;
		}
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == 'r'){
			refresh = true;
		}
	}
}
