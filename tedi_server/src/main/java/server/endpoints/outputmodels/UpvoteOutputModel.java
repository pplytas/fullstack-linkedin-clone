package server.endpoints.outputmodels;

public class UpvoteOutputModel {
	
	private String upvoterName;
	private String upvoterSurname;
	private String upvoterEmail; //currently email, may change to name later on
	
	public UpvoteOutputModel() {}

	public String getUpvoterName() {
		return upvoterName;
	}

	public String getUpvoterSurname() {
		return upvoterSurname;
	}

	public String getUpvoterEmail() {
		return upvoterEmail;
	}

	public void setUpvoterName(String upvoterName) {
		this.upvoterName = upvoterName;
	}

	public void setUpvoterSurname(String upvotedSurname) {
		this.upvoterSurname = upvotedSurname;
	}

	public void setUpvoterEmail(String upvoterEmail) {
		this.upvoterEmail = upvoterEmail;
	}

}
