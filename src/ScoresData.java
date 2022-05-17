package YahtzeeGame;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoresData implements Serializable {
	public String name, time;
	public int rolls, round;
	
	public Integer[] dataTable = new Integer[19];
	public boolean[] filled = new boolean[13];
	
	
	public ScoresData() {
		name = GameFrame.getPlayerName();
		
		SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = sformat.format(new Date());
		
		rolls = GameFrame.rolls;
		round = GameFrame.round;
		
		// scores
		for (int i = 0; i < 19; i++) {
			dataTable[i] = Score.scores[i];
		}
		
		for (int i = 0; i < 13; i++) {
			filled[i] = Score.filled[i];
		}
	}
}
