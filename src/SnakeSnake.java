import java.util.ArrayList;

public class SnakeSnake {
	private SnakeDirection direction = SnakeDirection.RIGHT;
	private int length = 4;
	private int[][] allPoints = new int[1000][2];
	private ArrayList<SnakeDirection> turnQueue = new ArrayList<SnakeDirection>();
	private boolean turned = false;
	private SnakeDirection prevDirection;
	private Snake snk;
	private int swcounter = 0;
	private int rcounter = 0;

	//constructors
	public SnakeSnake(Snake snek) {
		snk = snek;
		setAllPoints();
	}
	public SnakeSnake(int startLength, Snake snek) {
		snk = snek;
		length = startLength;
		setAllPoints();
	}

	//set up snek
	private void setAllPoints() {
		turnQueue = new ArrayList<SnakeDirection>();
		if (snk.getSelected() == 5) {
			for (int i = 0; i < length; i++) {
				allPoints[i][1] = SnakeBoard.BOARDHEIGHT/2;
				allPoints[i][0] = SnakeBoard.BOARDLENGTH/2 + i;
			}
		} else if (snk.getSelected() == 7) {
			for (int i = 0; i < length; i++) {
				allPoints[i][1] = SnakeBoard.BOARDHEIGHT/2;
				allPoints[i][0] = SnakeBoard.BOARDLENGTH/2 - 2*i + 1;
			}
		} else {
			for (int i = 0; i < length; i++) {
				allPoints[i][1] = SnakeBoard.BOARDHEIGHT/2;
				allPoints[i][0] = SnakeBoard.BOARDLENGTH/2 - i;
			}
		}
	}

	//turn
	public void turn(SnakeDirection newDirec) {
		if (turned) {
			turnQueue.add(newDirec);
		} else {
			if (snk.getState() == SnakeGameState.INGAME) {
				if (snk.getSelected() == 3) {
					if (direction.rightTurn(newDirec)) {
						turned = true;
						prevDirection = direction;
						direction = newDirec;
					}
				} else {
					if (direction.genericTurn(newDirec)) {
						turned = true;
						prevDirection = direction;
						direction = newDirec;
					}
				}
			}
		}
	}

	//set length
	public void setLength(int newLength) {
		length = newLength;
	}

	//get direction
	public SnakeDirection getDirec() {
		return direction;
	}

	//get length
	public int getLength() {
		return length;
	}

	//get all points
	public int[][] getAllPoints() {
		return allPoints;
	}

	//normal moving mechanics
	public void normMove() {
		//copy array
		int[][] iniAllPoints = new int[1000][2];
		for (int i = 0; i < length; i++) {
			iniAllPoints[i][0] = allPoints[i][0];
			iniAllPoints[i][1] = allPoints[i][1];
		}
		for (int i = length - 1; i > 0; i--) {
			allPoints[i][0] = allPoints[i - 1][0];
			allPoints[i][1] = allPoints[i - 1][1];
		}
		if (snk.getSelected() == 5) {
			coralIncrement();
		} else {
			normIncrement();
		}
		//check turns
		if (!turned && turnQueue.size() > 0) {
			turn(turnQueue.get(0));
			turnQueue.remove(0);
		}
		//walls
		for (int[] i : allPoints) {
			i[1] = (i[1] % SnakeBoard.BOARDHEIGHT + SnakeBoard.BOARDHEIGHT) % SnakeBoard.BOARDHEIGHT;
			i[0] = (i[0] % SnakeBoard.BOARDLENGTH + SnakeBoard.BOARDLENGTH) % SnakeBoard.BOARDLENGTH;
		}
	}

	//normal movement
	public void normIncrement() {
		switch (direction) {
		case RIGHT:
			allPoints[0][0]++;
			break;
		case LEFT:
			allPoints[0][0]--;
			break;
		case UP:
			allPoints[0][1]--;
			break;
		case DOWN:
			allPoints[0][1]++;
			break;
		}
	}

	//side movement (for sidewinder)
	public void weirdIncrement() {
		switch (direction) {
		case RIGHT:
			allPoints[0][1]--;
			break;
		case LEFT:
			allPoints[0][1]++;
			break;
		case UP:
			allPoints[0][0]--;
			break;
		case DOWN:
			allPoints[0][0]++;
			break;
		}
	}

	//backwards side movement (for sidewinder)
	public void backWeirdIncrement() {
		switch (direction) {
		case RIGHT:
			allPoints[0][1]++;
			break;
		case LEFT:
			allPoints[0][1]--;
			break;
		case UP:
			allPoints[0][0]++;
			break;
		case DOWN:
			allPoints[0][0]--;
			break;
		}
	}

	//inverted movement (for coral snake)
	public void coralIncrement() {
		switch (direction) {
		case RIGHT:
			allPoints[0][0]--;
			break;
		case LEFT:
			allPoints[0][0]++;
			break;
		case UP:
			allPoints[0][1]++;
			break;
		case DOWN:
			allPoints[0][1]--;
			break;
		}
	}

	//trouser movement (for trouser snake)
	public void trouserMove() {
		int[][] iniAllPoints = new int[1000][2];
		for (int i = 0; i < length; i++) {
			iniAllPoints[i][0] = allPoints[i][0];
			iniAllPoints[i][1] = allPoints[i][1];
		}
		for (int i = length; i > 0; i--) {
			allPoints[i][0] = allPoints[i - 1][0];
			allPoints[i][1] = allPoints[i - 1][1];
		}
		normIncrement();
		if (!turned && turnQueue.size() > 0) {
			turn(turnQueue.get(0));
			turnQueue.remove(0);
		}
		for (int[] i : allPoints) {
			i[1] = (i[1] % SnakeBoard.BOARDHEIGHT + SnakeBoard.BOARDHEIGHT) % SnakeBoard.BOARDHEIGHT;
			i[0] = (i[0] % SnakeBoard.BOARDLENGTH + SnakeBoard.BOARDLENGTH) % SnakeBoard.BOARDLENGTH;
		}
		length++;
	}

	//sidewinder movement (for sidewinder)
	public void sideWinderMove() {
		swcounter++;
		normMove();
		if (swcounter % 10 == 5) {
			weirdIncrement();
		} else if (swcounter % 10 == 0) {
			backWeirdIncrement();
		}
		for (int[] i : allPoints) {
			i[1] = (i[1] % SnakeBoard.BOARDHEIGHT + SnakeBoard.BOARDHEIGHT) % SnakeBoard.BOARDHEIGHT;
			i[0] = (i[0] % SnakeBoard.BOARDLENGTH + SnakeBoard.BOARDLENGTH) % SnakeBoard.BOARDLENGTH;
		}
	}

	//ring snake movement (for ring snake)
	public void ringSnakeMove() {
		int[][] iniAllPoints = new int[1000][2];
		for (int i = 0; i < length; i++) {
			iniAllPoints[i][0] = allPoints[i][0];
			iniAllPoints[i][1] = allPoints[i][1];
		}
		if (rcounter % 2 == 0) {
			for (int i = length - 1; i > 0; i--) {
				allPoints[i][0] = allPoints[i-1][0];
				allPoints[i][1] = allPoints[i-1][1];
			}
		}
		normIncrement();
		rcounter++;
		for (int[] i : allPoints) {
			i[1] = (i[1] % SnakeBoard.BOARDHEIGHT + SnakeBoard.BOARDHEIGHT) % SnakeBoard.BOARDHEIGHT;
			i[0] = (i[0] % SnakeBoard.BOARDLENGTH + SnakeBoard.BOARDLENGTH) % SnakeBoard.BOARDLENGTH;
		}
		if (!turned && turnQueue.size() > 0) {
			turn(turnQueue.get(0));
			turnQueue.remove(0);
		}
	}

	//overarching move function
	public void move() {
		turned = false;
		switch (snk.getSelected()) {
		case 0:
			normMove();
			break;
		case 1:
			sideWinderMove();
			break;
		case 2:
			normMove();
			break;
		case 3:
			normMove();
			break;
		case 4:
			normMove();
			break;
		case 5:
			normMove();
			break;
		case 6:
			normMove();
			break;
		case 7:
			ringSnakeMove();
			break;
		case 8:
			normMove();
			break;
		case 9:
			trouserMove();
			break;
		case 10:
			normMove();
			break;
		case 11:
			normMove();
			break;
		}
	}

	//snek grow
	public void lengthen() {
		for (int i = length; i > 0; i--) {
			allPoints[i][0] = allPoints[i-1][0];
			allPoints[i][1] = allPoints[i-1][1];
		}
		length++;
		rcounter++;

		if (snk.getSelected() == 5) {
			coralIncrement();
		} else {
			normIncrement();
		}
		for (int[] i : allPoints) {
			i[1] = (i[1] % SnakeBoard.BOARDHEIGHT + SnakeBoard.BOARDHEIGHT) % SnakeBoard.BOARDHEIGHT;
			i[0] = (i[0] % SnakeBoard.BOARDLENGTH + SnakeBoard.BOARDLENGTH) % SnakeBoard.BOARDLENGTH;
		}
	}

	//get head position
	public int[] getHead() {
		return allPoints[0];
	}
}
