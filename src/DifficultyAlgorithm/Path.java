package DifficultyAlgorithm;

public class Path
{
	private final int[] mCurrentPath;
	private final int mCurrentVertex;
	
	public Path(int vertex, int[] path)
	{
		mCurrentPath = path;
		mCurrentVertex = vertex;
	}
	
	public int find(int v)
	{
		for (int index = 0; index < mCurrentPath.length; index++)
		{
			if (mCurrentPath[index] == v)
			{
				return index;
			}
		}
		
		return -1;
	}
	
	public int[] getPath()
	{
		return mCurrentPath;
	}
	
	public int getHead()
	{
		return mCurrentPath[mCurrentPath.length - 1];
	}
	
	public boolean contains(int v)
	{
		for (int index = 0; index < mCurrentPath.length; index++)
		{
			if (mCurrentPath[index] == v)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public int length()
	{
		return mCurrentPath.length;
	}
	
	public int getVertex()
	{
		return mCurrentVertex;
	}
}
