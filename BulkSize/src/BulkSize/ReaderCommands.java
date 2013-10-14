package BulkSize;

import java.util.Calendar;
import java.util.Date;

/*
 * This class refers to the reader cycle
 */
public class ReaderCommands implements Runnable {
	public static volatile String readingStatus;
	public int numberOfSamples;
	public static final String READING = "Sending Query To Tags";
	public static final String WAITING = "Waiting For Response From Tags";
	public static final String IDLE = "The Reader is Idle";
	public BulkSize bulkSize;
	public ExecuterStart EStart;
	public Calendar calendar;

	public ReaderCommands(int numberOfSamples, BulkSize bulkSize, ExecuterStart EStart) {
		this.numberOfSamples = numberOfSamples;
		this.bulkSize = bulkSize;
		this.EStart = EStart;
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {

			for (int i = 1; i <= numberOfSamples; i++) {
				readingStatus = ReaderCommands.READING;
				Thread.sleep(200);
				readingStatus = ReaderCommands.WAITING;
				Thread.sleep(500);
				readingStatus = ReaderCommands.READING;
				Thread.sleep(200);
				readingStatus = ReaderCommands.WAITING;
				Thread.sleep(500);
				readingStatus = ReaderCommands.IDLE;
				Thread.sleep(2000);
			}
			bulkSize.display(EStart);
			System.out.println(" GOOOOOOOOOOOO Finished ");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
