import java.util.HashSet;
import java.util.Set;

public class Blockchain {
	private Block last;			//Ultimo bloque en la blockchain
	private int currentIndex;	//Indice del ultimo bloque agregado en la blockchain
	private int initialCeros;	//Cantidad de ceros iniciales para validar el hash

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
			Set<Integer> modifiedValues; //Los bloques que fueron modificados en la ultima operacion.
			
			BlockData(String operation, AVLTree currentState, Set<Integer> modValues) {
				this.operation = operation;
				this.currentState = currentState;
				this.modifiedValues = modValues;
			}
			
			private void print() {
				System.out.println("Operation: " + operation);
				if(modifiedValues!=null)
					System.out.println("Modified nodes: " + modifiedValues);
				else
					System.out.println("No modified nodes");
				System.out.println("Tree: ");
				currentState.print();
			}
		}
		
		Block(Integer index, String operation, AVLTree currentState, Set<Integer> modValues, Block previousBlock) {
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
			if(data.modifiedValues!=null)
				block.append(data.modifiedValues.toString());
			if(previousBlock!=null)
				block.append(HashUtilities.bytesToHex(previousHash));	
			
			return block.toString();
		}

		public String blockTrueHash(){
			return this.blockToHash() + Integer.toString(this.nonce);
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
		this.initialCeros = initialCeros;
	}
	
	public boolean addBlock(String operation, AVLTree currentTree, Set<Integer> modValues) {
		 
		if(validate()) {
			currentIndex++;
			Block block = new Block(currentIndex, operation, currentTree, modValues, last);
			mine(block);
			last = block;
			return true;
		}
		
		return false;
	}

	public Set<Integer> lookup(Integer num){

		if(!last.data.currentState.contains(num)){ 
			addBlock("lookup " + num + " - false", last.data.currentState, null);
			return null;
		} else {
			Set<Integer> blockIndexes = new HashSet<>();
			addBlock("lookup " + num + " - true", last.data.currentState, null);
			lookupRec(blockIndexes, this.last, num);
			return blockIndexes;
		}
	}

	private void lookupRec(Set<Integer> indexRet, Block current, Integer num) {
		if(current.data.modifiedValues!=null && current.data.modifiedValues.contains(num)){
			indexRet.add(current.index);
		}
		
		if (current.previousBlock == null) 
			return;
			
		lookupRec(indexRet, current.previousBlock, num);
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
			if (current.previousBlock != null) {
				if (!HashUtilities.compareHashes(current.previousHash, HashUtilities.hash(current.previousBlock.blockTrueHash()))){
					return false;
				}
			}

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
				if(prevHash == null)
					current.previousHash = null;
				else
					current.previousHash = HashUtilities.hexToByte(prevHash);
				return true;
			}
			
			current = current.previousBlock;
		}
		
		return false;
	}
}
