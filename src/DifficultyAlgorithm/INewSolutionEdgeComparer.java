package DifficultyAlgorithm;

import java.util.Comparator;

/**
 * Interface for defining a {@code NewSolutionEdge} {@code Comparator}.
 * @author Jared Sibson
 * @version 1.0
 */
public interface INewSolutionEdgeComparer extends Comparator<NewSolutionEdge>
{
	/**
	 * Compares two given {@code NewSolutionEdge} objects to determine their order.
	 * Returns a negative integer, zero, or a positive integer if the first argument is less than, equal to, or greater than the second.
	 * @param e1 the first object to be compared
	 * @param e2 the second object o be compared
	 */
	public int compare(NewSolutionEdge e1, NewSolutionEdge e2);
}
