package com.esit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONObject;

public class InstallationManager {
  private String saleId;
  private String installerId;
  private String installationDateTime;
  private String folderId;
  private String envelopeId;
  private String status;

  ConnectionManager conn;

  // Default constructor, do nothing
  public InstallationManager() {

  }

  // Constructor for POST requests
  public InstallationManager(MultivaluedMap<String, String> formParams) {
      this.setSaleId(formParams.get("saleId").get(0));
      this.setInstallerId(formParams.get("installerId").get(0));
      this.setInstallationDateTime(formParams.get("installationDateTime").get(0));
      this.setFolderId(formParams.get("folderId").get(0));
  }

  // Create installation
  public JSONObject create() {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    int result = 0;

    //final json object to return
    JSONObject jsonObject = new JSONObject();

    //create new installation object
    String createInstallationQuery = "INSERT INTO Installation (installer, sale, installationDateTime, status, folderId) "
            + "VALUES( ?, ?, ?, ?, ?)";
    String updateSaleQuery = "UPDATE Sale set status= ? WHERE saleId= ? ";
    try {
      //getting a database connection
      conn = new ConnectionManager();
      dbConnection = conn.getDBConnection();

      //TODO validate salesId and installerId before creating a new installation

      //creating a prepared statement
      preparedStatement = dbConnection.prepareStatement(createInstallationQuery);
      preparedStatement.setInt(1, Integer.parseInt(this.getInstallerId()));
      preparedStatement.setInt(2, Integer.parseInt(this.getSaleId()));
      preparedStatement.setString(3, this.getInstallationDateTime());
      preparedStatement.setString(4, "Scheduled");
      preparedStatement.setString(5, this.getFolderId());

      //execute new installation query
      result = preparedStatement.executeUpdate();

      //checking whether we added a new entry to the Installation object
      if(result > 0) {
        //creating a prepared statement
        preparedStatement = dbConnection.prepareStatement(updateSaleQuery);
        preparedStatement.setString(1, "Finished");
        preparedStatement.setInt(2, Integer.parseInt(this.getSaleId()));

        //execute update sale query
        result = preparedStatement.executeUpdate();

        if(!(result > 0)) {
          //failed to update the status
          return jsonObject.put("error", 500);
        }

        //failed to add a new Installation
      } else {
        return jsonObject.put("error", 500);
      }

    } catch (Exception e) {
      e.printStackTrace();
      return jsonObject.put("error", 500);
    } finally {
      //close the connection to the database
      conn.closeConnection();
    }

    return jsonObject;
  }

  // Update installation
  public JSONObject update(int id, MultivaluedMap<String, String> formParams) {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    //form data
    String sqft = formParams.get("sqft").get(0);
    String bathrooms = formParams.get("bathrooms").get(0);
    String residents = formParams.get("residents").get(0);
    String pool = formParams.get("pool").get(0);
    String notes = formParams.get("notes").get(0);
    String installedDate = formParams.get("installedDate").get(0);

    int result = 0;

    //final json object to return
    JSONObject jsonObject = new JSONObject();

    String updateInstallationQuery = "UPDATE Property, Customer, Sale, Installation "
        + "SET Property.sqFootage = ?, Property.bathrooms = ?, Property.residents = ?, "
        + "Property.hasPool = ?, Installation.notes = ?, Installation.installationDateTime = ? "
        + "WHERE Installation.installationId = ? AND Installation.sale = Sale.saleId "
        + "AND Sale.customer = Customer.customerId AND Property.customer = Customer.customerId";
    try {
      //getting a database connection
      conn = new ConnectionManager();
      dbConnection = conn.getDBConnection();

      //creating a prepared statement
      preparedStatement = dbConnection.prepareStatement(updateInstallationQuery);
      preparedStatement.setInt(1, Integer.parseInt(sqft));
      preparedStatement.setInt(2, Integer.parseInt(bathrooms));
      preparedStatement.setInt(3, Integer.parseInt(residents));
      preparedStatement.setBoolean(4, Boolean.parseBoolean(pool));
      preparedStatement.setString(5, notes);
      preparedStatement.setString(6, installedDate);
      preparedStatement.setInt(7, id);

      //execute update sale query
      result = preparedStatement.executeUpdate();

      //checking whether we updated the Installation
      if(!(result >= 0)) {
        return jsonObject.put("error", 500);
      }

    } catch (Exception e) {
        e.printStackTrace();
        return jsonObject.put("error", 500);
    } finally {
        //close the connection to the database
        conn.closeConnection();
    }

    return jsonObject;
  }

  // Get all installations
  public JSONObject getAllInstallations(int id) throws NamingException {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    //final json object to return
    JSONObject jsonObject = new JSONObject();

    //query to select all installations related to the installer's id
    String selectInstallersInstallations = "SELECT Installation.installationId, "
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
        + "JOIN Customer ON Sale.customer = Customer.customerId "
        + "WHERE Installation.installer = ? ";
    //query to select all installations
    String selectAllInstallations = "SELECT Installation.installationId, "
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
    try {

      //retrieving the employee by id and check their role
      EmployeeManager employeeMngr = new EmployeeManager();
      JSONObject jsonObj = new JSONObject();
      jsonObj = employeeMngr.getEmployeeById(id);
      JSONObject employee = jsonObj.getJSONObject("employee");
      String role = employee.getString("role");

      //getting a database connection
      conn = new ConnectionManager();
      dbConnection = conn.getDBConnection();

      if(role != null && !role.isEmpty()) {
          if(role.equals("installer")) {
            //creating a prepared statement
            preparedStatement = dbConnection.prepareStatement(selectInstallersInstallations);
            preparedStatement.setInt(1, id);
          }
          else {
            preparedStatement = dbConnection.prepareStatement(selectAllInstallations);
          }
      }

      //execute the query statement and get the ResultSet
      ResultSet resultSet = preparedStatement.executeQuery();


      //validating the received resultSet
      if(resultSet.isBeforeFirst()) {
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

      } else {
        //resultSet was empty
        return jsonObject.put("error", 204);
      }

    } catch (Exception e) {
        e.printStackTrace();
        return jsonObject.put("error", 500);
    } finally {
        //close the connection to the database
        conn.closeConnection();
    }
    return jsonObject;
  }
    
  public JSONObject setEnvelopeId(int id, MultivaluedMap<String, String> formParams) {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    int result = 0;
    //final json object to return
    JSONObject jsonObject = new JSONObject();

    String envelopeId = formParams.get("envelopeId").get(0);
    System.out.println(envelopeId);

    //update envelope id query
    String updateEnvelopeId = "UPDATE Installation SET envelopeId = ? WHERE installationId = ? ";
    String getEnvelopeIdQuery = "SELECT installationId, envelopeId FROM Installation WHERE installationId = ? ";
    try {

      //getting a database connection
      conn = new ConnectionManager();
      dbConnection = conn.getDBConnection();

      // Set the folderId
      this.setEnvelopeId(envelopeId);
      System.out.println("get: " + this.getEnvelopeId());

      //updating the envelope id in the Installation
      //creating a prepared statement
      preparedStatement = dbConnection.prepareStatement(updateEnvelopeId);
      preparedStatement.setString(1, this.getEnvelopeId());
      preparedStatement.setInt(2, id);

      //execute new sale query
      result = preparedStatement.executeUpdate();

      //checking whether we updated an Installation
      //creating a prepared statement
      preparedStatement = dbConnection.prepareStatement(getEnvelopeIdQuery);
      preparedStatement.setInt(1, id);

      //executing the prepared statement and retrieving the ResultSet
      ResultSet resultSet = preparedStatement.executeQuery();

      if(resultSet.next()) {
        if(resultSet.getString("envelopeId").equals(envelopeId)) {
          jsonObject.put("installationId", Integer.parseInt(resultSet.getString("installationId")));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return jsonObject.put("error", 500);
    } finally {
      //close the connection to the database
      conn.closeConnection();
    }

    return jsonObject;
  }

  // Get all scheduled installations
  public JSONObject getAllScheduled(int id) throws NamingException {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    //final json object to return
    JSONObject jsonObject = new JSONObject();

    //query to select all scheduled installations for an installer
    String getAllInstallersScheduledInstallations = "SELECT Installation.installationId, "
        + "CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
        + "CONCAT(Employee.firstName, ' ', Employee.lastName) AS installerName, "
        + "Program.programName, "
        + "Address.street, "
        + "Installation.installationDateTime, "
        + "Installation.status, "
        + "Installation.folderId "
        + "FROM Installation "
        + "JOIN Sale ON Installation.sale = Sale.saleId "
        + "JOIN Employee ON Installation.installer = Employee.employeeId "
        + "JOIN Program ON Sale.program = Program.programId "
        + "JOIN Property ON Sale.customer = Property.customer "
        + "JOIN Address ON Property.address = Address.addressId "
        + "JOIN Customer ON Sale.customer = Customer.customerId "
        + "WHERE Installation.status = ? "
        + "AND Installation.installer = ? ";
    //query to get all scheduled installations
    String getAllInstallations = "SELECT Installation.installationId, "
        + "CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
        + "CONCAT(Employee.firstName, ' ', Employee.lastName) AS installerName, "
        + "Program.programName, "
        + "Address.street, "
        + "Installation.installationDateTime, "
        + "Installation.status, "
        + "Installation.folderId "
        + "FROM Installation "
        + "JOIN Sale ON Installation.sale = Sale.saleId "
        + "JOIN Employee ON Installation.installer = Employee.employeeId "
        + "JOIN Program ON Sale.program = Program.programId "
        + "JOIN Property ON Sale.customer = Property.customer "
        + "JOIN Address ON Property.address = Address.addressId "
        + "JOIN Customer ON Sale.customer = Customer.customerId "
        + "WHERE Installation.status = ? ";
    try {
      //getting the employee object and checking their role
      EmployeeManager employeeMngr = new EmployeeManager();
      JSONObject employeeJsonObj = new JSONObject();
      employeeJsonObj = employeeMngr.getEmployeeById(id);

      //checking if getEmployeeById worked as expected
      if(!(employeeJsonObj.has("error"))) {
        JSONObject employee = employeeJsonObj.getJSONObject("employee");
        String role = employee.getString("role");

        //getting a database connection
        conn = new ConnectionManager();
        dbConnection = conn.getDBConnection();

        if(role != null && !role.isEmpty()) {
          if(role.equals("installer")) {
            //creating a prepared statement
            preparedStatement = dbConnection.prepareStatement(getAllInstallersScheduledInstallations);
            preparedStatement.setString(1, "Scheduled");
            preparedStatement.setInt(2, id);
          } else {
            //creating a prepared statement
            preparedStatement = dbConnection.prepareStatement(getAllInstallations);
            preparedStatement.setString(1, "Scheduled");
          }
        }

        //execute the query statement and get the ResultSet
        ResultSet resultSet = preparedStatement.executeQuery();

        //validating the received resultSet
        if(resultSet.isBeforeFirst()) {
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
            tempJson.put("installationDateTime", resultSet.getString("installationDateTime"));
            tempJson.put("status", resultSet.getString("status"));
            tempJson.put("folderId", resultSet.getString("folderId"));
            installations.add(tempJson);
          }

          //creating a final JSON object
          jsonObject.put("installations", installations);

          //installation table was empty
        } else {
          return jsonObject.put("error", 204);
        }
        //couldn't fetch an employee by id
      } else {
        return employeeJsonObj;
      }

    } catch (Exception e) {
        e.printStackTrace();
        return jsonObject.put("error", 500);
    } finally {
        //close the connection to the database
        conn.closeConnection();
    }
    return jsonObject;
  }

  // Get installation by Id
  public JSONObject getInstallationById(int id) throws NamingException {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    JSONObject jsonObject = new JSONObject();

    String selectInstallationByIdQuery = "SELECT Installation.installationId, Customer.firstName, Customer.lastName, "
        + "CONCAT(Employee.firstName, ' ', Employee.lastName) AS installerName, "
        + "Employee.employeeId, Employee.email AS installerEmail, Program.programName, "
        + "Address.street, Address.unit, Address.city, Address.province, Address.postalCode, "
        + "Customer.enbridgeNum, Customer.email, Customer.homePhone, Customer.cellPhone, "
        + "Property.sqFootage , Property.bathrooms , Property.residents , Property.hasPool, "
        + "Installation.installationDateTime, Installation.status "
        + "FROM Installation "
        + "JOIN Sale ON Installation.sale = Sale.saleId "
        + "JOIN Employee ON Installation.installer = Employee.employeeId "
        + "JOIN Program ON Sale.program = Program.programId "
        + "JOIN Property ON Sale.customer = Property.customer "
        + "JOIN Address ON Property.address = Address.addressId "
        + "JOIN Customer ON Sale.customer = Customer.customerId "
        + "WHERE Installation.installationId = ? ";
    try {

      //getting a database connection
      conn = new ConnectionManager();
      dbConnection = conn.getDBConnection();

      //creating a prepared statement
      preparedStatement = dbConnection.prepareStatement(selectInstallationByIdQuery);
      preparedStatement.setInt(1, id);

      //execute the query statement and get the ResultSet
      ResultSet resultSet = preparedStatement.executeQuery();

      //creating a temporary JSON object and put there a data from the database
      JSONObject installation = new JSONObject();

      // If there are results fill the jsonObject
      if (resultSet.next()) {
        installation.put("installationNumber", resultSet.getString("installationId"));
        installation.put("customerFirstName", resultSet.getString("firstName"));
        installation.put("customerLastName", resultSet.getString("lastName"));
        installation.put("installerId", resultSet.getString("employeeId"));
        installation.put("installerEmail", resultSet.getString("installerEmail"));
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
        installation.put("status", resultSet.getString("status"));
        installation.put("email", resultSet.getString("email"));
        installation.put("sqFootage", resultSet.getString("sqFootage"));
        installation.put("bathrooms", resultSet.getString("bathrooms"));
        installation.put("residents", resultSet.getString("residents"));
        installation.put("hasPool", resultSet.getString("hasPool"));
        installation.put("installationDateTime", resultSet.getString("installationDateTime"));

        //creating a final JSON object
        jsonObject.put("installation", installation);
      } else {
        //couldn't find a requested installation
        return jsonObject.put("error", 204);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return jsonObject.put("error", 500);
    } finally {
      //close the connection to the database
      conn.closeConnection();
    }
    return jsonObject;
  }


  public JSONObject setInstallationStatus(int id, MultivaluedMap<String, String> formParams) {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    int result = 0;

    //final json object to return
    JSONObject jsonObject = new JSONObject();

    String status = formParams.get("status").get(0);
    System.out.println(status);

    //update installation status query
    String updateInstallationStatusQuery = "UPDATE Installation SET status = ? WHERE installationId = ? ";
    //select installation status query
    String selectInstallationStatusQuery = "SELECT installationId, status FROM Installation WHERE installationId = ? ";
    try {

      //getting a database connection
      conn = new ConnectionManager();
      dbConnection = conn.getDBConnection();

      // Set the folderId
      this.setStatus(status);
      System.out.println("get: " + this.getStatus());

      //creating a prepared statement
      preparedStatement = dbConnection.prepareStatement(updateInstallationStatusQuery);
      preparedStatement.setString(1, this.getStatus());
      preparedStatement.setInt(2, id);

      //execute new sale query
      result = preparedStatement.executeUpdate();

      //checking whether we updated the status
      preparedStatement = dbConnection.prepareStatement(selectInstallationStatusQuery);
      preparedStatement.setInt(1, id);

      ResultSet resultSet = preparedStatement.executeQuery();

      if(resultSet.next() && resultSet.getString("status").equals(status)) {
        jsonObject.put("installationId", Integer.parseInt(resultSet.getString("installationId")));
      } else {
        return jsonObject.put("error", 500);
      }
    } catch (Exception e) {
        e.printStackTrace();
        return jsonObject.put("error", 500);
    } finally {
        //close the connection to the database
        conn.closeConnection();
    }

    return jsonObject;
  }

    
    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getInstallerId() {
        return installerId;
    }

    public void setInstallerId(String installerId) {
        this.installerId = installerId;
    }

    public String getInstallationDateTime() {
        return installationDateTime;
    }

    public void setInstallationDateTime(String installationDateTime) {
        this.installationDateTime = installationDateTime;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

	public String getEnvelopeId() {
		return envelopeId;
	}

	public void setEnvelopeId(String envelopeId) {
		this.envelopeId = envelopeId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
