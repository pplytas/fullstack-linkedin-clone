package server.endpoints;

import java.io.IOException;
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
import server.endpoints.inputmodels.ArticleInputModel;
import server.endpoints.outputmodels.ArticleListOutputModel;
import server.endpoints.outputmodels.ArticleOutputModel;
import server.entities.ArticleEntity;
import server.entities.UserEntity;
import server.repositories.ArticleRepository;
import server.repositories.UserRepository;
import server.utilities.StorageManager;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private StorageManager sm;
	
	@Autowired 
	private SecurityService secService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ArticleRepository articleRepo;
	
	@PostMapping("/article")
	public ResponseEntity<Object> addArticle(@RequestBody ArticleInputModel input) {
		
		try {
			UserEntity currentUser = secService.currentUser();
			
			ArticleEntity article = new ArticleEntity();
			article.setTitle(input.getTitle());
			article.setText(input.getText());
			article.setMediafile(sm.storeFile(input.getFile()));
			article.setUser(currentUser);
			articleRepo.save(article);
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Could not save media file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	//todo get articles for user
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
		
		List<ArticleEntity> articles = articleRepo.findByUser(refUser);
		try {
			ArticleListOutputModel output = new ArticleListOutputModel();
			for (ArticleEntity a : articles) {
				ArticleOutputModel outA = new ArticleOutputModel();
				outA.setAuthor(refUser.getEmail());
				outA.setTitle(a.getTitle());
				outA.setText(a.getText());
				outA.setFile(sm.getFile(a.getMediafile()));
				output.addArticle(outA);
			}
			return new ResponseEntity<>(output, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Could not load media file", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
