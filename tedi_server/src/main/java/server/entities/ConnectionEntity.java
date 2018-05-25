package server.entities;

import javax.persistence.Column;
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
	
	@Column
	private Boolean isPending;
	
	public ConnectionEntity() {
		//NOT TO BE USED IN OUR CODE
	}
	
	public ConnectionEntity(UserEntity user, UserEntity connected, boolean isPending) {
		this.connectionPK = new ConnectionEntityPK(user.getId(), connected.getId());
		this.user = user;
		this.connected = connected;
		this.isPending = isPending;
	}

	public UserEntity getUser() {
		return user;
	}

	public UserEntity getConnected() {
		return connected;
	}

	public Boolean getIsPending() {
		return isPending;
	}

	public void setIsPending(boolean isPending) {
		this.isPending = isPending;
	}
	
	
	
}
