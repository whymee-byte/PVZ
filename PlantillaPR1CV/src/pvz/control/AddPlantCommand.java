package pvz.control;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.view.Messages;
import utils.Position;

public class AddPlantCommand extends Command{
	private String plantName;
	private Position position;
	public AddPlantCommand() {
		this.name=Messages.COMMAND_ADD_PLANT_NAME;
		this.shortcut=Messages.COMMAND_ADD_PLANT_SHORTCUT;
		this.details=Messages.COMMAND_ADD_PLANT_DETAILS;
		this.help= Messages.COMMAND_ADD_PLANT_HELP;
		
	}
	public AddPlantCommand(String args,int c,int r) {
		this.name=Messages.COMMAND_ADD_PLANT_NAME;
		this.shortcut=Messages.COMMAND_ADD_PLANT_SHORTCUT;
		this.details=Messages.COMMAND_ADD_PLANT_DETAILS;
		this.help= Messages.COMMAND_ADD_PLANT_HELP;
		this.plantName=args;
		this.position=new Position(r,c);
		
	}
	protected Command create(String [] argumentos) {
		if(argumentos.length!=4 )return null;
		try {
			int col=Integer.parseInt(argumentos[2]);
			int row=Integer.parseInt(argumentos[3]);
			return new AddPlantCommand(argumentos[1],col,row);
		}catch(NumberFormatException e){
			return null;
		}
		
	}
	@Override
	public void execute(GameModel model, GameView view) {
		if(!model.addGameObject(plantName, position, false)) {
			view.showError("Could not add plant");
			return;
		}else {
			model.update();
			view.showGame();
		}
		
	}

}
