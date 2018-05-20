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
@Table(name = "experience")
public class ExperienceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String company;
	
	@Column
	private String position;
	
	@Temporal(value = TemporalType.DATE)
	private Date start;
	
	@Temporal(value = TemporalType.DATE)
	private Date finish;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	private UserEntity user;
	
	public ExperienceEntity() {}

	public Long getId() {
		return id;
	}

	public String getCompany() {
		return company;
	}

	public String getPosition() {
		return position;
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

	public void setCompany(String company) {
		this.company = company;
	}

	public void setPosition(String position) {
		this.position = position;
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
