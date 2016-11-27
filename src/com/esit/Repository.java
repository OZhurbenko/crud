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
	        		+ "firstName, "
	        		+ "lastName, "
	        		+ "role, "
	        		+ "email, "
	        		+ "homePhone, "
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
	        	employee.put("employeeId", resultSet.getString("employeeId"));
	        	employee.put("firstName", resultSet.getString("firstName"));
	        	employee.put("lastName", resultSet.getString("lastName"));
	        	employee.put("email", resultSet.getString("email"));
	        	employee.put("homePhone", resultSet.getString("homePhone"));
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
	        		+ "Address.street, "
	        		+ "Sale.dateSigned, "
	        		+ "Sale.status "
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
	          tempJson.put("date", resultSet.getString("dateSigned"));
	          tempJson.put("status", resultSet.getString("status"));
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
	        		+ "Customer.firstName, "
	        		+ "Customer.lastName, "
	        		+ "Program.programName, "
	        		+ "Address.street, "
	        		+ "Address.unit, "
	        		+ "Address.city, "
	        		+ "Address.province, "
	        		+ "Address.postalCode, "
	        		+ "Customer.enbridgeNum, "
	        		+ "Customer.email, "
	        		+ "Customer.homePhone, "
	        		+ "Customer.cellPhone, "
	        		+ "Program.programId, "
	        		+ "DATE(Installation.installationDateTime) as installationDate, "
	        		+ "TIME(Installation.installationDateTime) as installationTime, "
	        		+ "Installation.installationDateTime, "
	        		+ "Sale.notes, "
	        		+ "Sale.salesRepId "
	        		+ "FROM Sale " 
	        		+ "JOIN Customer ON Sale.customer = Customer.customerId "
	        		+ "JOIN Program ON Sale.program = Program.programId "
	        		+ "JOIN Property ON Sale.customer = Property.customer "
	        		+ "JOIN Address ON Property.address = Address.addressId "
	        		+ "JOIN Installation ON Sale.saleId = Installation.sale "
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
	          sale.put("firstName", resultSet.getString("firstName"));
	          sale.put("lastName", resultSet.getString("lastName"));
	          sale.put("product", resultSet.getString("programName"));
	          sale.put("address", resultSet.getString("street"));
	          sale.put("unit", resultSet.getString("unit"));
	          sale.put("city", resultSet.getString("city"));
	          sale.put("province", resultSet.getString("province"));
	          sale.put("postalCode", resultSet.getString("postalCode"));
	          sale.put("enbridgeNum", resultSet.getString("enbridgeNum"));
	          sale.put("homePhone", resultSet.getString("homePhone"));
	          sale.put("cellPhone", resultSet.getString("cellPhone"));
	          sale.put("email", resultSet.getString("email"));
	          sale.put("programId", resultSet.getString("programId"));
	          sale.put("installationDate", resultSet.getString("installationDate"));
	          sale.put("installationTime", resultSet.getString("installationTime"));
	          sale.put("installationDateTime", resultSet.getString("installationDateTime"));
	          sale.put("notes", resultSet.getString("notes"));
	          sale.put("salesRepId", resultSet.getString("salesRepId"));
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

    public int createNewSale(String fname, String lname, String address, String unitNum, String city,
            String province, String postalCode, String enbridge, String email, String homePhone, String cellPhone,
            String programType, String installationDate, String installationTime, String notes, String dateSigned,
            String saleNumber, String salesRepId, String applicationNumber) {
        int result = 0;
        try {
            int customerID = 0;
            int addressID = 0;

            //MISSING FIELDS
            String businessPhone = "416-981-5050";

            //getting a connection to the Database
            conn = new ConnectionManager();

            //create new Customer query
            String newCustomerQuery = "INSERT INTO Customer ("
                    + "firstName, lastName, email, homePhone, cellPhone, businessPhone, enbridgeNum) "
                    + "VALUES ('" + fname + "', '" + lname + "', '" + email + "', '" + homePhone + "', '"
                    + cellPhone + "', '" + businessPhone + "', '" + enbridge + "')";

            //execute create new Customer query and get the confirmation
            result = conn.executeUpdate(newCustomerQuery);

            //TODO validate result

            //getting the id of the new Customer object
            String getCustomerIdQuery = "SELECT customerId "
                    + "FROM Customer "
                    + "WHERE email = '" + email + "'";
            ResultSet resultSet = conn.executeQuery(getCustomerIdQuery);
            if(resultSet.next()) {
                customerID = Integer.parseInt(resultSet.getString("customerId"));
            }

            //create new Address query
            String newAddressQuery = "INSERT INTO Address (street, unit, city, province, postalCode) "
                    + "VALUES ('" + address + "', '" + unitNum + "', '" + city + "', '" + province + "', '" + postalCode + "')";
            //execute create new Address query and get the confirmation
            result = conn.executeUpdate(newAddressQuery);
            //TODO validate result

            //getting the id of the new Address object
            String getAddressIdQuery = "SELECT addressId "
                    + "FROM Address "
                    + "WHERE street = '" + address + "'"
                    + " AND unit = '" + unitNum + "'"
                    + " AND city = '" + city + "'"
                    + " AND province = '" + province + "'"
                    + " AND postalCode = '" + postalCode + "'";
            resultSet = conn.executeQuery(getAddressIdQuery);
            if(resultSet.next()) {
                addressID = Integer.parseInt(resultSet.getString("addressId"));
            }

            //create new Property object
            String newPropertyQuery = "INSERT INTO Property (address, customer, sqFootage, bathrooms, residents, hasPool) "
                    + "VALUES (" + addressID + ", " + customerID + ", 123, NULL, NULL, NULL)";

            //execute create new Property query here and get the result
            result = conn.executeUpdate(newPropertyQuery);

            //TODO validate salesRepId and program before creating a new Sale object
            //create new sale object
            String newSaleQuery = "INSERT INTO Sale ("
                    + "customer, salesRepId, program, "
                    + "rentalAgreement, PADForm, dateSigned, "
                    + "installationDateTime, notes, status) "
                    + "VALUES(" + customerID + ", " + salesRepId + ", " + programType
                    + ", NULL, NULL, " + "'2016-09-20', "
                    + "'2016-09-22 08:00:00', '" + notes + "', " + "'In progress')";

            //execute new sale query
            result = conn.executeUpdate(newSaleQuery);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return result;
    }

    // Get all customers
	public JSONObject getAllCustomers() throws NamingException {
		JSONObject jsonObject = new JSONObject();
		try {
			//create a query string
	        String _query = "SELECT Customer.customerId, " 
	        		+ "CONCAT(Customer.firstName, ' ', Customer.lastName) AS name, "
	        		+ "Customer.email, "
	        		+ "Customer.cellPhone, "
	        		+ "Customer.enbridgeNum, "
	        		+ "Sale.dateSigned AS date "
	        		+ "FROM Customer "
	        		+ "JOIN Sale ON Customer.customerId = Sale.customer";
	        
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
	          tempJson.put("date", resultSet.getString("date"));
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
			String _query = "SELECT Installation.installationId, " 
	        		+ "CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
	        		+ "CONCAT(Employee.firstName, ' ', Employee.lastName) AS installerName, "
	        		+ "Program.programName, "
	        		+ "Address.street, "
	        		+ "DATE(Installation.installationDateTime) AS installationDate, "
	        		+ "Installation.status "
	        		+ "FROM Installation " 
	        		+ "JOIN Sale ON Installation.sale = Sale.saleId "
	        		+ "JOIN Employee ON Installation.installer = Employee.employeeId "
	        		+ "JOIN Program ON Sale.program = Program.programId "
	        		+ "JOIN Property ON Sale.customer = Property.customer "
	        		+ "JOIN Address ON Property.address = Address.addressId "
	        		+ "JOIN Customer ON Sale.customer = Customer.customerId";
	        
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
	          tempJson.put("installationNumber", resultSet.getString("installationId"));
	          tempJson.put("customerName", resultSet.getString("customerName"));
	          tempJson.put("installerName", resultSet.getString("installerName"));
	          tempJson.put("product", resultSet.getString("programName"));
	          tempJson.put("address", resultSet.getString("street"));
	          tempJson.put("installationDate", resultSet.getString("installationDate"));
	          tempJson.put("status", resultSet.getString("status"));
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
	        String _query = "SELECT Installation.installationId, " 
                + "Customer.firstName, "
                + "Customer.lastName, "
                + "CONCAT(Employee.firstName, ' ', Employee.lastName) AS installerName, "
                + "Employee.employeeId, "
                + "Program.programName, "
                + "Address.street, "
                + "Address.unit, "
                + "Address.city, "
                + "Address.province, "
                + "Address.postalCode, "
                + "Customer.enbridgeNum, "
                + "Customer.email, "
                + "Customer.homePhone, "
                + "Customer.cellPhone, "
                + "Property.sqFootage , "
                + "Property.bathrooms , "
                + "Property.residents , "
                + "Property.hasPool, "
                + "Installation.installationDateTime "
                + "FROM Installation "
                + "JOIN Sale ON Installation.sale = Sale.saleId "
                + "JOIN Employee ON Installation.installer = Employee.employeeId "
                + "JOIN Program ON Sale.program = Program.programId "
                + "JOIN Property ON Sale.customer = Property.customer "
                + "JOIN Address ON Property.address = Address.addressId "
                + "JOIN Customer ON Sale.customer = Customer.customerId "
                + "WHERE Installation.installationId = " + id;

	        //create a new Query object
	        conn = new ConnectionManager();
	        
	        //execute the query statement and get the ResultSet
	        ResultSet resultSet = conn.executeQuery(_query);
	        
	        //creating a temporary JSON object and put there a data from the database
	        JSONObject installation = new JSONObject();

	        // If there are results fill the jsonObject
	        if (resultSet.next()) {
	          installation.put("installationNumber", resultSet.getString("installationId"));
	          installation.put("customerFirstName", resultSet.getString("firstName"));
	          installation.put("customerLastName", resultSet.getString("lastName"));
	          installation.put("installerId", resultSet.getString("employeeId"));
	          installation.put("installerName", resultSet.getString("installerName"));
	          installation.put("product", resultSet.getString("programName"));
	          installation.put("address", resultSet.getString("street"));
	          installation.put("unit", resultSet.getString("unit"));
	          installation.put("city", resultSet.getString("city"));
	          installation.put("province", resultSet.getString("province"));
	          installation.put("postalCode", resultSet.getString("postalCode"));
	          installation.put("enbridgeNum", resultSet.getString("enbridgeNum"));
	          installation.put("homePhone", resultSet.getString("homePhone"));
	          installation.put("cellPhone", resultSet.getString("cellPhone"));
	          installation.put("email", resultSet.getString("email"));
	          installation.put("sqFootage", resultSet.getString("sqFootage"));
	          installation.put("bathrooms", resultSet.getString("bathrooms"));
	          installation.put("residents", resultSet.getString("residents"));
	          installation.put("hasPool", resultSet.getString("hasPool"));
	          installation.put("installationDateTime", resultSet.getString("installationDateTime"));
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
