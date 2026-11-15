package pvz.logic.gameobjects.zombieTypes;

import pvz.logic.GameWorld;
import pvz.logic.gameobjects.Zombie;
import pvz.view.Messages;
import utils.Position;

public class BucketHeadZombie extends Zombie{
	private final int INITIAL_HEALTH=8;
	private final int MOVE_EVERY_CYCLE=4;
	private final int DAMAGE=1;
	private static final String SYMBOL = Messages.BUCKET_HEAD_ZOMBIE_SYMBOL;
	private static final String NAME = Messages.BUCKET_HEAD_ZOMBIE_NAME;
	
	public BucketHeadZombie() {
		this(null,null);
	}
	public BucketHeadZombie(GameWorld gw, Position position) {
		super(gw, position);
		initialHealth = INITIAL_HEALTH;
		health = initialHealth;
		moveEveryCycles=MOVE_EVERY_CYCLE;
		damage=DAMAGE;
	}

	@Override
	public Zombie create(GameWorld game, Position position) {
		return new BucketHeadZombie(game, position);
	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
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
