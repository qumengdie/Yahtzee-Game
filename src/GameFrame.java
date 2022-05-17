package YahtzeeGame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class GameFrame extends JFrame {
	
	//upper score panel items
	private JPanel uppPanel = new JPanel();
	private JButton acesButton, twosButton, threesButton, foursButton, fivesButton, sixesButton;
	private JLabel upperBonusLabel, upperTotalLabel;
	private JTextArea acesTextArea, twosTextArea, threesTextArea, foursTextArea, fivesTextArea, sixesTextArea,
			upperScoreTextArea,upperBonusTextArea, upperTotalTextArea;
	
	//lower score panel items
	private JPanel lowerPanel = new JPanel();
	
	private JButton toakButton, foakButton, fhButton, ssButton, lsButton, yaButton, chanceButton;
	private JLabel yaBonusLabel, lowerTotalLabel, grandTotalLabel;
	private JTextArea toakTextArea, foakTextArea, fhTextArea, ssTextArea, lsTextArea, yaTextArea, chanceTextArea,
			yaBonusTextArea,lowerTotalTextArea, grandTotalTextArea;
	
	//dice panel items
	private JPanel dicePanel = new JPanel();
	private ImagePanel dice1, dice2, dice3, dice4, dice5;
	private JPanel dicePanel1 = new JPanel();
	private JPanel dicePanel2 = new JPanel();
	private JPanel dicePanel3 = new JPanel();
	private JPanel dicePanel4 = new JPanel();
	private JPanel dicePanel5 = new JPanel();
	private JCheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5; 
	private JButton rollButton = new JButton("Roll Dice");
	public static int rolls = 0, round = 0;
	public static List<ImagePanel> dices = new ArrayList<>();
	public static List<JCheckBox> checkBoxes = new ArrayList<>();
	public static List<Integer> dicesNum = new ArrayList<>();
	
	//player panel items
	private JPanel playerPanel = new JPanel();
	private JLabel playerLabel = new JLabel("Player Name: ");
    public static JTextField playerNameTextField = new JTextField("player", 15);
    public static String playerName = "Player";
	
    // list of all the text areas
    public static List<JTextArea> tas = new ArrayList<>();
	
    
	public static void main(String[] args)
    {  
		GameFrame frame = new GameFrame();  
    }
	
	
	public GameFrame() {
		//set up basic properties of the frame
		super("Yahtzee Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.LIGHT_GRAY);
		setSize(800,650);
		
		//set up the menu bar
		JMenuBar menuBar = new JMenuBar();     
	    setJMenuBar(menuBar);
	    menuBar.add(createMenu());
			    
	    //set up the panels
	    setupPanels();
		
		setVisible(true);
	}
	
	
	//set up the menu
	public JMenu createMenu() {
		//create the menu and menu items
		JMenu menu = new JMenu("Game");
		JMenuItem loadItem = new JMenuItem("Load");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		//add listeners to items
		loadItem.addActionListener(new LoadListener());
		saveItem.addActionListener(new SaveListener());
		exitItem.addActionListener(new ExitActionListener());
		
		//add items to the menu
		menu.add(loadItem);
		menu.add(saveItem);
		menu.add(exitItem);
		
		return menu;
	}

	//menu items' listeners
	class LoadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// connect to the load server
				Socket socket = new Socket("localhost", 8080);
				
				System.out.println("Game is loading...");
				
				// Load data from the server
				ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
				ScoresData loadData = (ScoresData) fromServer.readObject();
				
				playerName = loadData.name;
				rolls = loadData.rolls;
				round = loadData.round;
				
				Score.acesScore = loadData.dataTable[0];
				Score.twosScore = loadData.dataTable[1];
				Score.threesScore = loadData.dataTable[2];
				Score.foursScore = loadData.dataTable[3];
				Score.fivesScore = loadData.dataTable[4];
				Score.sixesScore = loadData.dataTable[5];
				Score.uppScore = loadData.dataTable[6];
				Score.uppBonus = loadData.dataTable[7];
				Score.uppTotal = loadData.dataTable[8];
				Score.toakScore = loadData.dataTable[9];
				Score.foakScore = loadData.dataTable[10];
				Score.fhScore = loadData.dataTable[11];
				Score.ssScore = loadData.dataTable[12];
				Score.lsScore = loadData.dataTable[13];
				Score.yaScore = loadData.dataTable[14];
				Score.chanceScore = loadData.dataTable[15];
				Score.yaBouns = loadData.dataTable[16];
				Score.lowerTotal = loadData.dataTable[17];
				Score.totalScore = loadData.dataTable[18];
				
				Score.filled = loadData.filled;
				updateTotalScores();
				showRealScores();
				
			} catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	class SaveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// connect to the save server
				Socket socket = new Socket("localhost", 9000);
				
				// pass data to the server
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ScoresData saveData = new ScoresData();
				out.writeObject(saveData);
				out.flush();
				out.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	class ExitActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
	
	//set up the panels
	public void setupPanels() {
		JPanel scorePanel = new JPanel();
		scorePanel.setBorder(BorderFactory.createTitledBorder("Your Score: "));
		
		scorePanel.add(setUpUpperScorePannel(), BorderLayout.WEST);
		scorePanel.add(setUpLowerScorePannel(), BorderLayout.EAST);
		add(scorePanel, BorderLayout.CENTER);
		add(setUpDicePanel(), BorderLayout.NORTH);
		add(setUpPlayerPanel(), BorderLayout.SOUTH);
	}
	
	//set up the player panel
	public JPanel setUpPlayerPanel() {
		playerPanel.add(playerLabel);
		playerPanel.add(playerNameTextField);
		playerName = playerNameTextField.getText();
		return playerPanel;
	}
	
	public static String getPlayerName() {
		return playerNameTextField.getText();
	}
	
	//set up the upper score panel
	public JPanel setUpUpperScorePannel() {
		//create buttons and labels of upper section types
		acesButton = new JButton("Aces");
		twosButton = new JButton("Twos");
		threesButton = new JButton("Threes");
		foursButton = new JButton("Fours");
		fivesButton = new JButton("Fives");
		sixesButton = new JButton("Sixes");
		upperBonusLabel = new JLabel("Upper Section Bonus");
		upperTotalLabel = new JLabel("Upper Total");
		
		//add listeners to the items
		acesButton.addActionListener(new AcesListener());
		twosButton.addActionListener(new TwosListener());
		threesButton.addActionListener(new ThreesListener());
		foursButton.addActionListener(new FoursListener());
		fivesButton.addActionListener(new FivesListener());
		sixesButton.addActionListener(new SixesListener());
		
		
		//set up upper section scores' text areas
		acesTextArea = new JTextArea();
		twosTextArea = new JTextArea();
		threesTextArea = new JTextArea();
		foursTextArea = new JTextArea();
		fivesTextArea = new JTextArea();
		sixesTextArea = new JTextArea();
		upperScoreTextArea = new JTextArea();
		upperBonusTextArea = new JTextArea();
		upperTotalTextArea = new JTextArea();
		
		acesTextArea.setEditable(false);
		twosTextArea.setEditable(false);
		threesTextArea.setEditable(false);
		foursTextArea.setEditable(false);
		fivesTextArea.setEditable(false);
		sixesTextArea.setEditable(false);
		upperBonusTextArea.setEditable(false);
		upperTotalTextArea.setEditable(false);
		
		upperBonusTextArea.setText("   0");
		upperTotalTextArea.setText("   0");
		
		tas.add(acesTextArea);  tas.add(twosTextArea);
		tas.add(threesTextArea);  tas.add(foursTextArea);
		tas.add(fivesTextArea);  tas.add(sixesTextArea);
		tas.add(upperScoreTextArea);
		tas.add(upperBonusTextArea);  tas.add(upperTotalTextArea);
		
		
		//set up panel layout
		uppPanel.setLayout(new GridLayout(8,2,10,10));
		uppPanel.setBorder(BorderFactory.createTitledBorder("Upper Section Scores: "));
		
		//add items to the panel
		uppPanel.add(acesButton);
		uppPanel.add(acesTextArea);
		uppPanel.add(twosButton);
		uppPanel.add(twosTextArea);
		uppPanel.add(threesButton);
		uppPanel.add(threesTextArea);
		uppPanel.add(foursButton);
		uppPanel.add(foursTextArea);
		uppPanel.add(fivesButton);
		uppPanel.add(fivesTextArea);
		uppPanel.add(sixesButton);
		uppPanel.add(sixesTextArea);
		uppPanel.add(upperBonusLabel);
		uppPanel.add(upperBonusTextArea);
		uppPanel.add(upperTotalLabel);
		uppPanel.add(upperTotalTextArea);
		
		return uppPanel;
	}
	
	//update total scores on every click of button
	public void updateTotalScores() {
		Score.upperSubScore(dicesNum);
		upperBonusTextArea.setText("   " + Score.upperBonus(dicesNum));
		upperTotalTextArea.setText("   " + Score.uppTotal(dicesNum));
		yaBonusTextArea.setText("   " + Score.yaBonus(dicesNum));
		lowerTotalTextArea.setText("   " + Score.lowerTotal(dicesNum));
		grandTotalTextArea.setText("   " + Score.totalScore(dicesNum));
		
		//add scores to the scores array on every button click
		Score.addScores();
	}
	
	//reset dices for every round
	public void resetDices() {
		for (ImagePanel d : dices) {
			d.setDice("die1.png");
		}
		for (JCheckBox cb: checkBoxes) {
			cb.setSelected(false);
		}
	}
	
	//track the round of game
	public void newRound() {
		rolls = 0;
		round++;
		if (round == 13) {
			JOptionPane.showMessageDialog(null, "Your total score is: " + Score.totalScore(dicesNum));
		}
	}
	
	// show the real current scores
	public void showRealScores() {
		for (int i=0; i<19; i++) {
			tas.get(i).setText("   " + Score.scores[i]);
		}
	}
	
	
	//upper section listeners
	class AcesListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[0] = true;
			//acesTextArea.setText("   " + Score.acesScore(dicesNum));
			acesButton.setEnabled(false);
			
			Score.acesScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class TwosListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[1] = true;
			//twosTextArea.setText("   " + Score.twosScore(dicesNum));
			twosButton.setEnabled(false);

			Score.twosScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class ThreesListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[2] = true;
			//threesTextArea.setText("   " + Score.threesScore(dicesNum));
			threesButton.setEnabled(false);

			Score.threesScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class FoursListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[3] = true;
			//foursTextArea.setText("   " + Score.foursScore(dicesNum));
			foursButton.setEnabled(false);

			Score.foursScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class FivesListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[4] = true;
			//fivesTextArea.setText("   " + Score.fivesScore(dicesNum));
			fivesButton.setEnabled(false);

			Score.fivesScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class SixesListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[5] = true;
			//sixesTextArea.setText("   " + Score.sixesScore(dicesNum));
			sixesButton.setEnabled(false);

			Score.sixesScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	
	//set up the lower score panel
	public JPanel setUpLowerScorePannel() {
		//create buttons and labels of lower section types
		toakButton = new JButton("Three Of A Kind");
		foakButton = new JButton("Four Of A Kind");
		fhButton = new JButton("Full House");
		ssButton = new JButton("Small Straight");
		lsButton = new JButton("Large Straight");
		yaButton = new JButton("Yahtzee");
		chanceButton = new JButton("Chance");
		yaBonusLabel = new JLabel("Yahtzee Bounus");
		lowerTotalLabel = new JLabel("Lower Section Total");
		grandTotalLabel = new JLabel("Grand Total");
		
		
		//add listeners for buttons
		toakButton.addActionListener(new ThreeOfAKindListener());
		foakButton.addActionListener(new FourOfAKindListener());
		fhButton.addActionListener(new FullHouseListener());
		ssButton.addActionListener(new SmallStraightListener());
		lsButton.addActionListener(new LargeStraightListener());
		yaButton.addActionListener(new YahtzeeListener());
		chanceButton.addActionListener(new ChanceListener());
		
		//set up scores' text areas
		toakTextArea = new JTextArea();
		foakTextArea = new JTextArea();
		fhTextArea = new JTextArea();
		ssTextArea = new JTextArea();
		lsTextArea = new JTextArea();
		yaTextArea = new JTextArea();
		chanceTextArea = new JTextArea();
		yaBonusTextArea = new JTextArea();
		lowerTotalTextArea = new JTextArea();
		grandTotalTextArea = new JTextArea();
		
		toakTextArea.setEditable(false);
		foakTextArea.setEditable(false);
		fhTextArea.setEditable(false);
		ssTextArea.setEditable(false);
		lsTextArea.setEditable(false);
		yaTextArea.setEditable(false);
		chanceTextArea.setEditable(false);
		yaBonusTextArea.setEditable(false);
		lowerTotalTextArea.setEditable(false);
		grandTotalTextArea.setEditable(false);
		
		yaBonusTextArea.setText("   0");
		lowerTotalTextArea.setText("   0");
		grandTotalTextArea.setText("   0");
		
		tas.add(toakTextArea);  tas.add(foakTextArea);
		tas.add(fhTextArea);  tas.add(ssTextArea);
		tas.add(lsTextArea);  tas.add(yaTextArea);
		tas.add(chanceTextArea);  tas.add(yaBonusTextArea);
		tas.add(lowerTotalTextArea);  tas.add(grandTotalTextArea);
		
		//set up the layout of the lower section scores' panel
		lowerPanel.setLayout(new GridLayout(10,2,10,10));
		lowerPanel.setBorder(BorderFactory.createTitledBorder("Lower Section Scores: "));
		
		//add items to the panel
		lowerPanel.add(toakButton);
		lowerPanel.add(toakTextArea);
		lowerPanel.add(foakButton);
		lowerPanel.add(foakTextArea);
		lowerPanel.add(fhButton);
		lowerPanel.add(fhTextArea);
		lowerPanel.add(ssButton);
		lowerPanel.add(ssTextArea);
		lowerPanel.add(lsButton);
		lowerPanel.add(lsTextArea);
		lowerPanel.add(yaButton);
		lowerPanel.add(yaTextArea);
		lowerPanel.add(chanceButton);
		lowerPanel.add(chanceTextArea);
		lowerPanel.add(yaBonusLabel);
		lowerPanel.add(yaBonusTextArea);
		lowerPanel.add(lowerTotalLabel);
		lowerPanel.add(lowerTotalTextArea);
		lowerPanel.add(grandTotalLabel);
		lowerPanel.add(grandTotalTextArea);
		
		return lowerPanel;
	}
	
	//lower section listeners
	class ThreeOfAKindListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[6] = true;
			//toakTextArea.setText("   " + Score.toakScore(dicesNum));
			toakButton.setEnabled(false);

			Score.toakScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class FourOfAKindListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[7] = true;
			//foakTextArea.setText("   " + Score.foakScore(dicesNum));
			foakButton.setEnabled(false);

			Score.foakScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class FullHouseListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[8] = true;
			//fhTextArea.setText("   " + Score.fhScore(dicesNum));
			fhButton.setEnabled(false);

			Score.fhScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class SmallStraightListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[9] = true;
			//ssTextArea.setText("   " + Score.ssScore(dicesNum));
			ssButton.setEnabled(false);

			Score.ssScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	

	class LargeStraightListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[10] = true;
			//lsTextArea.setText("   " + Score.lsScore(dicesNum));
			lsButton.setEnabled(false);

			Score.lsScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class YahtzeeListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[11] = true;
			//yaTextArea.setText("   " + Score.yaScore(dicesNum));
			yaButton.setEnabled(false);

			Score.yaScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	class ChanceListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			Score.filled[12] = true;
			//chanceTextArea.setText("   " + Score.chanceScore(dicesNum));
			chanceButton.setEnabled(false);

			Score.chanceScore(dicesNum);
			updateTotalScores();
			showRealScores();
			
			resetDices();
			newRound();
		}
	}
	
	
	//set up the dice panel
	public JPanel setUpDicePanel() {
		//set up panels' layout
		dicePanel.setLayout(new GridLayout(1,6));
		dicePanel1.setLayout(new BoxLayout(dicePanel1, BoxLayout.Y_AXIS));
		dicePanel2.setLayout(new BoxLayout(dicePanel2, BoxLayout.Y_AXIS));
		dicePanel3.setLayout(new BoxLayout(dicePanel3, BoxLayout.Y_AXIS));
		dicePanel4.setLayout(new BoxLayout(dicePanel4, BoxLayout.Y_AXIS));
		dicePanel5.setLayout(new BoxLayout(dicePanel5, BoxLayout.Y_AXIS));
		
		//create 5 dices
		dice1 = new ImagePanel("die1.png");
		dice2 = new ImagePanel("die1.png");
		dice3 = new ImagePanel("die1.png");
		dice4 = new ImagePanel("die1.png");
		dice5 = new ImagePanel("die1.png");
		dices.add(dice1); 
		dices.add(dice2);
		dices.add(dice3); 
		dices.add(dice4);
		dices.add(dice5);
		for (int i=0; i<5; i++) {
			dicesNum.add(0);
		}
		
		//create check boxes to keep the wanted dices
		checkBox1 = new JCheckBox("keep");
		checkBox2 = new JCheckBox("keep");
		checkBox3 = new JCheckBox("keep");
		checkBox4 = new JCheckBox("keep");
		checkBox5 = new JCheckBox("keep");
		checkBox1.setAlignmentX((float) 0.7);
		checkBox2.setAlignmentX((float) 0.7);
		checkBox3.setAlignmentX((float) 0.7);
		checkBox4.setAlignmentX((float) 0.7);
		checkBox5.setAlignmentX((float) 0.7);
		checkBoxes.add(checkBox1);
		checkBoxes.add(checkBox2);
		checkBoxes.add(checkBox3);
		checkBoxes.add(checkBox4);
		checkBoxes.add(checkBox5);
		
		//add listeners
		rollButton.addActionListener(new RollDicesListener());
		for (JCheckBox box : checkBoxes) {
			box.addActionListener(new checkBoxListener());
		}
		
		//add items to the dice panel
		dicePanel1.add(dice1); 
		dicePanel1.add(checkBox1);
		dicePanel2.add(dice2); 
		dicePanel2.add(checkBox2);
		dicePanel3.add(dice3); 
		dicePanel3.add(checkBox3);
		dicePanel4.add(dice4); 
		dicePanel4.add(checkBox4);
		dicePanel5.add(dice5); 
		dicePanel5.add(checkBox5);
		
		dicePanel.add(dicePanel1);
		dicePanel.add(dicePanel2);
		dicePanel.add(dicePanel3);
		dicePanel.add(dicePanel4);
		dicePanel.add(dicePanel5);
		dicePanel.add(rollButton);
		
		return dicePanel;
	}
	
	class checkBoxListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//prohibit selecting "keep" for the first roll
			if (rolls == 0) {
				JOptionPane.showMessageDialog(null, "You can't keep any dice for the first roll. Please click 'Roll Dice' for this roll.");
				for (JCheckBox box : checkBoxes) {
					box.setSelected(false);
				}
			}
		}
		
	}
	
	class RollDicesListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			//in one round, when rolls taken is less or equal to 3
			if (rolls < 3) {
				rolls++;
				int delay = 100;
				
				ActionListener taskPerformer = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//roll the unselected dices and record dice numbers
						for (int i=0; i<dices.size(); i++) {
							if (!checkBoxes.get(i).isSelected()) {
								int d = dices.get(i).roll();
								dicesNum.set(i, d);
							}
						}
					}
				};
				
				Timer t = new Timer(delay, taskPerformer);
				t.start();
				
				class rollStopListener implements ActionListener {
					@Override
					public void actionPerformed(ActionEvent e) {
						t.stop();
					}
				}
				int randomTime = (int)(Math.random() * 2000);
				Timer stop = new Timer(randomTime, new rollStopListener());
				stop.start();
			}
			else {
				JOptionPane.showMessageDialog(null,"You only have total of 3 rolls in one round!\nPlease choose a type to get your score!");
			}
		}
	}
	
	
	
}
