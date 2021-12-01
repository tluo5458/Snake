import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class SnakeBoardArray {
	private SnakePoint[][] array = new SnakePoint[SnakeBoard.BOARDLENGTH][SnakeBoard.BOARDHEIGHT]; {
		for (SnakePoint[] i : array) {
			for (int j = 0; j < SnakeBoard.BOARDHEIGHT; j++) {
				i[j] = SnakePoint.EMPTY;
			}
		}
	}
	private SnakeSnake snake;
	private SnakeBoard board;
	private boolean isDead = false;
	int fwall = 0;

	//constructor
	public SnakeBoardArray(SnakeSnake snek, SnakeBoard boadr) {
		snake = snek;
		board = boadr;
		setUpArray();
	}

	//pregame setup
	public void setUpArray() {
		if (board.getType() == 11) {
			for (int i = 0; i < SnakeBoard.BOARDLENGTH; i++) {
				for (int j = 0; j < SnakeBoard.BOARDHEIGHT; j++) {
					if (i % 3 == 1 && j % 3 == 2) {
						array[i][j] = SnakePoint.WALL;
					}
				}
			}
		}
		generateFood();
		//generate 3 food for hognose
		if (board.getType() == 6) {
			generateFood();
			generateFood();
		}
	}

	//generic regular food generation
	public void regGen() {
		boolean isIn = true;
		int x = 0;
		int y = 0;
		while (isIn) {
			x = ThreadLocalRandom.current().nextInt(0, SnakeBoard.BOARDLENGTH);
			y = ThreadLocalRandom.current().nextInt(0, SnakeBoard.BOARDHEIGHT);
			isIn = false;
			for (int[] i : snake.getAllPoints()) {
				if (i[0] == x && i[1] == y) {
					isIn = true;
				}
			}
			if (array[x][y] != SnakePoint.EMPTY) {
				isIn = true;
			}
		}
		array[x][y] = SnakePoint.FOOD;
	}

	//ring generation
	public void ringGen() {
		boolean isIn = true;
		int x = 0;
		int y = 0;
		while (isIn) {
			x = ThreadLocalRandom.current().nextInt(0, SnakeBoard.BOARDLENGTH);
			y = ThreadLocalRandom.current().nextInt(0, SnakeBoard.BOARDHEIGHT);
			isIn = false;
			for (int[] i : snake.getAllPoints()) {
				if (i[0] == x && i[1] == y) {
					isIn = true;
				}
			}
			if (array[x][y] != SnakePoint.EMPTY) {
				isIn = true;
			}
			if ((x - y) % 2 == 0) {
				isIn = true;
			}
		}
		array[x][y] = SnakePoint.FOOD;
	}

	//fruit generation for asp
	public void fruitGen() {
		boolean isIn = true;
		int x = 0;
		int y = 0;
		while (isIn) {
			x = ThreadLocalRandom.current().nextInt(1, SnakeBoard.BOARDLENGTH - 1);
			y = ThreadLocalRandom.current().nextInt(1, SnakeBoard.BOARDHEIGHT - 1);
			isIn = false;
			for (int i = x - 1; i < x + 2; i++) {
				for (int j = y - 1; j < y + 2; j++) {
					if (array[i][j] != SnakePoint.EMPTY) {
						isIn = true;
					}
					int[] head = snake.getAllPoints()[0];
					if (head[0] == i && head[1] == j) {
						isIn = true;
					}
				}
			}
		}
		for (int i = x - 1; i < x + 2; i++) {
			for (int j = y - 1; j < y + 2; j++) {
				array[i][j] = SnakePoint.FOOD;
			}
		}
		array[x][y] = SnakePoint.WALL;
	}

	//mult generation for rattlesnake
	public void multGen() {
		int numGen = 0;
		int[][] rand = new int[4][2];
		int x = 0;
		int y = 0;
		while (numGen < 4) {
			boolean isIn = false;
			x = ThreadLocalRandom.current().nextInt(0, SnakeBoard.BOARDLENGTH);
			y = ThreadLocalRandom.current().nextInt(0, SnakeBoard.BOARDHEIGHT);
			int[] head = snake.getAllPoints()[0];
			if (head[0] == x && head[1] == y) {
				isIn = true;
			}
			if (array[x][y] != SnakePoint.EMPTY) {
				isIn = true;
			}
			for (int i = 0; i < numGen; i++) {
				if (x == rand[i][0] && y == rand[i][1]) {
					isIn = true;
				}
			}
			if (!isIn) {
				rand[numGen][0] = x;
				rand[numGen][1] = y;
				numGen++;
			}
		}
		int m = ThreadLocalRandom.current().nextInt(0, 4);
		for (int i = 0; i < rand.length; i++) {
			if (i == m) {
				array[rand[i][0]][rand[i][1]] = SnakePoint.FOOD;
			} else {
				array[rand[i][0]][rand[i][1]] = SnakePoint.FAKEWALL;
			}
		}
	}

	//generate food
	public void generateFood() {
		switch (board.getType()) {
		case 0:
			regGen();
			break;
		case 1:
			regGen();
			break;
		case 2:
			int counter = 0;
			for (SnakePoint[] i : array) {
				for (SnakePoint j : i) {
					if (j == SnakePoint.FOOD) {
						counter++;
					}
				}
			}
			if (counter == 0) {
				fruitGen();
			}
			break;
		case 3:
			regGen();
			break;
		case 4:
			regGen();
			break;
		case 5:
			regGen();
			break;
		case 6:
			regGen();
			break;
		case 7:
			ringGen();
			break;
		case 8:
			regGen();
			break;
		case 9:
			break;
		case 10:
			multGen();
			break;
		case 11:
			regGen();
			break;

		}
	}

	//check collision
	public void checkColl() {
		int x = snake.getAllPoints()[0][0];
		int y = snake.getAllPoints()[0][1];
		SnakePoint currHead = array[x][y];
		if (currHead == SnakePoint.WALL) {
			//hit wall
			isDead = true;
		} else if (currHead == SnakePoint.FAKEWALL) {
			//hit fake wall, for rattlesnake
			snake.move();
			array[x][y] = SnakePoint.WALL;
		} else if (currHead == SnakePoint.FOOD) {
			//eat food
			snake.lengthen();
			array[x][y] = SnakePoint.EMPTY;
			generateFood();
			switch (board.getType()) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				int numGen = 0;
				int[][] rand = new int[2][2];
				int a = 0;
				int b = 0;
				while (numGen < 2) {
					boolean isIn = false;
					a = ThreadLocalRandom.current().nextInt(0, SnakeBoard.BOARDLENGTH);
					b = ThreadLocalRandom.current().nextInt(0, SnakeBoard.BOARDHEIGHT);
					int[] head = snake.getAllPoints()[0];
					if (head[0] == a && head[1] == b) {
						isIn = true;
					}
					if (array[a][b] != SnakePoint.EMPTY) {
						isIn = true;
					}
					if (!isIn) {
						rand[numGen][0] = a;
						rand[numGen][1] = b;
						numGen++;
					}
				}
				for (int[] i : rand) {
					array[i[0]][i[1]] = SnakePoint.WALL;
				}
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				array[x][y] = SnakePoint.WALL;
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
			}
		} else if (board.getType() == 6) {
			//extra case to check hognose fod
			boolean isIn = false;
			for (int i = x - 1; i < x + 2; i++) {
				for (int j = y - 1; j < y + 2; j++) {
					int m = (i % SnakeBoard.BOARDLENGTH + SnakeBoard.BOARDLENGTH) % SnakeBoard.BOARDLENGTH;
					int n = (j % SnakeBoard.BOARDHEIGHT + SnakeBoard.BOARDHEIGHT) % SnakeBoard.BOARDHEIGHT;
					if (array[m][n] == SnakePoint.FOOD) {
						isIn = true;
						snake.lengthen();
						array[m][n] = SnakePoint.EMPTY;
						generateFood();
					}
				}
			}
			if (!isIn) {
				snake.move();
			}
		} else {
			//nothing happen
			snake.move();
		}
		//check self intersections
		int[] head = snake.getAllPoints()[0];
		for (int i = 1; i < snake.getLength(); i++) {
			if (Arrays.equals(head, snake.getAllPoints()[i])) {
				isDead = true;
			}
		}
	}

	//get dead
	public boolean getDead() {
		return isDead;
	}

	//get array
	public SnakePoint[][] getArray() {
		return array;
	}

}
