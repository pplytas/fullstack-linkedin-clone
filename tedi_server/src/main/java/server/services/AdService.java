package server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import server.endpoints.outputmodels.AdApplicationOutputModel;
import server.endpoints.outputmodels.AdOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.AdApplicationEntity;
import server.entities.AdEntity;
import server.entities.AdSkillEntity;
import server.utilities.StorageManager;

@Service
public class AdService {
	
	@Autowired
	private StorageManager sm;

	@Autowired
	private UserEntityService userEntityService;

	public AdOutputModel adToOutputModel(AdEntity ad) {
		AdOutputModel adOut = new AdOutputModel();
		adOut.setId(ad.getId());
		adOut.setTitle(ad.getTitle());
		adOut.setDescription(ad.getDescription());
		try {
			adOut.setPublisher(userEntityService.getUserOutputModelFromUser(ad.getPublisher()));
		} catch (IOException e) {
			adOut.setPublisher(userEntityService.getSafeUserOutputModelFromUser(ad.getPublisher()));
		}
		adOut.setPublishDate(ad.getPublishDate());
		for (AdSkillEntity adskill : ad.getSkills()) {
			SkillOutputModel sOut = new SkillOutputModel();
			sOut.setName(adskill.getName());
			adOut.addSkill(sOut);
		}
		return adOut;
	}

	public List<AdApplicationOutputModel> adAppToOutputModel(List<AdApplicationEntity> adApplications) {
		List<AdApplicationOutputModel> output = new ArrayList<>();
		for (AdApplicationEntity adApp : adApplications) {
			AdApplicationOutputModel adAppOut = new AdApplicationOutputModel();
			adAppOut.setId(adApp.getId());
			AdOutputModel adOut = adToOutputModel(adApp.getAd());
			adAppOut.setAd(adOut);
			try {
				adAppOut.setUser(userEntityService.getUserOutputModelFromUser(adApp.getUser()));
			} catch (IOException e) {
				adAppOut.setUser(userEntityService.getSafeUserOutputModelFromUser(adApp.getUser()));
			}
			adAppOut.setStatus(adApp.getStatus());
			output.add(adAppOut);
		}
		return output;
	}

}
