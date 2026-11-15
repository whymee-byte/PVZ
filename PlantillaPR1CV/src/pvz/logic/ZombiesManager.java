package pvz.logic;

import java.util.Random;

import pvz.control.Level;
import pvz.logic.gameobjects.Zombie;
import pvz.logic.gameobjects.ZombieFactory;
import utils.Position;

public class ZombiesManager {

	private Game game;

	private Level level;

	private Random rand;

	private int remainingZombies;
	private int aliveZombies;

	public ZombiesManager(Game game, Level level, Random rand) {
		this.game = game;
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
		
		Zombie z = ZombieFactory.spawnZombie(rand,game,zombiePosition);

		boolean canAdd = remainingZombies > 0 && shouldAddZombie() && game.isEmpty(zombiePosition);

		if(canAdd) {	
			placeZombie(z);
			remainingZombies--;
		}
		return canAdd;
	}

	public boolean addZombie(String name, Position position) {
		if (!game.isInsideBoard(position) || !game.isEmpty(position)) {
			return false;
		}
		Zombie zombie = ZombieFactory.spawnZombie(name, game, position);
		if (zombie == null) {
			return false;
		}
		zombie.startMovementNextCycle();
		placeZombie(zombie);
		return true;
	}

	private void placeZombie(Zombie zombie) {
		game.addGameObject(zombie);
		aliveZombies++;
	}
	
	public void zombieDied() {
		aliveZombies--;
	}

	public int getRemainingZombies() {
		return remainingZombies;
	}

	public boolean doZombiesReachedTheHouse() {
		return game.hasObjectInColumn(-1);
	}

	public boolean allZombiesWereKilled() {
		return remainingZombies == 0 && aliveZombies == 0;
	}
}
