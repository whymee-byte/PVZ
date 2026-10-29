package pvz.logic;

import utils.Position;

public interface GameWorld {
	public static final int NUM_ROWS = 4;
	public static final int NUM_COLS = 8;
	
	boolean isInsideBoard(Position position);
	boolean isEmpty(Position position);
	public boolean addGameObject(String plantType, Position pos);
	public void attackPlant(Position p,int damage);
	public void attackZombie(Position p,int damage);
	public void generateCoins(int amount);
	public boolean checkGameObject(String objectName);
	public String positionToString(Position position);
	
}
