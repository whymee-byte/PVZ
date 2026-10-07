package pvz.logic;
import pvz.logic.gameobjects.Zombie;
import utils.Position;

public class ZombieList {

	private int numberOfZombies;
	private Zombie[]zombies;

	public ZombieList() {

	}

	public int size() {
		return numberOfZombies;
	}

	public String iconInPosition(Position p) {
		return ""; //Placeholder
	}

	public void add(Zombie z) {
		
	}

	public boolean damage(Position p, int damage) {
		return false; //Placeholder
	}

	public boolean isEmpty(Position p) {
		return false; //Placeholder
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
