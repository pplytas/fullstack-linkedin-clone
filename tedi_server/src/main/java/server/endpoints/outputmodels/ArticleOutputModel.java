package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class ArticleOutputModel {

	private Long id;
	private String title;
	private String text;
	private String file;
	private String author; //this is email, perhaps change to name later
	private List<CommentOutputModel> comments;
	
	public ArticleOutputModel() {
		comments = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getFile() {
		return file;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public List<CommentOutputModel> getComments() {
		return comments;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setComments(List<CommentOutputModel> comments) {
		this.comments = comments;
	}
	
	public void addComment(CommentOutputModel comment) {
		this.comments.add(comment);
	}
	
}
