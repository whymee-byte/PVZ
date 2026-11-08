package pvz.control;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.logic.gameobjects.Peashooter;
import pvz.logic.gameobjects.Sunflower;
import pvz.view.Messages;

public class ListPlantsCommand extends Command {
	public ListPlantsCommand() {
	    this.name = Messages.COMMAND_LIST_PLANTS_NAME;
	    this.shortcut = Messages.COMMAND_LIST_PLANTS_SHORTCUT;
	    this.details = Messages.COMMAND_LIST_PLANTS_DETAILS;
	    this.help = Messages.COMMAND_LIST_PLANTS_HELP;
	}
	@Override
	public void execute(GameModel model, GameView view) {
	    StringBuilder sb = new StringBuilder();

	    sb.append(Peashooter.getDescription());
	    sb.append(Messages.LINE_SEPARATOR);
	    sb.append(Sunflower.getDescription());

	    view.showMessage(sb.toString());
	}

}
