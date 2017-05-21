package DifficultyAlgorithm;

import java.util.ArrayList;
import java.util.Iterator;

import shared.Edge;
import shared.MazeUtil;

public class DifficultMST
{
	public static Iterable<Edge> modifyDifficulty(int start, int goal, int height, int width, Iterator<Edge> mst)
	{
		MazeInformation mazeInfo = new MazeInformation(mst, start, goal, height, width);
		
		long startTime = System.currentTimeMillis();
		System.out.println("Start Time(ms): " + startTime);
		
		NewSolutionEdge solutionEdge = getNewSolutionEdge(mazeInfo, mazeInfo.getSolutionPaths().get(0), new DefaultNewSolutionEdgeComparator());
		
		long newSolutionTime = System.currentTimeMillis();
		System.out.println("New Solution Edge Time(ms): " + (newSolutionTime - startTime));
		System.out.println("New Solution Edge: " + solutionEdge.getEdge().toString());
		
		Edge loopEdge = getNewLoopEdge(mazeInfo, new DefaultPathComparator(mazeInfo), new DefaultLoopComparator());
		
		long newLoopTime = System.currentTimeMillis();
		System.out.println("New Loop Edge Time(ms): " + (newLoopTime - newSolutionTime));
		System.out.println("New Loop Edge: " + loopEdge.toString());
		
		// Create the new modified maze
		ArrayList<Edge> modifiedMaze = mazeInfo.getMaze();
		
		modifiedMaze.add(solutionEdge.getEdge());
		System.out.println("New Solution Edge added");
		
		if (!edgeCompare(loopEdge, solutionEdge.getEdge()))
		{
			// Add if unique
			modifiedMaze.add(loopEdge);
			System.out.println("New Loop Edge added");
		}
		
		return modifiedMaze;
	}
	
	private static Edge getNewLoopEdge(MazeInformation mazeInfo, IPathComparer pathComparer, ILoopComparer loopComparer)
	{
		ArrayList<Path> pathsCopy = new ArrayList<Path>();
		for (Path p : mazeInfo.getPaths())
		{
			pathsCopy.add(p);
		}
		
		pathsCopy.sort(pathComparer);
		
		ArrayList<Loop> possibleLoops = getPossibleLoops(mazeInfo, pathsCopy);
		System.out.println("Number of possible loops: " + possibleLoops.size());
		
		Loop loop = null;
		for (Loop l : possibleLoops)
		{
			if (loop == null)
			{
				loop = l;
				continue;
			}
			
			if (loopComparer.compare(l, loop) > 0)
			{
				loop = l;
			}
		}
		
		return loop.getEdge();
	}
	
	private static ArrayList<Loop> getPossibleLoops(MazeInformation mazeInfo, ArrayList<Path> paths)
	{
		ArrayList<Loop> possibleLoops = new ArrayList<Loop>();
		
		int pathsIndex = 0;
		while (possibleLoops.size() < 2)
		{
			// Check to see if we've exceeded the number of paths
			if (pathsIndex >= paths.size())
			{
				break;
			}
			
			// Get the path and an array of all paths minus our selected path
			Path p1 = paths.get(pathsIndex);
			Path[] otherPaths = paths.stream().filter(path -> path != p1).toArray(Path[]::new);
			// Create the current traversal list
			ArrayList<Integer> currentPath = new ArrayList<Integer>();
			
			// Traverse the selected path
			for (int index = 0; index < p1.length(); index++)
			{
				// Get the current vertex and update the traversal list
				int currentVertex = p1.getPath()[index];
				currentPath.add(currentVertex);
				
				int[] adjacentVertex = MazeUtil.getSurroundingVertices(mazeInfo, currentVertex);
				
				// Loop through the adjacent vertex
				for (int v : adjacentVertex)
				{
					if (!mazeInfo.checkBounds(v)
						|| !MazeUtil.checkVerticesHasValidEdge(mazeInfo, currentVertex, v)
						|| MazeUtil.checkEdgeExistence(mazeInfo, currentVertex, v))
					{
						continue;
					}
					
					// Loop through other paths
					for (Path p2 : otherPaths)
					{
						if (!p2.contains(v))
						{
							continue;
						}
						
						// We've found a vertex-vertex pair that will create a loop between two paths
						// Edge between currentVertex and v will create a loop between p1 and p2
						// Find the index within p2 where v lies
						int edgeIndex = p2.find(v);
						Edge possibleEdge = new Edge(currentVertex, v, 0);
						
						ArrayList<Integer> forwardPath = new ArrayList<Integer>(currentPath);
						ArrayList<Integer> backwardPath = new ArrayList<Integer>(currentPath);
						
						// Loop forward
						for (int p2Index = edgeIndex; p2Index < p2.length(); p2Index++)
						{
							// Get the loop
							Loop loop = findLoopPathForIndex(p1, p2, forwardPath, possibleEdge, p2Index);
							// Add the loop if it is unique
							if (loop != null && possibleLoops.stream().filter(l -> loopCompare(l, loop)).count() == 0)
							{
								possibleLoops.add(loop);
							}
						}
						
						// Loop backward
						for (int p2Index = edgeIndex; p2Index >= 0; p2Index--)
						{
							// Get the loop
							Loop loop = findLoopPathForIndex(p1, p2, backwardPath, possibleEdge, p2Index);
							// Add the loop if it is unique
							if (loop != null && possibleLoops.stream().filter(l -> loopCompare(l, loop)).count() == 0)
							{
								possibleLoops.add(loop);
							}
						}
					}
				}
			}
			
			pathsIndex++;
		}
		
		return possibleLoops;
	}
	
	private static boolean edgeCompare(Edge e1, Edge e2)
	{
		int e1V = e1.either();
		int e1W = e1.other(e1V);
		
		int e2V = e2.either();
		int e2W = e2.other(e2V);
		
		if (e1V == e2V && e1W == e2W)
		{
			return true;
		}
		
		return false;
	}
	
	private static boolean loopCompare(Loop l1, Loop l2)
	{
		int l1V = l1.getEdge().either();
		int l1W = l1.getEdge().other(l1V);
		
		int l2V = l2.getEdge().either();
		int l2W = l2.getEdge().other(l2V);
		
		// Compare the edges
		if (l1V != l2V || l1W != l2W)
		{
			return false;
		}
		
		// Compare loop lengths
		if (l1.getLoop().length != l2.getLoop().length)
		{
			return false;
		}
		
		// Compare each vertex of the loop
		for (int index = 0; index < l1.getLoop().length; index++)
		{
			if (l1.getLoop()[index] != l2.getLoop()[index])
			{
				return false;
			}
		}
		
		return true;
	}
	
	private static Loop findLoopPathForIndex(Path p1, Path p2, ArrayList<Integer> currentPath, Edge edge, int p2Index)
	{
		// Get the p2 index vertex and update the current path
		int p2V = p2.getPath()[p2Index];
		currentPath.add(p2V);

		// We've found a loop!
		if (p1.contains(p2V))
		{
			// Create the loop path
			int[] bPath = new int[currentPath.size()];
			for (int i = 0; i < currentPath.size(); i++)
			{
				bPath[i] = currentPath.get(i);
			}
			
			// Return the new loop
			return new Loop(p1, p2, bPath, edge);
		}
		
		return null;
	}
	
	private static NewSolutionEdge getNewSolutionEdge(MazeInformation mazeInfo, Path solutionPath, INewSolutionEdgeComparer comparer)
	{
		ArrayList<Edge> solutionPathJunctions = getSolutionPathJunctionEdges(mazeInfo, solutionPath, false);
		
		if (solutionPathJunctions.isEmpty())
		{
			// :(
			return null;
		}
		
		ArrayList<NewSolutionEdge> possibleEdges = getPossibleNewSolutionEdges(mazeInfo, solutionPath, solutionPathJunctions);
		
		if (possibleEdges.isEmpty())
		{
			// :(
			return null;
		}
		
		NewSolutionEdge edge = null;
		for (NewSolutionEdge e : possibleEdges)
		{
			if (edge == null)
			{
				edge = e;
				continue;
			}
			
			if (comparer.compare(e, edge) > 0)
			{
				edge = e;
			}
		}
		
		return edge;
	}
	
	private static ArrayList<Edge> getSolutionPathJunctionEdges(MazeInformation mazeInfo, Path solutionPath, boolean considerStartVertex)
	{
		ArrayList<Edge> solutionPathJunctions = new ArrayList<Edge>();
		
		// Find the closest junction on the solution path first
		for (int index = solutionPath.length() - 2; index >= 0; index--)
		{
			// Get our current node along the solution path and its edges
			int currentNode = solutionPath.getPath()[index];
			ArrayList<Edge> nodeEdges = mazeInfo.getNodeEdgeMap().get(currentNode);
			if (nodeEdges.size() <= 2 && (index != 0 || !considerStartVertex)) // All Paths start from the start vertex therefore solutionPath[0] = startVertex
			{
				// There is no junction, continue
				continue;
			}
			
			int forwardNode = solutionPath.getPath()[index+1];
			
			int backwardNode = -1;
			if (index != 0)
			{
				backwardNode = solutionPath.getPath()[index-1];
			}
			
			// Loop through the junctions edges to find an edge
			for (Edge e : nodeEdges)
			{
				int v = e.either();
				int w = e.other(v);
				
				// Discard forward edge
				if (v == forwardNode || w == forwardNode)
				{
					continue;
				}
				
				// Discard backward edge
				if (backwardNode != -1 && (v == backwardNode || w == backwardNode))
				{
					continue;
				}

				// Add the edge to our list of possible edges
				// leading from the given solution path
				solutionPathJunctions.add(e);
			}
		}
		
		return solutionPathJunctions;
	}

	private static ArrayList<NewSolutionEdge> getPossibleNewSolutionEdges(MazeInformation mazeInfo, Path solutionPath, ArrayList<Edge> solutionPathJunctions)
	{
		ArrayList<NewSolutionEdge> possibleEdges = new ArrayList<NewSolutionEdge>();
		
		for (Edge e : solutionPathJunctions)
		{
			// Get the vertex not on the solution path
			int v = e.either();
			if (solutionPath.contains(v))
			{
				v = e.other(v);
			}
			
			int solutionVertex = e.other(v);
			for (Path p : mazeInfo.getPaths())
			{
				if (p == solutionPath || !p.contains(v))
				{
					continue;
				}
				
				// The path deviates from the solution path so find the indices at which the deviation occurs
				int vIndex = -1, sIndex = -1;
				for (int index = 0; index < p.length(); index++)
				{
					if (p.getPath()[index] == v)
					{
						vIndex = index;
					}
					else if (p.getPath()[index] == solutionVertex)
					{
						sIndex = index;
					}
				}
				
				if (vIndex == -1 || sIndex == -1)
				{
					// We haven't found the index of either
					continue;
				}
				
				// Find out which direction we need to traverse the new path
				int direction = -1;
				int start = sIndex - 1;
				int end = 0;
				
				if (vIndex > sIndex)
				{
					direction = 1;
					start = vIndex + 1;
					end = p.length();
				}
				
				// Check our path traversal variables so we don't need to catch exceptions
				if (direction == 0
					|| start < 0
					|| start >= p.length()
					|| end < 0
					|| end > p.length())
				{
					continue;
				}
				
				// Traverse the path
				if (end != 0)
				{
					for (int index = start; index < end; index += direction)
					{
						NewSolutionEdge edge = findPathSolutionEdge(mazeInfo, solutionPath, p, direction, start, index);
						if (edge != null)
						{
							possibleEdges.add(edge);
						}
					}
				}
				else
				{
					for (int index = start; index >= end; index += direction)
					{
						NewSolutionEdge edge = findPathSolutionEdge(mazeInfo, solutionPath, p, direction, start, index);
						if (edge != null)
						{
							possibleEdges.add(edge);
						}
					}
				}
			}
		}
		
		return possibleEdges;
	}
	
	private static NewSolutionEdge findPathSolutionEdge(MazeInformation mazeInfo, Path solutionPath, Path p, int direction, int start, int index)
	{
		int vertex = p.getPath()[index];
		
		int[] adjacentVertices = MazeUtil.getSurroundingVertices(mazeInfo, vertex);
		
		for (int i : adjacentVertices)
		{
			if (!mazeInfo.checkBounds(i) // Check if vertex is within the bounds of the maze
				|| !MazeUtil.checkVerticesHasValidEdge(mazeInfo, vertex, i) // Check we can create a valid edge between the two
				|| !solutionPath.contains(i)) // Check that the adjacent vertex lies on the solution path
			{
				continue;
			}
			
			if (MazeUtil.checkEdgeExistence(mazeInfo, vertex, i))
			{
				continue;
			}
			
			// Create and return new edge between the current vertex and its adjacent vertex
			return new NewSolutionEdge(new Edge(vertex, i, 0), p, direction, index, Math.abs(start - index) + 2);
		}
		
		return null;
	}
}
