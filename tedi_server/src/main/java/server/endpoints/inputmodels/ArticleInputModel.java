package server.endpoints.inputmodels;

public class ArticleInputModel {
	
	private String title;
	private String text;
	private String media;
	
	public ArticleInputModel() {}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getMedia() {
		return media;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setMedia(String media) {
		this.media = media;
	}
	
}
