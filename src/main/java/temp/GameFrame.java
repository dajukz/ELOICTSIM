package temp;

/**
 * ELOICTSIM; GameFrame
 *
 * @author youke
 * @version 28/05/2022
 */
import javax.swing.JFrame;

public class GameFrame extends JFrame{

    GameFrame(){
        GamePanel panel = new GamePanel();
        this.add(panel);
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}
