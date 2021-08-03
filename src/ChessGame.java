
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChessGame extends Application{
    public static void main(String args[]){
        launch(args);
    }
    public void start(Stage stage) throws Exception{
        int size = 560;
        BoardGenerator bg = new BoardGenerator();
        Board b = bg.generateBoard(size);
        Group root = new Group();
        GameData gd = new GameData(root, size, b);
        Player wP = new Player(1);
        Player bP = new Player(0);
        GameFlow gameFlow = new GameFlow(root, size, gd, wP, bP);
        AIhandler aIhandler = new AIhandler(bP, gameFlow);
        Scene scene = new Scene(root, size + size / 4, size + size / 8);
        b.draw(root);
        gd.draw();
        gd.drawTurn(1);
        Button start = new Button(String.valueOf(Character.toChars(Integer.parseInt("11118"))));
        start.setLayoutY(size / 2 + size/ 16);
        root.getChildren().add(start);
        Button flip = new Button(String.valueOf(Character.toChars(Integer.parseInt("128260"))));
        flip.setLayoutY(size / 2 + size/ 16 + size / 16);
        flip.toFront();
        root.getChildren().add(flip);
        Button backward = new Button(String.valueOf(Character.toChars(Integer.parseInt("129092"))));
        backward.setLayoutY(size / 2 + size/ 16 - size / 16);
        root.getChildren().add(backward);
        backward.toFront();
        Button forward = new Button(String.valueOf(Character.toChars(Integer.parseInt("129094"))));
        forward.setLayoutY(size / 2 + size/ 16 - size / 16);
        forward.setLayoutX( size / 16);
        root.getChildren().add(forward);
        forward.toFront();
        Button comp = new Button(String.valueOf(Character.toChars(Integer.parseInt("128187"))));
        comp.setLayoutY(size / 2 + size/ 16 + size / 16 + size / 16);
        comp.setFont(Font.font("Ariel", size / 22));
        root.getChildren().add(comp);
        comp.toFront();
        List<Button> buttons = new ArrayList<>();
        buttons.add(start);
        buttons.add(flip);
        buttons.add(backward);
        buttons.add(forward);
        buttons.add(comp);

        final boolean[] isComp = {false};
        final int[] isFlipped = {1};
        final int[] i = {2};
        backward.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (gameFlow.getMovesList().size() - i[0] >= 0) {
                    gameFlow.setBoard(gameFlow.getBoardState(gameFlow.getMovesList().size() - 2).getBoard().copyBoard());
                    for (int j = 0; j < b.getGrid().length; j++) {
                        for (int k = 0; k < b.getGrid().length; k++) {
                            if (gameFlow.getBoard().getGrid()[j][k] != null)
                                gameFlow.getBoard().getGrid()[j][k].setGrid(gameFlow.getBoard().getGrid());
                        }
                    }
                    gd.drawScore(gameFlow.getBoardState(gameFlow.getMovesList().size() - 2).getwScore(),
                            gameFlow.getBoardState(gameFlow.getMovesList().size() - 2).getbScore(), isFlipped[0]);
                    gd.drawTurn(gameFlow.getBoardState(gameFlow.getMovesList().size() - 2).getLastColor());
                    gameFlow.setLastColor(1 - gameFlow.getBoardState(gameFlow.getMovesList().size() - 2).getLastColor());
                    gameFlow.getMovesList().remove(gameFlow.getMovesList().size() - 1);
                }
                gameFlow.getBoard().draw(root);
                start.toFront();
                flip.toFront();
                backward.toFront();
                forward.toFront();
                comp.toFront();
            }
        });
        flip.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                gameFlow.getBoard().flipBoard();
                isFlipped[0] *= -1;
                gd.drawScore(gd.getWs(), gd.getBs(), isFlipped[0]);
                gameFlow.getBoard().draw(root);
                start.toFront();
                flip.toFront();
                comp.toFront();
                backward.toFront();
                gd.drawTurn(1 - gameFlow.getLastColor());
            }
        });
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                root.getChildren().clear();
                gameFlow.setBoard(bg.generateBoard(size));
                gameFlow.setKings();
                gameFlow.setLastColor(0);
                gameFlow.getMovesList().clear();
                gameFlow.getMovesList().add(new BoardState(bg.generateBoard(size), 1, 0 ,0));
                i[0] = 2;
                gd.draw();
                gd.drawTurn(1);
                root.getChildren().add(start);
                root.getChildren().add(flip);
                root.getChildren().add(backward);
                root.getChildren().add(forward);
                root.getChildren().add(comp);
                gameFlow.getBoard().draw(root);
            }
        });
        comp.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (comp.getText().equals(String.valueOf(Character.toChars(Integer.parseInt("128187")))))
                    comp.setText(String.valueOf(Character.toChars(Integer.parseInt("128104"))));
                else
                    comp.setText(String.valueOf(Character.toChars(Integer.parseInt("128187"))));
                if (isComp[0])
                    isComp[0] = false;
                else
                    isComp[0] = true;
            }
        });
        final int[] clickCount = {0};
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                clickCount[0]++;
                gameFlow.getBoard().draw(root);
                int x = (int) ((event.getSceneX() - size / 8)/ (size / 8));
                int y = (int) ((event.getSceneY() - size / 16) / (size / 8));
                if (((event.getSceneX() - size / 8)/ (size / 8) >= 0 && (event.getSceneX() - size / 8)/ (size / 8) < 8) &&
                        ((event.getSceneY() - size / 16) / (size / 8) >= 0 && (event.getSceneY() - size / 16) / (size / 8) < 8)){
                    if (clickCount[0] == 1) {
                        if (gameFlow.choosePiece(x, y, isFlipped[0]) == null)
                            clickCount[0] = 0;
                    }
                    if (clickCount[0] == 2) {
                        gameFlow.movePiece(x, y, buttons, isFlipped[0], false);
                        clickCount[0] = 0;
                        if (isComp[0]) {
                            List<Point> pieceAndMove = aIhandler.getBestMove(isFlipped[0]);
                            if (pieceAndMove != null) {
                                gameFlow.choosePiece(pieceAndMove.get(0).getX(), pieceAndMove.get(0).getY(), isFlipped[0]);
                                gameFlow.movePiece(pieceAndMove.get(1).getX(), pieceAndMove.get(1).getY(), buttons, isFlipped[0], true);
                            }
                        }
                    }
                } else {
                    if (clickCount[0] == 2)
                        gameFlow.setLastColor(1 - gameFlow.getLastColor());
                    clickCount[0] = 0;
                }
            }
        });
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
    }
}
