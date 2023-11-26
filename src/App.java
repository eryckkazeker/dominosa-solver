import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
    public static void main(String[] args) throws Exception {

      Options opt = new Options();

      opt.addOption("s", "size", true, "Board Size");
      opt.addOption("i", "id", true, "Game Id");

      CommandLineParser parser = new DefaultParser();

      try {
          CommandLine cmd = parser.parse(opt, args);
          int size = Integer.parseInt(cmd.getOptionValue("size"));
          String id = cmd.getOptionValue("id");

          System.out.println("Creating Chrome Driver");
          var client = new WebClient();

          System.out.println("Fetching game data");
          var boardData = client.getBoard(BoardSizeMapper.map(size), id);

          System.out.println("Building Game Board");
          var game = Game.build(boardData, size);
            
          System.out.println("Solving Game");
          Solver solver = new Solver();
          solver.solve(game);

      } catch (ParseException e) {
          System.err.println("Error: " + e.getMessage());
          System.exit(1);
      }
    }

      
}
