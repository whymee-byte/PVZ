package pvz.logic.gameobjects.zombieTypes;

import pvz.logic.GameWorld;
import pvz.logic.gameobjects.Zombie;
import pvz.view.Messages;
import utils.Position;

public class NormalZombie extends Zombie{
	private final int INITIAL_HEALTH=5;
	private final int MOVE_EVERY_CYCLE=2;
	private final int DAMAGE=1;
	private static final String SYMBOL = Messages.ZOMBIE_SYMBOL;
	private static final String NAME = Messages.ZOMBIE_NAME;

	public NormalZombie() {
		this(null,null);
	}
	public NormalZombie(GameWorld gw, Position position) {
		super(gw, position);
		initialHealth = INITIAL_HEALTH;
		health = initialHealth;
		moveEveryCycles=MOVE_EVERY_CYCLE;
		damage=DAMAGE;
	}
	
	@Override
	public Zombie create(GameWorld game, Position position) {
		return new NormalZombie(game, position);
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
