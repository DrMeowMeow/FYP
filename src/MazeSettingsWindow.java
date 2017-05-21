import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MazeSettingsWindow extends JFrame
{
	public static final int sMinHeight = 5;
	public static final int sMinWidth = 5;
	
	private JTextField mHeightTextField;
	private JTextField mWidthTextField;
	private JTextField mSeedTextField;
	
	public MazeSettingsWindow()
	{
		setResizable(false);
		setSize(180, 200);
		getContentPane().setLayout(null);
		MazeSettingsWindow window = this;
		addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent windowEvent)
				{
					System.exit(0);
				}
			});
		
		JLabel mHeightLabel = new JLabel("Height:");
		mHeightLabel.setBounds(10, 10, 40, 14);
		getContentPane().add(mHeightLabel);
		
		mHeightTextField = new JTextField();
		mHeightTextField.setBounds(55, 7, 85, 20);
		getContentPane().add(mHeightTextField);
		mHeightTextField.setColumns(10);
		
		JLabel mWidthLabel = new JLabel("Width:");
		mWidthLabel.setBounds(10, 35, 40, 14);
		getContentPane().add(mWidthLabel);
		
		mWidthTextField = new JTextField();
		mWidthTextField.setBounds(55, 33, 85, 20);
		mWidthTextField.setColumns(10);
		getContentPane().add(mWidthTextField);
		
		JLabel mSeedLabel = new JLabel("Seed:");
		mSeedLabel.setBounds(10, 60, 40, 14);
		getContentPane().add(mSeedLabel);
		
		mSeedTextField = new JTextField();
		mSeedTextField.setBounds(55, 57, 85, 20);
		mSeedTextField.setColumns(10);
		getContentPane().add(mSeedTextField);
		
		JLabel mAlgorithmLabel = new JLabel("Algorithm:");
		mAlgorithmLabel.setBounds(10, 85, 60, 14);
		getContentPane().add(mAlgorithmLabel);
		
		JComboBox<MazeGenerationType> mAlgorithmBox = new JComboBox<MazeGenerationType>();
		mAlgorithmBox.setBounds(10, 110, 130, 20);
		mAlgorithmBox.addItem(MazeGenerationType.Prims);
		mAlgorithmBox.addItem(MazeGenerationType.Kruskals);
		mAlgorithmBox.addItem(MazeGenerationType.DepthFirstSearch);
		getContentPane().add(mAlgorithmBox);
		
		JButton mCreateButton = new JButton("Create");
		mCreateButton.setBounds(10, 135, 130, 25);
		mCreateButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int height, width, seed;
					
					try
					{
						height = checkHeight();
						width = checkWidth();
						seed = checkSeed();
					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(window, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					try
					{
						MazeWindow mazeWindow = new MazeWindow(height, width, seed, (MazeGenerationType)mAlgorithmBox.getSelectedItem());
						mazeWindow.setVisible(true);
					}
					catch (Exception ex)
					{
						JOptionPane.showMessageDialog(window, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		getContentPane().add(mCreateButton);
	}
	
	private int checkHeight() throws Exception
	{
		int height;
		try
		{
			height = Integer.parseInt(mHeightTextField.getText());
		}
		catch (NumberFormatException ex)
		{
			throw new Exception("Error parsing height. Check it is a number!");
		}
		
		if (height < sMinHeight)
		{
			throw new Exception("Minimum height is " + sMinHeight + ".");
		}
		
		return height;
	}
	
	private int checkWidth() throws Exception
	{
		int width;
		try
		{
			width = Integer.parseInt(mWidthTextField.getText());
		}
		catch (NumberFormatException ex)
		{
			throw new Exception("Error parsing width. Check it is a number!");
		}
		
		if (width < sMinWidth)
		{
			throw new Exception("Minimum width is " + sMinWidth + ".");
		}
		
		return width;
	}
	
	private int checkSeed() throws Exception
	{
		try
		{
			return Integer.parseInt(mSeedTextField.getText());
		}
		catch (NumberFormatException ex)
		{
			throw new Exception("Error parsing seed. Check it is a number!");
		}
	}
}
