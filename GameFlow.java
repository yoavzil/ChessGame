import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameFlow {
    private int size;
    private Board board;
    private List<Piece> pieces = new ArrayList<Piece>();
    private Piece chosen;
    private Group root;
    private Piece kingW;
    private Piece kingB;
    private int lastColor;
    private boolean isRed;
    private Player wP;
    private Player bP;
    private GameData gd;
    private List<BoardState> movesList = new ArrayList<BoardState>();

    public GameFlow(Group root, int size, GameData gd, Player wP, Player bP){
        this.root = root;
        this.size = size;
        this.board = new Board(size);
        this.movesList.add(new BoardState(new Board(size), 1, 0 , 0));
        this.wP = wP;
        this.bP = bP;
        this.gd = gd;
        for (int i = 0; i < board.getGrid().length; i++) {
            for (int j = 0; j < board.getGrid().length; j++) {
                if (board.getGrid()[i][j] != null) {
                    this.pieces.add(board.getGrid()[i][j]);
                    {
                        if (board.getGrid()[i][j].getSymbol().equals("2654"))
                            this.kingW = board.getGrid()[i][j];
                        if (board.getGrid()[i][j].getSymbol().equals("265A"))
                            this.kingB = board.getGrid()[i][j];
                    }
                }
            }
        }
    }

    public Piece choosePiece(int x, int y, int isFlipped){
        isRed = false;

        this.chosen = board.getGrid()[y][x];
        Rectangle chosenS = new Rectangle(size / board.getGrid().length * x + size / 8,
                size / board.getGrid().length * y + size / 16,
                size / board.getGrid().length, size / board.getGrid().length);
        chosenS.setFill(Color.TRANSPARENT);
        chosenS.setStrokeType(StrokeType.INSIDE);
        chosenS.setStrokeWidth(size / 128);
        if (this.chosen == null){
            chosenS.setStroke(Color.RED);
            isRed = true;
        } else if (lastColor == chosen.getColor()){
            chosenS.setStroke(Color.RED);
            isRed = true;
        } else {
            this.lastColor = chosen.getColor();
            chosenS.setStroke(Color.YELLOW);
            this.chosen.calAvailableMoves(isFlipped);
            int sizeS = size / board.getGrid().length;
            for (int i = 0; i < this.chosen.getAvailableMoves().size(); i++) {
                Circle c = new Circle(chosen.getAvailableMoves().get(i).getX() * sizeS + sizeS / 2 + size / 8,
                        chosen.getAvailableMoves().get(i).getY() * sizeS + sizeS / 2 + size / 16, size / 32);
                c.setFill(Color.GREY);
                c.setOpacity(0.4);
                root.getChildren().add(c);
            }
        }
        if (isRed) {
            this.chosen = null;
            isRed = false;
        }
        root.getChildren().add(chosenS);
        return this.chosen;
    }

    public void movePiece(int x, int y, List<Button> buttons, int isFlipped, boolean aiOP){
        isRed = true;
        Piece king;
        if (chosen == null)
            return;
        if (chosen.getColor() == 1)
            king = kingB;
        else
            king = kingW;
        Rectangle chosenS = new Rectangle(size / board.getGrid().length * x + size / 8,
                size / board.getGrid().length * y +size / 16,
                size / board.getGrid().length, size / board.getGrid().length);
        chosenS.setFill(Color.TRANSPARENT);
        chosenS.setStrokeType(StrokeType.INSIDE);
        chosenS.setStrokeWidth(size / 128);
        Rectangle checkMateS = new Rectangle(size / board.getGrid().length * king.getX() + size / 8,
                size / board.getGrid().length * king.getY() + size / 16,
                size / board.getGrid().length, size / board.getGrid().length);
        checkMateS.setFill(Color.TRANSPARENT);
        checkMateS.setStrokeType(StrokeType.INSIDE);
        checkMateS.setStrokeWidth(size / 128);
        for (int i = 0; i < chosen.getAvailableMoves().size(); i++) {
            if (x == chosen.getAvailableMoves().get(i).getX() &&
                    y == chosen.getAvailableMoves().get(i).getY()){
                chosenS.setStroke(Color.FORESTGREEN);
                chosen.move(x, y, isFlipped);
                this.movesList.add(new BoardState(this.board.copyBoard(), 1 - lastColor, this.wP.getScore(this.board.getGrid()), this.bP.getScore(this.board.getGrid())));
                this.board.draw(root);
                this.gd.drawScore(this.wP.getScore(this.board.getGrid()), this.bP.getScore(this.board.getGrid()), isFlipped);
                this.gd.drawTurn(1 - lastColor);
                for (Button button:buttons) {
                    button.toFront();
                }
                isRed = false;
                break;
            } else {
                chosenS.setStroke(Color.RED);
            }
        }
        if (isRed){
            lastColor = 1 - lastColor;
            isRed = false;
        }
        this.root.getChildren().add(chosenS);
        king.clearTreats();
        if (king.isThreatened(king.getX(), king.getY(), true, true, isFlipped)){
            checkMateS.setStroke(Color.BLUE);
//            this.movesList.add(new BoardState(this.board.copyBoard(), 1 - lastColor, this.wP.getScore(this.board.getGrid()), this.bP.getScore(this.board.getGrid())));
            if (king.isMate(isFlipped))
                checkMateS.setStroke(Color.BLACK);
        } else
            checkMateS.setStroke(Color.TRANSPARENT);
        this.root.getChildren().add(checkMateS);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setLastColor(int lastColor) {
        this.lastColor = lastColor;
    }

    public int getLastColor() {
        return lastColor;
    }
    public void setKings(){
        for (int i = 0; i < board.getGrid().length; i++) {
            for (int j = 0; j < board.getGrid().length; j++) {
                if (board.getGrid()[i][j] != null) {
                    this.pieces.add(board.getGrid()[i][j]);
                    {
                        if (board.getGrid()[i][j].getSymbol().equals("2654"))
                            this.kingW = board.getGrid()[i][j];
                        if (board.getGrid()[i][j].getSymbol().equals("265A"))
                            this.kingB = board.getGrid()[i][j];
                    }
                }
            }
        }
    }
    /*public Piece getCompPiece(int isFlipped){
        Random random = new Random();
        int i = 7;
        int j = 7;
        int avMoves = 0;
        while (avMoves == 0 || board.getGrid()[i][j].getColor() == 1) {
            while (board.getGrid()[i][j] == null || board.getGrid()[i][j].getColor() == 1) {
                i = random.nextInt(8);
                j = random.nextInt(8);
            }
            board.getGrid()[i][j].calAvailableMoves(isFlipped);
            avMoves = board.getGrid()[i][j].getAvailableMoves().size();
        }
        return board.getGrid()[i][j];
    }*/
    public List<Point> getCompMove(int isFlipped){
        this.updatePieces();
        int numAvailMoves = 0;
        for (int i = 0; i < this.pieces.size(); i++) {
            this.pieces.get(i).calAvailableMoves(isFlipped);
            if (this.pieces.get(i).getColor() == 0)
                numAvailMoves += this.pieces.get(i).getAvailableMoves().size();
        }
        if (numAvailMoves == 0)
            return null;
        List<Point> pieceAndMove = new ArrayList<Point>();
        Random random = new Random();
        int p = random.nextInt(this.pieces.size());
        while (this.pieces.get(p) == null || this.pieces.get(p).getColor() == 1 || this.pieces.get(p).getAvailableMoves().size() == 0){
            p = random.nextInt(this.pieces.size());
        }
        int m = random.nextInt(this.pieces.get(p).getAvailableMoves().size());
        pieceAndMove.add(new Point(this.pieces.get(p).getX(), this.pieces.get(p).getY()));
        pieceAndMove.add(this.pieces.get(p).getAvailableMoves().get(m));
        return pieceAndMove;
    }
    public BoardState getBoardState(int i){
        return this.movesList.get(i);
    }

    public List<BoardState> getMovesList() {
        return movesList;
    }
    public void updatePieces(){
        this.pieces.clear();
        for (int i = 0; i < board.getGrid().length; i++) {
            for (int j = 0; j < board.getGrid().length; j++) {
                if (board.getGrid()[i][j] != null) {
                    this.pieces.add(board.getGrid()[i][j]);
                }
            }
        }
    }

    public int getSize() {
        return size;
    }
    public GameData getGd(){return this.gd;}

    public Group getRoot() {
        return root;
    }
    public void transMove(Piece piece, int x, int y, int isFlipped){
        double tempX = piece.getX() + 1;
        double m = (y - piece.getY()) / (x - piece.getX());
        double tempY;
        while (piece.getX() != x){
            tempY = (tempX - piece.getX()) * m + piece.getY();
            //piece.move(tempX, tempY, isFlipped);
            tempX++;
        }
    }
}
