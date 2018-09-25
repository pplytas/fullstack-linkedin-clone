package server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.auth.SecurityService;
import server.endpoints.outputmodels.EducationOutputModel;
import server.endpoints.outputmodels.ExperienceOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.EducationEntity;
import server.entities.ExperienceEntity;
import server.entities.UserEntity;
import server.entities.UserSkillEntity;
import server.repositories.ConnectionRepository;
import server.utilities.DateUtils;
import server.utilities.StorageManager;

@Service
public class UserEntityService {

	@Autowired
	private StorageManager sm;

	@Autowired
	private SecurityService secService;
	
	@Autowired
	private ConnectionRepository connRepo;

	public UserOutputModel getUserOutputModelFromUser(UserEntity user) throws IOException {
		return new UserOutputModel.UserOutputBuilder(user.getId())
				.name(user.getName())
				.surname(user.getSurname())
				.telNumber(user.getTelNumber())
				.picture(sm.getFile(user.getPicture()))
				.currentExperience(getCurrentExperienceList(user, false))
				.build();
	}

	public UserOutputModel getSafeUserOutputModelFromUser(UserEntity user) {
		return new UserOutputModel.UserOutputBuilder(user.getId())
				.name(user.getName())
				.surname(user.getSurname())
				.telNumber(user.getTelNumber())
				.currentExperience(getCurrentExperienceList(user, false))
				.build();
	}

	public boolean getPublicVisibilityStatus(UserEntity user) {
		boolean viewPrivate = false;
		if (user.equals(secService.currentUser()) ||
			connRepo.findByUserAndConnectedAndIsPending(secService.currentUser(), user, false) != null ||
			connRepo.findByUserAndConnectedAndIsPending(user, secService.currentUser(), false) != null) {
			viewPrivate = true;
		}
		return viewPrivate;
	}
	
	public List<ExperienceOutputModel> getCurrentExperienceList(UserEntity user, boolean adminRequest) {
		List<ExperienceOutputModel> currentExpOut = new ArrayList<>();
		boolean viewPrivate = adminRequest || getPublicVisibilityStatus(user);
		if (user.isExperiencePublic() || viewPrivate) {
			List<ExperienceEntity> expList = user.getExperience();
			for (ExperienceEntity e : expList) {
				ExperienceOutputModel xOut = new ExperienceOutputModel();
				xOut.setCompany(e.getCompany());
				xOut.setPosition(e.getPosition());
				xOut.setStart(e.getStart());
				if (DateUtils.lessEqualThanCurrent(e.getStart()) && e.getFinish() == null) {
					xOut.setFinish("Present");
				}
				else if (DateUtils.greaterEqualThanCurrent(e.getStart()) && e.getFinish() == null) {
					xOut.setFinish("Unknown");
				}
				else {
					xOut.setFinish(e.getFinish());
				}
				if (DateUtils.lessEqualThanCurrent(e.getStart()) && DateUtils.greaterEqualThanCurrent(e.getFinish())) {
					currentExpOut.add(xOut);
				}
			}
		}
		return currentExpOut;
	}
	
	public List<ExperienceOutputModel> getExperienceList(UserEntity user, boolean adminRequest) {
		List<ExperienceOutputModel> expOut = new ArrayList<>();
		boolean viewPrivate = adminRequest || getPublicVisibilityStatus(user);
		if (user.isExperiencePublic() || viewPrivate) {
			List<ExperienceEntity> expList = user.getExperience();
			for (ExperienceEntity e : expList) {
				ExperienceOutputModel xOut = new ExperienceOutputModel();
				xOut.setCompany(e.getCompany());
				xOut.setPosition(e.getPosition());
				xOut.setStart(e.getStart());
				if (DateUtils.lessEqualThanCurrent(e.getStart()) && e.getFinish() == null) {
					xOut.setFinish("Present");
				}
				else if (DateUtils.greaterEqualThanCurrent(e.getStart()) && e.getFinish() == null) {
					xOut.setFinish("Unknown");
				}
				else {
					xOut.setFinish(e.getFinish());
				}
				expOut.add(xOut);
			}
		}
		return expOut;
	}

	public List<EducationOutputModel> getEducationList(UserEntity user, boolean adminRequest) {
		List<EducationOutputModel> eduOut = new ArrayList<>();
		boolean viewPrivate = adminRequest || getPublicVisibilityStatus(user);
		if (user.isEducationPublic() || viewPrivate) {
			List<EducationEntity> eduList = user.getEducation();
			for (EducationEntity e : eduList) {
				EducationOutputModel eOut = new EducationOutputModel();
				eOut.setOrganization(e.getOrganization());
				eOut.setStart(e.getStart());
				if (DateUtils.lessEqualThanCurrent(e.getStart()) && e.getFinish() == null) {
					eOut.setFinish("Present");
				}
				else if (DateUtils.greaterEqualThanCurrent(e.getStart()) && e.getFinish() == null) {
					eOut.setFinish("Unknown");
				}
				else {
					eOut.setFinish(e.getFinish());
				}
				eduOut.add(eOut);
			}
		}
		return eduOut;
	}

	public List<SkillOutputModel> getSkillList(UserEntity user, boolean adminRequest) {
		List<SkillOutputModel> skillOut = new ArrayList<>();
		boolean viewPrivate = adminRequest || getPublicVisibilityStatus(user);
		if (user.isSkillsPublic() || viewPrivate) {
			List<UserSkillEntity> skillList = user.getSkills();
			for (UserSkillEntity s : skillList) {
				SkillOutputModel sOut = new SkillOutputModel();
				sOut.setName(s.getName());
				skillOut.add(sOut);
			}
		}
		return skillOut;
	}

}
