package server.endpoints.outputmodels;

public class AdApplicationOutputModel {
	
	AdOutputModel ad;
	UserOutputModel user;
	public AdOutputModel getAd() {
		return ad;
	}
	public UserOutputModel getUser() {
		return user;
	}
	public void setAd(AdOutputModel ad) {
		this.ad = ad;
	}
	public void setUser(UserOutputModel user) {
		this.user = user;
	}

}
