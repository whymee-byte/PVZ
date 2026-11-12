package pvz.logic.gameobjects;
import pvz.view.Messages;
import utils.Position;
import pvz.logic.GameWorld;

public class Sunflower extends Plant {
	public static final int COST = 20;
	private static final int DAMAGE = 0;
	private static final int INITIAL_HEALTH = 1;
	private static final int GENERATED_SUN_COINS = 10;
	private static final int COOLDOWN = 3;
	private static final String SYMBOL = Messages.SUNFLOWER_SYMBOL;
	private static final String NAME = Messages.SUNFLOWER_NAME;
	private static final String DESCRIPTION =
			String.format(Messages.SUNFLOWER_DESCRIPTION, COST, DAMAGE, INITIAL_HEALTH);
	private static final String ICON = Messages.SUNFLOWER_ICON;

	private int health;
	private int cyclesSinceLastCoinGeneration;
	Sunflower(){
		this(null,null);
	}
	public Sunflower( GameWorld g,Position position) {
		super(g,position);
		this.cyclesSinceLastCoinGeneration = 0;
		this.health=INITIAL_HEALTH;
	}
	@Override
	public String shortName() {
		return SYMBOL;
	}
	@Override
	public String longName() {
		return NAME;
	}
	@Override
	public int getCost() {
		return COST;
	}
	@Override
	public Plant create(GameWorld game, Position position) {
		return new Sunflower(game, position);
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	public boolean isInPosition(Position position) {
		return this.position.equals(position);
	}
	
	public String getIcon() {
		return String.format(ICON, health);
	}
	
	public void update() {
		if (cyclesSinceLastCoinGeneration < COOLDOWN - 1) cyclesSinceLastCoinGeneration++;
		else {
			cyclesSinceLastCoinGeneration=0;
			gw.generateCoins(GENERATED_SUN_COINS);
		}
	}
	
	public void receiveDamage(int damage) {
		health -= damage;
	}
	
	public boolean isAlive() {
		return health > 0;
	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}
	public boolean receiveZombieAttack(int damage) {
		receiveDamage(damage);
		return true;
	}
}
