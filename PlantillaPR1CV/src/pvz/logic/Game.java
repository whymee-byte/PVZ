package pvz.logic;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

import pvz.control.Level;
import pvz.logic.actions.GameAction;
import pvz.logic.gameobjects.Plant;
import pvz.logic.gameobjects.PlantFactory;
import utils.Position;

public class Game implements GameStatus, GameWorld, GameModel {
	private static final int INITIAL_COINS = 50;
	private static final int SUN_VALUE = 10;

	private boolean catchalreadyExecuted;
	private int cycles;
	private int coins;
	private Long longSeed;
	private Random rand;
	private boolean playerQuit;
	private Level level;
	private ZombiesManager zombieManager;
	private SunManager sunManager;
	private GameObjectContainer container;
	private Deque<GameAction> actions;
	
	public Game(Long seed, Level level){
		reset(level, seed);
	}
	
	public String positionToString(Position position) {
		if (!container.isEmpty(position)) return container.positionToString(position);
		return "";
	}
	
	public boolean hasGameFinished() {
		if(playerQuits()) {
			return true;
		} else if(playerWins()) {
			return true;
		} else if (zombiesWin()){
			return true;
		}
		return false;
	}
	
	public boolean zombiesWin() {
		return zombieManager.doZombiesReachedTheHouse();
	}
	
	public boolean playerWins() {
		return zombieManager.allZombiesWereKilled();
	}
	
	public boolean playerQuits() {
		return playerQuit;
	}
	
	public void quit() {
		playerQuit=true;
	}
	
	public void update() {
		executePendingActions();
		zombieManager.addZombie();
		container.update();
		sunManager.update();
		//si hay un muerto extra, se apila dentro
		do {
			executePendingActions();
		} while (container.removeDead());
		catchalreadyExecuted=false;
		cycles++;
	}

	@Override
	public void pushAction(GameAction action) {
		actions.addLast(action);
	}

	private void executePendingActions() {
		while (!actions.isEmpty()) {
			actions.removeLast().execute(this);
		}
	}

	@Override
	public boolean addZombie(String name, Position position) {
		return zombieManager.addZombie(name, position);
	}
	
	public void reset() {
		reset(this.level,this.longSeed);
	}
	
	public void generateSuns() {
		sunManager.addSun();
	}
	
	private void buyWithCoins(int cost) {
		coins -= cost;
	}
	
	
	public void attackPlant(Position p,int damage) {
		container.attackPlant(p, damage, NUM_COLS);
	}
	
	public static Position newZombiePosition(int row) {
		return new Position(row, NUM_COLS);
	}

	public boolean isEmpty(Position p) {
		return container.isEmpty(p);
	}
	
	public int getCycles() {
		return cycles;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public int getRemainingZombies() {
		return zombieManager.getRemainingZombies();
	}
	
	@Override
	public boolean addGameObject(String plantName, Position position,boolean cheat) {
	    if (!isInsideBoard(position) || !isEmpty(position)) {
	        return false;
	    }

	    Plant plant = PlantFactory.spawnPlant(plantName, this, position);
	    if (plant == null ) {
	        return false;
	    }
	    if(cheat) {
		    addGameObject(plant);
		    return true;
	    } else if( getCoins() < plant.getCost()) {
		    return false;
	    } else {
	    	buyWithCoins(plant.getCost());
	    	addGameObject(plant);
		    return true;
	    }
	}

	public boolean addGameObject(GameObject object) {
		return container.add(object);
	}
	
	public boolean isInsideBoard(Position p) {
		if (p.column() < 0 || p.column() >= NUM_COLS || p.row() < 0 || p.row() >= NUM_ROWS) return false;
		return true;
	}
	
	public GameItem getGameItemInPosition(Position position) {
		return container.getGameItemInPosition(position);
	}

	@Override
	public void damagePlantAt(Position position, int damage) {
		container.attackZombie(position, damage);
	}

	@Override
	public void damageZombieAt(Position position, int damage) {
		container.damageZombieAt(position, damage);
	}

	@Override
	public void reset(Level level, long seed) {
		this.level=level;
		this.longSeed=seed;
		this.cycles=0;
		this.coins=INITIAL_COINS;
		this.playerQuit=false;
		this.rand=new Random(seed);
		this.container=new GameObjectContainer();
		this.actions = new ArrayDeque<>();
		this.zombieManager=new ZombiesManager(this,level,rand);
		this.sunManager=new SunManager(rand,this);
		this.catchalreadyExecuted=false;
	}
	
	public void zombieDied() {
		zombieManager.zombieDied();
	}
	
	public boolean hasObjectInColumn(int col) {
		return container.hasObjectInColumn(col);
	}
	
	public boolean catchObject(Position position) {
		if(catchalreadyExecuted) return false;
		
		if(container.catchObject(position) > 0) {
			catchalreadyExecuted = true;
			return true;
		}
		return false;
	}
	
	public void collectSuns() {
		coins += SUN_VALUE;
	}
	
	@Override
	public boolean addGameObject(String plantType, Position pos) {
		// TODO Auto-generated method stub
		//return addGameObject(new());
		return true;
	}

}
