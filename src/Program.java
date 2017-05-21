import java.io.IOException;

public class Program
{
	public static void main(String args[])
	{
		if (args.length > 0 && args[0].equalsIgnoreCase("help"))
		{
			System.out.println("Command Line Arguments:");
			System.out.println("[showUI(boolean):0,1]");
			System.out.println("[mazeType(int):0," + (MazeGenerationType.values().length - 1) + "]");
			System.out.println("[mazeWidth(int):" + MazeSettingsWindow.sMinWidth	+ ",n]");
			System.out.println("[mazeHeight(int):" + MazeSettingsWindow.sMinHeight + ",n]");
			System.out.println("[mazeSeed(int)]");
			System.out.println("[filePath(string)]");
			return;
		}
		
		boolean showUI = true;
		try
		{
			int cmdOption = Integer.parseInt(args[0]);
			if (cmdOption == 0)
			{
				showUI = false;
			}
		}
		catch (NumberFormatException | ArrayIndexOutOfBoundsException ex)
		{
			System.out.println("Failed to parse 'showUI' command line argument. Reverting to true.");
			showUI = true;
		}
		
		if (showUI)
		{
			MazeSettingsWindow window = new MazeSettingsWindow();
			window.setVisible(true);
		}
		else
		{
			MazeGenerationType mazeType;
			try
			{
				int cmdOption = Integer.parseInt(args[1]);
				mazeType = MazeGenerationType.values()[cmdOption];
			}
			catch (NumberFormatException | ArrayIndexOutOfBoundsException ex)
			{
				System.out.println("Invalid 'mazeType' command line argument.");
				
				MazeGenerationType[] mazeTypes = MazeGenerationType.values();
				for (int index = 0; index < mazeTypes.length; index++)
				{
					System.out.println(index + " = " + mazeTypes[index].toString());
				}
				
				return;
			}
			
			int mazeWidth;
			try
			{
				mazeWidth = Integer.parseInt(args[2]);
				if (mazeWidth < MazeSettingsWindow.sMinWidth)
				{
					System.out.println("Minimum maze width is " + MazeSettingsWindow.sMinWidth);
					return;
				}
			}
			catch (NumberFormatException | ArrayIndexOutOfBoundsException ex)
			{
				System.out.println("Invalid 'mazeWidth' command line argument.");
				return;
			}
			
			int mazeHeight;
			try
			{
				mazeHeight = Integer.parseInt(args[3]);
				if (mazeHeight < MazeSettingsWindow.sMinHeight)
				{
					System.out.println("Minimum maze height is " + MazeSettingsWindow.sMinHeight);
					return;
				}
			}
			catch (NumberFormatException | ArrayIndexOutOfBoundsException ex)
			{
				System.out.println("Invalid 'mazeHeight' command line argument.");
				return;
			}
			
			int mazeSeed;
			try
			{
				mazeSeed = Integer.parseInt(args[4]);
			}
			catch (NumberFormatException | ArrayIndexOutOfBoundsException ex)
			{
				System.out.print("Invalid 'mazeSeed' command line argument. Generating random seed... ");
				mazeSeed = (int) System.currentTimeMillis();
				System.out.println(mazeSeed);
			}
			
			String filePath;
			try
			{
				filePath = args[5];
				if (filePath == null)
				{
					// This is ugly, but code duplication is worse, eh?
					throw new IllegalArgumentException();
				}
			}
			catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ex)
			{
				System.out.println("Invalid 'filePath' command line argument.");
				return;
			}
			
			try
			{
				Maze newMaze = new Maze(mazeHeight, mazeWidth, mazeSeed, mazeType);
				newMaze.export(filePath);
				System.out.println("Maze exported: " + filePath);
			}
			catch (IOException ex)
			{
				System.out.println("An error occurred whilst saving the maze: " + ex.getMessage());
				return;
			}
			catch (Exception ex)
			{
				System.out.println("An error occurred generating the maze: " + ex.getMessage());
				return;
			}
		}
	}
}
