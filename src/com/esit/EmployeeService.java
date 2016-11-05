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

@Path("/EmployeeService")
public class EmployeeService {
      Repository repository;

      // Get all employees
      @Path("getAllEmployees")
      @GET
      @Produces("application/json")
      public Response getAllEmployees() throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getAllEmployees() + "";
        return Response.status(200).entity(result).build();
      }
      
      // Get employee by id
      @Path("getEmployeeById/{id}")
      @GET
      @Produces("application/json")
      public Response getEmployeeById(@PathParam("id") int id) throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getEmployeeById(id) + "";
        return Response.status(200).entity(result).build();
      }

}