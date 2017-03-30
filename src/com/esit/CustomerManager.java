package com.esit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;

import org.json.JSONObject;

public class CustomerManager {
  private String fname;
  private String lname;
  private String email;
  private String homePhone;
  private String cellPhone;
  private String enbridge;
  private String address;
  private String unitNum;
  private String city;
  private String province;
  private String postalCode;

  private ConnectionManager conn;

  //default constructor, do nothing
  public CustomerManager() {

  }

    //constructor
  public CustomerManager(
      String fname, String lname, String email,
      String homePhone, String cellPhone, String enbridge,
      String address, String unitNum, String city,
      String province, String postalCode) {

    this.setFname(fname);
    this.setLname(lname);
    this.setAddress(address);
    this.setUnitNum(unitNum);
    this.setCity(city);
    this.setProvince(province);
    this.setPostalCode(postalCode);
    this.setEnbridge(enbridge);
    this.setEmail(email);
    this.setHomePhone(homePhone);
    this.setCellPhone(cellPhone);
  }

  //creates a Customer object together with Address and Property
  //and returns a customer id if everything is fine
  public JSONObject create(ConnectionManager conn) {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    int result = 0;
    int customerID = 0;
    int addressID = 0;

    JSONObject jsonObject = new JSONObject();
    String getCustomerIdQuery = "SELECT customerId FROM Customer WHERE enbridgeNum = ? ";
    String getCustomerIdQuery2 = "SELECT customerId FROM Customer WHERE email = ? ";
    String createCustomerQuery = "INSERT INTO Customer (firstName, lastName, email, homePhone, cellPhone, enbridgeNum) "
            + "VALUES ( ?, ?, ?, ?, ?, ?)";
    String createAddressQuery = "INSERT INTO Address (street, unit, city, province, postalCode) "
            + "VALUES ( ?, ?, ?, ?, ?)";
    String getAddressIdQuery = "SELECT addressId FROM Address WHERE street = ? AND unit = ? AND city = ? AND province = ? AND postalCode = ? ";
    String createPropertyQuery = "INSERT INTO Property (address, customer, sqFootage, bathrooms, residents, hasPool) "
            + "VALUES ( ?, ?, NULL, NULL, NULL, NULL)";
    try {

      //check whether we have a connection sent to us
      if(conn == null) {
        //getting a connection to the Database
        conn = new ConnectionManager();
      }
      dbConnection = conn.getDBConnection();

      //checking whether a customer with the same enbridge number is already in the database
      preparedStatement = dbConnection.prepareStatement(getCustomerIdQuery);
      preparedStatement.setString(1, this.getEnbridge());
      ResultSet resultSet = preparedStatement.executeQuery();

      if(resultSet.next()) {
        customerID = Integer.parseInt(resultSet.getString("customerId"));
        jsonObject.put("error", 409);
        jsonObject.put("errorMessage", "An invalid enbridge number");
        return jsonObject;
      }

      //checking whether a customer with the same email already exists
      preparedStatement = dbConnection.prepareStatement(getCustomerIdQuery2);
      preparedStatement.setString(1, this.getEmail());

      //executing preparedStatement and getting the resultSet back
      resultSet = preparedStatement.executeQuery();
      if(resultSet.next()) {
        customerID = Integer.parseInt(resultSet.getString("customerId"));
        return jsonObject.put("customerId", customerID);

        //Customer doesn't exist, creating a new one
      } else {
        //create new Customer query
        preparedStatement = dbConnection.prepareStatement(createCustomerQuery);
        preparedStatement.setString(1, this.getFname());
        preparedStatement.setString(2, this.getLname());
        preparedStatement.setString(3, this.getEmail());
        preparedStatement.setString(4, this.getHomePhone());
        preparedStatement.setString(5, this.getCellPhone());
        preparedStatement.setString(6, this.getEnbridge());

        //execute create new Customer query and get the confirmation
        result = preparedStatement.executeUpdate();

        //checking whether customer was created properly
        if(result > 0) {
          //getting the id of the new Customer object
          preparedStatement = dbConnection.prepareStatement(getCustomerIdQuery2);
          preparedStatement.setString(1, this.getEmail());

          resultSet = preparedStatement.executeQuery();
          if(resultSet.next()) {
            customerID = Integer.parseInt(resultSet.getString("customerId"));
          }

          //creating a new Address object
          preparedStatement = dbConnection.prepareStatement(createAddressQuery);
          preparedStatement.setString(1, this.getAddress());
          preparedStatement.setString(2, this.getUnitNum());
          preparedStatement.setString(3, this.getCity());
          preparedStatement.setString(4, this.getProvince());
          preparedStatement.setString(5, this.getPostalCode());

          //execute create new Address query and get the confirmation
          result = preparedStatement.executeUpdate();

          //checking whether Address was created successfully
          if(result > 0) {
            //getting the id of the new Address object
            preparedStatement = dbConnection.prepareStatement(getAddressIdQuery);
            preparedStatement.setString(1, this.getAddress());
            preparedStatement.setString(2, this.getUnitNum());
            preparedStatement.setString(3, this.getCity());
            preparedStatement.setString(4, this.getProvince());
            preparedStatement.setString(5, this.getPostalCode());

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
              addressID = Integer.parseInt(resultSet.getString("addressId"));
            }

            //create new Property object
            preparedStatement = dbConnection.prepareStatement(createPropertyQuery);
            preparedStatement.setInt(1, addressID);
            preparedStatement.setInt(2, customerID);
            //execute create new Property query here and get the result
            result = preparedStatement.executeUpdate();

            //checking whether the property was created
            if(!(result >= 0)) {
              //couldn't create a Property object
              return jsonObject.put("error", 500);
            }
            //couldn't create Address object
          } else {
            return jsonObject.put("error", 500);
          }
          //couldn't create a Customer
        } else {
          return jsonObject.put("error", 500);
        }
      }
    } catch (Exception e) {
        e.printStackTrace();
        return jsonObject.put("error", 500);
    } finally {
      //close the connection to the database
      conn.closeConnection();
    }

    return jsonObject.put("customerId", customerID);
  }
    
  // Get all customers
  public JSONObject getAllCustomers() throws NamingException {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    JSONObject jsonObject = new JSONObject();

    String getAllCustomersQuery = "SELECT Customer.customerId, "
            + "CONCAT(Customer.firstName, ' ', Customer.lastName) AS name, "
            + "Customer.email, Customer.cellPhone, Customer.homePhone, Customer.enbridgeNum "
            + "FROM Customer";
    try {

      //getting a database connection
      conn = new ConnectionManager();
      dbConnection = conn.getDBConnection();
      //creating a prepared statement
      preparedStatement = dbConnection.prepareStatement(getAllCustomersQuery);
      //executing the prepared statement and retrieving the ResultSet
      ResultSet resultSet = preparedStatement.executeQuery();

      //validating the received resultSet
      if(resultSet.isBeforeFirst()) {

        //creating an object to keep a collection of JSONs
        Collection<JSONObject> customers = new ArrayList<JSONObject>();

        // Iterating through the Results and filling the jsonObject
        while (resultSet.next()) {
          //creating a temporary JSON object and put there a data from the database
          JSONObject tempJson = new JSONObject();
          tempJson.put("customerId", resultSet.getString("customerId"));
          tempJson.put("name", resultSet.getString("name"));
          tempJson.put("email", resultSet.getString("email"));
          tempJson.put("cellPhone", resultSet.getString("cellPhone"));
          tempJson.put("enbridgeNumber", resultSet.getString("enbridgeNum"));
          tempJson.put("homePhone", resultSet.getString("homePhone"));
          customers.add(tempJson);
        }

        //creating a final JSON object
        jsonObject.put("customers", customers);

        //there is no customers in the database
      } else {
        return jsonObject.put("error", 204);
      }
    } catch (Exception e) {
        e.printStackTrace();
        jsonObject.put("error", 500);
        return jsonObject;
    } finally {
        //close the connection to the database
        conn.closeConnection();
    }
    return jsonObject;
  }

  // Get customer by Id
  public JSONObject getCustomerById(int id) throws NamingException {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;

    JSONObject jsonObject = new JSONObject();

    String getcustomerQuery = "SELECT customerId, firstName, lastName, email, homePhone, cellPhone, enbridgeNum FROM Customer WHERE customerId = ? ";
    try {

      //getting a connection to the database
      conn = new ConnectionManager();
      dbConnection = conn.getDBConnection();
      //creating a prepared statement
      preparedStatement = dbConnection.prepareStatement(getcustomerQuery);
      preparedStatement.setInt(1, id);

      //execute the query statement and get the ResultSet
      ResultSet resultSet = preparedStatement.executeQuery();

      //validating the received resultSet
      if(resultSet.isBeforeFirst()) {
        //creating a temporary JSON object and put there a data from the database
        JSONObject customer = new JSONObject();

        // If there are results fill the jsonObject
        if (resultSet.next()) {
          customer.put("customerId", resultSet.getString("customerId"));
          customer.put("firstName", resultSet.getString("firstName"));
          customer.put("lastName", resultSet.getString("lastName"));
          customer.put("email", resultSet.getString("email"));
          customer.put("homePhone", resultSet.getString("homePhone"));
          customer.put("cellPhone", resultSet.getString("cellPhone"));
          customer.put("enbridgeNumber", resultSet.getString("enbridgeNum"));
        }

        //creating a final JSON object
        jsonObject.put("customer", customer);

      //couldn't find an employee in the database 
      } else {
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

    public String getFname() {
      return fname;
    }

    public void setFname(String fname) {
      this.fname = fname;
    }

    public String getLname() {
      return lname;
    }

    public void setLname(String lname) {
      this.lname = lname;
    }

    public String getEnbridge() {
      return enbridge;
    }

    public void setEnbridge(String enbridge) {
      this.enbridge = enbridge;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getUnitNum() {
      return unitNum;
    }

    public void setUnitNum(String unitNum) {
      this.unitNum = unitNum;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getHomePhone() {
      return homePhone;
    }

    public void setHomePhone(String homePhone) {
      this.homePhone = homePhone;
    }

    public String getCellPhone() {
      return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
      this.cellPhone = cellPhone;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getProvince() {
      return province;
    }

    public void setProvince(String province) {
      this.province = province;
    }

    public String getPostalCode() {
      return postalCode;
    }

    public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
    }
}
