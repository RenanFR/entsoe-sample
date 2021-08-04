package pt.edp.unge.tdmi.entsoe.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pt.edp.unge.tdmi.entsoe.service.ENTSOEService;
import pt.edp.unge.tdmi.entsoe.xml.UnavailabilityMarketDocument;

@Path("/entsoe")
public class ENTSOEResource {
	
    @Inject
    ENTSOEService service;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public List<UnavailabilityMarketDocument> startDataIngestion() throws Exception {
    	List<UnavailabilityMarketDocument> files = service.downloadFile("897ba873-cdb2-4b33-9acb-936cbfd935fc", "A78", "201811070000", 
        		"201811080000", "10YPT-REN------W", "10YES-REE------0");
        return files;
    }
}