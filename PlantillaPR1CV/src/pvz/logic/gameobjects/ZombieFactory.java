package pvz.logic.gameobjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import pvz.logic.GameWorld;
import pvz.logic.gameobjects.zombieTypes.*;
import pvz.view.Messages;
import utils.Position;

public class ZombieFactory {
	private static final List<Zombie> AVAILABLE_ZOMBIES = Arrays.asList(
		new NormalZombie(),
		new BucketHeadZombie(),
		new SportyZombie(),
		new ExplosiveZombie()
	);

	public static Zombie spawnZombie(String zombieName, GameWorld game, Position position) {
		for(Zombie z : AVAILABLE_ZOMBIES) {
			if(z.matchZombie(zombieName)) {
				return z.create(game,position);
			}
		}
		return null;
	}

	public static String getAvailableZombies(){
		 StringJoiner sb = new StringJoiner(Messages.LINE_SEPARATOR);

		 for(Zombie z : AVAILABLE_ZOMBIES) {
			 sb.add(z.getDescription());
		 }
		 return sb.toString();

	}

}