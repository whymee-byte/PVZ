package pvz.logic.gameobjects;

import pvz.logic.GameWorld;
import pvz.view.Messages;
import utils.Position;

public class CherryBomb extends Plant{
	public static final int COST = 50;
	private static final int DAMAGE = 10;
	private static final int INITIAL_HEALTH = 2;
	private static final int COOLDOWN = 2;
	private int cycle;
	private static final String SYMBOL = Messages.CHERRY_BOMB_SYMBOL;
	private static final String NAME = Messages.CHERRY_BOMB_NAME;
	private static final String NAME_AND_SHORT = Messages.CHERRY_BOMB_NAME_SHORTCUT;
	CherryBomb(){
		this(null,null);
	}
	protected CherryBomb(GameWorld game, Position position) {
		super(game, position);
		this.health = INITIAL_HEALTH;
		cycle=-1;

	}

	@Override
	public String shortName() {
		// TODO Auto-generated method stub
		return SYMBOL;
	}

	@Override
	public int getCost() {
		// TODO Auto-generated method stub
		return COST;
	}

	@Override
	public String longName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public Plant create(GameWorld game, Position position) {
		// TODO Auto-generated method stub
		return new CherryBomb(game,position);
	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		String symb;
		if(cycle==1)symb= SYMBOL.toUpperCase();
		else symb= SYMBOL.toLowerCase();
		return String.format(Messages.GAME_OBJECT_ICON, symb,health);
	}

	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		cycle++;
		if(cycle==COOLDOWN) {
			setAlive(false);
			
		}
		
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
		return DAMAGE;
	}
	@Override
	public String nameAndShort() {
		return NAME_AND_SHORT;
	}

	

}
