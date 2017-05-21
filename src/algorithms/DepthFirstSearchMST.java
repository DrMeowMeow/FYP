package algorithms;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import shared.Edge;
import shared.EdgeWeightedGraph;
import shared.Queue;

/**
 * 
 * @author Jared Sibson
 */
public class DepthFirstSearchMST
{
	private final Edge[] edgeTo;
	private final boolean[] marked;
	
	public DepthFirstSearchMST(EdgeWeightedGraph G, int seed)
	{
		Random random = new Random(seed);
		Stack<Integer> visited = new Stack<Integer>();
		ArrayList<Integer> unvisited = new ArrayList<Integer>();
		for (int i = 0; i < G.V(); i++)
		{
			unvisited.add(i);
		}
		
		edgeTo = new Edge[G.V()];
		marked = new boolean[G.V()];
		
		int v = random.nextInt(G.V());
		marked[v] = true;
		unvisited.remove(new Integer(v));
		
		while (!unvisited.isEmpty())
		{
			ArrayList<Edge> unvisitedNeighbours = getUnvisitedNeighbours(G, v);
			if (unvisitedNeighbours.size() > 0)
			{
				Edge e = unvisitedNeighbours.get(random.nextInt(unvisitedNeighbours.size()));
				int w = e.other(v);
				visited.push(w);
				unvisited.remove(new Integer(w));
				
				edgeTo[w] = e;
				
				v = w;
				marked[v] = true;
			}
			else if (visited.size() > 0)
			{
				v = visited.pop();
			}
		}
	}
	
	private ArrayList<Edge> getUnvisitedNeighbours(EdgeWeightedGraph G, int v)
	{
		ArrayList<Edge> unvisitedNeighbours = new ArrayList<Edge>();
		
		for (Edge e : G.adj(v))
		{
			int w = e.other(v);
			if (!marked[w])
			{
				unvisitedNeighbours.add(e);
			}
		}
		
		return unvisitedNeighbours;
	}
	
	public Iterable<Edge> edges()
	{
		Queue<Edge> mst = new Queue<Edge>();
        for (int v = 0; v < edgeTo.length; v++)
        {
            Edge e = edgeTo[v];
            if (e != null)
            {
                mst.enqueue(e);
            }
        }
        
        return mst;
    }
}
