import java.util.List;

public interface Piece {
    public void move(int x, int y, int isFlipped);
    public boolean checkMove(int x, int y, boolean isChecked, boolean hypo, int isFlipped);
    public  boolean checkEat(int x, int y, boolean isChecked, boolean hypo, int isFlipped);
    public int isWhite();
    public String getSymbol();
    public void setX(int x);
    public void setY(int y);
    public int getX();
    public int getY();
    public int getColor();
    public int getValue();
    public Piece create(int x, int y, int color);
    public boolean isThreatened(int x, int y, boolean isChecked, boolean toAdd, int isFlipped);
    public void calAvailableMoves(int isFlipped);
    public List<Point> getAvailableMoves();
    public boolean isMate(int isFlipped);
    public void addThreat(Piece p);
    public void clearTreats();
    public void setKing();
    public boolean getIsFirstMove();
    public void setIsFirstMove(boolean isFirstMove);
    public Piece copyPiece(Piece[][] grid);
    public void setGrid(Piece[][] grid);
    public double[][] getEvTable();
    public void flipEvTable();
}
