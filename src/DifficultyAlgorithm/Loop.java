package DifficultyAlgorithm;

import shared.Edge;

public class Loop
{
	private final Path mP1;
	private final Path mP2;
	private final int[] mLoop;
	private final Edge mEdge;
	
	public Loop(Path p1, Path p2, int[] loop, Edge edge)
	{
		mP1 = p1;
		mP2 = p2;
		mLoop = loop;
		mEdge = edge;
	}
	
	public Path p1()
	{
		return mP1;
	}
	
	public Path p2()
	{
		return mP2;
	}
	
	public Edge getEdge()
	{
		return mEdge;
	}
	
	public int[] getLoop()
	{
		return mLoop;
	}
}
