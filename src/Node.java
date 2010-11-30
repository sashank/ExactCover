public abstract class Node {
	private Node left, right, up, down;
	
	public Node getLeft()  { return left;  }
	public Node getRight() { return right; }
	public Node getUp()    { return up;    }
	public Node getDown()  { return down;  }
	
	public void setLeft(Node n)  { left = n;  }
	public void setRight(Node n) { right = n; }
	public void setUp(Node n)    { up = n;    }
	public void setDown(Node n)  { down = n;  }
}
