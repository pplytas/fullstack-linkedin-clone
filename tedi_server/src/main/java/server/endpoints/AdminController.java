package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auth.SecurityService;
import server.endpoints.inputmodels.IdListInputModel;
import server.endpoints.outputmodels.*;
import server.entities.*;
import server.repositories.AdRepository;
import server.repositories.ConnectionRepository;
import server.repositories.UserRepository;
import server.services.AdService;
import server.services.UserEntityService;
import server.utilities.StorageManager;
import server.utilities.Validator;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StorageManager sm;
	
	@Autowired
	private ConnectionRepository connRepo;

	@Autowired
	private AdRepository adRepo;
	
	@Autowired
	private AdService adService;

	@Autowired
	private UserEntityService userEntityService;

	@Autowired
	private SecurityService secService;

	@Autowired
	private ServletContext servletContext;
	
	//todo add search parameters(?)
	@GetMapping("/userlist")
	public ResponseEntity<Object> userList() {
		
		List<UserEntity> allUsers = userRepo.findByRoleIsNotNull();
		UserListOutputModel output = new UserListOutputModel();
		for (UserEntity u : allUsers) {
			try {
				output.addUser(userEntityService.getUserOutputModelFromUser(u));
			} catch (IOException e) {
				return new ResponseEntity<>("Could not load images", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
	}

	@GetMapping(value = "/userdetails")
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
			List<EducationOutputModel> eduOut = userEntityService.getEducationList(user, true);
			output.setEducation(eduOut.stream().sorted(Comparator.comparing(EducationOutputModel::getStart).reversed()).collect(Collectors.toList()));

			List<ExperienceOutputModel> currentExpOut = userEntityService.getCurrentExperienceList(user, true);
			List<ExperienceOutputModel> expOut = userEntityService.getExperienceList(user, true);
			output.setCurrentExperience(currentExpOut);
			output.setExperiencePublic(user.isExperiencePublic());
			output.setExperience(expOut.stream().sorted(Comparator.comparing(ExperienceOutputModel::getStart).reversed()).collect(Collectors.toList()));

			output.setSkillsPublic(user.isSkillsPublic());
			List<SkillOutputModel> skillOut = userEntityService.getSkillList(user, true);
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
	@GetMapping("/usersearch")
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
				if (user.getRole() != null) {
					output.addUser(userEntityService.getUserOutputModelFromUser(user));
				}
			}
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Could not load profile picture", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	//on ioexception in pictures we can set them null, xml files will not have the pictures in them
	//made it post because 1. it writes data in the server side (creates an xml file)
	//					   2. we need a requestbody
	@PostMapping("/export")
	public ResponseEntity<Resource> exportUsers(@RequestBody IdListInputModel input) {
		
		List<UserDetailedOutputModel> details = new ArrayList<>();
		for (Long id : input.getIds()) {
			
			UserEntity user = userRepo.findById(id).orElse(null);
			
			//ignore bad email input and data set users
			if (user == null || user.getRole() == null)
				continue;
			if (!Validator.validateUserAuth(user)) {
				continue;
			}
			
			UserDetailedOutputModel output = new UserDetailedOutputModel();
			output.setId(user.getId());
			output.setName(user.getName());
			output.setSurname(user.getSurname());
			output.setTelNumber(user.getTelNumber());
			try {
				output.setPicture(sm.getFile(user.getPicture()));
			} catch (IOException e) {
				output.setPicture(null);
			}
			
			List<EducationOutputModel> eduOut = new ArrayList<>();
			List<EducationEntity> eduList = user.getEducation();
			for (EducationEntity e : eduList) {
				EducationOutputModel eOut = new EducationOutputModel();
				eOut.setOrganization(e.getOrganization());
				eOut.setStart(e.getStart());
				eOut.setFinish(e.getFinish());
				eduOut.add(eOut);
			}
			output.setEducation(eduOut);
			
			List<ExperienceOutputModel> expOut = new ArrayList<>();
			List<ExperienceEntity> expList = user.getExperience();
			for (ExperienceEntity e : expList) {
				ExperienceOutputModel xOut = new ExperienceOutputModel();
				xOut.setCompany(e.getCompany());
				xOut.setPosition(e.getPosition());
				xOut.setStart(e.getStart());
				xOut.setFinish(e.getFinish());
				expOut.add(xOut);
			}
			output.setExperience(expOut);
			
			List<SkillOutputModel> skillOut = new ArrayList<>();
			List<UserSkillEntity> skillList = user.getSkills();
			for (UserSkillEntity s : skillList) {
				SkillOutputModel sOut = new SkillOutputModel();
				sOut.setName(s.getName());
				skillOut.add(sOut);
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
				UserOutputModel uOut;
				try {
					uOut = userEntityService.getUserOutputModelFromUser(u);
				} catch (IOException e) {
					uOut = userEntityService.getSafeUserOutputModelFromUser(u);
				}
				connOut.add(uOut);
			}
			output.setConnected(connOut);
			
			List<AdOutputModel> adsOut = new ArrayList<>();
			for (AdEntity ad : user.getAds()) {
				AdOutputModel adOut = adService.adToOutputModel(ad);
				adsOut.add(adOut);
			}
			output.setAds(adsOut);
			
			details.add(output);
		}
		try {
			String fileName = "export.xml";
			ByteArrayResource export = sm.exportUsers(details);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
					.contentType(MediaType.parseMediaType(servletContext.getMimeType(fileName)))
					.contentLength(export.contentLength())
					.body(export);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
