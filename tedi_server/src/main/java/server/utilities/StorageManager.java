package server.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class StorageManager {

	private final Logger logger = Logger.getLogger(StorageManager.class.getName());

	@Value("${uploads_path}")	
	private String filePath;
	
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
