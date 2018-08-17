package server.endpoints.outputmodels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExperienceOutputModel {
	
	private String company;
	private String position;
	private String start; //supposing format yyyy-MM-dd
	private String finish;//supposing format yyyy-MM-dd
	
	public ExperienceOutputModel() {}
	
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
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public void setPosition(String position) {
		this.position = position;
	}

	public void setStart(Date start) {
		if (start != null) {
			this.start = new SimpleDateFormat("yyyy-MM-dd").format(start);
		} else {
			this.start = "";
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
