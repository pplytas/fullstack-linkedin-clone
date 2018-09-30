package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auth.SecurityService;
import server.auth.UserService;
import server.classification.Categories;
import server.classification.UserClassifier;
import server.endpoints.inputmodels.*;
import server.endpoints.outputmodels.*;
import server.entities.*;
import server.repositories.*;
import server.services.AdService;
import server.services.UserEntityService;
import server.utilities.StorageManager;
import server.utilities.Validator;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
	
	@Autowired
	private UserEntityService userEntityService;
	
	@Autowired
	private AdService adService;
	
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
		
		if (input.getName() != null)
			currUser.setName(input.getName());
		if (input.getSurname() != null)
			currUser.setSurname(input.getSurname());
		if (input.getTelNumber() != null)
			currUser.setTelNumber(input.getTelNumber());
		if (input.getPicture() != null) {
			try {
				String storedpath = sm.storeFile(input.getPicture());
				currUser.setPicture(storedpath);
			} catch (IOException e) {
				//keep the old picture since the exception is thrown at storeFile line
			}
		}
		userRepo.save(currUser);
		
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@PostMapping("/education")
	public ResponseEntity<Object> setEducation(@RequestBody EducationWrappedInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		currUser.setEducationPublic(input.isPublic());
		try {
			List<EducationEntity> newEducations = new ArrayList<>();
			if (input.getEducations() != null) {
				for (EducationInputModel e : input.getEducations()) {
					if (e.getOrganization() == null || e.getOrganization().equals("")
						|| e.getStartDate() == null) {
						return new ResponseEntity<>("One or more necessary fields were null", HttpStatus.BAD_REQUEST);
					}
					EducationEntity entity = new EducationEntity();
					entity.setOrganization(e.getOrganization());
					entity.setStart(e.getStartDate());
					entity.setFinish(e.getFinishDate());
					entity.setUser(currUser);
					newEducations.add(entity);
				}
			}
			List<EducationEntity> old = eduRepo.findByUser(currUser);
			eduRepo.deleteAll(old);
			eduRepo.saveAll(newEducations);
		} catch (ParseException e) {
			return new ResponseEntity<>("Could not parse education data", HttpStatus.BAD_REQUEST);
		}
		userRepo.save(currUser);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	@PostMapping("/experience")
	public ResponseEntity<Object> setExperience(@RequestBody ExperienceWrappedInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		currUser.setExperiencePublic(input.isPublic());
		try {
			List<ExperienceEntity> newExperiences = new ArrayList<>();
			if (input.getExperiences() != null) {
				for (ExperienceInputModel e : input.getExperiences()) {
					if (e.getCompany() == null || e.getCompany().equals("")
						|| e.getPosition() == null || e.getPosition().equals("")
						|| e.getStartDate() == null) {
						return new ResponseEntity<>("One or more necessary fields were null", HttpStatus.BAD_REQUEST);
					}
					ExperienceEntity entity = new ExperienceEntity();
					entity.setCompany(e.getCompany());
					entity.setPosition(e.getPosition());
					entity.setStart(e.getStartDate());
					entity.setFinish(e.getFinishDate());
					entity.setUser(currUser);
					newExperiences.add(entity);
				}
			}
			List<ExperienceEntity> old = expRepo.findByUser(currUser);
			expRepo.deleteAll(old);
			expRepo.saveAll(newExperiences);
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
		if (input.getSkills() != null) {
			for (SkillInputModel s : input.getSkills()) {
				if (s.getName() == null || s.getName().equals("")) {
					continue;
				}
				UserSkillEntity entity = new UserSkillEntity();
				entity.setName(s.getName());
				entity.setUser(currUser);
				skillRepo.save(entity);
			}
		}
		//avoid using admin users or self in classification
		Categories newCategory = userClass.classify(currUser, userRepo.findByEmailNotAndRoleNotAdminOrIsNull(currUser.getEmail()));
		currUser.setCategories(newCategory);
		userRepo.save(currUser);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	//gets info for a given user
	//or for the active user, if no parameter is specified
	@GetMapping(value="/simple")
	public ResponseEntity<Object> getAccount(@RequestParam(required = false) Long id) {
		
		try {
			UserEntity user;
			
			if (id == null)
				user = secService.currentUser();
			else
				user = userRepo.findById(id).orElse(null);
			
			if (user == null)
				return new ResponseEntity<>("Not existing user with such id", HttpStatus.NOT_FOUND);

			return new ResponseEntity<>(userEntityService.getUserOutputModelFromUser(user), HttpStatus.OK);
		}
		catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//gets detailed info for a given user
	//or for the active user, if no parameter is specified
	@GetMapping(value = "/details")
	public ResponseEntity<Object> getAccountDetails(@RequestParam(required = false) Long id) {
		
		try {
			UserEntity user;
			if (id == null)
				user = secService.currentUser();
			else
				user = userRepo.findById(id).orElse(null);
			
			if (user == null)
				return new ResponseEntity<>("Not existing user with such id", HttpStatus.NOT_FOUND);
			
			if (!Validator.validateUserAuth(user)) {
				return new ResponseEntity<>("Not existing user with such id", HttpStatus.NOT_FOUND);
			}
			
			UserDetailedOutputModel output = new UserDetailedOutputModel();
			output.setId(user.getId());
			output.setName(user.getName());
			output.setSurname(user.getSurname());
			output.setTelNumber(user.getTelNumber());
			output.setPicture(sm.getFile(user.getPicture()));

			output.setEducationPublic(user.isEducationPublic());
			List<EducationOutputModel> eduOut = userEntityService.getEducationList(user, false);
			output.setEducation(eduOut.stream().sorted(Comparator.comparing(EducationOutputModel::getStart).reversed()).collect(Collectors.toList()));
			
			List<ExperienceOutputModel> currentExpOut = userEntityService.getCurrentExperienceList(user, false);
			List<ExperienceOutputModel> expOut = userEntityService.getExperienceList(user, false);
			output.setCurrentExperience(currentExpOut);
			output.setExperiencePublic(user.isExperiencePublic());
			output.setExperience(expOut.stream().sorted(Comparator.comparing(ExperienceOutputModel::getStart).reversed()).collect(Collectors.toList()));

			output.setSkillsPublic(user.isSkillsPublic());
			List<SkillOutputModel> skillOut = userEntityService.getSkillList(user, false);
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
				UserOutputModel uOut = userEntityService.getUserOutputModelFromUser(u);
				connOut.add(uOut);
			}
			output.setConnected(connOut);
			
			List<AdOutputModel> adsOut = new ArrayList<>();
			for (AdEntity ad : adRepo.findByPublisher(user)) {
				AdOutputModel adOut = adService.adToOutputModel(ad);
				adsOut.add(adOut);
			}
			output.setAds(adsOut);
			
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//searches and returns a non detailed user list
	@GetMapping("/search/simple")
	public ResponseEntity<Object> searchAccounts(@RequestParam String query) {
		
		//first, we split the string to allow for name + surname query
		String[] split = query.split("\\s+");
		List<UserEntity> results;
		if (split.length == 1) {
			results = userRepo.findByNameContainingOrSurnameContaining(split[0], split[0]);
		}
		else if (split.length == 2) {
			results = userRepo.findByNameContainingAndSurnameContaining(split[0], split[1]);
		}
		else {
			results = userRepo.findByNameContainingOrSurnameContaining(query, query);
		}
		
		UserListOutputModel output = new UserListOutputModel();
		try {
			for (UserEntity user : results) {
				if (Validator.validateUserAuth(user)) {
					output.addUser(userEntityService.getUserOutputModelFromUser(user));
				}
			}
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Could not load profile picture", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
}
