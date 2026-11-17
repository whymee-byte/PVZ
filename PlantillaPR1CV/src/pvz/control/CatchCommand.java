package pvz.control;

import pvz.logic.GameModel;
import pvz.view.GameView;
import pvz.view.Messages;
import utils.Position;

public class CatchCommand extends Command {
	private Position position;
	public CatchCommand() {
		this.name = Messages.COMMAND_CATCH_NAME;
		this.shortcut = Messages.COMMAND_CATCH_SHORTCUT;
		this.details = Messages.COMMAND_CATCH_DETAILS;
		this.help = Messages.COMMAND_CATCH_HELP;
		
	}
	public CatchCommand(int row,int col) {
		this();
		this.position=new Position(row,col);
	}
	@Override
	protected Command create(String [] argumentos) {
		if(argumentos.length!=3)return null;
		try {
			int row=Integer.parseInt(argumentos[3]);
			int col=Integer.parseInt(argumentos[2]);
			return new CatchCommand(row,col);
		}catch(NumberFormatException e) {
			return new IllegalParametersCommand(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
		}
	}
	@Override
	public void execute(GameModel model, GameView view) {
		model.catchObject(position);
		view.showGame();
		
	}
}
