package pvz.logic;

public interface GameItem {
	boolean receiveZombieAttack(int damage);
	boolean receivePlantAttack(int damage);
}
