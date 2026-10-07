package pvz.logic;

import java.util.Arrays;

import pvz.logic.gameobjects.Sunflower;
import utils.Position;

public class SunflowerList {
	private int numberOfSunflowers;
	private Sunflower[]sunflowers;
	
	public SunflowerList () {
		this.numberOfSunflowers = 0;
		this.sunflowers = new Sunflower[0];
	}
	
	public void add(Sunflower s) {
		if (numberOfSunflowers >= sunflowers.length) sunflowers = Arrays.copyOf(sunflowers, numberOfSunflowers + 1);
		sunflowers[numberOfSunflowers] = s;
		numberOfSunflowers++;
	}
	
	public boolean isEmpty(Position position) {
		int i = 0;
		while (i < numberOfSunflowers && !sunflowers[i].isInPosition(position)) i++;
		return i == numberOfSunflowers;
	}
}
