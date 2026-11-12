package pvz.logic.gameobjects;

import pvz.logic.GameObject;
import pvz.logic.GameWorld;
import utils.Position;

public abstract class Plant extends GameObject {

    protected Plant(GameWorld game, Position position) {
        super(game, position);
    }

    public abstract String shortName();
    public abstract int getCost();
    public abstract String longName();
    public boolean matchPlant(String nombre) {
        return longName().equalsIgnoreCase(nombre)
            || shortName().equalsIgnoreCase(nombre);
    }
    public abstract Plant create(GameWorld game, Position position);
}
