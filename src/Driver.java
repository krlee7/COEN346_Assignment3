import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
	
	public static void main (String args[]) throws FileNotFoundException
	{
	
		// read the process text file 
		
        // Reading Input File
        Scanner scanner = new Scanner(new FileInputStream("processes.txt"));
        
        boolean firstLine = true;
        
        int id = 0;
        
        ArrayList<Process> processList = new ArrayList<Process>();
        
        while(scanner.hasNextLine()){
        	
        	if(firstLine) {

                String line = scanner.nextLine();

                firstLine = false;
                continue;
            }
        	
        	String line = scanner.nextLine();
        	
        	String delims = "[ ]+";
        	String[] tokens = line.split(delims);
        	
        	Process process = new Process(id++, Long.parseLong(tokens[0]),  Long.parseLong(tokens[1]));
        	
        	processList.add(process);
       	
        	}
        
        
        // read the memconfig file
        
        // Reading the input file
        scanner = new Scanner(new FileInputStream("memconfig.txt"));
        
        String lineMem = scanner.nextLine();
        int memSize = Integer.parseInt(lineMem);
        
        // creating our main memory
    	ArrayList<Variable> mainMemory = new ArrayList<Variable>(memSize);
    
    	
    	// read the command file
    	
        // Reading Input File
        scanner = new Scanner(new FileInputStream("commands.txt"));
        
        ArrayList<Command> commandsList = new ArrayList<Command>();
        
        while(scanner.hasNextLine()){
        	
        	String lineCommand = scanner.nextLine();
        	
        	String delims = "[ ]+";
        	String[] tokens = lineCommand.split(delims);
        	        	
        	if(tokens.length == 2) {
        		
        		Command command = new Command(tokens[0], Integer.parseInt(tokens[1]));
        		commandsList.add(command);
        	}
        	
        	else if (tokens.length == 3) {
				
        		Command command = new Command(tokens[0], Integer.parseInt(tokens[1]),Integer.parseInt(tokens[2]));
        		commandsList.add(command);
			}
	}
        
    	// creating the controller with all that it needs
    	Controller controller = new Controller(mainMemory, processList, commandsList);
    	
    	controller.start();
}
}
