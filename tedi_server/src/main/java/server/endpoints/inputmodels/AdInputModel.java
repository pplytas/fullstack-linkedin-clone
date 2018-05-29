package server.endpoints.inputmodels;

import java.util.ArrayList;
import java.util.List;

public class AdInputModel {

	private String title;
	private String description;
	private List<SkillInputModel> skills = new ArrayList<>();
	
	public AdInputModel() {}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public List<SkillInputModel> getSkills() {
		return skills;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSkills(List<SkillInputModel> skills) {
		this.skills = skills;
	}
	
}
