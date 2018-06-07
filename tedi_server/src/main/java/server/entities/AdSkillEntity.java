package server.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ad_skills")
public class AdSkillEntity extends SkillEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ad")
	private AdEntity ad;
	
	public AdSkillEntity() {}
	
	public AdSkillEntity(String name, AdEntity ad) {
		super(name);
		this.ad = ad;
	}

	public AdEntity getAd() {
		return ad;
	}

	public void setAd(AdEntity ad) {
		this.ad = ad;
	}
	
}
