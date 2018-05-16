package server.endpoints.inputmodels;

public class CommentInputModel {

	private Long articleId;
	private String text;
	
	public CommentInputModel() {}

	public Long getArticleId() {
		return articleId;
	}

	public String getText() {
		return text;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
