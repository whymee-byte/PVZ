package pvz.logic;
import java.util.Arrays;

import pvz.logic.gameobjects.Zombie;
import utils.Position;

public class ZombieList {

	private int numberOfZombies;
	private Zombie[]zombies;

	public ZombieList() {
		this.numberOfZombies=0;
		this.zombies=new Zombie[0];
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
		if(numberOfZombies==zombies.length) {
			zombies=Arrays.copyOf(zombies, numberOfZombies+1);
		}
		zombies[numberOfZombies]=z;
		numberOfZombies++;
		
	}

	public boolean damage(Position p, int damage) {
		return false; //Placeholder
	}

	public boolean isEmpty(Position position) {
		int i = 0;
		while (i < numberOfZombies && !zombies[i].isInPosition(position)) i++;
		return i == numberOfZombies;
	}

	public void update() {

	}

	public void removeDead() {
		
	}

	public boolean anyInColumn(int column) {
		return false; //Placeholder
	}

	private void removeFromIndex(int index) {

	}
}
