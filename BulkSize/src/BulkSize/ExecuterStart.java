package BulkSize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.omg.CORBA.Current;

/*
 * WHAT TO CHANGE HERE ?
 * getTotalNumberofTags
 */
public class ExecuterStart implements Runnable {
	// public static final int TIME_TO_RANEG = 18000; // a tag travel time until
	// it
	// reaches the Reader Range.
	public static final int TIME_TO_LEAVE_RANEG = 400; // a tag time in the
	private AtomicInteger TOTAL_NUMBER_OF_TAGS; // Reader Range
	private AtomicInteger NUMBER_OF_TAGS_IN_INTERVAL1,
			NUMBER_OF_TAGS_IN_INTERVAL2, NUMBER_OF_TAGS_IN_INTERVAL3,
			NUMBER_OF_TAGS_IN_INTERVAL4, NUMBER_OF_TAGS_IN_INTERVAL5,
			NUMBER_OF_TAGS_IN_INTERVAL6;
	private AtomicInteger READ_NO_RESPONSE;
	private AtomicInteger MISSED_COMPLETELY;
	private AtomicInteger SUCCESS_RATE;
	private AtomicInteger FAILURE_RATE;
	// private AtomicInteger interval;
	private Random RInterval;
	public String tagName;
	public  volatile ArrayList<Thread> collection_Of_Threads;

	/*
	 * This class represents a tag cycle on the belt
	 */

	public ExecuterStart() {
		NUMBER_OF_TAGS_IN_INTERVAL1 = new AtomicInteger();
		NUMBER_OF_TAGS_IN_INTERVAL2 = new AtomicInteger();
		NUMBER_OF_TAGS_IN_INTERVAL3 = new AtomicInteger();
		NUMBER_OF_TAGS_IN_INTERVAL4 = new AtomicInteger();
		NUMBER_OF_TAGS_IN_INTERVAL5 = new AtomicInteger();
		NUMBER_OF_TAGS_IN_INTERVAL6 = new AtomicInteger();
		READ_NO_RESPONSE = new AtomicInteger();
		MISSED_COMPLETELY = new AtomicInteger();
		SUCCESS_RATE = new AtomicInteger();
		FAILURE_RATE = new AtomicInteger();
		TOTAL_NUMBER_OF_TAGS = new AtomicInteger();
		// interval = new AtomicInteger();
		RInterval = new Random();
		this.collection_Of_Threads = new ArrayList<Thread>();
	}

	@Override
	public void run() {
		try {
			this.reading();

		} catch (Exception e) {
			e.printStackTrace();
		}
		TOTAL_NUMBER_OF_TAGS.incrementAndGet();
		if (this.getTotalNumberOfTags() >= BulkSize.NUMBER_OF_TAGS) {
			ReaderCommands.CountinueRunning = false;
		}
		System.out.println(Thread.currentThread().getName() + " is Finished");
	}

	public  void reading() {
		boolean waitingForResponse = true; // just a parameter to stop the
		// while loop
		boolean readingNoResponse = false; // true, if a tag received the
		// query from the reader, but
		// went out of range before get
		// the chance to respond
		// False, otherwise
		boolean enterWaiting = false;
		boolean allowDecrement = false;
		boolean needReading = true;
		boolean missedCompletely = true; // true if the tag went out of the
		// range without even getting a
		// one query from reader
		int interval;
		interval = RInterval.nextInt(4) + 1;
		System.out.println("Waiting " + interval);
		long endTime = System.currentTimeMillis() + TIME_TO_LEAVE_RANEG;
		while (timeInRangeOver(endTime) == false) {
			if (ReaderCommands.readingStatus == ReaderCommands.READING
					&& (needReading)) {
				readingNoResponse = true;
				missedCompletely = false;
				while (waitingForResponse && timeInRangeOver(endTime) == false) {

					if (interval == 1
							&& ReaderCommands.readingStatus == ReaderCommands.WAITING1) {
						NUMBER_OF_TAGS_IN_INTERVAL1.incrementAndGet();
						this.successRate(1);
						enterWaiting = true;
					}
					if (interval == 2
							&& ReaderCommands.readingStatus == ReaderCommands.WAITING2) {
						NUMBER_OF_TAGS_IN_INTERVAL2.incrementAndGet();
						this.successRate(2);
						enterWaiting = true;
					}
					if (interval == 3
							&& ReaderCommands.readingStatus == ReaderCommands.WAITING3) {
						NUMBER_OF_TAGS_IN_INTERVAL3.incrementAndGet();
						this.successRate(3);
						enterWaiting = true;
					}
					if (interval == 4
							&& ReaderCommands.readingStatus == ReaderCommands.WAITING4) {
						NUMBER_OF_TAGS_IN_INTERVAL4.incrementAndGet();
						this.successRate(4);
						enterWaiting = true;
					}
					/*
					 * if (interval == 5 && ReaderCommands.readingStatus ==
					 * ReaderCommands.WAITING5) {
					 * NUMBER_OF_TAGS_IN_INTERVAL5.incrementAndGet();
					 * this.successRate(5); enterWaiting = true; } if (interval
					 * == 6 && ReaderCommands.readingStatus ==
					 * ReaderCommands.WAITING6) {
					 * NUMBER_OF_TAGS_IN_INTERVAL6.incrementAndGet();
					 * this.successRate(6); enterWaiting = true; }
					 */
					if (enterWaiting) {
						waitingForResponse = false;
						readingNoResponse = false;
						needReading = false;
						enterWaiting = false;
						allowDecrement = true;
					}
				}
			}
		}
		if (interval == 1 && allowDecrement)
			NUMBER_OF_TAGS_IN_INTERVAL1.decrementAndGet();
		if (interval == 2 && allowDecrement)
			NUMBER_OF_TAGS_IN_INTERVAL2.decrementAndGet();
		if (interval == 3 && allowDecrement)
			NUMBER_OF_TAGS_IN_INTERVAL3.decrementAndGet();
		if (interval == 4 && allowDecrement)
			NUMBER_OF_TAGS_IN_INTERVAL4.decrementAndGet();
		if (interval == 5 && allowDecrement)
			NUMBER_OF_TAGS_IN_INTERVAL5.decrementAndGet();
		if (interval == 6 && allowDecrement)
			NUMBER_OF_TAGS_IN_INTERVAL6.decrementAndGet();
		if (readingNoResponse)
			READ_NO_RESPONSE.incrementAndGet();
		if (missedCompletely)
			MISSED_COMPLETELY.incrementAndGet();
	}

	public synchronized boolean timeInRangeOver(long EndTime) {
		long startTime = System.currentTimeMillis();
		if (startTime <= EndTime)
			return false;
		else
			return true;
	}

	public void successRate(int currentInterval) {
		if (currentInterval == 1) {
			if (getTagsIn1() == 1) {
				System.out.println(Thread.currentThread().getName()
						+ " succedded");
				SUCCESS_RATE.incrementAndGet();
			} else
				FAILURE_RATE.incrementAndGet();
		}
		if (currentInterval == 2) {
			if (getTagsIn2() == 1) {
				System.out.println(Thread.currentThread().getName()
						+ " succedded");
				SUCCESS_RATE.incrementAndGet();
			} else
				FAILURE_RATE.incrementAndGet();
		}
		if (currentInterval == 3) {
			if (getTagsIn3() == 1) {
				System.out.println(Thread.currentThread().getName()
						+ " succedded");
				SUCCESS_RATE.incrementAndGet();
			} else
				FAILURE_RATE.incrementAndGet();
		}
		if (currentInterval == 4) {
			if (getTagsIn4() == 1) {
				System.out.println(Thread.currentThread().getName()
						+ " succedded");
				SUCCESS_RATE.incrementAndGet();
			} else
				FAILURE_RATE.incrementAndGet();
		}
		if (currentInterval == 5) {
			if (getTagsIn5() == 1) {
				System.out.println(Thread.currentThread().getName()
						+ " succedded");
				SUCCESS_RATE.incrementAndGet();
			} else
				FAILURE_RATE.incrementAndGet();
		}
		if (currentInterval == 6) {
			if (getTagsIn6() == 1) {
				System.out.println(Thread.currentThread().getName()
						+ " succedded");
				SUCCESS_RATE.incrementAndGet();
			} else
				FAILURE_RATE.incrementAndGet();
		}
		while(ReaderCommands.readingStatus != 11 ){	
		}
		

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

	/*
	 * public int getTagsInRange() { return this.NUMBER_OF_TAGS_IN_RANGE.get();
	 * }
	 */

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

	public int getTotalNumberOfTags() {
		return this.TOTAL_NUMBER_OF_TAGS.get();
	}

	public int getTagsIn1() {
		return this.NUMBER_OF_TAGS_IN_INTERVAL1.get();
	}

	public int getTagsIn2() {
		return this.NUMBER_OF_TAGS_IN_INTERVAL2.get();
	}

	public int getTagsIn3() {
		return this.NUMBER_OF_TAGS_IN_INTERVAL3.get();
	}

	public int getTagsIn4() {
		return this.NUMBER_OF_TAGS_IN_INTERVAL4.get();
	}

	public int getTagsIn5() {
		return this.NUMBER_OF_TAGS_IN_INTERVAL5.get();
	}

	public int getTagsIn6() {
		return this.NUMBER_OF_TAGS_IN_INTERVAL6.get();
	}
}
