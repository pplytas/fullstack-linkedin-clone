package server.endpoints.inputmodels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EducationInputModel {

	private String organization;
	private String start; //supposing format yyyy-MM-dd
	private String finish;//supposing format yyyy-MM-dd
	
	public EducationInputModel() {}

	public String getOrganization() {
		return organization;
	}

	public String getStart() {
		return start;
	}

	public String getFinish() {
		return finish;
	}
	
	public Date getStartDate() throws ParseException {
		if (start != null && !start.equals("")) {
			return new SimpleDateFormat("yyyy-MM-dd").parse(start);
		} else {
			return null;
		}
	}

	public Date getFinishDate() throws ParseException {
		if (finish != null && !finish.equals("")) {
			return new SimpleDateFormat("yyyy-MM-dd").parse(finish);
		} else {
			return null;
		}
	}
	
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}
	
}
