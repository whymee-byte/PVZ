package pvz.control;

import pvz.logic.Game;
import pvz.view.GamePrinter;
import pvz.view.GameView;
import pvz.view.Messages;
import utils.Position;

public class Controller {

	private final Game game;
	private final GameView view;

	public Controller(Game game) {
		this.game = game;
		this.view = new GamePrinter(game);
	}
	public void run(){
		view.showGame();
		while(!game.hasGameFinished()) {
			String[]c=view.getPrompt();
			Command command = Command.parse(c);
			if(command!=null) {
				command.execute(game, view);
			}
		}
		view.showEndMessage();
	}
}