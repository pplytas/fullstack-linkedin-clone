package server.endpoints.inputmodels;

import java.util.ArrayList;
import java.util.List;

public class EmailListInputModel {
	
	List<String> emails = new ArrayList<>();
	
	public EmailListInputModel() {}

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}

}
