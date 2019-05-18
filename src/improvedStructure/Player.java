package improvedStructure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/** This class implements a player
 * 
 * A player is an entity represented by a slim vertical rectangle on the side of the 
 * screen ("Paddle"). A player can move up or down within the boundary of the game
 * window.
 * 
 * @author Valentin
 *
 */
public class Player {
	public int xPos, yPos;	
	public final boolean IS_LEFT;			

	private final Dimension FIELD_DIM;		// needed for keeping player in field
	private final Dimension PADDLE_DIM;

	private int isMoving;
	private final int Y_VEL = 4;
	
	/*******************************************************************************/
				/* Constructor */
	/*******************************************************************************/

	/** Initializes Player
	 * 
	 * @param xPos	left side of player paddle	
	 * @param yPos	upper sider of player paddle
	 * @param side	side on playing field.
	 */
	public Player(int pnl_width, int pnl_height, int side) {
		FIELD_DIM = new Dimension(pnl_width, pnl_height);
		PADDLE_DIM = new Dimension(pnl_width/40, pnl_height/7);   

		if(side == 0) {
			IS_LEFT = true;
			xPos = PADDLE_DIM.width;
		}else {
			IS_LEFT = false;
			xPos = FIELD_DIM.width - 2*PADDLE_DIM.width;
		}
		yPos = FIELD_DIM.height/2;
	}

	/*******************************************************************************/
					/* Methods */
	/*******************************************************************************/
	
	/** Sets player current movement behaviour
	 * 
	 * This is done in order to make movement smoother. This way the movement is set
	 * AND KEPT as soon as the player initiates it.
	 * 
	 * @param direction in {-1,0,1} for up, no movement, down
	 */
	public void setMovement(int direction) {
		isMoving = direction;
	}

	/** Updates the Player's state
	 * 
	 * The method does not allow to move outside the screen.
	 * 
	 * @param maxHeight boundary of playing field
	 */
	public void update() {
		int nextY = yPos + (isMoving * Y_VEL);
		

		// Only move if within window
		if (nextY >= 0 && nextY + PADDLE_DIM.height <= FIELD_DIM.height) {
			yPos = nextY;
		}
	}
	
	/**	Info about position of paddle boundary.
	 * 
	 * This function computes x positions of the east/west and y position of the 
	 * north/south side of the paddle. The info is used for collision detection.
	 * 
	 * @return int array {y,y,x} coord. value of paddle boundary for n, s ,w/e 
	 */
	public int[] getBounds() {
		if(IS_LEFT) {
			int[] res = {yPos, yPos + PADDLE_DIM.height, xPos + PADDLE_DIM.width};
			return res;
		}else {
			int[] res = {yPos, yPos + PADDLE_DIM.height, xPos};
			return res;
		}
			
	}
	
	/** Render the paddle at the current y position
	 */
	public void draw(Graphics g) {
		g.fillRect(xPos, yPos, PADDLE_DIM.width, PADDLE_DIM.height);
	}
}
