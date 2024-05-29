package hw3;

import static api.Direction.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import api.BodySegment;
import api.Cell;
import api.Direction;
import api.Exit;
import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Wall;


/**
 * Class that models a game.
 * @author Taylor Bauer
 */
public class LizardGame {
	/** Listens for dialog - from API */
	private ShowDialogListener dialogListener;
	/** Listens for game score - from API */
	private ScoreUpdateListener scoreListener;
	/** Width of the grid dimensions */
	private Integer width;
	/** Height of the grid dimensions */
	private Integer height;
	/** 2D array - holdings cells/grid layout */
	private Cell[][] cells;
	/** ArrayList - holds current lizards in game */
	private ArrayList<Lizard> lizards;

	/**
	 * Constructs a new LizardGame object with given grid dimensions.
	 * 
	 * @param width  number of columns
	 * @param height number of rows
	 */
	public LizardGame(int width, int height) {
		this.width = width;
		this.height = height;
		cells = new Cell[height][width];
		lizards = new ArrayList<Lizard>();
		// for every row
		for (int row = 0; row < height; ++row) {
			// go through every col
			for (int col = 0; col < width; ++col) {
				// create cell object for grid config/layout
				cells[row][col] = new Cell(col, row);
			}
		}
	}

	/**
	 * Get the grid's width.
	 * 
	 * @return width of the grid
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the grid's height.
	 * 
	 * @return height of the grid
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Adds a wall to the grid.
	 * 
	 * @param wall to add
	 */
	public void addWall(Wall wall) {
		wall.getCell().placeWall(wall);
	}

	/**
	 * Adds an exit to the grid.
	 * 
	 * @param exit to add
	 */
	public void addExit(Exit exit) {
		exit.getCell().placeExit(exit);
	}

	/**
	 * Gets a list of all lizards on the grid. Does not include lizards that have
	 * exited.
	 * 
	 * @return lizards list of lizards
	 */
	public ArrayList<Lizard> getLizards() {
		return lizards;
	}

	/**
	 * Adds the given lizard to the grid.
	 * <p>
	 * The scoreListener to should be updated with the number of lizards.
	 * 
	 * @param lizard to add
	 */
	public void addLizard(Lizard lizard) {
		// iterate over lizard segments
		for (int i = 0; i < lizard.getSegments().size(); ++i) {
			// the segments
			BodySegment segment = lizard.getSegments().get(i);
			// the cell of the current segment
			Cell cell = segment.getCell();
			// place lizard in cell
			cell.placeLizard(lizard);
		}
		// add lizard to list of lizards
		lizards.add(lizard);
		// update score after putting down the lizard
		scoreListener.updateScore(lizards.size());
	}

	/**
	 * Removes the given lizard from the grid. Be aware that each cell object knows
	 * about a lizard that is placed on top of it. It is expected that this method
	 * updates all cells that the lizard used to be on, so that they now have no
	 * lizard placed on them.
	 * <p>
	 * The scoreListener to should be updated with the number of lizards using
	 * updateScore().
	 * 
	 * @param lizard to remove
	 */
	public void removeLizard(Lizard lizard) {
		// iterate over lizard segments
		for (int i = 0; i < lizard.getSegments().size(); i++) {
			// the segments
			BodySegment segment = lizard.getSegments().get(i);
			// the cell of the current segment
			Cell cell = segment.getCell();
			// take away lizard from cell
			cell.removeLizard();
		}
		// remove lizard from list of lizards
		lizards.remove(lizard);
		// update score after putting removing the lizard
		scoreListener.updateScore(lizards.size());
	}

	/**
	 * Gets the cell for the given column and row.
	 * <p>
	 * If the column or row are outside of the boundaries of the grid the method
	 * returns null.
	 * 
	 * @param col column of the cell
	 * @param row of the cell
	 * @return the cell or null
	 */
	public Cell getCell(int col, int row) {
		// boundary check
		if (row >= 0 && row < height && col >= 0 && col < width) {
			// the cell
			return cells[row][col];
		}
		return null;
	}

	/**
	 * Gets the cell that is adjacent to (one over from) the given column and row,
	 * when moving in the given direction. For example (1, 4, UP) returns the cell
	 * at (1, 3).
	 * <p>
	 * If the adjacent cell is outside of the boundaries of the grid, the method
	 * returns null.
	 * 
	 * @param col the given column
	 * @param row the given row
	 * @param dir the direction from the given column and row to the adjacent cell
	 * @return the adjacent cell or null
	 */
	public Cell getAdjacentCell(int col, int row, Direction dir) {
		if (dir == UP) {
			row -= 1;
		}
		if (dir == DOWN) {
			row += 1;
		}
		if (dir == LEFT) {
			col -= 1;
		}
		if (dir == RIGHT) {
			col += 1;
		}
		// boundary check <3
		if (row >= 0 && row < height && col >= 0 && col < width) {
			return cells[row][col];
		}
		return null;
	}

	/**
	 * Resets the grid. After calling this method the game should have a grid of
	 * size width x height containing all empty cells. Empty means cells with no
	 * walls, exits, etc.
	 * <p>
	 * All lizards should also be removed from the grid.
	 * 
	 * @param width  number of columns of the resized grid
	 * @param height number of rows of the resized grid
	 */
	public void resetGrid(int width, int height) {
		lizards.clear();
		// update to initial dimensions
		this.width = width;
		this.height = height;
		// update grid
		cells = new Cell[height][width];
		// initialize that grid!!
		for (int col = 0; col < width; ++col) {
			for (int row = 0; row < height; ++row) {
				cells[row][col] = new Cell(col, row);
			}
		}
	}

	/**
	 * Returns true if a given cell location (col, row) is available for a lizard to
	 * move into. Specifically the cell cannot contain a wall or a lizard. Any other
	 * type of cell, including an exit is available.
	 * 
	 * @param row of the cell being tested
	 * @param col of the cell being tested
	 * @return true if the cell is available, false otherwise
	 */
	public boolean isAvailable(int col, int row) {
		// cell at col & row
		Cell cell = getCell(col, row);
		// check for availability
		if (cell.getWall() == null && cell.getLizard() == null) {
			// return true
			return true;
		}
		return false;
	}

	/**
	 * Move the lizard specified by its body segment at the given position (col,
	 * row) one cell in the given direction. The entire body of the lizard must move
	 * in a snake like fashion, in other words, each body segment pushes and pulls
	 * the segments it is connected to forward or backward in the path of the
	 * lizard's body. The given direction may result in the lizard moving its body
	 * either forward or backward by one cell.
	 * 
	 * <p>
	 * The segments of a lizard's body are linked together and movement must always
	 * be "in-line" with the body. It is allowed to implement movement by either
	 * shifting every body segment one cell over or by creating a new head or tail
	 * segment and removing an existing head or tail segment to achieve the same
	 * effect of movement in the forward or backward direction.
	 * <p>
	 * 
	 * If any segment of the lizard moves over an exit cell, the lizard should be
	 * removed from the grid.
	 * <p>
	 * If there are no lizards left on the grid the player has won the puzzle the
	 * the dialog listener should be used to display (see showDialog) the message
	 * "You win!".
	 * <p>
	 * It is possible that the given direction is not in-line with the body of the
	 * lizard (as described above), in that case this method should do nothing.
	 * <p>
	 * It is possible that the given column and row are outside the bounds of the
	 * grid, in that case this method should do nothing.
	 * <p>
	 * It is possible that there is no lizard at the given column and row, in that
	 * case this method should do nothing.
	 * <p>
	 * as * direction, in that case this method should do nothing.
	 * 
	 * 
	 * <p>
	 * <b>Developer's note: You may have noticed that there are a lot of details
	 * that need to be considered when implement this method method. It is highly
	 * recommend to explore how you can use the public API methods of this class,
	 * Grid and Lizard (hint: there are many helpful methods in those classes that
	 * will simplify your logic here) and also create your own private helper
	 * methods. Break the problem into smaller parts are work on each part
	 * individually.</b>
	 * 
	 * @param col the given column of a selected segment
	 * @param row the given row of a selected segment
	 * @param dir the given direction to move the selected segment
	 */
	public void move(int col, int row, Direction dir) {
		// current cell of the lizard
	    Cell currentLizCell = getCell(col, row);
	    // lizard construct
	    Lizard godzillaN = currentLizCell.getLizard();
	   
	    // if no lizard, do nothing!
	    if (godzillaN == null) {
	    	return;
	    }
	    
	    // the current segments godzilla (liz)
	    BodySegment zillaHead = godzillaN.getHeadSegment();
	    BodySegment zillaTail = godzillaN.getTailSegment();
	    
	    // direction of current lizard head 
	    Direction zillaHeadDirection = godzillaN.getHeadDirection();
	    //direction of current lizard tail
	    Direction zillaTailDirection = godzillaN.getTailDirection();
	    
	    // new head to be added
	    Cell newZillHeadCell = null;
	    // new tail to be added
	    Cell newZillaTailCell = null;
	    
	    // if direction matches head direction
	    if (dir == zillaHeadDirection) {
	    	newZillHeadCell = newLizardPartCell(zillaHead, dir);
	    	// if direction matches tail direction
	    } else if (dir == zillaTailDirection) {
	    	newZillaTailCell = newLizardPartCell(zillaTail, dir);
	    }
	    // if the new head cell exits & it's available
	    if (newZillHeadCell != null && isAvailable(newZillHeadCell.getCol(), newZillHeadCell.getRow())) {
	    	growNewLizardHead(godzillaN, newZillHeadCell);
	    	checkLizardExit(godzillaN, newZillHeadCell.getExit());
	    	// if new tail cell exits & it's available
	    } else if (newZillaTailCell != null && isAvailable(newZillaTailCell.getCol(), newZillaTailCell.getRow())) {
	    		growNewLizardTail(godzillaN, newZillaTailCell);
	    		checkLizardExit(godzillaN, newZillaTailCell.getExit());
	        }
	    }
	
	/**
	 * HELPER METHOD: Gets the adjacent cell for given segment and direction
	 * @param segment current segment
	 * @param dir the direction of desired move
	 * @return the cell adjacent to the given segment in the desired direction
	 */
    private Cell newLizardPartCell(BodySegment segment, Direction dir) {
    	Cell newLizardPartCell = getAdjacentCell(segment.getCell().getCol(), segment.getCell().getRow(), dir);
        return newLizardPartCell;
    }
    
    /**
     * HELPER METHOD: Grows new lizard head in specified cell
     * @param lizard the lizard that will grow a new head
     * @param newLizardHeadCell the cell where the new head will reside
     */
	private void growNewLizardHead(Lizard lizard, Cell newLizardHeadCell ) {
	    ArrayList<BodySegment> segments = lizard.getSegments();
        // grow (create) new head for next cell
	    BodySegment newLizardHead = new BodySegment(lizard, newLizardHeadCell);
	    // add newly grown head body segment
	    segments.add(newLizardHead);
	    // tail cell to (shed) remove
        Cell sheddedLizardTailCell = lizard.getTailSegment().getCell();
        // shed (remove contents) from the tail cell
        sheddedLizardTailCell.removeLizard();
        // shed (remove) the tail segment (first element)
        segments.remove(0);
	}
	
	/**
	 * HELPER METHOD: Grows new lizard tail in specified cell
	 * @param lizard the lizard that will grow a new tail
	 * @param newLizardTailCell the cell where the new tail will reside
	 */
	 private void growNewLizardTail(Lizard lizard, Cell newLizardTailCell) {
		 ArrayList<BodySegment> segments = lizard.getSegments();
         // grow (create) new tail segment
         BodySegment newLizardTail = new BodySegment(lizard, newLizardTailCell);
         // add newly grown tail body segment to first index
         segments.add(0, newLizardTail);
         // head cell to (shed) remove
         Cell sheddedLizardHeadCell = lizard.getHeadSegment().getCell(); 
         // shed (remove contents) from the head cell
         sheddedLizardHeadCell.removeLizard();
         // shed (remove) the head segment (last element)
         segments.remove(segments.size() - 1); 
	 }
	 
	 /**
	  * HELPER METHOD: Checks if lizard at exit and for a win
	  * If lizard is at exit, it gets removed
	  * 
	  * @param lizard the lizard
	  * @param exit the exit
	  */
	 private void checkLizardExit(Lizard lizard, Exit exit) {
	        if (exit != null) {
	            removeLizard(lizard);
	            checkLizardWinner();
	        }
	    }
	 
	 /**
	  * HELPER METHOD: Checks if all lizards have reached exit, meaning a win has occurred
	  */
	private void checkLizardWinner() {
	    if (lizards.isEmpty()) {
	        dialogListener.showDialog("You win!!");
	    }
	}        
		        	     
	/**
	 * Sets callback listeners for game events.
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * @param scoreListener  listener for updating the player's score
	 */
	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {
		this.dialogListener = dialogListener;
		this.scoreListener = scoreListener;
	}

	/**
	 * Load the game from the given file path
	 * 
	 * @param filePath location of file to load
	 * @throws FileNotFoundException
	 */
	public void load(String filePath) {
		GameFileUtil.load(filePath, this);
	}

	@Override
	public String toString() {
		String str = "---------- GRID ----------\n";
		str += "Dimensions:\n";
		str += getWidth() + " " + getHeight() + "\n";
		str += "Layout:\n";
		for (int y = 0; y < getHeight(); y++) {
			if (y > 0) {
				str += "\n";
			}
			for (int x = 0; x < getWidth(); x++) {
				str += getCell(x, y);
			}
		}
		str += "\nLizards:\n";
		for (Lizard l : getLizards()) {
			str += l;
		}
		str += "\n--------------------------\n";
		return str;
	}
}
