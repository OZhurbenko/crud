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

@Path("/InstallationService")
public class InstallationService {
	// Get all installations
  @Path("getAllInstallations/{id}")
  @GET
  @Produces("application/json")
  public Response getAllInstallations(@PathParam("id") int id) throws JSONException, NamingException {
    InstallationManager installation = new InstallationManager();
    JSONObject allInstallations = installation.getAllInstallations(id);

    if(!(allInstallations.has("error"))) {
      String result = allInstallations + "";
      return Response.status(200).entity(result).build();
    } else {
      return Response.status(allInstallations.getInt("error")).build();
    }
  }

  // Get all installations
  @Path("getScheduledInstallations/{id}")
  @GET
  @Produces("application/json")
  public Response getScheduledInstallations(@PathParam("id") int id) throws JSONException, NamingException {
    InstallationManager installation = new InstallationManager();
    JSONObject scheduledInstallations = installation.getAllScheduled(id);

    if(!(scheduledInstallations.has("error"))) {
      String result = scheduledInstallations + "";
      return Response.status(200).entity(result).build();
    } else {
      return Response.status(scheduledInstallations.getInt("error")).build();
    }
  }

  // Get installation by Id
  @Path("getInstallationById/{id}")
  @GET
  @Produces("application/json")
  public Response getInstallationById(@PathParam("id") int id) throws JSONException, NamingException {
    InstallationManager installation = new InstallationManager();
    JSONObject installationJson = installation.getInstallationById(id);

    if(!(installationJson.has("error"))) {
      String result = installationJson + "";
      return Response.status(200).entity(result).build();
    } else {
      return Response.status(installationJson.getInt("error")).build();
    }
  }

  // Create new installation
  @POST
  @Path("/createNewInstallation")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response create(MultivaluedMap<String, String> formParams) {
    InstallationManager installation = new InstallationManager(formParams);
    JSONObject result = installation.create();

    if(!(result.has("error"))) {
      return Response.status(201).build();
    } else {
      return Response.status(result.getInt("error")).build();
    }
  }

  // Update Installation object
  @PUT
  @Path("/updateInstallation/{id}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updateInstallation(@PathParam("id") int id, MultivaluedMap<String, String> formParams) throws NamingException {
    InstallationManager installation = new InstallationManager();
    JSONObject result = installation.update(id, formParams);

    if(!(result.has("error"))) {
      return Response.status(200).build();
    } else {
      return Response.status(result.getInt("error")).build();
    }
  }

  // Set envelopeId for installation
  @PUT
  @Path("/setEnvelopeId/{id}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response setEnvelopeId(@PathParam("id") int id, MultivaluedMap<String, String> formParams) throws NamingException {
    InstallationManager installation = new InstallationManager();
    JSONObject installationIdJson = installation.setEnvelopeId(id, formParams);

    if(!(installationIdJson.has("error"))) {
      JSONObject jsonObj = new JSONObject();
      jsonObj.put("installationId", installationIdJson.getInt("installationId"));
      return Response.status(200).entity(jsonObj + "").build();
    } else {
      return Response.status(installationIdJson.getInt("error")).build();
    }
  }

  @PUT
  @Path("/setStatus/{id}")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response setInstallationStatus(@PathParam("id") int id, MultivaluedMap<String, String> formParams) throws NamingException {
    InstallationManager installation = new InstallationManager();
    JSONObject installationIdJson = installation.setInstallationStatus(id, formParams);

    if(!(installationIdJson.has("error"))) {
      JSONObject jsonObj = new JSONObject();
      jsonObj = installation.getInstallationById(installationIdJson.getInt("installationId"));
      return Response.status(200).entity(jsonObj + "").build();
    } else {
      return Response.status(installationIdJson.getInt("error")).build();
    }
  }
}
