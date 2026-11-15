package pvz.logic;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

import pvz.control.Level;
import pvz.logic.actions.GameAction;
import pvz.logic.gameobjects.Plant;
import pvz.logic.gameobjects.PlantFactory;
import utils.Position;
//import pvz.logic.PeashooterList;
//import pvz.logic.SunflowerList;


public class Game implements GameStatus, GameWorld, GameModel {

	public final static int INITIAL_COINS = 50;
	
	private int cycles;
	private int coins;
	private Long longSeed;
	private Random rand;
	private boolean playerQuit;
	private Level level;
	private ZombiesManager zombieManager; //Esto habrá que moverlo supongo pero no tengo claro a dónde
	private GameObjectContainer container;
	private Deque<GameAction> actions;
	//private PeashooterList peashooterList;
	//private SunflowerList sunflowerList;
	
	public Game(Long seed, Level level){
		reset(level, seed);
	}
	
	public String positionToString(Position position) {
		if (!container.isEmpty(position)) return container.iconInPosition(position);
		//if (!sunflowerList.isEmpty(position)) return sunflowerList.iconInPosition(position);
		//if (!peashooterList.isEmpty(position)) return peashooterList.iconInPosition(position);
		//if (!zombieManager.isEmpty(position)) return zombieManager.iconInPosition(position);
		return "";
	}
	
	public boolean checkGameObject(String objectName) {
		//Necesita actualizar. Comprobar en container, no con nombres pre-escritos
		if(objectName.equals("p")||objectName.equals("peashooter")||
				objectName.equals("s")||objectName.equals("sunflower")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean hasGameFinished() {
		if(playerQuits()) {
			return true;
		}else if(playerWins()) {
			return true;
		}else if (zombiesWin()){
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
		//si hay un muerto extra, se apila dentro
		do {
			executePendingActions();
		} while (container.removeDead());
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
	
	public void generateCoins(int amount) {
		coins += amount;
	}
	
	private void buyWithCoins(int cost) {
		coins -= cost;
	}
	
	
	public void attackPlant(Position p,int damage) {
		//zombieManager.damageZombie(p, damage);
		container.attackPlant(p, damage, NUM_COLS);
	}
	
	public static Position newZombiePosition(int row) {
		return new Position(row, NUM_COLS);
	}

	public boolean isEmpty(Position p) {
		return container.isEmpty(p);
		//return zombieManager.isEmpty(p) && container.isEmpty(p);
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
	    }else if( getCoins() < plant.getCost()) {
		    return false;
	    }else {
	    	buyWithCoins(plant.getCost());
	    	addGameObject(plant);
		    return true;
	    }
	}
	/*public boolean addGameObject(String plantType, Position pos) {
		if(!checkGameObject(plantType)) {
			return false;
		}
		if (!isInsideBoard(pos) || !isEmpty(pos)) {
			return false;
		}
		if (plantType.equals("peashooter") || plantType.equals("p")) {
			if (getCoins() < Peashooter.COST) {
				return false;
			}
			peashooterList.add(new Peashooter(pos, this));
			buyWithCoins(Peashooter.COST);
		}
		else if (plantType.equals("sunflower") || plantType.equals("s")) {
			if (getCoins() < Sunflower.COST) {
				return false;
			}
			sunflowerList.add(new Sunflower(pos, this));
			buyWithCoins(Sunflower.COST);
		}
		return true;
	}*/
	public void addGameObject(GameObject object) {
		container.add(object);
	}
	
	public boolean isInsideBoard(Position p) {
		if (p.column() < 0 || p.column() >= NUM_COLS || p.row() < 0 || p.row() >= NUM_ROWS) return false;
		return true;
	}

	@Override
	public boolean addGameObject(String plantType, Position pos) {
		// TODO Auto-generated method stub
		return false;
	}
	public GameItem getGameItemInPosition(Position position) {
		return container.getGameItemInPosition(position);
		
	}
//	@Override
//	public void attackZombie(Position p, int damage) {
//		container.attackZombie(p, damage);
//	}

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
		
	}
	
	public void zombieDied() {
		zombieManager.zombieDied();
	}
	
	public boolean hasObjectInColumn(int col) {
		return container.hasObjectInColumn(col);
	}
}
