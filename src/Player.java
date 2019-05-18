import java.awt.Color;
import java.awt.Graphics;

public class Player {
	private int speed = 15;
	public int position;		//vertical position along wall
	private int side;			//horizontal position, which is either 0 or width.
	private final int blockWidth = 10;		//dimensions of block
	public final int blockHeight = 75;
	
	
	public Player(){
	}
	
	public void renderPlayer(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(side, position, blockWidth, blockHeight);
	}

	public void setStartPosition(int w, int h) {
		side = w;
		position = h;
	}
	
	public void moveUp(int maxHeight) {
		if(position + speed <= maxHeight - blockHeight){
			position += speed;
		}
	}

	public void moveDown() {
		if(position - speed >= 0){
			position -= speed;
		}
	}
}
