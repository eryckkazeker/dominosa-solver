public class App {
    public static void main(String[] args) throws Exception {

        var game = Game.build();
        
        Solver solver = new Solver();
        solver.solve(game);
    }
}
