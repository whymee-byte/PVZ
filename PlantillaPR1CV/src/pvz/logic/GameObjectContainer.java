package pvz.logic;

import java.util.ArrayList;
import java.util.List;
import utils.Position;

public class GameObjectContainer {
	private List<GameObject> gameObjects;

	public GameObjectContainer() {
		gameObjects = new ArrayList<>();
	}

	public void add(GameObject object) {
		this.gameObjects.add(object);
		object.onEnter();
	}

	public void update() {
		for (int i = 0; i < gameObjects.size(); i++) {
			GameObject obj = gameObjects.get(i);
			if (obj.isAlive()) {
				obj.update();
			}
		}
	}

	public void removeDead() {
		for (int i = gameObjects.size() - 1; i >= 0; i--) {
			GameObject obj = gameObjects.get(i);
			if (!obj.isAlive()) {
				obj.onExit();
				gameObjects.remove(i);
			}
		}
	}

	public boolean isEmpty(Position position) {
		return getGameObjectInPosition(position) == null;
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
			if (obj.isInPosition(position)) {
				return obj;
			}
		}
		return null;
	}

	public boolean hasElements() {
		return !gameObjects.isEmpty();
	}
	
	public String iconInPosition(Position position) {
		for (GameObject obj : gameObjects) {
			if (obj.isInPosition(position)) return obj.getIcon();
		}
		return "";
	}
}