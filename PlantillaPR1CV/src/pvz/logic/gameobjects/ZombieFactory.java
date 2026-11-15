package pvz.logic.gameobjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.Random;

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

	public static Zombie spawnZombie(Random rand, GameWorld game, Position position) {
		return AVAILABLE_ZOMBIES.get(rand.nextInt(AVAILABLE_ZOMBIES.size())).create(game,position);
	}

	public static Zombie spawnZombie(String name, GameWorld game, Position position) {
		for (Zombie prototype : AVAILABLE_ZOMBIES) {
			if (prototype.matchZombie(name)) {
				return prototype.create(game, position);
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
