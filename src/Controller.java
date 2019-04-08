import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Controller extends Thread{
	private ArrayList<Command> commandList;	//Read file
	private ArrayList<Process> processList;	//Read file		
	
	// memory containers
	private Variable[] mainMemory;
	private File disk; // filepath to our file on our system
	private File outputTextFile; // file to output our results
	
	
	Queue<Process> readyQueue = new LinkedList<>();
	long startTime;
	
	// constructor
	
	public Controller(Variable[] mainMemory, ArrayList<Process> processList,ArrayList<Command> commandList) throws FileNotFoundException {
		
		this.mainMemory = mainMemory;
		this.processList = processList;
		this.commandList = commandList;
		
		disk = new File("disk.txt");
		outputTextFile = new File("outputTextFile.txt");
		
		// clearing the disk file
		PrintWriter writer = new PrintWriter(disk);
		writer.print("");
		writer.close();
		
		// clearing the output file
		PrintWriter writerOut = new PrintWriter(outputTextFile);
		writerOut.print("");
		writerOut.close();
	}
	
	@Override
	public void run(){
		/*
		while(!commandList.isEmpty()){
			
			Command first = commandList.get(0);
			try {
				first.doCommand(mainMemory,  disk,  outputTextFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			commandList.remove(0);
		}

		System.out.println("Command List size : ");
		System.out.println(commandList.size());
		*/

		startTime = System.currentTimeMillis();		

		while(!commandList.isEmpty()){
									
			if(checkReadyQueue(readyQueue)){
				Process currentProcess = getFrontProcess(readyQueue);
				currentProcess.setCommandList(commandList);
				currentProcess.setMainMemory(mainMemory);
				currentProcess.setDisk(disk);
				currentProcess.setOutputTextFile(outputTextFile);
				if(!currentProcess.isAlive()) {
					currentProcess.start();
				}



				readyQueue.remove();
			}
			
			updateReadyQueue(processList);
			
		}

				
	}
	
	//Check if ready queue is empty
	public boolean checkReadyQueue(Queue readyQueue){
		if(readyQueue.isEmpty())
			return false;
		else
			return true;
	}
	
	public Process getFrontProcess(Queue readyQueue){
		return (Process) readyQueue.peek();
	}
	
	public void updateReadyQueue(ArrayList processList){
				
		for(int i=0;i<processList.size();i++){
			
			if((System.currentTimeMillis()-startTime) >= ((Process) processList.get(i)).getReadyTime()){
				
				readyQueue.add((Process) processList.get(i));
			}
		}
	}
}
