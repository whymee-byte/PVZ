package pvz.logic.gameobjects;
import pvz.view.Messages;

public class Sunflower {
	private static final int COST = 20;
	private static final int DAMAGE = 0;
	private static final int INITIAL_HEALTH = 1;
	private int health;
	
	public static String getDescription() {
		return String.format(Messages.PEASHOOTER_DESCRIPTION, COST, DAMAGE, INITIAL_HEALTH);
	}
}
