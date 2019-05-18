package improvedStructure;

public class Bot extends Player {

	public Bot(int panelWidth, int panelHeight) {
		super(panelWidth, panelHeight, 1);
	}

	public void update(Ball b) {
		yPos = (int) b.yPos;
	}
}
