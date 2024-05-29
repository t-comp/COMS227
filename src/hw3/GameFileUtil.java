package hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import api.BodySegment;
import api.Cell;
import api.Exit;
import api.Wall;

/**
 * Utility class with static methods for loading game files.
 * @author Taylor Bauer
 */
public class GameFileUtil {
	/**
	 * Loads the file at the given file path into the given game object. When the
	 * method returns the game object has been modified to represent the loaded
	 * game.
	 * 
	 * @param filePath the path of the file to load
	 * @param game the game to modify
	 * @throws FileNotFoundException
	 */
	public static void load(String filePath, LizardGame game) {
		Integer width;
		Integer height;
		Scanner scnr = null;

		File file = new File(filePath);

		try {
			scnr = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
		// read the next (first) line of file, i.e., grid layout & reset grid to desired dimensions
		String fileLine = scnr.nextLine();
		String[] gridConfigs = fileLine.split("x");
		width = Integer.parseInt(gridConfigs[0]);
		height = Integer.parseInt(gridConfigs[1]);
		game.resetGrid(width, height);

		// get next contents of file, using only board layout dims to place walls & exits
		for (int row = 0; row < height; ++row) {
			fileLine = scnr.nextLine();
			for (int col = 0; col < width; ++col) {
				// if a 'W' is read, place a new wall to grid
				if (fileLine.charAt(col) == 'W') {
					game.addWall(new Wall(game.getCell(col, row)));
					// if an 'E' is read, place a new exit to grid
				} else if (fileLine.charAt(col) == 'E') {
					game.addExit(new Exit(game.getCell(col, row)));
				}
			}
		}
		// while loop for lizard
		while (scnr.hasNextLine()) {
			fileLine = scnr.nextLine();
			String[] lizardParts = fileLine.trim().split(" ");
			// lizard construct
			Lizard godzilla = new Lizard();
			// use to hold initial lizard segment positions
			ArrayList<BodySegment> initLizardPos = new ArrayList<>();
			// iterate through the array of lizardPart elements, start at 1 (index 0 holds 'L')
			for (int i = 1; i < lizardParts.length; ++i) {
				String[] coords = lizardParts[i].split(",");
				// if 2 coordinates (col & row)
				if (coords.length == 2) {
					// parse column coord
					int col = Integer.parseInt(coords[0]);
					// parse row coord
					int row = Integer.parseInt(coords[1]);
					// create new body segment at that cell
					BodySegment lizardBodySegment = new BodySegment(godzilla, game.getCell(col, row));
					// add to array of initial lizard segment positions
					initLizardPos.add(lizardBodySegment);
				}
			}
			// set the lizard segments
			godzilla.setSegments(initLizardPos);
			// add this lizard to the grid/game
			game.addLizard(godzilla);
		}
		scnr.close();
	}
}