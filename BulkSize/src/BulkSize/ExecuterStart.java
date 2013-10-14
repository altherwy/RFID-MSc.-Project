package BulkSize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecuterStart implements Runnable {
	//public static final int TIME_TO_RANEG = 18000; // a tag travel time until it
													// reaches the Reader Range.
	public static final int TIME_TO_LEAVE_RANEG =35; // a tag time in the
	private AtomicInteger TOTAL_NUMBER_OF_TAGS;												// Reader Range
	private AtomicInteger NUMBER_OF_TAGS_IN_RANGE;
	private AtomicInteger READ_NO_RESPONSE;
	private AtomicInteger MISSED_COMPLETELY;
	private AtomicInteger SUCCESS_RATE;
	private AtomicInteger FAILURE_RATE;
	public String tagName;

	/*
	 * This class represents a tag cycle on the belt
	 */

	public ExecuterStart() {
		NUMBER_OF_TAGS_IN_RANGE = new AtomicInteger();
		READ_NO_RESPONSE = new AtomicInteger();
		MISSED_COMPLETELY = new AtomicInteger();
		SUCCESS_RATE = new AtomicInteger();
		FAILURE_RATE = new AtomicInteger();
		TOTAL_NUMBER_OF_TAGS = new AtomicInteger();
	}

	@Override
	public void run() {
		try {
			boolean waitingForResponse = true; // just a parameter to stop the
												// while loop
			boolean readingNoResponse = false; // true, if a tag received the
												// query from the reader, but
												// went out of range before get
												// the chance to respond
			// False, otherwise
			boolean needReading = true;
			boolean missedCompletely = true; // true if the tag went out of the
												// range without even getting a
												// one query from reader
			NUMBER_OF_TAGS_IN_RANGE.incrementAndGet();
			TOTAL_NUMBER_OF_TAGS.incrementAndGet();
			long endTime = System.currentTimeMillis() + TIME_TO_LEAVE_RANEG;
			while (timeInRangeOver(endTime) == false) {
				if (ReaderCommands.readingStatus.equals(ReaderCommands.READING)
						&& (needReading)) {
					readingNoResponse = true;
					missedCompletely = false;
					while (waitingForResponse
							&& timeInRangeOver(endTime) == false) {

						if (ReaderCommands.readingStatus
								.equals(ReaderCommands.WAITING)) {

							this.successRate();
							waitingForResponse = false;
							readingNoResponse = false;
							needReading = false;
						}
					}
				}
			}
			NUMBER_OF_TAGS_IN_RANGE.decrementAndGet();
			if (readingNoResponse)
				READ_NO_RESPONSE.incrementAndGet();
			if (missedCompletely)
				MISSED_COMPLETELY.incrementAndGet();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.getTotalNumberOfTags() >= 1001){
			ReaderCommands.CountinueRunning = false;
		}
		
	}

	public boolean timeInRangeOver(long EndTime) {
		long startTime = System.currentTimeMillis();
		if (startTime <= EndTime)
			return false;
		else
			return true;
		// long differenceTime = endTime - startTime;
		// System.out.println(TimeUnit.MILLISECONDS.toSeconds(differenceTime));
	}

	public void successRate() {
		if (getTagsInRange() == 1)
			SUCCESS_RATE.incrementAndGet();
		else
			FAILURE_RATE.incrementAndGet();
	}

	public void stepByStep(String tagName, long currentTime, long endTime) {
		/*
		 * System.out.println("Current Status for " + tagName + " is: " +
		 * this.reader.readingStatus + " at Time:" + currentTime +
		 * " where end time is: " + endTime);
		 */
		/*
		 * try {
		 * 
		 * String content = "Current Status for " + tagName + " is: " +
		 * this.reader.readingStatus + " at Time:" + currentTime +
		 * " where end time is: " + endTime;
		 * 
		 * // if file doesnt exists, then create it // if (!file.exists()) { //
		 * file.createNewFile(); // } if (tagName.equals("Tag_1"))
		 * bw.write(content + "\n"); else bw2.write(content + "\n");
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 */
	}

	public int getTagsInRange() {
		return this.NUMBER_OF_TAGS_IN_RANGE.get();
	}

	public int getSuccessRate() {
		return this.SUCCESS_RATE.get();
	}

	public int getFailureRate() {
		return this.FAILURE_RATE.get();
	}

	public int getMissed() {
		return this.MISSED_COMPLETELY.get();
	}

	public int getReadNoResponse() {
		return this.READ_NO_RESPONSE.get();
	}
	public int getTotalNumberOfTags(){
		return this.TOTAL_NUMBER_OF_TAGS.get();
	}
}
