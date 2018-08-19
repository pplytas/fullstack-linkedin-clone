package server.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.endpoints.outputmodels.AdOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.AdEntity;
import server.entities.AdSkillEntity;
import server.utilities.StorageManager;

@Service
public class AdService {
	
	@Autowired
	private StorageManager sm;
	
	public AdOutputModel adToOutputModel(AdEntity ad) {
		AdOutputModel adOut = new AdOutputModel();
		adOut.setId(ad.getId());
		adOut.setTitle(ad.getTitle());
		adOut.setDescription(ad.getDescription());
		try {
			adOut.setPublisher(new UserOutputModel.UserOutputBuilder(ad.getPublisher().getEmail())
													.name(ad.getPublisher().getName())
													.surname(ad.getPublisher().getSurname())
													.telNumber(ad.getPublisher().getTelNumber())
													.picture(sm.getFile(ad.getPublisher().getPicture())).build());
		} catch (IOException e) {
			adOut.setPublisher(new UserOutputModel.UserOutputBuilder(ad.getPublisher().getEmail())
					.name(ad.getPublisher().getName())
					.surname(ad.getPublisher().getSurname())
					.telNumber(ad.getPublisher().getTelNumber()).build());
		}
		adOut.setPublishDate(ad.getPublishDate());
		for (AdSkillEntity adskill : ad.getSkills()) {
			SkillOutputModel sOut = new SkillOutputModel();
			sOut.setName(adskill.getName());
			adOut.addSkill(sOut);
		}
		return adOut;
	}

}
