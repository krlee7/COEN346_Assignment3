import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Process extends Thread{

	private int id;
	private long readyTime;
	private long serviceTime;
	ArrayList<Command> commandList;

	// memory containers
	private Variable[] mainMemory;
	File disk; // filepath to our file on our system
	File outputTextFile; // file to output our results
	long realStartTime;

	Semaphore mutex = new Semaphore(1);

	@Override
	public void run(){
		long startTime = System.currentTimeMillis();
		long processEndTime = startTime + this.serviceTime;

		System.out.println("Time: " + (System.currentTimeMillis() - realStartTime) + ", Process " + this.id + " started ");

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(outputTextFile,true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // true flag to not overwrite file
		String lineString = "Clock: " + Long.toString(startTime - realStartTime) + ", " + "Process " + Integer.toString(id) + ": started";

		try {
			fileWriter.write(lineString + System.getProperty("line.separator"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			fileWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(System.currentTimeMillis() <= processEndTime){

			//System.out.println(System.currentTimeMillis() / 1000);

			try {
				if (!commandList.isEmpty()) {
					mutex.acquire();
					Command firstCommand = commandList.get(0);

					//System.out.println("Process " + this.id + " ," + firstCommand.commandType + ": Variable " + firstCommand.variableID + " ,Value: " + firstCommand.value );
					try {
						TimeUnit.MILLISECONDS.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					commandList.remove(0);    //Remove first element in list
					firstCommand.doCommand(mainMemory, disk, outputTextFile, realStartTime, this.id);    //Needs implementation


				}
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
		System.out.println("Time: " + (System.currentTimeMillis() - realStartTime) + ", Process " + this.id + " finished ");


		FileWriter fileWriterFin = null;
		try {
			fileWriterFin = new FileWriter(outputTextFile,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // true flag to not overwrite file

		String lineStringFin = "Clock: " + Long.toString(System.currentTimeMillis() - realStartTime) + ", " + "Process " + Integer.toString(id) + ": finished";



		try {
			fileWriterFin.write(lineStringFin + System.getProperty("line.separator"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileWriterFin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void setStartTime(long realStartTime){
		this.realStartTime = realStartTime;
	}

}

// [CHANGES MADE]

/*
- change duration -> serviceTime
- deleted command from constructor
*/