import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import algorithms.DepthFirstSearchMST;
import algorithms.KruskalMST;
import algorithms.PrimMST;

import shared.Edge;
import shared.EdgeWeightedGraph;

@SuppressWarnings("serial")
public class Maze extends Canvas
{
	private static final int sCellSize = 25;
	private static final Color sBackgroundColour = Color.black;
	private static final Color sCellColour = Color.white;
	private static final Color sStartCellColour = Color.blue;
	private static final Color sFinishCellColour = Color.red;
	
	private final int mHeight;
	private final int mWidth;
	private Iterable<Edge> mMST;
	
	public Maze(int height, int width, Iterable<Edge> mst)
	{
		mHeight = height;
		mWidth = width;
		initCanvas();
		mMST = mst;
	}
	
	public Maze(int height, int width, int seed, MazeGenerationType type) throws Exception
	{
		mHeight = height;
		mWidth = width;
		initCanvas();
		
		EdgeWeightedGraph graph = generateGraph(height, width);
		switch (type)
		{
			case Kruskals:
			{
				KruskalMST kruskals = new KruskalMST(graph, seed);
				mMST = kruskals.edges();
				break;
			}
			case Prims:
			{
				PrimMST prims = new PrimMST(graph, seed);
				mMST = prims.edges();
				break;
			}
			case DepthFirstSearch:
			{
				DepthFirstSearchMST dfs = new DepthFirstSearchMST(graph, seed);
				mMST = dfs.edges();
				break;
			}
		}
		
		if (mMST == null)
		{
			throw new Exception("Minimum spanning tree not initialised");
		}
	}
	
	private void initCanvas()
	{
		setBackground(sBackgroundColour);
		setSize(sCellSize * (mWidth + 2), sCellSize * (mHeight + 2));
	}
	
	public Iterable<Edge> getMaze()
	{
		return mMST;
	}
	
	public int getMazeHeight()
	{
		return mHeight;
	}
	
	public int getMazeWidth()
	{
		return mWidth;
	}
	
	public void export(String filePath) throws IOException
	{
		BufferedImage image = new  BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics = image.createGraphics();
	    paint(graphics);
	    graphics.dispose();
	    
        FileOutputStream fileStream = new FileOutputStream(filePath);
        ImageIO.write(image, "png", fileStream);
        fileStream.close();
	}
	
	@Override
	public void paint(Graphics g)
	{
		// Draw starting grid
		g.setColor(sCellColour);
		for (int y = 0; y < mHeight; y++)
		{
			for (int x = 0; x < mWidth; x++)
			{
				g.drawRect(sCellSize * (x + 1), sCellSize * (y + 1), sCellSize, sCellSize); // Draw each cell
			}
		}
		
		// Draw start cell
		g.setColor(sStartCellColour);
		g.fillRect(sCellSize + 1, sCellSize + 1, sCellSize - 1, sCellSize - 1); // Cell 0
		
		// Draw finish cell
		g.setColor(sFinishCellColour);
		g.fillRect((sCellSize * mWidth) + 1, (sCellSize * mHeight) + 1, sCellSize - 1, sCellSize - 1); // Cell (w*h)
		
		// Start removing walls where edges exist
		g.setColor(sBackgroundColour);
		for (Edge e : mMST)
		{
			int v = e.either();
			int w = e.other(v);
			
			int vRectPosX = sCellSize * ((v % mWidth) + 1);
			int vRectPosY = sCellSize * (((v - (v % mWidth)) / mWidth) + 1);
			
			int wRectPosX = sCellSize * ((w % mWidth) + 1);
			int wRectPosY = sCellSize * (((w - (w % mWidth)) / mWidth) + 1);
			
			if (v + 1 == w)
			{
				g.drawLine(vRectPosX + sCellSize, vRectPosY + 1, vRectPosX + sCellSize, vRectPosY + sCellSize - 1);
			}
			else if (w + 1 == v)
			{
				g.drawLine(wRectPosX + sCellSize, wRectPosY + 1, wRectPosX + sCellSize, wRectPosY + sCellSize - 1);
			}
			else if (v + mWidth == w)
			{
				g.drawLine(vRectPosX + 1, vRectPosY + sCellSize, vRectPosX + sCellSize - 1, vRectPosY + sCellSize);
			}
			else if (w + mWidth == v)
			{
				g.drawLine(wRectPosX + 1, wRectPosY + sCellSize, wRectPosX + sCellSize - 1, wRectPosY + sCellSize);
			}
		}
	}
	
	public static int getCellSize()
	{
		return sCellSize;
	}
	
	private static EdgeWeightedGraph generateGraph(int height, int width)
	{
		int numberOfVerticies = height * width;
		
		EdgeWeightedGraph graph = new EdgeWeightedGraph(numberOfVerticies);
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				int position = (y * width) + x;
				
				if (x != width - 1)
				{
					// Add an edge to the right vertex
					graph.addEdge(new Edge(position, position + 1, 0));
				}
				
				if (y != height - 1)
				{
					// Add an edge to the vertex below
					graph.addEdge(new Edge(position, ((y + 1) * width) + x, 0));
				}
			}
		}
		
		return graph;
	}
}
