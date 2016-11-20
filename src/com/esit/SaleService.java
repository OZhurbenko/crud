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
//import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import org.json.JSONException;
//import org.json.JSONObject;
import org.json.JSONObject;

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

      @POST
      @Path("/createNewSale")
      @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
      public Response create(
              @FormParam("fname") String fname,
              @FormParam("lname") String lname,
              @FormParam("address") String address,
              @FormParam("unitNum") String unitNum,
              @FormParam("city") String city,
              @FormParam("province") String province,
              @FormParam("postalCode") String postalCode,
              @FormParam("enbridge") String enbridge,
              @FormParam("email") String email,
              @FormParam("homePhone") String homePhone,
              @FormParam("cellPhone") String cellPhone,
              @FormParam("programType") String programType,
              @FormParam("installationDate") String installationDate,
              @FormParam("installationTime") String installationTime,
              @FormParam("notes") String notes,
              @FormParam("dateSigned") String dateSigned,
              @FormParam("saleNumber") String saleNumber,
              @FormParam("salesRepId") String salesRepId,
              @FormParam("applicationNumber") String applicationNumber) {

          repository = new Repository();
          int result = repository.createNewSale(fname, lname, address, unitNum, city, province,
                  postalCode, enbridge, email, homePhone, cellPhone, programType, installationDate, installationTime, notes,
                  dateSigned, saleNumber, salesRepId, applicationNumber);

          JSONObject jsonObj = new JSONObject();
          jsonObj.put("fname", fname);
          jsonObj.put("lname", lname);
          jsonObj.put("address", address);
          jsonObj.put("unitNum", unitNum);
          jsonObj.put("city", city);
          jsonObj.put("province", province);
          jsonObj.put("postalCode", postalCode);
          jsonObj.put("enbridge", enbridge);
          jsonObj.put("email", email);
          jsonObj.put("homePhone", homePhone);
          jsonObj.put("cellPhone", cellPhone);
          jsonObj.put("programType", programType);
          jsonObj.put("installationDate", installationDate);
          jsonObj.put("installationTime", installationTime);
          jsonObj.put("notes", notes);
          jsonObj.put("dateSigned", dateSigned);
          jsonObj.put("saleNumber", saleNumber);
          jsonObj.put("salesRepId", salesRepId);
          jsonObj.put("applicationNumber", applicationNumber);
          jsonObj.put("result", result);

          if(result != 0) {
              return Response.status(200).entity(jsonObj + "").build();
          } else {
              return Response.status(400).build();
          }
      }
}
