package pvz.logic.gameobjects.zombieTypes;

import pvz.logic.GameWorld;
import pvz.logic.gameobjects.Zombie;
import pvz.view.Messages;
import utils.Position;

public class ExplosiveZombie extends Zombie{
	private final int INITIAL_HEALTH=5;
	private final int MOVE_EVERY_CYCLE=2;
	private final int DAMAGE=1;
	private static final String SYMBOL = Messages.EXPLOSIVE_ZOMBIE_SYMBOL;
	private static final String NAME = Messages.EXPLOSIVE_ZOMBIE_NAME;
	
	public ExplosiveZombie(GameWorld gw, Position position) {
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
		// Explosi�n
		
	}

}