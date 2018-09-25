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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import server.endpoints.outputmodels.AdOutputModel;
import server.endpoints.outputmodels.EducationOutputModel;
import server.endpoints.outputmodels.ExperienceOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.endpoints.outputmodels.UserDetailedOutputModel;
import server.endpoints.outputmodels.UserOutputModel;

@Component
public class StorageManager {

	private final Logger logger = Logger.getLogger(StorageManager.class.getName());

	@Value("${uploads_path}")	
	private String filePath;
	
	@Value("${export_path}")
	private String exportPath;
	
	public ByteArrayResource exportUsers(List<UserDetailedOutputModel> users) 
			throws IOException, ParserConfigurationException, TransformerException {
		
		//create document
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = docBuilder.newDocument();
				
		//root element
		Element root = doc.createElement("Users");
		doc.appendChild(root);
		
		//add each user's data
		for (UserDetailedOutputModel u : users) {
			
			Element user = doc.createElement("user");
			root.appendChild(user);
			user.setAttribute("id", u.getId().toString());
			
			Element name = doc.createElement("name");
			if (u.getName()!=null)
				name.appendChild(doc.createTextNode(u.getName()));
			user.appendChild(name);
			
			Element surname = doc.createElement("surname");
			if (u.getSurname()!=null)
				surname.appendChild(doc.createTextNode(u.getSurname()));
			user.appendChild(surname);
			
			Element telNumber = doc.createElement("telNumber");
			if (u.getTelNumber()!=null)
				telNumber.appendChild(doc.createTextNode(u.getTelNumber()));
			user.appendChild(telNumber);
			
			Element educations = doc.createElement("educations");
			for (EducationOutputModel ed : u.getEducation()) {
				
				Element education = doc.createElement("education");
				
				Element edOrg = doc.createElement("organization");
				edOrg.appendChild(doc.createTextNode(ed.getOrganization()));
				education.appendChild(edOrg);
				
				Element edStart = doc.createElement("startDate");
				edStart.appendChild(doc.createTextNode(ed.getStart()));
				education.appendChild(edStart);
				Element edFinish = doc.createElement("finishDate");
				edFinish.appendChild(doc.createTextNode(ed.getFinish()));
				education.appendChild(edFinish);
				
				educations.appendChild(education);
				
			}
			user.appendChild(educations);
			
			Element experiences = doc.createElement("experiences");
			for (ExperienceOutputModel exp : u.getExperience()) {
				
				Element experience = doc.createElement("experience");
				
				Element company = doc.createElement("company");
				company.appendChild(doc.createTextNode(exp.getCompany()));
				experience.appendChild(company);
				
				Element position = doc.createElement("position");
				position.appendChild(doc.createTextNode(exp.getPosition()));
				experience.appendChild(position);
				
				Element expStart = doc.createElement("startDate");
				expStart.appendChild(doc.createTextNode(exp.getStart()));
				experience.appendChild(expStart);
				Element expFinish = doc.createElement("finishDate");
				expFinish.appendChild(doc.createTextNode(exp.getFinish()));
				experience.appendChild(expFinish);
				
				experiences.appendChild(experience);
				
			}
			user.appendChild(experiences);
			
			Element skills = doc.createElement("skills");
			for (SkillOutputModel s : u.getSkills()) {
				
				Element skill = doc.createElement("skill");
				
				Element skillName = doc.createElement("name");
				skillName.appendChild(doc.createTextNode(s.getName()));
				skill.appendChild(skillName);
				
				skills.appendChild(skill);
				
			}
			user.appendChild(skills);
			
			Element connected = doc.createElement("connected");
			for (UserOutputModel c : u.getConnected()) {
				
				Element connectedEmail = doc.createElement("id");
				connectedEmail.appendChild(doc.createTextNode(c.getId().toString()));
				connected.appendChild(connectedEmail);
				
			}
			user.appendChild(connected);
			
			Element ads = doc.createElement("ads");
			for (AdOutputModel a : u.getAds()) {
				
				Element ad = doc.createElement("ad");
				
				Element title = doc.createElement("title");
				if (a.getTitle()!=null)
					title.appendChild(doc.createTextNode(a.getTitle()));
				ad.appendChild(title);
				
				Element desc = doc.createElement("description");
				if (a.getDescription()!=null)
					desc.appendChild(doc.createTextNode(a.getDescription()));
				ad.appendChild(desc);
				
				Element adSkills = doc.createElement("skills");
				for (SkillOutputModel s : a.getSkills()) {
					
					Element adSkill = doc.createElement("skill");
					adSkills.appendChild(adSkill);
					
					Element adSkillName = doc.createElement("name");
					adSkillName.appendChild(doc.createTextNode(s.getName()));
					adSkill.appendChild(adSkillName);
					
				}
				ad.appendChild(adSkills);
				
				ads.appendChild(ad);
				
			}
			user.appendChild(ads);
			
		}
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(doc);
		File toWrite = new File(exportPath + "/export.xml");
		toWrite.getParentFile().mkdirs();
		toWrite.createNewFile();
		StreamResult result = new StreamResult(toWrite.toURI().getPath());
		transformer.transform(source, result);
		
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
