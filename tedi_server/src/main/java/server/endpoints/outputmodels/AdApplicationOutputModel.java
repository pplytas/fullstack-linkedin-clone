package server.endpoints.outputmodels;

public class AdApplicationOutputModel {
	
	private Long id; //id used to change application status
	private AdOutputModel ad;
	private UserOutputModel user;
	private Integer status;
	
	public Long getId() {
		return id;
	}
	
	public AdOutputModel getAd() {
		return ad;
	}
	public UserOutputModel getUser() {
		return user;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setAd(AdOutputModel ad) {
		this.ad = ad;
	}
	
	public void setUser(UserOutputModel user) {
		this.user = user;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}

}
