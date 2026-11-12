package pvz.logic.gameobjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pvz.logic.GameWorld;
import utils.Position;

public class PlantFactory {
	private static final List<Plant> AVAILABLE_PLANTS= Arrays.asList(
		new Sunflower(),
		new Peashooter()
	);

	public static Plant spawnPlant(String plantName, GameWorld game, Position position) {
		for(Plant p:AVAILABLE_PLANTS) {
			if(p.matchPlant(plantName)) {
				return p.create(game,position);
			}
		}
		return null;
	}

	public static List<Plant> getAvailablePlants(){
		return Collections.unmodifiableList(AVAILABLE_PLANTS);
	}

}
