
public class ExactCover {
	public static void main(String[] args) {
		Node rn = RootNode.getInstance();
		Node c1 = new ColumnNode(0, "asdf");
		Node c2 = new ColumnNode(0, "jkl");
		rn.setLeft(c1);
		rn.setRight(c2);
		
		System.out.println(rn.getUp());
		System.out.println(rn.getLeft());
	}
}
