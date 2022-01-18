
import java.util.ArrayList;
import java.util.Random;

public class AIplayer {

    private int maxDepth;
    private int playerLetter;

   

    public AIplayer(int maxDepth, int playerLetter) {
        this.maxDepth = maxDepth;
        this.playerLetter = playerLetter;
    }

    //Initiates the MiniMax algorithm
    public Move MiniMax(State board) {
        //If the AI plays then it wants to MAXimize the heuristics value
       // if (playerLetter == player) {
            return max(new State(board), 0, playerLetter, Integer.MIN_VALUE, Integer.MAX_VALUE);
       // } 
        
        /*
        else {
            return min(new State(board), 0, -player, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }*/
        
    }

    // The max and min functions are called interchangingly, one after another until a max depth is reached
    public Move max(State board, int depth, int player, int alpha, int beta) {
        Random r = new Random();

        /* If MAX is called on a state that is terminal or after a maximum depth is reached,
	         * then a heuristic is calculated on the state and the move returned.
         */
        if ((board.isTerminal()) || (depth == maxDepth)) {
            Move lastMove = new Move(board.getLastMove().getMove(), board.evaluate(player));
            return lastMove;
        }
        //The children-moves of the state are calculated
        ArrayList<State> children = new ArrayList<State>(board.getChildren(board.getLegalMoves(player), player));

        Move maxMove = new Move(Integer.MIN_VALUE);

        for (State child : children) {
            //And for each child min is called, on a lower depth
            Move move = min(child, depth + 1, -player, alpha, beta);
            //The child-move with the greatest value is selected and returned by max
            if (move.getValue() >= maxMove.getValue()) {
                if ((move.getValue() == maxMove.getValue())) {
                    //If the heuristic has the same value then we randomly choose one of the two moves
                    if (r.nextInt(2) == 0) {

                        maxMove.setMove(child.getLastMove().getMove());
                        maxMove.setValue(move.getValue());
                    }
                } else {
                    maxMove.setMove(child.getLastMove().getMove());
                    maxMove.setValue(move.getValue());
                }
            }
			/*
            if (maxMove.getValue() >= beta) {
                return maxMove;
            }
            alpha = Math.max(alpha, move.getValue());
			*/
			alpha = Math.max(alpha, move.getValue());            
            if (alpha >= beta) {
                return maxMove;
            }
			
        }
        return maxMove;
    }

    //Min works similarly to max
    public Move min(State board, int depth, int player, int alpha, int beta) {
        Random r = new Random();

        if ((board.isTerminal()) || (depth == maxDepth)) {
            Move lastMove = new Move(board.getLastMove().getMove(), board.evaluate(player));
            return lastMove;
        }
        ArrayList<State> children = new ArrayList<State>(board.getChildren(board.getLegalMoves(player), player));
        Move minMove = new Move(Integer.MAX_VALUE);

        for (State child : children) {
            Move move = max(child, depth + 1, -player, alpha, beta);

            if (move.getValue() <= minMove.getValue()) {
                if ((move.getValue() == minMove.getValue())) {
                    if (r.nextInt(2) == 0) {
                        minMove.setMove(child.getLastMove().getMove());
                        minMove.setValue(move.getValue());
                    }
                } else {
                    minMove.setMove(child.getLastMove().getMove());
                    minMove.setValue(move.getValue());
                }
            }
			
            /* if (minMove.getValue() <= alpha) {
                return minMove;
            }
            beta = Math.min(beta, move.getValue()); */
			
			
			beta = Math.min(beta, move.getValue());
            if (beta <= alpha) {
                return minMove;
            }
			
        }
        return minMove;
    }

}
