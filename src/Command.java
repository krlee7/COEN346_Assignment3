import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.Line;

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
	
	// COMMANDS
	
	// store
	public int store(Variable[] mainMemory, File disk) throws IOException {
		
		System.out.println("Attempting to store variable into Main Memory ...");
		
		if (!this.writeMemory(mainMemory, variableID, value)) { // if was not able to store in main memory
			
			System.out.println("Main Memory was too full, insert into disk ...");
			
			this.writeDisk(disk, variableID, value);
			
			return 0;
		}
		
		System.out.println("Inserted variable into main memory successful ...");
		
		return 0;
		
	}
	
	// release
	public int release(Variable[] mainMemory) throws IOException {
		
		for(int i = 0; i < mainMemory.length; i++) {
			
        	int varID = mainMemory[i].getVariableID();
        	int varValue = mainMemory[i].getValue();
        	
        	if(varID == variableID) {
        		
        		System.out.print("Releasing Variable : ");
        		System.out.print(varID);
        		
        		mainMemory[i] = null;
        		
        		return 0;
        	}
		}
		
		return -1; // error
	}
	
	// lookup
	public int lookup(Variable[] mainMemory, File disk) throws IOException {
		
		if(this.readMemory(mainMemory, variableID).getVariableID() > 0) // we have a valid variable
		{
			return this.readMemory(mainMemory, variableID).getValue(); // read the variable in 
		}
		
		else { // if we cant find it in the main memory 
			
			Variable variable = this.readDisk(disk, variableID);
			
			if(this.writeMemory(mainMemory, variable.getVariableID(), variable.getValue())) {  // if it was able to write in the main memory 
				
				return this.readMemory(mainMemory, variableID).getValue(); // read the variable in 
			}
			
			else { // there is no space in the main memory
				
				// swap to read the variable from disk to memory
				
				this.swapDiskMemory(mainMemory, disk, variableID);
				
				return this.readMemory(mainMemory, variableID).getValue(); // read the variable in 
			}
		}
	}
	
	// execute command
	public void doCommand(Variable[] mainMemory, File disk, File outputTextFile) throws IOException, InterruptedException {
		
		// random time gen
		
		Random r = new Random();
		int low = 1;
		int high = 1000;
		int delayTime = r.nextInt(high-low) + low;
		
		// case statement here
		
		String lineString = "";
		
		switch (commandType) {
		case "Store":
			
			lineString = "STORE : " + Integer.toString(variableID) + Integer.toString(value);
			//this.writeOutputText(lineString, outputTextFile);
			
			this.store(mainMemory, disk);
			
			TimeUnit.MILLISECONDS.sleep(delayTime);
			
			break;
			
		case "Release":
			
			lineString = "RELEASE : " + Integer.toString(variableID) + Integer.toString(value);
			//this.writeOutputText(lineString, outputTextFile);
			
			this.release(mainMemory);
			
			TimeUnit.MILLISECONDS.sleep(delayTime);
			
			break;
			
		case "Lookup":
			
			lineString = "RELEASE : " + Integer.toString(variableID) + Integer.toString(value);
			//this.writeOutputText(lineString, outputTextFile);
			
			this.lookup(mainMemory, disk);
			
			TimeUnit.MILLISECONDS.sleep(delayTime);
	
			break;
		}
		
	}
	
	// print
	public void print() {
		
		System.out.print("Command : ");
		System.out.print(commandType);
		System.out.print(" ");
		System.out.print(variableID);
		System.out.print(" ");
		System.out.println(value);
	}
	
	// disk I/O
	
	public Variable readDisk(File disk, int variableID) throws FileNotFoundException // searches for the element and returns it
	{
		// read disk line by line
		// parse string
		// if found -> break 
		// construct element
		// return element
		// else
		// return empty variable indicating error
		
		// *** //
		
		System.out.println("reading disk ...");
		
		// init variable to be returned
		
		Variable variable = new Variable(-1,-1);
		
        // Reading disk
        Scanner scanner = new Scanner(new FileInputStream(disk.getAbsolutePath()));
              
        while(scanner.hasNextLine()){
        	
        	String lineCommand = scanner.nextLine();
        	
        	String delims = "[ ]+";
        	String[] tokens = lineCommand.split(delims);
        	
        	int varID = Integer.parseInt(tokens[0]);
        	int varValue = Integer.parseInt(tokens[1]);
        	
        	if(varID == variableID) {
        		variable = new Variable(varID, varValue);
        		
        		System.out.print("Found Variable : ");
        		System.out.println(varID);
        	}
        }
                
        return variable;
	}
	
	public void writeDisk(File disk, int variableID, int value) throws IOException // write to first free space
	{
		FileWriter fr = new FileWriter(disk, true);
		
		// Writing variable
		
		fr.write(Integer.toString(variableID));
		fr.write(" ");
		fr.write(Integer.toString(value));
		fr.write(System.getProperty("line.separator")); // add an end line 
		
		fr.close();
	}
	
	public Variable readMemory(Variable[] mainMemory, int variableID) // searches for the element and returns it
	{
		// read memory
		// if found -> returns variable
		// if not -> return error
		
		System.out.println("reading mainMemory ...");
		
		// init variable to be returned
		
		Variable variable = new Variable(-1,-1);
		
		for(int i = 0; i < mainMemory.length; i++) {
			
        	int varID = mainMemory[i].getVariableID();
        	int varValue = mainMemory[i].getValue();
        	
        	if(varID == variableID) {
        		
        		System.out.print("Found Variable : ");
        		System.out.print(varID);
        		
        		// increment its accessCounter
        		mainMemory[i].incrementAccessCounter();
        		
        		return mainMemory[i];
        	}
		}
		
		// if nothing found
		return variable;
		
	}
	
	public boolean writeMemory(Variable[] mainMemory, int variableID, int value) // write to first free space
	{
		// checking if array is empty or full
		boolean full = true;
		for (int i=0; i<mainMemory.length; i++) {
		  if (mainMemory[i] == null) {
			full = false;
		    break;
		  }
		}
		
		// if the mainMemory is full
		if(full) {
			return false;
		}
		
		// free spot available
		else {
			
			Variable variable = new Variable(variableID, value);
			
			for (int i=0; i<mainMemory.length; i++) {
				  if (mainMemory[i] == null) {
					mainMemory[i] = variable;
				    break;
				  }
				}
		}
		
		return true;
	}
	
	public void swapDiskMemory(Variable[] mainMemory, File disk, int variableID ) throws IOException // swaps disk element with memory element
	{
		// checks who was last accessed
		// remove element from main memory
		// remove element from disk
		// insert elements
		
		// for loop memarray
		// if max 
		// return index
						
		// remove element from main memory
		int maxCounter = 0;
		int variableIndexLRA = -1;
		
		for (int i=0; i<mainMemory.length; i++) {
			if (mainMemory[i].getLastAccessedCounter() >= maxCounter) {
				variableIndexLRA = i;
			}
		}		
		
		Variable tempMMVariable = mainMemory[variableIndexLRA];
		mainMemory[variableIndexLRA] = null;
		
		// remove element from disk
		
		Variable tempDVariable = this.removeRetrieveDisk(disk, variableID); // removes the variable and return it 
		
		// insert the disk element to main memory
		
		mainMemory[variableIndexLRA] = tempDVariable;
		
		// insert memory element to disk
		
		this.writeDisk(disk, tempMMVariable.getVariableID(), tempMMVariable.getValue());
	}
	
	public Variable removeRetrieveDisk(File disk, int variableID) throws IOException {
		
		// makes a temp file
		// only copies the elements we need and deletes the target line
		// return the element deleted
		// renames it to orginal file
		
		File temp = new File("temp.txt");
	    BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
		
        // Reading disk
        Scanner scanner = new Scanner(new FileInputStream(disk.getAbsolutePath()));
        
        // init variable 
        Variable variable = new Variable(-1,-1);
              
        while(scanner.hasNextLine()){
        	
        	String line = scanner.nextLine();
        	
        	String delims = "[ ]+";
        	String[] tokens = line.split(delims);
        	
        	int varID = Integer.parseInt(tokens[0]);
        	int varValue = Integer.parseInt(tokens[1]);
        	
        	String currentLine = line;
        	
        	if(varID == variableID) {
        		variable = new Variable(varID, varValue);
        		
        		System.out.print("Found Variable : ");
        		System.out.println(varID);
        		
        		currentLine = "";
        	}
        	
        	bw.write(currentLine + System.getProperty("line.separator"));
        }
        
        disk.delete();
        temp.renameTo(disk); // rename the temp file
        
        return variable;
	}
	
	// write to output file
	public void writeOutputText(String line, File output) throws IOException {
		
		FileWriter fileWriter = new FileWriter(output);
	    fileWriter.write(line);
	    fileWriter.close();
	}
}

// IDEAS FOR RETURNING ERROR : 

// - instead 
