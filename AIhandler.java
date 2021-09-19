import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIhandler {
    private Player player;
    private GameFlow gameFlow;
    private List<Piece> bPieces;
    private List<Piece> wPieces;

    public AIhandler(Player p, GameFlow gameFlow){
        this.player = p;
        this.gameFlow = gameFlow;
        this.wPieces = new ArrayList<>();
        this.bPieces = new ArrayList<>();
        this.updatePieces();
    }

    public List<Point> getRandomMove(int isFlipped){
        this.updatePieces();
        int numAvailMoves = 0;
        for (int i = 0; i < this.bPieces.size(); i++) {
            this.bPieces.get(i).calAvailableMoves(isFlipped);
            numAvailMoves += this.bPieces.get(i).getAvailableMoves().size();
        }
        if (numAvailMoves == 0)
            return null;
        List<Point> pieceAndMove = new ArrayList<Point>();
        Random random = new Random();
        int p = random.nextInt(this.bPieces.size());
        while (this.bPieces.get(p).getAvailableMoves().size() == 0){
            p = random.nextInt(this.bPieces.size());
        }
        int m = random.nextInt(this.bPieces.get(p).getAvailableMoves().size());
        pieceAndMove.add(new Point(this.bPieces.get(p).getX(), this.bPieces.get(p).getY()));
        pieceAndMove.add(this.bPieces.get(p).getAvailableMoves().get(m));
        return pieceAndMove;
    }

    public List<Point> getBestMove(int isFlipped){
        this.updatePieces();
        int numAvailMoves = 0;
        List<List<Point>> allAvailableMoves = new ArrayList<>();
        for (int i = 0; i < this.bPieces.size(); i++) {
            this.bPieces.get(i).calAvailableMoves(isFlipped);
            for (int j = 0; j < this.bPieces.get(i).getAvailableMoves().size(); j++) {
                List<Point> move = new ArrayList<>();
                move.add(new Point(this.bPieces.get(i).getX(), this.bPieces.get(i).getY()));
                move.add(this.bPieces.get(i).getAvailableMoves().get(j));
                allAvailableMoves.add(move);
            }
            numAvailMoves += this.bPieces.get(i).getAvailableMoves().size();
        }
        if (numAvailMoves == 0)
            return null;
        int index = 0;
        int currentScore = this.gameFlow.getGd().getBs();
        int bestScore = this.gameFlow.getGd().getBs();
        for (int i = 0; i < allAvailableMoves.size(); i++) {
            Board phantomBoard = this.gameFlow.getBoard().copyBoard();
            phantomBoard.getGrid()[allAvailableMoves.get(i).get(0).getY()][allAvailableMoves.get(i).get(0).getX()].move(
                    allAvailableMoves.get(i).get(1).getX(), allAvailableMoves.get(i).get(1).getY(), isFlipped
            );
            int localBest = this.player.getScore(phantomBoard.getGrid());
            if(bestScore <= localBest){
                bestScore = localBest;
                index = i;
            }
        }
        if (bestScore == currentScore)
            return this.getEvMove(isFlipped);
        return allAvailableMoves.get(index);
    }
    public List<Point> getEvMove(int isFlipped){
        this.updatePieces();
        int numAvailMoves = 0;
        List<List<Point>> allAvailableMoves = new ArrayList<>();
        for (int i = 0; i < this.bPieces.size(); i++) {
            this.bPieces.get(i).calAvailableMoves(isFlipped);
            for (int j = 0; j < this.bPieces.get(i).getAvailableMoves().size(); j++) {
                List<Point> move = new ArrayList<>();
                move.add(new Point(this.bPieces.get(i).getX(), this.bPieces.get(i).getY()));
                move.add(this.bPieces.get(i).getAvailableMoves().get(j));
                allAvailableMoves.add(move);
            }
            numAvailMoves += this.bPieces.get(i).getAvailableMoves().size();
        }
        if (numAvailMoves == 0)
            return null;
        double bestEV = -5;
        for (int i = 0; i < allAvailableMoves.size(); i++) {
            double localBest = this.gameFlow.getBoard().getGrid()[allAvailableMoves.get(i).get(0).getY()][allAvailableMoves.get(i).get(0).getX()].getEvTable()[allAvailableMoves.get(i).get(1).getY()][allAvailableMoves.get(i).get(1).getX()];
            if(bestEV <= localBest){
                bestEV = localBest;
            }
        }
        List<Integer> allBestEv = new ArrayList<>();
        for (int i = 0; i < allAvailableMoves.size(); i++) {
            double localBest = this.gameFlow.getBoard().getGrid()[allAvailableMoves.get(i).get(0).getY()][allAvailableMoves.get(i).get(0).getX()].getEvTable()[allAvailableMoves.get(i).get(1).getY()][allAvailableMoves.get(i).get(1).getX()];
            if (localBest == bestEV)
                allBestEv.add(i);
        }
        Random random = new Random();
        int r = random.nextInt(allBestEv.size());
        return allAvailableMoves.get(allBestEv.get(r));
    }
    public void updatePieces(){
        this.wPieces.clear();
        this.bPieces.clear();
        for (int i = 0; i < gameFlow.getBoard().getGrid().length; i++) {
            for (int j = 0; j < gameFlow.getBoard().getGrid().length; j++) {
                if (gameFlow.getBoard().getGrid()[i][j] != null) {
                    if (gameFlow.getBoard().getGrid()[i][j].getColor() == 1)
                        this.wPieces.add(gameFlow.getBoard().getGrid()[i][j]);
                    else
                        this.bPieces.add(gameFlow.getBoard().getGrid()[i][j]);
                }
            }
        }
    }
}
