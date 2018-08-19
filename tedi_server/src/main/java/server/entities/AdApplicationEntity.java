package server.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ad_applications")
public class AdApplicationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private UserEntity user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ad")
	private AdEntity ad;

	public Long getId() {
		return id;
	}

	public UserEntity getUser() {
		return user;
	}

	public AdEntity getAd() {
		return ad;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public void setAd(AdEntity ad) {
		this.ad = ad;
	}

}
