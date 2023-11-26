import java.util.Map;
import java.util.Set;

public class GameState {
  Map<Domino, Set<Pair>> board;
  Set<Domino> solvedDominoes;

  public GameState(Map<Domino, Set<Pair>> board, Set<Domino> solvedDominoes) {
    this.board = board;
    this.solvedDominoes = solvedDominoes;
  }

  public Map<Domino, Set<Pair>> getBoard() {
    return board;
  }

  public Set<Domino> getSolvedDominoes() {
    return solvedDominoes;
  }
  
}
