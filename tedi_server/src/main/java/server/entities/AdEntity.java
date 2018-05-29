package server.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ads")
public class AdEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String title;
	
	@Column
	private String description;
	
	@Temporal(value = TemporalType.DATE)
	private Date postDate;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "ad")
	private List<AdSkillEntity> skills = new ArrayList<>();
	
	public AdEntity() {}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Date getPostDate() {
		return postDate;
	}

	public List<AdSkillEntity> getSkills() {
		return skills;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public void setSkills(List<AdSkillEntity> skills) {
		this.skills = skills;
	}
	
}
