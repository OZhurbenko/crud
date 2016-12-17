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

@Path("/CustomerService")
public class CustomerService {
      // Get all customers
      @Path("getAllCustomers")
      @GET
      @Produces("application/json")
      public Response getAllCustomers() throws JSONException, NamingException {
        CustomerManager customer = new CustomerManager();
        String result = customer.getAllCustomers() + "";
        return Response.status(200).entity(result).build();
      }
      
      // Get customer by Id
      @Path("getCustomerById/{id}")
      @GET
      @Produces("application/json")
      public Response getCustomerById(@PathParam("id") int id) throws JSONException, NamingException {
        CustomerManager customer = new CustomerManager();
        String result = customer.getCustomerById(id) + "";
        return Response.status(200).entity(result).build();
      }

}