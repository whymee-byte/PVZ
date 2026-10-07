package pvz.logic;
import java.util.Random;

import pvz.control.Level;
import utils.Position;
import pvz.logic.gameobjects.*;

public class Game {
	public final static int NUM_ROWS = 4;
	public final static int NUM_COLS = 8;
	public final static int INITIAL_COINS=50;
	
	private int cycles;
	private int coins;
	private Long longSeed;
	private Random rand;
	private boolean playerQuit;
	private Level level;
	private ZombiesManager zombieManager;
	private PeashooterList peashooterList;
	private SunflowerList sunflowerList;
	
	public Game(Long seed, Level level){
		this.longSeed=seed;
		this.cycles=0;
		this.coins=INITIAL_COINS;
		this.rand = new Random(seed);
		this.level=level;
		this.peashooterList = new PeashooterList();
		this.sunflowerList = new SunflowerList();
	}
	
	public String positionToString(Position position) {
		return position.toString();
	}
	
	public static Position newZombiePosition(int row) {
		return new Position(row, NUM_COLS - 1);
	}
//	todavia sin hacer 
	public boolean isEmpty(Position p) {
		return true;
	}
	
	public int getCycles() {
		return cycles;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public int getRemainingZombies() {
		return ZombiesManager.getRemainingZombies();
	}

	public void addGameObject(String plantType, Position position) {
		if (plantType.equals("peashooter"))
			peashooterList.add(new Peashooter(position, this));
		else if (plantType.equals("sunflower"))
			sunflowerList.add(new Sunflower(position, this));
	}
}
