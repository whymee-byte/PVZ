package pvz.logic;

import utils.Position;

public abstract class GameObject implements GameItem {
    protected GameWorld gw;
    protected Position position;
    protected int health;
    protected boolean alive;

    public GameObject(GameWorld gw, Position position) {
        this.gw = gw;
        this.position = position;
        this.alive = true;
    }

    public boolean isInPosition(Position other) {
        return this.position.equals(other);
    }

    public Position getPosition() {
        return this.position;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public abstract String getIcon();

    public abstract String getDescription();

    public abstract void onEnter();
    
    public void update() {
    	
    }
    
    public abstract void onExit();

    @Override
    public boolean receiveZombieAttack(int damage) { //Tiene que ser sobreescrito por las plantas
        return false;
    }
    
    public boolean receivePlantAttack(int damage) { //Tiene que ser sobreescrito por los zombies
    	return false;
    }

	public boolean isInColumn(int col) {
		// TODO Auto-generated method stub
		return position.column() == col;
	}
	
	public void receiveDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            setAlive(false);
        }
    }
}
