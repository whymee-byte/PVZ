package pvz.logic;

import java.util.Random;

import pvz.control.Level;
import pvz.logic.gameobjects.Zombie;
import pvz.logic.gameobjects.ZombieFactory;
import utils.Position;

public class ZombiesManager {

	private GameWorld gw;

	private Level level;

	private Random rand;

	private int remainingZombies;
	private int aliveZombies;

	public ZombiesManager(GameWorld gw, Level level, Random rand) {
		this.gw = gw;
		this.level = level;
		this.rand = rand;
		this.remainingZombies = level.getNumberOfZombies();
		this.aliveZombies = 0;
	}

	private boolean shouldAddZombie() {
		return rand.nextDouble() < level.getZombieFrequency();
	}
	
	private int randomZombieRow() {
		return rand.nextInt(Game.NUM_ROWS);
	}
	
	public boolean addZombie() {
		int row = randomZombieRow();
		return addZombie(row);
	}

	public boolean addZombie(int row) {
		Position zombiePosition = Game.newZombiePosition(row);
		
		Zombie z = ZombieFactory.spawnZombie(rand,gw,zombiePosition);

		boolean canAdd = remainingZombies > 0 && shouldAddZombie();

		if(canAdd) {	
			placeZombie(z);
			remainingZombies--;
		}
		return canAdd;
	}

	public boolean addZombie(String name, Position position) {
		if (!gw.isInsideBoard(position) || !gw.isEmpty(position)) {
			return false;
		}
		Zombie zombie = ZombieFactory.spawnZombie(name, gw, position);
		if (zombie == null) {
			return false;
		}
		placeZombie(zombie);
		return true;
	}

	private void placeZombie(Zombie zombie) {
		gw.addGameObject(zombie);
		aliveZombies++;
	}
	
	public void zombieDied() {
		aliveZombies--;
	}

	public int getRemainingZombies() {
		return remainingZombies;
	}

	public boolean doZombiesReachedTheHouse() {
		return gw.hasObjectInColumn(-1);
	}

	public boolean allZombiesWereKilled() {
		return remainingZombies == 0 && aliveZombies == 0;
	}
}
