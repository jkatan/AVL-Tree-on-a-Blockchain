
public class Blockchain {
	private Block last;			//Ultimo bloque en la blockchain
	private int currentIndex;	//Indice del ultimo bloque agregado en la blockchain
	
	private static class Block {
		Integer index;
		Integer nonce;
		Integer hash;
		BlockData data;
		Integer previousHash;
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
			
			if(previousBlock==null)
				previousHash = 0;
			else
				previousHash = previousBlock.hash;
			
			//Crear un metodo "Mine" que busque un nonce que de un hash valido, y llamarlo desde este
			//constructor para setear las variables nonce y hash del bloque.
		}
	}
	
	public Blockchain() {
		currentIndex = 0;
		last = null;
	}
	
	public void addBlock(String operation, AVLTree currentTree) {
		 
		currentIndex++;
		Block block = new Block(currentIndex, operation, currentTree, last);
		last = block;
	}
}
