package DifficultyAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import shared.Edge;
import shared.MazeUtil;

/**
 * Provides methods and information for a given Maze (minimum spanning tree).
 * @author Jared Sibson
 * @version 1.0
 */
public class MazeInformation
{
	private final int mStart;
	private final int mGoal;
	private final int mHeight;
	private final int mWidth;
	private final ArrayList<Edge> mMST;
	
	private HashMap<Integer, ArrayList<Edge>> mNodeEdgeMap;
	private ArrayList<Path> mSolutionPaths;
	private ArrayList<Path> mPaths;
	
	/**
	 * Creates a new instance of the {@code MazeInformation} class.
	 * @param mst the minimum spanning tree of the maze
	 * @param start the starting vertex of the maze
	 * @param goal the goal vertex of the maze
	 * @param height the height of the maze
	 * @param width the width of the maze
	 */
	public MazeInformation(Iterator<Edge> mst, int start, int goal, int height, int width)
	{
		mStart = start;
		mGoal = goal;
		mHeight = height;
		mWidth = width;
		
		// Copy the original minimum spanning tree
		mMST = new ArrayList<Edge>();
		while (mst.hasNext())
		{
			mMST.add(mst.next());
		}
		
		update();
	}
	
	/**
	 * Gets the start vertex of the maze.
	 * @return the integer vertex that represents the start of this maze
	 */
	public int getStartVertex()
	{
		return mStart;
	}
	
	/**
	 * Gets the goal vertex of the maze.
	 * @return the integer vertex that represents the goal of this maze
	 */
	public int getGoalVertex()
	{
		return mGoal;
	}
	
	/**
	 * Gets the minimum spanning tree of this maze.
	 * @return the copied minimum spanning tree of this maze
	 */
	public ArrayList<Edge> getMaze()
	{
		return mMST;
	}
	
	/**
	 * Gets a mapping of every edge leading from each vertex in the maze.
	 * @return a mapping for each vertex in the maze
	 */
	public HashMap<Integer, ArrayList<Edge>> getNodeEdgeMap()
	{
		return mNodeEdgeMap;
	}
	
	public ArrayList<Path> getSolutionPaths()
	{
		return mSolutionPaths;
	}
	
	public ArrayList<Path> getPaths()
	{
		return mPaths;
	}
	
	public int getHeight()
	{
		return mHeight;
	}
	
	public int getWidth()
	{
		return mWidth;
	}
	
	public boolean checkBounds(int v)
	{
		int max = mHeight * mWidth;
		if (v < 0)
		{
			return false;
		}
		
		if (v >= max)
		{
			return false;
		}
		
		return true;
	}
	
	public void update()
	{
		mNodeEdgeMap = MazeUtil.getNodeEdgeMap(mMST.iterator());
		mSolutionPaths = new ArrayList<Path>();
		mPaths = MazeUtil.getPaths(mNodeEdgeMap, mStart, mGoal);
		
		for (Path p : mPaths)
		{
			if (p.getHead() == mGoal)
			{
				mSolutionPaths.add(p);
			}
		}
	}
}