import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Bishop implements Piece{
    private int x;
    private int y;
    private int value;
    private int color;
    private String symbolW = "2657";
    private String symbolB = "265D";
    private Piece[][] grid;
    private List<Point> availableMoves = new ArrayList<Point>();
    private List<Piece> threatening = new ArrayList<Piece>();
    private Piece king;
    private double[][] evTable =
            {{-2,-1,-1,-1,-1,-1,-1,-2},
            {-1,0,0,0,0,0,0,-1},
            {-1,0,0.5,1,1,0.5,0,-1},
            {-1,0.5,0.5,1,1,0.5,0.5,-1},
            {-1,0,1,1,1,1,0,-1},
            {-1,1,1,1,1,1,1,-1},
            {-1,0.5,0,0,0,0,0.5,-1},
            {-2,-1,-1,-1,-1,-1,-1,-2}};

    public Bishop(int x, int y, int color, Piece[][] grid){
        this.x = x;
        this.y = y;
        this.color = color;
        this.grid = grid;
        this.value = 3;
        if (color == 0)
            this.flipEvTable();
    }

    public boolean checkMove(int x, int y, boolean isChecked, boolean hypo, int isFlipped){
        int subX = Math.abs(x - this.x);
        int subY = Math.abs(y - this.y);
        int negPosX = -1, negPosY = -1;
        int count = 0;
        if (this.x - x < 0)
            negPosX = 1;
        if (this.y - y < 0)
            negPosY = 1;
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
        if (subX != subY)
            return false;
        int i = this.y + negPosY;
        int j = this.x + negPosX;
        while (count + 1 < subX){
            if (this.grid[i][j] != null){
                return false;
            }
            count++;
            i += negPosY;
            j += negPosX;
        }
        if (hypo){
            int tempX = this.x;
            int tempY = this.y;
            Piece tempP = grid[y][x];
            setKing();
            move(x, y, isFlipped);
            if (this.king.isThreatened(this.king.getX(), this.king.getY(), true, false, isFlipped)) {
                move(tempX, tempY, isFlipped);
                grid[y][x] = tempP;
                return false;
            }
            move(tempX, tempY, isFlipped);
            grid[y][x] = tempP;
        }
        return true;
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
    public boolean checkEat(int x, int y, boolean isChecked, boolean hypo, int isFlipped){
        return checkMove(x, y, isChecked, hypo, isFlipped);
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
        return new Bishop(x, y, color, this.grid);
    }
    public void calAvailableMoves(int isFlipped) {
        this.availableMoves.clear();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (checkMove(j, i, false, true, isFlipped))
                    this.availableMoves.add(new Point(j, i));
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
    }

    public boolean getIsFirstMove() {
        return false;
    }

    public void setIsFirstMove(boolean isFirstMove) {}

    @Override
    public Piece copyPiece(Piece[][] grid) {
        return new Bishop(this.x, this.y, this.color, grid);
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
