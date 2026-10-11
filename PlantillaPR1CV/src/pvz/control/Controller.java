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
				if(!game.checkGameObject(Command[1])) {
					view.showError(Messages.INVALID_GAME_OBJECT);
					continue;
				}
				Position pos = new Position(Integer.parseInt(Command[3]), Integer.parseInt(Command[2]));
				if (!game.isInsideBoard(pos) || !game.isEmpty(pos)) {
					view.showError(Messages.INVALID_POSITION);
					continue;
				}
				if (Command[1].equals("peashooter") || Command[1].equals("p")) {
					if (game.getCoins() < Peashooter.COST) {
						view.showError(Messages.NOT_ENOUGH_COINS);
						continue;
					}
				}
				else if (Command[1].equals("sunflower") || Command[1].equals("s")) {
					if (game.getCoins() < Sunflower.COST) {
						view.showError(Messages.NOT_ENOUGH_COINS);
						continue;
					}
				}
				else {
					view.showError(Messages.INVALID_GAME_OBJECT);
					continue;
				}
				game.addGameObject(Command[1], pos);
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