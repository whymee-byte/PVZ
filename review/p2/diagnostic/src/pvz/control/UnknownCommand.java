package pvz.control;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.view.Messages;

public class UnknownCommand extends Command {

    @Override
    public void execute(GameModel model, GameView view) {
        view.showError(Messages.UNKNOWN_COMMAND);
    }
}