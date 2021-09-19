
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;


public class Pawn implements Piece {
    private int x;
    private int y;
    private int value;
    private int color;
    private boolean isFirstMove;
    private String symbolW = "2659";
    private String symbolB = "265F";
    private Piece[][] grid;
    private List<Point> availableMoves = new ArrayList<Point>();
    private List<Piece> threatening = new ArrayList<Piece>();
    private Piece king;
    private double[][] evTable =
            {{0,0,0,0,0,0,0,0},
            {5,5,5,5,5,5,5,5},
            {1,1,2,3,3,2,1,1},
            {0.5,0.5,1,2.5,2.5,1,0.5,0.5},
            {0,0,0,2,2,0,0,0},
            {0.5,-0.5,-1,0,0,-1,-0.5,0.5},
            {0.5,1,1,-2,-2,1,1,0.5},
            {0,0,0,0,0,0,0,0}};

    public Pawn(int x, int y, int color,boolean isFirstMove , Piece[][] grid){
        this.x = x;
        this.y = y;
        this.color = color;
        this.grid = grid;
        this.value = 1;
        this.isFirstMove = isFirstMove;
        if (color == 0)
            this.flipEvTable();
    }

    public boolean checkMove(int x, int y, boolean isChecked, boolean hypo, int isFlipped){
        if (grid[y][x] != null) {
            if (this.checkEat(x, y, false, hypo, isFlipped))
                return this.checkEat(x, y, false, hypo, isFlipped);
            else
                return false;
        }
        if (this.color == 1){
            if (x != this.x)
                return false;
            if (!((y - this.y == -2 * isFlipped && this.isFirstMove) || (y - this.y == -1 * isFlipped)))
                return false;
            if (Math.abs(y - this.y) == 2)
                if (this.grid[(y + this.y) / 2][x] != null)
                    return false;
        } else {
            if (x != this.x)
                return false;
            if (!((y - this.y == 2 * isFlipped && this.isFirstMove) || (y - this.y == 1 * isFlipped)))
            return false;
            if (Math.abs(y - this.y) == 2)
                if (this.grid[(y + this.y) / 2][x] != null)
                    return false;
        }
        if (hypo) {
            int tempX = this.x;
            int tempY = this.y;
            Piece tempP = grid[y][x];
            boolean isFirstM = isFirstMove;
            setKing();
            move(x, y, isFlipped);
            if (this.king.isThreatened(this.king.getX(), this.king.getY(), true, false, isFlipped)) {
                move(tempX, tempY, isFlipped);
                grid[y][x] = tempP;
                this.isFirstMove = isFirstM;
                return false;
            }
            move(tempX, tempY, isFlipped);
            grid[y][x] = tempP;
            this.isFirstMove = isFirstM;
        }
        return true;
    }
    public boolean checkEat(int x, int y, boolean isChecked, boolean hypo, int isFlipped){
        if (x == this.x && y == this.y)
            return false;
        boolean sameSide = false;
        if (grid[y][x] != null)
            sameSide = grid[y][x].getColor() == grid[this.y][this.x].getColor();
        if (isChecked)
            sameSide = false;
        if (grid[y][x] != null) {
            if (sameSide)
                return false;
        }
        if (hypo) {
            int tempX = this.x;
            int tempY = this.y;
            Piece tempP = grid[y][x];
            boolean isFirstM = isFirstMove;
            setKing();
            move(x, y, isFlipped);
            if (this.king.isThreatened(this.king.getX(), this.king.getY(), true, false, isFlipped)) {
                move(tempX, tempY, isFlipped);
                grid[y][x] = tempP;
                this.isFirstMove = isFirstM;
                return false;
            }
            move(tempX, tempY, isFlipped);
            grid[y][x] = tempP;
            this.isFirstMove = isFirstM;
        }
        if (this.color == 1){
            if ((x - this.x == -1 || x - this.x == 1) && y - this.y == -1 * isFlipped) {
                return true;
            }
        } else {
            if ((x - this.x == -1 || x - this.x == 1) && y - this.y == 1 * isFlipped) {
                return true;
            }
        }
        return false;
    }
    public boolean isThreatened(int x, int y, boolean isChecked, boolean toAdd, int isFlipped){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] != null)
                    if (grid[i][j].getColor() != this.color)
                        if (grid[i][j].checkEat(x, y, isChecked, false, isFlipped))
                            return true;
            }
        }
        return false;
    }
    public int isWhite(){
        return this.color;
    }
    public String getSymbol(){
        if (isWhite() == 1)
            return this.symbolW;
        else
            return this.symbolB;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getX(){return this.x;}
    public int getY(){return this.y;}
    public int getColor(){return this.color;}
    public int getValue(){return this.value;}
    public Piece create(int x, int y, int color){
        return new Pawn(x, y, color, isFirstMove, this.grid);
    }
    public void calAvailableMoves(int isFlipped) {
        boolean keepFirstMove =  this.isFirstMove;
        this.availableMoves.clear();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (checkMove(j, i, false, true, isFlipped)) {
                    this.availableMoves.add(new Point(j, i));
                    if (keepFirstMove)
                        this.isFirstMove = true;
                }
            }
        }
    }
    public boolean isMate(int isFlipped) {
        return false;
    }
    public List<Point> getAvailableMoves() {
        return availableMoves;
    }
    public void addThreat(Piece p) {
        this.threatening.add(p);
    }
    public void clearTreats() {
        this.threatening.clear();
    }
    public void setKing() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] != null)
                    if (grid[i][j].getSymbol().equals("2654") || grid[i][j].getSymbol().equals("265A"))
                        if (grid[i][j].getColor() == this.color){
                            this.king = grid[i][j];
                            break;
                        }
            }
        }
    }
    public void move(int x, int y, int isFlipped) {
        this.grid[y][x] = this;
        this.grid[this.y][this.x] = null;
        this.x = x;
        this.y = y;
        this.isFirstMove = false;
        if (y == 0 || y ==7) {
            grid[y][x] = new Queen(x, y, this.color, this.grid);
            return;
        }
    }

    public boolean getIsFirstMove() {
        return this.isFirstMove;
    }

    public void setIsFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    @Override
    public Piece copyPiece(Piece[][] grid) {
        return new Pawn(this.x, this.y, this.color,this.isFirstMove, grid);
    }

    @Override
    public void setGrid(Piece[][] grid) {
        this.grid = grid;
    }

    @Override
    public double[][] getEvTable() {
        return evTable;
    }
    public void flipEvTable() {
        double[][] newEv = this.evTable;
        for (int i = 0; i < this.evTable.length; i++) {
            for (int j = 0; j < this.evTable.length; j++) {
                newEv[i][j] = this.evTable[this.evTable.length - i - 1][this.evTable.length - j - 1];
            }
        }
        this.evTable = newEv;
    }
}
