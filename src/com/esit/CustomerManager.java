package com.esit;

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
    public int create(ConnectionManager conn) {
        int result = 0;
        int customerID = 0;
        int addressID = 0;
        try {

            //check whether we have a connection sent to us
            if(conn == null) {
              //getting a connection to the Database
              conn = new ConnectionManager();
            }

            //checking whether a customer with such an email already exists
            String getCustomerIdQuery = "SELECT customerId "
                    + "FROM Customer "
                    + "WHERE email = '" + this.getEmail() + "'";
            ResultSet resultSet = conn.executeQuery(getCustomerIdQuery);
            if(resultSet.next()) {
              customerID = Integer.parseInt(resultSet.getString("customerId"));
              return customerID;

              //Customer doesn't exist, creating a new one
            } else {
              //create new Customer query
              String newCustomerQuery = "INSERT INTO Customer ("
                      + "firstName, lastName, email, homePhone, cellPhone, enbridgeNum) "
                      + "VALUES ('" + this.getFname() + "', '" + this.getLname() + "', '" + this.getEmail() + "', '" +
                      this.getHomePhone() + "', '" + this.getCellPhone() + "', '" + this.getEnbridge() + "')";

              //execute create new Customer query and get the confirmation
              result = conn.executeUpdate(newCustomerQuery);

              //checking whether customer was created properly
              if(result > 0) {
                //getting the id of the new Customer object
                getCustomerIdQuery = "SELECT customerId "
                        + "FROM Customer "
                        + "WHERE email = '" + this.getEmail() + "'";
                resultSet = conn.executeQuery(getCustomerIdQuery);
                if(resultSet.next()) {
                    customerID = Integer.parseInt(resultSet.getString("customerId"));
                }

                //create new Address query
                String newAddressQuery = "INSERT INTO Address (street, unit, city, province, postalCode) "
                        + "VALUES ('" + this.getAddress() + "', '" + this.getUnitNum() + "', '" + this.getCity() 
                        + "', '" + this.getProvince() + "', '" + this.getPostalCode() + "')";
                //execute create new Address query and get the confirmation
                result = conn.executeUpdate(newAddressQuery);

                //checking whether Address was created successfully
                if(result > 0) {
                  //getting the id of the new Address object
                  String getAddressIdQuery = "SELECT addressId "
                          + "FROM Address "
                          + "WHERE street = '" + this.getAddress() + "'"
                          + " AND unit = '" + this.getUnitNum() + "'"
                          + " AND city = '" + this.getCity() + "'"
                          + " AND province = '" + this.getProvince() + "'"
                          + " AND postalCode = '" + this.getPostalCode() + "'";

                  resultSet = conn.executeQuery(getAddressIdQuery);
                  if(resultSet.next()) {
                      addressID = Integer.parseInt(resultSet.getString("addressId"));
                  }

                  //create new Property object
                  String newPropertyQuery = "INSERT INTO Property (address, customer, sqFootage, bathrooms, residents, hasPool) "
                          + "VALUES (" + addressID + ", " + customerID + ", 123, NULL, NULL, NULL)";

                  //execute create new Property query here and get the result
                  result = conn.executeUpdate(newPropertyQuery);

                }
              }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return customerID;
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
                    + "Customer.homePhone, "
                    + "Customer.enbridgeNum "
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
              tempJson.put("cellPhone", resultSet.getString("cellPhone"));
              tempJson.put("enbridgeNumber", resultSet.getString("enbridgeNum"));
              tempJson.put("homePhone", resultSet.getString("homePhone"));
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
                    + "firstName, "
                    + "lastName, "
                    + "email, "
                    + "homePhone, "
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
              customer.put("firstName", resultSet.getString("firstName"));
              customer.put("lastName", resultSet.getString("lastName"));
              customer.put("email", resultSet.getString("email"));
              customer.put("homePhone", resultSet.getString("homePhone"));
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
