import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Solver {

  public void solve(Game game) {

    Map<Domino, Set<Pair>> initialBoard = new HashMap<Domino,Set<Pair>>(game.getBoard());
    Set<Domino> solvedDominoes = new LinkedHashSet<>();

    // System.out.println(initialBoard);
    // System.out.println(game.getBoard());
    
    trySolve(initialBoard, solvedDominoes, "0");

  }

  private boolean trySolve(Map<Domino, Set<Pair>> board, Set<Domino> solvedDominoes, String id) {
    boolean end = false;

    while (!end) {

      System.out.println("Operation: " +id);
      // System.out.println("Solved Dominoes: "+ solvedDominoes);
      // System.out.println("==================================================");
      // printBoard(board);

      // if success print board return true
      if (isGameDone(board)) {
        System.out.println("SOLUTION FOUND!!!!!");
        printBoard(board);
        System.exit(0);
      }

      // if error return false
      if (noSolutionPossible(board)) {
        return false;
      }

      // find solved pairs in current board and continue
      if (solveForUniqueDominoes(board, solvedDominoes) != null) continue;

      // find unique points in current board and continue
      if (solveForUniquePoint(board, solvedDominoes) != null) continue;

      // solved = trySolve with new hipotesys
      Domino unsolvedDomino = findUnsolvedDomino(board);

      if (unsolvedDomino == null) {
        return false;
      }

      int idCounter = 0;
      for(Pair p : board.get(unsolvedDomino)) {
        String opId = new String(id + idCounter);
        Map<Domino, Set<Pair>> hypotesisBoard = deepCopy(board);
        Set<Pair> solvedSet = new HashSet<>();
        solvedSet.add(p);
        hypotesisBoard.put(unsolvedDomino, solvedSet);
        end = trySolve(hypotesisBoard, solvedDominoes, opId);
        idCounter++;
      }

      return false;

    }
    
    return true;
  }

  private Map<Domino, Set<Pair>> deepCopy(Map<Domino, Set<Pair>> other) {
    Map<Domino, Set<Pair>> clone = new HashMap<>();

    other.entrySet().forEach(t -> {
      Set<Pair> set = new HashSet<>();
      t.getValue().forEach(v -> set.add(new Pair(new Point(v.getP1().getX(), v.getP1().getY()), new Point(v.getP2().getX(), v.getP2().getY()))));
      clone.put(t.getKey(), set);
    });

    return clone;
  }

  private void printBoard(Map<Domino, Set<Pair>> board) {
    System.out.println("================================================");
    board.entrySet().forEach(t -> {
      System.out.println(String.format("%s = %s", t.getKey(), t.getValue()));
    });
    System.out.println("================================================");
  }

  private Domino findUnsolvedDomino(Map<Domino, Set<Pair>> board) {
    var dominoOptional = board.entrySet().stream().filter(t -> t.getValue().size() > 1).findFirst();
    
    if(!dominoOptional.isPresent()) {
      return null;
    }
    return dominoOptional.get().getKey();
  }

  private Pair solveForUniquePoint(Map<Domino, Set<Pair>> board, Set<Domino> solvedDominoes) {
    Pair uniquePointPair = findUniquePoint(board);
    uniquePointPair = solveDominoWithPair(uniquePointPair, board, solvedDominoes);
    removeCollisions(uniquePointPair, board);
    return uniquePointPair;
  }

  private Pair solveDominoWithPair(Pair pair, Map<Domino, Set<Pair>> board, Set<Domino> solvedDominoes) {

    var entryOptional = board.entrySet().stream().filter(t -> t.getValue().contains(pair)).findFirst();
    if(!entryOptional.isPresent()) {
      return null;
    }

    if(solvedDominoes.contains(entryOptional.get().getKey())) {
      return null;
    }

    Set<Pair> set = new HashSet<>();
    set.add(pair);
    Domino key = entryOptional.get().getKey();
    board.put(key, set);
    if (!solvedDominoes.add(key)) {
      return null;
    }

    return pair;

  }

  private Pair findUniquePoint(Map<Domino, Set<Pair>> board) {
    List<Pair> pairList = new ArrayList<>();
    var pairSets = board.values();
    pairSets.forEach(set -> {
      if (set != null) {
        pairList.addAll(set);
      }
    });

    var pointMap = new HashMap<Point, Set<Pair>>();

    for (Pair p : pairList) {
      Set<Pair> p1Set = new HashSet<Pair>();
      Set<Pair> p2Set = new HashSet<Pair>();
      if (pointMap.containsKey(p.getP1())) {
        p1Set = pointMap.get(p.getP1());
      }
      p1Set.add(p);
      pointMap.put(p.getP1(), p1Set);

      if (pointMap.containsKey(p.getP2())) {
        p2Set = pointMap.get(p.getP2());
      }
      p2Set.add(p);
      pointMap.put(p.getP2(), p2Set);
    }

    var uniquePointSetOptional = pointMap.values().stream().filter(set -> set.size() == 1).findFirst();
    if (!uniquePointSetOptional.isPresent()) {
      // System.err.println("I couldn't find any unique point pairs");
      return null;
    }

    var uniquePointSet = uniquePointSetOptional.get();
    var resultPair = (Pair)uniquePointSet.toArray()[0];
    // System.out.println("Found solved Domino: "+ resultPair.toString());
    return resultPair;
  }

  private boolean noSolutionPossible(Map<Domino, Set<Pair>> board) {
    return board.values().stream().filter(set -> set.size() == 0).collect(Collectors.toList()).size() > 0;
  }

  private boolean isGameDone(Map<Domino, Set<Pair>> board) {
    var dominoes = board.keySet();
    // System.out.println("Dominoes "+dominoes);

    var pairs = board.values().stream().flatMap(set -> set.stream()).collect(Collectors.toSet());
    // System.out.println("Pairs "+pairs);

    var points = pairs.stream().flatMap(p -> Set.of(p.getP1(), p.getP2()).stream()).collect(Collectors.toSet());


    if (dominoes.size() == pairs.size()) {
      return pairs.size()*2 == points.size();
    }
    return false;
  }

  private Pair solveForUniqueDominoes(Map<Domino, Set<Pair>> board, Set<Domino> solvedDominoes) {
    var boardEntries = board.entrySet();
    var solvedDominoOptional = boardEntries.stream().filter(t -> t.getValue() != null && t.getValue().size() == 1 && !solvedDominoes.contains(t.getKey())).findFirst();
    if (!solvedDominoOptional.isPresent()) {
      // System.err.println("I couldn't find any resolved dominoes");
      return null;
    }
    var solvedPairSet = solvedDominoOptional.get().getValue();
    solvedDominoes.add(solvedDominoOptional.get().getKey());
    var solvedPair = (Pair)solvedPairSet.toArray()[0];
    // System.out.println("Found solved Domino: "+ solvedPair.toString());
    removeCollisions(solvedPair, board);

    return solvedPair;
  }

  private void removeCollisions(Pair solvedPair, Map<Domino, Set<Pair>> board) {
    
    if (solvedPair == null) return;


    board.values().forEach((set) -> {
      if(set != null) {
        set.removeAll(set.stream().filter(pair -> pair.collidesWith(solvedPair)).toList());
      }
    });

    // System.out.println(board);

  }
  
}