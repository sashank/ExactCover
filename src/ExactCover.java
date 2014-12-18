import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class ExactCover extends DLXSolver {
	private final Set<String> initial;
    private final int size;   // for 9 X 9 grid, size is 3 (box size)
    private final int constraints;
	
	public ExactCover(Set<String> initial, int size, int constraints) {
		this.initial = initial;
        this.size = size;
        this.constraints = constraints;
        Map<String, Node> rowtag2node = init();
		// account for initial entries by covering all columns whose constraints they satisfy
		for(String s : initial) {
			Node n = rowtag2node.get(s);
			coverColumn(n.getColumnHeader());
			for(Node r = n.right; r != n; r = r.right) {
				coverColumn(r.getColumnHeader());
			}
		}
	}

    private Map<String, Node> init() {
        int grid = size * size; // for 9 X 9 grid, size is 3 (box size)
        int rows = grid * grid * grid;
        int cols = grid * grid * constraints;

        Node[][] nodeSmatrix = new Node[rows + 1][cols];  // we need additional row for header
        Map<String, Node> rowtag2node = new HashMap<String, Node>();

        // initialize header nodes in nodeSmatrix
        for (int c = 0; c < nodeSmatrix[0].length; ++c) {
            nodeSmatrix[0][c] = new Node("C" + c, 4, null);
        }

        int ec_cnt = 0, box_no = 1, box_r = 0, box_c =0;
        ArrayList<String> exactcover_tagList = new ArrayList<String>(rows);
        // Outer three loops are Rows of the Exact Cover Matrix
        for(int su_row = 0 ; su_row < grid; su_row ++){
           for(int su_col = 0 ; su_col < grid; su_col ++) {
                for (int su_val = 0; su_val < grid ; su_val++){


                    Boolean[] array = new Boolean[cols];
                    Arrays.fill(array, Boolean.FALSE);
                    int constraint_indx = 0;

                    // Cell Constraint 0 - 80 cols,
                    array[constraint_indx + ((su_row * grid ) + su_col)] = Boolean.TRUE;

                    // Row Constraint 81 - 161 Cols
                    constraint_indx = constraint_indx + (grid * grid) -1;
                    array[constraint_indx + ((su_row * grid ) + su_val)] = Boolean.TRUE;

                    // Col Constraint 162- 243 Cols
                    constraint_indx = constraint_indx + (grid * grid) -1;
                    array[constraint_indx + ((su_col * grid ) + su_val)] = Boolean.TRUE;

                    //  Box Constraint 244 - 323 Cols
                    constraint_indx = constraint_indx + (grid * grid) -1;

                    if( (su_row >=0 && su_row < 3) && (su_col >=0 && su_col < 3 ))
                        box_no = 0;
                    if( (su_row >=0 && su_row < 3) && (su_col >=4 && su_col < 6 ))
                        box_no = 1;
                    if( (su_row >=0 && su_row < 3) && (su_col >=7 && su_col < 9 ))
                        box_no = 2;
                    if( (su_row >=4 && su_row < 6) && (su_col >=0 && su_col < 3 ))
                        box_no = 3;
                    if( (su_row >=4 && su_row < 6) && (su_col >=4 && su_col < 6 ))
                        box_no = 4;
                    if( (su_row >=4 && su_row < 6) && (su_col >=7 && su_col < 9 ))
                        box_no = 5;
                    if( (su_row >=7 && su_row < 9) && (su_col >=0 && su_col < 3 ))
                        box_no = 6;
                    if( (su_row >=7 && su_row < 9) && (su_col >=4 && su_col < 6 ))
                        box_no = 7;
                    if( (su_row >=7 && su_row < 9) && (su_col >=7 && su_col < 9 ))
                        box_no = 8;

                    array[constraint_indx + (box_no * grid) + su_val] = Boolean.TRUE;

                    System.out.print("r"+(su_row+1)+"c"+(su_col+1)+"#"+(su_val+1) +" : ");
                    for(int j=0 ; j <cols; j++) {
                      if(array[j] == Boolean.TRUE)
                          System.out.print(1);
                      else
                          System.out.print(0);
                    }

                    System.out.println();
                    ec_cnt++;
                }
             }
         }

        Scanner sc = null;
        try {
            sc = new Scanner(new File("sudoku_matrix"));
            // initialize data nodes in nodeSmatrix from file
            int row = 1;
            while(sc.hasNext()) {
                String[] line = sc.nextLine().split(" ");
                if(line[0].startsWith("#"))
                    continue;

                String tag = line[0];
                String bits = line[1];

                int col = 0;
                for(char c : bits.toCharArray()) {
                    if(c == '1') {
                        nodeSmatrix[row][col] = new Node(tag, -1, nodeSmatrix[0][col]);
                    // store first node in each row so we can remove the initial positions later
                        if(rowtag2node.get(tag) == null)
                            rowtag2node.put(tag, nodeSmatrix[row][col]);
                    }
                    ++col;
                }
                ++row;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // initialize root
        Node root = new Node("root", -1, null);

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
        nodeSmatrix[0][nodeSmatrix[0].length-1].right = root;

        this.root = root;
        return rowtag2node;
    }

    protected void printSolution(int k) {
		int[][] solution = new int[9][9];
		System.out.println("Solution:: ");
		for(int i = 0; i < k; i++) {
			String name = path.get(i).getName();
			// names are in r_c_#_ format
			solution[name.charAt(1)-'0'-1][name.charAt(3)-'0'-1] = name.charAt(5)-'0';
		}
		for(String s : initial)
			solution[s.charAt(1)-'0'-1][s.charAt(3)-'0'-1] = s.charAt(5)-'0';
		
		for(int r = 0; r < solution.length; ++r) {
			System.out.print("|");
			for(int c = 0; c < solution[r].length; ++c) {
				System.out.print(solution[r][c] + "|");
			}
			System.out.println();
		}
	}
}
