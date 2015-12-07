package gr.auth.ee.dsproject.proximity.defplayers;

import gr.auth.ee.dsproject.proximity.board.Board;

public interface AbstractPlayer {
	public int getId();

	public String getName();

	public int[] getNextMove(Board board);

	public int getNumOfTiles();

	public int getScore();

	public void setId(int id);

	public void setName(String name);

	public void setNumOfTiles(int tiles);

	public void setScore(int score);

}
