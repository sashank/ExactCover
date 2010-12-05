import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;


public class SudokuSolver extends DLXSolver {
	public SudokuSolver(Set<String> initial) {
		Node[][] nodeSmatrix = new Node[730][324];
		
		Scanner sc = null;
		try {
			sc = new Scanner(new File("sudoku_matrix"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// initialize root
		Node root = new Node("root", -1, null);
		
		// initialize header nodes in nodeSmatrix
		for(int c = 0; c < nodeSmatrix[0].length; ++c) {
			nodeSmatrix[0][c] = new Node("C" + c, 4, null);
		}
		
		// initialize data nodes in nodeSmatrix from file
		int row = 1;
		while(sc.hasNext()) {
			String line = sc.nextLine();
			if(line.startsWith("#"))
				continue;
			
			int col = 0;
			for(char c : line.toCharArray()) {
				if(c == '1') {
					nodeSmatrix[row][col] = new Node("$" + row + "_" + col, -1, nodeSmatrix[0][col]);
				}
				++col;
			}
			++row;
		}
		
		// set left, right, up, down links
		for(int r = 0; r < nodeSmatrix.length; ++r) {
			for(int c = 0; c < nodeSmatrix[r].length; ++c) {
				if(nodeSmatrix[r][c] == null) continue;
				
				//System.out.println("Considering: " + nodeSmatrix[r][c]);
				int tempr, tempc;
				
				//set left
				tempc = c;
				do {
					--tempc;
					if(tempc < 0) tempc = nodeSmatrix[r].length-1;
				} while(nodeSmatrix[r][tempc] == null);
				nodeSmatrix[r][c].left = nodeSmatrix[r][tempc];
				
				// set right
				tempc = c;
				do {
					tempc = (tempc + 1) % nodeSmatrix[r].length;
				} while(nodeSmatrix[r][tempc] == null);
				nodeSmatrix[r][c].right = nodeSmatrix[r][tempc];
				
				// set up
				tempr = r;
				do {
					tempr = (tempr + 1) % nodeSmatrix.length;
				} while(nodeSmatrix[tempr][c] == null);
				nodeSmatrix[r][c].up = nodeSmatrix[tempr][c];
				
				// set down
				tempr = r;
				do {
					--tempr;
					if(tempr < 0) tempr = nodeSmatrix.length-1;
				} while(nodeSmatrix[tempr][c] == null);
				nodeSmatrix[r][c].down = nodeSmatrix[tempr][c];
			}
		}
		
		// tie root into the node network
		root.right = nodeSmatrix[0][0];
		root.left  = nodeSmatrix[0][nodeSmatrix[0].length-1];
		nodeSmatrix[0][0].left = root;
		nodeSmatrix[0][nodeSmatrix.length-1].right = root;
		
		//TODO: process initial entries; call uncover on them
		
		this.root = root;
	}

}
