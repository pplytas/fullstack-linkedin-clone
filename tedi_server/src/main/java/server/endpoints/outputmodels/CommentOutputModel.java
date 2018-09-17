package server.endpoints.outputmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentOutputModel {
	
	private String text;
	private String dateTime;
	private String commentatorName;
	private String commentatorSurname;
	private String commentatorPicture;
	private List<ExperienceOutputModel> commentatorCurrentExperience;
	private Long commentatorId;
	
	public CommentOutputModel() {
		commentatorCurrentExperience = new ArrayList<>();
	}

	public String getText() {
		return text;
	}
	
	public String getCommentatorName() {
		return commentatorName;
	}

	public String getCommentatorSurname() {
		return commentatorSurname;
	}
	
	public String getCommentatorPicture() {
		return commentatorPicture;
	}
	
	public List<ExperienceOutputModel> getCommentatorCurrentExperience() {
		return commentatorCurrentExperience;
	}

	public Long getCommentatorId() {
		return commentatorId;
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

	public void setCommentatorPicture(String commentatorPicture) {
		this.commentatorPicture = commentatorPicture;
	}
	
	public void setCommentatorCurrentExperience(List<ExperienceOutputModel> commentatorCurrentExperience) {
		this.commentatorCurrentExperience = commentatorCurrentExperience;
	}
	
	public void setCommentatorId(Long commentatorId) {
		this.commentatorId = commentatorId;
	}
	
	public void setDateTime(Date dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dateTime = sdf.format(dateTime);
	}
	
}
