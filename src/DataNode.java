
public class DataNode extends Node {
	private ColumnNode columnHeader;
	
	public DataNode(ColumnNode cn) {
		columnHeader = cn;
	}
	
	public ColumnNode getColumnHeader() { return columnHeader; }
}
