package server.endpoints.inputmodels;

import java.util.List;

public class ExperienceWrappedInputModel {

	private List<ExperienceInputModel> experiences;
	private boolean isPublic;
	
	public ExperienceWrappedInputModel() {}

	public List<ExperienceInputModel> getExperiences() {
		return experiences;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setExperiences(List<ExperienceInputModel> experiences) {
		this.experiences = experiences;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
}
