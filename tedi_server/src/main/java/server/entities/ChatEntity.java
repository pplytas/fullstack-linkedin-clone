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
@Table(name = "chats")
public class ChatEntity {
	
	//since this is auto incremental id, to retrieve the message list we just sort by this in decreasing order
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender")
	private UserEntity sender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver")
	private UserEntity receiver;
	
	@Column
	private String message;
	
	public ChatEntity() {}

	public Long getId() {
		return id;
	}

	public UserEntity getSender() {
		return sender;
	}

	public UserEntity getReceiver() {
		return receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSender(UserEntity sender) {
		this.sender = sender;
	}

	public void setReceiver(UserEntity receiver) {
		this.receiver = receiver;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
