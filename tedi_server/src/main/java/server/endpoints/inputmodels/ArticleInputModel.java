package server.endpoints.inputmodels;

public class ArticleInputModel {
	
	private String title;
	private String text;
	private String file;
	
	public ArticleInputModel() {}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getFile() {
		return file;
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
	
}
