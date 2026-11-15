package pvz.control;

import pvz.logic.GameModel;
import pvz.logic.gameobjects.ZombieFactory;
import pvz.view.GameView;
import pvz.view.Messages;

public class ListZombiesCommand extends Command {
	public ListZombiesCommand() {
		name = Messages.COMMAND_LIST_ZOMBIES_NAME;
		shortcut = Messages.COMMAND_LIST_ZOMBIES_SHORTCUT;
		details = Messages.COMMAND_LIST_ZOMBIES_DETAILS;
		help = Messages.COMMAND_LIST_ZOMBIES_HELP;
	}

	@Override
	public void execute(GameModel model, GameView view) {
		view.showMessage(Messages.AVAILABLE_ZOMBIES + Messages.LINE_SEPARATOR
				+ ZombieFactory.getAvailableZombies());
	}
}
