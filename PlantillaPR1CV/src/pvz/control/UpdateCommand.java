package pvz.control;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.view.Messages;

public class UpdateCommand extends Command {
	UpdateCommand(){
		this.name = Messages.COMMAND_UPDATE_NAME;
		this.shortcut = Messages.COMMAND_UPDATE_SHORTCUT;
		this.details = Messages.COMMAND_UPDATE_DETAILS;
		this.help =  Messages.COMMAND_UPDATE_HELP;
	}
	@Override
	boolean matchCommand(String word) {
	    return word.isEmpty() || super.matchCommand(word);
	}
	public void execute(GameModel model, GameView view) {
		// TODO Auto-generated method stub
		model.update();
		view.showGame();
		
	}

}
