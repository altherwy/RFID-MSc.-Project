package BulkSize;

import java.util.Calendar;
import java.util.Date;

/*
 * This class refers to the reader cycle
 */
public class ReaderCommands implements Runnable {
	public static volatile String readingStatus;
	public static volatile boolean CountinueRunning;
	//public int numberOfSamples;
	public static final String READING = "Sending Query To Tags";
	public static final String WAITING = "Waiting For Response From Tags";
	public static final String IDLE = "The Reader is Idle";
	public BulkSize bulkSize;
	public ExecuterStart EStart;
	public Calendar calendar;

	public ReaderCommands( BulkSize bulkSize, ExecuterStart EStart) {
		//this.numberOfSamples = numberOfSamples;
		this.bulkSize = bulkSize;
		this.EStart = EStart;
		this.CountinueRunning = true;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			while(CountinueRunning){
				readingStatus = ReaderCommands.READING;
				Thread.sleep(2);
				readingStatus = ReaderCommands.WAITING;
				Thread.sleep(5);
				readingStatus = ReaderCommands.READING;
				Thread.sleep(2);
				readingStatus = ReaderCommands.WAITING;
				Thread.sleep(5);
				readingStatus = ReaderCommands.IDLE;
				Thread.sleep(20);
			}
			bulkSize.display(EStart);
			System.out.println(" GOOOOOOOOOOOO Finished ");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
