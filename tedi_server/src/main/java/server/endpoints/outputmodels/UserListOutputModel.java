package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class UserListOutputModel {

	private List<UserOutputModel> users;
	
	public UserListOutputModel() {
		this.users = new ArrayList<>();
	}

	public List<UserOutputModel> getUsers() {
		return users;
	}

	public void setUsers(List<UserOutputModel> users) {
		this.users = users;
	}
	
	public void addUser(UserOutputModel user) {
		this.users.add(user);
	}
	
}
