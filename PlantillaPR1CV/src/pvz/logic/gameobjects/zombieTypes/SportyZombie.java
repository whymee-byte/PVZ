package pvz.logic.gameobjects.zombieTypes;

import pvz.logic.GameWorld;
import pvz.logic.gameobjects.Zombie;
import pvz.view.Messages;
import utils.Position;

public class SportyZombie extends Zombie{
	private final int INITIAL_HEALTH=2;
	private final int MOVE_EVERY_CYCLE=1;
	private final int DAMAGE=1;
	private static final String SYMBOL = Messages.SPORTY_ZOMBIE_SYMBOL;
	private static final String NAME = Messages.SPORTY_ZOMBIE_NAME;
	
	public SportyZombie() {
		this(null,null);
	}
	public SportyZombie(GameWorld gw, Position position) {
		super(gw, position);
		initialHealth = INITIAL_HEALTH;
		health = initialHealth;
		moveEveryCycles=MOVE_EVERY_CYCLE;
		damage=DAMAGE;
	}

	@Override
	public Zombie create(GameWorld game, Position position) {
		return new SportyZombie(game, position);
	}
	
	@Override
	public void onEnter() {
		
	}

	@Override
	public void onExit() {
		gw.zombieDied();
	}

	@Override
	public String shortName() {
		return SYMBOL;
	}
	
	@Override
	public String longName() {
		return NAME;
	}
}
