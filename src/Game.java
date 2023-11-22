import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
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

  public static Game build() throws IOException {
    Scanner s = new Scanner(new FileReader(new File("board")));
    var size = Integer.valueOf(s.nextLine());
    Game board = new Game(size);

    var matrix = new int[size+1][size+2];
    var lineCount = 0;

    while (s.hasNext()) {
      var lineString = s.nextLine().split(",");
      var colCount = 0;
      for (String v : lineString) {
        matrix[lineCount][colCount] = Integer.valueOf(v);
        colCount++;
      }
      lineCount++;
    }

    s.close();

    // depth first navigation to build board
    buildPairs(matrix, board);

    return board;
  }

  private static void buildPairs(int[][] matrix, Game b) {
    buildPair(0, 0, matrix, b);
  }

  private static void buildPair(int x, int y, int[][] matrix, Game board) {
    if (x >= matrix[0].length) return;
    if (y >= matrix.length) return;

    if (x < matrix[0].length-1) {
      Domino d = new Domino(matrix[y][x], matrix[y][x+1]);
      var set = board.get(d);
      set.add(Pair.ofCoordinates(x, y, x+1, y));
      board.put(d, set);
      buildPair(x+1, y, matrix, board);
    }

    if (y < matrix.length-1) {
      Domino d = new Domino(matrix[y][x], matrix[y+1][x]);
      var set = board.get(d);
      set.add(Pair.ofCoordinates(x, y, x, y+1));
      board.put(d, set);
      buildPair(x, y+1, matrix, board);
    }
  }

  public Map<Domino, Set<Pair>> getBoard() {
    return board;
  }
  
}
