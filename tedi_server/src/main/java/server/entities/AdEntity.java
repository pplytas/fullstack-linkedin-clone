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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import server.classification.Categories;

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
	private Date publishDate;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "ad", orphanRemoval = true)
	private List<AdSkillEntity> skills = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publisher")
	private UserEntity publisher;
	
	@Column
	private Categories category;
	
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

	public Date getPublishDate() {
		return publishDate;
	}

	public List<AdSkillEntity> getSkills() {
		return skills;
	}
	
	public UserEntity getPublisher() {
		return publisher;
	}
	
	public Categories getCategories() {
		return category;
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
	
	public void setPublishDate() {
		this.publishDate = new Date();
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public void setSkills(List<AdSkillEntity> skills) {
		this.skills = skills;
	}
	
	public void addSkill(AdSkillEntity skill) {
		this.skills.add(skill);
	}
	
	public void setPublisher(UserEntity publisher) {
		this.publisher = publisher;
	}
	
	public void setCategories(Categories category) {
		this.category = category;
	}
	
}
