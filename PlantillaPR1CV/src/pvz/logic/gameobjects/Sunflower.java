package pvz.logic.gameobjects;
import pvz.view.Messages;
import utils.Position;
import pvz.logic.Game;

public class Sunflower {
	private static final int COST = 20;
	private static final int DAMAGE = 0;
	private static final int INITIAL_HEALTH = 1;
	private int health;
	private Position position;
	private Game game;
	
	public Sunflower(Position position, Game g) {
		this.position = position;
		this.game = g;
		this.health = INITIAL_HEALTH;
	}
	
	public static String getDescription() {
		return String.format(Messages.PEASHOOTER_DESCRIPTION, COST, DAMAGE, INITIAL_HEALTH);
	}
}
