package server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.classification.Categories;
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
import server.utilities.StorageManager;

@Service
public class ArticleService {

	@Autowired
	private StorageManager sm;
	
	@Autowired
	private ArticleRepository articleRepo;
	
	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private UpvoteRepository upvoteRepo;
	
	public ArticleOutputModel convertArticleToOutput(ArticleEntity article) throws IOException {
		ArticleOutputModel output = new ArticleOutputModel();
		output.setId(article.getId());
		output.setAuthorName(article.getUser().getName());
		output.setAuthorSurname(article.getUser().getSurname());
		output.setAuthorEmail(article.getUser().getEmail());
		output.setTitle(article.getTitle());
		output.setText(article.getText());
		output.setFile(sm.getFile(article.getMediafile()));
		output.setDateTime(article.getDateTime());
		List<CommentEntity> comments = commentRepo.findByArticleOrderByDateTimeDesc(article);
		for (CommentEntity c : comments) {
			CommentOutputModel cOut = new CommentOutputModel();
			cOut.setText(c.getText());
			cOut.setCommentatorName(c.getUser().getName());
			cOut.setCommentatorSurname(c.getUser().getSurname());
			cOut.setCommentatorEmail(c.getUser().getEmail());
			cOut.setDateTime(c.getDateTime());
			output.addComment(cOut);
		}
		List<UpvoteEntity> upvotes = upvoteRepo.findByArticle(article);
		for (UpvoteEntity u : upvotes) {
			UpvoteOutputModel uOut = new UpvoteOutputModel();
			uOut.setUpvoterName(u.getUser().getName());
			uOut.setUpvoterSurname(u.getUser().getSurname());
			uOut.setUpvoterEmail(u.getUser().getEmail());
			output.addUpvote(uOut);
		}
		return output;
	}
	
	public List<ArticleEntity> getPersonalisedFeed(UserEntity currUser) {
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
		boolean remainingArticles = true;
		List<ArticleEntity> reorderedList = new ArrayList<>();
		while (remainingArticles) {
			remainingArticles = false;
			List<ArticleEntity> partialReorderedList = new ArrayList<>();
			for (Categories category : articlesMap.keySet()) {
				List<ArticleEntity> categoryList = articlesMap.get(category);
				//get ratio of reordered articles per 10
				//if some categories end, the others will take their place, just after more iterations
				//categoryEntries is double to weight the 1 upvote case
				double categoryEntries = preferenceMap.get(category) == null ? 0.5 : preferenceMap.get(category);
				int articleLimit = (int)Math.floor((categoryEntries/totalPreferenceValues)*10) != 0 ? 
						(int)Math.floor((categoryEntries/totalPreferenceValues)*10) : 1;
				for (int i=0; i<articleLimit; i++) {
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
			.sorted(Comparator.comparing(ArticleEntity::getDateTime).reversed())
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
