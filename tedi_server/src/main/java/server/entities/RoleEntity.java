package server.entities;

import java.util.ArrayList;
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

@Entity
@Table(name = "role")
public class RoleEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	private Long id;
	
	@Column
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
			mappedBy = "role", orphanRemoval = true)
	private List<UserEntity> users = new ArrayList<>();
	
	public RoleEntity() {}
	
	public RoleEntity(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public List<UserEntity> getUsers() {
		return users;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

}
