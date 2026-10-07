package pvz.control;

import pvz.logic.Game;
import pvz.logic.gameobjects.Peashooter;
import pvz.logic.gameobjects.Sunflower;
import pvz.view.GamePrinter;
import pvz.view.GameView;
import pvz.view.Messages;
import utils.Position;

/**
 * Input/output coordinator of the game (the C in MVC).
 *
 * <p>Owns the game loop: reads a line from stdin, parses it into a
 * command, validates parameters (plant type, position), delegates
 * state changes to {@link Game}, and triggers a board reprint via
 * {@link tp1.pvz.view.GameView>} when the cycle advances. It holds no game
 * state of its own; the source of truth is always {@link Game}.
 */
public class Controller {

	private final Game game;
	private final GameView view;

	public Controller(Game game) {
		this.game = game;
		this.view = new GamePrinter(game);
	}

	/**
	 * Runs the game logic.
	 */
	public void run() {
		// TODO fill your code
		view.showGame();
		String Command[]=view.getPrompt();
		while(!Command[0].equals("exit")) {
			
			if(Command[0].equals("help")) {
				view.showMessage(Messages.HELP);
			}
			else if(Command[0].equals("list")) {
				view.showMessage(Sunflower.getDescription());
				view.showMessage(Peashooter.getDescription());
			}
			else if (Command[0].equals("add")) {
				game.addGameObject(Command[1], new Position(Integer.parseInt(Command[2]), Integer.parseInt(Command[3])));
			}
			System.out.println();
			Command=view.getPrompt();
		}
		view.showEndMessage();
	}

}