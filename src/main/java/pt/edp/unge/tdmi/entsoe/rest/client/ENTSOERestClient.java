package pt.edp.unge.tdmi.entsoe.rest.client;

import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/api")
@RegisterRestClient
public interface ENTSOERestClient {
	
	@GET
	InputStream downloadFile(
			@QueryParam("securityToken") String securityToken,
			@QueryParam("documentType") String documentType,
			@QueryParam("periodStart") String periodStart,
			@QueryParam("periodEnd") String periodEnd,
			@QueryParam("In_Domain") String inDomain,
			@QueryParam("Out_Domain") String outDomain);

}	