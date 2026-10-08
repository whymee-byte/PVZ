package pvz.logic.gameobjects;
import pvz.view.Messages;
import pvz.logic.Game;
import utils.Position;

public class Peashooter {
	public static final int COST = 50;
	private static final int DAMAGE = 1;
	private static final int INITIAL_HEALTH = 3;
	private int health;
	private Position position;
	private Game game;
	
	public Peashooter(Position position,Game game) {
		this.position = position;
		this.game = game;
		this.health = INITIAL_HEALTH;
	}
	
	public static String getDescription() {
		return String.format(Messages.PEASHOOTER_DESCRIPTION, COST, DAMAGE, INITIAL_HEALTH);
	}
	
	public String getIcon() {
		return "P[".concat(Integer.toString(health)).concat("]");
	}
	
	public boolean isInPosition(Position position) {
		return position.column() == this.position.column() && position.row() == this.position.row();
	}
	
	public boolean isAlive() {
		return health > 0;
	}
	
	public void update() {
		shoot();
	}
	
	private void shoot() {
		game.attackPlant(position, DAMAGE);
	}
	
	public void receiveDamage(int damage) {
		health -= damage;
	}
	
	public String shortName() {
		return "P";
	}
	
	public String longName() {
		return "Peashooter";
	}
}
