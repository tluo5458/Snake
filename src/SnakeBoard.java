import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeBoard extends JPanel implements ActionListener {
	public final static int PIXELSIZE = 25;
	public final static int BOARDLENGTH = Snake.WINDOWLENGTH / PIXELSIZE;
	public final static int BOARDHEIGHT = Snake.WINDOWHEIGHT / PIXELSIZE;
	private int type;
	private Snake snake;
	private SnakeSnake snek;
	private Timer timer;
	private int speed = 70;
	private int score = 0;
	private SnakeBoardArray board;


	//Constructor
	SnakeBoard(int game, Snake snerk) {
		type = game;
		snake = snerk;
		snek = new SnakeSnake(snake);
		board = new SnakeBoardArray(snek, this);
		addKeyListener(new SnakeKeys(snek, snake, this));
		setBackground(Color.BLACK);
		setFocusable(true);
		setPreferredSize(new Dimension(Snake.WINDOWLENGTH, Snake.WINDOWHEIGHT));
	}

	//start game
	public void initializeGame() {
		timer = new Timer(speed, this);
		timer.start();
	}

	//draw board
	public void drawBoard(Graphics g) {
		int i = snake.getSelected()/3;
		int j = snake.getSelected() % 3;
		Color defCol = new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5))));
		Color fudCol = opp(defCol);
		Color wallCol = half(defCol);
		setScore();
		for (int k = 0; k < BOARDLENGTH; k++) {
			for (int l = 0; l < BOARDHEIGHT; l++) {
				if (board.getArray()[k][l] == SnakePoint.FOOD || board.getArray()[k][l] == SnakePoint.FAKEWALL) {
					g.setColor(fudCol);
					g.fillRect(k * PIXELSIZE, l * PIXELSIZE, PIXELSIZE, PIXELSIZE);
				} else if (board.getArray()[k][l] == SnakePoint.WALL) {
					g.setColor(wallCol);
					g.fillRect(k * PIXELSIZE, l * PIXELSIZE, PIXELSIZE, PIXELSIZE);
				}
			}
		}
	}

	//draw entire field
	public void draw(Graphics g) {
		if (snake.getState() == SnakeGameState.INGAME || snake.getState() == SnakeGameState.STARTING) {
			drawBoard(g);
			int i = snake.getSelected()/3;
			int j = snake.getSelected() % 3;
			Color defCol = new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5))));
			Color headCol = emph(defCol);
			g.setColor(headCol);
			//draw head
			g.fillRect(snek.getAllPoints()[0][0]*PIXELSIZE, snek.getAllPoints()[0][1]*PIXELSIZE, PIXELSIZE, PIXELSIZE);
			g.setColor(defCol);
			//draw snek
			for (int m = 1; m < snek.getLength(); m++) {
				g.fillRect(snek.getAllPoints()[m][0]*PIXELSIZE, snek.getAllPoints()[m][1]*PIXELSIZE, PIXELSIZE, PIXELSIZE);
			}
			Toolkit.getDefaultToolkit().sync();
			//draw score
			g.setColor(Color.WHITE);
			g.setFont(new Font("Helvetica", Font.BOLD, 18));
			g.drawString("Score: " + Integer.toString(score), 10, 23);
		} else if (snake.getState() == SnakeGameState.GAME_OVER) {
			//die
			timer.stop();
			endGame(g);
		} else if (snake.getState() == SnakeGameState.PAUSED) {
			//pause
			pauseScreen(g);
		}
	}

	//game over screen
	public void endGame(Graphics g) {
		int i = snake.getSelected()/3;
		int j = snake.getSelected() % 3;
		g.setColor(new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5)))));
		Font font = new Font("Helvetica", Font.BOLD, 26);
		g.setFont(font);
		FontMetrics metrics = getFontMetrics(font);
		g.drawString("Game over", (Snake.WINDOWLENGTH - metrics.stringWidth("Game over"))/2, Snake.WINDOWHEIGHT / 2 - 50);
		String scoreString = "Your score: " + Integer.toString(score);
		g.drawString(scoreString, (Snake.WINDOWLENGTH - metrics.stringWidth(scoreString))/2, Snake.WINDOWHEIGHT / 2 + 20);
		if (snake.setHighScore(snake.getSelected(), score)) {
			String highScoreString = "New high score!";
			g.drawString(highScoreString, (Snake.WINDOWLENGTH - metrics.stringWidth(highScoreString)) / 2, Snake.WINDOWHEIGHT / 2 + 50);
		}
		if (snake.betterThanDev(snake.getSelected(), score)) {
			String beatDev = "Congratulations, you beat the developer!";
			g.drawString(beatDev, (Snake.WINDOWLENGTH - metrics.stringWidth(beatDev))/2, Snake.WINDOWHEIGHT / 2 + 90);
		}
	}

	//stop timer
	public void stopTimer() {
		timer.stop();
	}
	
	//pause screen
	public void pauseScreen(Graphics g) {
		int i = snake.getSelected()/3;
		int j = snake.getSelected() % 3;
		g.setColor(new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5)))));
		Font font = new Font("Helvetica", Font.BOLD, 32);
		g.setFont(font);
		FontMetrics metrics = getFontMetrics(font);
		g.drawString("Paused", (Snake.WINDOWLENGTH - metrics.stringWidth("Paused"))/2, Snake.WINDOWHEIGHT / 2);
	}

	//type
	public int getType() {
		return type;
	}

	//set score
	public void setScore() {
		score = snek.getLength() - 4;
	}

	//timer update
	@Override
	public void actionPerformed(ActionEvent e) {
		if (snake.getState() != SnakeGameState.PAUSED) {
			board.checkColl();
		}
		setName(snake.getState().toString());
		setScore();
		if (snake.getState() == SnakeGameState.INGAME){
			if (board.getDead()) {
				snake.setState(SnakeGameState.GAME_OVER);
			}
		}
		repaint();
	}

	//draw a thing
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	//flip color
	private static Color opp(Color col) {
		Color opp = new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue());
		return opp;
	}

	//half color for wall
	private static Color half(Color col) {
		Color half = new Color(col.getRed()/2, col.getGreen()/2, col.getBlue()/2);
		return half;
	}

	//grayer color for head
	private static Color emph(Color col) {
		Color emph = new Color(change(col.getRed()), change(col.getGreen()), change(col.getBlue()));
		return emph;
	}

	//subfunction for head color
	private static int change(int n) {
		if (n >= 129) {
			return n - 80;
		} else {
			return n + 80;
		}
	}
}
