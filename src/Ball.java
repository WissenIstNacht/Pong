import java.awt.Color;
import java.awt.Graphics;

public class Ball {
	
	boolean playerCollision;
	Angle angularDirection;
	int xPos;
	int yPos;
	
	Player p1, p2;
	
	public Ball(Player p1_, Player p2_){
		angularDirection = new Angle();
		xPos = 300;
		yPos = 300;
		p1 = p1_;
		p2 = p2_;
		playerCollision = false;
	}
	
	
	public void updateBall(int paneWidth, int paneHeight){
		checkWallCollsion(paneWidth, paneHeight);
		checkBallPlayerCollision(paneWidth, paneHeight);
		
		
		xPos += angularDirection.xProjection();
		yPos += angularDirection.yProjection();
	}
	
	private void checkBallPlayerCollision(int paneWidth, int paneHeight) {
		int newXPos = xPos + angularDirection.xProjection();
		
		
		if(newXPos + 40 > paneWidth){
			if(yPos+15 >= p2.position && yPos + 15 <= p2.position + p2.blockHeight){
				angularDirection.mirrorY();
			}else{
				playerCollision = true;
			}
		}
		
		if(newXPos < 10){
			if(yPos+15 >= p1.position && yPos + 15 <= p1.position + p1.blockHeight){
				angularDirection.mirrorY();
			}else{
				playerCollision = true;
			}
		}
	}

	public void checkWallCollsion(int paneWidth, int paneHeight){
		//AT THE MOMENT GRAPHICS ARE NOT ROTATED
		//i.e. "up" really means down and the ceiling really is the bottom of the pane
		int newYPos = yPos + angularDirection.yProjection();
		
		//collision with ceiling, ground, left right
		if(newYPos + 30 > paneHeight){
			angularDirection.mirrorX();
		}
		if(newYPos < 0){
			angularDirection.mirrorX();
		}
	}
	
	public void renderBall(Graphics g){
		g.setColor(Color.yellow);
		g.fillOval(xPos, yPos, 30, 30);
	}
	
	class Angle {
		double radianValue;
		private final int multiplier = 5;
		
		public Angle(){
			int factor = (int) (Math.random()*12);
			radianValue = factor*Math.PI*2/12;
			System.out.println(radianValue);
		}
		
		public Angle(double radian){
			radianValue = radian;
		}
		
		public Angle(int degree){
			radianValue = 2*Math.PI*degree/360;
		}
		
		public int xProjection(){
			return (int) Math.round(multiplier*Math.cos(radianValue));
		}
		
		public int yProjection(){
			return (int) Math.round(multiplier*Math.sin(radianValue));
		}
		
		/**
		 * Mirrors the angle along along the x axis
		 */
		public void mirrorX(){
			radianValue *= -1;
		}

		/**
		 * Mirrors the angle along along the y axis
		 */
		public void mirrorY(){
			radianValue = Math.PI - radianValue;
		}
	}
}
