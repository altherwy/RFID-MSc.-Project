package BulkSize;

import java.util.Calendar;
import java.util.Date;

/*
 * This class refers to the reader cycle
 */
public class ReaderCommands implements Runnable {
	public static volatile int readingStatus;
	public static volatile boolean CountinueRunning;
	//public int numberOfSamples;
	public static final int READING = 0; // Reading 
	//public static final String WAITING = "Waiting For Response From Tags";
	public static final int WAITING1 = 1;
	public static final int WAITING2 = 2;
	public static final int WAITING3 = 3;
	public static final int WAITING4 = 4;
	public static final int WAITING5 = 5;
	public static final int WAITING6 = 6;
	public static final int MUTE = 11;
	public static final int IDLE = 10;
	public BulkSize bulkSize;
	public ExecuterStart EStart;
	public Calendar calendar;

	public ReaderCommands( BulkSize bulkSize, ExecuterStart EStart) {
		//this.numberOfSamples = numberOfSamples;
		this.bulkSize = bulkSize;
		this.EStart = EStart;
		ReaderCommands.CountinueRunning = true;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			while(CountinueRunning){
				readingStatus = ReaderCommands.READING;
				Thread.sleep(30);
				readingStatus = ReaderCommands.WAITING1;
				Thread.sleep(50);
				readingStatus = ReaderCommands.MUTE;
				Thread.sleep(30);
				readingStatus = ReaderCommands.WAITING2;
				Thread.sleep(50);
				readingStatus = ReaderCommands.MUTE;
				Thread.sleep(30);
				readingStatus = ReaderCommands.WAITING3;
				Thread.sleep(50);
				readingStatus = ReaderCommands.MUTE;
				Thread.sleep(30);
				readingStatus = ReaderCommands.WAITING4;
				Thread.sleep(50);
				readingStatus = ReaderCommands.MUTE;
				Thread.sleep(30);
				/*readingStatus = ReaderCommands.WAITING5;
				Thread.sleep(40);
				readingStatus = ReaderCommands.WAITING6;
				Thread.sleep(40);*/
				readingStatus = ReaderCommands.IDLE;
				Thread.sleep(100);
			}
			bulkSize.display(EStart);
			System.out.println(" GOOOOOOOOOOOO Finished ");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
