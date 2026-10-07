package pvz.logic.gameobjects;

import java.util.Arrays;

import utils.Position;

public class PeashooterList {
	private int numberOfPeashooters;
	private Peashooter[]peashooters;
	
	public PeashooterList () {
		this.numberOfPeashooters = 0;
		this.peashooters = new Peashooter[0];
	}
	
	public void add(Peashooter p) {
		if (numberOfPeashooters >= peashooters.length) peashooters = Arrays.copyOf(peashooters, numberOfPeashooters + 1);
		peashooters[numberOfPeashooters] = p;
		numberOfPeashooters++;
	}
}
