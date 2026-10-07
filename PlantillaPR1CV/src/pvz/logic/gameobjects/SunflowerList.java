package pvz.logic.gameobjects;

import java.util.Arrays;

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
}
