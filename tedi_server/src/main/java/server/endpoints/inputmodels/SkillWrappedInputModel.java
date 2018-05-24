package server.endpoints.inputmodels;

import java.util.List;

public class SkillWrappedInputModel {
	
	private List<SkillInputModel> skills;
	private boolean isPublic;
	
	public SkillWrappedInputModel() {}

	public List<SkillInputModel> getSkills() {
		return skills;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setSkills(List<SkillInputModel> skills) {
		this.skills = skills;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

}
