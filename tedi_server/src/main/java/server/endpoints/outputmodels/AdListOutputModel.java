package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AdListOutputModel {
	
	private List<AdOutputModel> ads;
	
	@JsonIgnore
	private int lastConnEntry = 0;
	
	public AdListOutputModel() {
		ads = new ArrayList<>();
	}

	public List<AdOutputModel> getAds() {
		return ads;
	}

	public void setAds(List<AdOutputModel> ads) {
		this.ads = ads;
	}
	
	public void addAd(AdOutputModel ad) {
		this.ads.add(ad);
	}
	
	public void addAdByConn(AdOutputModel ad) {
		this.ads.add(lastConnEntry, ad);
		lastConnEntry++;
	}

}
