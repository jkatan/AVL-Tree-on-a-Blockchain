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
					blockchain.addBlock("add " + numToRead, new AVLTree(tree));
					blockchain.printCurrentBlock();
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
					blockchain.addBlock("remove " + numToRead, new AVLTree(tree));
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
						System.out.println("Nombre de archivo: " + fileName + ", Indice: " + numToRead);
						try {
							FileReader fileReader = new FileReader(fileName);
							BufferedReader bufferedReader = new BufferedReader(fileReader);
							parseFile(numToRead, bufferedReader, blockchain);

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
		if(args==null) {
			System.out.println("Invalid command");
			return -1;
		}
		
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
	private static void parseFile(int index, BufferedReader buff, Blockchain blockchain) throws InvalidFileFormatException, IOException {

		//String blockIndexStr = buff.readLine();
		String nonceStr = buff.readLine();
		String operationStr = buff.readLine();
		String treeBFS = buff.readLine();
		String modifiedNodesStr = buff.readLine();
		String prevHashStr = buff.readLine();
		
		int nonce = 0;
		String operation = null;

		/*String [] indexArr = blockIndexStr.split(": ");
		if(indexArr.length != 2 || !indexArr[0].equals("index"))
			throw new InvalidFileFormatException("formato del index no compatible");
		try {
			index = Integer.parseInt(indexArr[1]);
		} catch (NumberFormatException e) {
			index = -1;
			System.out.println("formato del index no compatible");
			return;
		}*/

		String [] nonceArr =  nonceStr.split(": ");
		if(nonceArr.length != 2 || !nonceArr[0].equals("nonce"))
			throw new InvalidFileFormatException("formato del nonce no compatible");
		try {
			nonce = Integer.parseInt(nonceArr[1]);
		} catch (NumberFormatException e) {
			nonce = -1;
			System.out.println("formato del nonce no compatible");
			return;
		}
		
		// hay que estandarizar los nombres de las operaciones para el archivo
		String [] operationArr =  operationStr.split(": ");
		if(operationArr.length != 2 || !operationArr[0].equals("operation") || (!operationArr[1].startsWith("add") 
				&& !operationArr[1].startsWith("remove")))
			throw new InvalidFileFormatException("formato de los datos no compatible");
		operation = operationArr[1];

		//el arbol debe estar como una list en formato BFS en el archivo
		String[] treeData = treeBFS.split(": ");
		if(treeData.length != 2 || !treeData[0].equals("tree"))
			throw new InvalidFileFormatException("formato del arbol no compatible");
		String[] nodes = treeData[1].split(", ");
		AVLTree tree = new AVLTree();
		for(int i = 0; i< nodes.length; i++){
			
			try {
				tree.insert(Integer.parseInt(nodes[i]));
			} catch (NumberFormatException e) {
				System.out.println("formato de nodo invalido");
				return;
			}
		}

		String[] modifiedNodesArr = modifiedNodesStr.split(": ");
		if(modifiedNodesArr.length != 2 || !modifiedNodesArr[0].equals("modified"))
			throw  new InvalidFileFormatException("formato de nodos modificados invalido");
		String[] modifiedNodes = modifiedNodesArr[1].split(", ");
		int[] modified = new int[modifiedNodes.length];
		for(int i = 0;i<modifiedNodes.length; i++){
			
			try {
				modified[i] = Integer.parseInt(modifiedNodes[i]);
			} catch (NumberFormatException e) {
				System.out.println("formato de nodos modificados invalido");
				return;
			}
		}

		String [] prevHashArr =  prevHashStr.split(": ");
		if(prevHashArr.length != 2 || !prevHashArr[0].equals("prevHash"))
			throw new InvalidFileFormatException("formato del prevHash no compatible");
		
		if(!SHA256.isHex(prevHashArr[1]))
			throw new InvalidFileFormatException("prevHash escrito no es hexadecimal");
		
		String prevHash = prevHashArr[1];
		
		System.out.println("index: " + index);
		System.out.println("nonce " + nonce);
		System.out.println("operation " + operation);
		System.out.println("tree ");
		tree.print();		
		System.out.println("prevHash " + prevHash);

		blockchain.modify(index, nonce, operation, tree, prevHash);
	}
}
