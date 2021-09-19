import java.util.List;

public class Player {
    private int color;
    private int score;

    public Player(int color){this.color = color;}
    public int getScore(Piece[][] grid){
        int score = 0;
        int scoreO = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] == null)
                    continue;
                if (grid[i][j].getColor() == this.color)
                    score += grid[i][j].getValue();
                else
                    scoreO += grid[i][j].getValue();
            }
        }
        score -= 1000;
        scoreO -= 1000;
        this.score = score - scoreO;
        return this.score;
    }
}
