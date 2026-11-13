package pvz.control;

import java.util.StringJoiner;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.logic.gameobjects.Plant;
import pvz.logic.gameobjects.PlantFactory;
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
	    view.showMessage(PlantFactory.getAvailablePlants());
	}

}
