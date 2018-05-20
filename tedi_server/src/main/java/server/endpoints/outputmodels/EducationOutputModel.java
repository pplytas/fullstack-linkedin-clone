package server.endpoints.outputmodels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EducationOutputModel {

	private String organization;
	private String start; //returned as yyyy-MM-dd
	private String finish; //returned as yyyy-MM-dd
	
	public EducationOutputModel() {}

	public String getOrganization() {
		return organization;
	}

	public String getStart() {
		return start;
	}

	public String getFinish() {
		return finish;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setStart(Date start) {
		this.start = new SimpleDateFormat("yyyy-MM-dd").format(start);
	}

	public void setFinish(Date finish) {
		this.finish = new SimpleDateFormat("yyyy-MM-dd").format(finish);
	}
	
}
