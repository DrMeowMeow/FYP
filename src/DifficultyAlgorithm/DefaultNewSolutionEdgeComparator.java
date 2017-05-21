package DifficultyAlgorithm;

/**
 * The default comparator for {@code NewSolutionEdge} objects.
 * Compares using the length of the associated Path.
 * @author Jared Sibson
 * @version 1.0
 */
public class DefaultNewSolutionEdgeComparator implements INewSolutionEdgeComparer
{
	@Override
	public int compare(NewSolutionEdge e1, NewSolutionEdge e2)
	{
		if (e1.getLength() > e2.getLength())
		{
			return 1;
		}
		
		if (e1.getLength() == e2.getLength())
		{
			return 0;
		}
		
		return -1;
	}

}
