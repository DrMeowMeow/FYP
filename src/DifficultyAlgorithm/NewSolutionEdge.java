package DifficultyAlgorithm;

import shared.Edge;

public class NewSolutionEdge
{
	private final Edge mEdge;
	private final Path mPath;
	private final int mDirection;
	private final int mPathIndex;
	private final int mLength;
	
	public NewSolutionEdge(Edge e, Path p, int direction, int pathIndex, int length)
	{
		mEdge = e;
		mPath = p;
		mDirection = direction;
		mPathIndex = pathIndex;
		mLength = length;
	}
	
	public Edge getEdge()
	{
		return mEdge;
	}
	
	public Path getPath()
	{
		return mPath;
	}
	
	public int getDirection()
	{
		return mDirection;
	}
	
	public int getIndex()
	{
		return mPathIndex;
	}
	
	public int getLength()
	{
		return mLength;
	}
}