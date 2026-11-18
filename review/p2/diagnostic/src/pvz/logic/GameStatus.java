package pvz.logic;

import utils.Position;

public interface GameStatus {
	public final static int NUM_ROWS = 4;
	public final static int NUM_COLS = 8;
	public int getRemainingZombies();
	public int getCoins();
	public int getCycles();
	public boolean isEmpty(Position p);
	public String positionToString(Position position);
	public boolean zombiesWin();
	public boolean playerWins();
	public boolean playerQuits();
	public int generatedSuns();
	public int getCaughtSuns() ;
	
}
