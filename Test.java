
public class Test {
	public static void main(String[] args) {
		AVLTree tree = new AVLTree();
		tree.insert(8);
		tree.insert(10);
		tree.insert(15);
		tree.insert(20);
		tree.insert(25);
		tree.insert(22);
		tree.insert(17);
		tree.insert(24);
		tree.insert(12);
		tree.insert(29);
		tree.insert(39);
		tree.insert(3);
		tree.insert(9);
		tree.insert(5);
		tree.insert(11);
		tree.insert(16);
		tree.print();
		tree.remove(25);
		tree.print();
		tree.remove(39);
		tree.print();
	}
}
