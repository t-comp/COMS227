package hw3;

import static api.Direction.*;

import java.util.ArrayList;

import api.BodySegment;
import api.Cell;
import api.Direction;

/**
 * Represents a Lizard as a collection of body segments.
 * @author Taylor Bauer
 */
public class Lizard {
	/** Array List to hold Lizard's body segments */
	private ArrayList<BodySegment> segments;

	/**
	 * Constructs a Lizard object.
	 */
	public Lizard() {
		segments = new ArrayList<BodySegment>();
	}

	/**
	 * Sets the segments of the lizard. Segments should be ordered from tail to
	 * head.
	 * 
	 * @param segments list of segments ordered from tail to head
	 */
	public void setSegments(ArrayList<BodySegment> segments) {
		this.segments = segments;
	}

	/**
	 * Gets the segments of the lizard. Segments are ordered from tail to head.
	 * 
	 * @return a list of segments ordered from tail to head
	 */
	public ArrayList<BodySegment> getSegments() {
		return segments;
	}

	/**
	 * Gets the head segment of the lizard. Returns null if the segments have not
	 * been initialized or there are no segments.
	 * 
	 * @return the head segment
	 */
	public BodySegment getHeadSegment() {
		// if segments are not (not) initialized
		if (segments != null) {
			// get the head segment, which is at last index
			return segments.get(segments.size() - 1);
		}
		// return null if no segments
		return null;
	}

	/**
	 * Gets the tail segment of the lizard. Returns null if the segments have not
	 * been initialized or there are no segments.
	 * 
	 * @return the tail segment
	 */
	public BodySegment getTailSegment() {
		// if my segments are not (not) initialized
		if (segments != null) {
			// get the tail segment, which is at index zero
			return segments.get(0);
		}
		// return null if no segments
		return null;
	}

	/**
	 * Gets the segment that is located at a given cell or null if there is no
	 * segment at that cell.
	 * 
	 * @param cell to look for lizard
	 * @return the segment that is on the cell or null if there is none
	 */
	public BodySegment getSegmentAt(Cell cell) {
		// local variable for updating the index
		BodySegment segmentAt;
		// going through each element in the segment ArrayList
		for (int i = 0; i < segments.size(); ++i) {
			// updating segmentAt index on each iteration
			segmentAt = segments.get(i);
			// check for segmentAt is the same as given cell
			if (segmentAt.getCell() == cell) {
				return segmentAt;
			}
		}
		// return null if not desired segment
		return null;
	}

	/**
	 * Get the segment that is in front of (closer to the head segment than) the
	 * given segment. Returns null if there is no segment ahead.
	 * 
	 * @param segment of the starting segment
	 * @return the segment in front of the given segment or null
	 */
	public BodySegment getSegmentAhead(BodySegment segment) {
		// index of segment
		int i = segments.indexOf(segment);
		// check to ensure segment not at last index
		if (i < segments.size() - 1) {
			// segment ahead
			return segments.get(i + 1);
		}
		// return null if no segment
		return null;
	}

	/**
	 * Get the segment that is behind (closer to the tail segment than) the given
	 * segment. Returns null if there is not segment behind.
	 * 
	 * @param segment the starting segment
	 * @return the segment behind of the given segment or null
	 */
	public BodySegment getSegmentBehind(BodySegment segment) {
		// index of segment
		int i = segments.indexOf(segment);
		// check to ensure segment behind exists in array
		if (i > 0) {
			// segment behind
			return segments.get(i - 1);
		}
		// return null if no segment
		return null;
	}

	/**
	 * Gets the direction from the perspective of the given segment point to the
	 * segment ahead (in front of) of it. Returns null if there is no segment ahead
	 * of the given segment.
	 * 
	 * @param segment the starting segment
	 * @return the direction to the segment ahead of the given segment or null
	 */
	public Direction getDirectionToSegmentAhead(BodySegment segment) {
		// segment ahead
		BodySegment segmentAhead = getSegmentAhead(segment);
		// direction of ahead segment from helper method
		Direction directionAheadSegment = getDirectionLizardParts(segment, segmentAhead);

		return directionAheadSegment;
	}

	/**
	 * Gets the direction from the perspective of the given segment point to the
	 * segment behind it. Returns null if there is no segment behind of the given
	 * segment.
	 * 
	 * @param segment the starting segment
	 * @return the direction to the segment behind of the given segment or null
	 */
	public Direction getDirectionToSegmentBehind(BodySegment segment) {
		// segment behind
		BodySegment segmentBehind = getSegmentBehind(segment);
		// direction of behind segment from helper method
		Direction directionBehindSegment = getDirectionLizardParts(segment, segmentBehind);

		return directionBehindSegment;
	}

	/**
	 * Gets the direction in which the head segment is pointing. This is the
	 * direction formed by going from the segment behind the head segment to the
	 * head segment. A lizard that does not have more than one segment has no
	 * defined head direction and returns null.
	 * 
	 * @return the direction in which the head segment is pointing or null
	 */
	public Direction getHeadDirection() {
        if (segments.size() > 1) {
        // head segment
        BodySegment headSegment = getHeadSegment();
        // behind segment 
        BodySegment segmentBehind = getSegmentBehind(headSegment);
        // direction of head
        Direction directionHead = getDirectionLizardParts(segmentBehind, headSegment);
        
        return directionHead;
        }
        return null;
    }
	
	/**
	 * Gets the direction in which the tail segment is pointing. This is the
	 * direction formed by going from the segment ahead of the tail segment to the
	 * tail segment. A lizard that does not have more than one segment has no
	 * defined tail direction and returns null.
	 * 
	 * @return the direction in which the tail segment is pointing or null
	 */
	public Direction getTailDirection() {
        if (segments.size() > 1) {
        // tail segment
        BodySegment tailSegment = getTailSegment();
        // ahead segment 
        BodySegment segmentAhead = getSegmentAhead(tailSegment);
        // direction of tail
        Direction directionTail = getDirectionLizardParts(segmentAhead, tailSegment);
        
        return directionTail;
        }
        return null;
    }
	
	/**
	 * HELPER METHOD: Determines direction between two lizard segments
	 * If desired segment is eactly ahead of current segment
	 * Returns the direction to move from those two segments
	 * Returns null if segments are not "in-line" or does not exist
	 * 
	 * @param segment the current segment for determining
	 * @param lizardPartSegment desired segment for which direction is wanted
	 * @return the direction from the given segment to the desired segment or null
	 */
	private Direction getDirectionLizardParts(BodySegment segment, BodySegment lizardPartSegment) {
		// check to make there is segment
		if (lizardPartSegment != null) {
			// cell coords of current segment
			int col = segment.getCell().getCol();
			int row = segment.getCell().getRow();
			// cell coordinates of the segment ahead/behind
			int lizardPartCol = lizardPartSegment.getCell().getCol();
			int lizardPartRow = lizardPartSegment.getCell().getRow();

			// check horizontal direction, if segments are in same row
			if (row == lizardPartRow) {
				// if the col number is greater, then it's direction is to the left, if not, it's to the right
				if (col > lizardPartCol) {
					return LEFT;
				} else {
					return RIGHT;
				}
				// check vertical direction, if segments are in same column
			} else if (col == lizardPartCol) {
				// if row number is greater, then it's direction is up, if not, it's down
				if (row > lizardPartRow) {
					return UP;
				} else {
					return DOWN;
				}
			}
		}
		// return null if no segment
		return null;
	}
	
	@Override
	public String toString() {
		String result = "";
		for (BodySegment seg : getSegments()) {
			result += seg + " ";
		}
		return result;
	}
}
