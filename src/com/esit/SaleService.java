package com.esit;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

@Path("/SaleService")
public class SaleService {
      // Get all sales
      @Path("getAllSales")
      @GET
      @Produces("application/json")
      public Response getAllSales() throws JSONException, NamingException {
        SaleManager sales = new SaleManager();
        String result = sales.getAllSales() + "";
        return Response.status(200).entity(result).build();
      }
      
      // Get all sales
      @Path("getAllCompletedSales")
      @GET
      @Produces("application/json")
      public Response getAllCompletedSales() throws JSONException, NamingException {
        SaleManager sales = new SaleManager();
        String result = sales.getAllCompleted() + "";
        return Response.status(200).entity(result).build();
      }

      // Get sale by Id
      @Path("getSaleById/{id}")
      @GET
      @Produces("application/json")
      public Response getSaleById(@PathParam("id") int id) throws JSONException, NamingException {
        SaleManager sale = new SaleManager();
        String result = sale.getSaleById(id) + "";
        return Response.status(200).entity(result).build();
      }

      @POST
      @Path("/createNewSale")
      @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
      public Response create(MultivaluedMap<String, String> formParams) {
          SaleManager sale = new SaleManager(formParams);
          int result = sale.create();

          JSONObject jsonObj = new JSONObject();
          jsonObj.put("result", result);

          if(result != 0) {
              return Response.status(200).entity(jsonObj + "").build();
          } else {
              return Response.status(400).build();
          }
      }
}
