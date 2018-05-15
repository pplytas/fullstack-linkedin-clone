package server.entities;

import java.io.IOException;
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
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import server.endpoints.inputmodels.RegisterInputModel;
import server.utilities.StorageManager;

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
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "user")
	private List<ArticleEntity> articles;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles = new HashSet<>();
	
	public UserEntity() {}
	
	public UserEntity(UserBuilder builder) {
		this.email = builder.email;
		this.password = builder.password;
		this.name = builder.name;
		this.surname = builder.surname;
		this.telNumber = builder.telNumber;
		this.picture = builder.picture;
		this.roles = builder.roles;
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
	
	public List<ArticleEntity> getArticles() {
		return articles;
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
	
	public void setArticles(List<ArticleEntity> articles) {
		this.articles = articles;
	}
	
	public void addArticle(ArticleEntity article) {
		this.articles.add(article);
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
