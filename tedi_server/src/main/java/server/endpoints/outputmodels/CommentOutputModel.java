package server.endpoints.outputmodels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentOutputModel {
	
	private String text;
	private String dateTime;
	private String commentator; //this is email, maybe change to name later
	
	public CommentOutputModel() {}

	public String getText() {
		return text;
	}

	public String getCommentator() {
		return commentator;
	}
	
	public String getDateTime() {
		return dateTime;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setCommentator(String commentator) {
		this.commentator = commentator;
	}
	
	public void setDateTime(Date dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dateTime = sdf.format(dateTime);
	}
	
}
