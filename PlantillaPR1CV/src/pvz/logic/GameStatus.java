package pvz.logic;

import utils.Position;

public interface GameStatus {
	public int getRemainingZombies();
	public int getCoins();
	public int getCycles();
	public boolean isEmpty(Position p);
	
}
