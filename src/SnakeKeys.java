import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SnakeKeys extends KeyAdapter {
	public static int[] keys = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER, KeyEvent.VK_P};
	//stores hotkeys, goes L, R, U, D, start, pause
	SnakeSnake snake;
	Snake snkae;
	SnakeBoard board;
	
	//constructor
	public SnakeKeys(SnakeSnake snek, Snake snaek, SnakeBoard boadr) {
		snake = snek;
		snkae = snaek;
		board = boadr;
	}
	
	//key listener do things
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == keys[0]) {
			snake.turn(SnakeDirection.LEFT);
		} else if (key == keys[1]) {
			snake.turn(SnakeDirection.RIGHT);
		} else if (key == keys[2]) {
			snake.turn(SnakeDirection.UP);
		} else if (key == keys[3]) {
			snake.turn(SnakeDirection.DOWN);
		} else if (key == keys[4]) {
			if (snkae.getState() == SnakeGameState.STARTING) {
				snkae.setState(SnakeGameState.INGAME);
				board.initializeGame();
			}
		} else if (key == keys[5]) {
			if (snkae.getState() == SnakeGameState.INGAME) {
				snkae.setState(SnakeGameState.PAUSED);
			} else if (snkae.getState() == SnakeGameState.PAUSED) {
				snkae.setState(SnakeGameState.INGAME);
			}
		}
	}
}
