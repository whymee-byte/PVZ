package pvz.logic;

import utils.Position;
import pvz.control.Level;
public interface GameModel {
	void reset();

	// Dentro de GameModel:
	void reset(Level level, long seed);
	void update();
	void quit();
	public boolean playerWins();
	public boolean playerQuits();
	public boolean hasGameFinished();
	public boolean addGameObject(String plantName, Position position,boolean cost);
	
}
