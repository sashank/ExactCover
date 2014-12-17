import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;



public class SudokoSolver {

	public static void main(String[] args) {
        Set<String> initial = new HashSet<String>();
        Scanner sc = null;
        try {
            sc = new Scanner(new File("input9x9"));
            int row = 1;
            while(sc.hasNext()) {
                String[] literals = sc.nextLine().split("|");
                int col = 1;
                for(String c : literals){

                  //System.out.println(c);
                    int val;
                  try {
                      val = Integer.parseInt(c);
                      if (val != 0)
                          initial.add("r" + row + "c" + col + "#" + val);

                      col++;
                  }catch (NumberFormatException nfe){
                      // Do nothing ignore delimiters, spaces etc.
                  }
                }
                row++;
            }

        }catch (Exception e){
            System.out.println("Some thing went wrong parsing input " + e.getMessage());
            System.exit(0);
        }
		ExactCover exactCover = new ExactCover(initial,3,4);   // Sudoko has 4 constraints, cell, row, col, box
		exactCover.findSolution();
	}
}
