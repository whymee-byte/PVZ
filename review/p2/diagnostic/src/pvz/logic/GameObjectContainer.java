package pvz.logic;

import java.util.ArrayList;
import java.util.List;
import utils.Position;

public class GameObjectContainer {
 public boolean isMovementBlocked(Position p) { for (GameObject o : gameObjects) if (o.isAlive() && o.isInPosition(p) && o.blocksMovement()) return true; return false; }
	private List<GameObject> gameObjects;

	public GameObjectContainer() {
		gameObjects = new ArrayList<>();
	}

	public boolean add(GameObject object) {
		this.gameObjects.add(object);
		object.onEnter();
		return true;
	}
	
	public int catchObject(Position position) {
		int amountOfSunsCatched = 0;
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject obj = gameObjects.get(i);
			if(obj.catchObject(position)) amountOfSunsCatched++;
		}
		return amountOfSunsCatched;
	}

	public void update() {
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject obj = gameObjects.get(i);
			if (obj.isAlive()) {
				obj.update();
			}
		}
	}

	public boolean removeDead() {
		boolean removed = false;
		for (int i = gameObjects.size() - 1; i >= 0; i--) {
			GameObject obj = gameObjects.get(i);
			if (!obj.isAlive()) {
				obj.onExit();
				gameObjects.remove(i);
				removed = true;
			}
		}
		return removed;
	}

	public boolean isEmpty(Position position) {
		return getGameItemInPosition(position) == null ;
	}

	public GameObject getGameObjectInPosition(Position position) {
		for (GameObject obj : gameObjects) {
			if (obj.isInPosition(position)) {
				return obj;
			}
		}
		return null;
	}

	public GameItem getGameItemInPosition(Position position) {
		for (GameObject obj : gameObjects) {
			if (obj.isAlive() && obj.isInPosition(position)) {
				return obj;
			}
		}
		return null;
	}

	public boolean hasElements() {
		return !gameObjects.isEmpty();
	}
	
	public String positionToString(Position position) {
		StringBuilder text=new StringBuilder();
		for (GameObject obj : gameObjects) {
			if (obj.isAlive() && obj.isInPosition(position)) {
				obj.appendIconTo(text);
			}
		}
		return text.toString();
	}
	
	public boolean hasObjectInColumn(int col) {
		for (GameObject obj : gameObjects) {
			if (obj.isInColumn(col)) return true;
		}
		return false;
	}
	
	public void attackZombie(Position p, int damage) {
		for (GameObject obj : gameObjects) {
			if (obj.isInPosition(p)) obj.receiveZombieAttack(damage);
		}
	}
	
	public void attackPlant(Position p, int damage, int maxCol) {
		while (p.column() < maxCol) {
			if (damageZombieAt(p, damage)) return;
			p = p.right();
		}
	}

	public boolean damageZombieAt(Position position, int damage) {
		for (GameObject obj : gameObjects) {
			if (obj.isInPosition(position) && obj.isAlive() && obj.receivePlantAttack(damage)) {
				return true;
			}
		}
		return false;
	}
}
