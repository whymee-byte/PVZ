package pvz.control;

import pvz.logic.GameModel;
import pvz.view.*;

public class ResetCommand extends Command{
	private Level level;
	private long seed;
	public ResetCommand() {
	    this.name = Messages.COMMAND_RESET_NAME;
	    this.shortcut = Messages.COMMAND_RESET_SHORTCUT;
	    this.details = Messages.COMMAND_RESET_DETAILS;
	    this.help = Messages.COMMAND_RESET_HELP;
	}
	public ResetCommand(Level difficulty,long seed) {
		this();
		this.level=difficulty;
		this.seed=seed;
	}
	@Override
	public Command create(String[]argumentos) {
		if(argumentos.length==1)return this;
		if(argumentos.length!=3)return new IllegalParametersCommand(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
		Level level=Level.valueOfIgnoreCase(argumentos[1]);
		if(level ==null) {
			return new IllegalParametersCommand(Messages.ALLOWED_LEVELS);
		}
		try {
			long seed=Long.parseLong(argumentos[2]);
			return  new ResetCommand(level,seed);
		}catch(NumberFormatException ex) {
			return new IllegalParametersCommand(Messages.SEED_NOT_A_NUMBER);
		}
	}
	@Override
	public void execute(GameModel model, GameView view) {
		// TODO Auto-generated method stub
		if(level==null) {
			model.reset();
		}else {
			model.reset(level, seed);
		}
		view.showGame();
		
	}

}
