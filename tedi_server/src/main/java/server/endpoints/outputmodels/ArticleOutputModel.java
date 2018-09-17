package server.endpoints.outputmodels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleOutputModel {

	private Long id;
	private String title;
	private String text;
	private String media;
	private String authorName;
	private String authorSurname;
	private String authorPicture;
	private List<ExperienceOutputModel> currentExperience;
	private Long authorId;
	private List<CommentOutputModel> comments;
	private List<UpvoteOutputModel> upvotes;
	private String dateTime;
	
	public ArticleOutputModel() {
		currentExperience = new ArrayList<>();
		comments = new ArrayList<>();
		upvotes = new ArrayList<>();
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

	public String getMedia() {
		return media;
	}
	
	public String getAuthorName() {
		return authorName;
	}

	public String getAuthorSurname() {
		return authorSurname;
	}

	public String getAuthorPicture() {
		return authorPicture;
	}
	
	public List<ExperienceOutputModel> getCurrentExperience() {
		return currentExperience;
	}
	
	public Long getAuthorId() {
		return authorId;
	}

	public List<CommentOutputModel> getComments() {
		return comments;
	}
	
	public List<UpvoteOutputModel> getUpvotes() {
		return upvotes;
	}
	
	public String getDateTime() {
		return dateTime;
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

	public void setMedia(String media) {
		this.media = media;
	}
	
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public void setAuthorSurname(String authorSurname) {
		this.authorSurname = authorSurname;
	}
	
	public void setAuthorPicture(String authorPicture) {
		this.authorPicture = authorPicture;
	}

	public void setCurrentExperience(List<ExperienceOutputModel> currentExperience) {
		this.currentExperience = currentExperience;
	}
	
	public void addCurrentExperience(ExperienceOutputModel currentExperience) {
		this.currentExperience.add(currentExperience);
	}
	
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	
	public void setComments(List<CommentOutputModel> comments) {
		this.comments = comments;
	}
	
	public void addComment(CommentOutputModel comment) {
		this.comments.add(comment);
	}
	
	public void setUpvotes(List<UpvoteOutputModel> upvotes) {
		this.upvotes = upvotes;
	}
	
	public void addUpvote(UpvoteOutputModel upvote) {
		this.upvotes.add(upvote);
	}
	
	public void setDateTime(Date dateTime) {
		if (dateTime == null) {
			this.dateTime = null;
			return;
		}
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dateTime = sdf.format(dateTime);
	}
	
}
