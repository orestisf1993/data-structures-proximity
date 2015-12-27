package gr.auth.ee.dsproject.proximity.defplayers;

import gr.auth.ee.dsproject.proximity.board.Board;

public class MinMaxPlayer implements AbstractPlayer {

    int score;
    int id;
    String name;
    int numOfTiles;

    public MinMaxPlayer(final Integer pid) {
        id = pid;
        name = "MinMaxPlayerTeam2";
    }

    public void createMySubTree(final Node parent, final int depth) {

    }

    public void createOpponentSubtree(final Node parent, final int depth) {

    }

    public int getId() {
        return id;
    }

    public String getName() {

        return name;

    }

    public int[] getNextMove(final Board board, final int randomNumber) {
        return null;

    }

    public int getNumOfTiles() {
        return numOfTiles;
    }

    public int getScore() {
        return score;
    }

    public void setId(final int id) {

        this.id = id;

    }

    public void setName(final String name) {
        this.name = name;

    }

    public void setNumOfTiles(final int tiles) {
        numOfTiles = tiles;
    }

    public void setScore(final int score) {
        this.score = score;
    }

}
