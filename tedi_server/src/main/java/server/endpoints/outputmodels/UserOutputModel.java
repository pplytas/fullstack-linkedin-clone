package server.endpoints.outputmodels;

public class UserOutputModel {
	
	private String email;
	private String name;
	private String surname;
	private String telNumber;
	private String picture;
	
	public UserOutputModel() {}
	
	public UserOutputModel(UserOutputBuilder builder) {
		this.email = builder.email;
		this.name = builder.name;
		this.surname = builder.surname;
		this.telNumber = builder.telNumber;
		this.picture = builder.picture;
	}

	public String getEmail() {
		return email;
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

	public void setEmail(String email) {
		this.email = email;
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
	
	public static class UserOutputBuilder {
		private final String email;
		private String name;
		private String surname;
		private String telNumber;
		private String picture;
		
		public UserOutputBuilder(String email) {
			this.email = email;
		}
		
		public UserOutputBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public UserOutputBuilder surname(String surname) {
			this.surname = surname;
			return this;
		}
		
		public UserOutputBuilder telNumber(String telNumber) {
			this.telNumber = telNumber;
			return this;
		}
		
		public UserOutputBuilder picture(String picture) {
			this.picture = picture;
			return this;
		}
		
		public UserOutputModel build() {
			return new UserOutputModel(this);
		}
		
	}
	
}
