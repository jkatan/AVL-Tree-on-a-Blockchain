import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class AVLTree {
	private AVLNode root;
	
	private class AVLNode {
		AVLNode left;
		AVLNode right;
		Integer num;
		Integer height;
		
		AVLNode(Integer num) {
			this.left = null;
			this.right = null;
			this.num = num;
			this.height = 0;
		}
	}
	
	public AVLTree() {};
	
	public AVLTree(AVLTree tree) {
		root = new AVLNode(tree.root.num);
		buildFromTree(tree.root);
	}
	
	private void buildFromTree(AVLNode original) {
		
		if(original.left==null && original.right==null)
			return;
		
		if(original.right!=null && original.left==null) {
			this.insert(original.right.num);
			buildFromTree(original.right);
			return;
		}
		
		if(original.left!=null && original.right==null) {
			this.insert(original.left.num);
			buildFromTree(original.left);
			return;
		}
		
		this.insert(original.left.num);
		this.insert(original.right.num);
		buildFromTree(original.left);
		buildFromTree(original.right);
	}
	
	public Set<Integer> insert(Integer num) {
		Set<Integer> setModifiedNodes = new HashSet<>();
		root = insert(num, root, setModifiedNodes);
		return setModifiedNodes;
	}
	
	private AVLNode insert(Integer num, AVLNode node, Set<Integer> set) {
		if (node == null) {
			node = new AVLNode(num);
			set.add(node.num);
		}
		else if (num < node.num) {
			node.left = insert(num, node.left, set);
			if (balanceFactor(node) == 2) {
				if (num < node.left.num)
					node = rightRotation(node, set);
				else
					node = leftRightRotation(node, set);
			}
		}
		else if (num > node.num) {
			node.right = insert(num, node.right, set);
			if (balanceFactor(node) == -2) {
				if (num > node.right.num)
					node = leftRotation(node, set);
				else
					node = rightLeftRotation(node, set);
			}
		}
		
		node.height = Math.max(height(node.left), height(node.right)) + 1;
		return node;
	}
	
	public Set<Integer> remove(Integer num) {
		Set<Integer> setModifiedNodes = new HashSet<>();
		root = remove(num, root, setModifiedNodes);
		return setModifiedNodes;
	}
	
	private AVLNode remove(Integer num, AVLNode node, Set<Integer> set) {
		if (node == null)
			return node;
		
		if (num < node.num) {
			node.left = remove(num, node.left, set);
		}
		else if (num > node.num) {
			node.right = remove(num, node.right, set);
		}
		else if (num == node.num) {
			if (node.right != null) {
				AVLNode aux = node.right;
				while (aux.left != null) {
					aux = aux.left;
				}
				
				node.num = aux.num;
				node.right = remove(aux.num, node.right, set);
			}
			else if (node.left != null) {
				AVLNode aux = node.left;
				while (aux.right != null) {
					aux = aux.right;
				}
				
				node.num = aux.num;
				node.left = remove(aux.num, node.left, set);
			}
			else {
				set.add(node.num);
				node = null;
			}
		}
		
		if (node == null)
			return node;
		
		node.height = Math.max(height(node.left), height(node.right)) + 1;
		
		if (balanceFactor(node) > 1) {
			if (balanceFactor(node.left) >= 0)
				node = rightRotation(node, set);
			else
				node = leftRightRotation(node, set);
		}
		else if (balanceFactor(node) < -1) {
			if (balanceFactor(node.right) <= 0)
				node = leftRotation(node, set);
			else
				node = rightLeftRotation(node, set);
		}

		return node;
	}

	public boolean contains(Integer num){//Desarrollar busqueda en arbol binario.
		return containsRec(num, root);
	}

	private boolean containsRec(Integer num, AVLNode current){
		if (current == null){
			return false; // The node was not found

		} else if (num.compareTo(current.num) < 0){
			return containsRec(num, current.left);
		} else if (num.compareTo(current.num) > 0){
			return containsRec(num, current.right);
		}
		return true;
	}
	
	private static int height(AVLNode node) {
		  return node == null ? -1 : node.height;
	}
	
	private static int balanceFactor(AVLNode node) {
		return height(node.left) - height(node.right);
	}

	private static AVLNode rightRotation(AVLNode node, Set<Integer> set) {
		AVLNode aux = node.left;
        node.left = aux.right;
        aux.right = node;
        set.add(aux.num);
        set.add(node.num);
        if (node.left != null)
        	set.add(node.left.num);
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        aux.height = Math.max(height(aux.left), node.height) + 1;
        return aux;
	}
	
	private static AVLNode leftRotation(AVLNode node, Set<Integer> set) {
		AVLNode aux = node.right;
        node.right = aux.left;
        aux.left = node;
        set.add(aux.num);
        set.add(node.num);
        if (node.right != null)
        	set.add(node.right.num);
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        aux.height = Math.max(height(aux.right), node.height) + 1;
        return aux;
	}
	
	private static AVLNode leftRightRotation(AVLNode node, Set<Integer> set) {
		node.left = leftRotation(node.left, set);
        return rightRotation(node, set);
	}
	
	private static AVLNode rightLeftRotation(AVLNode node, Set<Integer> set) {
		node.right = rightRotation(node.right, set);
        return leftRotation(node, set);
	}
	
	public void print() {
		Queue<AVLNode> queue = new LinkedList<AVLNode>();
		LinkedList<AVLNode> list = new LinkedList<AVLNode>();
		
		queue.offer(root);
		while(list.size() < (Math.pow(2, height(root)+1)-1)) {
			AVLNode aux = queue.poll();
			if(aux != null) {
				queue.offer(aux.left);
				queue.offer(aux.right);
				list.add(aux);
			}
			else {
				queue.offer(null);
				queue.offer(null);
				list.add(null);
			}
		}
		
		int index = 0;
		int power = 1;
		for (AVLNode node : list) {
			if (index+1 == power) {
				if (index != 0)
					System.out.println("]");
				System.out.print("[");
				power *= 2;
			}
			else {
				if (index % 2 == 1)
					System.out.print("] [");
				else
					System.out.print(",");
			}
				
			if (node != null)
				System.out.print(node.num + "(" + balanceFactor(node) + ")");
			else
				System.out.print("X");
			index++;
		}
		
		if (root != null)
			System.out.println("]\n");
	}
	
	//El String que devuelve es un recorrido BFS del arbol.
	public String toString() {
		
		StringBuilder tree = new StringBuilder();
		
		Queue<AVLNode> queue = new LinkedList<>();
		queue.add(root);
		
		while(!queue.isEmpty()) {
			AVLNode node = queue.poll();
			tree.append(node.num);

			if(node.left!=null)
				queue.add(node.left);
			
			if(node.right!=null)
				queue.add(node.right);
		}
		
		return tree.toString();
	}
}
