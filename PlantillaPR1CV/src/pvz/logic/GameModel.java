package pvz.logic;

import utils.Position;
import pvz.control.Level;
public interface GameModel {
	void reset();

	void reset(Level level, long seed);
	void update();
	void quit();
	public boolean hasGameFinished();
	public boolean addGameObject(String plantName, Position position,boolean cost);
	boolean addZombie(String name, Position position);
}
