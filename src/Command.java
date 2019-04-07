
public class Command {
	
	// attributes
	
	String commandType;
	int variableID;
	int value;
	
	// constructor
	
	public Command(String commandType, int variableID, int value)
	{
		this.commandType = commandType;
		this.variableID = variableID;
		this.value = value;
	}
	
	public Command(String commandType, int variableID)
	{
		this.commandType = commandType;
		this.variableID = variableID;
	}
	

}
