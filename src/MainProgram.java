import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;

public class MainProgram {
	public static void main(String[] args) {
		boolean exit = false;
		int zeros = 0;

		try {
			zeros = readArgs(args);
		}
		catch (IllegalArgumentException e) {
			System.out.println("Invalid Argument");
			exit = true;
		}
		
		AVLTree tree = new AVLTree();
		Blockchain blockchain = new Blockchain(zeros);
		Integer numToRead = null;
		Scanner cmdReader = new Scanner(System.in);
		
		while (!exit) {
			System.out.print("> ");
			String cmd = cmdReader.nextLine();
			if (cmd.equals("exit"))
				exit = true;
			else if (cmd.startsWith("add ")) {
				String num = cmd.substring(4);
				try {
					numToRead = Integer.parseInt(num);
				}
				catch (NumberFormatException e) {
					System.out.println("Invalid Command");
					numToRead = null;
				}
				
				if(numToRead!=null) {
					tree.insert(numToRead);
					blockchain.addBlock(numToRead + " added", new AVLTree(tree));
					blockchain.print();
				}
			}
			else if (cmd.startsWith("remove ")) {
				String num = cmd.substring(7);
				try {
					numToRead = Integer.parseInt(num);
				}
				catch (NumberFormatException e) {
					System.out.println("Invalid Command");
					numToRead = null;
				}
				if(numToRead!=null) {
					tree.remove(numToRead);
					blockchain.addBlock(numToRead + " removed", new AVLTree(tree));
					blockchain.print();
				}
			}
			else if (cmd.startsWith("lookup ")) {
				
			}
			else if (cmd.equals("validate")) {
				if(blockchain.validate())
					System.out.println("Valid blockchain");
				else
					System.out.println("Invalid blockchain");
			}
			else if (cmd.startsWith("modify ")) {
				if(cmd.length()>=10) {
					boolean check = true;
					Scanner in = new Scanner(cmd.substring(7));
					try {
						numToRead = Integer.parseInt(in.next());
					} catch (NumberFormatException e) {
						numToRead = null;
						System.out.println("Invalid command");
						check = false;
					}
					if(check) {
						String fileName = in.next();
						System.out.println("Nombre de archivo: " + fileName + " numero: " + numToRead);
						try {
							FileReader fileReader = new FileReader(fileName);
							BufferedReader bufferedReader = new BufferedReader(fileReader);
							parseFile(bufferedReader, blockchain);

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
					
					in.close();
				}
				else
					System.out.println("Invalid Command");
			}
			else
				System.out.println("Invalid Command");
		}
		
		cmdReader.close();
	}
	
	private static int readArgs(String[] args) throws IllegalArgumentException {
		StringBuilder stringArgs = new StringBuilder();
		String space = " ";
		for (String arg : args) {
			stringArgs.append(arg);
			stringArgs.append(space);
		}
		
		Scanner argReader = new Scanner(stringArgs.toString());
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

	/**Parsea el archivo y modifica el bloque indicado
	 * */
	private static void parseFile(BufferedReader buff, Blockchain blockchain) throws InvalidFileFormatException, IOException {

		String blockIndexStr = buff.readLine();
		String nonceStr = buff.readLine();
		String dataStr = buff.readLine();
		String prevHashStr = buff.readLine();

		String [] indexArr = blockIndexStr.split(": ");
		if(indexArr.length > 2)
			throw new InvalidFileFormatException("formato del index no compatible");
		int index = Integer.parseInt(indexArr[1]);

		String [] nonceArr =  nonceStr.split(": ");
		if(nonceArr.length > 2)
			throw new InvalidFileFormatException("formato del nonce no compatible");
		int nonce = Integer.parseInt(nonceArr[1]);

		// hay que estandarizar los nombres de las operaciones para el archivo
		String [] dataArr =  dataStr.split(": ");
		if(dataArr.length > 4)
			throw new InvalidFileFormatException("formato de los datos no compatible");
		String operation = dataArr[1];

		//el arbol debe estar como una list en formato BFS en el archivo
		//me parece que BFS es la mejor manera asi no hace rotaciones cuando se crea el nuevo arbol
		String[] nodes = dataArr[2].split(",");
		AVLTree tree = new AVLTree();
		for(int i = 0; i< nodes.length; i++){
			tree.insert(Integer.parseInt(nodes[i]));
		}

		String[] modifiedArr = dataArr[3].split(",");
		if(modifiedArr.length == 0)
			throw  new InvalidFileFormatException("no hay nodos modificados en el array de nodos modificados");
		int[] modified = new int[modifiedArr.length];
		for(int i = 0;i<modifiedArr.length; i++){
			modified[i] = Integer.parseInt(modifiedArr[i]);
		}

		String [] prevHashArr =  prevHashStr.split(": ");
		if(indexArr.length > 2)
			throw new InvalidFileFormatException("formato del prevHash no compatible");
		String prevHash = prevHashArr[1];

		blockchain.modify(index, nonce, operation, tree, prevHash);
	}
}
