package server.endpoints.inputmodels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExperienceInputModel {
	
	private String company;
	private String position;
	private String start; //supposing format yyyy-MM-dd
	private String finish;//supposing format yyyy-MM-dd
	
	public ExperienceInputModel() {}
	
	public String getCompany() {
		return company;
	}
	
	public String getPosition() {
		return position;
	}

	public String getStart() {
		return start;
	}

	public String getFinish() {
		return finish;
	}
	
	public Date getStartDate() throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(start);
	}

	public Date getFinishDate() throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(finish);
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

}
