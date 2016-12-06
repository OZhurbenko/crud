package com.esit;

import javax.ws.rs.core.MultivaluedMap;

public class InstallationManager {
    private String saleId;
    private String installerId;
    private String installationDateTime;

    ConnectionManager conn;

    //default constructor, do nothing
    public InstallationManager() {

    }

    //constructor for POST requests
    public InstallationManager(MultivaluedMap<String, String> formParams) {
        this.setSaleId(formParams.get("saleId").get(0));
        this.setInstallerId(formParams.get("installerId").get(0));
        this.setInstallationDateTime(formParams.get("installationDateTime").get(0));
    }

    public int create() {
        int result = 0;
        try {
            //Customer's create closed a connection, so we are creating a new one
            conn = new ConnectionManager();

            //TODO validate salesId and installerId before creating a new installation

            //create new installation object
            String newInstallationQuery = "INSERT INTO Installation ("
                    + "installer, sale, installationDateTime, status) "
                    + "VALUES(" + this.getInstallerId() + ", " + this.getSaleId() + ", '"
                    + this.getInstallationDateTime() + "', " + "'In Progress')";

            //execute new installation query
            result = conn.executeUpdate(newInstallationQuery);

            String updateSaleQuery = "UPDATE Sale set status='Waiting for the installation' "
                    + "WHERE saleId=" + this.getSaleId();

            //execute update sale query
            result = conn.executeUpdate(updateSaleQuery);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close the connection to the database
            conn.closeConnection();
        }

        return result;
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
}
