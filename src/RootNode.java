
public class RootNode extends Node {
	private static final RootNode rn = new RootNode();
	
	private RootNode() {
		this.setUp(this);
		this.setDown(this);
	}
	
	public static RootNode getInstance() {
		return rn;
	}
	
	public String toString() { return "Root Node"; }
}