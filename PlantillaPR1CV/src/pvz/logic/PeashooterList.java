package pvz.logic;

import java.util.Arrays;

import pvz.logic.gameobjects.Peashooter;
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
	
	public boolean isEmpty(Position position) {
		int i = 0;
		while (i < numberOfPeashooters && !peashooters[i].isInPosition(position)) i++;
		return i == numberOfPeashooters;
	}
	
	public String iconInPosition(Position position) {
		int i = 0;
		while (i < numberOfPeashooters && !peashooters[i].isInPosition(position)) i++;
		return peashooters[i].getIcon();
	}
}
