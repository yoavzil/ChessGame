import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameData {
    private Group root;
    private  int size;
    private Board board;
    private int ws;
    private int bs;

    public GameData(Group root, int size, Board board){
        this.root = root;
        this.size = size;
        this.board = board;
    }

    public void draw(){
        Rectangle r1 = new Rectangle(0, 0,
                size / 8, size + size / 8);
        r1.setFill(Color.LIGHTGRAY);
        Rectangle r2 = new Rectangle(size + size / 8, 0,
                size / 8, size + size / 8);
        r2.setFill(Color.LIGHTGRAY);
        Rectangle r3 = new Rectangle(0, 0,
                size + size / 4, size / 16);
        r3.setFill(Color.LIGHTGRAY);
        Rectangle r4 = new Rectangle(0, size + size / 16,
                size + size / 4, size / 16);
        r4.setFill(Color.LIGHTGRAY);
        root.getChildren().add(r1);
        root.getChildren().add(r2);
        root.getChildren().add(r3);
        root.getChildren().add(r4);
    }
    public void drawScore(int wScore, int bScore, int isFlipped){
        this.ws = wScore;
        this.bs = bScore;
        this.draw();
        Text ws;
        Text bs;
        double wP = size + size / 16 + size / 32 + size / 70;
        double bP = 0 + size / 32 + size / 70;
        ws = new Text();
        ws.setText(Integer.toString(wScore));
        ws.setX(size / 2 + size / 8);
        if (isFlipped ==1)
            ws.setY(wP);
        else
            ws.setY(bP);
        ws.setFont(Font.font("Ariel",size / 35));
        root.getChildren().add(ws);
        bs = new Text();
        bs.setText(Integer.toString(bScore));
        bs.setX(size / 2 + size / 8);
        if (isFlipped == 1)
            bs.setY(bP);
        else
            bs.setY(wP);
        bs.setFont(Font.font("Ariel",size / 35));
        root.getChildren().add(bs);
    }
    public void drawTurn(int color){
        Rectangle r1 = new Rectangle(size + size / 8 + size / 16 - size / 60, size / 2 + size / 16,
                size / 30,  size / 30);
        if (color == 1)
            r1.setFill(Color.WHITE);
        else
            r1.setFill(Color.BLACK);
        root.getChildren().add(r1);
    }

    public int getWs() {
        return ws;
    }

    public int getBs() {
        return bs;
    }
}
