package pvz.control;
import pvz.logic.GameModel;
import java.util.Arrays;
import java.util.List;
import pvz.view.*;

public abstract class Command {
	private static final List<Command> AVAILABLE_COMMANDS=Arrays.asList(
			new AddPlantCommand(),
			new ListPlantsCommand(),
			new ResetCommand(),
			new HelpCommand()
	);
	protected String name;
	protected String shortcut;
	protected String details;
	protected String help;
	
	protected String getName() {return name;}
	protected String getShortcut() {return shortcut;}
	protected String getdetails() {return details;}
	protected String getHelp() {return help;}
	public static Command parse(String[] commandWords) {
		for(Command c: AVAILABLE_COMMANDS) {
			if(c.matchCommand(commandWords[0])) {
				 Command result= c.create(commandWords);
				 //quiza debe detectar distintos tipos de illegal parameter
				if(result==null) {
					return new IllegalParametersCommand(Messages.COMMAND_PARAMETERS_MISSING);
				}
				return result;
			}
		}
		return new UnknownCommand();
	}
	boolean matchCommand(String Command) {
		return Command.equalsIgnoreCase(this.name)||Command.equalsIgnoreCase(this.shortcut);
	}
	protected Command create(String [] argumentos) {
		if(argumentos.length!=1)return null;
		return this;
		
	}
	public abstract void execute(GameModel model, GameView view);
	
	public static String getAvailableCommandsHelp() {
	    StringBuilder sb = new StringBuilder();

	    sb.append(Messages.HELP_AVAILABLE_COMMANDS);
	    sb.append(Messages.LINE_SEPARATOR);

	    for (Command c : AVAILABLE_COMMANDS) {
	        sb.append(c.getdetails());
	        sb.append(Messages.HELP_DETAILS_COMMAND_HELP_SEPARATOR);
	        sb.append(c.getHelp());
	        sb.append(Messages.LINE_SEPARATOR);
	    }

	    return sb.toString();
	}
}
