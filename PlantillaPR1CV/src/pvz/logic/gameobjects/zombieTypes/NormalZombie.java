package pvz.logic.gameobjects.zombieTypes;

import pvz.logic.GameWorld;
import pvz.logic.gameobjects.Zombie;
import pvz.view.Messages;
import utils.Position;

public class NormalZombie extends Zombie{
	private final int INITIAL_HEALTH=5;
	private final int MOVE_EVERY_CYCLE=2;
	private final int DAMAGE=1;
	private static final String SYMBOL = Messages.SUNFLOWER_SYMBOL;
	private static final String NAME = Messages.SUNFLOWER_NAME;
	
	public NormalZombie(GameWorld gw, Position position) {
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
