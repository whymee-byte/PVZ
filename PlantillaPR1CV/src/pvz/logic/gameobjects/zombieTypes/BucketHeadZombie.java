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
	
	public BucketHeadZombie(GameWorld gw, Position position) {
		super(gw, position);
		health=INITIAL_HEALTH;
		moveEveryCycles=MOVE_EVERY_CYCLE;
		damage=DAMAGE;
	}


	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

}
