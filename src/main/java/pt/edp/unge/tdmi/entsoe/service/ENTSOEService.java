package pt.edp.unge.tdmi.entsoe.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.edp.unge.tdmi.entsoe.dto.ENTSOEFile;
import pt.edp.unge.tdmi.entsoe.rest.client.ENTSOERestClient;
import pt.edp.unge.tdmi.entsoe.xml.UnavailabilityMarketDocument;

@ApplicationScoped
public class ENTSOEService {
	
    @Inject
    @RestClient
    ENTSOERestClient restClient;
    
    public static final Logger LOGGER = LoggerFactory.getLogger(ENTSOEService.class.getName());
    
    public List<UnavailabilityMarketDocument> downloadFile(String securityToken, String documentType, String periodStart, 
    		String periodEnd, String inDomain, String outDomain) throws Exception {
				List<ENTSOEFile> xmlList = new ArrayList<>();
				List<UnavailabilityMarketDocument> documents = new ArrayList<>();
				InputStream file = restClient.downloadFile(securityToken, documentType, periodStart, periodEnd, inDomain, outDomain);
				try (ZipInputStream zipInputStream = new ZipInputStream(file)) {
		            ZipEntry zipEntry;

		            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
		            	ENTSOEFile xml = new ENTSOEFile();
		            	try  {
		            		
		            		File fileTemp = new File("C:\\Temp", zipEntry.getName());
		            		FileOutputStream fileOutputStream = new FileOutputStream(fileTemp);
		            		int length;
		            		byte[] buffer = new byte[1024];
		            		while ((length = zipInputStream.read(buffer)) > 0) {
		            			fileOutputStream.write(buffer, 0, length);
		            		}
		            		fileOutputStream.close();		            	
		            		xml.setFileName(zipEntry.getName());
		            		FileInputStream fis = FileUtils.openInputStream(fileTemp);
							String zipContent = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
		            		xml.setZipContent(zipContent);
		            		fis.close();
		            		LOGGER.info(zipEntry.getName());
		            		xmlList.add(xml);
		            	} catch (Exception e) {
		            		LOGGER.error(e.getMessage());
						}
		            }
				}
				xmlList.forEach((xml) -> {
					JAXBContext jaxbContext;
					try {
						jaxbContext = JAXBContext.newInstance(UnavailabilityMarketDocument.class);
						Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
						UnavailabilityMarketDocument document = (UnavailabilityMarketDocument) unmarshaller
								.unmarshal(new StringReader(xml.getZipContent()));
						LOGGER.info(document.getMRID());
						documents.add(document);
					} catch (JAXBException e) {
						LOGGER.error(e.getMessage());
					}
				});
				return documents;
    }

}
