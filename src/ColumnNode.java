
public class ColumnNode extends Node {
	private int size;
	private String name;
	
	public ColumnNode(int s, String n) {
		size = s;
		name = n;
	}
	
	public String getName() { return name; }
	public int    getSize() { return size; }
	public String toString() { return name + ":" + size; }
}
