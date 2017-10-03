import java.util.Scanner;

public class AVLTreeInBlockChain {
	public static void main(String[] args) {
		AVLTree tree = new AVLTree();
		boolean exit = false;
		int zeros;

		try {
			zeros = readArgs(args);
		}
		catch (IllegalArgumentException e) {
			System.out.println("Invalid Argument");
			exit = true;
		}
		
		Scanner cmdReader = new Scanner(System.in);
		
		while (!exit) {
			System.out.print("> ");
			String cmd = cmdReader.nextLine();
			if (cmd.equals("exit"))
				exit = true;
			else if (cmd.startsWith("add ")) {
				String num = cmd.substring(4);
				try {
					tree.insert(Integer.parseInt(num));
				}
				catch (NumberFormatException e) {
					System.out.println("Invalid Command");
				}
				tree.print();
			}
			else if (cmd.startsWith("remove ")) {
				String num = cmd.substring(7);
				try {
					tree.remove(Integer.parseInt(num));
				}
				catch (NumberFormatException e) {
					System.out.println("Invalid Command");
				}
				tree.print();
			}
			else if (cmd.startsWith("lookup ")) {
				
			}
			else if (cmd.equals("validate")) {
				
			}
			else if (cmd.startsWith("modify ")) {
				
			}
			else
				System.out.println("Invalid Command");
		}
		
		cmdReader.close();
	}
	
	private static int readArgs(String[] args) throws IllegalArgumentException {
		String stringArgs = "";
		for (String arg : args)
			stringArgs += arg + " ";
		
		Scanner argReader = new Scanner(stringArgs);
		String arg;
		int number;
		boolean extraArgs;
		try {
			arg = argReader.next();
			number = argReader.nextInt();
			extraArgs = argReader.hasNext();
		}
		catch (Exception e) {
			throw new IllegalArgumentException();
		}
		finally {
			argReader.close();
		}
		
		if (!arg.equals("zeros") && !extraArgs)
			throw new IllegalArgumentException();
		else
			return number;
	}
}
