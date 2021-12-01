import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SnakeGame extends JFrame {
	private SnakeBoard baord;
	private Snake snake;
	
	//constructor
	public SnakeGame(SnakeBoard board, Snake snek) {
		this.setTitle(SnakeGameType.valueOf("TEMP" + snek.getSelected()).toString());
		baord = board;
		snake = snek;
		add(board);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	//start
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new SnakeGame(baord, snake);
				frame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent arg0) {
						baord.stopTimer();
						snake.setState(SnakeGameState.DESC);
					}
				});
				frame.setVisible(true);
			}
		});
	}
}
