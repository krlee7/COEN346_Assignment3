import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Process extends Thread{
	
	private int id;
	private long readyTime;
	private long serviceTime;
	ArrayList<Command> commandList;
	
	// memory containers
	private Variable[] mainMemory;
	File disk; // filepath to our file on our system
	File outputTextFile; // file to output our results
	
	Semaphore mutex = new Semaphore(1);
	
	@Override
	public void run(){
		long startTime = System.currentTimeMillis();
		long processEndTime = startTime + this.serviceTime;
				
		while(System.currentTimeMillis() <= processEndTime){
			
			try{
				mutex.acquire();
				Command firstCommand = commandList.get(0);
				firstCommand.doCommand(mainMemory,  disk,  outputTextFile);	//Needs implementation
				commandList.remove(0);	//Remove first element in list
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	// SET 
	public void setCommandList(ArrayList<Command> commandList) {
		
		this.commandList = commandList;
	}
	
	public void setMainMemory(Variable[] mainMemory) {
		
		this.mainMemory = mainMemory;
	}

	public void setDisk(File disk) {
	
		this.disk = disk;
	}
	
	public void setOutputTextFile(File outputTextFile) {
		
		this.outputTextFile = outputTextFile;
	}
	
	// print
	public void print() {
		
		System.out.print("Process : ");
		System.out.print(id);
		System.out.print(" ");
		System.out.print(readyTime);
		System.out.print(" ");
		System.out.println(serviceTime);
	}
	
}

// [CHANGES MADE]

/*
- change duration -> serviceTime
- deleted command from constructor
*/