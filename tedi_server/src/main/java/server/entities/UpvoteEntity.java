package server.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "upvotes")
public class UpvoteEntity {

	@EmbeddedId
	private UpvoteEntityPK upvoteId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name = "article_id", insertable = false, updatable = false)
	private ArticleEntity article;
	
	public UpvoteEntity() {}
	
	public UpvoteEntity(UserEntity user, ArticleEntity article) {
		this.upvoteId = new UpvoteEntityPK(user.getId(), article.getId());
		this.user = user;
		this.article = article;
	}
	
	public UserEntity getUser() {
		return user;
	}
	
	public ArticleEntity getArticle() {
		return article;
	}
	
}
