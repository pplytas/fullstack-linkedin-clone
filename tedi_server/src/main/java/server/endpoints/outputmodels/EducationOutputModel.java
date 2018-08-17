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
		if (start != null) {
			this.start = new SimpleDateFormat("yyyy-MM-dd").format(start);
		} else {
			this.start = "Unknown";
		}
	}
	
	public void setFinish(String finish) {
		this.finish = finish;
	}

	public void setFinish(Date finish) {
		if (finish != null) {
			this.finish = new SimpleDateFormat("yyyy-MM-dd").format(finish);
		} else {
			this.finish = "";
		}
	}
	
}
