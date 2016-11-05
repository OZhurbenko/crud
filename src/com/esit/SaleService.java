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

@Path("/SaleService")
public class SaleService {
      Repository repository;

      // Get all sales
      @Path("getAllSales")
      @GET
      @Produces("application/json")
      public Response getAllSales() throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getAllSales() + "";
        return Response.status(200).entity(result).build();
      }
      
      // Get sale by Id
      @Path("getSaleById/{id}")
      @GET
      @Produces("application/json")
      public Response getSaleById(@PathParam("id") int id) throws JSONException, NamingException {
        repository = new Repository();
        String result = repository.getSaleById(id) + "";
        return Response.status(200).entity(result).build();
      }

}