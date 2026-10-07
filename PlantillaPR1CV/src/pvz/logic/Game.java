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

	public boolean isEmpty(Position p) {
		return zombieManager.isEmpty(p) && peashooterList.isEmpty(p) && sunflowerList.isEmpty(p);
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
		if (plantType.equals("peashooter") || plantType.equals("p"))
			peashooterList.add(new Peashooter(position, this));
		else if (plantType.equals("sunflower") || plantType.equals("s"))
			sunflowerList.add(new Sunflower(position, this));
	}
	
	public boolean isInsideBoard(Position p) {
		if (p.column() < 0 || p.column() >= NUM_COLS || p.row() < 0 || p.row() >= NUM_ROWS) return false;
		return true;
	}
}
