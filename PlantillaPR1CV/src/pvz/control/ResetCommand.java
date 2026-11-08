package pvz.control;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.view.*;

public class ResetCommand extends Command{
	public ResetCommand() {
	    this.name = Messages.COMMAND_RESET_NAME;
	    this.shortcut = Messages.COMMAND_RESET_SHORTCUT;
	    this.details = Messages.COMMAND_RESET_DETAILS;
	    this.help = Messages.COMMAND_RESET_HELP;
	}
	@Override
	public void execute(GameModel model, GameView view) {
		// TODO Auto-generated method stub
		model.reset();
		view.showGame();
		
	}

}
