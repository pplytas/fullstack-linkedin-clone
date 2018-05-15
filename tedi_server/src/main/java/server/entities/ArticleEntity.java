package server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private UserEntity user;
	
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
	
	public UserEntity getUser() {
		return user;
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
	
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
}
