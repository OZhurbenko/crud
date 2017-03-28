package com.esit;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.NamingException;
import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONObject;

public class SaleManager {

    CustomerManager customer;
    ConnectionManager conn;
    private String programType;
    private String installationDateTime;
    private String notes;
    private String dateSigned;
    private String salesRepId;
    private String folderId;
    private String envelopeId;
    private String status;

    //default constructor, do nothing
    public SaleManager() {

    }

    //constructor
    public SaleManager(MultivaluedMap<String, String> formParams) {
        customer = new CustomerManager(
                formParams.get("fname").get(0),
                formParams.get("lname").get(0),
                formParams.get("email").get(0),
                formParams.get("homePhone").get(0),
                formParams.get("cellPhone").get(0),
                formParams.get("enbridge").get(0),
                formParams.get("address").get(0),
                formParams.get("unitNum").get(0),
                formParams.get("city").get(0),
                formParams.get("province").get(0),
                formParams.get("postalCode").get(0)
                );

        this.programType = formParams.get("programType").get(0);
        this.installationDateTime = formParams.get("installationDateTime").get(0);
        this.notes = formParams.get("notes").get(0);
        this.dateSigned = formParams.get("dateSigned").get(0);
        this.salesRepId = formParams.get("salesRepId").get(0);
    }
    
    public int setSaleStatus(int id, MultivaluedMap<String, String> formParams) {
        int result = 0;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        String updateSaleQuery = "UPDATE Sale SET status = ? WHERE saleId = ?";
        String getSaleQuery = "SELECT saleId, status FROM Sale WHERE saleId = ?";
        try {

        	//getting a connection to the Database
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            //update Sale status query
            preparedStatement = dbConnection.prepareStatement(updateSaleQuery);
            preparedStatement.setString(1, formParams.get("status").get(0));
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            //select Sale status query
            preparedStatement = dbConnection.prepareStatement(getSaleQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            String status = formParams.get("status").get(0);
            this.setStatus(status);

            if(resultSet.next() && resultSet.getString("status").equals(status)) {
            	result = Integer.parseInt(resultSet.getString("saleId"));
            } else {
                System.out.println("RESULT: " + result);
            	result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return result;
    }

    public JSONObject getAllCompleted() throws NamingException {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        JSONObject jsonObject = new JSONObject();

        String selectSalesQuery = "SELECT Sale.saleId, CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
                + "Program.programName, Address.street, Sale.installationDateTime, Sale.status, Sale.folderId "
                + "FROM Sale "
                + "JOIN Customer ON Sale.customer = Customer.customerId "
                + "JOIN Program ON Sale.program = Program.programId "
                + "JOIN Property ON Sale.customer = Property.customer "
                + "JOIN Address ON Property.address = Address.addressId "
                + "WHERE status = ? ";

        try {
            //getting a connection to the Database
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            //creating and executing a PreparedStatement object
            preparedStatement = dbConnection.prepareStatement(selectSalesQuery);
            preparedStatement.setString(1, "Paid");

            //execute the prepared statement and retrieve the ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

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
              tempJson.put("installationDateTime", resultSet.getString("installationDateTime"));
              tempJson.put("status", resultSet.getString("status"));
              tempJson.put("folderId", resultSet.getString("folderId"));
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

    public int create() {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        int result = 0;
        int customerID = 0;
        int salesRepId = 0;
        int programType = 0;
        int saleId = 0;

        String getSalesRepIdQuery = "SELECT employeeId FROM Employee WHERE employeeId = ? AND role = ? ";
        String getProgramTypeQuery = "SELECT programId FROM Program WHERE programId = ? ";
        String newSaleQuery = "INSERT INTO Sale (customer, salesRepId, program, rentalAgreement, PADForm, dateSigned, installationDateTime, notes, status) "
                + "VALUES( ?, ?, ?, NULL, NULL, ?, ?, ?, ?)";
        String getSaleQuery = "SELECT saleId FROM Sale WHERE customer = ? AND salesRepId = ? "
                + "AND program = ? AND dateSigned = ? AND installationDateTime = ? AND status = ?";
        try {

            //getting a connection to the Database
            conn = new ConnectionManager();

            //attempting to create a new customer
            customerID = customer.create(conn);

            //if returned id > 0 then everything went well
            if(customerID > 0) {

                //Customer's create closed a connection, so we are creating a new one
                conn = new ConnectionManager();
                dbConnection = conn.getDBConnection();

                //validating sales rep id
                preparedStatement = dbConnection.prepareStatement(getSalesRepIdQuery);
                preparedStatement.setInt(1, Integer.parseInt(this.getSalesRepId()));
                preparedStatement.setString(2, "salesperson");

                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()) {
                  salesRepId = Integer.parseInt(resultSet.getString("employeeId"));
                }

                //validating programType
                preparedStatement = dbConnection.prepareStatement(getProgramTypeQuery);
                preparedStatement.setInt(1, Integer.parseInt(this.getProgramType()));
                resultSet = preparedStatement.executeQuery();

                if(resultSet.next()) {
                  programType = Integer.parseInt(resultSet.getString("programId"));
                }

                //checking if we had valid salesRepId and programType before creating a new Sale
                if(salesRepId > 0 && programType > 0) {
                  //creating a new sale object
                  preparedStatement = dbConnection.prepareStatement(newSaleQuery);
                  preparedStatement.setInt(1, customerID);
                  preparedStatement.setInt(2, Integer.parseInt(this.getSalesRepId()));
                  preparedStatement.setInt(3, Integer.parseInt(this.getProgramType()));
                  preparedStatement.setString(4, this.getDateSigned());
                  preparedStatement.setString(5, this.getInstallationDateTime());
                  preparedStatement.setString(6, this.getNotes());
                  preparedStatement.setString(7, "Created");

                  //execute new sale query
                  result = preparedStatement.executeUpdate();

                  //checking whether we created a Sale
                  preparedStatement = dbConnection.prepareStatement(getSaleQuery);
                  preparedStatement.setInt(1, customerID);
                  preparedStatement.setInt(2, Integer.parseInt(this.getSalesRepId()));
                  preparedStatement.setInt(3, Integer.parseInt(this.getProgramType()));
                  preparedStatement.setString(4, this.getDateSigned());
                  preparedStatement.setString(5, this.getInstallationDateTime());
                  preparedStatement.setString(6, "Created");
                  resultSet = preparedStatement.executeQuery();

                  if(resultSet.next()) {
                    saleId = Integer.parseInt(resultSet.getString("saleId"));
                  }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return saleId;
    }
    
    public int putEnvelopeId(int id, MultivaluedMap<String, String> formParams) {
        int result = 0;
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        //update sale query
        String updateSaleQuery = "UPDATE Sale SET envelopeId = ? WHERE saleId = ? ";
        //select Sale query
        String selectSaleQuery = "SELECT saleId, envelopeId FROM Sale WHERE saleId = ? ";

        try {
            String envelopeId = formParams.get("envelopeId").get(0);

            //getting a connection to the Database
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            //update Sale query
            preparedStatement = dbConnection.prepareStatement(updateSaleQuery);
            preparedStatement.setString(1, formParams.get("envelopeId").get(0));
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();

            // Set the folderId
            this.setEnvelopeId(envelopeId);
            System.out.println("envelopeId: " + this.getEnvelopeId());

            //checking whether we created a Sale
            preparedStatement = dbConnection.prepareStatement(selectSaleQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
            	result = Integer.parseInt(resultSet.getString("saleId"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return result;
    }

    public int setFolderId(int id, MultivaluedMap<String, String> formParams) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;

        //update sale query
        String updateSaleQuery = "UPDATE Sale SET folderId = ? WHERE saleId = ? ";
        //get one sale query
        String getSaleQuery = "SELECT saleId, folderId FROM Sale WHERE saleId = ? ";
        try {

        	//getting a connection to the Database
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            // Set the folderId
            this.setFolderId(formParams.get("folderId").get(0));
            String folderId = this.getFolderId();
            System.out.println("get: " + this.getFolderId());

            //update sale object
            preparedStatement = dbConnection.prepareStatement(updateSaleQuery);
            preparedStatement.setString(1, this.getFolderId());
            preparedStatement.setInt(2, id);

            //execute update sale query
            result = preparedStatement.executeUpdate();

            //checking whether we created a Sale
            preparedStatement = dbConnection.prepareStatement(getSaleQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                if (resultSet.getString("folderId") == folderId) {
                    result = Integer.parseInt(resultSet.getString("saleId"));
                }
          }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return result;
    }
    
    // Get all sales
    public JSONObject getAllSales(int id) throws NamingException {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        JSONObject jsonObject = new JSONObject();
        String getAllSalesPersonSalesQuery =  "SELECT Sale.saleId, CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
                + "Program.programName, Address.street, Sale.dateSigned, Sale.status FROM Sale "
                + "JOIN Customer ON Sale.customer = Customer.customerId "
                + "JOIN Program ON Sale.program = Program.programId "
                + "JOIN Property ON Sale.customer = Property.customer "
                + "JOIN Address ON Property.address = Address.addressId "
                + "WHERE Sale.salesRepId = ? ";
        String getAllSalesQuery =  "SELECT Sale.saleId, CONCAT(Customer.firstName, ' ', Customer.lastName) AS customerName, "
                + "Program.programName, Address.street, Sale.dateSigned, Sale.status FROM Sale "
                + "JOIN Customer ON Sale.customer = Customer.customerId "
                + "JOIN Program ON Sale.program = Program.programId "
                + "JOIN Property ON Sale.customer = Property.customer "
                + "JOIN Address ON Property.address = Address.addressId";
        try {

            //creating a new EmployeeManager obj, getting an employee json and retrieve the employee's role
            EmployeeManager employeeMngr = new EmployeeManager();
            JSONObject jsonObj = new JSONObject();
            jsonObj = employeeMngr.getEmployeeById(id);
            JSONObject employee = jsonObj.getJSONObject("employee");
            String role = employee.getString("role");

            //getting a connection to the Database
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();

            //validating the role
            if(role != null && !role.isEmpty()) {
                //if the role equals "salesperson", then we return only sales related to that employee
                if(role.equals("salesperson")) {
                  preparedStatement = dbConnection.prepareStatement(getAllSalesPersonSalesQuery);
                  preparedStatement.setInt(1, id);
                //any role other than "salesperson" means that we return all the sales from the database
                } else {
                  preparedStatement = dbConnection.prepareStatement(getAllSalesQuery);
                }
            }

            //execute the query statement and get the ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

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
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        //final json object
        JSONObject jsonObject = new JSONObject();

        //get one sale query
        String getOneSaleQuery = "SELECT Sale.saleId, Customer.firstName, Customer.lastName, Program.programName, Program.programId, "
                + "Address.street, Address.unit, Address.city, Address.province, Address.postalCode, "
                + "Customer.enbridgeNum, Customer.email, Customer.homePhone, Customer.cellPhone, "
                + "Sale.installationDateTime, Sale.notes, Sale.folderId, Sale.envelopeId, Sale.status, Sale.dateSigned, Sale.salesRepId, "
                + "CONCAT(Employee.firstName, ' ', Employee.lastName) AS salesRepName "
                + "FROM Sale "
                + "JOIN Employee ON Sale.salesRepId = Employee.employeeId "
                + "JOIN Customer ON Sale.customer = Customer.customerId "
                + "JOIN Program ON Sale.program = Program.programId "
                + "JOIN Property ON Sale.customer = Property.customer "
                + "JOIN Address ON Property.address = Address.addressId "
                + "WHERE Sale.saleId = ? ";
        try {

            //create a new Query object
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();
            preparedStatement = dbConnection.prepareStatement(getOneSaleQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

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
              sale.put("installationDateTime", resultSet.getString("installationDateTime"));
              sale.put("notes", resultSet.getString("notes"));
              sale.put("status", resultSet.getString("status"));
              sale.put("salesRepId", resultSet.getString("salesRepId"));
              sale.put("salesRepName", resultSet.getString("salesRepName"));
              sale.put("folderId", resultSet.getString("folderId"));
              sale.put("envelopeId", resultSet.getString("envelopeId"));
              sale.put("dateSigned", resultSet.getString("dateSigned"));
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

    
    // Get sale by Id
    public JSONObject getFolderIdByEnvelopeId(String id) throws NamingException {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        //final json object to return
        JSONObject jsonObject = new JSONObject();

        //select folderId queries
        String selectSaleFolderIdQuery = "SELECT saleId, folderId FROM Sale WHERE envelopeId LIKE ? ";
        String selectInstallationFolderId = "SELECT sale, folderId FROM Installation WHERE envelopeId LIKE ?";
        try {
            
            //create a new Query object
            conn = new ConnectionManager();
            dbConnection = conn.getDBConnection();
            preparedStatement = dbConnection.prepareStatement(selectSaleFolderIdQuery);
            preparedStatement.setString(1, id);
            //execute the query statement and get the ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            //creating a temporary JSON object for the data from the database
            JSONObject sale = new JSONObject();

            // If there are results fill the jsonObject
            if (resultSet.next()) {
              String folderId = resultSet.getString("folderId");
              sale.put("salesNumber", resultSet.getString("saleId"));
              sale.put("folderId", folderId == null || folderId.isEmpty() ? "0" : folderId);
            } else {
              // See if envelopeId exists in Installation
              preparedStatement = dbConnection.prepareStatement(selectInstallationFolderId);
              preparedStatement.setString(1, id);
              // Execute the query statement and get the ResultSet
              ResultSet resultSet2 = preparedStatement.executeQuery();

              // If there are results fill the jsonObject
              if (resultSet2.next()) {
                  sale.put("salesNumber", resultSet2.getString("sale"));
                  sale.put("folderId", resultSet2.getString("folderId"));
              } else {
                  // Default to 'unsorted' folder on Box.com
                  sale.put("salesNumber", "0");
                  sale.put("folderId", "15932309040");
              }
            }

            //filling a final JSON object
            jsonObject.put("sale", sale);

          } catch (Exception e) {
              e.printStackTrace();
          } finally {
              //close the connection to the database
              conn.closeConnection();
          }
        return jsonObject;
    }


    public String getFname() {
        return customer.getFname();
    }
    public void setFname(String fname) {
        customer.setFname(fname);
    }
    public String getAddress() {
        return customer.getAddress();
    }
    public void setAddress(String address) {
        customer.setAddress(address);
    }
    public String getLname() {
        return customer.getLname();
    }
    public void setLname(String lname) {
        customer.setLname(lname);
    }
    public String getUnitNum() {
        return customer.getUnitNum();
    }
    public void setUnitNum(String unitNum) {
        customer.setUnitNum(unitNum);
    }
    public String getCity() {
        return customer.getCity();
    }
    public void setCity(String city) {
        customer.setCity(city);
    }
    public String getProvince() {
        return customer.getProvince();
    }
    public void setProvince(String province) {
        customer.setProvince(province);
    }
    public String getPostalCode() {
        return customer.getPostalCode();
    }
    public void setPostalCode(String postalCode) {
        customer.setPostalCode(postalCode);
    }
    public String getEnbridge() {
        return customer.getEnbridge();
    }
    public void setEnbridge(String enbridge) {
        customer.setEnbridge(enbridge);
    }
    public String getHomePhone() {
        return customer.getHomePhone();
    }
    public void setHomePhone(String homePhone) {
        customer.setHomePhone(homePhone);
    }
    public String getEmail() {
        return customer.getEmail();
    }
    public void setEmail(String email) {
        customer.setEmail(email);
    }
    public String getCellPhone() {
        return customer.getCellPhone();
    }
    public void setCellPhone(String cellPhone) {
        customer.setCellPhone(cellPhone);
    }
    public String getInstallationDateTime() {
        return installationDateTime;
    }
    public void setInstallationDateTime(String installationDateTime) {
        this.installationDateTime = installationDateTime;
    }
    public String getProgramType() {
        return programType;
    }
    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getDateSigned() {
        return dateSigned;
    }
    public void setDateSigned(String dateSigned) {
        this.dateSigned = dateSigned;
    }
    public String getSalesRepId() {
        return salesRepId;
    }
    public void setSalesRepId(String salesRepId) {
        this.salesRepId = salesRepId;
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
