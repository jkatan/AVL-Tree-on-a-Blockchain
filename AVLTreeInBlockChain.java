import java.util.Scanner;
import java.io.*;

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

				String fileName = cmd.substring(7);
				try {
					FileReader fileReader = new FileReader(fileName);
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					parseFile(bufferedReader);

				}
				catch(InvalidFileFormatException e){
					System.out.println("El formato del archivo no es compatible con el standard de input");
				}
				catch(FileNotFoundException e) {
					System.out.println("Unable to open file '" + fileName + "'");
				}
				catch(IOException e) {
					System.out.println("Error reading file '" + fileName + "'");
				}

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

	/**Parsea el archivo y agrega a la blockchain el bloque descripto en el archivo.
	 * */
	private static void parseFile(BufferedReader buff) throws InvalidFileFormatException, IOException {

		String blockIDStr = buff.readLine();
		String nonceStr = buff.readLine();
		String dataStr = buff.readLine();
		String prevHashStr = buff.readLine();

		String [] IDarr = blockIDStr.split(": ");
		if(IDarr.length > 2)
			throw new InvalidFileFormatException("formato del BlockID no compatible");
		int blockID = Integer.parseInt(IDarr[1]);

		String [] nonceArr =  nonceStr.split(": ");
		if(IDarr.length > 2)
			throw new InvalidFileFormatException("formato del nonce no compatible");
		int nonce = Integer.parseInt(nonceArr[1]);

		// hay que estandarisar los nombres de las operaciones para el archivo
		String [] DataArr =  dataStr.split(": ");
		if(IDarr.length > 4)
			throw new InvalidFileFormatException("formato de los datos no compatible");
		String operation = DataArr[1];

		//el arbol debe estar como una list en formato BFS en el archivo
		//me parece que BFS es la mejor manera asi no hace rotaciones cuando se crea el nuevo arbol
		String[] nodes = DataArr[2].split(",");
		AVLTree tree = new AVLTree();
		for(int i = 0; i< nodes.length; i++){
			tree.insert(Integer.parseInt(nodes[i]));
		}

		String[] modifiedArr = DataArr[3].split(",");
		if(modifiedArr.length == 0)
			throw  new InvalidFileFormatException("no hay nodos modificados en el array de nodos modificados");
		int[] modified = new int[modifiedArr.length];
		for(int i = 0;i<modifiedArr.length; i++){
			modified[i] = Integer.parseInt(modifiedArr[i]);
		}

		String [] prevHashArr =  prevHashStr.split(": ");
		if(IDarr.length > 2)
			throw new InvalidFileFormatException("formato del prevHash no compatible");
		int prevHash = Integer.parseInt(prevHashArr[1]);

		//hay que crear un bloque con blockID, nonce, operacion, tree, modificados , prevhash y despues
		//agregarlo a la blockchain


	}
}
