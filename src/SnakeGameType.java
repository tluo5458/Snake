public enum SnakeGameType {
	TEMP0 (0, "Snake"), 
	TEMP1 (1, "Sidewinder"), 
	TEMP2 (2, "Asp"), 
	TEMP3 (3, "Viper"), 
	TEMP4 (4, "Rat Snake"), 
	TEMP5 (5, "Coral Snake"), 
	TEMP6 (6, "Hognose"), 
	TEMP7 (7, "Ringneck"), 
	TEMP8 (8, "Mamushi"), 
	TEMP9 (9, "Trouser"), 
	TEMP10 (10, "Rattlesnake"), 
	TEMP11 (11, "Death Adder");
	
	private int ind;
	private String name;
	
	
	private SnakeGameType(int num, String s) {
		this.ind = num;
		this.name = s;
	}
	public int getInd() {
		return ind;
	}
	public String toString() {
		return name;
	}
}
