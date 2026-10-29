package pvz.logic.gameobjects;
import pvz.view.Messages;
import pvz.logic.Game;
import utils.Position;

public class Peashooter {
	public static final int COST = 50;
	private static final int DAMAGE = 1;
	private static final int INITIAL_HEALTH = 3;
	private int health;
	private  final Position position;
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
		return String.format(Messages.PEASHOOTER_ICON, health);
	}
	
	public boolean isInPosition(Position position) {
		return this.position.equals(position);
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
