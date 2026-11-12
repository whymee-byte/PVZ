package pvz.logic;

import utils.Position;

public interface GameModel {
	void reset();
	void update();
	void quit();
	public boolean playerWins();
	public boolean playerQuits();
	public boolean hasGameFinished();
	public boolean addGameObject(String plantName, Position position,boolean cost);
	
}
