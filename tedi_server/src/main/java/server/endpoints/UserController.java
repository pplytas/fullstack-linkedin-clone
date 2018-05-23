package server.endpoints;

import java.io.IOException;
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
import server.entities.ArticleEntity;
import server.entities.CommentEntity;
import server.entities.UpvoteEntity;
import server.entities.UserEntity;
import server.repositories.ArticleRepository;
import server.repositories.CommentRepository;
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
	private ArticleRepository articleRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private UpvoteRepository upvoteRepo;
	
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
	
}
