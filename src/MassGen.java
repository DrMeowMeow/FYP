import java.util.Random;
import java.util.Scanner;

public class MassGen
{
	public static void main(String args[])
	{
		String[] arguments = new String[6];
		arguments[0] = "0"; // disable ui
		
		Scanner in = new Scanner(System.in);
		
		System.out.println("Width:");
		arguments[2] = in.nextLine(); // width 10 cells
		
		System.out.println("Height:");
		arguments[3] = in.nextLine(); // height 10 cells
		
		System.out.println("Iterations:");
		int count = in.nextInt();
		
		Random r = new Random();
		for (int index = 0; index < count; index++)
		{
			Integer seed = r.nextInt();
			arguments[4] = seed.toString();
			for (Integer mazeType = 0; mazeType < 3; mazeType++)
			{
				arguments[1] = mazeType.toString();
				arguments[5] = "MassGen\\" + arguments[2] + "x" + arguments[3] + " s " + seed + " m" + mazeType + ".png";
				Program.main(arguments);
			}
		}
		
		in.close();
	}
}
