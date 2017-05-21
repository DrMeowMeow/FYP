package DifficultyAlgorithm;

/**
 * The default comparator for {@code Path} objects.
 * Compares using the length of the Path & whether are solution paths.
 * @author Jared Sibson
 * @version 1.0
 */
public class DefaultPathComparator implements IPathComparer
{
	private final MazeInformation mMaze;
	
	public DefaultPathComparator(MazeInformation mazeInfo)
	{
		mMaze = mazeInfo;
	}
	
	@Override
	public int compare(Path p1, Path p2)
	{
		boolean p1S = isSolutionPath(p1);
		boolean p2S = isSolutionPath(p2);
		
		if (p1S && !p2S)
		{
			return -1;
		}
		
		if (p1S && p2S)
		{
			return 0;
		}
		
		if (!p1S && p2S)
		{
			return 1;
		}
		
		if (p1.length() > p2.length())
		{
			return 1;
		}
		
		if (p1.length() == p2.length())
		{
			return 0;
		}
		
		return -1;
	}
	
	private boolean isSolutionPath(Path p)
	{
		if (p.getPath()[p.getPath().length - 1] == mMaze.getGoalVertex())
		{
			return true;
		}
		
		return false;
	}
}
