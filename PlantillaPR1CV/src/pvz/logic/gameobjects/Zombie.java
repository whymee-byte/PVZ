package pvz.logic.gameobjects;
import utils.Position;
import pvz.logic.Game;
import pvz.logic.GameItem;
import pvz.logic.GameObject;
import pvz.logic.GameWorld;
import pvz.view.Messages;


public abstract class Zombie extends GameObject{
	
	private int cyclesSinceLastMovement;
	protected int damage;
	protected int initialHealth;
	protected int moveEveryCycles;
	
	public abstract String shortName();
	public abstract String longName();
	public boolean matchZombie(String nombre) {
        return longName().equalsIgnoreCase(nombre) || shortName().equalsIgnoreCase(nombre);
    }

	public abstract Zombie create(GameWorld game, Position position);

	public Zombie(GameWorld gw, Position position) {
		super(gw,position);
		cyclesSinceLastMovement=-1;
	}

	public boolean isInPosition(Position position) {
		return this.position.equals(position);
	}

	public boolean isHorizontallyAligned(Position p) {
		return position.isHorizontallyAligned(p);
	}

	public boolean isVerticallyAligned(Position p) {
		return position.isVerticallyAligned(p);
	}

	public void receiveAttack(int damage) {
		health -= damage;
	}

	public void update(){
		cyclesSinceLastMovement++;
		if(canMove()) {
			if (cyclesSinceLastMovement >= moveEveryCycles ) {
				cyclesSinceLastMovement = 0;
				move();
			}
		}
		attack();

	}

	private boolean canMove() {
		return gw.isEmpty(position.left());
	}

	private void move() {
		position = position.left();
	}

	public boolean isAlive() {
		return health > 0;
	}

	private void attack() {
		gw.damagePlantAt(position.left(), damage);
	}
	
	public String getIcon() {
		return String.format(Messages.GAME_OBJECT_ICON, shortName(), health);
	}
	
	@Override
	public String getDescription() {
		return String.format(Messages.ZOMBIE_DESCRIPTION, longName(), moveEveryCycles, damage, initialHealth);
	}
	
	@Override
    public boolean receivePlantAttack(int damage) {
        receiveDamage(damage);
        return true;
    }
}
