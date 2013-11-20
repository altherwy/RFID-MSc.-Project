package RFIDSimulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.naming.Context;
import javax.swing.JPanel;
import javax.swing.Box.Filler;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class RFIDSimulator {

	public static final String xml_File_Name = "src/RFIDSimulator/FloorPlan.xml";
	// public static final int START_ROW = 6; //1
	// public static final int START_COLUMN = 10; //0
	// public static final int END_ROW = 19;//5
	// public static final int END_COLUMN = 29;//0
	public Vector<Integer> numbersForSort = new Vector<Integer>();
	public Vector<Vector<Integer>> RFIDPlaces = new Vector<Vector<Integer>>();

	public RFIDSimulator() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RFIDSimulator RFID = new RFIDSimulator();
		// Vector<ArrayList<Vector<Integer>>> savePaths = new
		// Vector<ArrayList<Vector<Integer>>>(); // to save all routes for
		// different
		// ... end and start points
		Vector<Vector<Integer>> startandEnd = RFID.fillStartandEndPoints();
		// System.out.println("Size "+ startandEnd.size());
		for (int index = 0; index < startandEnd.size(); index++) {
			AStarAlgorithm AStar = new AStarAlgorithm();
			ArrayList<Vector<Integer>> path = new ArrayList<Vector<Integer>>();
			Vector<Integer> collection = new Vector<Integer>();
			collection = startandEnd.get(index);

			path = AStar.findPath(collection.get(0), collection.get(1),
					collection.get(2), collection.get(3));
			// Path A starts from the target all the way to the start
			// System.out.println("Start   *** "+
			// collection.get(0)+"  "+collection.get(1));
			path = RFID.finalSolution(RFID.swap(path), collection.get(0),
					collection.get(1));
			if (path == null)
				System.out.println("No cells in the path!!");
			System.out.println("---------------------------");
			for (int i = 0; i < path.size(); i++) {
				System.out.println("Row: " + path.get(i).get(1) + "Column: "
						+ path.get(i).get(2));
			}
			System.out.println("*****************************");
			// savePaths.add(path);
			RFID.extractNumbers(path);
		}
		Collections.sort(RFID.numbersForSort);
		/*for (int x = 0; x < RFID.numbersForSort.size(); x++)
			System.out.println("* " + RFID.numbersForSort.get(x) + " "
					+ RFID.numbersForSort.size());*/
		// //// ********** ///////
		 RFID.RFIDPlaces = RFID.orderCells();
		 System.out.println("*****************************");
		 for (int j = 0; j < RFID.RFIDPlaces.size(); j++)
				System.out.println(RFID.RFIDPlaces.get(j));

	}

	/*
	 * (modify the given list of cells so that it start form the beginning all
	 * the way to the target)
	 */
	public ArrayList<Vector<Integer>> swap(ArrayList<Vector<Integer>> path) {
		ArrayList<Vector<Integer>> route = new ArrayList<Vector<Integer>>();
		for (int i = path.size() - 1; i >= 0; i--) {
			route.add(path.get(i));
		}
		return route;
	}

	/*
	 * return the final route from the start to the target
	 */

	public ArrayList<Vector<Integer>> finalSolution(
			ArrayList<Vector<Integer>> pathA, int startRow, int startColumn) {
		Vector<Integer> tempA = new Vector<Integer>();
		Vector<Integer> tempB = new Vector<Integer>();
		ArrayList<Vector<Integer>> theRoute = new ArrayList<Vector<Integer>>();
		int index = 0;
		tempA = pathA.get(index);
		theRoute.add(tempA);
		while (index + 1 < pathA.size()) {
			tempA = pathA.get(index);
			tempB = pathA.get(index + 1);
			if (this.isNeighbour(tempA, tempB)) {
				index++;
				theRoute.add(tempB);
			} else {
				theRoute = findPathtoTheStartPoint(tempB, startRow,
						startColumn, pathA);
				if (theRoute == null) {
					System.out.println("Route To Parent is null!!! ");
					return null;
				}
				index++;
			}

		}
		return theRoute;
	}

	/*
	 * Find if two cells are neighbours ( one of them lies on the other 9
	 * neighbours cells)
	 */
	public boolean isNeighbour(Vector<Integer> cellA, Vector<Integer> cellB) {
		int ARow = cellA.get(1);
		int AColumn = cellA.get(2);
		int BRow = cellB.get(1);
		int BColumn = cellB.get(2);
		if (ARow == BRow || ARow == BRow - 1 || ARow == BRow + 1) {
			if (AColumn == BColumn || AColumn == BColumn - 1
					|| AColumn == BColumn + 1) {

				return true;
			}
		}
		return false;

	}

	/*
	 * Find the route back to the start point for the given cell
	 */
	public ArrayList<Vector<Integer>> findPathtoTheStartPoint(
			Vector<Integer> current, int startRow, int startColumn,
			ArrayList<Vector<Integer>> list) {

		ArrayList<Vector<Integer>> routetoParent = new ArrayList<Vector<Integer>>();
		int row = current.get(1);
		int column = current.get(2);
		while (true) {
			routetoParent.add(current);
			/*
			 * System.out.println("YRS " + current.get(0) + " / " +
			 * current.get(1) + "/" + current.get(2) + "/" + current.get(3) +
			 * "/  " + current.get(4) + " / " + current.get(5) + "/ " +
			 * current.get(6) + "/ " + current.get(7));
			 */
			current = this.returnParentVector(current, list);
			if (current == null) {
				System.out.println("NO Parent !!!!");
			}
			row = current.get(1);
			column = current.get(2);
			if (row == startRow && column == startColumn) {
				routetoParent.add(current);

				return swap(routetoParent);
			}

		}
	}

	/*
	 * public ArrayList<Vector<Integer>> returnNewRoute(Vector<Integer>
	 * stranger, ArrayList<Vector<Integer>> route) {
	 * 
	 * int index = route.size() - 1;
	 * 
	 * while (!route.isEmpty()) { Vector<Integer> temp = route.get(index); if
	 * (this.isParent(stranger, temp)) { route.add(stranger); //
	 * System.out.println("Right"); return route; } else { route.remove(index);
	 * index = route.size() - 1; }
	 * 
	 * } return null; }
	 */
	/*
	 * Return the parent of the current cell
	 */

	public boolean isParent(Vector<Integer> son, Vector<Integer> parent) {
		int sonParentRow = son.get(3);
		int sonParentColumn = son.get(4);
		int parentRow = parent.get(1);
		int parentColumn = parent.get(2);
		if (sonParentRow == parentRow && sonParentColumn == parentColumn)
			return true;
		return false;
	}

	/*
	 * Return the parent of the given cell
	 */
	public Vector<Integer> returnParentVector(Vector<Integer> current,
			ArrayList<Vector<Integer>> list) {

		for (int index = 0; index < list.size(); index++) {
			if (list.get(index).get(1) == current.get(3)
					&& list.get(index).get(2) == current.get(4))
				return list.get(index);
		}
		return null;
	}

	/*
	 * add collections of start and end points
	 */
	public Vector<Vector<Integer>> fillStartandEndPoints() {
		Vector<Vector<Integer>> save = new Vector<Vector<Integer>>();
		save.add(fillStartandEndPointsHelper(12, 8, 19, 20));
		save.add(fillStartandEndPointsHelper(19, 20, 9, 32));
		save.add(fillStartandEndPointsHelper(9, 32, 4, 32));
		save.add(fillStartandEndPointsHelper(4, 32, 4, 19));
		save.add(fillStartandEndPointsHelper(4, 19, 2, 30));
		save.add(fillStartandEndPointsHelper(2, 30, 19, 29));
		save.add(fillStartandEndPointsHelper(19, 29, 7, 8));
		save.add(fillStartandEndPointsHelper(7, 8, 1, 3));
		save.add(fillStartandEndPointsHelper(1, 3, 7, 2));
		save.add(fillStartandEndPointsHelper(7, 2, 12, 4));
		Vector<Vector<Integer>> temp = this.readFromFile();
		// ****** The Second Person *************/
		for (int index = 0; index < temp.size(); index++)
			save.add(fillStartandEndPointsHelper(temp.get(index).get(0), temp
					.get(index).get(1), temp.get(index).get(2), temp.get(index)
					.get(3)));
		return save;

	}

	/*
	 * Help fillStartandEndPoints method to do its job
	 */
	public Vector<Integer> fillStartandEndPointsHelper(int startRow,
			int startcolumn, int endRow, int endColumn) {
		Vector<Integer> StartAndEnd = new Vector<Integer>();
		StartAndEnd.add(startRow);
		StartAndEnd.add(startcolumn);
		StartAndEnd.add(endRow);
		StartAndEnd.add(endColumn);
		return StartAndEnd;
	}

	/*
	 * combine row and column as "Row Column" integer for sorting purposes
	 */
	public void extractNumbers(ArrayList<Vector<Integer>> path) {

		for (int i = 0; i < path.size(); i++) {
			Integer row = path.get(i).get(1);
			Integer column = path.get(i).get(2);
			String temp = row.toString() + column.toString();
			if (temp.length() == 3) {
				if (row.toString().length() == 1)
					temp = "0" + row.toString() + column.toString();
				else
					temp = row.toString() + "0" + column.toString();
			}
			numbersForSort.add(Integer.parseInt(temp));
		}
	}

	/*
	 * Read from file, start and end points
	 */

	public Vector<Vector<Integer>> readFromFile() {
		Vector<Vector<Integer>> temp = new Vector<Vector<Integer>>();
		BufferedReader br = null;
		Vector<Integer> points = new Vector<Integer>();

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(
					"/Users/youssef_mac/Desktop/startandend.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
				if (!sCurrentLine.equals("**")) {
					points.add(Integer.parseInt(sCurrentLine.trim()));
				} else {
					temp.add(points);
					points = new Vector<Integer>();
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return temp;

	}

	/*
	 * Order cells based on the popularity
	 */
	public Vector<Vector<Integer>> orderCells() {
		int count = 1;
		Vector<Vector<Integer>> ordered = new Vector<Vector<Integer>>();

		for (int i = 0; i < numbersForSort.size(); i++) {
			Vector<Integer> temp = new Vector<Integer>();
			if (i != numbersForSort.size() - 1) {
				int firstnumber = numbersForSort.get(i);
				int secondnumber = numbersForSort.get(i + 1);
				if (firstnumber == secondnumber) {
					count++;
				} else {
					temp.add(numbersForSort.get(i));
					temp.add(count);
					ordered.add(temp);
					count =1;
				}
			}
			else
			{
				temp.add(numbersForSort.get(i));
				temp.add(count);
				ordered.add(temp);
				count =1;
			}
		}
		return ordered;
	}
	

}
