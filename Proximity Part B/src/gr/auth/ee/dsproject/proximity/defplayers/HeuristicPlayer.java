package gr.auth.ee.dsproject.proximity.defplayers;

import java.util.HashMap;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

public class HeuristicPlayer implements AbstractPlayer {

    int score;
    int id;
    int opponentId;
    String name;
    int numOfTiles;
    private HashMap<Integer, Integer> opponentsPool = new HashMap<Integer, Integer>();

    public HeuristicPlayer(final Integer pid) {
        id = pid;
        opponentId = (pid == 1) ? 2 : 1;
        for (Integer key = 1; key <= 20; key++) {
            opponentsPool.put(key, 3);
        }
    }

    public double getEvaluation(final Board board, final int randomNumber, final Tile tile) {
        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        int scoreFromAlies = 0;
        int scoreFromEnemies = 0;

        for (final Tile neighbor : neighbors) {
            if (neighbor == null) {
                continue;
            }
            final int neighborPlayerId = neighbor.getPlayerId();
            final int neighborScore = neighbor.getScore();
            scoreFromEnemies += (neighborPlayerId == opponentId && randomNumber > neighborScore)
                    ? neighborScore : 0;
            scoreFromAlies += (neighborPlayerId == id && neighborScore != 20) ? 1 : 0;
        }
        double evaluation = scoreFromAlies + scoreFromEnemies;
        return evaluation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "Heuristic";
    }

    public int[] getNextMove(final Board board, final int randomNumber) {
        double max = -1;
        final int[] result = new int[3];
        updateOpponentsPool(board);
        for (int i = 0; i < ProximityUtilities.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < ProximityUtilities.NUMBER_OF_COLUMNS; j++) {
                final Tile tile = board.getTile(j, i);
                if (tile.getPlayerId() == 0) {
                    final double evaluation = getEvaluation(board, randomNumber, tile);
                    System.out.println("" + evaluation + " at " + tile.getX() + " " + tile.getY());
                    if (evaluation >= max) {
                        max = evaluation;
                        result[0] = tile.getX();
                        result[1] = tile.getY();

                    }
                }
            }
        }
        System.out.println(result[0] + " " + result[1] + "==" + max);
        result[2] = randomNumber;
        return result;
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

    private void updateOpponentsPool(Board board) {
        int[] lastMove = board.getOpponentsLastMove();
        // decrease by 1.
        Tile lastMoveTile = board.getTile(lastMove[0], lastMove[1]);
        assert (opponentId == lastMoveTile.getPlayerId());
        Integer key = lastMoveTile.getScore();
        Integer value = opponentsPool.get(key) - 1;
        opponentsPool.put(key, value);
    }

}
