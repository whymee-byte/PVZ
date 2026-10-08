package pvz.logic.gameobjects;
import pvz.view.Messages;
import utils.Position;
import pvz.logic.Game;

public class Sunflower {
	private static final int COST = 20;
	private static final int DAMAGE = 0;
	private static final int INITIAL_HEALTH = 1;
	private static final int GENERATED_SUN_COINS = 10;
	private static final int COOLDOWN = 3;
	private int health;
	private Position position;
	private Game game;
	private int cyclesSinceLastCoinGeneration;
	
	public Sunflower(Position position, Game g) {
		this.position = position;
		this.game = g;
		this.health = INITIAL_HEALTH;
		this.cyclesSinceLastCoinGeneration = 0;
	}
	
	public static String getDescription() {
		return String.format(Messages.PEASHOOTER_DESCRIPTION, COST, DAMAGE, INITIAL_HEALTH);
	}
	
	public boolean isInPosition(Position position) {
		return position.column() == this.position.column() && position.row() == this.position.row();
	}
	
	public String getIcon() {
		return "S[".concat(Integer.toString(health)).concat("]");
	}
	
	public void update() {
		if (cyclesSinceLastCoinGeneration < COOLDOWN - 1) cyclesSinceLastCoinGeneration++;
		else {
			cyclesSinceLastCoinGeneration=0;
			game.generateCoins(GENERATED_SUN_COINS);
		}
	}
	
	public void receiveDamage(int damage) {
		health-=damage;
	}
	
	public boolean isAlive() {
		return health > 0;
	}
}
