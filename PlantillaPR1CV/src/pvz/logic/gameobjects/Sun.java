package pvz.logic.gameobjects;

import pvz.logic.GameObject;
import pvz.logic.GameWorld;
import pvz.view.Messages;
import utils.Position;

public class Sun extends GameObject {
	private final static int HEALTH=10;
	public Sun(GameWorld gw, Position position) {
		super(gw, position);
		health=HEALTH+1;
	}
	
	@Override
	public boolean catchObject(Position position) {
		if(this.position.equals(position)) {
			health = 0;
			alive = false;
			gw.collectSuns();
			return true;
		}
		return false;
	}
	
	@Override
	public void update() {
    	health-=1;
    	if(health==0) alive=false;
    }
	
	@Override
	public void appendIconTo(StringBuilder text) {
		if(text.indexOf(this.getIcon())==-1) {
			text.append(getIcon());
		}
	}
	
	@Override
	public String getIcon() {
		return Messages.SUN_SYMBOL;
	}

	@Override
	public String getDescription() {
		return Messages.SUN_DESCRIPTION;
	}

	@Override
	public void onEnter() {
		
	}

	@Override
	public void onExit() {
		
	}

}
