package gr.auth.ee.dsproject.proximity.defplayers;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.Tile;

public class HeuristicPlayer implements AbstractPlayer {

	int score;
	int id;
	String name;
	int numOfTiles;

	public HeuristicPlayer(Integer pid) {
		id = pid;
	}

	public String getName() {

		return "Random";

	}

	public int getNumOfTiles() {
		return numOfTiles;
	}

	public void setNumOfTiles(int tiles) {
		numOfTiles = tiles;
	}

	public int getId() {
		return id;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setId(int id) {
		// TODO Auto-generated method stub
		this.id = id;

	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;

	}

	public int[] getNextMove(Board board, int randomNumber) {
		// TODO fill this function

	}

	double getEvaluation(Board board, int randomNumber, Tile tile) {
		board.
		return 0.0;
	}

}
