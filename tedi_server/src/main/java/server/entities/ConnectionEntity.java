package server.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "connections")
public class ConnectionEntity {

	@EmbeddedId
	private ConnectionEntityPK connectionPK;
	
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name = "connected_id", insertable = false, updatable = false)
	private UserEntity connected;
	
	public ConnectionEntity() {}
	
	public ConnectionEntity(UserEntity user, UserEntity connected) {
		this.connectionPK = new ConnectionEntityPK(user.getId(), connected.getId());
		this.user = user;
		this.connected = connected;
	}

	public UserEntity getUser() {
		return user;
	}

	public UserEntity getConnected() {
		return connected;
	}
	
}
