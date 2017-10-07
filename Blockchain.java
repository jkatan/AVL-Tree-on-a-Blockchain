import sun.security.provider.SHA;

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
			
			BlockData(String operation, AVLTree currentState) {
				this.operation = operation;
				this.currentState = currentState;
			}
		}
		
		Block(Integer index, String operation, AVLTree currentState, Block previousBlock) {
			this.index = index;
			this.previousBlock = previousBlock;
			this.data = new BlockData(operation, currentState);		
			
		}
		
		//Representacion en String del bloque para ser hasheado por SHA256
		public String blockToHash() {
			StringBuilder block = new StringBuilder();
			block.append(index);
			block.append(data.operation);
			block.append(data.currentState.toString());
			if(previousBlock!=null) {
				previousHash = previousBlock.hash;
				block.append(SHA256.bytesToHex(previousHash));	
			}
			
			return block.toString();
		}
	}
		
	public Blockchain(int initialCeros) {
		currentIndex = 0;
		last = null;
		this.initialCeros = initialCeros;
	}
	
	public void addBlock(String operation, AVLTree currentTree) {
		 
		currentIndex++;
		Block block = new Block(currentIndex, operation, currentTree, last);
		mine(block);
		last = block;
	}
	
	private void mine(Block block) {
		block.nonce = 1;
		String toHash = block.blockToHash() + block.nonce.toString();
		block.hash = SHA256.hash(toHash);
		
		while(!validCeros(block.hash)) {
			block.nonce++;
			toHash = block.blockToHash() + block.nonce.toString();
			block.hash = SHA256.hash(toHash);
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
}
