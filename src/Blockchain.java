
public class Blockchain {
	private Block last;			//Ultimo bloque en la blockchain
	private int currentIndex;	//Indice del ultimo bloque agregado en la blockchain
	private int initialCeros;	//Cantidad de ceros iniciales para validar el hash
	private ArrayList<Block> blockArray; //Este array hace que la blockchain sea mas sencilla de recorrer, facilitando
	//los algoritmos de modify, validate y lookup. No pierde seguridad ya que la blockchain en si no es segura al tener
	//un arbol por bloque, ni teniendo un arbol auxiliar.
	//Comentario extra: si no pidiera retornar nodos modificados, la blockchain podria hacerse mucho mas segura armando
	//el arbol unicamente cuando se lo pide, es decir guardando instrucciones (partes) del mismo en cada bloque.

	private static class Block {
		Integer index;
		Integer nonce;
		byte[] hash;
		BlockData data;
		byte[] previousHash;
		Block previousBlock;		
		
		private static class BlockData {
			String operation;		//Operacion realizada sobre el arbol AVL.
			AVLTree currentState;	//El arbol AVL al momento de haber realizado la operacion.
			ArrayList<Integer> modifiedValues; //Los bloques que fueron modificados en la ultima operacion.
			
			BlockData(String operation, AVLTree currentState, ArrayList<Integer> modValues) {
				this.operation = operation;
				this.currentState = currentState;
				this.modifiedBlocks = modBlocks;
			}
			
			private void print() {
				System.out.println("Operation: " + operation);
				System.out.println("Tree: ");
				currentState.print();
			}
		}
		
		Block(Integer index, String operation, AVLTree currentState, ArrayList<Integer> modValues, Block previousBlock) {
			this.index = index;
			this.previousBlock = previousBlock;
			this.data = new BlockData(operation, currentState, modValues);
			if(previousBlock!=null)
				previousHash = previousBlock.hash;
		}
		
		//Representacion en String del bloque para ser hasheado por HashUtilities
		public String blockToHash() {
			StringBuilder block = new StringBuilder();
			block.append(index);
			block.append(data.operation);
			block.append(data.currentState.toString());
			if(previousBlock!=null)
				block.append(HashUtilities.bytesToHex(previousHash));	
			
			return block.toString();
		}
		
		private void print() {
			System.out.println("Index: " + index);
			System.out.println("Nonce: " + nonce);
			System.out.println("Hash: " + HashUtilities.bytesToHex(hash));
			if(previousHash!=null)
				System.out.println("Previous hash: " + HashUtilities.bytesToHex(previousHash));
			data.print();
		}
	}
		
	public Blockchain(int initialCeros) {
		currentIndex = 0;
		last = null;
		blockArray = new ArrayList<Block>();
		this.initialCeros = initialCeros;
	}
	
	public boolean addBlock(String operation, AVLTree currentTree, ArrayList<Integer> modValues) {
		 
		if(validate()) {
			currentIndex++;
			Block block = new Block(currentIndex, operation, currentTree, modValues, last);
			mine(block);
			last = block;
			blockArray.insert(block);
			return true;
		}
		
		return false;
	}

	public ArrayList<Integer> lookup(Integer num){
		ArrayList<Integer> blockIndexes = new ArrayList<Integer>();
		for(Block b: blockArray){
			if(b.blockData.modifiedBlocks.contains(num))
				blockIndexes.insert(b.index);
		}
		if(blockIndexes.isEmpty())
			return null;
		return blockIndexes;
	}
	
	private void mine(Block block) {
		block.nonce = 1;
		String toHash = block.blockToHash() + block.nonce.toString();
		block.hash = HashUtilities.hash(toHash);
		
		while(!validCeros(block.hash)) {
			block.nonce++;
			toHash = block.blockToHash() + block.nonce.toString();
			block.hash = HashUtilities.hash(toHash);
		}
	}
	
	private boolean validCeros(byte[] hash) {
		int i=0;
		while(i<initialCeros/2) {
			if(hash[i]!=0)
				return false;
			i++;
		}
		
		if(initialCeros%2==0)
			return true;
		
		int shifted = hash[i] >> 4; //Tomo los 4 bits mas significativos
		return shifted==0?true:false;
	}
	
	public boolean validate() {
		
		Block current = last;
		while(current!=null) {
			if(current.previousBlock!=null)
				if(!HashUtilities.compareHashes(current.previousHash, current.previousBlock.hash))
					return false;
			
			current = current.previousBlock;
		}
		
		return true;
	}
	
	public void print() {
		
		Block current = last;
		while(current!=null) {
			current.print();
			current = current.previousBlock;
		}
	}
	
	public void printCurrentBlock() {
		last.print();
	}
	
	//Devuelve falso si no existe el bloque con el indice indicado, verdadero en caso contrario
	//Esta funcion se puede hacer mas sencilla recorriendo por el blockArray
	public boolean modify(int index, int nonce, String operation, AVLTree tree, String prevHash) {
		
		Block current = last;
		while(current!=null) {
			if(current.index==index) {
				current.nonce = nonce;
				current.data.operation = operation;
				current.data.currentState = tree;
				current.previousHash = HashUtilities.hexToByte(prevHash);
				current.hash = HashUtilities.hash(current.blockToHash());	//Actualizo el hash del bloque luego de modificarlo
				return true;
			}
			
			current = current.previousBlock;
		}
		
		return false;
	}
}
