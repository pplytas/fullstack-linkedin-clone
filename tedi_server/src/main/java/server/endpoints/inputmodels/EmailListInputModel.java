package server.endpoints.inputmodels;

import java.util.ArrayList;
import java.util.List;

public class EmailListInputModel {
	
	List<EmailInputModel> emails = new ArrayList<>();
	
	public EmailListInputModel() {}

	public List<String> getEmails() {
		List<String> emailStr = new ArrayList<>();
		for (EmailInputModel e : emails) {
			emailStr.add(e.getEmail());
		}
		return emailStr;
	}

	public void setEmails(List<EmailInputModel> emails) {
		this.emails = emails;
	}

}
