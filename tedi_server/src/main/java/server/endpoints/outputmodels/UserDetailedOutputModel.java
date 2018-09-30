package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class UserDetailedOutputModel {
	
	private Long id;
	private String name;
	private String surname;
	private String telNumber;
	private String picture;
	private List<ExperienceOutputModel> currentExperience = new ArrayList<>();
	private Boolean educationPublic;
	private List<EducationOutputModel> education = new ArrayList<>();
	private Boolean experiencePublic;
	private List<ExperienceOutputModel> experience = new ArrayList<>();
	private Boolean skillsPublic;
	private List<SkillOutputModel> skills = new ArrayList<>();
	private List<UserOutputModel> connected = new ArrayList<>();
	private List<AdOutputModel> ads = new ArrayList<>();
	
	public UserDetailedOutputModel() {}

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

	public List<ExperienceOutputModel> getCurrentExperience() {
		return currentExperience;
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
	
	public List<UserOutputModel> getConnected() {
		return connected;
	}
	
	public List<AdOutputModel> getAds() {
		return ads;
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

	public void setCurrentExperience(List<ExperienceOutputModel> currentExperience) {
		this.currentExperience = currentExperience;
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
	
	public void setConnected(List<UserOutputModel> connected) {
		this.connected = connected;
	}
	
	public void setAds(List<AdOutputModel> ads) {
		this.ads = ads;
	}

	public Boolean getEducationPublic() {
		return educationPublic;
	}

	public void setEducationPublic(Boolean educationPublic) {
		this.educationPublic = educationPublic;
	}

	public Boolean getExperiencePublic() {
		return experiencePublic;
	}

	public void setExperiencePublic(Boolean experiencePublic) {
		this.experiencePublic = experiencePublic;
	}

	public Boolean getSkillsPublic() {
		return skillsPublic;
	}

	public void setSkillsPublic(Boolean skillsPublic) {
		this.skillsPublic = skillsPublic;
	}
}
