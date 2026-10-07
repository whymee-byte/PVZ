package pvz.logic.gameobjects;
import pvz.view.Messages;
import pvz.logic.Game;
import utils.Position;

public class Peashooter {
	private static final int COST = 50;
	private static final int DAMAGE = 1;
	private static final int INITIAL_HEALTH = 3;
	private int health;
	private Position position;
	private Game game;
	
	public Peashooter(Position position,Game game) {
		this.position = position;
		this.game = game;
		this.health=INITIAL_HEALTH;
	}
	public static String getDescription() {
		return String.format(Messages.PEASHOOTER_DESCRIPTION, COST, DAMAGE, INITIAL_HEALTH);
	}
	public String getIcon() {
		return "";
	}
	public boolean isInPosition(Position position) {
		return true;
	}
	public boolean isAlive() {
		return true;
	}
	public void update() {
		
	}
	private void shoot() {
		
	}
	public void receiveDamage(int damage) {
		
	}
	public String shortName() {
		return "P";
	}
	public String longName() {
		return "Peashooter";
	}
	
}
