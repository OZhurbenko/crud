package com.esit;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/EmployeeService")
public class EmployeeService {
  // Get all employees
  @Path("getAllEmployees")
  @GET
  @Produces("application/json")
  public Response getAllEmployees() throws JSONException, NamingException {
    EmployeeManager employee = new EmployeeManager();
    JSONObject jsonObj = employee.getAll();

    if(jsonObj.has("error")) {
      return Response.status(jsonObj.getInt("error")).build();
    } else {
      String result = jsonObj + "";
      return Response.status(200).entity(result).build();
    }
  }

  // Get all employees
  @Path("getEmployeesByRole/{role}")
  @GET
  @Produces("application/json")
  public Response getEmployeesByRole(@PathParam("role") String role) throws JSONException, NamingException {
    EmployeeManager employee = new EmployeeManager();
    JSONObject jsonObj = employee.getEmployeesByRole(role);

    if(jsonObj.has("error")) {
      return Response.status(jsonObj.getInt("error")).build();
    } else {
      String result = jsonObj + "";
      return Response.status(200).entity(result).build();
    }
  }

  // Update Employee object
  @PUT
  @Path("/updateEmployee/{id}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateInstallation(@PathParam("id") int id, MultivaluedMap<String, String> formParams) throws NamingException {
    EmployeeManager employee = new EmployeeManager(formParams);


    JSONObject jsonObj = employee.update(id);

    if(jsonObj.has("error")) {
      return Response.status(jsonObj.getInt("error")).build();
    } else {
      return Response.status(200).build();
    }
  }

  // Get all employees
  @PUT
  @Path("setStatus/{employeeId}")
  @Produces("application/json")
  public Response updateEmployeeStatus(@PathParam("employeeId") int employeeId) throws JSONException, NamingException {
    EmployeeManager employee = new EmployeeManager();
    JSONObject jsonObj = employee.updateStatus(employeeId);

    if(jsonObj.has("error")) {
      return Response.status(jsonObj.getInt("error")).build();
    } else {
      String result = jsonObj + "";
      return Response.status(200).entity(result).build();
    }
  }

  // Get employee by id
  @Path("getEmployeeById/{id}")
  @GET
  @Produces("application/json")
  public Response getEmployeeById(@PathParam("id") int id) throws JSONException, NamingException {
    EmployeeManager employee = new EmployeeManager();
    JSONObject jsonObj = employee.getEmployeeById(id);

    if(jsonObj.has("error")) {
      return Response.status(jsonObj.getInt("error")).build();
    } else {
      String result = jsonObj + "";
      return Response.status(200).entity(result).build();
    }
  }

  @POST
  @Path("/createNewEmployee")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response create(MultivaluedMap<String, String> formParams) {
    EmployeeManager employee = new EmployeeManager(formParams);
    JSONObject jsonObj = employee.create();

    if(jsonObj.has("error")) {
      return Response.status(jsonObj.getInt("error")).build();
    } else {
      return Response.status(201).build();
    }
  }
}
