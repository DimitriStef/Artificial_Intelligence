
public class Move {

    private int[] move = new int[2];
    private int value;

    public Move() {
        value = 0;
    }

    public Move(int value) {
        this.value = value;
    }

    public Move(int[] move) {
        this.move[0] = move[0];
        this.move[1] = move[1];

    }

    public Move(int[] move, int value) {
        this.move[0] = move[0];
        this.move[1] = move[1];
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setMove(int[] move) {
        this.move[0] = move[0];
        this.move[1] = move[1];
    }

    public int[] getMove() {
        return move;
    }
	
	 public String toString() {
    	return "["+move[0]+", "+move[1]+"]";
	 }

}
