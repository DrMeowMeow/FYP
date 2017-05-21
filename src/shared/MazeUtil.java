package shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import DifficultyAlgorithm.Path;
import DifficultyAlgorithm.MazeInformation;

public final class MazeUtil
{
	private MazeUtil()
	{
	}
	
	public static HashMap<Integer, ArrayList<Edge>> getNodeEdgeMap(Iterator<Edge> mst)
	{
		HashMap<Integer, ArrayList<Edge>> edges = new HashMap<Integer, ArrayList<Edge>>();
		while (mst.hasNext())
		{
			Edge e = mst.next();
			int v = e.either();
			int w = e.other(v);

			if (!edges.containsKey(v))
			{
				ArrayList<Edge> newEdges = new ArrayList<Edge>();
				newEdges.add(e);
				edges.put(v, newEdges);
			}
			else if (!edges.get(v).contains(e))
			{
				edges.get(v).add(e);
			}
			
			if (!edges.containsKey(w))
			{
				ArrayList<Edge> newEdges = new ArrayList<Edge>();
				newEdges.add(e);
				edges.put(w, newEdges);
			}
			else if (!edges.get(w).contains(e))
			{
				edges.get(w).add(e);
			}
		}
		
		return edges;
	}
	
	public static ArrayList<Path> getPaths(HashMap<Integer, ArrayList<Edge>> nodeEdgeMap, int start, int goal)
	{
		ArrayList<Path> paths = new ArrayList<Path>();
		Queue<Path> currentPaths = new Queue<Path>();
		// Create the starting paths
		for (Edge e : nodeEdgeMap.get(start))
		{
			int[] currentPath = new int[2];
			currentPath[0] = start;
			currentPath[1] = e.other(start);
			
			currentPaths.enqueue(new Path(currentPath[1], currentPath));
		}
		
		while (!currentPaths.isEmpty())
		{
			Path p = currentPaths.dequeue();
			if (p.getHead() == goal)
			{
				// The path has reached the goal so add it to the list of solutions
				paths.add(p);
				continue;
			}
			
			// This path has reached a dead end as the only edge available is back the way we came
			if (nodeEdgeMap.get(p.getHead()).size() == 1
				&& p.length() > 1
				&& nodeEdgeMap.get(p.getHead()).get(0).other(p.getHead()) == p.getPath()[p.length() - 2]) // Compare the endpoints
			{
				paths.add(p);
				continue;
			}
			
			// Loop through each of the edges and create new paths
			for (Edge e : nodeEdgeMap.get(p.getHead()))
			{
				// Get the edges endpoint
				int v = e.other(p.getHead());
				if (p.contains(v))
				{
					// We've found the backwards edge or a loop
					continue;
				}
				
				// Copy the existing path
				int[] newPath = new int[p.length() + 1];
				for (int index = 0; index < p.length(); index++)
				{
					newPath[index] = p.getPath()[index];
				}
				
				// Add the edges endpoint
				newPath[newPath.length - 1] = v;
				
				// Add the new path
				currentPaths.enqueue(new Path(v, newPath));
			}
		}
		
		return paths;
	}
	
	/**
	 * Check whether or not an edge should bind two vertices.
	 * @param v
	 * @param w
	 * @return
	 */
	public static boolean checkVerticesHasValidEdge(MazeInformation mazeInfo, int v, int w)
	{
		if (v - mazeInfo.getWidth() == w)
		{
			// North
			return true;
		}
		
		if (v + mazeInfo.getWidth() == w)
		{
			// South
			return true;
		}
		
		int vHeight = (v - (v % mazeInfo.getWidth())) / mazeInfo.getWidth();
		int wHeight = (w - (w % mazeInfo.getWidth())) / mazeInfo.getWidth();
		
		if (vHeight != wHeight)
		{
			return false;
		}
		
		return true;
	}
	
	public static int[] getSurroundingVertices(MazeInformation mazeInfo, int v)
	{
		int[] adjacentVertex = new int[4];
		// Get the north, east, south and west adjacent vertices
		adjacentVertex[0] = v - mazeInfo.getWidth();
		adjacentVertex[1] = v + 1;
		adjacentVertex[2] = v + mazeInfo.getWidth();
		adjacentVertex[3] = v - 1;
		
		return adjacentVertex;
	}
	
	public static boolean checkEdgeExistence(MazeInformation mazeInfo, int v, int w)
	{
		for (Edge e : mazeInfo.getMaze())
		{
			int edgeV = e.either();
			int edgeW = e.other(edgeV);
			
			if ((v == edgeV && w == edgeW) || (v == edgeW && w == edgeV))
			{
				return true;
			}
		}
		
		return false;
	}
}
