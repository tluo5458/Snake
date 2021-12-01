import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.ImageObserver;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Snake extends FlexiblePictureExplorer implements ImageObserver, ActionListener {
	private SnakeGameState state;
	private JTextArea text;
	private int selected;
	private String input;
	public final static SnakeGameType[] list = new SnakeGameType[12];{ //12 is number of types
		for (int i = 0; i < 12; i++) {
			list[i] = SnakeGameType.valueOf("TEMP" + i);
		}
	}
	private String[] desc = new String[12]; {
		desc[0] = "Regular old snake. Nothing much else to it.";
		desc[1] = "Sidewinders come at all problems in life from strange angles.";
		desc[2] = "Asps are very finicky eaters and consider themselves above eating the \nseeds of fruit.";
		desc[3] = "Vipers are the UPS drivers of the snake kingdom.";
		desc[4] = "Rat Snakes are notoriously unlucky - every time they solve one problem, \ntwo more appear. Even their name is unfortunate.";
		desc[5] = "Coral Snakes tend to come from rough backgrounds and have everything \nbackwards as a result. Truly a snake to be pitied.";
		desc[6] = "Despite its appearance, Hognose snakes have quite the big mouth.";
		desc[7] = "Ringnecks are actually pretty creepy - they're just a series of disconnected \nrings floating ominously.";
		desc[8] = "Mamushis never think things through and have a bad habit of leaving \nenemies in their wake as a result.";
		desc[9] = "Trouser snakes are only interested in one thing: being the biggest snakes \naround.";
		desc[10] = "Rattlesnakes are incapable of telling the difference between food and \nnot-food unless they actually taste it. Gross.";
		desc[11] = "Death Adders are the most useless snakes ever. A Death Subtracter would \nbe a lot more helpful.";
	}
	public static final int WINDOWHEIGHT = 500;
	public static final int WINDOWLENGTH = 800;
	public static final int[] devHighScores = {74, 36, 102, 59, 19, 28, 167, 43, 32, 628, 16, 19};
	private static int[] userHighScores = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	private Picture screen;
	private SnakeGameState prevState = SnakeGameState.MENU;
	private Picture hScreen = new Picture(500,800);
	
	//constructor
	public Snake() {
		super(new Picture(WINDOWHEIGHT, WINDOWLENGTH));
		displayMenu();
	}
	
	//get state
	public SnakeGameState getState() {
		return state;
	}
	
	//change state
	public void setState(SnakeGameState newstate) {
		state = newstate;
	}
	
	//draw string with line breaks
	private void drawString(Graphics2D g, String text, int x, int y) {
		y -= g.getFontMetrics().getHeight();
        for (String line : text.split("\n"))
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
    }
	
	
	//draw menu
	public void displayMenu() {
		state = SnakeGameState.MENU;
		Picture disp = new Picture(500, 800);
		Graphics2D graphics = disp.createGraphics();
		graphics.setBackground(Color.WHITE);
		//game tiles
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				Picture pic = new Picture(100, 200);
				Graphics2D temp = pic.createGraphics();
				temp.setColor(new Color(Math.max(0, 255-60*(i+j)), (int) (255-84*Math.abs(2.5-i-j)), Math.max(0, 255+60*(i+j-5))));
				temp.setFont(new Font("Helvetica", Font.BOLD, 22));
				temp.fillRect(0, 0, 10, 100); 
				temp.fillRect(190, 0, 10, 100);
				temp.fillRect(0, 0, 200, 10);
				temp.fillRect(0, 90, 200, 10);
				int width = temp.getFontMetrics().stringWidth(Snake.list[3*i + j].toString());
				temp.drawString(Snake.list[3*i+j].toString(), 100-width/2, 58);
				graphics.drawImage(pic.getBufferedImage(), 200*i, 100*j, this);
			}
		}
		//bottom portion
		graphics.setColor(Color.BLACK);
		graphics.fillRect(650, 450, 150, 5);
		graphics.fillRect(650, 495, 150, 5);
		graphics.fillRect(650, 450, 5, 50);
		graphics.fillRect(795, 450, 5, 50);
		graphics.setFont(new Font("Helvetica", Font.BOLD, 16));
		graphics.drawString("Save", 704, 480);
		graphics.setFont(new Font("Helvetica", Font.PLAIN, 16));
		graphics.drawString("Controls: Mouse in this menu.", 15, 470);
		graphics.drawString("While in game: ENTER to start, Arrow keys to move, P to pause", 15, 490);

		setImage(disp);
		setTitle("Snake");
		screen = disp;
	}
	
	//clicky click
	@Override
	public void mouseClickedAction(DigitalPicture pict, Pixel pix) {
		Picture pic = (Picture) pict;
		Graphics2D graph = pic.createGraphics();
		int i = pix.getX()/200;
		int j = pix.getY()/100;
		if (j < 3 && (state == SnakeGameState.MENU || state == SnakeGameState.DESC)) {
			//if click one of the games
			Color defColor = new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5))));
			graph.setColor(Color.WHITE);
			graph.fillRect(0, 300, 800, 150);
			graph.setFont(new Font("Helvetica", Font.BOLD, 22));
			graph.setColor(defColor);
			drawString(graph, desc[3*i+j], 25, 340);
			graph.fillRect(300, 400, 200, 70);
			graph.setColor(Color.WHITE);
			graph.fillRect(310, 410, 180, 50);
			graph.setColor(defColor);
			graph.drawString("Play!", 375, 440);
			graph.drawString("High score: " + Integer.toString(userHighScores[3*i + j]), 60, 440);
			graph.drawString("Dev score: " + Integer.toString(devHighScores[3*i + j]), 570, 440);
			setImage(pic);
			screen = pic;
			state = SnakeGameState.DESC;
			selected = 3*i + j;
			prevState = state;
		} else if (pix.getX() >= 650 && pix.getY() >= 450 && (state == SnakeGameState.MENU || state == SnakeGameState.DESC)) {
			//click save
			state = SnakeGameState.SAVE;
			setSaveScreen();
			setImage(hScreen);
		} else if (state == SnakeGameState.SAVE) {
			if (pix.getX() >= 650 && pix.getY() >= 450) {
				//click go back
				setImage(screen);
				state = prevState;
			} else if (pix.getX() >= 100 && pix.getX() <= 300 && pix.getY() >= 100 && pix.getY() <= 300) {
				//click export
				state = SnakeGameState.IE;
				Base36 scores = new Base36(userHighScores, true);
				JFrame frame = new JFrame();
				JPanel panel = new JPanel();
				String score = scores.getBase36();
				Graphics2D bleh = screen.createGraphics();
				graph.setFont(new Font("Dialog", Font.PLAIN, 12));
				FontMetrics metric = graph.getFontMetrics();
				//proper centering with the string
				int p = metric.stringWidth(score);
				int s = metric.stringWidth("Your string is: ");
				int q = metric.stringWidth(" ");
				int r = 2 + (p - s) / 2 / q;
				String setString = "Your string is: \n";
				for (int e = 0; e < r; e++) {
					setString = " " + setString;
				}
				JTextArea text = new JTextArea(setString + scores.getBase36());
				text.setEditable(false);
				panel.add(text);
				//check when closed
				frame.addWindowListener(new WindowListener() {

					@Override
					public void windowActivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowClosed(WindowEvent arg0) {
						// TODO Auto-generated method stub
						state = SnakeGameState.SAVE;
					}

					@Override
					public void windowClosing(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeactivated(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeiconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowIconified(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowOpened(WindowEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
				JLabel label = new JLabel("Close to continue.");
				panel.add(label);
				frame.add(panel);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLocation(200, 200);
				frame.setSize(200, 150);
				frame.setVisible(true);
			} else if (pix.getX() >= 500 && pix.getX() <= 700 && pix.getY() >= 100 && pix.getY() <= 300) {
				//click import
				state = SnakeGameState.IE;
				JFrame frame = new JFrame();
				JPanel panel = new JPanel();
				JLabel label = new JLabel("Enter your code: ");
				text = new JTextArea("Enter your code here");
				JButton button = new JButton();
				button.setText("Submit");
				//check button press
				button.addActionListener(this);
				//check when closed
				frame.addWindowListener(new WindowListener() {

					@Override
					public void windowActivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
						state = SnakeGameState.SAVE;
					}

					@Override
					public void windowClosing(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeactivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeiconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowIconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowOpened(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}
					
				});
				panel.add(label);
				panel.add(text);
				panel.add(button);
				frame.add(panel);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLocation(600, 200);
				frame.setSize(200, 150);
				frame.setVisible(true);
			}
		} else {
			//click play button
			if (state == SnakeGameState.DESC){
				if (pix.getX() >= 300 && pix.getX() <= 500 && pix.getY() >= 400 && pix.getY() <= 470) {
					state = SnakeGameState.STARTING;
					SnakeBoard board = new SnakeBoard(selected, this);
					SnakeGame gaem = new SnakeGame(board, this);
					gaem.start();
				}
			}
		}
		setTitle("Snake");
	}
	
	//get game type
	public int getSelected() {
		return selected;
	}
	
	//override, ignore
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		return false;
	}
	
	//set high score
	public boolean setHighScore(int index, int score) {
		if (score > userHighScores[index]) {
			Graphics2D graph = screen.createGraphics();
			int i = index / 3;
			int j = index % 3;
			Color defColor = new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5))));
			graph.setFont(new Font("Helvetica", Font.BOLD, 22));
			graph.setColor(Color.WHITE);
			graph.fillRect(60, 410, 230, 30);
			graph.setColor(defColor);
			userHighScores[index] = score;
			graph.drawString("High score: " + Integer.toString(userHighScores[3*i + j]), 60, 440);
			setImage(screen);
			setTitle("Snake");
			return true;
		}
		return false;
	}
	
	//show dev+
	public boolean betterThanDev(int index, int score) {
		if (score > devHighScores[index]) {
			int i = index / 3;
			int j = index % 3;
			Color defColor = new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5))));
			Graphics2D graph = screen.createGraphics();
			graph.setColor(defColor);
			graph.setFont(new Font("Helvetica", Font.BOLD, 18));
			graph.drawString("dev+", 200 * i + 80, 100 * j + 30);
			setImage(screen);
			setTitle("Snake");
			return true;
		}
		return false;
	}
	
	//set save screen
	public void setSaveScreen() {
		Graphics2D graph = hScreen.createGraphics();
		graph.setFont(new Font("Helvetica", Font.BOLD, 36));
		graph.setColor(Color.BLACK);
		FontMetrics metrid = graph.getFontMetrics();
		graph.drawString("SAVE", 400 - metrid.stringWidth("SAVE") / 2, 60);
		graph.setFont(new Font("Helvetica", Font.BOLD, 28));
		drawBorderRect(hScreen, 100, 100, 200, 200, 12, Color.RED);
		drawBorderRect(hScreen, 500, 100, 200, 200, 12, Color.BLUE);
		graph.setColor(Color.RED);
		FontMetrics metric = graph.getFontMetrics();
		graph.drawString("Export", 200 - metric.stringWidth("Export") / 2, 210);
		graph.setColor(Color.BLUE);
		graph.drawString("Import", 600 - metric.stringWidth("Import") / 2, 210);
		drawBorderRect(hScreen, 650, 450, 150, 50, 5, Color.BLACK);
		graph.setFont(new Font("Helvetica", Font.BOLD, 16));
		graph.setColor(Color.BLACK);
		graph.drawString("Return", 700, 480);
		drawString(graph, "Clicking export will give you a string that represents your high scores. Save this! \nClicking import will allow you to enter the string and get your high scores back.", 100, 350);
	}
	
	//draw rectangle with border
	public void drawBorderRect(Picture pic, int x, int y, int width, int height, int thick, Color color) {
		Graphics2D graph = pic.createGraphics();
		graph.setColor(color);
		graph.fillRect(x, y, width, thick);
		graph.fillRect(x, y, thick, height);
		graph.fillRect(x, y + height - thick, width, thick);
		graph.fillRect(x + width - thick, y, thick, height);
	}
	
	//set high scores after import
	public void setHighScores() {
		if (prevState == SnakeGameState.DESC) {
			Graphics2D graph = screen.createGraphics();
			int i = selected / 3;
			int j = selected % 3;
			Color defColor = new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5))));
			graph.setFont(new Font("Helvetica", Font.BOLD, 22));
			graph.setColor(Color.WHITE);
			graph.fillRect(60, 410, 230, 30);
			graph.setColor(defColor);
			graph.drawString("High score: " + Integer.toString(userHighScores[3*i + j]), 60, 440);
		}
		for (int k = 0; k < 12; k++) {
			if (userHighScores[k] > devHighScores[k]) {
				int i = k / 3;
				int j = k % 3;
				Color defColor = new Color(Math.max(0, 255-60*(i+j)), (int) Math.max(0, (255-84*Math.abs(2.5-i-j))), Math.min(255, Math.max(0, 255+60*(i+j-5))));
				Graphics2D graph = screen.createGraphics();
				graph.setColor(defColor);
				graph.setFont(new Font("Helvetica", Font.BOLD, 18));
				graph.drawString("dev+", 200 * i + 80, 100 * j + 30);
			}
		}
	}
	
	//button press
	@Override
	public void actionPerformed(ActionEvent arg0) {
		input = text.getText();
		setHighScores(input);
		setHighScores();
	}
	
	//interpret input and convert
	public void setHighScores(String input) {
		Base36 converter = new Base36(input);
		userHighScores = converter.getScore();
	}
	
	//run game
	public static void main(String[] args) {
		new Snake();
	}
	
}
