package server.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "notifications")
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private UserEntity referencedUser;
	
	@Column
	private ArticleEntity referencedArticle;

	@Column
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private UserEntity user;
	
	public NotificationEntity() {}

	public Long getId() {
		return id;
	}

	public UserEntity getReferencedUser() {
		return referencedUser;
	}
	
	public ArticleEntity getReferencedArticle() {
		return referencedArticle;
	}

	public String getMessage() {
		return message;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setReferencedUser(UserEntity referencedUser) {
		this.referencedUser = referencedUser;
	}
	
	public void setReferencedArticle(ArticleEntity referencedArticle) {
		this.referencedArticle = referencedArticle;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDateTime() {
		dateTime = new Date();
	}
	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
}
