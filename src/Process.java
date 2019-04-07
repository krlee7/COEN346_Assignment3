import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Process extends Thread{
	
	private int id;
	private long readyTime;
	private long serviceTime;
	ArrayList<Command> commandList;
	
	//ArrayList<String> commandList;	//Needs read file
	Semaphore mutex = new Semaphore(1);
	
	@Override
	public void run(){
		long startTime = System.currentTimeMillis();
		long processEndTime = startTime + this.serviceTime;
		
		while(System.currentTimeMillis() != processEndTime){
			
			try{
				mutex.acquire();
				Command firstCommand = this.commandList.getFirst();	//Needs implementation
				firstCommand.doCommand();	//Needs implementation
				commandList.remove(0);	//Remove first element in list
				mutex.release();
			}catch(Exception ex){
				ex.printStackTrace();
			} 
			
			
		}
		
	}
	
	public Process(int id, long readyTime, long serviceTime){
		this.id = id;
		this.readyTime = readyTime;
		this.serviceTime = serviceTime;
		this.commandList = commandList;
		
	}

	public long getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(long readyTime) {
		this.readyTime = readyTime;
	}
	
	public void setCommandList(ArrayList commandList){
		this.commandList = commandList;
	}
	

	
}

// [CHANGES MADE]

/*
- change duration -> serviceTime
- deleted command from constructor
*/