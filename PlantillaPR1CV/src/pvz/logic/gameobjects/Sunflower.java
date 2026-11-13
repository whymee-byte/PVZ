package pvz.logic.gameobjects;
import pvz.view.Messages;
import utils.Position;
import pvz.logic.GameWorld;

public class Sunflower extends Plant {
	public static final int COST = 20;
	private static final int INITIAL_HEALTH = 1;
	private static final int GENERATED_SUN_COINS = 10;
	private static final int COOLDOWN = 3;
	private static final String NAME_AND_SHORT = Messages.SUNFLOWER_NAME_AND_SHORTCUT;

	private int cyclesSinceLastCoinGeneration;
	Sunflower(){
		this(null,null);
	}
	public Sunflower( GameWorld g,Position position) {
		super(g,position);
		this.cyclesSinceLastCoinGeneration = 0;
		this.health = INITIAL_HEALTH;

	}
	@Override
	public String shortName() {
		return Messages.SUNFLOWER_SYMBOL;
	}
	@Override
	public String longName() {
		return Messages.SUNFLOWER_NAME;
	}

	@Override
	public String nameAndShort() {
		return NAME_AND_SHORT;
	}
	@Override
	public int getCost() {
		return COST;
	}
	@Override
	public Plant create(GameWorld game, Position position) {
		return new Sunflower(game, position);
	}

	
	public void update() {
		if (cyclesSinceLastCoinGeneration < COOLDOWN - 1) cyclesSinceLastCoinGeneration++;
		else {
			cyclesSinceLastCoinGeneration=0;
			gw.generateCoins(GENERATED_SUN_COINS);
		}
	}
	
	

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getEndurance() {
		return INITIAL_HEALTH;
	}
	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}
}
