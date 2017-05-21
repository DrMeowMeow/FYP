package DifficultyAlgorithm;

/**
 * The default comparator for {@code Loop} objects.
 * Compares using the length of the Loop.
 * @author Jared Sibson
 * @version 1.0
 */
public class DefaultLoopComparator implements ILoopComparer
{
	@Override
	public int compare(Loop l1, Loop l2)
	{
		int l1L = getLoopLength(l1);
		int l2L = getLoopLength(l2);
		
		if (l1L > l2L)
		{
			return 1;
		}
		
		if (l1L == l2L)
		{
			return 0;
		}
		
		return -1;
	}
	
	private int getLoopLength(Loop l1)
	{
		int start = l1.getEdge().either();
		if (l1.getLoop()[l1.getLoop().length - 1] != start)
		{
			start = l1.getEdge().other(start);
		}
		
		int length = Integer.MAX_VALUE;
		for (int index = 0; index < l1.getLoop().length; index++)
		{
			if (l1.getLoop()[index] == start)
			{
				length = (l1.getLoop().length - 1) - index;
				break;
			}
		}
		
		return length;
	}
}
