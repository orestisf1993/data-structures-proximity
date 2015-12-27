package gr.auth.ee.dsproject.proximity.defplayers;

import java.util.HashMap;
import java.util.Map;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

public class HeuristicPlayer implements AbstractPlayer {

    @SuppressWarnings("unused")
    private static void printHashMap(final HashMap<Integer, Integer> map) {
        for (final Map.Entry<Integer, Integer> entry : map.entrySet()) {
            final Integer key = entry.getKey();
            final Integer value = entry.getValue();
            System.out.println("key, " + key + " value " + value);
        }
    }

    int score;
    int id;
    int opponentId;
    String name;
    int numOfTiles;
    private final HashMap<Integer, Integer> opponentsPool = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> myPool;

    public HeuristicPlayer(final Integer pid) {
        id = pid;
        opponentId = (pid == 1) ? 2 : 1;
        name = "HeuristicPlayerTeam2";
        for (Integer key = 1; key <= 20; key++) {
            opponentsPool.put(key, 3);
        }
    }

    private double calculateRisk(final Tile tile, final Board board, final int nextTileScore) {
        HashMap<Integer, Integer> map;
        final int tileId = tile.getPlayerId();
        final double scoreTile = tile.getScore();
        final boolean isEnemyBigger = (tileId == opponentId && nextTileScore <= scoreTile);
        if (isEnemyBigger) {
            map = myPool;
        } else {
            map = opponentsPool;
        }

        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        double emptyNeighbors = (tileId == 0) ? 1 : 0;
        for (final Tile neighbor : neighbors) {
            if (neighbor != null && neighbor.getPlayerId() == 0) {
                emptyNeighbors++;
            }
        }
        assert (emptyNeighbors != 0);

        double totalValuesCount = 0;
        double biggerValuesCount = 0;
        for (final Map.Entry<Integer, Integer> entry : map.entrySet()) {
            final Integer tileValue = entry.getKey();
            final Integer tileCount = entry.getValue();
            totalValuesCount += tileCount;
            if (tileValue > scoreTile) {
                biggerValuesCount += tileCount;
            }
        }
        if (totalValuesCount == 0) {
            return 0;
        }

        final double percentLarger = biggerValuesCount / totalValuesCount;
        final double risk = 2 * percentLarger * (scoreTile) / (emptyNeighbors * emptyNeighbors);
        if (isEnemyBigger) {
            return -risk;
        } else {
            return risk;
        }
    }

    public double getEvaluation(final Board board, final int randomNumber, final Tile tile) {
        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        int scoreFromAlies = 0;
        int scoreFromEnemies = 0;
        double scoreFromRisk = calculateRisk(tile, board, randomNumber);

        for (final Tile neighbor : neighbors) {
            if (neighbor == null || neighbor.getPlayerId() == 0) {
                continue;
            }
            final int neighborPlayerId = neighbor.getPlayerId();
            final int neighborScore = neighbor.getScore();
            if (neighborPlayerId == opponentId && randomNumber > neighborScore) {
                scoreFromEnemies += neighborScore;
            } else if (neighborPlayerId == id && neighborScore != 20) {
                scoreFromAlies++;
            }
            scoreFromRisk += calculateRisk(neighbor, board, randomNumber);
        }
        final double evaluation = scoreFromAlies + scoreFromEnemies + scoreFromRisk;
        return evaluation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getNextMove(final Board board, final int randomNumber) {
        double max = Double.NEGATIVE_INFINITY;
        final int[] result = new int[3];
        updateOpponentsPool(board);
        myPool = board.getMyPool();
        for (int i = 0; i < ProximityUtilities.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < ProximityUtilities.NUMBER_OF_COLUMNS; j++) {
                final Tile tile = board.getTile(j, i);
                if (tile.getPlayerId() == 0) {
                    final double evaluation = getEvaluation(board, randomNumber, tile);
                    if (evaluation >= max) {
                        max = evaluation;
                        result[0] = tile.getX();
                        result[1] = tile.getY();
                    }
                }
            }
        }

        // System.out.println(result[0] + " " + result[1] + "==" + max);
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

    private void updateOpponentsPool(final Board board) {
        final int[] lastMove = board.getOpponentsLastMove();
        if (lastMove[0] == -1) {
            return;
        }
        // printHashMap(opponentsPool);
        final Tile lastMoveTile = board.getTile(lastMove[0], lastMove[1]);
        assert (opponentId == lastMoveTile.getPlayerId());
        final Integer key = lastMoveTile.getScore();
        // decrease by 1.
        final Integer value = opponentsPool.get(key) - 1;
        opponentsPool.put(key, value);
    }
}
