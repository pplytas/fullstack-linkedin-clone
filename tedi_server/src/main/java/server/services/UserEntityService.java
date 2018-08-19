package server.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.auth.SecurityService;
import server.endpoints.outputmodels.ExperienceOutputModel;
import server.entities.ExperienceEntity;
import server.entities.UserEntity;
import server.repositories.ConnectionRepository;
import server.utilities.DateUtils;

@Service
public class UserEntityService {
	
	@Autowired
	private SecurityService secService;
	
	@Autowired
	private ConnectionRepository connRepo;
	
	public boolean getPublicVisibilityStatus(UserEntity user) {
		boolean viewPrivate = false;
		if (user.equals(secService.currentUser()) ||
			connRepo.findByUserAndConnectedAndIsPending(secService.currentUser(), user, false) != null ||
			connRepo.findByUserAndConnectedAndIsPending(user, secService.currentUser(), false) != null) {
			viewPrivate = true;
		}
		return viewPrivate;
	}
	
	public List<ExperienceOutputModel> getCurrentExperienceList(UserEntity user) {
		List<ExperienceOutputModel> currentExpOut = new ArrayList<>();
		boolean viewPrivate = getPublicVisibilityStatus(user);
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
	
	public List<ExperienceOutputModel> getExperienceList(UserEntity user) {
		List<ExperienceOutputModel> expOut = new ArrayList<>();
		boolean viewPrivate = getPublicVisibilityStatus(user);
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

}
