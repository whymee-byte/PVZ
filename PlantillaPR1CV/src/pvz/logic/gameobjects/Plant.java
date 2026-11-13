package pvz.logic.gameobjects;

import pvz.logic.GameObject;
import pvz.logic.GameWorld;
import pvz.view.Messages;
import utils.Position;

public abstract class Plant extends GameObject {
    protected Plant(GameWorld game, Position position) {
        super(game, position);
    }
    @Override
    public String getDescription() {
        return String.format(Messages.PLANT_DESCRIPTION, nameAndShort(), getCost(), getDamage(), getEndurance());
    }
    @Override
    public String getIcon() {
        return String.format(Messages.GAME_OBJECT_ICON, shortName(), health);
    }
    public abstract String nameAndShort();
    public abstract int getEndurance();
    public abstract int getDamage();
    public abstract String shortName();
    public abstract int getCost();
    public abstract String longName();
    public boolean matchPlant(String nombre) {
        return longName().equalsIgnoreCase(nombre)
            || shortName().equalsIgnoreCase(nombre);
    }
    public abstract Plant create(GameWorld game, Position position);

    @Override
    public boolean receiveZombieAttack(int damage) {
        receiveDamage(damage);
        return true;
    }

    public void receiveDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            setAlive(false);
        }
    }
}
