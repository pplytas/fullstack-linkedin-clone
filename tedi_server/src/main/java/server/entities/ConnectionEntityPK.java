package server.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ConnectionEntityPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "user_id", insertable = false, updatable = false)
	private Long userId;
	
	@Column(name = "connected_id", insertable = false, updatable = false)
	private Long connectedId;
	
	public ConnectionEntityPK() {}
	
	public ConnectionEntityPK(Long userId, Long connectedId) {
		this.userId = userId;
		this.connectedId = connectedId;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public Long getConnectedId() {
		return connectedId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if ( !(o instanceof ConnectionEntityPK) ) return false;
		ConnectionEntityPK that = (ConnectionEntityPK) o;
		return Objects.equals(getUserId(), that.getUserId()) &&
				Objects.equals(getConnectedId(), that.getConnectedId());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getUserId(), getConnectedId());
	}
	
}
