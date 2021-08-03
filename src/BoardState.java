public class BoardState {
    private Board board;
    private int lastColor;
    private int wScore;
    private int bScore;

    public BoardState(Board board, int lastColor, int wScore, int bScore){
        this.board = board;
        this.lastColor = lastColor;
        this.wScore = wScore;
        this.bScore = bScore;
    }

    public Board getBoard() {
        return board;
    }

    public int getLastColor() {
        return lastColor;
    }

    public int getwScore() {
        return wScore;
    }

    public int getbScore() {
        return bScore;
    }
}
