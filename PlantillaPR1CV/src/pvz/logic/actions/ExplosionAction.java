package pvz.logic.actions;

import pvz.logic.GameWorld;
import utils.Position;

public class ExplosionAction implements GameAction {

	private final int damage;
	private final Position[] positions;
	private final boolean damagesZombies;

	public ExplosionAction(int damage, Position positions, boolean damagesZombies) {
		this.damage = damage;
		this.positions = positions.adjacentPositions();
		this.damagesZombies = damagesZombies;
	}

	@Override
	public void execute(GameWorld game) {
		// TODO  recorre positions y comprueba game.isInsideBoard(pos).
		// En cada casilla válida decide con damagesZombies qué método llamar:
		// game.damageZombieAt(pos, damage) o game.damagePlantAt(pos, damage).
		for(int i=0;i<positions.length;i++) {
			if(!game.isInsideBoard(positions[i]))continue;
			if(damagesZombies) {
				game.damageZombieAt(positions[i], damage);
			}else {
				game.damagePlantAt(positions[i], damage);
			}
			
		}
	}
}
