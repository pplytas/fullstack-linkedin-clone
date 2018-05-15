package server.endpoints.outputmodels;

public class ArticleOutputModel {

	private String title;
	private String text;
	private String file;
	private String author; //this is email, perhaps change to name later
	
	public ArticleOutputModel() {}

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
	
}
