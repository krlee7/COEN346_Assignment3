import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Controller extends Thread{
	Queue<Process> readyQueue = new LinkedList<>();
	ArrayList<String> commandList;	//Read file
	ArrayList<Process> processList;	//Read file
	
	long startTime;
	
	@Override
	public void run(){
		startTime = System.currentTimeMillis();
		while(!commandList.isEmpty()){
			if(checkReadyQueue(readyQueue)){
				Process currentProcess = getFrontProcess(readyQueue);
				currentProcess.start();
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
			if((startTime-System.currentTimeMillis()) == ((Process) processList.get(i)).getReadyTime()){
				readyQueue.add((Process) processList.get(i));
			}
		}
		
	}
	
}
