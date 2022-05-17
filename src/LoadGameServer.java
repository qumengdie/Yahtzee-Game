package YahtzeeGame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LoadGameServer extends JFrame {
	
	private JPanel loadCBPanel = new JPanel();
	private JComboBox<String> cb = new JComboBox<>();
	
	private JPanel loadButPanel = new JPanel();
	private JButton loadButton = new JButton("Load Game");
	
	private Socket socket;
	
	
	public static void main(String[] args) throws SQLException {
		LoadGameServer loadServer = new LoadGameServer();
	}
	
	
	public LoadGameServer() throws SQLException {
		super("Load Game Server");
		this.setSize(400, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		createMenu();
		this.setVisible(true);
		
		System.out.println("Load game server ready.");
	
		try {
	        // Create a server socket
	        ServerSocket serverSocket = new ServerSocket(8080);
	        
	        while (true) {
	          // Listen for a new connection request
	          socket = serverSocket.accept();
	          
		  System.out.println("Load Server started serving for the client.");
		  LoadGame();
	        }
	        
		} catch(IOException ex) {
	        System.err.println(ex);
	    }
	}
	
	private void LoadGame() {
		try {
			setUpPanel();
			this.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	private void setUpPanel() throws SQLException {
		// select name and time for the user to choose which one to load
		String query = "SELECT name, time FROM ScoresData";
		
		Connection connection = DriverManager.getConnection
			      ("jdbc:sqlite:gamedata.db");
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		
		// add names and times to the combo box
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			cb.addItem(rs.getString("name") + "," + rs.getString("time"));
		}
		
		// add the combo box to the panel
		loadCBPanel.add(cb);
		//add the button to the panel
		loadButton.addActionListener(new LoadListener());
		loadButPanel.add(loadButton);
		// add panels
		this.add(loadCBPanel, BorderLayout.NORTH);
		this.add(loadButPanel, BorderLayout.SOUTH);
	}
	
	public class LoadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				// get the load choice from the combo box
				String choice = (String) cb.getSelectedItem();
				String[] sp = choice.split(",");
				String time = sp[1];
				
				// get the wanted scores data by time
				String query = "SELECT * FROM ScoresData WHERE time = ?";
				Connection connection = DriverManager.getConnection
					      ("jdbc:sqlite:gamedata.db");
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, time);
				ResultSet rs = preparedStatement.executeQuery();
				
				// get the data from data base
				ScoresData loadData = new ScoresData();
				loadData.name = rs.getString(1);
				loadData.time = rs.getString(2);
				loadData.rolls = rs.getInt(3);
				loadData.round = rs.getInt(4);
				
				for (int i=0; i<19; i++) {
					loadData.dataTable[i] = rs.getInt(i+5);
				}
				
				for (int i=0; i<13; i++) {
					loadData.filled[i] = rs.getBoolean(i+24);
				}
				
				// write all the data to the socket so they'll be pass to the game
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(loadData);
                System.exit(0);
				
			} catch (IOException | SQLException ex) {
				ex.printStackTrace();
			}
			
			
		}
		
	}
	
}
