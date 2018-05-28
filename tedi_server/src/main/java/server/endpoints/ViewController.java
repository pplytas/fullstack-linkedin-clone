package server.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.endpoints.outputmodels.ArticleListOutputModel;
import server.endpoints.outputmodels.ArticleOutputModel;
import server.endpoints.outputmodels.ChatMessageOutputModel;
import server.endpoints.outputmodels.ChatOutputModel;
import server.endpoints.outputmodels.CommentOutputModel;
import server.endpoints.outputmodels.EducationOutputModel;
import server.endpoints.outputmodels.ExperienceOutputModel;
import server.endpoints.outputmodels.SkillOutputModel;
import server.endpoints.outputmodels.UpvoteOutputModel;
import server.endpoints.outputmodels.UserDetailedOutputModel;
import server.endpoints.outputmodels.UserListOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.ArticleEntity;
import server.entities.ChatEntity;
import server.entities.CommentEntity;
import server.entities.ConnectionEntity;
import server.entities.EducationEntity;
import server.entities.ExperienceEntity;
import server.entities.RoleEntity;
import server.entities.SkillEntity;
import server.entities.UpvoteEntity;
import server.entities.UserEntity;
import server.repositories.ArticleRepository;
import server.repositories.ChatRepository;
import server.repositories.CommentRepository;
import server.repositories.ConnectionRepository;
import server.repositories.UpvoteRepository;
import server.repositories.UserRepository;
import server.utilities.StorageManager;

@RestController
@RequestMapping("/view")
//contains endpoints to get info for active or other users
public class ViewController {
	
	@Autowired
	private StorageManager sm;
	
	@Autowired 
	private SecurityService secService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ArticleRepository articleRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private UpvoteRepository upvoteRepo;
	
	@Autowired
	private ConnectionRepository connRepo;
	
	@Autowired
	private ChatRepository chatRepo;
	
	//gets info for a given user
	//or for the active user, if no parameter is specified
	@GetMapping(value="/account")
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
	//TODO verify that we try to get non admin details
	@GetMapping(value = "/account/details")
	public ResponseEntity<Object> getAccountDetails(@RequestParam(defaultValue = "") String email) {
		
		try {
			UserEntity user;
			if (email.equals(""))
				user = secService.currentUser();
			else
				user = userRepo.findByEmail(email);
			
			if (user == null)
				return new ResponseEntity<>("No active user found", HttpStatus.NOT_FOUND);
			
			Set<RoleEntity> userRoles = user.getRoles();
			boolean userIsAdmin = false;
			for (RoleEntity r : userRoles) {
				if (r.getName().equals("ADMIN")) {
					userIsAdmin = true;
				}
			}
			if (userIsAdmin) {
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
				List<SkillEntity> skillList = user.getSkills();
				for (SkillEntity s : skillList) {
					SkillOutputModel sOut = new SkillOutputModel();
					sOut.setName(s.getName());
					skillOut.add(sOut);
				}
			}
			output.setSkills(skillOut);
			
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//gets a list of articles for a given user
	//or for the active user, if no parameter is specified
	@GetMapping("/articles")
	public ResponseEntity<Object> getArticles(@RequestParam(defaultValue = "") String email) {
		
		UserEntity refUser;
		if (email.equals("")) {
			refUser = secService.currentUser();
		}
		else {
			refUser = userRepo.findByEmail(email);
		}
		
		if (refUser == null) {
			return new ResponseEntity<>("No user with specified email found", HttpStatus.BAD_REQUEST);
		}
		
		//this instead of refUser.getArticles() because we want to sort
		List<ArticleEntity> articles = articleRepo.findByUserOrderByDateTimeDesc(refUser);
		try {
			ArticleListOutputModel output = new ArticleListOutputModel();
			for (ArticleEntity a : articles) {
				ArticleOutputModel outA = new ArticleOutputModel();
				outA.setId(a.getId());
				outA.setAuthor(refUser.getEmail());
				outA.setTitle(a.getTitle());
				outA.setText(a.getText());
				outA.setFile(sm.getFile(a.getMediafile()));
				outA.setDateTime(a.getDateTime());
				List<CommentEntity> comments = commentRepo.findByArticleOrderByDateTimeDesc(a);
				for (CommentEntity c : comments) {
					CommentOutputModel cOut = new CommentOutputModel();
					cOut.setText(c.getText());
					cOut.setCommentator(c.getUser().getEmail());
					cOut.setDateTime(c.getDateTime());
					outA.addComment(cOut);
				}
				List<UpvoteEntity> upvotes = upvoteRepo.findByArticle(a);
				for (UpvoteEntity u : upvotes) {
					UpvoteOutputModel uOut = new UpvoteOutputModel();
					uOut.setUpvoter(u.getUser().getEmail());
					outA.addUpvote(uOut);
				}
				output.addArticle(outA);
			}
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Could not load media file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	//gets a list of upvoted articles for a given user
	//or for the active user, if no parameter is specified
	@GetMapping("/upvoted")
	public ResponseEntity<Object> upvotes(@RequestParam(defaultValue = "") String email) {
		
		UserEntity refUser;
		if (email.equals("")) {
			refUser = secService.currentUser();
		}
		else {
			refUser = userRepo.findByEmail(email);
		}
		
		List<UpvoteEntity> upvoted = upvoteRepo.findByUser(refUser);
		List<ArticleEntity> articles = new ArrayList<>();
		for (UpvoteEntity u : upvoted) {
			articles.add(u.getArticle());
		}
		
		try {
			ArticleListOutputModel output = new ArticleListOutputModel();
			for (ArticleEntity a : articles) {
				ArticleOutputModel outA = new ArticleOutputModel();
				outA.setId(a.getId());
				outA.setAuthor(refUser.getEmail());
				outA.setTitle(a.getTitle());
				outA.setText(a.getText());
				outA.setFile(sm.getFile(a.getMediafile()));
				outA.setDateTime(a.getDateTime());
				List<CommentEntity> comments = commentRepo.findByArticleOrderByDateTimeDesc(a);
				for (CommentEntity c : comments) {
					CommentOutputModel cOut = new CommentOutputModel();
					cOut.setText(c.getText());
					cOut.setCommentator(c.getUser().getEmail());
					cOut.setDateTime(c.getDateTime());
					outA.addComment(cOut);
				}
				List<UpvoteEntity> upvotes = upvoteRepo.findByArticle(a);
				for (UpvoteEntity u : upvotes) {
					UpvoteOutputModel uOut = new UpvoteOutputModel();
					uOut.setUpvoter(u.getUser().getEmail());
					outA.addUpvote(uOut);
				}
				output.addArticle(outA);
			}
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Could not load media file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	//gets a user list with the connected or the pending to connect users of the active user
	//parameter specified which of the 2
	@GetMapping("/connections")
	public ResponseEntity<Object> getConnection(@RequestParam(defaultValue = "connected") String type) {
		
		UserEntity currUser = secService.currentUser();
		UserListOutputModel output = new UserListOutputModel();
		List<ConnectionEntity> requestedConnections;
		if (type.equals("connected")) {
			requestedConnections = connRepo.findByUserInversibleAndIsPending(currUser, false);
		}
		else if (type.equals("pending")) {
			requestedConnections = connRepo.findByConnectedAndIsPending(currUser, true);
		}
		else if (type.equals("sent")) {
			requestedConnections = connRepo.findByUserAndIsPending(currUser, true);
		}
		else {
			return new ResponseEntity<>("Parameter must be connected, pending or sent", HttpStatus.BAD_REQUEST);
		}
		try {
			for (ConnectionEntity c : requestedConnections) {
				UserEntity u;
				if (c.getUser().equals(currUser)) {
					u = c.getConnected();
				}
				else {
					u = c.getUser();
				}
				output.addUser(new UserOutputModel.UserOutputBuilder(u.getEmail())
													.name(u.getName())
													.surname(u.getSurname())
													.telNumber(u.getTelNumber())
													.picture(sm.getFile(u.getPicture())).build()
						);
			}
		} catch (IOException e) {
			return new ResponseEntity<>("Could not load profile pictures", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
	//gets the messages between current user and user specified by email
	@GetMapping("/messages")
	public ResponseEntity<Object> messages(@RequestParam String email) {
		
		UserEntity currUser = secService.currentUser();
		UserEntity otherUser = userRepo.findByEmail(email);
		if (otherUser == null) {
			return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
		}
		Set<RoleEntity> otherRoles = otherUser.getRoles();
		boolean otherIsAdmin = false;
		for (RoleEntity r : otherRoles) {
			if (r.getName().equals("ADMIN")) {
				otherIsAdmin = true;
			}
		}
		if (otherIsAdmin) {
			return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
		}
		//I dont check for connected or not users, but can be easily implemented if needed
		List<ChatEntity> messages = chatRepo.findBySenderAndReceiverInversibleOrderByIdDesc(currUser, otherUser);
		ChatOutputModel output = new ChatOutputModel();
		for (ChatEntity c : messages) {
			ChatMessageOutputModel mOut = new ChatMessageOutputModel();
			mOut.setMessage(c.getMessage());
			mOut.setSender(c.getSender().getEmail());
			mOut.setReceiver(c.getReceiver().getEmail());
			output.addMessage(mOut);
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
}
