package hw2;

/**
 * models a game like baseball called fuzzball.
 *
 * @author Marshall Alexander
 */
public class FuzzballGame{
  /**
   * number of strikes to get player out
   */
  public static final int MAX_STRIKES = 2;
  /**
   * number of balls to walk player
   */
  public static final int MAX_BALLS = 5;
  /**
   * number of outs before the teams switch
   */
  public static final int MAX_OUTS = 3;
  /**
   * checks if game over
   */
  private boolean gameOver = false;
  /**
   * current outs
   */
  private int outs;
  /**
   * current strikes
   */
  private int strikes;
  /**
   * current balls
   */
  private int balls;
  /**
   * score of team 0
   */
  private int team0Score;
  /**
   * score of team 1
   */
  private int team1Score;
  /**
   * checks if player on first
   */
  private boolean onFirst;
  /**
   * checks if player on second
   */
  private boolean onSecond;
  /**
   * checks if player on third
   */
  private boolean onThird;
  /**
   * checks if team0 is at bat
   */
  private boolean team0Bat = true;
  /**
   * checks if bottom of the inning
   */
  private boolean bottom = false;
  /**
   * checks if player at bat has hit the ball
   */
  private boolean hasHit;
  /**
   * current inning
   */
  private int innings;
  /**
   * number of innings in the game given by user
   */
  private final int givenInnings;

  /**
   * Returns a three-character string representing the players on base, in the
   * order first, second, and third, where 'X' indicates a player is present and
   * 'o' indicates no player.
   *
   * @return three-character string showing players on base
   */
  public String getBases(){
    return (runnerOnBase(1) ? "X" : "o") + (runnerOnBase(2) ? "X" : "o")
            + (runnerOnBase(3) ? "X" : "o");
  }

  /**
   * Returns a one-line string representation of the current game state. The
   * format is:
   * <pre>
   * ooo Inning:1 [T] Score:0-0 Balls:0 Strikes:0 Outs:0
   * </pre>
   * The first three characters represent the players on base as returned by the
   * <code>getBases()</code> method. The 'T' after the inning number indicates
   * it's the top of the inning, and a 'B' would indicate the bottom. The score always
   * shows team 0 first.
   *
   * @return a single line string representation of the state of the game
   */
  public String toString(){
    String bases = getBases();
    String topOrBottom = (isTopOfInning() ? "T" : "B");
    String fmt = "%s Inning:%d [%s] Score:%d-%d Balls:%d Strikes:%d Outs:%d";
    return String.format(fmt, bases, whichInning(), topOrBottom, getTeam0Score(),
            getTeam1Score(), getBallCount(), getCalledStrikes(), getCurrentOuts());
  }

  /**
   * creates a fuzzball game
   *
   * @param givenNumInnings
   */
  public FuzzballGame(int givenNumInnings){
    givenInnings = givenNumInnings;
    outs = 0;
    innings = 1;
    strikes = 0;
    balls = 0;
    team0Score = 0;
    team1Score = 0;
  }

  /**
   * returns true if a runner is on the base
   * false otherwise
   *
   * @param which
   * @return
   */
  public boolean runnerOnBase(int which){
    if (which == 1){
      return onFirst;
    }
    if (which == 2){
      return onSecond;
    }
    if (which == 3){
      return onThird;
    }
    return false;
  }

  /**
   * returns true if game is over
   * false otherwise
   *
   * @return
   */
  public boolean gameEnded(){
    if (innings > givenInnings){
      gameOver = true;
      return true;
    }
    else{
      return false;
    }
  }

  /**
   * returns true if its the first half of the inning
   *
   * @return
   */
  public boolean isTopOfInning(){
    return !bottom;
  }

  /**
   * returns current inning
   *
   * @return
   */
  public int whichInning(){
    if (!gameOver){
      return innings;
    }
    else{
      return innings + 1;
    }
  }

  /**
   * returns the count of balls for the current batter
   *
   * @return
   */
  public int getBallCount(){
    return balls;
  }

  /**
   * returns the number of outs for the team currently at bat
   *
   * @return
   */
  public int getCalledStrikes(){
    return strikes;
  }

  /**
   * returns the number of outs for the team at bat
   *
   * @return
   */
  public int getCurrentOuts(){
    return outs;
  }

  /**
   * returns score for team 0
   *
   * @return
   */
  public int getTeam0Score(){
    return team0Score;
  }

  /**
   * returns score for team 1
   *
   * @return
   */
  public int getTeam1Score(){
    return team1Score;
  }

  /**
   * specifies a non-strike that the batter didnt swing on
   */
  public void ball(){
    if (!gameOver){
      balls++;
      if (balls > 4){
        strikes = 0;
        balls = 0;
        walk();
      }
    }
  }

  /**
   * specifies a strike for the current batter
   *
   * @param swung
   */
  public void strike(boolean swung){
    if (!gameOver){
      if (swung){
        outs++;
        strikes = 0;
        balls = 0;
      }
      else{
        strikes++;
        if (strikes >= 2){
          outs++;
          strikes = 0;
          balls = 0;
        }
      }
      inningChange();
    }
  }

  /**
   * specifies that the batter hit the ball
   *
   * @param distance
   */
  public void hit(int distance){
    if (!gameOver){
      if (distance < 15){
        outs++;
        inningChange();
      }
      else if (distance >= 15 && distance < 150){
        single();
        strikes = 0;
        balls = 0;
      }
      else if (distance >= 150 && distance < 200){
        Double();
        strikes = 0;
        balls = 0;
      }
      else if (distance >= 200 && distance < 250){
        triple();
        strikes = 0;
        balls = 0;
      }
      else{
        homeRun();
        strikes = 0;
        balls = 0;
      }
    }
  }

  /**
   * specifies that the batter is out from a caught fly
   */
  public void caughtFly(){
    if (!gameOver){
      outs++;
      inningChange();
      strikes = 0;
      balls = 0;
    }
  }

  /**
   * changes the inning from bottom to top or top to bottom
   */
  private void switchBottomOrTop(){
    if (bottom){
      bottom = false;
      team0Bat = !team0Bat;
    }
    else{
      bottom = true;
      team0Bat = !team0Bat;
    }
    outs = 0;
    strikes = 0;
    balls = 0;
    onFirst = false;
    onSecond = false;
    onThird = false;
    hasHit = false;
  }

  /**
   * changes to a new inning and sets the field to empty and count to 0
   */
  private void newInning(){
    outs = 0;
    strikes = 0;
    balls = 0;
    innings++;
    onFirst = false;
    onSecond = false;
    onThird = false;
    hasHit = false;
    switchBottomOrTop();
  }

  /**
   * finds which teams score to increment
   */
  private void whichTeamScores(){
    if (team0Bat){
      team0Score++;
    }
    else{
      team1Score++;
    }
  }

  /**
   * called to move to a new inning or switch to the bottom of the inning
   */
  private void inningChange(){
    if (outs >= 3){
      if (bottom){
        newInning();
      }
      else{
        switchBottomOrTop();
      }
    }
  }

  /**
   * simulates single
   */
  private void single(){
    hasHit = false;
    if(onThird && onSecond && onFirst && !hasHit){
      whichTeamScores();
      hasHit = true;
    }
    if(onThird && onSecond && !onFirst && !hasHit){
      whichTeamScores();
      onSecond = false;
      onFirst = true;
      hasHit = true;
    }
    if(onThird && !onSecond && onFirst && !hasHit){
      whichTeamScores();
      onThird = false;
      onSecond = true;
      hasHit = true;
    }
    if(!onThird && onSecond && onFirst && !hasHit){
      onThird = true;
      onSecond = true;
      onFirst = true;
      hasHit = true;
    }
    if(onThird && !onSecond && !onFirst && !hasHit){
      whichTeamScores();
      onThird = false;
      onFirst = true;
      hasHit = true;
    }
    if(!onThird && onSecond && !onFirst && !hasHit){
      onThird = true;
      onSecond = false;
      onFirst = true;
      hasHit = true;
    }
    if(!onThird && !onSecond && onFirst && !hasHit){
      onSecond = true;
      hasHit = true;
    }
    if(!onFirst && !onSecond && !onThird && !hasHit){
      onFirst = true;
      hasHit = true;
    }
  }
  /**
   * simulates double
   */
  private void Double(){
    hasHit = false;
    if(onThird && onSecond && onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      onFirst = false;
      hasHit = true;
    }
    if(onThird && onSecond && !onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      onSecond = true;
      onThird = false;
      hasHit = true;
    }
    if(onThird && !onSecond && onFirst && !hasHit){
      whichTeamScores();
      onSecond = true;
      onThird = true;
      hasHit = true;
    }
    if(!onThird && onSecond && onFirst && !hasHit){
      whichTeamScores();
      onSecond = true;
      onThird = true;
      hasHit = true;
    }
    if(onThird && !onSecond && !onFirst && !hasHit){
      whichTeamScores();
      onSecond = true;
      onThird = false;
      hasHit = true;
    }
    if(!onThird && onSecond && !onFirst && !hasHit){
      whichTeamScores();
      hasHit = true;
    }
    if(!onThird && !onSecond && onFirst && !hasHit){
      onThird = true;
      onSecond = true;
      onFirst = false;
      hasHit = true;
    }
    if(!onFirst && !onSecond && !onThird && !hasHit){
      onSecond = true;
      hasHit = true;
    }
  }
  /**
   * simulates triple
   */
  private void triple(){
    hasHit = false;
    if(onThird && onSecond && onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      whichTeamScores();
      onSecond = false;
      onFirst = false;
      hasHit = true;
    }
    if(onThird && onSecond && !onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      onSecond = false;
      hasHit = true;
    }
    if(onThird && !onSecond && onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      onFirst = false;
      hasHit = true;
    }
    if(!onThird && onSecond && onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      onThird = true;
      onSecond = false;
      onFirst = false;
      hasHit = true;
    }
    if(onThird && !onSecond && !onFirst && !hasHit){
      whichTeamScores();
      hasHit = true;
    }
    if(!onThird && onSecond && !onFirst && !hasHit){
      whichTeamScores();
      onThird = true;
      onSecond = false;
      hasHit = true;
    }
    if(!onThird && !onSecond && onFirst && !hasHit){
      whichTeamScores();
      onThird = true;
      onFirst = false;
      hasHit = true;
    }
    if(!onThird && !onSecond && !onFirst && !hasHit){
      onThird = true;
      hasHit = true;
    }
  }
  /**
   * simulates home run
   */
  private void homeRun(){
    hasHit = false;
    if(onThird && onSecond && onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      whichTeamScores();
      whichTeamScores();
      onThird = false;
      onSecond = false;
      onFirst = false;
      hasHit = true;
    }
    if(onThird && onSecond && !onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      whichTeamScores();
      onThird = false;
      onSecond = false;
      hasHit = true;
    }
    if(onThird && !onSecond && onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      whichTeamScores();
      onThird = false;
      onFirst = false;
      hasHit = true;
    }
    if(!onThird && onSecond && onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      whichTeamScores();
      onSecond = false;
      onFirst = false;
      hasHit = true;
    }
    if(onThird && !onSecond && !onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      onThird = false;
      hasHit = true;
    }
    if(!onThird && onSecond && !onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      onSecond = false;
      hasHit = true;
    }
    if(!onThird && !onSecond && onFirst && !hasHit){
      whichTeamScores();
      whichTeamScores();
      onFirst = false;
      hasHit = true;
    }
    if(!onThird && !onSecond && !onFirst && !hasHit){
      whichTeamScores();
      hasHit = true;
    }
  }
  /**
   * simulates walk
   */
  private void walk(){
    if(onThird && onSecond && onFirst){
      whichTeamScores();
    }
    if(onThird && onSecond && !onFirst){
      onFirst = true;
    }
    if(onThird && !onSecond && onFirst){
      onSecond = true;
    }
    if(!onThird && onSecond && onFirst){
      onThird = true;
    }
    if(onThird && !onSecond && !onFirst){
      onFirst = true;
    }
    if(!onThird && onSecond && !onFirst){
      onFirst = true;
    }
    if(!onThird && !onSecond && onFirst){
      onSecond = true;
    }
    if(!onThird && !onSecond && !onFirst){
      onFirst = true;
    }
  }
}
