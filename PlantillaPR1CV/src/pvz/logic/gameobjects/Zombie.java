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
		this.health = INITIAL_HEALTH;
		this.cyclesSinceLastMovement = 0;
		this.position = position;
		this.game = game;
	}

	public boolean isInPosition(Position position) {
		return position.column() == this.position.column() && position.row() == this.position.row();
	}

	public boolean isHorizontallyAligned(Position p) {
		return position.isHorizantallyAligned(p);
	}

	public boolean isVerticallyAligned(Position p) {
		return position.isVerticallyAligned(p);
	}

	public void receiveAttack(int damage) {
		this.health -= damage;
	}

	public void update() {
		cyclesSinceLastMovement++;
		if(canMove()) {
			if (cyclesSinceLastMovement >= MOVE_EVERY_CYCLES ) {
				cyclesSinceLastMovement = 0;
				move();
			}
		} else attack();
	}

	private boolean canMove() {
		return game.isEmpty(position.left());
	}

	private void move() {
		position = position.left();
	}

	public boolean isAlive() {
		return health > 0;
	}

	private void attack() {
		game.attackZombie(position.left(), DAMAGE);
	}
	
	public String getIcon() {
		return "Z[".concat(Integer.toString(health)).concat("]");
	}
}
