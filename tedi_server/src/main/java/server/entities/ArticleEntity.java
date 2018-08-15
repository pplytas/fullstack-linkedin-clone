package server.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import server.classification.Categories;

@Entity
@Table(name = "article")
public class ArticleEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String title;
	
	@Column
	private String text;
	
	@Column
	private String mediafile;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private UserEntity user;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "article", orphanRemoval = true)
	private List<CommentEntity> comments = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "article", orphanRemoval = true)
	private List<UpvoteEntity> upvotes = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
			mappedBy = "referencedArticle", orphanRemoval = true)
	private List<NotificationEntity> notifications = new ArrayList<>();
	
	@Column
	private Categories category;
	
	public ArticleEntity() {}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	public String getMediafile() {
		return mediafile;
	}
	
	public Date getDateTime() {
		return this.dateTime;
	}
	
	public UserEntity getUser() {
		return user;
	}
	
	public List<CommentEntity> getComments() {
		return comments;
	}
	
	public List<UpvoteEntity> getUpvotes() {
		return upvotes;
	}
	
	public List<NotificationEntity> getNotifications() {
		return notifications;
	}
	
	public Categories getCategories() {
		return category;
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

	public void setMediafile(String mediafile) {
		this.mediafile = mediafile;
	}
	
	public void setDateTime() {
		this.dateTime = new Date();
	}
	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	public void setComments(List<CommentEntity> comments) {
		this.comments = comments;
	}
	
	public void addComment(CommentEntity comment) {
		this.comments.add(comment);
	}
	
	public void setUpvotes(List<UpvoteEntity> upvotes) {
		this.upvotes = upvotes;
	}
	
	public void addUpvote(UpvoteEntity upvote) {
		this.upvotes.add(upvote);
	}
	
	public void setNotifications(List<NotificationEntity> notifications) {
		this.notifications = notifications;
	}
	
	public void addNotification(NotificationEntity notification) {
		this.notifications.add(notification);
	}
	
	public void setCategories(Categories category) {
		this.category = category;
	}
	
}
