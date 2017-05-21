package DifficultyAlgorithm;

import java.util.Comparator;

/**
 * Interface for defining a {@code Loop} {@code Comparator}.
 * @author Jared Sibson
 * @version 1.0
 */
public interface ILoopComparer extends Comparator<Loop>
{
	/**
	 * Compares two given {@code Loop} objects to determine their order.
	 * Returns a negative integer, zero, or a positive integer if the first argument is less than, equal to, or greater than the second.
	 * @param l1 the first object to be compared
	 * @param l2 the second object o be compared
	 */
	public int compare(Loop l1, Loop l2);
}
