import java.util.HashMap;
import java.util.Map;


public class DLX {
	private final Node root;
	private Map<Integer, Node>  path;
	
	public DLX(Node root) {
		this.root = root;
		path = new HashMap<Integer, Node>();
	}
	
	public void findSolution() {
		search(0);
	}
	
	private void search(int k) {
		if(root.right == root) {
			printSolution(k);
			return;
		}
		
		Node c = chooseColumn();
		coverColumn(c);
		Node d = c.down;
		while(d != c) {
			path.put(k, d);
			Node r = d.right;
			while(r != d) {
				coverColumn(r);
				r = r.right;
			}
			search(k+1);
			d = path.get(k);
			c = d.getColumnHeader();
			Node l = d.left;
			while(l != d) {
				uncoverColumn(l);
				l = l.left;
			}
			d = d.down;
		}
		uncoverColumn(c);
	}

	private void coverColumn(Node n) {
		n.right.left = n.left;
		n.left.right = n.right;

		Node d = n.down;
		while(d != n) {
			Node r = d.right;
			while(r != d) {
				r.up.down = r.down;
				r.down.up = r.up;
				r.getColumnHeader().size--;
				r = r.right;
			}
			d = d.down;
		}
	}
	
	private void uncoverColumn(Node n) {
		Node u = n.up;
		
		while(u != n) {
			Node l = u.left;
			while(l != u) {
				l.getColumnHeader().size++;
				l.up.down = l;
				l.down.up = l;
			}
		}
		n.left.right = n;
		n.right.left = n;
	}

	private Node chooseColumn() {
		int currSize   = Integer.MAX_VALUE;
		Node ret = null;

		Node currNode = root.right;
		while(root != currNode) {
			if(currNode.size < currSize) 
				ret = currNode;
			currNode = currNode.right;
		}

		return ret;
	}

	private void printSolution(int k) {
		System.out.println("Solution:: ");
		for(int i = 0; i < k; i++)
			System.out.println(path.get(k));
	}
}
