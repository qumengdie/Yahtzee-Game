package YahtzeeGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class SaveGameServer extends JFrame {
	
	private static int WIDTH = 400;
	private static int HEIGHT = 300;
	
	// Text area for displaying contents
	private JTextArea ta;
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SaveGameServer saveServer = new SaveGameServer();
	}
	
	public SaveGameServer() throws SQLException, ClassNotFoundException {
		super("Save Game Server");
		ta = new JTextArea(10,10);
		JScrollPane sp = new JScrollPane(ta);
		this.add(sp);
		this.setSize(this.WIDTH, this.HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		this.setVisible(true);
		
		try {
	        // Create a server socket
	        ServerSocket serverSocket = new ServerSocket(9000);
	        ta.append("Game Save server started at " + new Date() + '\n');
	        
	        while (true) {
	          // Listen for a new connection request
	          Socket socket = serverSocket.accept();
	          
	          ta.append("Save Game server started serving the client.\n");
	          System.out.println("save 1");
	          
	          ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
	          System.out.println("1");
	          ScoresData scoresData = (ScoresData) objIn.readObject();
	          System.out.println("2");
	          saveGame(scoresData);
	          ta.append("Game saved at " + new Date() + '\n');
	        }
	        
		} catch(IOException ex) {
	        System.err.println(ex);
	    }
		
	}
	
	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener((e) -> System.exit(0));
		menu.add(exitItem);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	
	public void saveGame(ScoresData scoresData) throws SQLException {
		// set up prepared statement to insert data into the data base
		PreparedStatement preparedStatement;
		String insert = "INSERT INTO ScoresData "
				+ "(name, time, rolls, round,"
				+ "aces, twos, threes, fours, fives, sixes,"
				+ "uppScore, uppBonus, uppTotal, "
				+ "toak, foak, fh, ss, ls, ya, chance,"
				+ "yaBonus, lowerTotal, grandTotal,"
				+ "filled0, filled1, filled2, filled3, "
	      		+ "filled4, filled5, filled6, filled7, "
	      		+ "filled8, filled9, filled10, filled11, filled12)"
				
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		//System.out.println("saving game...");
				
		Connection connection = DriverManager.getConnection
			      ("jdbc:sqlite:gamedata.db");
		preparedStatement = connection.prepareStatement(insert);
		
		preparedStatement.setString(1, scoresData.name);
		preparedStatement.setString(2, scoresData.time);
		preparedStatement.setInt(3, scoresData.rolls);
		preparedStatement.setInt(4, scoresData.round);
		
		// set the scores
		for (int i=0; i<19; i++) {
			preparedStatement.setInt(i+5, scoresData.dataTable[i]);
		}
		
		for (int i=0; i<13; i++) {
			preparedStatement.setBoolean(i+24, scoresData.filled[i]);
		}
		
		preparedStatement.execute();
	}
	
}
