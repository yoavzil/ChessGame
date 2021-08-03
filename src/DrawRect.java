import javax.swing.*;
import java.awt.*;

public class DrawRect extends JComponent {
    int x;
    int y;
    int width;
    int height;
    int[] color;
    public DrawRect(int x, int y, int width, int[] color){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = width;
        this.color = color;
    }
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Color c = new Color(this.color[0], this.color[1], this.color[2]);
        g2.setColor(c);
        Rectangle rect = new Rectangle(x, y, width, height);
        g2.fill(rect);
    }
}
