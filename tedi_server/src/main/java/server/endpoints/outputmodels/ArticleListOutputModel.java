package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class ArticleListOutputModel {
	
	private List<ArticleOutputModel> articles;
	
	public ArticleListOutputModel() {
		this.articles = new ArrayList<>();
	}

	public List<ArticleOutputModel> getArticles() {
		return articles;
	}

	public void setArticles(List<ArticleOutputModel> articles) {
		this.articles = articles;
	}
	
	public void addArticle(ArticleOutputModel article) {
		this.articles.add(article);
	}

}
