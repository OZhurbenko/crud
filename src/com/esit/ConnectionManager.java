package com.esit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class ConnectionManager {
    Context context = null;
    DataSource datasource = null;
    Connection connect = null;
    Statement statement = null;
    ResultSet resultSet;

    public ResultSet executeQuery(String query) throws NamingException {
    	try {
	        // Get the context and create a connection
	      	Context initCtx = new InitialContext();
	      	Context envCtx = (Context) initCtx.lookup("java:comp/env");
	      	datasource = (DataSource)envCtx.lookup("jdbc/esit");
	        connect = datasource.getConnection();

	        // Create the statement to be used to get the results.
	        statement = connect.createStatement();

	        // Execute the query and get the result set.
	        resultSet = statement.executeQuery(query);
	      } catch (SQLException e) {
	    	  e.printStackTrace();
	      } 
		return resultSet;
    }

    public void closeConnection(){
    	try {
        	if(statement != null) {
	          statement.close();
	        }
          	if(connect != null) {
          	  connect.close();
          	}
        } catch (SQLException e) { 
          e.printStackTrace(); 
        }
    }
}
