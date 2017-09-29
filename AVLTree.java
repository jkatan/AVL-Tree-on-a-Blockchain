import java.util.LinkedList;
import java.util.Queue;

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
	
	public void insert(Integer num) {
		root = insert(num, root);
	}
	
	private AVLNode insert(Integer num, AVLNode node) {
		if (node == null)
			node = new AVLNode(num);
		else if (num < node.num) {
			node.left = insert(num, node.left);
			if (balanceFactor(node) == 2) {
				if (num < node.left.num)
					node = rightRotation(node);
				else
					node = leftRightRotation(node);
			}
		}
		else if (num > node.num) {
			node.right = insert(num, node.right);
			if (balanceFactor(node) == -2) {
				if (num > node.right.num)
					node = leftRotation(node);
				else
					node = rightLeftRotation(node);
			}
		}
		
		node.height = Math.max(height(node.left), height(node.right)) + 1;
		return node;
	}
	
	public void remove(Integer num) {
		root = remove(num, root);
	}
	
	private AVLNode remove(Integer num, AVLNode node) {
		if (num < node.num) {
			node.left = remove(num, node.left);
		}
		else if (num > node.num) {
			node.right = remove(num, node.right);
		}
		else if (num == node.num) {
			if (node.right != null) {
				AVLNode aux = node.right;
				while (aux.left != null) {
					aux = aux.left;
				}
				
				node.num = aux.num;
				node.right = remove(aux.num, node.right);
			}
			else if (node.left != null) {
				AVLNode aux = node.left;
				while (aux.right != null) {
					aux = aux.right;
				}
				
				node.num = aux.num;
				node.left = remove(aux.num, node.left);
			}
			else
				node = null;
		}
		
		if (node == null)
			return node;
		
		node.height = Math.max(height(node.left), height(node.right)) + 1;
		
		if (balanceFactor(node) > 1) {
			if (balanceFactor(node.left) >= 0)
				node = rightRotation(node);
			else
				node = leftRightRotation(node);
		}
		else if (balanceFactor(node) < -1) {
			if (balanceFactor(node.right) <= 0)
				node = leftRotation(node);
			else
				node = rightLeftRotation(node);
		}

		return node;
	}
	
	private static int height(AVLNode node) {
		  return node == null ? -1 : node.height;
	}
	
	private static int balanceFactor(AVLNode node) {
		return height(node.left) - height(node.right);
	}

	private static AVLNode rightRotation(AVLNode node) {
		AVLNode aux = node.left;
        node.left = aux.right;
        aux.right = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        aux.height = Math.max(height(aux.left), node.height) + 1;
        return aux;
	}
	
	private static AVLNode leftRotation(AVLNode node) {
		AVLNode aux = node.right;
        node.right = aux.left;
        aux.left = node;
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        aux.height = Math.max(height(aux.right), node.height) + 1;
        return aux;
	}
	
	private static AVLNode leftRightRotation(AVLNode node) {
		node.left = leftRotation(node.left);
        return rightRotation(node);
	}
	
	private static AVLNode rightLeftRotation(AVLNode node) {
		node.right = rightRotation(node.right);
        return leftRotation(node);
	}
	
	public void print() {
		Queue<AVLNode> queue = new LinkedList<AVLNode>();
		LinkedList<AVLNode> list = new LinkedList<AVLNode>();
		
		queue.offer(root);
		while(list.size() < 31) {
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
		System.out.println("]\n");
	}
}
