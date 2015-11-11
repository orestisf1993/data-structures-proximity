package gr.auth.ee.dsproject.proximity.defplayers;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

public class RandomPlayer implements AbstractPlayer {
	final static int[][] addOnEven = { { 1, 0 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 }, { 0, -1 } };
	final static int[][] addOnOdd = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, -1 } };

	static public int[][] getNeighborsCoordinates(Board board, int x, int y) {
		int[][] neighbors = new int[6][2];
		int[][] adder = (y % 2 == 0) ? addOnEven : addOnOdd;
		for (int i = 0; i < 6; i++) {
			int xn = x + adder[i][0];
			int yn = y + adder[i][1];
			if (board.isInsideBoard(xn, yn)) {
				neighbors[i][0] = xn;
				neighbors[i][1] = yn;
			} else {
				neighbors[i][0] = -1;
				neighbors[i][1] = -1;
			}
		}
		return neighbors;
	}

	int id;
	String name;
	int numOfTiles;
	int score;

	public RandomPlayer(Integer pid) {
		super();
		id = pid;
	}

	public RandomPlayer(Integer pid, String pname, int pscore, int pnumOfTiles) {
		super();
		id = pid;
		name = pname;
		score = pscore;
		numOfTiles = pnumOfTiles;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int[] getNextMove(Board board) {
		boolean tileIsNotEmpty;
		int x, y;
		do {
			x = (int) (Math.random() * ProximityUtilities.NUMBER_OF_COLUMNS);
			y = (int) (Math.random() * ProximityUtilities.NUMBER_OF_ROWS);

			Tile t = board.getTile(x, y);
			tileIsNotEmpty = (t.getPlayerId() != 0);
		} while (tileIsNotEmpty);
		int[] nextMove = { x, y };
		return nextMove;
	}

	public int getNumOfTiles() {
		return numOfTiles;
	}

	public int getScore() {
		return score;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumOfTiles(int numOfTiles) {
		this.numOfTiles = numOfTiles;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
