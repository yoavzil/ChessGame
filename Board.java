
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board {
    private Piece[][] grid = new Piece[8][8];
    private int size;
    private int isFlipped = 1;

    public Board(int size) {
        this.size = size;
        //black pieces
        grid[0][0] = new Rook(0, 0, 0, this.grid);
        grid[0][7] = new Rook(7, 0, 0, this.grid);
        grid[0][1] = new Knight(1, 0, 0, this.grid);
        grid[0][6] = new Knight(6, 0, 0, this.grid);
        grid[0][2] = new Bishop(2, 0, 0, this.grid);
        grid[0][5] = new Bishop(5, 0, 0, this.grid);
        grid[0][3] = new Queen(3, 0, 0, this.grid);
        grid[0][4] = new King(4, 0, 0, this.grid);
        //white pieces
        grid[7][0] = new Rook(0, 7, 1, this.grid);
        grid[7][7] = new Rook(7, 7, 1, this.grid);
        grid[7][1] = new Knight(1, 7, 1, this.grid);
        grid[7][6] = new Knight(6, 7, 1, this.grid);
        grid[7][2] = new Bishop(2, 7, 1, this.grid);
        grid[7][5] = new Bishop(5, 7, 1, this.grid);
        grid[7][3] = new Queen(3, 7, 1, this.grid);
        grid[7][4] = new King(4, 7, 1, this.grid);
        for (int i = 0; i < 8; i++) {
            grid[1][i] = new Pawn(i, 1, 0, true, this.grid);
            grid[6][i] = new Pawn(i, 6, 1, true, this.grid);
        }
    }

    public Board(Piece[][] g) {
        this.size = g.length;
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g.length; j++) {
                this.grid[i][j] = g[i][j];
            }
        }
    }

    public Piece[][] getGrid() {
        return this.grid;
    }

    public void setGrid(int x, int y, Piece p) {
        this.grid[x][y] = p;
    }

    public void draw(Group root) {
        String[] ab = {"A", "B", "C", "D", "E", "F", "G", "H"};
        int mod = 2;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if ((j + i) % mod == 0) {
                    Rectangle r1 = new Rectangle(size / grid.length * j + size / 8, size / grid.length * i + size / 16,
                            size / grid.length, size / grid.length);
                    r1.setFill(Color.rgb(229, 246, 252));
                    root.getChildren().add(r1);
                } else {
                    Rectangle r2 = new Rectangle(size / grid.length * j + size / 8, size / grid.length * i + size / 16,
                            size / grid.length, size / grid.length);
                    r2.setFill(Color.rgb(53, 198, 250));
                    root.getChildren().add(r2);
                }
                Text num;
                Text letter;
                if (j == 0) {
                    num = new Text();
                    if (this.isFlipped == 1)
                        num.setText(Integer.toString(grid.length - i));
                    else
                        num.setText(Integer.toString(i));
                    num.setX(0 + size / 8);
                    num.setY(size / grid.length * i + size / 42 + size / 16);
                    num.setFont(Font.font("Ariel", size / 42));
                    root.getChildren().add(num);
                }
                if (i == 7) {
                    letter = new Text();
                    if (isFlipped == 1)
                        letter.setText(ab[j]);
                    else
                        letter.setText(ab[grid.length - j - 1]);
                    letter.setX(size / grid.length * j + size / grid.length - size / 64 + size / 8);
                    letter.setY(size + size / 17);
                    letter.setFont(Font.font("Ariel", size / 45));
                    root.getChildren().add(letter);
                }
            }
        }
        drawPieces(root);
    }

    public void drawPieces(Group root) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                Text text;
                if (grid[i][j] != null) {
                    text = new Text();
                    text.setText(String.valueOf(Character.toChars(Integer.parseInt(grid[i][j].getSymbol(), 16))));
                    text.setX(size / grid.length * j + size / grid.length / 2 - size / 25.6 + size / 8);
                    text.setY(size / grid.length * i + size / grid.length / 2 + size / 25.6 + size / 16);
                    text.setFont(Font.font("Ariel", size / 12.8));
                    root.getChildren().add(text);
                }
            }
        }
    }
    public void drawPiece(Group root, Piece piece, double x, double y){

    }
    public int getSize() {
        return size;
    }

    public void flipBoard() {
        this.isFlipped *= -1;
        Board tmp = new Board(size);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] != null)
                    tmp.setGrid(grid.length - i - 1, grid.length - j - 1, grid[i][j]);
                else
                    tmp.setGrid(grid.length - i - 1, grid.length - j - 1, null);
            }
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (tmp.getGrid()[i][j] != null) {
                    this.grid[i][j] = tmp.getGrid()[i][j];
                    this.grid[i][j].setX(j);
                    this.grid[i][j].setY(i);
                } else
                    this.grid[i][j] = null;
            }
        }
    }
    public Board copyBoard(){
        Board copy = new Board(this.size);
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                if (this.grid[i][j] == null)
                    copy.grid[i][j] = null;
                else
                    copy.grid[i][j] = this.grid[i][j].copyPiece(this.getGrid());
            }
        }
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                if (this.grid[i][j] != null)
                    copy.grid[i][j].setGrid(copy.getGrid());
            }
        }
        return copy;
    }
    public void setPiece(Piece p, int x, int y){
        this.grid[y][x] = p;
    }
}
