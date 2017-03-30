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

@Path("/SaleService")
public class SaleService {
  // Get all sales
  @Path("getAllSales/{id}")
  @GET
  @Produces("application/json")
  public Response getAllSales(@PathParam("id") int id) throws JSONException, NamingException {
    SaleManager sales = new SaleManager();
    String result = sales.getAllSales(id) + "";
    return Response.status(200).entity(result).build();
  }

  // Get all sales
  @Path("getAllCompletedSales")
  @GET
  @Produces("application/json")
  public Response getAllCompletedSales() throws NamingException {
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
  public Response create(MultivaluedMap<String, String> formParams) throws NamingException {
    SaleManager sale = new SaleManager(formParams);
    JSONObject saleIdJson = sale.create();

    JSONObject saleObjJson = new JSONObject();
    if(!saleIdJson.has("error")) {
      saleObjJson = sale.getSaleById(saleIdJson.getInt("saleId"));
    } else {
      //entity with the error message can be returned in case if there is a conflict (409, etc)
      JSONObject jsonObject = new JSONObject();
      if(saleIdJson.getInt("error") == 409) {
        System.out.println("Got 409, error message: " + saleIdJson.get("errorMessage"));
        jsonObject.put("errorMessage", saleIdJson.get("errorMessage"));
      }
      return Response.status(saleIdJson.getInt("error")).entity(jsonObject + "").build();
    }

    if(!saleObjJson.has("error")) {
      return Response.status(201).entity(saleObjJson + "").build();
    } else {
      return Response.status(saleObjJson.getInt("error")).build();
    }
  }

  @PUT
  @Path("/setEnvelopeId/{id}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response setEnvelopeId(@PathParam("id") int id, MultivaluedMap<String, String> formParams) throws NamingException {
    SaleManager sale = new SaleManager();
    JSONObject saleIdJson = sale.putEnvelopeId(id, formParams);

    if(!(saleIdJson.has("error"))) {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("saleId", saleIdJson.getInt("saleId"));
      return Response.status(200).entity(jsonObj + "").build();
    } else {
      return Response.status(saleIdJson.getInt("error")).build();
    }
  }

  @PUT
  @Path("/setFolderId/{id}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response setFolderId(@PathParam("id") int id, MultivaluedMap<String, String> formParams) throws NamingException {
    SaleManager sale = new SaleManager();
    JSONObject saleIdJson = sale.setFolderId(id, formParams);

    if(!(saleIdJson.has("error"))) {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("folderId", saleIdJson.getInt("saleId"));
      return Response.status(200).entity(jsonObj + "").build();

    } else {
      return Response.status(saleIdJson.getInt("error")).build();
    }
  }

  @PUT
  @Path("/setStatus/{id}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response setSaleStatus(@PathParam("id") int id, MultivaluedMap<String, String> formParams) throws NamingException {
	  SaleManager sale = new SaleManager();
    int result = sale.setSaleStatus(id, formParams);

    JSONObject jsonObj = new JSONObject();
    if(result > 0) {
      jsonObj = sale.getSaleById(result);
    }

    if(result != 0) {
      return Response.status(200).entity(jsonObj + "").build();
    } else {
      return Response.status(400).build();
    }
  }

  // Get folderId by envelopeId
  @Path("getFolderIdByEnvelopeId/{id}")
  @GET
  @Produces("application/json")
  public Response getFolderIdByEnvelopeId(@PathParam("id") String id) throws JSONException, NamingException {
    SaleManager sale = new SaleManager();
    JSONObject jsonObject = sale.getFolderIdByEnvelopeId(id);
    if(!(jsonObject.has("error"))) {
      String result = jsonObject + "";
      return Response.status(200).entity(result).build();
    } else {
      return Response.status(jsonObject.getInt("error")).build();
    }
  }
}
