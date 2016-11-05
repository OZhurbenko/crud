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

@Path("/InstallationService")
public class InstallationService {

	Repository repository;

	// Get all installations
    @Path("getAllInstallations")
    @GET
    @Produces("application/json")
    public Response getAllInstallations() throws JSONException, NamingException {
      repository = new Repository();
      String result = repository.getAllInstallations() + "";
      return Response.status(200).entity(result).build();
    }

    // Get installation by Id
    @Path("getInstallationById/{id}")
    @GET
    @Produces("application/json")
    public Response getInstallationById(@PathParam("id") int id) throws JSONException, NamingException {
      repository = new Repository();
      String result = repository.getInstallationById(id) + "";
      return Response.status(200).entity(result).build();
    }
}
