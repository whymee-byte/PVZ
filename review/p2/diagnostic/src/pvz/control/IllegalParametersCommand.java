package pvz.control;

import pvz.logic.GameModel;
import pvz.view.GameView;

public class IllegalParametersCommand extends Command {

    private String errorMessage;

    public IllegalParametersCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute(GameModel model, GameView view) {
        view.showError(errorMessage);
    }
}