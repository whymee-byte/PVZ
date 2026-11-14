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
	//private static final String DESCRIPTION = Messages.ZOMBIE_DESCRIPTION;
	//private static final String ICON = Messages.ZOMBIE_ICON;
	//private static final String NAME_AND_SHORTCUT = Messages.ZOMBIE_NAME_AND_SHORTCUT;

	public NormalZombie() {
		this(null,null);
	}
	public NormalZombie(GameWorld gw, Position position) {
		super(gw, position);
		health=INITIAL_HEALTH;
		moveEveryCycles=MOVE_EVERY_CYCLE;
		damage=DAMAGE;
	}
	
	@Override
	public Zombie create(GameWorld game, Position position) {
		return new NormalZombie(game, position);
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
	public String shortName() {
		return SYMBOL;
	}
	@Override
	public String longName() {
		return NAME;
	}
}
