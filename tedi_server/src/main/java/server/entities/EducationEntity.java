package server.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "education")
public class EducationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String organization;
	
	@Temporal(value = TemporalType.DATE)
	private Date start;
	
	@Temporal(value = TemporalType.DATE)
	private Date finish;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private UserEntity user;
	
	public EducationEntity() {}

	public Long getId() {
		return id;
	}

	public String getOrganization() {
		return organization;
	}

	public Date getStart() {
		return start;
	}

	public Date getFinish() {
		return finish;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setFinish(Date finish) {
		this.finish = finish;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}
