package improvedStructure;

import java.awt.Color;
import java.awt.Graphics;

/** This class implements the Ball
 * 
 * The game ball is a circle moving around the screen with a constant speed and 
 * direction. It physically interacts with it's environment. This is handled by 
 * collision detection functions. 
 * 
 * @author Valentin
 *
 */
public class Ball {
	double angle;
	double xPos, yPos;
	double xVel, yVel;
	double speed;		// This is 2 norm of vel vector [xVel, yVel]
	private int radius;
	
	/*******************************************************************************/
	/*               *consructor*                                		           */
	/*******************************************************************************/

	public Ball(double x, double y) {
		xPos = x;
		yPos = y;
		speed = 5;
		radius = 20;
		
		angle = Math.random()*Math.PI*2;	//at game start direction is random.
		xVel = speed * Math.cos(angle);
		yVel = speed * -Math.sin(angle);
	}

	
	/*******************************************************************************/
					/*state Functions*/
	/*******************************************************************************/

	public void update() {
		xPos += xVel; 
		yPos += yVel; 
	}
	
	public boolean playerCollide(Player p) {
		int[] bounds_p1 = p.getBounds();
		
		/* Check for collisions with player 1 */
		boolean yCondition = yPos >= bounds_p1[0] && yPos <= bounds_p1[1];
		boolean xCondition;

		if(p.IS_LEFT) 
			xCondition = xPos < bounds_p1[2];
		else 
			xCondition = xPos + radius > p.xPos;
		
		if(yCondition && xCondition) {
			/* mirror trajectory about x axis*/
			angle = Math.PI - angle;
			xVel = speed * Math.cos(angle);
			yVel = speed * -Math.sin(angle);
			return true;
		}
		return false;
	}
	
	public boolean wallCollide(int height) {
		if(yPos <= 0) {
			/* mirror trajectory about y axis*/
			angle = 2*Math.PI - angle;
			angle += Math.random()/5 - 0.1;
			xVel = speed * Math.cos(angle);
			yVel = speed * -Math.sin(angle);
			yPos = 0;
			return true;
		}else if (yPos + radius >= height) {
			/* mirror trajectory about y axis*/
			angle = 2*Math.PI - angle;
			angle += Math.random()/5 - 0.1;
			xVel = speed * Math.cos(angle);
			yVel = speed * -Math.sin(angle);
			yPos = height- radius;
			return true;
		}
		return false;
	}

	public void draw(Graphics g) {
		g.fillOval((int) xPos, (int) yPos, radius, radius);
	}
}
