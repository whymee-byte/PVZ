package pvz.logic.gameobjects;

import pvz.logic.GameWorld;
import pvz.view.Messages;
import utils.Position;

public class Wallnut extends Plant{
	public static final int COST = 50;
	private static final int INITIAL_HEALTH = 10;
	private static final String NAME_AND_SHORT = Messages.WALL_NUT_NAME_SHORTCUT;
	Wallnut() {
		this(null,null);// TODO Auto-generated constructor stub
	}

	protected Wallnut(GameWorld game, Position position) {
		super(game, position);
		this.health = INITIAL_HEALTH;
	}


	@Override
	public String shortName() {
		return Messages.WALL_NUT_SYMBOL;
	}

	@Override
	public int getCost() {
		// TODO Auto-generated method stub
		return COST;
	}

	@Override
	public String longName() {
		return Messages.WALL_NUT_NAME;
	}

	@Override
	public String nameAndShort() {
		return NAME_AND_SHORT;
	}

	@Override
	public Plant create(GameWorld game, Position position) {
		// TODO Auto-generated method stub
		return new Wallnut(game,position);
	}




	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
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
