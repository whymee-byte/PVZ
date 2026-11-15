package pvz.control;

import java.util.Arrays;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.view.Messages;
import utils.Position;

public class AddZombieCommand extends Command {
	private final String zombieName;
	private final Position position;

	public AddZombieCommand() {
		this(null, null);
	}

	private AddZombieCommand(String zombieName, Position position) {
		name = Messages.COMMAND_ADD_ZOMBIE_NAME;
		shortcut = Messages.COMMAND_ADD_ZOMBIE_SHORTCUT;
		details = Messages.COMMAND_ADD_ZOMBIE_DETAILS;
		help = Messages.COMMAND_ADD_ZOMBIE_HELP;
		this.zombieName = zombieName;
		this.position = position;
	}

	@Override
	protected Command create(String[] words) {
		if (words.length < 4) {
			return new IllegalParametersCommand(Messages.COMMAND_PARAMETERS_MISSING);
		}
		// El nombre puede tener dos palabras, como "BucketHead Zombie".
		int colIndex = words.length - 2;
		String name = String.join(" ", Arrays.copyOfRange(words, 1, colIndex));
		try {
			int col = Integer.parseInt(words[colIndex]);
			int row = Integer.parseInt(words[colIndex + 1]);
			return new AddZombieCommand(name, new Position(row, col));
		} catch (NumberFormatException e) {
			return new IllegalParametersCommand(Messages.INVALID_POSITION);
		}
	}

	@Override
	public void execute(GameModel model, GameView view) {
		if (model.addZombie(zombieName, position)) {
			model.update();
			view.showGame();
		} else {
			view.showError(Messages.COULD_NOT_ADD_ZOMBIE);
		}
	}
}
