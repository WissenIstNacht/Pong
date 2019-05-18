import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class Game {
	Ball b;
	Player p1;
	Player p2;

	// frame dimension
	int frameHeight = 600;
	int frameWidth = 800;

	// Declaration GUI components
	JFrame jframe;
	DrawingPanel drawingPanel;

	public Game() {
		// create frame and set properties
		jframe = new JFrame("Pong");
		jframe.setSize(new Dimension(frameWidth, frameHeight));
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// initialize components and players
		initialiseComponents();

		jframe.setVisible(true);

		// set players start position.
		// Requires dimensions of panel, hence must be done after setvisible.
		int w = drawingPanel.getWidth();
		int h = drawingPanel.getHeight();
		p1.setStartPosition(5, h / 2);
		p2.setStartPosition(w - 15, h / 2);
	}

	public void initialiseComponents() {
		p1 = new Player();
		p2 = new Player();
		b = new Ball(p1, p2);

		/* set the frame's content pane to be the drawing panel  */
		drawingPanel = new DrawingPanel(b, p1, p2);
		jframe.setContentPane(drawingPanel);

	}

	public void playGame() {
		while (!drawingPanel.refresh) {
			drawingPanel.repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		jframe.dispose();
	}

	public static void main(String[] args) {
		while (true) {
			Game g = new Game();
			g.playGame();
		}
	}
}
