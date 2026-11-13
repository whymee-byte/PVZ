package pvz.logic.gameobjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import pvz.logic.GameWorld;
import pvz.view.Messages;
import utils.Position;

public class PlantFactory {
	private static final List<Plant> AVAILABLE_PLANTS= Arrays.asList(
		new Sunflower(),
		new Peashooter(),
		new Wallnut(),
		new CherryBomb()
	);

	public static Plant spawnPlant(String plantName, GameWorld game, Position position) {
		for(Plant p:AVAILABLE_PLANTS) {
			if(p.matchPlant(plantName)) {
				return p.create(game,position);
			}
		}
		return null;
	}

	public static String getAvailablePlants(){
		 StringJoiner sb = new StringJoiner(Messages.LINE_SEPARATOR);

		 for(Plant p: AVAILABLE_PLANTS) {
			 sb.add(p.getDescription());
		 }
		 return sb.toString();

	}

}
