package com.esit;

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
        this.setIsActive(formParams.get("isActive").get(0));
    }

    //create a new Employee and return his id
    public int create() {
        int addressId = 0;
        int employeeId = 0;
        int result = 0;
        try {

            conn = new ConnectionManager();

            //create new Address query
            String newAddressQuery = "INSERT INTO Address (street, unit, city, province, postalCode) "
                    + "VALUES ('" + this.getStreet() + "', '" + this.getUnitNum() + "', '" + this.getCity() 
                    + "', '" + this.getProvince() + "', '" + this.getPostalCode() + "')";
            //execute create new Address query and get the confirmation
            result = conn.executeUpdate(newAddressQuery);

            //getting the id of the new Address object
            String getAddressIdQuery = "SELECT addressId "
                    + "FROM Address "
                    + "WHERE street = '" + this.getStreet() + "'"
                    + " AND unit = '" + this.getUnitNum() + "'"
                    + " AND city = '" + this.getCity() + "'"
                    + " AND province = '" + this.getProvince() + "'"
                    + " AND postalCode = '" + this.getPostalCode() + "'";

            ResultSet resultSet = conn.executeQuery(getAddressIdQuery);
            resultSet = conn.executeQuery(getAddressIdQuery);
            if(resultSet.next()) {
                addressId = Integer.parseInt(resultSet.getString("addressId"));
            }

            //check if Address object was created
            if(addressId > 0) {
              //create new Employee object
              String newEmployeeQuery = "INSERT INTO Employee ("
                      + "firstName, lastName, email, "
                      + "homePhone, cellPhone, hireDate, "
                      + "isActive, password, role, addressId) "
                      + "VALUES('" + this.getFname() + "', '" + this.getLname() + "', '" + this.getEmail()
                      + "', '" + this.getHomePhone() + "', '" + this.getCellPhone() + "', '" + this.getHireDate()
                      + "', " + this.getIsActive() + ", '" + this.getPassword() + "', '" + this.getEmployeeType()
                      + "', " + addressId + ")";

              //execute create new Property query here and get the result
              result = conn.executeUpdate(newEmployeeQuery);

              //getting the id of the new Employee object
              String getEmployeeIdQuery = "SELECT employeeId "
                      + "FROM Employee "
                      + "WHERE email = '" + this.getEmail() + "'";

              resultSet = conn.executeQuery(getEmployeeIdQuery);

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
    public int update(int id, MultivaluedMap<String, String> formParams) {
        int result = 0;
        try {
            //Creating a connection
            conn = new ConnectionManager();

            String fname = formParams.get("fname").get(0);
            String lname = formParams.get("lname").get(0);
            String email = formParams.get("email").get(0);
            String homePhone = formParams.get("homePhone").get(0);
            String cellPhone = formParams.get("cellPhone").get(0);
            String hireDate = formParams.get("hireDate").get(0);
            String employeeType = formParams.get("employeeType").get(0);

            String street = formParams.get("street").get(0);
            String unitNum = formParams.get("unitNum").get(0);
            String city = formParams.get("city").get(0);
            String province = formParams.get("province").get(0);
            String postalCode = formParams.get("postalCode").get(0);

            //password is optional
            String password = formParams.get("password").get(0);

            String updateQuery = "UPDATE Employee, Address "
                                + "SET Employee.firstName = '" + fname + "', "
                                + "Employee.lastName = '" + lname + "', "
                                + "Employee.email = '" + email + "', "
                                + "Employee.homePhone = '" + homePhone + "', "
                                + "Employee.cellPhone = '" + cellPhone + "', "
                                + "Employee.hireDate = '" + hireDate + "', "
                                + "Employee.role = '" + employeeType + "', "
                                + (password != null && !password.isEmpty() ?
                                        "Employee.password = '" + password + "', " : "" )
                                + "Address.street = '" + street + "', "
                                + "Address.unit = '" + unitNum + "', "
                                + "Address.city = '" + city + "', "
                                + "Address.province = '" + province + "', "
                                + "Address.postalCode = '" + postalCode + "' "
                                + "WHERE Employee.employeeId = " + id + " "
                                + "AND Employee.addressId = Address.addressId ";

            //execute update sale query
            result = conn.executeUpdate(updateQuery);

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
    
 // Get employee by id
    public JSONObject getEmployeeById(int id) throws NamingException {
        JSONObject jsonObject = new JSONObject();
        try {
            //create a query string
            String _query = "SELECT Employee.employeeId, "
                    + "Employee.firstName, "
                    + "Employee.lastName, "
                    + "Employee.role, "
                    + "Employee.email, "
                    + "Employee.homePhone, "
                    + "Employee.cellPhone, "
                    + "Employee.hireDate, "
                    + "Employee.isActive, "
                    + "Address.street, "
                    + "Address.unit, "
                    + "Address.city, "
                    + "Address.province, "
                    + "Address.postalCode "
                    + "FROM Employee "
                    + "JOIN Address ON Employee.addressId = Address.addressId "
                    + "WHERE employeeId = " + id;

            //create a new Query object
            conn = new ConnectionManager();

            //execute the query statement and get the ResultSet
            ResultSet resultSet = conn.executeQuery(_query);

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
        role = role.toLowerCase().trim();
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
                    + "WHERE role = '" + role + "' "
                    + "AND isActive = true";

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

    public JSONObject updateStatus(int employeeId) {
        JSONObject jsonObject = new JSONObject();
        int result = 0;
        try {
            JSONObject employeeObj = this.getEmployeeById(employeeId);

            if(employeeObj.length() > 0) {
                employeeObj = employeeObj.getJSONObject("employee");
                //status can be either true or false, so we just toggle it
                String updateQuery = "UPDATE Employee SET "
                      + "isActive = " + !employeeObj.getBoolean("isActive")
                      + " "
                      + "WHERE employeeId = " + employeeId;

                conn = new ConnectionManager();
                //execute new sale query
                result = conn.executeUpdate(updateQuery);

                jsonObject.put("updated", true);
                //TODO validate update
            } else {
                //TODO error handling
            }
        } catch (NamingException e) {
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
