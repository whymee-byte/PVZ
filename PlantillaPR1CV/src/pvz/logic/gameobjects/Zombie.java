package pvz.logic.gameobjects;

public class Zombie {
	private final int INITIAL_HEALTH = 5;
	private final int DAMAGE = 1;
	private final int MOVE_EVERY_CYCLES = 2;
	private int health;
	private int cyclesSinceLastMovement;
}
