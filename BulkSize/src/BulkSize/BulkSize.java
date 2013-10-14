package BulkSize;

import java.util.ArrayList;
import java.util.Random;

public class BulkSize {
	/*
	 * public static volatile int NUMBER_OF_TAGS_IN_RANGE = 0; public static
	 * volatile int SUCCESS_RATE = 0; public static volatile int FAILURE_RATE =
	 * 0; public static volatile int READ_NO_RESPONSE = 0; public static
	 * volatile int MISSED_COMPLETELY = 0;
	 */
	public static final int NUMBER_OF_TAGS = 1000;
	public ReaderCommands startSample;
	public ArrayList<Integer> randomDistances;
	public static Thread threadOne;

	public BulkSize() {
		this.randomDistances = new ArrayList<Integer>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BulkSize instance = new BulkSize();
		ExecuterStart EStart = new ExecuterStart();
		ArrayList<Thread> tags = new ArrayList<Thread>();
		instance.startSample = new ReaderCommands(instance,EStart);
		instance.generateRandomVariables(4, 1000);
		Thread firstReader = new Thread(instance.startSample);
		firstReader.start();
		threadOne = new Thread(EStart);
		threadOne.start();
		try {
			tags = instance.initiateTags(EStart);
			for (int i = 0; i <1000; i++) {
				Thread.sleep(instance.randomDistances.get(i));
				(tags.get(i)).start();
				System.out.println("The Distance btw " + (i - 1) + " and " + i
						+ " " + instance.randomDistances.get(i));
			}
			firstReader = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//instance.display(EStart);

	}

	public void generateRandomVariables(int maximumDistance,
			int maximumNumberOfTags) {
		Random firstNumber = new Random();
		Random secondNumber = new Random();
		/*
		 * int rangeMin = 1; int rangeMax = 21; for (int i=0;i <20;i++){ double
		 * randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		 * System.out.println(randomValue); }
		 */
		Integer a, b;
		for (int i = 1; i <= maximumNumberOfTags; i++) {
			a = firstNumber.nextInt(maximumDistance);
			// b = secondNumber.nextInt(100);
			// String finalNumber = a.toString() + "."+b.toString();
			this.randomDistances.add(a * 10);
		}
	}

	public  void display(ExecuterStart EStart) {
		System.out.println("Sucees Rate " + EStart.getSuccessRate()
				+ "  Failure Rate " + EStart.getFailureRate()
				+ "  Read but Ignore " + EStart.getReadNoResponse()
				+ "  Missed " + EStart.getMissed());

	}

	public ArrayList<Thread> initiateTags(ExecuterStart EStart) {
		ArrayList<Thread> listOfTags = new ArrayList<Thread>();
		for (Integer i = 1; i <= BulkSize.NUMBER_OF_TAGS; i++) {
			listOfTags.add(new Thread(EStart));
		}
		return listOfTags;
	}
}
