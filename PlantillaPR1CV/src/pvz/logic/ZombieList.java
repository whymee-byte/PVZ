package pvz.logic;

import java.util.Arrays;
import pvz.logic.gameobjects.Zombie;
import utils.Position;

public class ZombieList {
	private int numberOfZombies;
	private Zombie[]zombies;

	public ZombieList() {
		this.numberOfZombies = 0;
		this.zombies = new Zombie[0];
	}

	public int size() {
		return numberOfZombies;
	}

	public String iconInPosition(Position position) {
		int i = 0;
		while (i < numberOfZombies && !zombies[i].isInPosition(position)) i++;
		return zombies[i].getIcon();
	}

	public void add(Zombie z) {
		if(numberOfZombies == zombies.length) {
			zombies = Arrays.copyOf(zombies, numberOfZombies+1);
		}
		zombies[numberOfZombies] = z;
		numberOfZombies++;
	}

	public boolean damage(Position p, int damage) {
		int i = 0;
		while(i < numberOfZombies && !zombies[i].isHorizontallyAligned(p)) i++;
		if(i == numberOfZombies)return false;
		zombies[i].receiveAttack(damage);
		return true;
	}

	public boolean isEmpty(Position position) {
		int i = 0;
		while (i < numberOfZombies && !zombies[i].isInPosition(position)) i++;
		return i == numberOfZombies;
	}

	public void update() {
		for (int i = 0; i < numberOfZombies; i++) zombies[i].update();
	}

	public void removeDead() {
		for (int i = 0; i < numberOfZombies; i++) if (!zombies[i].isAlive()) {
			removeFromIndex(i);
			i--;
		}
	}

	public boolean anyInColumn(int column) {
		int i = 0;
		while(i < numberOfZombies && !zombies[i].isVerticallyAligned(new Position(0, column))) i++;
		return i != numberOfZombies;
	}

	private void removeFromIndex(int index) {
		int i = index;
		while (i < numberOfZombies - 1) zombies[i] = zombies[i+1];
		numberOfZombies--;
	}
}
