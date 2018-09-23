package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auth.SecurityService;
import server.classification.AdClassifier;
import server.classification.Categories;
import server.endpoints.inputmodels.AdInputModel;
import server.endpoints.inputmodels.SkillInputModel;
import server.endpoints.outputmodels.AdApplicationOutputModel;
import server.endpoints.outputmodels.AdListOutputModel;
import server.endpoints.outputmodels.AdOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.entities.AdApplicationEntity;
import server.entities.AdEntity;
import server.entities.AdSkillEntity;
import server.entities.UserEntity;
import server.repositories.AdApplicationRepository;
import server.repositories.AdRepository;
import server.repositories.ConnectionRepository;
import server.repositories.UserRepository;
import server.services.AdService;
import server.services.UserEntityService;
import server.utilities.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdController {
	
	@Autowired
	private SecurityService secService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AdRepository adRepo;
	
	@Autowired
	private ConnectionRepository connRepo;
	
	@Autowired
	private AdApplicationRepository adAppRepo;
	
	@Autowired
	private AdClassifier adClass;
	
	@Autowired
	private AdService adService;

	@Autowired
	private UserEntityService userEntityService;
	
	@PostMapping("/add")
	public ResponseEntity<Object> publishAd(@RequestBody AdInputModel input) {
		
		if (input.getTitle() == null || input.getTitle().equals("")
			|| input.getDescription() == null || input.getDescription().equals("")) {
			return new ResponseEntity<>("Title and description can't be null or empty", HttpStatus.BAD_REQUEST);
		}
		
		UserEntity currUser = secService.currentUser();
		AdEntity newAd = new AdEntity();
		newAd.setTitle(input.getTitle());
		newAd.setDescription(input.getDescription());
		if (input.getSkills() != null) {
			for (SkillInputModel adskill : input.getSkills()) {
				if (adskill.getName() == null) {
					continue;
				}
				AdSkillEntity entity = new AdSkillEntity();
				entity.setName(adskill.getName());
				entity.setAd(newAd);
				newAd.addSkill(entity);
			}
		}
		newAd.setPublisher(currUser);
		newAd.setPublishDate();
		Categories category = adClass.classify(newAd, adRepo.findAll());
		newAd.setCategories(category);
		adRepo.save(newAd);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	//gets a user's ads
	//or my ads if no parameter specified
	@GetMapping("/ofuser")
	public ResponseEntity<Object> getUserAds(@RequestParam(required = false) Long id) {
		
		UserEntity user;
		if (id == null) {
			user = secService.currentUser();
		}
		else {
			user = userRepo.findById(id).orElse(null);
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
				adOut.setPublisher(userEntityService.getUserOutputModelFromUser(user));
				adOut.setPublishDate(ad.getPublishDate());
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
		//this is so that the final list has the ads sorted from newest to oldest
		List<AdEntity> suggestedAds = adRepo.findByCategoryAndPublisherIsNotNullOrderByPublishDateDesc(currUser.getCategory());
		AdListOutputModel output = new AdListOutputModel();
		for (AdEntity ad : suggestedAds) {
			//ignore ads by same user
			if (ad.getPublisher() == currUser) continue;
			AdOutputModel adOut = adService.adToOutputModel(ad);
			if (connRepo.findByUserAndConnectedInversibleAndIsPending(currUser, ad.getPublisher(), false) != null) {
				output.addAdByConn(adOut);
			}
			else {
				output.addAd(adOut);
			}
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
	@PostMapping("/apply")
	public ResponseEntity<Object> applyForAd(@RequestParam Long id) {
		
		UserEntity currUser = secService.currentUser();
		AdEntity ad = adRepo.findById(id).orElse(null);
		if (ad == null) {
			return new ResponseEntity<>("No ad with id " + id, HttpStatus.NOT_FOUND);
		}
		if (adAppRepo.findByAdAndUser(ad, currUser) != null) {
			return new ResponseEntity<>("Already applied for this ad", HttpStatus.CONFLICT);
		}
		AdApplicationEntity adApplication = new AdApplicationEntity();
		adApplication.setAd(ad);
		adApplication.setUser(currUser);
		adApplication.setStatus(0);
		adAppRepo.save(adApplication);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@GetMapping("/applications")
	public ResponseEntity<Object> getAdApplications(@RequestParam Long id) {
		
		AdEntity refAd = adRepo.findById(id).orElse(null);
		//if the ad is not of the current user, quietly reject
		if (refAd == null || !refAd.getPublisher().getEmail().equals(secService.currentUser().getEmail())) {
			return new ResponseEntity<>("No ad with id " + id, HttpStatus.NOT_FOUND);
		}
		List<AdApplicationEntity> adApplications = adAppRepo.findByAd(refAd);
		List<AdApplicationOutputModel> output = new ArrayList<>();
		for (AdApplicationEntity adApp : adApplications) {
			AdApplicationOutputModel adAppOut = new AdApplicationOutputModel();
			adAppOut.setId(adApp.getId());
			AdOutputModel adOut = adService.adToOutputModel(adApp.getAd());
			adAppOut.setAd(adOut);
			try {
				adAppOut.setUser(userEntityService.getUserOutputModelFromUser(adApp.getUser()));
			} catch (IOException e) {
				adAppOut.setUser(userEntityService.getSafeUserOutputModelFromUser(adApp.getUser()));
			}
			adAppOut.setStatus(adApp.getStatus());
			output.add(adAppOut);
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
	@PutMapping("/application/accept")
	public ResponseEntity<Object> acceptApplication(@RequestParam Long applicationId) {
		
		AdApplicationEntity refAdApp = adAppRepo.findById(applicationId).orElse(null);
		//if the application is not for an ad of the current user, quietly reject
		if (refAdApp == null || !refAdApp.getAd().getPublisher().getEmail().equals(secService.currentUser().getEmail())) {
			return new ResponseEntity<>("No application with id " + applicationId, HttpStatus.NOT_FOUND);
		}
		if (refAdApp.getStatus() != 0) {
			return new ResponseEntity<>("Only pending applications can be accepted", HttpStatus.BAD_REQUEST);
		}
		refAdApp.setStatus(1);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/application/reject")
	public ResponseEntity<Object> tejectApplication(@RequestParam Long applicationId) {
		
		AdApplicationEntity refAdApp = adAppRepo.findById(applicationId).orElse(null);
		//if the application is not for an ad of the current user, quietly reject
		if (refAdApp == null || !refAdApp.getAd().getPublisher().getEmail().equals(secService.currentUser().getEmail())) {
			return new ResponseEntity<>("No application with id " + applicationId, HttpStatus.NOT_FOUND);
		}
		if (refAdApp.getStatus() != 0) {
			return new ResponseEntity<>("Only pending applications can be rejected", HttpStatus.BAD_REQUEST);
		}
		refAdApp.setStatus(-1);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
