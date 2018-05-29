package server.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@Column
	private String name;
	
	@Column
	private String surname;
	
	@Column
	private String telNumber;
	
	@Column
	private String picture;
	
	@Column
	private Boolean educationPublic;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "user")
	private List<EducationEntity> education = new ArrayList<>();
	
	@Column
	private Boolean experiencePublic;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "user")
	private List<ExperienceEntity> experience = new ArrayList<>();
	
	@Column
	private Boolean skillsPublic;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "user")
	private List<SkillEntity> skills = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "user")
	private List<ArticleEntity> articles = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "user")
	private List<CommentEntity> comments = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "user")
	private List<UpvoteEntity> upvotes = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "user", orphanRemoval = true)
	private List<ConnectionEntity> connections = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "connected", orphanRemoval = true)
	private List<ConnectionEntity> connectedTo = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "sender", orphanRemoval = true)
	private List<ChatEntity> sentMessages = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "receiver", orphanRemoval = true)
	private List<ChatEntity> receivedMessages = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "publisher", orphanRemoval = true)
	private List<AdEntity> ads = new ArrayList<>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles = new HashSet<>();
	
	public UserEntity() {
		this.educationPublic = false;
		this.experiencePublic = false;
		this.skillsPublic = false;
	}
	
	public UserEntity(UserBuilder builder) {
		this.email = builder.email;
		this.password = builder.password;
		this.name = builder.name;
		this.surname = builder.surname;
		this.telNumber = builder.telNumber;
		this.picture = builder.picture;
		this.roles = builder.roles;
		this.educationPublic = false;
		this.experiencePublic = false;
		this.skillsPublic = false;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getTelNumber() {
		return telNumber;
	}

	public String getPicture() {
		return picture;
	}
	
	public boolean isEducationPublic() {
		return educationPublic;
	}

	public List<EducationEntity> getEducation() {
		return education;
	}
	
	public boolean isExperiencePublic() {
		return experiencePublic;
	}
	
	public List<ExperienceEntity> getExperience() {
		return experience;
	}
	
	public boolean isSkillsPublic() {
		return skillsPublic;
	}
	
	public List<SkillEntity> getSkills() {
		return skills;
	}
	
	public List<ArticleEntity> getArticles() {
		return articles;
	}
	
	public List<CommentEntity> getComments() {
		return comments;
	}
	
	public List<UpvoteEntity> getUpvotes() {
		return upvotes;
	}
	
	public List<ConnectionEntity> getConnections() {
		return connections;
	}
	
	public List<ConnectionEntity> getConnectedTo() {
		return connectedTo;
	}
	
	public List<ChatEntity> getSentMessages() {
		return sentMessages;
	}
	
	public List<ChatEntity> getReceivedMessages() {
		return receivedMessages;
	}
	
	public List<AdEntity> getAds() {
		return ads;
	}
	
	public Set<RoleEntity> getRoles() {
		return roles;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setTelNumber(String telNumber) {
		this.telNumber = telNumber;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public void setEducationPublic(boolean educationPublic) {
		this.educationPublic = educationPublic;
	}
	
	public void setEducation(List<EducationEntity> education) {
		this.education = education;
	}
	
	public void addEducation(EducationEntity education) {
		this.education.add(education);
	}
	
	public void setExperiencePublic(boolean experiencePublic) {
		this.experiencePublic = experiencePublic;
	}
	
	public void setExperience(List<ExperienceEntity> experience) {
		this.experience = experience;
	}
	
	public void addExperience(ExperienceEntity experience) {
		this.experience.add(experience);
	}
	
	public void setSkillsPublic(boolean skillsPublic) {
		this.skillsPublic = skillsPublic;
	}
	
	public void setSkills(List<SkillEntity> skills) {
		this.skills = skills;
	}
	
	public void addSkill(SkillEntity skill) {
		this.skills.add(skill);
	}
	
	public void setArticles(List<ArticleEntity> articles) {
		this.articles = articles;
	}
	
	public void addArticle(ArticleEntity article) {
		this.articles.add(article);
	}
	
	public void setComments(List<CommentEntity> comments) {
		this.comments = comments;
	}
	
	public void addComment(CommentEntity comment) {
		this.comments.add(comment);
	}
	
	public void setUpvotes(List<UpvoteEntity> upvotes) {
		this.upvotes = upvotes;
	}
	
	public void addUpvote(UpvoteEntity upvote) {
		this.upvotes.add(upvote);
	}
	
	public void setConnections(List<ConnectionEntity> connections) {
		this.connections = connections;
	}
	
	public void addConnection(ConnectionEntity connection) {
		this.connections.add(connection);
	}
	
	public void setConnectedTo(List<ConnectionEntity> connectedTo) {
		this.connectedTo = connectedTo;
	}
	
	public void addConnectedTo(ConnectionEntity connectedTo) {
		this.connectedTo.add(connectedTo);
	}
	
	public void setSentMessages(List<ChatEntity> sentMessages) {
		this.sentMessages = sentMessages;
	}
	
	public void addSentMessage(ChatEntity sentMessage) {
		this.sentMessages.add(sentMessage);
	}
	
	public void setReceivedMessages(List<ChatEntity> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}
	
	public void addReceivedMessage(ChatEntity receivedMessage) {
		this.receivedMessages.add(receivedMessage);
	}
	
	public void setAds(List<AdEntity> ads) {
		this.ads = ads;
	}
	
	public void addAd(AdEntity ad) {
		this.ads.add(ad);
	}
	
	public void setRoles(Set<RoleEntity> roles) {
		this.roles = roles;
	}
	
	public void addRole(RoleEntity role) {
		this.roles.add(role);
	}
	
	public static class UserBuilder {
		private final String email;
		private final String password;
		private String name;
		private String surname;
		private String telNumber;
		private String picture;
		private Set<RoleEntity> roles = new HashSet<>();
		
		public UserBuilder(String email, String password) {
			this.email = email;
			this.password = password;
		}
		
		public UserBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public UserBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		public UserBuilder telNumber(String telNumber) {
			this.telNumber = telNumber;
			return this;
		}
		
		public UserBuilder picture(String picture) {
			this.picture = picture;
			return this;
		}
		
		public UserBuilder roles(Set<RoleEntity> roles) {
			this.roles = roles;
			return this;
		}
		
		public UserBuilder role(RoleEntity role) {
			this.roles.add(role);
			return this;
		}
		
		public UserEntity build() {
			return new UserEntity(this);
		}
		
	}
	
}