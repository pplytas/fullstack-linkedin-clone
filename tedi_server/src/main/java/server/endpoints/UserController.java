package server.endpoints;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.auth.UserService;
import server.classification.Categories;
import server.classification.UserClassifier;
import server.endpoints.inputmodels.EducationInputModel;
import server.endpoints.inputmodels.EducationWrappedInputModel;
import server.endpoints.inputmodels.ExperienceInputModel;
import server.endpoints.inputmodels.ExperienceWrappedInputModel;
import server.endpoints.inputmodels.RegisterInputModel;
import server.endpoints.inputmodels.SkillInputModel;
import server.endpoints.inputmodels.SkillWrappedInputModel;
import server.endpoints.outputmodels.AdOutputModel;
import server.endpoints.outputmodels.EducationOutputModel;
import server.endpoints.outputmodels.ExperienceOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.endpoints.outputmodels.UserDetailedOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.AdEntity;
import server.entities.AdSkillEntity;
import server.entities.ConnectionEntity;
import server.entities.EducationEntity;
import server.entities.ExperienceEntity;
import server.entities.UserSkillEntity;
import server.entities.UserEntity;
import server.repositories.AdRepository;
import server.repositories.ConnectionRepository;
import server.repositories.EducationRepository;
import server.repositories.ExperienceRepository;
import server.repositories.UserSkillRepository;
import server.repositories.UserRepository;
import server.utilities.StorageManager;
import server.utilities.Validator;

@RestController
@RequestMapping("/user")
//includes endpoints to edit/insert new data for active user
public class UserController {
	
	@Autowired
	private StorageManager sm;
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private SecurityService secService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private EducationRepository eduRepo;
	
	@Autowired
	private ExperienceRepository expRepo;
	
	@Autowired
	private UserSkillRepository skillRepo;
	
	@Autowired
	private ConnectionRepository connRepo;
	
	@Autowired
	private AdRepository adRepo;
	
	@Autowired
	private UserClassifier userClass;
	
	//update current user credentials (only for users, not admins)
	@PutMapping("/update")
	public ResponseEntity<Object> updateUser(@RequestBody RegisterInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		if (input.getEmail() != null) {
			if (userRepo.findByEmail(input.getEmail()) != null) {
				String msg = "A user with this email is already registered";
				return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
			}
			if (!Validator.validateEmail(input.getEmail())) {
				String msg = "Invalid email format";
				return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
			}
		}
		if (input.getEmail() == null) {
			input.setEmail("");
		}
		if (input.getPassword() == null) {
			input.setPassword("");
		}
		//function below ignores empty email or password
		userService.updateCredentials(input.getEmail(), input.getPassword());
		
		//for the rest of the data we expect user to add correct info
		currUser.setName(input.getName());
		currUser.setSurname(input.getSurname());
		currUser.setTelNumber(input.getTelNumber());
		try {
			String storedpath = sm.storeFile(input.getPicture());
			currUser.setPicture(storedpath);
		} catch (IOException e) {
			//keep the old picture since the exception is thrown at storeFile line
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@PostMapping("/education")
	public ResponseEntity<Object> setEducation(@RequestBody EducationWrappedInputModel input) {
		
		System.out.println(input.isPublic());
		UserEntity currUser = secService.currentUser();
		currUser.setEducationPublic(input.isPublic());
		try {
			List<EducationEntity> old = eduRepo.findByUser(currUser);
			eduRepo.deleteAll(old);
			for (EducationInputModel e : input.getEducations()) {
				EducationEntity entity = new EducationEntity();
				entity.setOrganization(e.getOrganization());
				entity.setStart(e.getStartDate());
				entity.setFinish(e.getFinishDate());
				entity.setUser(currUser);
				eduRepo.save(entity);
			}
		} catch (ParseException e) {
			return new ResponseEntity<>("Could not parse education data", HttpStatus.BAD_REQUEST);
		}
		System.out.println("TEST " + currUser.isEducationPublic());
		userRepo.save(currUser);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@PostMapping("/experience")
	public ResponseEntity<Object> setExperience(@RequestBody ExperienceWrappedInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		currUser.setExperiencePublic(input.isPublic());
		try {
			List<ExperienceEntity> old = expRepo.findByUser(currUser);
			expRepo.deleteAll(old);
			for (ExperienceInputModel e : input.getExperiences()) {
				ExperienceEntity entity = new ExperienceEntity();
				entity.setCompany(e.getCompany());
				entity.setPosition(e.getPosition());
				entity.setStart(e.getStartDate());
				entity.setFinish(e.getFinishDate());
				entity.setUser(currUser);
				expRepo.save(entity);
			}
		} catch (ParseException e) {
			return new ResponseEntity<>("Could not parse education data", HttpStatus.BAD_REQUEST);
		}
		
		userRepo.save(currUser);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@PostMapping("/skills")
	public ResponseEntity<Object> setSkills(@RequestBody SkillWrappedInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		currUser.setSkillsPublic(input.isPublic());
		List<UserSkillEntity> old = skillRepo.findByUser(currUser);
		skillRepo.deleteAll(old);
		for (SkillInputModel s : input.getSkills()) {
			UserSkillEntity entity = new UserSkillEntity();
			entity.setName(s.getName());
			entity.setUser(currUser);
			skillRepo.save(entity);
		}
		//avoid using admin users or self in classification
		Categories newCategory = userClass.classify(currUser, userRepo.findByEmailNotAndRoleNotAdminOrIsNull(currUser.getEmail()));
		currUser.setCategories(newCategory);
		//System.out.println(newCategory.getCategory());
		userRepo.save(currUser);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	//gets info for a given user
	//or for the active user, if no parameter is specified
	@GetMapping(value="/simple")
	public ResponseEntity<Object> getAccount(@RequestParam(defaultValue = "") String email) {
		
		try {
			UserEntity user;
			
			if (email.equals(""))
				user = secService.currentUser();
			else
				user = userRepo.findByEmail(email);
			
			if (user == null)
				return new ResponseEntity<>("No active user found", HttpStatus.NOT_FOUND);

			return new ResponseEntity<>(
					new UserOutputModel.UserOutputBuilder(user.getEmail())
														.name(user.getName())
														.surname(user.getSurname())
														.telNumber(user.getTelNumber())
														.picture(sm.getFile(user.getPicture())).build()
					, HttpStatus.OK);
		}
		catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//gets detailed info for a given user
	//or for the active user, if no parameter is specified
	@GetMapping(value = "/details")
	public ResponseEntity<Object> getAccountDetails(@RequestParam(defaultValue = "") String email) {
		
		try {
			UserEntity user;
			if (email.equals(""))
				user = secService.currentUser();
			else
				user = userRepo.findByEmail(email);
			
			if (user == null)
				return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
			
			if (!Validator.validateUserAuth(user)) {
				return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
			}
			
			UserDetailedOutputModel output = new UserDetailedOutputModel();
			output.setEmail(user.getEmail());
			output.setName(user.getName());
			output.setSurname(user.getSurname());
			output.setTelNumber(user.getTelNumber());
			output.setPicture(sm.getFile(user.getPicture()));
			
			//check if the user we are getting details for is connected to us
			boolean viewPrivate = false;
			if (user.equals(secService.currentUser()) ||
				connRepo.findByUserAndConnectedAndIsPending(secService.currentUser(), user, false) != null ||
				connRepo.findByUserAndConnectedAndIsPending(user, secService.currentUser(), false) != null) {
				viewPrivate = true;
			}
			
			List<EducationOutputModel> eduOut = new ArrayList<>();
			if (user.isEducationPublic() || viewPrivate) {
				List<EducationEntity> eduList = user.getEducation();
				for (EducationEntity e : eduList) {
					EducationOutputModel eOut = new EducationOutputModel();
					eOut.setOrganization(e.getOrganization());
					eOut.setStart(e.getStart());
					eOut.setFinish(e.getFinish());
					eduOut.add(eOut);
				}
			}
			output.setEducation(eduOut);
			
			List<ExperienceOutputModel> expOut = new ArrayList<>();
			if (user.isExperiencePublic() || viewPrivate) {
				List<ExperienceEntity> expList = user.getExperience();
				for (ExperienceEntity e : expList) {
					ExperienceOutputModel xOut = new ExperienceOutputModel();
					xOut.setCompany(e.getCompany());
					xOut.setPosition(e.getPosition());
					xOut.setStart(e.getStart());
					xOut.setFinish(e.getFinish());
					expOut.add(xOut);
				}
			}
			output.setExperience(expOut);
			
			List<SkillOutputModel> skillOut = new ArrayList<>();
			if (user.isSkillsPublic() || viewPrivate) {
				List<UserSkillEntity> skillList = user.getSkills();
				for (UserSkillEntity s : skillList) {
					SkillOutputModel sOut = new SkillOutputModel();
					sOut.setName(s.getName());
					skillOut.add(sOut);
				}
			}
			output.setSkills(skillOut);
			
			List<UserOutputModel> connOut = new ArrayList<>();
			for (ConnectionEntity c : connRepo.findByUserInversibleAndIsPending(user, false)) {
				UserEntity u;
				if (!c.getUser().equals(user)) {
					u = c.getUser();
				}
				else {
					u = c.getConnected();
				}
				UserOutputModel uOut = new UserOutputModel.UserOutputBuilder(u.getEmail())
						.name(u.getName())
						.surname(u.getSurname())
						.telNumber(u.getTelNumber())
						.picture(sm.getFile(u.getPicture())).build();
				connOut.add(uOut);
			}
			output.setConnected(connOut);
			
			List<AdOutputModel> adsOut = new ArrayList<>();
			for (AdEntity ad : adRepo.findByPublisher(user)) {
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
				adsOut.add(adOut);
			}
			output.setAds(adsOut);
			
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
