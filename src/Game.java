import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {

  private Map<Domino,Set<Pair>> board;

  public Game(int size) {
    var board = new HashMap<Domino, Set<Pair>>();
    for (int x = 0; x <= size; x++) {
      for (int y = x; y <= size; y++) {
        var domino = new Domino(x, y);
        board.put(domino, new HashSet<>());
      }
    }
    this.board = board;
  }

  public Set<Pair> get(Domino d) {
    return this.board.get(d);
  }

  public void put(Domino d, Set<Pair> p) {
    this.board.put(d, p);
  }

  public Set<Domino> getDominoes() {
    return board.keySet();
  }

  public Set<Pair> getPairs() {
    return board.values().stream().flatMap(set -> set.stream()).collect(Collectors.toSet());
  }

  public static Game build(String boardData, int size) throws IOException {
    Game game = new Game(size);

    var matrix = new int[size+1][size+2];
    var lineCount = 0;
    var columnCount = 0;

    var boardArray = boardData.split(",");

    for (String s : boardArray) {

      matrix[lineCount][columnCount] = Integer.parseInt(s);

      columnCount++;

      if (columnCount == matrix[0].length) {
        columnCount = 0;
        lineCount++;
      }

    }

    // depth first navigation to build board
    buildPairs(matrix, game);

    return game;
  }

  private static void buildPairs(int[][] matrix, Game b) {
    Set<Pair> builtPairs = new HashSet<>();
    buildPair(0, 0, matrix, b, builtPairs);
  }

  private static void buildPair(int x, int y, int[][] matrix, Game board, Set<Pair> builtPairs) {

    Pair p1 = Pair.ofCoordinates(x, y, x+1, y);
    Pair p2 = Pair.ofCoordinates(x, y, x, y+1);

    if (builtPairs.contains(p1)) return;
    if (builtPairs.contains(p2)) return;

    if (x >= matrix[0].length) return;
    if (y >= matrix.length) return;

    if (x < matrix[0].length-1) {
      Domino d = new Domino(matrix[y][x], matrix[y][x+1]);
      var set = board.get(d);
      Pair p = Pair.ofCoordinates(x, y, x+1, y);
      builtPairs.add(p);
      set.add(p);
      board.put(d, set);
      buildPair(x+1, y, matrix, board, builtPairs);
    }

    if (y < matrix.length-1) {
      Domino d = new Domino(matrix[y][x], matrix[y+1][x]);
      var set = board.get(d);
      Pair p = Pair.ofCoordinates(x, y, x, y+1);
      set.add(p);
      builtPairs.add(p);
      board.put(d, set);
      buildPair(x, y+1, matrix, board, builtPairs);
    }
  }

  public Map<Domino, Set<Pair>> getBoard() {
    return board;
  }
  
}
