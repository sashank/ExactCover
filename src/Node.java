
public class Node {
	public int size;
	private final String name;
	private Node columnHeader;
	public Node left, right, up, down;
	
	private static final Node root = new Node("Root", -1, null);
	
	public Node(String name, int size, Node columnHeader) {
		this.size = size;
		this.name = name;
		this.columnHeader = columnHeader;
		
		left  = this;
		right = this;
		up    = this;
		down  = this;
	}
	
	public static Node getRoot() {
		return root;
	}
	
	public String getName()  { return name; }
	public Node getColumnHeader() { return columnHeader; }
	public String toString() { return name + ":" + size; }
}