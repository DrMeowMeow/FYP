package algorithms;

import java.util.ArrayList;
import java.util.Random;

import shared.*;

/**
 *  The {@code PrimMST} class represents a data type for computing a
 *  <em>minimum spanning tree</em> in an edge-weighted graph.
 *  The edge weights can be positive, zero, or negative and need not
 *  be distinct. If the graph is not connected, it computes a <em>minimum
 *  spanning forest</em>, which is the union of minimum spanning trees
 *  in each connected component. The {@code weight()} method returns the 
 *  weight of a minimum spanning tree and the {@code edges()} method
 *  returns its edges.
 *  <p>
 *  This implementation uses <em>Prim's algorithm</em> with an indexed
 *  binary heap.
 *  The constructor takes time proportional to <em>E</em> log <em>V</em>
 *  and extra space (not including the graph) proportional to <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the {@code weight()} method takes constant time
 *  and the {@code edges()} method takes time proportional to <em>V</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  For alternate implementations, see {@link LazyPrimMST}, {@link KruskalMST},
 *  and {@link BoruvkaMST}.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class PrimMST
{
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private final Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private final boolean[] marked;     // marked[v] = true if v on tree, false otherwise
	private final Random random;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public PrimMST(EdgeWeightedGraph G, int seed)
    {
    	random = new Random(seed);
    	
        edgeTo = new Edge[G.V()];
        marked = new boolean[G.V()];
        ArrayList<Integer> verticies = new ArrayList<Integer>(G.V());
        
        for (int v = 0; v < G.V(); v++)
        {
        	verticies.add(v);
        }

        prim(G, random.nextInt(verticies.size()));

        // check optimality conditions
        assert check(G);
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(EdgeWeightedGraph G, int s)
    {
    	marked[s] = true;
    	ArrayList<Edge> edges = new ArrayList<Edge>();
    	
    	for (Edge edge : G.adj(s))
    	{
    		edges.add(edge);
    	}
    	
    	while (!edges.isEmpty())
    	{
    		int i = random.nextInt(edges.size());
    		Edge e = edges.remove(i);
    		int v = e.either();
    		int w = e.other(v);
    		
    		if (marked[v] && !marked[w])
    		{
    			scan(G, edges, w, e);
    		}
    		else if (marked[w] && !marked[v])
    		{
    			scan(G, edges, v, e);
    		}
    	}
    }

    // scan vertex v
    private void scan(EdgeWeightedGraph G, ArrayList<Edge> edges, int w, Edge e)
    {
    	edgeTo[w] = e;
    	marked[w] = true;
    	
    	for (Edge edge : G.adj(w))
    	{
    		edges.add(edge);
    	}
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     *    an iterable of edges
     */
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

    /**
     * Returns the sum of the edge weights in a minimum spanning tree (or forest).
     * @return the sum of the edge weights in a minimum spanning tree (or forest)
     */
    public double weight()
    {
        double weight = 0.0;
        for (Edge e : edges())
        {
            weight += e.weight();
        }
        
        return weight;
    }


    // check optimality conditions (takes time proportional to E V lg* V)
    private boolean check(EdgeWeightedGraph G)
    {
        // check weight
        double totalWeight = 0.0;
        for (Edge e : edges())
        {
            totalWeight += e.weight();
        }
        
        if (Math.abs(totalWeight - weight()) > FLOATING_POINT_EPSILON)
        {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, weight());
            return false;
        }

        // check that it is acyclic
        UF uf = new UF(G.V());
        for (Edge e : edges())
        {
            int v = e.either(), w = e.other(v);
            if (uf.connected(v, w))
            {
                System.err.println("Not a forest");
                return false;
            }
            
            uf.union(v, w);
        }

        // check that it is a spanning forest
        for (Edge e : G.edges())
        {
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w))
            {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (Edge e : edges())
        {
            // all edges in MST except e
            uf = new UF(G.V());
            for (Edge f : edges())
            {
                int x = f.either(), y = f.other(x);
                if (f != e)
                {
                	uf.union(x, y);
                }
            }

            // check that e is min weight edge in crossing cut
            for (Edge f : G.edges())
            {
                int x = f.either(), y = f.other(x);
                if (!uf.connected(x, y))
                {
                    if (f.weight() < e.weight())
                    {
                        System.err.println("Edge " + f + " violates cut optimality conditions");
                        return false;
                    }
                }
            }

        }

        return true;
    }
}
