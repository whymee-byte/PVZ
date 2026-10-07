package pvz.logic.gameobjects;
import utils.Position;
import pvz.logic.Game;


public class Zombie {
	private final int INITIAL_HEALTH = 5;
	private final int DAMAGE = 1;
	private final int MOVE_EVERY_CYCLES = 2;
	private int health;
	private int cyclesSinceLastMovement;
	private Position position;
	private Game game;
	

	public Zombie(Position position, Game game) {

	}

	public boolean isInPosition(Position position) {
		return position.column() == this.position.column() && position.row() == this.position.row();
	}

	public boolean isHorizontallyAligned(Position p) {
		return false; //Placeholder
	}

	public boolean isVerticallyAligned(Position p) {
		return false; //Placeholder
	}

	public void receiveAttack(int damage) {

	}

	public void update() {

	}

	private boolean canMove() {
		return false; //Placeholder
	}

	private void move() {

	}

	public boolean isAlive() {
		return false; //Placeholder
	}

	private void attack() {

	}
	
	public String getIcon() {
		return "Z[".concat(Integer.toString(health)).concat("]");
	}
}
