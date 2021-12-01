public enum SnakeDirection {
	UP (1),
	LEFT (2),
	DOWN (3),
	RIGHT (0);

	private final int ind;

	SnakeDirection(int direc) {
		this.ind = direc;
	}

	public int getInd() {
		return ind;
	}
	
	public boolean leftTurn(SnakeDirection newd) {
		if (newd.getInd() == ind + 1) {
			return true;
		} else if (ind == 3 && newd.getInd() == 0) {
			return true;
		}
		return false;
	}
	
	public boolean rightTurn(SnakeDirection newd) {
		if (newd.getInd() == ind - 1) {
			return true;
		} else if (ind == 0 && newd.getInd() == 3) {
			return true;
		}
		return false;
	}
	
	public boolean genericTurn(SnakeDirection newd) {
		return leftTurn(newd) || rightTurn(newd);
	}
	
	public boolean notBackwards(SnakeDirection newd) {
		if (ind == newd.getInd()) {
			return true;
		} else if (genericTurn(newd)) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		switch (this) {
		case UP:
			return "UP";
		case LEFT:
			return "LEFT";
		case DOWN:
			return "DOWN";
		case RIGHT:
			return "RIGHT";
		}
		return "";
	}
}
