package server.endpoints;

import java.io.IOException;
import java.util.ArrayList;
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
import server.endpoints.outputmodels.AdApplicationOutputModel;
import server.endpoints.outputmodels.AdListOutputModel;
import server.endpoints.outputmodels.AdOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.AdApplicationEntity;
import server.entities.AdEntity;
import server.entities.AdSkillEntity;
import server.entities.UserEntity;
import server.repositories.AdApplicationRepository;
import server.repositories.AdRepository;
import server.repositories.ConnectionRepository;
import server.repositories.UserRepository;
import server.services.AdService;
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
	private ConnectionRepository connRepo;
	
	@Autowired
	private AdApplicationRepository adAppRepo;
	
	@Autowired
	private AdClassifier adClass;
	
	@Autowired
	private AdService adService;
	
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
		AdApplicationEntity adApplication = new AdApplicationEntity();
		adApplication.setAd(ad);
		adApplication.setUser(currUser);
		adAppRepo.save(adApplication);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@GetMapping("/applications")
	public ResponseEntity<Object> getAdApplications(@RequestParam Long id) {
		
		AdEntity refAd = adRepo.findById(id).orElse(null);
		if (refAd == null ) {
			return new ResponseEntity<>("No ad with id " + id, HttpStatus.NOT_FOUND);
		}
		List<AdApplicationEntity> adApplications = adAppRepo.getByAd(refAd);
		List<AdApplicationOutputModel> output = new ArrayList<>();
		for (AdApplicationEntity adApp : adApplications) {
			AdApplicationOutputModel adAppOut = new AdApplicationOutputModel();
			AdOutputModel adOut = adService.adToOutputModel(adApp.getAd());
			adAppOut.setAd(adOut);
			try {
				adAppOut.setUser(new UserOutputModel.UserOutputBuilder(adApp.getUser().getEmail())
															.name(adApp.getUser().getName())
															.surname(adApp.getUser().getSurname())
															.telNumber(adApp.getUser().getTelNumber())
															.picture(sm.getFile(adApp.getUser().getPicture())).build());
			} catch (IOException e) {
				adAppOut.setUser(new UserOutputModel.UserOutputBuilder(adApp.getUser().getEmail())
				.name(adApp.getUser().getName())
				.surname(adApp.getUser().getSurname())
				.telNumber(adApp.getUser().getTelNumber()).build());
			}
			output.add(adAppOut);
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}

}
