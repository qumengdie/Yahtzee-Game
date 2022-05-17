package YahtzeeGame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Score {
	public static int acesScore = 0;
	public static int twosScore = 0;
	public static int threesScore = 0;
	public static int foursScore = 0;
	public static int fivesScore = 0;
	public static int sixesScore = 0;
	public static int uppScore = 0;
	public static int uppBonus = 0;
	public static int uppTotal = 0;
	public static int toakScore = 0;
	public static int foakScore = 0;
	public static int fhScore = 0;
	public static int ssScore = 0;
	public static int lsScore = 0;
	public static int chanceScore = 0;
	public static int yaScore = 0;
	public static int yaBouns = 0;
	public static int lowerTotal = 0;
	public static int totalScore = 0;
	public static boolean[] filled = new boolean[13];
	public static Integer[] scores = new Integer[19];
	
	public static int acesScore(List<Integer> dicesNum) {
		for (int x : dicesNum) {
			if (x == 1) acesScore += 1;
		}
		return acesScore;
	}
	
	public static int twosScore(List<Integer> dicesNum) {
		for (int x : dicesNum) {
			if (x == 2) twosScore += 2;
		}
		return twosScore;
	}
	
	public static int threesScore(List<Integer> dicesNum) {
		for (int x : dicesNum) {
			if (x == 3) threesScore += 3;
		}
		return threesScore;
	}
	
	public static int foursScore(List<Integer> dicesNum) {
		for (int x : dicesNum) {
			if (x == 4) foursScore += 4;
		}
		return foursScore;
	}
	
	public static int fivesScore(List<Integer> dicesNum) {
		for (int x : dicesNum) {
			if (x == 5) fivesScore += 5;
		}
		return fivesScore;
	}
	
	public static int sixesScore(List<Integer> dicesNum) {
		for (int x : dicesNum) {
			if (x == 6) sixesScore += 6;
		}
		return sixesScore;
	}
	
	public static int upperSubScore(List<Integer> dicesNum) {
		uppScore = acesScore + twosScore + threesScore + foursScore + fivesScore + sixesScore;
		return uppScore;
	}
	
	public static int upperBonus(List<Integer> dicesNum) {
		if(uppScore >= 63) uppBonus = 35;
		return uppBonus;
	}
	
	public static int uppTotal(List<Integer> dicesNum) {
		uppTotal = uppScore + uppBonus;
		return uppTotal;
	}
	
	public static int toakScore(List<Integer> dicesNum) {
		List<Integer> freq = new ArrayList<>();
		//get the number of the same value of dice
		for (int i=0; i<dicesNum.size(); i++) {
			freq.add(Collections.frequency(dicesNum, i+1));
		}
		
		//if there is a dice value appears at least three times, sum up
		for (int x : freq) {
			if (x >= 3) {
				for (int d : dicesNum) {
					toakScore += d;
				}
			}
		}

		return toakScore;
	}
	
	public static int foakScore(List<Integer> dicesNum) {
		List<Integer> freq = new ArrayList<>();
		for (int i=0; i<dicesNum.size(); i++) {
			freq.add(Collections.frequency(dicesNum, i+1));
		}
		
		for (int x : freq) {
			if (x >= 4) {
				for (int d : dicesNum) {
					toakScore += d;
				}
			}
		}

		return toakScore;
	}
	
	public static int fhScore(List<Integer> dicesNum) {
		List<Integer> freq = new ArrayList<>();
		for (int i=0; i<dicesNum.size(); i++) {
			freq.add(Collections.frequency(dicesNum, i+1));
		}
		
		//three of one number and two of another
		if (freq.contains(2) && freq.contains(3)) {
			fhScore = 25;
		}
		
		//a yahtzee acts as a full house
		if (yaScore(dicesNum) == 50 && upperUsed(filled)) {
			fhScore = 25;
		}
		
		return fhScore;
	}
	
	public static int ssScore(List<Integer> dicesNum) {
		for (int i=1; i<=3; i++) {
			if (dicesNum.contains(i) && dicesNum.contains(i+1) && 
				dicesNum.contains(i+2) && dicesNum.contains(i+3)) {
				ssScore = 30;
			}
		}
		
		//a yahtzee acts as a small straight
		if (yaScore(dicesNum) == 50 && upperUsed(filled)) {
			ssScore = 30;
		}
		
		return ssScore;
	}
	
	public static int lsScore(List<Integer> dicesNum) {
		for (int i=1; i<=2; i++) {
			if (dicesNum.contains(i) && dicesNum.contains(i+1) && 
				dicesNum.contains(i+2) && dicesNum.contains(i+3) && dicesNum.contains(i+4)) {
				lsScore = 40;
			}
		}
		
		//a yahtzee acts as a large straight
		if (yaScore(dicesNum) == 50 && upperUsed(filled)) {
			lsScore = 40;
		}
		
		return lsScore;
	}
	
	public static int yaScore(List<Integer> dicesNum) {
		List<Integer> freq = new ArrayList<>();
		for (int i=0; i<dicesNum.size(); i++) {
			freq.add(Collections.frequency(dicesNum, i+1));
		}
		if (freq.contains(5)) {
			yaScore = 50;
		}
		
		return yaScore;
	}
	
	public static int chanceScore(List<Integer> dicesNum) {
		for (int d : dicesNum) {
			chanceScore += d;
		}
		
		return chanceScore;
	}
	
	public static int yaBonus(List<Integer> dicesNum) {
		if (yaScore == 50) {
			yaBouns = 100;
		} 
		else yaBouns = 0;
		
		return yaBouns;
	}
	
	public static int lowerTotal(List<Integer> dicesNum) {
		lowerTotal = toakScore + foakScore + fhScore + ssScore + lsScore + 
					 chanceScore + yaScore + yaBouns;
		return lowerTotal;
	}
	
	public static int totalScore(List<Integer> dicesNum) {
		totalScore = uppTotal + lowerTotal;
		return totalScore;
	}
	
	//test if the upper section is used
	public static boolean upperUsed(boolean[] filled) {
		boolean uppFilled = filled[0] && filled[1] && filled[2] && filled[3] && 
							filled[4] && filled[5];
		return uppFilled;
	}
	
	public static void allPossibleScore(List<Integer> dicesNum) {
		if (!filled[0]) acesScore = acesScore(dicesNum);
		if (!filled[1]) twosScore = twosScore(dicesNum);
		if (!filled[2]) threesScore = threesScore(dicesNum);
		if (!filled[3]) foursScore = foursScore(dicesNum);
		if (!filled[4]) fivesScore = fivesScore(dicesNum);
		if (!filled[5]) sixesScore = sixesScore(dicesNum);
		if (!filled[6]) toakScore = toakScore(dicesNum);
		if (!filled[7]) foakScore = foakScore(dicesNum);
		if (!filled[8]) fhScore = fhScore(dicesNum);
		if (!filled[9]) ssScore = ssScore(dicesNum);
		if (!filled[10]) lsScore = lsScore(dicesNum);
		if (!filled[11]) yaScore = yaScore(dicesNum);
		if (!filled[12]) chanceScore = chanceScore(dicesNum);
	}
	
	//add all scores to an array
	public static void addScores() {
		scores[0] = acesScore;
		scores[1] = twosScore;
		scores[2] = threesScore;
		scores[3] = foursScore;
		scores[4] = fivesScore;
		scores[5] = sixesScore;
		scores[6] = uppScore;
		scores[7] = uppBonus;
		scores[8] = uppTotal;
		
		scores[9] = toakScore;
		scores[10] = foakScore;
		scores[11] = fhScore;
		scores[12] = ssScore;
		scores[13] = lsScore;
		scores[14] = yaScore;
		scores[15] = chanceScore;
		scores[16] = yaBouns;
		scores[17] = lowerTotal;
		scores[18] = totalScore;
	}
}
