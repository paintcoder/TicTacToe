import javax.swing.JFrame;

public class Main{
    public static void main(String[] args){
        JFrame frame = new JFrame();
        
        frame.setBounds(0, 0, 900, 900);
        
        frame.setTitle("Tic Tac Toe");
        
        frame.setResizable(false);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        TicTacToeGame game = new TicTacToeGame();
        frame.add(game);
        frame.setVisible(true);
        
    }
}
