
import java.util.ArrayList;

public class State {

    private int[][] board;
    public static final int BLACK = 1;
    public static final int WHITE = -1;
    private final int WIDTH = 8;
    private final int HEIGHT = 8;   
    private ArrayList<int[]> legalMoves;
    private int lastPlayer;
    private Move lastMove;
	private static int boardMove = 0;
    public State() {
        lastMove = new Move();
        lastPlayer = -1;

        board = new int[WIDTH][HEIGHT];
        board[4][4] = WHITE;
        board[3][4] = BLACK;
        board[3][3] = WHITE;
        board[4][3] = BLACK;

    }

    public State(int[][] board) {
        this.board = new int[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    public State(State newBoard) {
        lastMove = newBoard.lastMove;
        lastPlayer = newBoard.lastPlayer;
        board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = newBoard.board[i][j];
            }
        }
    }

    public ArrayList<State> getChildren(ArrayList<int[]> arrayList, int player) {

        ArrayList<State> children = new ArrayList<State>();
        for (int[] move : arrayList) {
            State newState = new State(this);
            newState.makeMove(move, player);
            children.add(newState);
        }
        return children;
    }

    public void makeMove(int[] move, int player) {

        lastMove = new Move(move);
        setlastPlayer(player);
        board[move[0]][move[1]] = player;

        replaceHorizontal(this, move, player);
        replaceVertical(this, move, player);
        replaceDiagonalLeft(this, move, player);
        replaceDiagonalRight(this, move, player);

    }

    public State getNewState(int[] move, int color) {
        State child = new State(this.board);
        int x = move[0];
        int y = move[1];

        child.board[x][y] = color;
        return child;
    }

    public ArrayList<int[]> getLegalMoves(int player) {
    	legalMoves = new ArrayList<int[]>();
 
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (board[x][y] == player) {
                    if (checkDownRight(x, y, player) != (null)) {
                        legalMoves.add(checkDownRight(x, y, player));
                    }
                    if (checkDownLeft(x, y, player) != (null)) {
                        legalMoves.add(checkDownLeft(x, y, player));
                    }
                    if (checkDown(x, y, player) != (null)) {
                        legalMoves.add(checkDown(x, y, player));
                    }
                    if (checkRight(x, y, player) != (null)) {
                        legalMoves.add(checkRight(x, y, player));
                    }
                    if (checkLeft(x, y, player) != (null)) {
                        legalMoves.add(checkLeft(x, y, player));
                    }
                    if (checkUpRight(x, y, player) != (null)) {
                        legalMoves.add(checkUpRight(x, y, player));
                    }
                    if (checkUpLeft(x, y, player) != (null)) {
                        legalMoves.add(checkUpLeft(x, y, player));
                    }
                    if (checkUp(x, y, player) != (null)) {
                        legalMoves.add(checkUp(x, y, player));
                    }                                    	
                }

            }
        }

        return legalMoves;
    }

    public int[] checkDownRight(int xPos, int yPos, int player) {
        int oppositePlayer = -1 * player;
        int pos[] = new int[2];
        xPos++;
        yPos++;
                
        while ((xPos < 7) && (yPos < 7) && (board[xPos][yPos] == oppositePlayer)) {
            if ((board[xPos + 1][yPos + 1] == 0) && !containsElements(legalMoves,xPos + 1,yPos + 1)) {
                pos[0] = ++xPos;
                pos[1] = ++yPos;
                
                
                return pos;
            }
            xPos++;
            yPos++;
        }
        return null;
    }

    private int[] checkDownLeft(int xPos, int yPos, int player) {
        int oppositePlayer = -1 * player;
        int pos[] = new int[2];
        xPos++;
        yPos--;

        while ((xPos < 7) && (yPos > 0) && (board[xPos][yPos] == oppositePlayer)) {
            if ((board[xPos + 1][yPos - 1] == 0) && !containsElements(legalMoves,xPos + 1,yPos - 1)) {
                pos[0] = ++xPos;
                pos[1] = --yPos;
                return pos;
            }
            xPos++;
            yPos--;

        }
        return null;
    }

    private int[] checkDown(int xPos, int yPos, int player) {
        int oppositePlayer = -1 * player;
        int pos[] = new int[2];
        xPos++;

        while ((xPos < 7) && (board[xPos][yPos] == oppositePlayer)) {
            if ((board[xPos + 1][yPos] == 0) && !containsElements(legalMoves,xPos + 1,yPos)) {
                pos[0] = ++xPos;
                pos[1] = yPos;
                return pos;
            }
            xPos++;
        }
        return null;
    }

    private int[] checkRight(int xPos, int yPos, int player) {
        int oppositePlayer = -1 * player;
        int pos[] = new int[2];
        yPos++;

        while ((yPos < 7) && (board[xPos][yPos] == oppositePlayer)) {
            if ((board[xPos][yPos + 1] == 0) && !containsElements(legalMoves,xPos,yPos + 1)) {
                pos[0] = xPos;
                pos[1] = ++yPos;
                return pos;
            }
            yPos++;
        }
        return null;
    }

    private int[] checkLeft(int xPos, int yPos, int player) {
        int oppositePlayer = -1 * player;
        int pos[] = new int[2];
        yPos--;

        while ((yPos > 0) && (board[xPos][yPos] == oppositePlayer)) {
            if ((board[xPos][yPos - 1] == 0) && !containsElements(legalMoves,xPos,yPos - 1)) {
                pos[0] = xPos;
                pos[1] = --yPos;
                return pos;
            }
            yPos--;
        }
        return null;
    }

    private int[] checkUpRight(int xPos, int yPos, int player) {
        int oppositePlayer = -1 * player;
        int pos[] = new int[2];
        xPos--;
        yPos++;

        while ((xPos > 0) && (yPos < 7) && (board[xPos][yPos] == oppositePlayer)) {
            if ((board[xPos - 1][yPos + 1] == 0) && !containsElements(legalMoves,xPos - 1,yPos + 1)) {
                pos[0] = --xPos;
                pos[1] = ++yPos;
                return pos;
            }
            xPos--;
            yPos++;
        }
        return null;
    }

    private int[] checkUpLeft(int xPos, int yPos, int player) {
        int oppositePlayer = -1 * player;
        int pos[] = new int[2];
        xPos--;
        yPos--;

        while ((yPos > 0) && (xPos > 0) && (board[xPos][yPos] == oppositePlayer)) {
            if ((board[xPos - 1][yPos - 1] == 0) && !containsElements(legalMoves,xPos - 1,yPos - 1)) {
                pos[0] = --xPos;
                pos[1] = --yPos;
                return pos;
            }
            xPos--;
            yPos--;
        }
        return null;
    }

    private int[] checkUp(int xPos, int yPos, int player) {
        int oppositePlayer = -1 * player;
        int pos[] = new int[2];
        xPos--;

        while ((xPos > 0) && (board[xPos][yPos] == oppositePlayer)) {
            if ((board[xPos - 1][yPos] == 0) && !containsElements(legalMoves,xPos - 1,yPos)) {
                pos[0] = --xPos;
                pos[1] = yPos;
                return pos;
            }
            xPos--;
        }
        return null;
    }


    public int[][] getBoard() {
        return this.board;
    }

    public void setBoard(int x, int y, int value) {
        board[x][y] = value;
    }

    public void printBoard() {
		
		System.out.println("--- GAME MOVE: "+boardMove+" ---");
		boardMove++;
        System.out.println("    0 1 2 3 4 5 6 7");
		System.out.println("    _______________");
		
        for (int i = 0; i < this.board.length; i++) {
            System.out.print(" "+i + " "+"|");
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] == 1) {
                    System.out.print('B');
                } else if (this.board[i][j] == -1) {
                    System.out.print("W");

                } else {
                    System.out.print("_");
                }
                if (j < this.board.length - 1) {
                    System.out.print(' ');
                }
            }
            System.out.println();
        }       
    }

    public boolean isTerminal() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (this.board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getlastPlayer() {
        return lastPlayer;
    }

    public void setlastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    private int[][] heuristicBoard = {
        {20, -3, 11, 8, 8, 11, -3, 20},
        {-3, -7, -4, 1, 1, -4, -7, -3},
        {11, -4, 2, 2, 2, 2, -4, 11},
        {8, 1, 2, -3, -3, 2, 1, 8},
        {8, 1, 2, -3, -3, 2, 1, 8},
        {11, -4, 2, 2, 2, 2, -4, 11},
        {-3, -7, -4, 1, 1, -4, -7, -3},
        {20, -3, 11, 8, 8, 11, -3, 20}
    };

    //First heuristic Function
    public int heuristicFunction(int playerStarts) {
        int[][] currentBoard = this.board;
        int S = 0;
        if (playerStarts == 1) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (currentBoard[i][j] == 1) {
                        S += heuristicBoard[i][j];
                    }
                }
            }

        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (currentBoard[i][j] == -1) {
                        S += heuristicBoard[i][j];
                    }
                }
            }
        }
        return S;
    }

    //Counter carrying the game score 
    public int[] countScore() {
        int counter[] = new int[2];
        int[][] board = this.board;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == BLACK) {
                    counter[0]++;
                } else if (board[i][j] == WHITE) {
                    counter[1]++;
                }
            }
        }
        return counter;
    }

    //Oh look!! another heuristic Function
    public int evaluate(int playerStarts) {
        int player = (playerStarts == BLACK) ? 0 : 1;
        int val = heuristicFunction(playerStarts) + countScore()[player];
        return val;
    }

    public void replaceHorizontal(State state, int[] input, int colourNum) {
        int[][] board = state.getBoard();
        for (int j = input[1]; j != 0; j--) {
            if (board[input[0]][j] == colourNum) {
                for (int k = j; k != input[1]; k++) {
                    state.setBoard(input[0], k, colourNum);
                }
            }
        }
        for (int j = input[1]; j != 7; j++) {
            if (board[input[0]][j] == colourNum) {
                for (int k = j; k != input[1]; k--) {
                    state.setBoard(input[0], k, colourNum);
                }
            }
        }

    }

    public void replaceVertical(State state, int[] input, int colourNum) {
        int[][] board = state.getBoard();
        for (int j = input[0]; j != 0; j--) {
            if (board[j][input[1]] == colourNum) {
                for (int k = j; k != input[0]; k++) {
                    state.setBoard(k, input[1], colourNum);
                }
            }
        }
        for (int j = input[0]; j != 7; j++) {
            if (board[j][input[1]] == colourNum) {
                for (int k = j; k != input[0]; k--) {
                    state.setBoard(k, input[1], colourNum);
                }
            }
        }

    }

    public void replaceDiagonalLeft(State state, int[] input, int colourNum) {
        int[][] board = state.getBoard();
        for (int j = input[0], z = input[1]; j != 0 && z != 0; j--, z--) {
            if (board[j][z] == colourNum) {
                for (int k = j, l = z; k != input[0] && l != input[1]; k++, l++) {
                    state.setBoard(k, l, colourNum);
                }
            }
        }
        for (int j = input[0], z = input[1]; j != 7 && z != 7; j++, z++) {
            if (board[j][z] == colourNum) {
                for (int k = j, l = z; k != input[0] && l != input[1]; k--, l--) {
                    state.setBoard(k, l, colourNum);
                }
            }
        }

    }

    public void replaceDiagonalRight(State state, int[] input, int colourNum) {
        int[][] board = state.getBoard();
        for (int j = input[0], z = input[1]; j != 0 && z != 7; j--, z++) {
            if (board[j][z] == colourNum) {
                for (int k = j, l = z; k != input[0] && l != input[1]; k++, l--) {
                    state.setBoard(k, l, colourNum);
                }
            }
        }
        for (int j = input[0], z = input[1]; j != 7 && z != 0; j++, z--) {
            if (board[j][z] == colourNum) {
                for (int k = j, l = z; k != input[0] && l != input[1]; k--, l++) {
                    state.setBoard(k, l, colourNum);
                }
            }
        }

    }

  //check arrayList
    public  boolean containsElements(ArrayList<int[]> list, int x, int y) {
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i)[0] == x) && (list.get(i)[1] == y)) {
                return true;
            }
        }
        return false;
    }
    
}





















