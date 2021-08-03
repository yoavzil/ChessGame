import javafx.scene.image.Image;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class King implements Piece{
    private int x;
    private int y;
    private int value;
    private int color;
    private boolean isFirstMove;
    private String symbolW = "2654";
    private String symbolB = "265A";
    private Piece[][] grid;
    private List<Point> availableMoves = new ArrayList<Point>();
    private List<Piece> threatening = new ArrayList<Piece>();
    private Piece leftRook;
    private Piece rightRook;
    private boolean isRooksSwap = false;
    private double[][] evTable =
            {{-3,-4,-4,-5,-5,-4,-4,-3},
            {-3,-4,-4,-5,-5,-4,-4,-3},
            {-3,-4,-4,-5,-5,-4,-4,-3},
            {-3,-4,-4,-5,-5,-4,-4,-3},
            {-2,-3,-3,-4,-4,-3,-3,-2},
            {-1,-2,-2,-2,-2,-2,-2,-1},
            {2,2,0,0,0,0,2,2},
            {2,3,1,0,0,1,3,2}};


    public King(int x, int y, int color, Piece[][] grid){
        this.x = x;
        this.y = y;
        this.color = color;
        this.grid = grid;
        this.value = 1000;
        this.isFirstMove = true;
        if (this.color == 1){
            this.leftRook = grid[7][0];
            this.rightRook = grid[7][7];
        } else {
            this.leftRook = grid[0][0];
            this.rightRook = grid[0][7];
            this.flipEvTable();
        }
    }
    public boolean checkMove(int x, int y, boolean isChecked, boolean hypo, int isFlipped){
        int subX = Math.abs(x - this.x);
        int subY = Math.abs(y - this.y);
        if (x == this.x && y == this.y)
            return false;
        boolean sameSide = false;
        if (grid[y][x] != null)
            sameSide = grid[y][x].getColor() == grid[this.y][this.x].getColor();
        if (isChecked)
            sameSide = false;
        if (isThreatened(x, y, isChecked, false, isFlipped))
            return false;
        if (grid[y][x] != null) {
            if (sameSide)
                return false;
        }
        if (!((subX == 0 && subY == 1) || (subX == 1 && subY == 0) || (subX == 1 && subY == 1))) {
            int negPos = x - this.x;
            List<Integer> right = new ArrayList<Integer>();
            List<Integer> left = new ArrayList<Integer>();
            right.add(5);
            right.add(6);
            left.add(1);
            left.add(2);
            left.add(3);
            if (isFlipped == -1){
                right.add(0, 4);
                left.remove(2);
            }
            if (subX == 2 && subY == 0){
                if (negPos > 0){
                    if (this.rightRook.getIsFirstMove() && this.isFirstMove){
                        if (!isThreatened(this.x, this.y, false, false, isFlipped)){
                            boolean isEmptySide = true;
                            for (int i = 0; i < right.size(); i++) {
                                if (grid[this.y][right.get(i)] != null)
                                    isEmptySide = false;
                            }
                            if (isEmptySide){
                                boolean canCastle = true;
                                for (int i = 0; i < right.size(); i++) {
                                    if (isThreatened(right.get(i), this.y, false, false, isFlipped))
                                        canCastle = false;
                                }
                                return (canCastle);
                            } else
                                return false;
                        } else
                            return false;
                    } else
                        return false;
                } else {
                    if (this.leftRook.getIsFirstMove() && this.isFirstMove){
                        if (!isThreatened(this.x, this.y, false, false, isFlipped)){
                            boolean isEmptySide = true;
                            for (int i = 0; i < left.size(); i++) {
                                if (grid[this.y][left.get(i)] != null)
                                    isEmptySide = false;
                            }
                            if (isEmptySide){
                                boolean canCastle = true;
                                for (int i = 0; i < right.size(); i++) {
                                    if (isThreatened(right.get(i), this.y, false, false, isFlipped))
                                        canCastle = false;
                                }
                                return (canCastle);
                            } else
                                return false;
                        } else
                            return false;
                    } else
                        return false;
                }

            } else
                return false;
        }
        if (hypo) {
            int tempX = this.x;
            int tempY = this.y;
            Piece tempP = grid[y][x];
            boolean isFirstM = isFirstMove;
            setKing();
            move(x, y, isFlipped);
            if (this.isThreatened(this.getX(), this.getY(), true, false, isFlipped)) {
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
    public boolean isThreatened(int x, int y, boolean isChecked, boolean toAdd, int isFlipped){
        boolean thr = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] != null) {
                    if (!(grid[i][j].getSymbol().equals("2654") || grid[i][j].getSymbol().equals("265A")) &&
                            grid[i][j].getColor() != this.color)
                        if (grid[i][j].checkEat(x, y, isChecked, false, isFlipped)) {
                            if (toAdd)
                                this.threatening.add(grid[i][j]);
                            thr = true;
                        }
                    if ((grid[i][j].getSymbol().equals("2654") && this.color == 0) || (grid[i][j].getSymbol().equals("265A") && this.color == 1)) {
                        if ((Math.abs(i - y) == 1 || (i - y) == 0) && (Math.abs(j - x) == 1 || (j - x) == 0))
                            thr = true;
                    }
                }
            }
        }
        return thr;
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
        return new King(x, y, color, grid);
    }
    public void calAvailableMoves(int isFlipped){
        this.availableMoves.clear();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (checkMove(j, i, false, true, isFlipped))
                    if (!isThreatened(j, i, true, false, isFlipped))
                        this.availableMoves.add(new Point(j, i));
            }
        }
    }
    public boolean threatCanBeBlocked(int isFlipped){
        for (int i = 0; i < threatening.get(0).getAvailableMoves().size(); i++) {
            for (int j = 0; j < grid.length; j++) {
                for (int k = 0; k < grid.length; k++) {
                    if (grid[j][k] == null || (threatening.get(0).getX() == k && threatening.get(0).getY() == j))
                        continue;
                    if (grid[j][k].getColor() == threatening.get(0).getColor())
                        continue;
                    grid[j][k].calAvailableMoves(isFlipped);
                    for (int l = 0; l < grid[j][k].getAvailableMoves().size(); l++) {
                        if (threatening.get(0).getAvailableMoves().get(i).getX() == grid[j][k].getAvailableMoves().get(l).getX() &&
                                threatening.get(0).getAvailableMoves().get(i).getY() == grid[j][k].getAvailableMoves().get(l).getY()){
                            int tempx = grid[j][k].getAvailableMoves().get(l).getX();
                            int tempy = grid[j][k].getAvailableMoves().get(l).getY();
                            boolean isFirstM = grid[j][k].getIsFirstMove();
                            grid[j][k].move(grid[j][k].getAvailableMoves().get(l).getX(),
                                    grid[j][k].getAvailableMoves().get(l).getY(), isFlipped);
                            if (!grid[this.y][this.x].isThreatened(this.x, this.y, true, false, isFlipped)) {
                                grid[tempy][tempx].move(k, j, isFlipped);
                                if (isFirstM)
                                    grid[j][k].setIsFirstMove(isFirstM);
                                return true;
                            }
                            else {
                                grid[tempy][tempx].move(k, j, isFlipped);
                                if (isFirstM)
                                    grid[j][k].setIsFirstMove(isFirstM);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean isMate(int isFlipped) {
        calAvailableMoves(isFlipped);
        threatening.get(0).calAvailableMoves(isFlipped);
        if (this.availableMoves.size() != 0 || this.threatening.get(0).isThreatened(this.threatening.get(0).getX(),
                this.threatening.get(0).getY(), true, false, isFlipped) || this.threatCanBeBlocked(isFlipped))
            return false;
        else
            return true;
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

    }
    public void move(int x, int y, int isFlipped) {
        if (x - this.x == 2){
            if (isFlipped == 1)
                this.rightRook.move(5, y, isFlipped);
            else
                this.leftRook.move(4, y, isFlipped);
        }
        if (x - this.x == -2){
            if (isFlipped == 1)
                this.leftRook.move(3, y, isFlipped);
            else
                this.rightRook.move(2, y, isFlipped);
        }
        this.grid[y][x] = this;
        this.grid[this.y][this.x] = null;
        this.x = x;
        this.y = y;
        isFirstMove = false;
    }

    public boolean getIsFirstMove() {
        return this.isFirstMove;
    }

    public void setIsFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }
    public void swapRooks(){
        Piece tmp = this.rightRook;
        this.rightRook = this.leftRook;
        this.leftRook = tmp;
        this.isRooksSwap = true;
    }

    @Override
    public Piece copyPiece(Piece[][] grid) {
        return new King(this.x, this.y, this.color, grid);
    }

    @Override
    public void setGrid(Piece[][] grid) {
        this.grid = grid;
    }

    @Override
    public double[][] getEvTable() {
        return evTable;
    }

    @Override
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
