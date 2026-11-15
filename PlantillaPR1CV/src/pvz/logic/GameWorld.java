package pvz.logic;

import pvz.logic.actions.GameAction;
import utils.Position;

public interface GameWorld {

	boolean isInsideBoard(Position position);
	boolean isEmpty(Position position);
	public boolean addGameObject(String plantType, Position pos);
	public void attackPlant(Position p,int damage);
//	public void attackZombie(Position p,int damage);
	void damageZombieAt(Position position, int damage);
	void damagePlantAt(Position position, int damage);
	public void generateCoins(int amount);
	public boolean checkGameObject(String objectName);
	public String positionToString(Position position);
	public GameItem getGameItemInPosition(Position position);
	
	void zombieDied();
	void pushAction(GameAction action);
}
