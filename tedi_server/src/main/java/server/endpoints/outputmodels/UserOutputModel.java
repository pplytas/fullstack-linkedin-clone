package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class UserOutputModel {
	
	private Long id;
	private String name;
	private String surname;
	private String telNumber;
	private String picture;
	private List<ExperienceOutputModel> currentExperience = new ArrayList<>();
	
	public UserOutputModel() {}
	
	public UserOutputModel(UserOutputBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.surname = builder.surname;
		this.telNumber = builder.telNumber;
		this.picture = builder.picture;
		this.currentExperience = builder.currentExperience;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public String getPicture() {
		return picture;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public List<ExperienceOutputModel> getCurrentExperience() {
		return currentExperience;
	}

	public void setCurrentExperience(List<ExperienceOutputModel> currentExperience) {
		this.currentExperience = currentExperience;
	}

	public static class UserOutputBuilder {
		private final Long id;
		private String name;
		private String surname;
		private String telNumber;
		private String picture;
		private List<ExperienceOutputModel> currentExperience;
		
		public UserOutputBuilder(Long id) {
			this.id = id;
		}
		
		public UserOutputBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public UserOutputBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		public UserOutputBuilder telNumber(String telNumber) {
			this.telNumber = telNumber;
			return this;
		}
		
		public UserOutputBuilder picture(String picture) {
			this.picture = picture;
			return this;
		}

		public UserOutputBuilder currentExperience(List<ExperienceOutputModel> currentExperience) {
			this.currentExperience = currentExperience;
			return this;
		}

		public UserOutputModel build() {
			return new UserOutputModel(this);
		}
		
	}
	
}
