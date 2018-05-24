package server.endpoints;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.auth.UserService;
import server.endpoints.inputmodels.ArticleInputModel;
import server.endpoints.inputmodels.CommentInputModel;
import server.endpoints.inputmodels.EducationInputModel;
import server.endpoints.inputmodels.EducationWrappedInputModel;
import server.endpoints.inputmodels.ExperienceInputModel;
import server.endpoints.inputmodels.ExperienceWrappedInputModel;
import server.endpoints.inputmodels.SkillInputModel;
import server.endpoints.inputmodels.SkillWrappedInputModel;
import server.entities.ArticleEntity;
import server.entities.CommentEntity;
import server.entities.ConnectionEntity;
import server.entities.EducationEntity;
import server.entities.ExperienceEntity;
import server.entities.SkillEntity;
import server.entities.UpvoteEntity;
import server.entities.UserEntity;
import server.repositories.ArticleRepository;
import server.repositories.CommentRepository;
import server.repositories.ConnectionRepository;
import server.repositories.EducationRepository;
import server.repositories.ExperienceRepository;
import server.repositories.SkillRepository;
import server.repositories.UpvoteRepository;
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
	private SkillRepository skillRepo;
	
	@Autowired
	private ArticleRepository articleRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private UpvoteRepository upvoteRepo;
	
	@Autowired
	private ConnectionRepository connRepo;
	
	//update current user credentials (only for users, not admins)
	@PutMapping("/update")
	public ResponseEntity<Object> updateUser(@RequestParam(defaultValue = "") String email,
											 @RequestParam(defaultValue = "") String password) {
		
		if (!email.equals("")) {
			if (userRepo.findByEmail(email) != null) {
				String msg = "A user with this email is already registered";
				return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
			}
			if (!Validator.validateEmail(email)) {
				String msg = "Invalid email format";
				return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
			}
		}
		userService.updateCredentials(email, password);
		
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
		List<SkillEntity> old = skillRepo.findByUser(currUser);
		skillRepo.deleteAll(old);
		for (SkillInputModel s : input.getSkills()) {
			SkillEntity entity = new SkillEntity();
			entity.setName(s.getName());
			entity.setUser(currUser);
			skillRepo.save(entity);
		}
		
		userRepo.save(currUser);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	//add a new article for the current user
	@PostMapping("/article")
	public ResponseEntity<Object> addArticle(@RequestBody ArticleInputModel input) {
		
		try {
			UserEntity currentUser = secService.currentUser();
			
			ArticleEntity article = new ArticleEntity();
			article.setTitle(input.getTitle());
			article.setText(input.getText());
			article.setMediafile(sm.storeFile(input.getFile()));
			article.setUser(currentUser);
			article.setDateTime();
			articleRepo.save(article);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Could not save media file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//add a new comment to the article specified by articleId, with commentator the current user
	@PostMapping("/comment")
	public ResponseEntity<Object> addComment(@RequestBody CommentInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		Optional<ArticleEntity> optArticle = articleRepo.findById(input.getArticleId());
		if (optArticle.isPresent()) {
			ArticleEntity refArticle = optArticle.get();
			CommentEntity comment = new CommentEntity();
			comment.setText(input.getText());
			comment.setArticle(refArticle);
			comment.setUser(currUser);
			comment.setDateTime();
			commentRepo.save(comment);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Could not find specified article", HttpStatus.NOT_FOUND);
		}
		
	}
	
	//upvote an article specified by its article id
	//can be easily transformed to also upvote comments in the future
	@PostMapping("/upvote")
	public ResponseEntity<Object> upvote(@RequestParam Long articleId) {
		
		UserEntity currUser = secService.currentUser();
		Optional<ArticleEntity> optArticle = articleRepo.findById(articleId);
		if (optArticle.isPresent()) {
			ArticleEntity refArticle = optArticle.get();
			UpvoteEntity upvote = new UpvoteEntity(currUser, refArticle);
			upvoteRepo.save(upvote);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("Could not find specified article", HttpStatus.NOT_FOUND);
		}
		
	}
	
	//add a new connection between active user and the one specified by the parameter
	//TODO verify that connected user is not admin
	@PostMapping("/connect")
	public ResponseEntity<Object> connect(@RequestParam String email) {
		
		UserEntity currUser = secService.currentUser();
		UserEntity connUser = userRepo.findByEmail(email);
		//cant make yourself a connected person to you
		if (currUser.getId() == connUser.getId()) {
			return new ResponseEntity<>("Can't connect to self", HttpStatus.BAD_REQUEST);
		}
		ConnectionEntity connection = new ConnectionEntity(currUser, connUser);
		connRepo.save(connection);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
}
