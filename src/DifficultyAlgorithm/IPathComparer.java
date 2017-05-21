package DifficultyAlgorithm;

import java.util.Comparator;

/**
 * Interface for defining a {@code Path} {@code Comparator}.
 * @author Jared Sibson
 * @version 1.0
 */
public interface IPathComparer extends Comparator<Path>
{
	/**
	 * Compares two given {@code Path} objects to determine their order.
	 * Returns a negative integer, zero, or a positive integer if the first argument is less than, equal to, or greater than the second.
	 * @param p1 the first object to be compared
	 * @param p2 the second object o be compared
	 */
	public int compare(Path p1, Path p2);
}
