package pvz.logic;
import java.util.Random;

import pvz.control.Level;
import utils.Position;
import pvz.logic.gameobjects.*;
import pvz.logic.*;

public class Game {
	public final static int NUM_ROWS = 4;
	public final static int NUM_COLS = 8;
	public final static int INITIAL_COINS = 50;
	
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
		this.longSeed = seed;
		this.cycles = 0;
		this.coins = INITIAL_COINS;
		this.rand = new Random(seed);
		this.level = level;
		this.peashooterList = new PeashooterList();
		this.sunflowerList = new SunflowerList();
		this.zombieManager = new ZombiesManager(this,level,rand);
		this.playerQuit = false;
	}
	
	public String positionToString(Position position) {
		if (!sunflowerList.isEmpty(position)) return sunflowerList.iconInPosition(position);
		if (!peashooterList.isEmpty(position)) return peashooterList.iconInPosition(position);
		if (!zombieManager.isEmpty(position)) return zombieManager.iconInPosition(position);
		return "";
	}
	
	public boolean checkGameObject(String objectName) {
		return true; //Placeholder
	}
	
	public boolean hasGameFinished() {
		return zombieManager.allZombiesWereKilled() || zombieManager.allZombiesWereKilled();
	}
	
	public boolean playerWins() {
		return true; //Placeholder
	}
	
	public boolean playerQuits() {
		return true; //Placeholder
	}
	
	public void quit() {
		
	}
	
	public void update() {
		zombieManager.addZombie();
		sunflowerList.update();
		peashooterList.update();
		zombieManager.removeDead();
		zombieManager.update();
		sunflowerList.removeDead();
		peashooterList.removeDead();
	}
	
	public void reset() {
		cycles = 0;
		coins = INITIAL_COINS;
		rand = new Random(this.longSeed);
		peashooterList = new PeashooterList();
		sunflowerList = new SunflowerList();
		zombieManager = new ZombiesManager(this,level,rand);
	}
	
	public void generateCoins(int amount) {
		coins += amount;
	}
	
	private void buyWithCoins(int cost) {
		coins -= cost;
	}
	
	public void attackZombie(Position p,int damage) {
		if (!sunflowerList.isEmpty(p)) {
			sunflowerList.receiveDamage(p,damage);
		}else if(!peashooterList.isEmpty(p)) {
			peashooterList.receiveDamage(p,damage);
		}
	}
	
	public void attackPlant(Position p,int damage) {
		zombieManager.damageZombie(p, damage);
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
		if (plantType.equals("peashooter") || plantType.equals("p")) {
			peashooterList.add(new Peashooter(position, this));
			buyWithCoins(Peashooter.COST);
		}
		else if (plantType.equals("sunflower") || plantType.equals("s")) {
			sunflowerList.add(new Sunflower(position, this));
			buyWithCoins(Sunflower.COST);
		}
	}
	
	public boolean isInsideBoard(Position p) {
		if (p.column() < 0 || p.column() >= NUM_COLS || p.row() < 0 || p.row() >= NUM_ROWS) return false;
		return true;
	}
}
