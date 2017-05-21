import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import DifficultyAlgorithm.DifficultMST;

import shared.Edge;

@SuppressWarnings("serial")
public class MazeWindow extends JFrame
{
	private static final int sMazeWidthOffset = 12;
	private static final int sMazeHeightOffset = 35;
	private static final int sExportButtonHeightOffset = 32;
	private final Maze mMaze;
	
	private String mTitle;
	private boolean mDifficultMazeOpened = false;
	private MazeWindow mDifficultMazeWindow;
	
	public MazeWindow(int height, int width, Iterable<Edge> mst)
	{
		MazeWindow window = this;
		initWindow(width, height);
		
		addDefaultExportButton(window);
		
		mMaze = new Maze(height, width, mst);
		this.add(mMaze);
	}
	
	public MazeWindow(int height, int width, int seed, MazeGenerationType type) throws Exception
	{
		mTitle = height + "x" + width + ":" + seed + type;
		this.setTitle(mTitle);
		MazeWindow window = this;
		initWindow(width, height);
		
		addDefaultExportButton(window);
		
		JButton difficultyButton = new JButton("Difficult");
		difficultyButton.setSize(this.getWidth(), 25);
		difficultyButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if (mDifficultMazeOpened)
					{
						if (mDifficultMazeWindow != null)
						{
							mDifficultMazeWindow.toFront();
							mDifficultMazeWindow.repaint();
						}
						
						return;
					}
					
					mDifficultMazeOpened = true;
					Iterable<Edge> newMaze = DifficultMST.modifyDifficulty(0, (height*width) - 1, height, width, mMaze.getMaze().iterator());
					
					mDifficultMazeWindow = new MazeWindow(height, width, newMaze);
					mDifficultMazeWindow.addWindowListener(new WindowListener()
					{
						@Override
						public void windowOpened(WindowEvent e) {}
						
						@Override
						public void windowIconified(WindowEvent e) {}
						
						@Override
						public void windowDeiconified(WindowEvent e) {}
						
						@Override
						public void windowDeactivated(WindowEvent e) {}
						
						@Override
						public void windowActivated(WindowEvent e) {}
						
						@Override
						public void windowClosed(WindowEvent e) {}
						
						@Override
						public void windowClosing(WindowEvent e)
						{
							mDifficultMazeWindow = null;
							mDifficultMazeOpened = false;
						}
					});
					
					mDifficultMazeWindow.setTitle("Modified Difficulty " + mTitle);
					mDifficultMazeWindow.setVisible(true);
				}
			});
		
		this.add(difficultyButton);
		
		mMaze = new Maze(height, width, seed, type);
		this.add(mMaze);
	}
	
	private void initWindow(int width, int height)
	{
		setLayout(new FlowLayout());
		setSize(Maze.getCellSize() * (width + 2) + sMazeWidthOffset, Maze.getCellSize() * (height + 2) + sMazeHeightOffset + sExportButtonHeightOffset);
		setResizable(false);
	}
	
	private void addDefaultExportButton(MazeWindow window)
	{
		JButton exportButton = new JButton("Export");
		exportButton.setSize(this.getWidth(), 25);
		exportButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					String filePath = JOptionPane.showInputDialog("Enter file path:");
					if (filePath == null || filePath.equals(""))
					{
						JOptionPane.showMessageDialog(window, "Invalid file path", "Error", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					try
					{
						mMaze.export(filePath);
					}
					catch (IOException ex)
					{
						JOptionPane.showMessageDialog(window, "Could not save maze: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
		
		this.add(exportButton);
	}
}
