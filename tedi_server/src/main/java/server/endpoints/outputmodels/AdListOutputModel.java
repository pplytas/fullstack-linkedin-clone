package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class AdListOutputModel {
	
	private List<AdOutputModel> ads;
	
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

}
