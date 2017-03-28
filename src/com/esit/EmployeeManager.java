package com.esit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONObject;

public class EmployeeManager {

    private String fname;
    private String lname;
    private String street;
    private String unitNum;
    private String city;
    private String province;
    private String postalCode;
    private String email;
    private String homePhone;
    private String cellPhone;
    private String password;
    private String hireDate;
    private String isActive;
    private String employeeType;

    private ConnectionManager conn;

    //default constructor, do nothing
    public EmployeeManager() {

    }

    public EmployeeManager(MultivaluedMap<String, String> formParams) {
        this.setEmployeeType(formParams.get("employeeType").get(0));
        this.setFname(formParams.get("fname").get(0));
        this.setLname(formParams.get("lname").get(0));
        this.setStreet(formParams.get("street").get(0));
        this.setUnitNum(formParams.get("unitNum").get(0));
        this.setCity(formParams.get("city").get(0));
        this.setProvince(formParams.get("province").get(0));
        this.setPostalCode(formParams.get("postalCode").get(0));
        this.setEmail(formParams.get("email").get(0));
        this.setHomePhone(formParams.get("homePhone").get(0));
        this.setCellPhone(formParams.get("cellPhone").get(0));
        this.setPassword(formParams.get("password").get(0));
        this.setHireDate(formParams.get("hireDate").get(0));
        if(formParams.containsKey("isActive")) {
            this.setIsActive(formParams.get("isActive").get(0));
        }
    }

    //create a new Employee and return his id
    public int create() {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        int addressId = 0;
        int employeeId = 0;
        int result = 0;

        //create new Address query
        String newAddressQuery = "INSERT INTO Address (street, unit, city, province, postalCode) "
                + "VALUES ( ?, ?, ?, ?, ?)";
        String getAddressIdQuery = "SELECT addressId FROM Address "
                + "WHERE street = ? AND unit = ? AND city = ? AND province = ? AND postalCode = ? ";
        String createEmployeeQuery = "INSERT INTO Employee (firstName, lastName, email, homePhone, cellPhone, hireDate, isActive, password, role, addressId) "
                + "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String getEmployeeIdQuery = "SELECT employeeId FROM Employee WHERE email = ? ";
        try {

            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            //creating new Address object
            preparedStatement = dbConnection.prepareStatement(newAddressQuery);
            preparedStatement.setString(1, this.getStreet());
            preparedStatement.setString(2, this.getUnitNum());
            preparedStatement.setString(3, this.getCity());
            preparedStatement.setString(4, this.getProvince());
            preparedStatement.setString(5, this.getPostalCode());

            //execute create new Address query and get the confirmation
            result = preparedStatement.executeUpdate();

            //getting the id of the new Address object
            preparedStatement = dbConnection.prepareStatement(getAddressIdQuery);
            preparedStatement.setString(1, this.getStreet());
            preparedStatement.setString(2, this.getUnitNum());
            preparedStatement.setString(3, this.getCity());
            preparedStatement.setString(4, this.getProvince());
            preparedStatement.setString(5, this.getPostalCode());

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                addressId = Integer.parseInt(resultSet.getString("addressId"));
            }

            //check if Address object was created
            if(addressId > 0) {
              //create new Employee object
              preparedStatement = dbConnection.prepareStatement(createEmployeeQuery);
              preparedStatement.setString(1, this.getFname());
              preparedStatement.setString(2, this.getLname());
              preparedStatement.setString(3, this.getEmail());
              preparedStatement.setString(4, this.getHomePhone());
              preparedStatement.setString(5, this.getCellPhone());
              preparedStatement.setString(6, this.getHireDate());
              preparedStatement.setBoolean(7, Boolean.parseBoolean(this.getIsActive()));
              preparedStatement.setString(8, this.getPassword());
              preparedStatement.setString(9, this.getEmployeeType());
              preparedStatement.setInt(10, addressId);

              //execute create new Property query here and get the result
              result = preparedStatement.executeUpdate();

              //getting the id of the new Employee object
              preparedStatement = dbConnection.prepareStatement(getEmployeeIdQuery);
              preparedStatement.setString(1, this.getEmail());

              //execute getEmployeeId query and get the ResultSet
              resultSet = preparedStatement.executeQuery();

              if(resultSet.next()) {
                  employeeId = Integer.parseInt(resultSet.getString("employeeId"));
              }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return employeeId;
    }

    // Update employee
    public int update(int id) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        int result = 0;

        //update employee with password query
        String updateEmployeeQuery = "UPDATE Employee, Address "
                + "SET Employee.firstName = ?, "
                + "Employee.lastName = ?, Employee.email = ?, Employee.homePhone = ?, Employee.cellPhone = ?, "
                + "Employee.hireDate = ?, Employee.role = ?, Employee.password = ?, "
                + "Address.street = ?, Address.unit = ?, Address.city = ?, Address.province = ?, Address.postalCode = ? "
                + "WHERE Employee.employeeId = ? AND Employee.addressId = Address.addressId ";
        //update employee object without a password query
        String updateEmployeeQuery2 = "UPDATE Employee, Address "
                + "SET Employee.firstName = ?, "
                + "Employee.lastName = ?, Employee.email = ?, Employee.homePhone = ?, Employee.cellPhone = ?, "
                + "Employee.hireDate = ?, Employee.role = ?, "
                + "Address.street = ?, Address.unit = ?, Address.city = ?, Address.province = ?, Address.postalCode = ? "
                + "WHERE Employee.employeeId = ? AND Employee.addressId = Address.addressId ";
        try {
            //Creating a connection
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            if(password != null && !password.isEmpty()) {
              //creating a PreparedStatement to update the Employee object
              preparedStatement = dbConnection.prepareStatement(updateEmployeeQuery);
              preparedStatement.setString(1, this.getFname());
              preparedStatement.setString(2, this.getLname());
              preparedStatement.setString(3, this.getEmail());
              preparedStatement.setString(4, this.getHomePhone());
              preparedStatement.setString(5, this.getCellPhone());
              preparedStatement.setString(6, this.getHireDate());
              preparedStatement.setString(7, this.getEmployeeType());
              preparedStatement.setString(8, this.getPassword());
              preparedStatement.setString(9, this.getStreet());
              preparedStatement.setString(10, this.getUnitNum());
              preparedStatement.setString(11, this.getCity());
              preparedStatement.setString(12, this.getProvince());
              preparedStatement.setString(13, this.getPostalCode());
              preparedStatement.setInt(14, id);
            } else {
              //creating a PreparedStatement to update the Employee object
              preparedStatement = dbConnection.prepareStatement(updateEmployeeQuery2);
              preparedStatement.setString(1, this.getFname());
              preparedStatement.setString(2, this.getLname());
              preparedStatement.setString(3, this.getEmail());
              preparedStatement.setString(4, this.getHomePhone());
              preparedStatement.setString(5, this.getCellPhone());
              preparedStatement.setString(6, this.getHireDate());
              preparedStatement.setString(7, this.getEmployeeType());
              preparedStatement.setString(8, this.getStreet());
              preparedStatement.setString(9, this.getUnitNum());
              preparedStatement.setString(10, this.getCity());
              preparedStatement.setString(11, this.getProvince());
              preparedStatement.setString(12, this.getPostalCode());
              preparedStatement.setInt(13, id);
            }

            //execute update sale query
            result = preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return result;
    }

    // Get all employees
    public JSONObject getAll() throws NamingException {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        JSONObject jsonObject = new JSONObject();
        String getAllEmployeesQuery = "SELECT employeeId, CONCAT(firstName, ' ', lastName) AS name, role, "
                + "email, cellPhone, hireDate, isActive FROM Employee";
        try {

            //getting a connection
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            //creating a preparedStatement
            preparedStatement = dbConnection.prepareStatement(getAllEmployeesQuery);
            //execute the prepared statement and get the ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

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
    
 // Get employee by id
    public JSONObject getEmployeeById(int id) throws NamingException {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        JSONObject jsonObject = new JSONObject();

        String getOneEmployeeQuery = "SELECT Employee.employeeId, Employee.firstName, Employee.lastName, Employee.role, Employee.email, "
                + "Employee.homePhone, Employee.cellPhone, Employee.hireDate, Employee.isActive, Address.street, "
                + "Address.unit, Address.city, Address.province, Address.postalCode "
                + "FROM Employee "
                + "JOIN Address ON Employee.addressId = Address.addressId "
                + "WHERE employeeId = ? ";
        try {

            //getting a connection
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            //creating a preparedStatement
            preparedStatement = dbConnection.prepareStatement(getOneEmployeeQuery);
            preparedStatement.setInt(1, id);

            //execute the prepared statement and get the ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            //creating an object to keep a collection of JSONs
            JSONObject employee = new JSONObject();

            //Iterating through the Results and filling the jsonObject
            if (resultSet.next()) {
                employee.put("employeeId", resultSet.getString("employeeId"));
                employee.put("firstName", resultSet.getString("firstName"));
                employee.put("lastName", resultSet.getString("lastName"));
                employee.put("email", resultSet.getString("email"));
                employee.put("homePhone", resultSet.getString("homePhone"));
                employee.put("cellPhone", resultSet.getString("cellPhone"));
                employee.put("hireDate", resultSet.getDate("hireDate"));
                employee.put("isActive", resultSet.getBoolean("isActive"));
                employee.put("address", resultSet.getString("street"));
                employee.put("unit", resultSet.getString("unit"));
                employee.put("city", resultSet.getString("city"));
                employee.put("province", resultSet.getString("province"));
                employee.put("postalCode", resultSet.getString("postalCode"));
                employee.put("role", resultSet.getString("role"));;
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

    // Get all employees
    public JSONObject getEmployeesByRole(String role) throws NamingException {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        role = role.toLowerCase().trim();
        JSONObject jsonObject = new JSONObject();

        String getEmployeesByRoleQuery = "SELECT employeeId, CONCAT(firstName, ' ', lastName) AS name, "
                + "role, email, cellPhone, hireDate, isActive FROM Employee "
                + "WHERE role = ? AND isActive = ? ";
        try {

            //getting a connection
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            //creating a preparedStatement
            preparedStatement = dbConnection.prepareStatement(getEmployeesByRoleQuery);
            preparedStatement.setString(1, role);
            preparedStatement.setBoolean(2, true);

            //execute the query statement and get the ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

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

    public JSONObject updateStatus(int employeeId) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        JSONObject jsonObject = new JSONObject();
        int result = 0;

        String updateStatusQuery = "UPDATE Employee SET isActive = ? WHERE employeeId = ? ";

        try {
            JSONObject employeeObj = this.getEmployeeById(employeeId);

            if(employeeObj.length() > 0) {
                employeeObj = employeeObj.getJSONObject("employee");

                //getting a connection
                conn = new ConnectionManager();
                dbConnection = conn.getDBConnection();

                //creating a preparedStatement
                preparedStatement = dbConnection.prepareStatement(updateStatusQuery);
                //status can be either true or false, so we just toggle it
                preparedStatement.setBoolean(1, !employeeObj.getBoolean("isActive"));
                preparedStatement.setInt(2, employeeId);

                //execute prepared statement
                result = preparedStatement.executeUpdate();

                jsonObject.put("updated", true);
                //TODO validate update
            } else {
                //TODO error handling
            }
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
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

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }
}
