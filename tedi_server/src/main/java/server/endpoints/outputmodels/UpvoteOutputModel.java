package server.endpoints.outputmodels;

public class UpvoteOutputModel {
	
	private String upvoter; //currently email, may change to name later on
	
	public UpvoteOutputModel() {}

	public String getUpvoter() {
		return upvoter;
	}

	public void setUpvoter(String upvoter) {
		this.upvoter = upvoter;
	}

}
