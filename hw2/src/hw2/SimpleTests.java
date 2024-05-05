package hw2;

import hw2.FuzzballGame;

/**
 * Some very simple test cases as described in the assignment pdf...
 */
public class SimpleTests
{
  public static void main(String[] args)
  {
    FuzzballGame game = new FuzzballGame(3);
    System.out.println(game);
    
    game.strike(false);
    System.out.println(game);  // one strike
    game.strike(false);
    System.out.println(game);  // 0 strikes, one out
    game.strike(false);
    System.out.println(game);  // one strike, one out
    game.strike(false);
    System.out.println(game);  // 0 strike, two outs
    
    game.strike(true); // batter is immediately out for swung strike
    System.out.println(game.isTopOfInning()); // should be false now
    System.out.println(game);         // bottom of 1st inning, 0 outs

    // try some hits, look at the bases
    game = new FuzzballGame(3);
    game.hit(15);
    System.out.println(game.runnerOnBase(1)); // true
    System.out.println(game.getBases());      // Xoo    
    game.hit(15);
    System.out.println(game.getBases());      // XXo   
    game.hit(15);
    System.out.println(game.getBases());      // XXX
    game.hit(15);
    System.out.println(game.getBases());      // XXX
    System.out.println(game.getTeam0Score()); // 1
    
    // try hitting a double now
    game.hit(150);
    System.out.println(game.getBases());      // oXX
    System.out.println(game.getTeam0Score()); // 3

    // try counting balls
    game = new FuzzballGame(3);
    game.ball();
    System.out.println(game.getBallCount()); // 1
    game.ball();
    System.out.println(game.getBallCount()); // 2
    game.ball();
    System.out.println(game.getBallCount()); // 3
    game.strike(true);
    System.out.println(game.getBallCount()); // 0, since it's a new batter
    
    // effect of a walk on the runners on base
    game = new FuzzballGame(3);
    game.hit(225);  // a triple
    System.out.println(game.getBases());  // ooX   
    game.ball();
    game.ball();
    game.ball();
    game.ball();
    System.out.println(game.getBallCount()); // 4
    game.ball();  //  a walk 
    System.out.println(game.getBases());  // XoX 
  }
}
