package com.esit;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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

@Path("/InstallationService")
public class InstallationService {
	// Get all installations
    @Path("getAllInstallations")
    @GET
    @Produces("application/json")
    public Response getAllInstallations() throws JSONException, NamingException {
      InstallationManager installation = new InstallationManager();
      String result = installation.getAllInstallations() + "";
      return Response.status(200).entity(result).build();
    }

    // Get all installations
    @Path("getScheduledInstallations")
    @GET
    @Produces("application/json")
    public Response getScheduledInstallations() throws JSONException, NamingException {
      InstallationManager installation = new InstallationManager();
      String result = installation.getAllScheduled() + "";
      return Response.status(200).entity(result).build();
    }

    // Get installation by Id
    @Path("getInstallationById/{id}")
    @GET
    @Produces("application/json")
    public Response getInstallationById(@PathParam("id") int id) throws JSONException, NamingException {
      InstallationManager installation = new InstallationManager();
      String result = installation.getInstallationById(id) + "";
      return Response.status(200).entity(result).build();
    }

    // Create new installation
    @POST
    @Path("/createNewInstallation")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(MultivaluedMap<String, String> formParams) {
        InstallationManager installation = new InstallationManager(formParams);
        int result = installation.create();

        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("fname", sale.getFname());
//        jsonObj.put("lname", sale.getLname());
//        jsonObj.put("address", sale.getAddress());
//        jsonObj.put("unitNum", sale.getUnitNum());
//        jsonObj.put("city", sale.getCity());
//        jsonObj.put("province", sale.getProvince());
//        jsonObj.put("postalCode", sale.getPostalCode());
//        jsonObj.put("enbridge", sale.getEnbridge());
//        jsonObj.put("email", sale.getEmail());
//        jsonObj.put("homePhone", sale.getHomePhone());
//        jsonObj.put("cellPhone", sale.getCellPhone());
//        jsonObj.put("programType", sale.getProgramType());
//        jsonObj.put("installationDate", sale.getInstallationDate());
//        jsonObj.put("installationTime", sale.getInstallationTime());
//        jsonObj.put("notes", sale.getNotes());
//        jsonObj.put("dateSigned", sale.getDateSigned());
//        jsonObj.put("salesRepId", sale.getSalesRepI());
//        jsonObj.put("result", result);

        if(result != 0) {
            return Response.status(200).entity(jsonObj + "").build();
        } else {
            return Response.status(400).build();
        }
    }
    
    // Set envelopeId for installation
    @PUT
    @Path("/setEnvelopeId/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response setEnvelopeId(@PathParam("id") int id, MultivaluedMap<String, String> formParams) throws NamingException {
        InstallationManager installation = new InstallationManager();
        int result = installation.setEnvelopeId(id, formParams);

        JSONObject jsonObj = new JSONObject();
        if(result > 0) {
            jsonObj.put("installationId", result);
        }

        if(result != 0) {
            return Response.status(200).entity(jsonObj + "").build();
        } else {
            return Response.status(400).build();
        }
    }
}
