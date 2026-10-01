package pvz.logic.gameobjects;
import pvz.view.Messages;

public class Peashooter {
	private static final int COST = 50;
	private static final int DAMAGE = 1;
	private static final int INITIAL_HEALTH = 3;
	private int health;
	
	public static String getDescription() {
		return String.format(Messages.PEASHOOTER_DESCRIPTION, COST, DAMAGE, INITIAL_HEALTH);
	}
}
