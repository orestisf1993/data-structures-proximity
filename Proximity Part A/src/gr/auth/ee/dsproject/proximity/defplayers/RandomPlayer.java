package gr.auth.ee.dsproject.proximity.defplayers;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

/**
 * The RandomPlayer class implements a player for the game that randomly chooses
 * the next tile.
 * 
 * @author orestis
 * @author ioanna
 *
 */
public class RandomPlayer implements AbstractPlayer {

	// offsets of the neighboring tiles
	/** The Constant addOnEven used for even y coordinates. */
	final static int[][] addOnEven = { { 1, 0 }, { 0, 1 }, { -1, 1 }, { -1, 0 }, { -1, -1 }, { 0, -1 } };

	/** The Constant addOnOdd used for odd y coordinates. */
	final static int[][] addOnOdd = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, -1 } };

	/**
	 * Returns a 2-D array with the coordinates of the 6 neighbors of the tile
	 * at x,y.
	 *
	 * @param board
	 *            the board
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @return the neighbor's coordinates
	 */
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

	/** The id of the player. */
	int id;

	/** The name of the player. */
	String name;

	/** The number of tiles. */
	int numOfTiles;

	/** The current score. */
	int score;

	/**
	 * Instantiates a new random player.
	 *
	 * @param pid
	 *            the id of the player
	 */
	public RandomPlayer(Integer pid) {
		super();
		id = pid;
	}

	/**
	 * Instantiates a new random player.
	 *
	 * @param pid
	 *            the id of the player
	 * @param pname
	 *            the name of the player
	 * @param pscore
	 *            the score of the player
	 * @param pnumOfTiles
	 *            the number of tiles
	 */
	public RandomPlayer(Integer pid, String pname, int pscore, int pnumOfTiles) {
		super();
		id = pid;
		name = pname;
		score = pscore;
		numOfTiles = pnumOfTiles;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param board
	 *            The board of the game
	 * @return x,y coordinates of the next move
	 */
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

	/**
	 * @return the number of tiles
	 */
	public int getNumOfTiles() {
		return numOfTiles;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param id
	 *            the id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param numOfTiles
	 *            the number of tiles
	 */
	public void setNumOfTiles(int numOfTiles) {
		this.numOfTiles = numOfTiles;
	}

	/**
	 * @param score
	 *            the score
	 */
	public void setScore(int score) {
		this.score = score;
	}
}
