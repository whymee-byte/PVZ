package pvz.control;

import pvz.logic.Game;
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
		while(!game.hasGameFinished()) {
			String Command[]=view.getPrompt();
			
			if(Command[0].equals("help")||Command[0].equals("h")) {
				view.showMessage(Messages.HELP);
			}
			else if(Command[0].equals("list")||Command[0].equals("l")) {
				view.showMessage(Messages.LIST);
			}

			else if (Command[0].equals("add")||Command[0].equals("a")) {
				if (Command.length < 4) {
					view.showError(Messages.COMMAND_PARAMETERS_MISSING);
					continue;
				}
				Position pos;
				try {pos = new Position(Integer.parseInt(Command[3]), Integer.parseInt(Command[2]));}
				catch (NumberFormatException e) {
					view.showError(Messages.INVALID_POSITION);
					continue;
				}
				
				if(!game.addGameObject(Command[1], pos)) {
					view.showError(Messages.ERROR);
				}
				game.update();
				view.showGame();
			}
			else if(Command[0].equals("none")||Command[0].equals("n")||Command[0].equals("")) {
				game.update();
				view.showGame();
			}else if(Command[0].equals("reset")||Command[0].equals("r")) {
				game.reset();
				view.showGame();
			}else if(Command[0].equals("exit")||Command[0].equals("e")){
				game.quit();
			}else {
				view.showError(Messages.UNKNOWN_COMMAND);
			}
		}
		view.showEndMessage();
	}

}