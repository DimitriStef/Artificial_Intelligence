
import java.util.Scanner;

public class Main {

    public static Scanner reader;

    public static void main(String[] args) {
        int humanColour = 0;
        int aiColour = 0;
        int getlastPlayer;
        State state = new State();
        reader = new Scanner(System.in);

        System.out.println("Welcome to Reversi! Please pick your colour. Black goes first.");
        // Player picks colour    
        boolean correctInput;
        do {
            String userInput = reader.next();
            switch (userInput.toLowerCase()) {
                case "black":
                    humanColour = 1;
                    aiColour = -1;
                    correctInput = true;
                    break;
                case "white":
                    humanColour = -1;
                    aiColour = 1;
                    correctInput = true;
                    break;
                default:
                    System.out.println("Incorrect input. Please retype your prefered colour.");
                    correctInput = false;
                    break;
            }
        } while (!correctInput);

        // Player picks "difficulty level", which will be the max depth of the Minimax algorithm
        int maxDepth;
        System.out.println("Enter difficulty level, from easier (1) to hardest (10). Invalid numbers will not be accepted.");
        do {
            maxDepth = reader.nextInt();
        } while (maxDepth < 0 || maxDepth > 10);

        //We create the players and the board
        AIplayer aiPlayer = new AIplayer(maxDepth, aiColour);
        HumanPlayer humanPlayer = new HumanPlayer(humanColour);

        state.printBoard();

        //While the game has not finished		
        while (!state.isTerminal()) {

            System.out.println();
            getlastPlayer = state.getlastPlayer();

            //Human player 
            if (getlastPlayer == aiColour) {
                if (!state.getLegalMoves(humanColour).isEmpty()) {
                    System.out.println("Human's turn....\n");
                    Move humanMove = humanPlayer.getHumanMove(state);                   
                    state.makeMove(humanMove.getMove(), humanColour);                    
                    
                } else {
                    System.out.println("No Legal Move For Human!");
                    getlastPlayer = humanColour;
                }
                //AI player
            }if (getlastPlayer == humanColour) {
                if (!state.getLegalMoves(aiColour).isEmpty()) {
                    System.out.println("AI's turn....");
                    Move aiMove = aiPlayer.MiniMax(state);
					System.out.println("***AI move: "+aiMove.toString()+"***\n");
                    state.makeMove(aiMove.getMove(), aiColour);
                   
                } else {
                    System.out.println("No Legal Move For AI!");
                    state.setlastPlayer(aiColour);
                }
            }
			state.printBoard();
        }

        if (state.isTerminal()) {
            int black = state.countScore()[0];
            int white = state.countScore()[1];
            System.out.println("###GAME OVER###");
            System.out.println("Black :" + black + "  White :" + white);
            if (black > white) {
                System.out.println("Black WINS!");
            } else if (black < white) {
                System.out.println("White WINS!");
            } else {
                System.out.println("Draw!");
            }
        }

    }

}
