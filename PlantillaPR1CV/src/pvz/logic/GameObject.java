package pvz.logic;

import utils.Position;

public abstract class GameObject implements GameItem {
    protected GameWorld gw;
    protected Position position;
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

    public abstract String getDescription();
    public abstract String getIcon();
    public abstract void onEnter();
    
    public abstract void update();
    
    public abstract void onExit();

    @Override
    public boolean receiveZombieAttack(int damage) { //Tiene que ser sobreescrito por las plantas
        return false;
    }
}