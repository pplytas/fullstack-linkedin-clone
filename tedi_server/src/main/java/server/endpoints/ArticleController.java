package server.endpoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.classification.ArticleClassifier;
import server.classification.Categories;
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
	
	@Autowired
	private ArticleClassifier articleClass;
	
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
			Categories category = articleClass.classify(article, articleRepo.findAll());
			article.setCategories(category);
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
				outA.setAuthor(a.getUser().getEmail());
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
	
	//gets the feed for the active user in chronological order
	@GetMapping("/feed")
	public ResponseEntity<Object> getFeed() {
		
		UserEntity currUser = secService.currentUser();
		List<ArticleEntity> articles = getPersonalisedFeed(currUser);
		ArticleListOutputModel output = new ArticleListOutputModel();
		try {
			for (ArticleEntity a : articles) {
				ArticleOutputModel outA = new ArticleOutputModel();
				outA.setId(a.getId());
				outA.setAuthor(a.getUser().getEmail());
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
	
	//functions below help to manipulate the feed based on the categories
	private List<ArticleEntity> getPersonalisedFeed(UserEntity currUser) {
		List<ArticleEntity> articlesDefaultOrder = articleRepo.findFeedOrderByDateTimeDesc(currUser);
		//the fact that we got the time ordered list above 
		//guarantees unaffected order in the sublists created in the map when we split
		Map<Categories, List<ArticleEntity>> articlesCategoryMap = splitByCategory(articlesDefaultOrder);
		List<CommentEntity> comments = commentRepo.findByUser(currUser);
		List<UpvoteEntity> upvotes = upvoteRepo.findByUser(currUser);
		int totalCommentsAndUpvotes = comments.size() + upvotes.size();
		if (totalCommentsAndUpvotes == 0) {
			//avoid 1/0 division in reordering
			return articlesDefaultOrder;
		}
		Map<Categories, Integer> preferenceMap = getCommentsAndUpvotesPreferenceMap(comments, upvotes);
		List<ArticleEntity> articlesPersonalisedOrder = reorderFeedArticles(articlesCategoryMap, preferenceMap, totalCommentsAndUpvotes);
		return articlesPersonalisedOrder;
	}
	
	//split the articles in a map for O(1) search time
	private Map<Categories, List<ArticleEntity>> splitByCategory(List<ArticleEntity> articles) {
		Map<Categories, List<ArticleEntity>> articleMap = new HashMap<>();
		for (ArticleEntity article : articles) {
			putArticleInMap(article, articleMap);
		}
		return articleMap;
	}
	
	private void putArticleInMap(ArticleEntity article, Map<Categories, List<ArticleEntity>> articleMap) {
		if (articleMap.containsKey(article.getCategories())) {
			articleMap.get(article.getCategories()).add(article);
		}
		else {
			List<ArticleEntity> valueList = new ArrayList<>();
			valueList.add(article);
			articleMap.put(article.getCategories(), valueList);
		}
	}
	
	private List<ArticleEntity> reorderFeedArticles(Map<Categories, List<ArticleEntity>> articlesMap, Map<Categories, Integer> preferenceMap, int totalPreferenceValues) {
		boolean remainingArticles = false;
		List<ArticleEntity> reorderedList = new ArrayList<>();
		while (remainingArticles) {
			remainingArticles = false;
			List<ArticleEntity> partialReorderedList = new ArrayList<>();
			for (Categories category : articlesMap.keySet()) {
				List<ArticleEntity> categoryList = articlesMap.get(category);
				//get ratio of reordered articles per 10
				//if some categories end, the others will take their place, just after more iterations
				int categoryEntries = preferenceMap.get(category) == null ? 1 : preferenceMap.get(category);
				for (int i=0; i<(categoryEntries/totalPreferenceValues)*10; i++) {
					if (!categoryList.isEmpty()) {
						remainingArticles = true;
						partialReorderedList.add(categoryList.remove(0));
					} 
					else {
						break;
					}
				}
			}
			partialReorderedList = partialReorderedList.stream()
			.sorted(Comparator.comparing(ArticleEntity::getDateTime))
			.collect(Collectors.toList());
			reorderedList.addAll(partialReorderedList);
		}
		return reorderedList;
	}
	
	private Map<Categories, Integer> getCommentsAndUpvotesPreferenceMap(List<CommentEntity> comments, List<UpvoteEntity> upvotes) {
		Map<Categories, Integer> preferenceMap = new HashMap<>();
		countCommentsInCategories(comments, preferenceMap);
		countUpvotesInCategories(upvotes, preferenceMap);
		return preferenceMap;
	}
	
	private void countCommentsInCategories(List<CommentEntity> comments, Map<Categories, Integer> map) {
		for (CommentEntity comment : comments) {
			if (map.containsKey(comment.getArticle().getCategories())) {
				int previousValue = map.get(comment.getArticle().getCategories());
				map.put(comment.getArticle().getCategories(), previousValue + 1);
			}
			else {
				map.put(comment.getArticle().getCategories(), 1);
			}
		}
	}
	
	private void countUpvotesInCategories(List<UpvoteEntity> upvotes, Map<Categories, Integer> map) {
		for (UpvoteEntity upvote : upvotes) {
			if (map.containsKey(upvote.getArticle().getCategories())) {
				int previousValue = map.get(upvote.getArticle().getCategories());
				map.put(upvote.getArticle().getCategories(), previousValue + 1);
			}
			else {
				map.put(upvote.getArticle().getCategories(), 1);
			}
		}
	}

}
