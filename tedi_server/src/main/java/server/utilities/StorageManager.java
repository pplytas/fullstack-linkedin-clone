package server.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import server.endpoints.outputmodels.UserDetailedOutputModel;

@Component
public class StorageManager {

	private final Logger logger = Logger.getLogger(StorageManager.class.getName());

	@Value("${uploads_path}")	
	private String filePath;
	
	@Value("${export_path}")
	private String exportPath;
	
	public ByteArrayResource exportUsers(List<UserDetailedOutputModel> users) throws IOException, ParserConfigurationException {
		
		//create document
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.newDocument();
				
		//root element
		Element root = doc.createElement("users");
		doc.appendChild(root);
		
		//add each user's data
		for (UserDetailedOutputModel u : users) {
			
			//TODO
			
		}
		
		Path path = Paths.get(exportPath + "/export.xml");
		return new ByteArrayResource(Files.readAllBytes(path));
		
	}
	
	public String storeFile(String filestring) throws IOException {
		
		if (filestring == null || filestring.equals(""))
			return null;
		
		File directory = new File(filePath);
		if (! directory.exists()) {
			directory.mkdir();
		}
		
		byte[] filebytes = Base64.getDecoder().decode(filestring);
		String newFileName = generateRandomChars(16);
		String finalFileName = filePath + newFileName;
		File f = new File(finalFileName);
		FileOutputStream stream = new FileOutputStream(f);
		
		try {
			stream.write(filebytes);
		}
		finally {
			stream.close();
		}
		
		return newFileName;
		
	}
	
	public String getFile(String filename) throws IOException {

		if (filename == null || filename.equals(""))
			return null;
			
		File imgFile = new File(filePath + filename);
		InputStream targetStream = new FileInputStream(imgFile);
		byte[] bytes = StreamUtils.copyToByteArray(targetStream);
		String encoded = Base64.getEncoder().encodeToString(bytes);
		return encoded;
	}
	
	private String generateRandomChars(int length) {
		String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        sb.append(candidateChars.charAt(random.nextInt(candidateChars
	                .length())));
	    }

	    return sb.toString();
	}
	
}
