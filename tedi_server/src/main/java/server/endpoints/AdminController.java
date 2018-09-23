package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.endpoints.inputmodels.IdListInputModel;
import server.endpoints.outputmodels.*;
import server.entities.*;
import server.repositories.ConnectionRepository;
import server.repositories.UserRepository;
import server.services.AdService;
import server.services.UserEntityService;
import server.utilities.StorageManager;
import server.utilities.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private AdService adService;

	@Autowired
	private UserEntityService userEntityService;
	
	//todo add search parameters(?)
	@GetMapping("/userlist")
	public ResponseEntity<Object> userList() {
		
		List<UserEntity> allUsers = userRepo.findAll();
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
	
	//on ioexception in pictures we can set them null, xml files will not have the pictures in them
	//made it post because 1. it writes data in the server side (creates an xml file)
	//					   2. we need a requestbody
	@PostMapping("/export")
	public ResponseEntity<Resource> exportUsers(@RequestBody IdListInputModel input) {
		
		List<UserDetailedOutputModel> details = new ArrayList<>();
		for (Long id : input.getIds()) {
			
			UserEntity user = userRepo.findById(id).orElse(null);
			
			//ignore bad email input
			if (user == null)
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
			return new ResponseEntity<>(sm.exportUsers(details), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
