package com.esit;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
//import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import org.json.JSONException;
//import org.json.JSONObject;
import org.json.JSONObject;

@Path("/EmployeeService")
public class EmployeeService {
      Repository repository;

      // Get all employees
      @Path("getAllEmployees")
      @GET
      @Produces("application/json")
      public Response getAllEmployees() throws JSONException, NamingException {
        EmployeeManager employee = new EmployeeManager();
        String result = employee.getAll() + "";
        return Response.status(200).entity(result).build();
      }

      // Get all employees
      @Path("getAllInstallers")
      @GET
      @Produces("application/json")
      public Response getAllInstallers() throws JSONException, NamingException {
        EmployeeManager employee = new EmployeeManager();
        String result = employee.getAllInstallers() + "";
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

      @POST
      @Path("/createNewEmployee")
      @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
      public Response create(MultivaluedMap<String, String> formParams) {
          EmployeeManager employee = new EmployeeManager(formParams);
          int result = employee.create();

          JSONObject jsonObj = new JSONObject();
          jsonObj.put("fname", employee.getFname());
          jsonObj.put("lname", employee.getLname());
          jsonObj.put("street", employee.getStreet());
          jsonObj.put("unitNum", employee.getUnitNum());
          jsonObj.put("city", employee.getCity());
          jsonObj.put("province", employee.getProvince());
          jsonObj.put("postalCode", employee.getPostalCode());
          jsonObj.put("email", employee.getEmail());
          jsonObj.put("homePhone", employee.getHomePhone());
          jsonObj.put("cellPhone", employee.getCellPhone());
          jsonObj.put("password", employee.getPassword());
          jsonObj.put("isActive", employee.getIsActive());
          jsonObj.put("result", result);

          if(result != 0) {
              return Response.status(200).entity(jsonObj + "").build();
          } else {
              return Response.status(400).build();
          }
      }
}