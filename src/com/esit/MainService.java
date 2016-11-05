package com.esit;

import javax.naming.NamingException;
import javax.ws.rs.GET;
//import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
//import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import org.json.JSONException;
//import org.json.JSONObject;

@Path("/")
public class MainService {
      Repository repository;
      
      // Get all employees
      @Path("getAllEmployees")
      @GET
      @Produces("application/json")
      public Response getAllEmployees() throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getAllEmployees();
        return Response.status(200).entity(result).build();
      }
      
      // Get employee by id
      @Path("getEmployeeById/{id}")
      @GET
      @Produces("application/json")
      public Response getEmployeeById(@PathParam("id") int id) throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getEmployeeById(id);
        return Response.status(200).entity(result).build();
      }

      // Get all sales
      @Path("getAllSales")
      @GET
      @Produces("application/json")
      public Response getAllSales() throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getAllSales();
        return Response.status(200).entity(result).build();
      }
      
      // Get sale by Id
      @Path("getSaleById/{id}")
      @GET
      @Produces("application/json")
      public Response getSaleById(@PathParam("id") int id) throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getSaleById(id);
        return Response.status(200).entity(result).build();
      }
      
      // Get all customers
      @Path("getAllCustomers")
      @GET
      @Produces("application/json")
      public Response getAllCustomers() throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getAllCustomers();
        return Response.status(200).entity(result).build();
      }
      
      // Get customer by Id
      @Path("getCustomerById/{id}")
      @GET
      @Produces("application/json")
      public Response getCustomerById(@PathParam("id") int id) throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getCustomerById(id);
        return Response.status(200).entity(result).build();
      }
      
//      // Create new employee
//      @Path("addEmployee")
//      @POST
//      @Consumes("application/json")
//      public Response addCustomer(JSONObject newCustomer) throws JSONException, NamingException {
//        repository = new Repository();
//        System.console().writer().println(newCustomer);
//        String result = repository.addEmployee(newCustomer);
//        return Response.status(201).entity(result).build();
//      }
}