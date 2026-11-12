package pvz.logic.gameobjects;
import pvz.view.Messages;
import pvz.logic.GameWorld;
import utils.Position;

public class Peashooter extends Plant{
	public static final int COST = 50;
	private static final int DAMAGE = 1;
	private static final int INITIAL_HEALTH = 3;
	private static final String SYMBOL = Messages.PEASHOOTER_SYMBOL;
	private static final String NAME = Messages.PEASHOOTER_NAME;
	private static final String DESCRIPTION =
			String.format(Messages.PEASHOOTER_DESCRIPTION, COST, DAMAGE, INITIAL_HEALTH);
	private static final String ICON = Messages.PEASHOOTER_ICON;

	private int health;
//constructoras dummy para identificar a las plantas
	Peashooter(){
		this(null,null);
	}
	public Peashooter(GameWorld game,Position position) {
		super(game,position);
		this.health = INITIAL_HEALTH;
	}
	
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	public String getIcon() {
		return String.format(ICON, health);
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
	
	public void receiveDamage(int damage) {
		health -= damage;
	}
	@Override
	public String shortName() {
		return SYMBOL;
	}
	@Override
	public String longName() {
		return NAME;
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
	public boolean receiveZombieAttack(int damage) {
		receiveDamage(damage);
		return true;
	}
}
