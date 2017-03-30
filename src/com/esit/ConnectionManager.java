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
    System.out.println("ConnectionManager: establishing a db connection");

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

  public Connection getDBConnection() {
    return this.connect;
  }

  public void closeConnection(){
    try {
      if(statement != null) {
        System.out.println("ConnectionManager: Closing the Statement");
        statement.close();
      }
      if(connect != null) {
        System.out.println("ConnectionManager: Closing the Connection");
        connect.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
