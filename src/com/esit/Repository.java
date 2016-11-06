package com.esit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;

import org.json.JSONObject;

public class Repository {
	ConnectionManager conn;

	// Get all employees
	public JSONObject getAllEmployees() throws NamingException {
		JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
	        String _query = "SELECT employeeId, "
	        		+ "CONCAT(firstName, ' ', lastName) AS name, "
	        		+ "role, "
	        		+ "email, "
	        		+ "cellPhone, "
	        		+ "hireDate, "
	        		+ "isActive "
	        		+ "FROM Employee";

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
	          tempJson.put("employeeNumber", resultSet.getString("employeeId"));
	          tempJson.put("name", resultSet.getString("name"));
	          tempJson.put("email", resultSet.getString("email"));
	          tempJson.put("cellPhone", resultSet.getString("cellPhone"));
	          tempJson.put("hireDate", resultSet.getDate("hireDate"));
	          tempJson.put("isActive", resultSet.getBoolean("isActive"));
	          tempJson.put("role", resultSet.getString("role"));
	          employees.add(tempJson);
            }

	        //creating a final JSON object
	        jsonObject.put("employees", employees);

	      } catch (SQLException e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return jsonObject;
	}

	// Get employee by Id
	public JSONObject getEmployeeById(int id) throws NamingException {
		JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
	        String _query = "SELECT employeeId, "
	        		+ "CONCAT(firstName, ' ', lastName) AS name, "
	        		+ "role, "
	        		+ "email, "
	        		+ "cellPhone, "
	        		+ "hireDate, "
	        		+ "isActive "
	        		+ "FROM Employee "
	        		+ "WHERE employeeId = " + id;

	        //create a new Query object
	        conn = new ConnectionManager();

	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);

	        //creating a temporary JSON object and put there a data from the database
	        JSONObject employee = new JSONObject();

	        // If there are results fill the jsonObject
	        if (resultSet.next()) {
	        	employee.put("name", resultSet.getString("name"));
	        	employee.put("email", resultSet.getString("email"));
	        	employee.put("cellPhone", resultSet.getString("cellPhone"));
	        	employee.put("hireDate", resultSet.getDate("hireDate"));
	        	employee.put("isActive", resultSet.getBoolean("isActive"));
	        	employee.put("role", resultSet.getString("role"));
            }

	        //creating a final JSON object
	        jsonObject.put("employee", employee);

	      } catch (SQLException e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return jsonObject;
	}

	public JSONObject getAllSales() throws NamingException {
		JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
	        String _query = "SELECT Sale.saleId, " 
	        		+ "CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
	        		+ "Program.programName, "
	        		+ "Address.street "
	        		+ "FROM Sale " 
	        		+ "JOIN Customer ON Sale.customer = Customer.customerId "
	        		+ "JOIN Program ON Sale.program = Program.programId "
	        		+ "JOIN Property ON Sale.customer = Property.customer "
	        		+ "JOIN Address ON Property.address = Address.addressId";
	        
	        //create a new Query object
	        conn = new ConnectionManager();
	        
	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);

	        //creating an object to keep a collection of JSONs
	        Collection<JSONObject> sales = new ArrayList<JSONObject>();

	        // Iterating through the Results and filling the jsonObject
	        while (resultSet.next()) {
	          //creating a temporary JSON object and put there a data from the database
	          JSONObject tempJson = new JSONObject();
	          tempJson.put("salesNumber", resultSet.getString("saleId"));
	          tempJson.put("name", resultSet.getString("customerName"));
	          tempJson.put("product", resultSet.getString("programName"));
	          tempJson.put("address", resultSet.getString("street"));
	          sales.add(tempJson);
            }

	        //creating a final JSON object
	        jsonObject.put("sales", sales);

	      } catch (Exception e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return jsonObject;
	}

	// Get sale by Id
	public JSONObject getSaleById(int id) throws NamingException {
		JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
	        String _query = "SELECT Sale.saleId, " 
	        		+ "CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
	        		+ "Program.programName, "
	        		+ "Address.street "
	        		+ "FROM Sale " 
	        		+ "JOIN Customer ON Sale.customer = Customer.customerId "
	        		+ "JOIN Program ON Sale.program = Program.programId "
	        		+ "JOIN Property ON Sale.customer = Property.customer "
	        		+ "JOIN Address ON Property.address = Address.addressId "
	        		+ "WHERE Sale.saleId = " + id;
	        
	        //create a new Query object
	        conn = new ConnectionManager();
	        
	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);
	        
	        //creating a temporary JSON object and put there a data from the database
	        JSONObject sale = new JSONObject();

	        // If there are results fill the jsonObject
	        if (resultSet.next()) {
	          sale.put("salesNumber", resultSet.getString("saleId"));
	          sale.put("name", resultSet.getString("customerName"));
	          sale.put("product", resultSet.getString("programName"));
	          sale.put("address", resultSet.getString("street"));
            }
	        
	        //creating a final JSON object
	        jsonObject.put("sale", sale);

	      } catch (Exception e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return jsonObject;
	}
	
	// Get all customers
	public JSONObject getAllCustomers() throws NamingException {
		JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
	        String _query = "SELECT customerId, " 
	        		+ "CONCAT(firstName, ' ', lastName) AS name, "
	        		+ "email, "
	        		+ "cellPhone, "
	        		+ "enbridgeNum "
	        		+ "FROM Customer";
	        
	        //create a new Query object
	        conn = new ConnectionManager();
	        
	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);
	        
	        
	        //creating an object to keep a collection of JSONs
	        Collection<JSONObject> customers = new ArrayList<JSONObject>();

	        // Iterating through the Results and filling the jsonObject
	        while (resultSet.next()) {
	          //creating a temporary JSON object and put there a data from the database
	          JSONObject tempJson = new JSONObject();
	          tempJson.put("customerId", resultSet.getString("customerId"));
	          tempJson.put("name", resultSet.getString("name"));
	          tempJson.put("email", resultSet.getString("email"));
	          tempJson.put("phoneNumber", resultSet.getString("cellPhone"));
	          tempJson.put("enbridgeNumber", resultSet.getString("enbridgeNum"));
	          customers.add(tempJson);
            }
	        
	        //creating a final JSON object
	        jsonObject.put("customers", customers);

	      } catch (Exception e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return jsonObject;
	}

	// Get customer by Id
	public JSONObject getCustomerById(int id) throws NamingException {
		JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
	        String _query = "SELECT customerId, " 
	        		+ "CONCAT(firstName, ' ', lastName) AS name, "
	        		+ "email, "
	        		+ "cellPhone, "
	        		+ "enbridgeNum "
	        		+ "FROM Customer "
	        		+ "WHERE customerId = " + id;

	        //create a new Query object
	        conn = new ConnectionManager();

	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);

	        //creating a temporary JSON object and put there a data from the database
	        JSONObject customer = new JSONObject();

	        // If there are results fill the jsonObject
	        if (resultSet.next()) {
	          customer.put("customerId", resultSet.getString("customerId"));
	          customer.put("name", resultSet.getString("name"));
	          customer.put("email", resultSet.getString("email"));
	          customer.put("phoneNumber", resultSet.getString("cellPhone"));
	          customer.put("enbridgeNumber", resultSet.getString("enbridgeNum"));
            }

	        //creating a final JSON object
	        jsonObject.put("customer", customer);

	      } catch (Exception e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return jsonObject;
	}
	
	public JSONObject getAllInstallations() throws NamingException {
        JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
			String _query = "SELECT Installation.instalationId, " 
	        		+ "CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
	        		+ "CONCAT(Employee.firstName, ' ', Employee.lastName) AS installerName, "
	        		+ "Program.programName, "
	        		+ "Address.street "
	        		+ "FROM Installation " 
	        		+ "JOIN Customer ON Installation.customer = Customer.customerId "
	        		+ "JOIN Sale ON Installation.sale = Sale.saleId "
	        		+ "JOIN Employee ON Installation.installer = Employee.employeeId "
	        		+ "JOIN Program ON Sale.program = Program.programId "
	        		+ "JOIN Property ON Installation.customer = Property.customer "
	        		+ "JOIN Address ON Property.address = Address.addressId";
	        
	        //create a new Query object
	        conn = new ConnectionManager();
	        
	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);
	        
	        
	        //creating an object to keep a collection of JSONs
	        Collection<JSONObject> installations = new ArrayList<JSONObject>();

	        // Iterating through the Results and filling the jsonObject
	        while (resultSet.next()) {
	          //creating a temporary JSON object and put there a data from the database
	          JSONObject tempJson = new JSONObject();
	          tempJson.put("installationNumber", resultSet.getString("instalationId"));
	          tempJson.put("customerName", resultSet.getString("customerName"));
	          tempJson.put("installerName", resultSet.getString("installerName"));
	          tempJson.put("product", resultSet.getString("programName"));
	          tempJson.put("address", resultSet.getString("street"));
	          installations.add(tempJson);
            }
	        
	        //creating a final JSON object
	        jsonObject.put("installations", installations);

	      } catch (Exception e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return jsonObject;
	}
	
	// Get sale by Id
	public JSONObject getInstallationById(int id) throws NamingException {
        JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
	        String _query = "SELECT Installation.instalationId, " 
	        		+ "CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
	        		+ "CONCAT(Employee.firstName, ' ', Employee.lastName) AS installerName, "
	        		+ "Program.programName, "
	        		+ "Address.street "
	        		+ "FROM Installation " 
	        		+ "JOIN Customer ON Installation.customer = Customer.customerId "
	        		+ "JOIN Sale ON Installation.sale = Sale.saleId "
	        		+ "JOIN Employee ON Installation.installer = Employee.employeeId "
	        		+ "JOIN Program ON Sale.program = Program.programId "
	        		+ "JOIN Property ON Installation.customer = Property.customer "
	        		+ "JOIN Address ON Property.address = Address.addressId "
	        		+ "WHERE Installation.InstalationId = " + id;
	        
	        //create a new Query object
	        conn = new ConnectionManager();
	        
	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);
	        
	        //creating a temporary JSON object and put there a data from the database
	        JSONObject installation = new JSONObject();

	        // If there are results fill the jsonObject
	        if (resultSet.next()) {
	          installation.put("installationNumber", resultSet.getString("instalationId"));
	          installation.put("customerName", resultSet.getString("customerName"));
	          installation.put("installerName", resultSet.getString("installerName"));
	          installation.put("product", resultSet.getString("programName"));
	          installation.put("address", resultSet.getString("street"));
            }
	        
	        //creating a final JSON object
	        jsonObject.put("installation", installation);

	      } catch (Exception e) {
	    	  e.printStackTrace();
	      } finally {
	    	  //close the connection to the database
	    	  conn.closeConnection();
	      }
		return jsonObject;
	}

//	// Get create new employee
//	public String addEmployee(JSONObject newEmployee) throws NamingException {
//		String str = "";
//		try {
//			//create a query string
//	        String _query = "INSERT INTO Employee "
//	        		+ "(firstName, lastName, email, "
//	        		+ "homePhone, cellPhone, hireDate, isActive, "
//	        		+ "password, role) "
//	        		+ "VALUES ( "
//	        		+ newEmployee.getJSONObject("firstName") + ", "
//	        		+ newEmployee.getJSONObject("lastName") + ", "
//	        		+ newEmployee.getJSONObject("email") + ", "
//	        		+ newEmployee.getJSONObject("homePhone") + ", "
//	        		+ newEmployee.getJSONObject("cellPhone") + ", "
//    				+ newEmployee.getJSONObject("hireDate") + ", "
//	        		+ newEmployee.getJSONObject("isActive") + ", "
//	        		+ newEmployee.getJSONObject("password") + ", "
//	        		+ newEmployee.getJSONObject("role") + ", "
//	        		+ ")";
//	        
//	        //create a new Query object
//	        conn = new ConnectionManager();
//	        
//	        //execute the query statement and get the ResultSet
//	        ResultSet resultSet = conn.executeQuery(_query);
//	        
//	        
//	        //creating an object to keep a collection of JSONs
//	        Collection<JSONObject> employees = new ArrayList<JSONObject>();
//
//	        //Iterating through the Results and filling the jsonObject
//	        while (resultSet.next()) {
//	          //creating a temporary JSON object and put there a data from the database
//	          JSONObject tempJson = new JSONObject();
//	          tempJson.put("name", resultSet.getString("name"));
//	          tempJson.put("email", resultSet.getString("email"));
//	          tempJson.put("cellPhone", resultSet.getString("cellPhone"));
//	          tempJson.put("hireDate", resultSet.getDate("hireDate"));
//	          tempJson.put("isActive", resultSet.getBoolean("isActive"));
//	          tempJson.put("role", resultSet.getString("role"));
//	          employees.add(tempJson);
//            }
//	        
//	        //creating a final JSON object
//	        JSONObject jsonObject = new JSONObject();
//	        jsonObject.put("employees", employees);
//	        
//	        str = "@Produces(\"application/json\") Output: \n\n" + jsonObject;
//	        
//	        
//	      } catch (SQLException e) {
//	    	  e.printStackTrace();
//	      } finally {
//	    	  //close the connection to the database
//	    	  conn.closeConnection();
//	      }
//		return str;
//	}
}
