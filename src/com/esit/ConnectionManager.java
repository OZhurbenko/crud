package com.esit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class ConnectionManager {
    DataSource datasource;
    Connection connect;
    Statement statement;
    ResultSet resultSet;
    int result;

    public ConnectionManager() {
        this.statement = null;
        this.resultSet = null;
        this.result = 0;

        // Get the context and create a connection
        Context initCtx;
        Context envCtx;
        try {
            initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup("java:comp/env");
            this.datasource = (DataSource)envCtx.lookup("jdbc/esit");
            this.connect = this.datasource.getConnection();
        } catch (NamingException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            // Create the statement to be used to get the results.
            statement = connect.createStatement();

            // Execute the query and get the result set.
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int executeUpdate(String query) {
        try {
            // Create the statement to be used to get the results.
            statement = connect.createStatement();

            // Execute the query and get the result set.
            this.result = statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void closeConnection(){
        try {
            if(statement != null) {
                statement.close();
            }
            if(connect != null) {
                connect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
