package server.endpoints;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.classification.AdClassifier;
import server.classification.Categories;
import server.endpoints.inputmodels.AdInputModel;
import server.endpoints.inputmodels.SkillInputModel;
import server.endpoints.outputmodels.AdListOutputModel;
import server.endpoints.outputmodels.AdOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.AdEntity;
import server.entities.AdSkillEntity;
import server.entities.UserEntity;
import server.repositories.AdRepository;
import server.repositories.UserRepository;
import server.utilities.StorageManager;
import server.utilities.Validator;

@RestController
@RequestMapping("/ads")
public class AdController {
	
	@Autowired
	private SecurityService secService;
	
	@Autowired
	private StorageManager sm;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AdRepository adRepo;
	
	@Autowired
	private AdClassifier adClass;
	
	@PostMapping("/add")
	public ResponseEntity<Object> publishAd(@RequestBody AdInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		AdEntity newAd = new AdEntity();
		newAd.setTitle(input.getTitle());
		newAd.setDescription(input.getDescription());
		for (SkillInputModel adskill : input.getSkills()) {
			AdSkillEntity entity = new AdSkillEntity();
			entity.setName(adskill.getName());
			entity.setAd(newAd);
			newAd.addSkill(entity);
		}
		newAd.setPublisher(currUser);
		newAd.setPublishDate();
		Categories category = adClass.classify(newAd, adRepo.findAll());
		newAd.setCategories(category);
		adRepo.save(newAd);
		return new ResponseEntity<>("Ad published successfully", HttpStatus.OK);
		
	}
	
	//gets a user's ads
	//or my ads if no parameter specified
	@GetMapping("/ofuser")
	public ResponseEntity<Object> getUserAds(@RequestParam(defaultValue = "") String email) {
		
		UserEntity user;
		if (email.equals("")) {
			user = secService.currentUser();
		}
		else {
			user = userRepo.findByEmail(email);
			if (user == null || !Validator.validateUserAuth(user)) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}
		
		AdListOutputModel output = new AdListOutputModel();
		try {
			for (AdEntity ad : user.getAds()) {
				AdOutputModel adOut = new AdOutputModel();
				adOut.setId(ad.getId());
				adOut.setTitle(ad.getTitle());
				adOut.setDescription(ad.getDescription());
				adOut.setPublisher(new UserOutputModel.UserOutputBuilder(user.getEmail())
															.name(user.getName())
															.surname(user.getSurname())
															.telNumber(user.getTelNumber())
															.picture(sm.getFile(user.getPicture())).build());
				for (AdSkillEntity adskill : ad.getSkills()) {
					SkillOutputModel sOut = new SkillOutputModel();
					sOut.setName(adskill.getName());
					adOut.addSkill(sOut);
				}
				output.addAd(adOut);
			}
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//get suggested ads based on the classification category of the user and the ads
	@GetMapping("/suggested")
	public ResponseEntity<Object> getSuggested() {
		
		UserEntity currUser = secService.currentUser();
		List<AdEntity> suggestedAds = adRepo.findByCategoryAndPublisherIsNotNull(currUser.getCategory());
		AdListOutputModel output = new AdListOutputModel();
		try {
			for (AdEntity ad : suggestedAds) {
				AdOutputModel adOut = new AdOutputModel();
				adOut.setId(ad.getId());
				adOut.setTitle(ad.getTitle());
				adOut.setDescription(ad.getDescription());
				adOut.setPublisher(new UserOutputModel.UserOutputBuilder(adOut.getPublisher().getEmail())
															.name(adOut.getPublisher().getName())
															.surname(adOut.getPublisher().getSurname())
															.telNumber(adOut.getPublisher().getTelNumber())
															.picture(sm.getFile(adOut.getPublisher().getPicture())).build());
				for (AdSkillEntity adskill : ad.getSkills()) {
					SkillOutputModel sOut = new SkillOutputModel();
					sOut.setName(adskill.getName());
					adOut.addSkill(sOut);
				}
				output.addAd(adOut);
			}
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

}
