package pvz.logic.gameobjects.zombieTypes;

import pvz.logic.GameWorld;
import pvz.logic.actions.ExplosionAction;
import pvz.logic.gameobjects.Zombie;
import pvz.view.Messages;
import utils.Position;

public class ExplosiveZombie extends Zombie{
	private static final int EXPLOSION_DAMAGE = 3;
	private final int INITIAL_HEALTH=5;
	private final int MOVE_EVERY_CYCLE=2;
	private final int DAMAGE=1;
	private static final String SYMBOL = Messages.EXPLOSIVE_ZOMBIE_SYMBOL;
	private static final String NAME = Messages.EXPLOSIVE_ZOMBIE_NAME;
	
	public ExplosiveZombie() {
		this(null,null);
	}
	public ExplosiveZombie(GameWorld gw, Position position) {
		super(gw, position);
		initialHealth = INITIAL_HEALTH;
		health = initialHealth;
		moveEveryCycles=MOVE_EVERY_CYCLE;
		damage=DAMAGE;
	}

	@Override
	public Zombie create(GameWorld game, Position position) {
		return new ExplosiveZombie(game, position);
	}

	@Override
	public void onEnter() {
		
	}

	@Override
	public void onExit() {
		gw.zombieDied();
		gw.pushAction(new ExplosionAction(EXPLOSION_DAMAGE, position, false));
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
