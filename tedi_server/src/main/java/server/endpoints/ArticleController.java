package server.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.endpoints.inputmodels.ArticleInputModel;
import server.endpoints.inputmodels.CommentInputModel;
import server.endpoints.outputmodels.ArticleListOutputModel;
import server.endpoints.outputmodels.ArticleOutputModel;
import server.endpoints.outputmodels.CommentOutputModel;
import server.endpoints.outputmodels.UpvoteOutputModel;
import server.entities.ArticleEntity;
import server.entities.CommentEntity;
import server.entities.UpvoteEntity;
import server.entities.UserEntity;
import server.repositories.ArticleRepository;
import server.repositories.CommentRepository;
import server.repositories.UpvoteRepository;
import server.repositories.UserRepository;
import server.utilities.StorageManager;

@RestController
public class ArticleController {
	
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
	
	//add a new article for the current user
	@PostMapping("/article/new")
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
	@PostMapping("/article/comment")
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
	@PostMapping("/article/upvote")
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

}
