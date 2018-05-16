package server.entities;

import java.text.SimpleDateFormat;
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
@Table(name = "comments")
public class CommentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String text;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article")
	private ArticleEntity article;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private UserEntity user;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	public CommentEntity() {}

	public Long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public ArticleEntity getArticle() {
		return article;
	}

	public UserEntity getUser() {
		return user;
	}
	
	public Date getDateTime() {
		return dateTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setArticle(ArticleEntity article) {
		this.article = article;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}
	
	public void setDateTime() {
		dateTime = new Date();
	}
	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
}
