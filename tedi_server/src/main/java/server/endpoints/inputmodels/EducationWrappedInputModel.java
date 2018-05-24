package server.endpoints.inputmodels;

import java.util.List;

public class EducationWrappedInputModel {
	
	private List<EducationInputModel> educations;
	private boolean isPublic;
	
	public EducationWrappedInputModel() {}

	public List<EducationInputModel> getEducations() {
		return educations;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setEducations(List<EducationInputModel> educations) {
		this.educations = educations;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
	
}
