package server.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UpvoteEntityPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "user_id", insertable = false, updatable = false)
	private Long userId;
	
	@Column(name = "article_id", insertable = false, updatable = false)
	private Long articleId;
	
	public UpvoteEntityPK() {}
	
	public UpvoteEntityPK(Long userId, Long articleId) {
		this.userId = userId;
		this.articleId = articleId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public Long getArticleId() {
		return articleId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if ( !(o instanceof UpvoteEntityPK) ) return false;
		UpvoteEntityPK that = (UpvoteEntityPK) o;
		return Objects.equals(getUserId(), that.getUserId()) &&
				Objects.equals(getArticleId(), that.getArticleId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUserId(), getArticleId());
	}
	
}
