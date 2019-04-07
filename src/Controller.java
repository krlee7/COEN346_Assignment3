import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Controller extends Thread{
	Queue<Process> readyQueue = new LinkedList<>();
	ArrayList<Command> commandList;	//Read file
	ArrayList<Process> processList;	//Read file
	
	// memory containers
	ArrayList<Variable> mainMemory;	//Read file
	
	long startTime;
	
	// constructor
	
	public Controller(ArrayList<Variable> mainMemory, ArrayList<Process> processList,ArrayList<Command> commandList) {
		
		this.mainMemory = mainMemory;
		this.processList = processList;
		this.commandList = commandList;
	}
	
	@Override
	public void run(){
		
		Process process = new Process(1,1,1);
		
		System.out.println("Before : ");
		for(int i = 0; i < commandList.size(); i++) {
			commandList.get(i).print();
		}
		
		process.modifyCommandList(commandList);
		
		System.out.println("After : ");
		for(int i = 0; i < commandList.size(); i++) {
			commandList.get(i).print();
		}
		
		/*
		startTime = System.currentTimeMillis();
		while(!commandList.isEmpty()){
			if(checkReadyQueue(readyQueue)){
				Process currentProcess = getFrontProcess(readyQueue);
				currentProcess.start();
				readyQueue.remove();
			}
			
			updateReadyQueue(processList);
			
		}
		*/
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
			if((startTime-System.currentTimeMillis()) == ((Process) processList.get(i)).getReadyTime()){
				readyQueue.add((Process) processList.get(i));
			}
		}
		
	}
	
}
