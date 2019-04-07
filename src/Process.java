import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Process extends Thread{
	
	private int id;
	private long readyTime;
	private long serviceTime;
	private Command command;	//Needs implementation
	
	ArrayList<String> commandList;	//Needs read file
	Semaphore mutex = new Semaphore(1);
	
	@Override
	public void run(){
		long startTime = System.currentTimeMillis();
		long processEndTime = startTime + this.serviceTime;
		
		while(System.currentTimeMillis() != processEndTime){
			
			try{
				mutex.acquire();
				Command firstCommand = commandList.getFirst();	//Needs implementation
				firstCommand.doCommand();	//Needs implementation
				commandList.remove(0);	//Remove first element in list
			}finally{
				mutex.release();
			} 
			
			
		}
		
	}
	
	public Process(int id, long readyTime, long serviceTime){
		this.id = id;
		this.setReadyTime(readyTime);
		this.serviceTime = serviceTime;
		
	}

	public long getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(long readyTime) {
		this.readyTime = readyTime;
	}
	
}

// [CHANGES MADE]

/*
- change duration -> serviceTime
- deleted command from constructor
*/