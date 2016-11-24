package burlap.domain.singleagent.gridworld;

public class SalimGridWorldDomain extends GridWorldDomain {
	protected static int smallCaseDimension = 4;
	protected static int largeCaseDimension = 40;
	
	/**
	 * Constructs an empty map with deterministic transitions
	 * @param width width of the map
	 * @param height height of the map
	 */
	public SalimGridWorldDomain(int width, int height){
		super(width, height);
	}
	
	
	/**
	 * Constructs a deterministic world based on the provided map.
	 * @param map the first index is the x index, the second the y; 1 entries indicate a wall
	 */
	public SalimGridWorldDomain(int [][] map){
		super(map);
	}
	
	/**
	 * This is similar to the 4x4 case discussed in the lectures.
	 */
	public void setSmallCase() {
		this.width = smallCaseDimension;
		this.height = smallCaseDimension;
		this.makeEmptyMap();
		
		horizontalWall(1, 1, 1);		
	}
	
	public void setLargeCase() {
		this.width = largeCaseDimension;
		this.height = largeCaseDimension;
		this.makeEmptyMap();
		
		// Allow a straight path that almost gets to goal, but then blocks off.
		horizontalWall(1, this.width - 2, 1);
		verticalWall(1, this.height - 2, this.width - 2);
		horizontalWall(this.width - 1, this.width - 1, this.height - 2);
		
		// Insert an assortment of L-blocks such that it's difficult for the agent to go directly to the goal.
		int degrees = 0;
		for(int x = 1; x < this.width - 3; x += 3) {
			for(int y = 2; y < this.height - 3; y += 4) {
				insertLBlock(x, y, degrees);
				degrees = degrees >= 270 ? 0 : degrees + 90;
			}
			// Add more variety.
			//degrees = degrees >= 270 ? 0 : degrees + 90;
		}
		
		// Add one last horizontal at top such that the agent is forced to go around.
		horizontalWall(1, this.width - 2, this.height - 2);
	}

	protected void insertLBlock(int x, int y, int angle) {
		// Rotate on bottom left corner of the L.
		// |
		// |
		// |_
		switch(angle) {
			case 0:
				verticalWall(y, y + 2, x);
				horizontalWall(x + 1, x + 1, y);
				return;
			case 90:
				verticalWall(y + 1, y + 1, x);
				horizontalWall(x, x + 2, y);
				return;
			case 180:
				verticalWall(y, y + 2, x);
				horizontalWall(x - 1, x - 1, y);
				return;
			case 270:
				verticalWall(y - 1, y - 1, x);
				horizontalWall(x, x + 2, y);
				return;
			default:
				System.out.println("Invalid angle " + angle);
		}
	}
	
	public static int getDimension(boolean isSmallCase) {
		return isSmallCase ? smallCaseDimension : largeCaseDimension;
	}
}
