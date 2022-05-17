package YahtzeeGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
	 public static void main(String[] args) throws ClassNotFoundException
	  {
		 Connection connection = null;
	    try
	    {
	    	// create a database connection
	    	connection = DriverManager.getConnection
				      ("jdbc:sqlite:gamedata.db");
	    	
	    	Statement statement = connection.createStatement();
	      
	    	String createTable = "CREATE TABLE ScoresData (name String, time String, "
	      		+ "rolls int, round int,"
	      		+ "aces int, twos int, threes int, fours int, fives int, sixes int, "
	      		+ "uppScore int, uppBonus int, uppTotal int,"
	      		+ "toak int , foak int, fh int, ss int, ls int, ya int, chance int, "
	      		+ "yaBonus int, lowerTotal int, grandTotal int,"
	      		+ "filled0 boolean, filled1 boolean, filled2 boolean, filled3 boolean, "
	      		+ "filled4 boolean, filled5 boolean, filled6 boolean, filled7 boolean, "
	      		+ "filled8 boolean, filled9 boolean, filled10 boolean, filled11 boolean, filled12 boolean)";
	      
	    	statement.executeUpdate(createTable);
	    }
	    catch(SQLException e)
	    {
	    	// if the error message is "out of memory", 
	    	// it probably means no database file is found
	    	System.err.println(e.getMessage());
	    }
	    
	    
	  }
}


