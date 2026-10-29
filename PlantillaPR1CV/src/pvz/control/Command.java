package pvz.control;

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
		
	}
	boolean matchCommand(String Command) {
		
	}
	protected Command create(String [] argumentos) {
		return this;
		//crear nueva instancia si está mal
		return null;
	}
	public void execute(GameModel model, GameView view) {
		
	}
}
