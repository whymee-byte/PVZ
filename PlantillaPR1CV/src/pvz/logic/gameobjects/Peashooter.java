package pvz.logic.gameobjects;
import pvz.view.Messages;
import pvz.logic.GameWorld;
import utils.Position;

public class Peashooter extends Plant{
	public static final int COST = 50;
	private static final int DAMAGE = 1;
	private static final int INITIAL_HEALTH = 3;
	private static final String NAME_AND_SHORT = Messages.PEASHOOTER_NAME_AND_SHORTCUT;

//constructoras dummy para identificar a las plantas
	Peashooter(){
		this(null,null);
	}
	public Peashooter(GameWorld game,Position position) {
		super(game,position);
		this.health = INITIAL_HEALTH;
	}
	
	public boolean isInPosition(Position position) {
		return this.position.equals(position);
	}
	
	public boolean isAlive() {
		return health > 0;
	}
	
	public void update() {
		shoot();
	}
	
	private void shoot() {
		gw.attackPlant(position, DAMAGE);
	}
	
	@Override
	public String shortName() {
		return Messages.PEASHOOTER_SYMBOL;
	}
	@Override
	public String longName() {
		return Messages.PEASHOOTER_NAME;
	}

	@Override
	public String nameAndShort() {
		return NAME_AND_SHORT;
	}
	@Override
	public int getCost() {
		return COST;
	}

	@Override
	public Plant create(GameWorld game, Position position) {
		return new Peashooter(game, position);
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
	public int getEndurance() {
		return INITIAL_HEALTH;
	}
	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return DAMAGE;
	}
}
