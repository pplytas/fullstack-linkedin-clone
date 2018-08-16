package server.endpoints.outputmodels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentOutputModel {
	
	private String text;
	private String dateTime;
	private String commentatorName;
	private String commentatorSurname;
	private String commentatorEmail; //this is email, maybe change to name later
	
	public CommentOutputModel() {}

	public String getText() {
		return text;
	}
	
	public String getCommentatorName() {
		return commentatorName;
	}

	public String getCommentatorSurname() {
		return commentatorSurname;
	}

	public String getCommentatorEmail() {
		return commentatorEmail;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setCommentatorName(String commentatorName) {
		this.commentatorName = commentatorName;
	}

	public void setCommentatorSurname(String commentatorSurname) {
		this.commentatorSurname = commentatorSurname;
	}

	public void setCommentatorEmail(String commentatorEmail) {
		this.commentatorEmail = commentatorEmail;
	}
	
	public void setDateTime(Date dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dateTime = sdf.format(dateTime);
	}
	
}
