public class Base36 {
	private int[] threeSixArray;
	private int[] tenArray;
	private int[] scoreArray;
	private int[][] pow36 = new int[24][36];
	public String base36 = "";

	//c o n s t r u c t o r s
	//sets everything given score array or split score array
	public Base36(int[] scores, boolean isScore) {
		setPow36();
		if (isScore) {
			scoreArray = scores;
			tenArray = new int[36];
			convertBCD();
		} else {
			tenArray = scores;
		}
		threeSixArray = new int[24];
		convertBase36();
		setb36();
	}

	//sets everything given base36 string
	public Base36(String str) {
		setPow36();
		if (checkString(str)) {
			base36 = str;
		} else {
			base36 = "0022vsne8vw3qv0stm5tc0tl";
		}
		threeSixArray = new int[24];
		convertFromStr();
		tenArray = new int[36];
		convertBase10();
		scoreArray = new int[12];
		convertScore();
	}

	//check if string is valid
	public boolean checkString(String str) {
		String[] array = new String[24];
		int[] nums = new int[24];
		int[] max = {1, 1, 1, 12, 5, 4, 20, 24, 7, 30, 22, 25, 10, 13, 9, 7, 11, 21, 19, 11, 2, 22, 26, 34};
		if (str.length() != 24) {
			return false;
		} else {
			for (int i = 0; i < 24; i++) {
				array[i] = str.substring(i, i+1);
			}
			for (int i = 0; i < 24; i++) {
				nums[i] = Integer.valueOf(array[i], 36);
			}
			if (isLarger(max, nums)) {
				return true;
			} else {
				return false;
			}
		}
	}



	//c o n v e r s i o n s
	//base 36 array to string
	public void setb36() {
		for (int i : threeSixArray) {
			base36 = base36 + Integer.toString(i,36);
		}
	}
	//string to base36 array
	public void convertFromStr() {
		for (int i = 0; i < 24; i++) {
			String bleh = base36.substring(i,i+1);
			threeSixArray[i] = Integer.valueOf(bleh,36);
		}
	}

	//base 36 array to base 10 array
	public void convertBase10() {
		for (int i = 0; i < 36; i++) {
			tenArray[i] = 0;
		}
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < threeSixArray[i]; j++) {
				tenArray = addArrays(tenArray, pow36[23-i]);
			}
		}
	}
	//base 10 array to base 36 array
	public void convertBase36() {
		for (int i = 23; i >= 0; i--) {
			while (isLarger(tenArray,pow36[i])) {
				tenArray = subtractArrays(tenArray, pow36[i]);
				threeSixArray[23-i]++;
			}
		}
	}

	//split score to score
	public void convertScore() {
		for (int i = 0; i < 12; i++) {
			scoreArray[i] = 100 * tenArray[3*i] + 10 * tenArray[3*i + 1] + tenArray[3*i + 2] - 1;
		}
	}
	//score to split score
	public void convertBCD() {
		for (int i = 0; i < 12; i++) {
			int k = scoreArray[i];
			k++;
			tenArray[3*i] = k / 100;
			tenArray[3*i + 1] = (k / 10) % 10;
			tenArray[3*i + 2] = k % 10;
		}
	}



	//m a t h
	//add 2 arrays with same size, doesn't work with different sizes
	public static int[] addArrays(int[] one, int[] two) {
		int[] rawArray = new int[one.length];
		for (int i = 0; i < one.length; i++) {
			rawArray[i] = one[i] + two[i];
		}
		for (int i = one.length - 1; i > 0; i--) {
			rawArray[i-1] += rawArray[i] / 10;
			rawArray[i] = rawArray[i] % 10;
		}
		return rawArray;
	}

	//subtract first array minus second array, doesn't work with different sizes
	public static int[] subtractArrays(int[] one, int[] two) {
		int[] rawArray = new int[one.length];
		for (int i = 0; i < one.length; i++) {
			rawArray[i] = one[i] - two[i];
		}
		for (int i = 1; i < one.length; i++) {
			if (rawArray[i] < 0) {
				rawArray[i-1]--;
				rawArray[i] += 10;
			}
		}

		return rawArray;
	}

	//multiply arrays
	public static int[] multArrays(int[] one, int[] two) {
		int length = one.length;
		int otherLength = two.length;
		int[][] multArray = new int[length][otherLength];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < otherLength; j++) {
				multArray[i][j] = one[i]*two[j];
			}
		}
		int[] rawArray = addDiagonals(multArray);
		int[] paddedArray = new int[length + otherLength];
		for (int i = rawArray.length - 1; i >= 0; i--) {
			paddedArray[i + length + otherLength - rawArray.length] = rawArray[i];
		}
		for (int i = 0; i < length + otherLength - rawArray.length; i++) {
			paddedArray[i] = 0;
		}
		for (int i = paddedArray.length - 1; i > 0; i--) {
			paddedArray[i-1] += (paddedArray[i] / 10);
			paddedArray[i] = paddedArray[i] % 10;
		}
		int lastDig = paddedArray[0];
		int lastDigIndex = 0;
		while (lastDig == 0) {
			lastDigIndex++;
			lastDig = paddedArray[lastDigIndex];
			if (lastDig == 0 && lastDigIndex == length + otherLength - 1) {
				lastDig = 1;
			}
		}
		int[] finalDigs = new int[length + otherLength - lastDigIndex];
		for (int i = 0; i < finalDigs.length; i++) {
			finalDigs[i] = paddedArray[i + lastDigIndex];
		}
		return finalDigs;
	}

	//subfunction of addDiagonals
	public static int addDiagonal(int[][] lattice, int topSqCol, int topSqRow) {
		int finalSum = 0;
		while (topSqCol >= 0 && topSqRow < lattice.length) {
			finalSum += lattice[topSqRow][topSqCol];
			topSqRow++;
			topSqCol--;
		}
		return finalSum;
	}

	//subfunction of multiply
	public static int[] addDiagonals(int[][] lattice) {
		int[] finalArray = new int[lattice.length + lattice[0].length - 1];
		for (int i = 0; i < lattice[0].length; i++) {
			finalArray[i] = addDiagonal(lattice, i, 0);
		}
		for (int i = lattice[0].length; i < finalArray.length; i++) {
			finalArray[i] = addDiagonal(lattice, lattice[0].length - 1, i - lattice[0].length + 1);
		}

		return finalArray;
	}



	//m i s c
	//set powers of 36 array
	public void setPow36() {
		for (int i = 0; i < pow36.length; i++) {
			int[] curr = power36(i);
			for (int j = 0; j < curr.length; j++) {
				pow36[i][35-j] = curr[curr.length - j - 1];
			}
		}
	}
	
	//check which array is larger number, true if one >= two
	public static boolean isLarger(int[] one, int[] two) {
		if (one.length > two.length) {
			return true;
		} else if (two.length < one.length) {
			return false;
		} else {
			for (int i = 0; i < one.length; i++) {
				if (one[i] > two[i]) {
					return true;
				} else if (one[i] < two[i]) {
					return false;
				}
			}
		}
		return true;
	}

	//find power of 36
	public static int[] power36(int exp) {
		int[] ts = {3, 6};
		int[] curr = {1};
		for (int i = 0; i < exp; i++) {
			curr = multArrays(curr, ts);
		}
		return curr;
	}



	//g e t t e r s
	//get base 36 string
	public String getBase36() {
		return base36;
	}

	//get power of 36 array
	public int[][] getPow36() {
		return pow36;
	}

	//get base 10 array
	public int[] getTen() {
		return tenArray;
	}

	//get score array
	public int[] getScore() {
		return scoreArray;
	}

	//get base 36 array
	public int[] getTS() {
		return threeSixArray;
	}
}
