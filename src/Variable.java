
public class Variable {
	
	// attributes
	
	private int variableID;
	private int value;
	
	private int lastAccessedCounter;
	
	//constructor
	public Variable (int variableID, int value) {
		
		this.variableID = variableID;
		this.value = value;
	}
	
	// COPY constructor
	public Variable (Variable var) {
		
		this.variableID = var.variableID;
		this.value = var.value;
	}
	
	public int getVariableID () {
		return variableID;
	}
	
	public int getValue () {
		return value;
	}
	
	public int getLastAccessedCounter() {
		return lastAccessedCounter;
	}
	
	// increment counter
	public void incrementAccessCounter() {
		lastAccessedCounter = lastAccessedCounter + 1;
	}

}
