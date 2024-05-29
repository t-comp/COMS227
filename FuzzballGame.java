
package hw2;

/**
 * Models a simplified baseball-like game called Fuzzball.
 * 
 * @author Taylor Bauer
 */
public class FuzzballGame {

	/** Number of strikes causing a player to be out. */
	public static final int MAX_STRIKES = 2;

	/** Number of balls causing a player to walk. */
	public static final int MAX_BALLS = 5;

	/** Number of outs before the teams switch. */
	public static final int MAX_OUTS = 3;

	/** Holds the value of the Top or Bottom of the inning. */
	private boolean isInningTop;

	/** Holds the value of which inning it is. */
	private int theInning;

	/** Holds the count of the balls. */
	private int ballCount;

	/** Holds the count of the strikes. */
	private int strikeCount;

	/** Holds the count of the outs. */
	private int outCount;

	/** Holds the number of FuzzBall innings to play. */
	private int numFuzzBallInnings;

	/** Holds boolean value of a caught fly. */
	private boolean flyCaught;

	/** Holds boolean value for runner on first base. */
	private boolean runnerOnFirst;

	/** Holds boolean value for runner on second base. */
	private boolean runnerOnSecond;

	/** Holds boolean value for runner on third base. */
	private boolean runnerOnThird;

	/** Holds the score for Team 0. */
	private int team0Score;

	/** Holds the score for Team . */
	private int team1Score;

	/**
	 * Constructs a game that has the given number of innings and starts at the top
	 * of inning 1.
	 * 
	 * @param givenNumInning - number of innings for the game.
	 */
	public FuzzballGame(int givenNumInning) {
		numFuzzBallInnings = givenNumInning;
		isInningTop = true;
		theInning = 1;
		ballCount = 0;
		strikeCount = 0;
		outCount = 0;
		flyCaught = true;
		runnerOnFirst = false;
		runnerOnSecond = false;
		runnerOnThird = false;

	}

	/**
	 * Returns true if the game is over, false otherwise.
	 * 
	 * @return - true if game over, false otherwise.
	 */
	public boolean gameEnded() {
		return theInning > numFuzzBallInnings;
	}

	/**
	 * Returns true if there is a runner on the indicated base, false otherwise.
	 * Returns false if the given argument is anything other than 1, 2, or 3.
	 * 
	 * @param which - the base number to check
	 * @return - true if the is a runner on the indicated base
	 */
	public boolean runnerOnBase(int which) {
		if (which == 1) {
			return runnerOnFirst;
		}
		if (which == 2) {
			return runnerOnSecond;
		}
		if (which == 3) {
			return runnerOnThird;
		} else {
			return false;
		}
	}

	/**
	 * Returns the current inning. Innings are numbered starting at 1. When it is
	 * game over, this method will return the game's total number of innings plus
	 * one.
	 * 
	 * @return - current inning, or the number of inning plus one if game is over.
	 */
	public int whichInning() {
		return theInning;
	}

	/**
	 * Returns true if it is the first half of the inning (team 0 at bat).
	 * 
	 * @return - true if it is the first half of the inning, false otherwise
	 */
	public boolean isTopOfInning() {
		return isInningTop;
	}

	/**
	 * Returns the number of outs for the team currently at bat.
	 * 
	 * @return - current number of outs.
	 */
	public int getCurrentOuts() {
		return outCount;
	}

	/**
	 * Returns the number of called strikes for the current batter.
	 * 
	 * @return - current number of strikes.
	 */
	public int getCalledStrikes() {
		return strikeCount;
	}

	/**
	 * Returns the count of calls for the current batter.
	 * 
	 * @return - current number of balls.
	 */
	public int getBallCount() {
		return ballCount;
	}

	/**
	 * Returns the score for team 0!
	 * 
	 * @return - the score for team 0.
	 */
	public int getTeam0Score() {
		return team0Score;
	}

	/**
	 * Returns the score for team 1!
	 * 
	 * @return - the score for team 1.
	 */
	public int getTeam1Score() {
		return team1Score;
	}

	/**
	 * Method called to portray a strike for the current batter. If swung parameter
	 * is true, the batter is immediately out. Otherwise, 1 is added to the batters
	 * current count of called strikes (possibly resulting in the batter being out).
	 * This method does nothing if the game has ended
	 * 
	 * @param swung - true if the batter swung at the pitch,, false if it's a
	 *              "called" strike.
	 */
	public void strike(boolean swung) {
		if (gameEnded()) {
			return;
		}
		if (swung || strikeCount == MAX_STRIKES - 1) {
			outCount++;
			resetBallStrike();
		} else {
			strikeCount++;
		}
		if (outCount >= MAX_OUTS) {
			switchTeams();
		}
	}

	/**
	 * Method to show the batter is out due to a caught fly. This method will do
	 * nothing if the game has ended.
	 */
	public void caughtFly() {
		if (gameEnded()) {
			return;
		}
		if (flyCaught) {
			outCount++;
			resetBallStrike();
		}

		if (outCount >= MAX_OUTS) {
			switchTeams();
		}

	}

	/**
	 * Method to indicate a bad pitch at which the batter did not swing. This method
	 * will add 1 to the batter's ball count, possibly resulting in a walk. This
	 * method will do nothing if the game has ended.
	 */
	public void ball() {
		if (gameEnded()) {
			return;
		}
		if (outCount < MAX_OUTS) {
			ballCount++;
			if (ballCount == MAX_BALLS) {
				shiftRunnersForWalk();
				resetBallStrike();
				runnerOnFirst = true;
			}
		}
	}

	/**
	 * Method for the indication that the batter should hit the ball. This method
	 * does nothing if the game has ended. 
	 * 
	 * The interpretation of the distance parameter is as follows:
	 * 
	 * less than 15: the hit is a foul and the batter is immediately out.
	 * 
	 * at least 15, but less than 150: the hit is a single. An imaginary runner advances to first base, and all 
	 * other runners on base advance by 1. If there was a runner on third base, the score increases by 1.
	 * 
	 * at least 150, but less than 200: the hit is a double. An imaginary runner advances to second base, and
	 * all other runners on base advance by 2. If there were runners on second or third, the score increases 
	 * by 1 or 2.
	 * 
	 * at least 200, but less than 250: the hit is a triple. An imaginary runner advances to third base, and all 
	 * other runners on base advance by 3. If there were runners on first, second, or third, the score is increased 
	 * by 1, 2, or 3.
	 * 
	 * at least 250: the hit is a home run. All imaginary runners currently on base advance to home. The score is increased
	 * by 1 plus the number of runners on base.
	 * @param distance - Distance the ball travels (possibly negative).
	 */
	public void hit(int distance) {
		if (gameEnded()) {
			return;
		}
		boolean newRunner = true;
		
		resetBallStrike();
		
		if (distance < 15) {
			outCount++;
		} else {
			if (distance < 150) {
				shiftRunners();
				runnerOnFirst = true;
			} else if (distance < 200) {
				shiftRunners();
				shiftRunners();
				runnerOnSecond = newRunner;
			} else if (distance < 250) {
				shiftRunners();
				shiftRunners();
				shiftRunners();
				runnerOnThird = newRunner;
			} else if (distance >= 250) {
				shiftRunners();
				shiftRunners();
				shiftRunners();
				pointScore();
			}
		}
		if (outCount >= MAX_OUTS) {
			switchTeams();
		}
	}

	/**
	 * This is a helper function which increases the score in the desired areas.
	 */
	private void pointScore() {
		if (isInningTop) {
			team0Score++;
		} else {
			team1Score++;
		}
	}

	/**
	 * This is a helper method to shift the runners for a forced move. I.e, when
	 * there are too many balls called for the hitter and they must move to first.
	 */
	private void shiftRunnersForWalk() {
		if (runnerOnFirst) {
			if (runnerOnSecond) {
				if (runnerOnThird) {
					pointScore();
				}
				runnerOnThird = true;
			}
			runnerOnSecond = true;
		}
		runnerOnFirst = true;
	}

	/**
	 * This is a helper method that shifts the runners onto their respective bases
	 * when a single, double, triple, or home run is hit.
	 */
	private void shiftRunners() {
		if (runnerOnThird) {
			pointScore();
			runnerOnThird = false;
		}
		if (runnerOnSecond) {
			runnerOnThird = true;
			runnerOnSecond = false;
		}

		if (runnerOnFirst) {
			runnerOnSecond = true;
			runnerOnFirst = false;
		}
	}

	/**
	 * This is a helper method for clearing all runners off bases.
	 */
	private void clearBases() {
		runnerOnFirst = false;
		runnerOnSecond = false;
		runnerOnThird = false;
	}

	/**
	 * This is a helper method which resets the ball and strike count when it is
	 * needed (typically with a new batter).
	 */
	private void resetBallStrike() {
		ballCount = 0;
		strikeCount = 0;
	}

	/**
	 * This is a helper method that switches the teams and handles the necessities
	 * of switching a team. It sets outCount to zero, and calls the
	 * resetBallStrike() and clearBases() methods to have a clean slate for a new
	 * team.
	 */
	private void switchTeams() {
		outCount = 0;
		
		resetBallStrike();
		clearBases();

		if (isInningTop) {
			isInningTop = false;
		} else {
			isInningTop = true;
			theInning++;
		}
	}

	/**
	 * Returns a three-character string representing the players on base, in the
	 * order first, second, and third, where 'X' indicates a player is present and
	 * 'o' indicates no player. For example, the string "oXX" means that there are
	 * players on second and third but not on first.
	 * 
	 * @return three-character string showing players on base
	 */

	public String getBases() {
		return (runnerOnBase(1) ? "X" : "o") + (runnerOnBase(2) ? "X" : "o") + (runnerOnBase(3) ? "X" : "o");
	}

	/**
	 * Returns a one-line string representation of the current game state. The
	 * format is:
	 * 
	 * <pre>
	 *      ooo Inning:1 [T] Score:0-0 Balls:0 Strikes:0 Outs:0
	 * </pre>
	 * 
	 * The first three characters represent the players on base as returned by the
	 * <code>getBases()</code> method. The 'T' after the inning number indicates
	 * it's the top of the inning, and a 'B' would indicate the bottom. The score
	 * always shows team 0 first.
	 * 
	 * @return a single line string representation of the state of the game
	 */
	public String toString() {
		String bases = getBases();
		String topOrBottom = (isTopOfInning() ? "T" : "B");
		String fmt = "%s Inning:%d [%s] Score:%d-%d Balls:%d Strikes:%d Outs:%d";
		return String.format(fmt, bases, whichInning(), topOrBottom, getTeam0Score(), getTeam1Score(), getBallCount(),
				getCalledStrikes(), getCurrentOuts());
	}
}