package pvz.control;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.view.Messages;
import utils.Position;

public class AddPlantCheatCommand extends Command {
    private String plantName;
    private Position position;

    public AddPlantCheatCommand() {
        // TODO: inicializar name, shortcut, details y help con Messages.COMMAND_CHEAT_PLANT_*.
    	this.name=Messages.COMMAND_CHEAT_PLANT_NAME;
		this.shortcut=Messages.COMMAND_CHEAT_PLANT_SHORTCUT;
		this.details=Messages.COMMAND_CHEAT_PLANT_DETAILS;
		this.help= Messages.COMMAND_CHEAT_PLANT_HELP;
    }

    private AddPlantCheatCommand(String plantName, Position position) {
        this();
        this.plantName=plantName;
        this.position=position;
    }

    @Override
    protected Command create(String[] words) {
        if(words.length!=4) {
        	return new IllegalParametersCommand(Messages.COMMAND_PARAMETERS_MISSING);
        }
        try {
        	//quizas puede haber un error de row y col
        	int col=Integer.parseInt(words[2]);
        	int row=Integer.parseInt(words[3]);
        	return new AddPlantCheatCommand(words[1],new Position(col,row));
        }catch(NumberFormatException e){
        	return new IllegalParametersCommand(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
        }
    }

    @Override
    public void execute(GameModel model, GameView view) {
    	if(!model.addGameObject(plantName, position, true)) {
			view.showError(Messages.INVALID_POSITION);
			return;
		}else {
			model.update();
			view.showGame();
		}
    }
}
