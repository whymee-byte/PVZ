package pvz.logic;

import java.util.Random;
import pvz.logic.gameobjects.*;
import utils.Position;

public class SunManager {
	private int generatedSuns;
	private GameWorld gw;
	private int cyclesSinceLastSunGeneration;
	public final static int SUN_CYCLES=5;
	private Random rand;
	SunManager(Random rand, GameWorld gw){
		cyclesSinceLastSunGeneration=0;
		this.rand=rand;
		this.gw=gw;
		generatedSuns=0;
		
	}
	void update() {
		cyclesSinceLastSunGeneration++;
		if(cyclesSinceLastSunGeneration==SUN_CYCLES) {
			addSun();
			cyclesSinceLastSunGeneration=1;
		}
	}
	void addSun() {
		int row=rand.nextInt(Game.NUM_ROWS);
		int col=rand.nextInt(Game.NUM_COLS);
		Position position=new Position(row,col);
		gw.addGameObject(new Sun(gw,position));
		generatedSuns++;
	}
}
