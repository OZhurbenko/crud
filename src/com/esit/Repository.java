package com.esit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;

import org.json.JSONObject;

public class Repository {
	ConnectionManager conn;
    
	public String getAllEmployees() throws NamingException {
		String str = "";
		try {
			//create a query string
	        String _query = "SELECT * FROM Employee";
	        
	        //create a new Query object
	        conn = new ConnectionManager();
	        
	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);
	        
	        
	        //creating an object to keep a collection of JSONs
	        Collection<JSONObject> employees = new ArrayList<JSONObject>();

	        //Iterating through the Results and filling the jsonObject
	        while (resultSet.next()) {
	          //creating a temporary JSON object and put there a data from the database
	          JSONObject tempJson = new JSONObject();
	          tempJson.put("firstName", resultSet.getString("firstName"));
	          tempJson.put("lastName", resultSet.getString("lastName"));
	          tempJson.put("email", resultSet.getString("email"));
	          tempJson.put("homePhone", resultSet.getString("homePhone"));
	          tempJson.put("cellPhone", resultSet.getString("cellPhone"));
	          tempJson.put("hireDate", resultSet.getDate("hireDate"));
	          tempJson.put("isActive", resultSet.getBoolean("isActive"));
	          tempJson.put("role", resultSet.getString("role"));
	          employees.add(tempJson);
            }
	        
	        //creating a final JSON object
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put("employees", employees);
	        
	        str = "@Produces(\"application/json\") Output: \n\n" + jsonObject;
	        
	        
	      } catch (SQLException e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return str;
	}
}
