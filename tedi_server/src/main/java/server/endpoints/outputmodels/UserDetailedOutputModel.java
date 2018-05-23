package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class UserDetailedOutputModel {
	
	private String email;
	private String name;
	private String surname;
	private String telNumber;
	private String picture;
	private List<EducationOutputModel> education = new ArrayList<>();
	private List<ExperienceOutputModel> experience = new ArrayList<>();
	private List<SkillOutputModel> skills = new ArrayList<>();
	
	public UserDetailedOutputModel() {}

	public String getEmail() {
		return email;
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

	public List<EducationOutputModel> getEducation() {
		return education;
	}

	public List<ExperienceOutputModel> getExperience() {
		return experience;
	}

	public List<SkillOutputModel> getSkills() {
		return skills;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public void setEducation(List<EducationOutputModel> education) {
		this.education = education;
	}

	public void setExperience(List<ExperienceOutputModel> experience) {
		this.experience = experience;
	}

	public void setSkills(List<SkillOutputModel> skills) {
		this.skills = skills;
	}

}
