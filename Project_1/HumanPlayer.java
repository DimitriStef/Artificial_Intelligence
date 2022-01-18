
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HumanPlayer {

    ArrayList<int[]> list;
    int player;
    public  Scanner r;
    Move move;
    int[] input;

    public HumanPlayer(int player) {
        this.player = player;
    }

    //check arrayList
    public  boolean containsElements(ArrayList<int[]> list, int[] array) {
        for (int x = 0; x < list.size(); x++) {
            if ((list.get(x)[0] == array[0]) && (list.get(x)[1] == array[1])) {
                return true;
            }
        }
        return false;
    }
    
    
    // print legal moves
    public void printLegalMoves(ArrayList<int[]> list) {
    	System.out.println("---Legal Moves---\n");
    	if(!list.isEmpty()) {
	        for (int[] item : list) {
	            if (item != null) {
	                System.out.println("    "+Arrays.toString(item));
	            }
	        }
    	}else {
    		System.out.println("No legal Moves");
    	}
    }

    public Move getHumanMove(State board) {

        move = new Move();
        //store x,y position
        input = new int[2];
        r = new Scanner(System.in);

        //get the legal moves of the initial state
        ArrayList<int[]>  list = board.getLegalMoves(player);
        //print legal moves
        printLegalMoves(list);
                
        while (true) {

        	System.out.print("\nEnter ROW position: ");			
            input[0] = r.nextInt();
			System.out.print("["+input[0]+", ]");
            System.out.print("\nEnter COLUMN position: ");
            input[1] = r.nextInt();
			System.out.print("***Human move: ["+input[0]+", "+input[1]+"]***\n\n");
			
            if (containsElements(list, input)) {
                //System.out.println("Legal move! Good job.\n");            
                move.setMove(input);
                return move;
                
            } else {
                System.out.println("ILLEGAL move! Try again...\n");
                
            }
        }       

    }
}




























