package com.esit;

import java.sql.ResultSet;

import javax.ws.rs.core.MultivaluedMap;

public class SaleManager {

    CustomerManager customer;
    ConnectionManager conn;
    private String programType;
    private String installationDate;
    private String installationTime;
    private String notes;
    private String dateSigned;
    private String salesRepId;

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
        this.installationDate = formParams.get("installationDate").get(0);
        this.installationTime = formParams.get("installationTime").get(0);
        this.notes = formParams.get("notes").get(0);
        this.dateSigned = formParams.get("dateSigned").get(0);
        this.salesRepId = formParams.get("salesRepId").get(0);
    }

    public int create() {
        int result = 0;
        try {
            int customerID = 0;

            //getting a connection to the Database
            conn = new ConnectionManager();

            customerID = customer.create(conn);

            //Customer's create closed a connection, so we are creating a new one
            conn = new ConnectionManager();

            //TODO validate salesRepId and program before creating a new Sale object

            //create new sale object
            String newSaleQuery = "INSERT INTO Sale ("
                    + "customer, salesRepId, program, "
                    + "rentalAgreement, PADForm, dateSigned, "
                    + "installationDateTime, notes, status) "
                    + "VALUES(" + customerID + ", " + this.getSalesRepI() + ", " + this.getProgramType()
                    + ", NULL, NULL, " + "'2016-09-20', "
                    + "'2016-09-22 08:00:00', '" + this.getNotes() + "', " + "'In progress')";

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
    public String getInstallationDate() {
        return installationDate;
    }
    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }
    public String getProgramType() {
        return programType;
    }
    public void setProgramType(String programType) {
        this.programType = programType;
    }
    public String getInstallationTime() {
        return installationTime;
    }
    public void setInstallationTime(String installationTime) {
        this.installationTime = installationTime;
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
    public String getSalesRepI() {
        return salesRepId;
    }
    public void setSalesRepId(String salesRepId) {
        this.salesRepId = salesRepId;
    }
}
