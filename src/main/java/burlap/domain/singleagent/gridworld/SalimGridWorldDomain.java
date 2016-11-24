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
		
		horizontalWall(0, 0, 5);
		horizontalWall(2, 4, 5);
		horizontalWall(6, 7, 4);
		horizontalWall(9, 10, 4);

		horizontalWall(0, 0, 10);
		horizontalWall(2, 4, 10);
		horizontalWall(6, 7, 9);
		horizontalWall(9, 10, 9);
		
		verticalWall(0, 0, 5);
		verticalWall(2, 7, 5);
		verticalWall(9, 10, 5);
		
		verticalWall(0, 0, 10);
		verticalWall(2, 7, 10);
		verticalWall(9, 10, 10);
	}
	
	public static int getDimension(boolean isSmallCase) {
		return isSmallCase ? smallCaseDimension : largeCaseDimension;
	}
}
