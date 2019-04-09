import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Controller extends Thread{
	private ArrayList<Command> commandList;	//Read file
	private ArrayList<Process> processList;	//Read file		
	
	// memory containers
	private Variable[] mainMemory;
	private File disk; // filepath to our file on our system
	private File outputTextFile; // file to output our results
	
	
	Queue<Process> readyQueue = new LinkedList<>();
	long startTime;
    Semaphore mutex = new Semaphore(1);
	
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

		startTime = System.currentTimeMillis();
		//System.out.println("Controller starts " + (System.currentTimeMillis() - startTime));
		while(!commandList.isEmpty()){

			if(checkReadyQueue(readyQueue) == true){

					Process currentProcess = getFrontProcess(readyQueue);
					readyQueue.remove();
					currentProcess.setCommandList(commandList);
					currentProcess.setMainMemory(mainMemory);
					currentProcess.setDisk(disk);
					currentProcess.setOutputTextFile(outputTextFile);
					currentProcess.setStartTime(startTime);

                    if(!currentProcess.isAlive()) {

                        currentProcess.start();
                    }

				try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}



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
				processList.remove(i);
			}
		}
	}
}
