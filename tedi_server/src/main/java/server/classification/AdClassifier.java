package server.classification;

import java.util.ArrayList;
import java.util.List;

import server.entities.AdEntity;
import server.entities.SkillEntity;
import server.utilities.Distance;

public class AdClassifier extends Classifier<AdEntity> {

	public AdClassifier() {
		super();
	}
	
	public AdClassifier(int k) {
		super(k);
	}
	
	//calculates distance based on how many common skills the ads have
	//also by hamming distance on title and description
	//TODO change it to something better
	protected int calculateDistance(AdEntity ad1, AdEntity ad2) {
		
		int totalDistance = 0;
		List<String> ad1Skills = new ArrayList<>();
		for (SkillEntity s : ad1.getSkills()) {
			ad1Skills.add(s.getName());
		}
		List<String> ad2Skills = new ArrayList<>();
		for (SkillEntity s : ad2.getSkills()) {
			ad2Skills.add(s.getName());
		}
		if (ad1Skills.size() < ad2Skills.size()) {
			for (String str : ad1Skills) {
				if (!ad2Skills.contains(str)) {
					totalDistance++;
				}
			}
		}
		else {
			for (String str : ad2Skills) {
				if (!ad1Skills.contains(str)) {
					totalDistance++;
				}
			}
		}
		
		totalDistance += Distance.Hamming(ad1.getTitle(), ad2.getTitle());
		totalDistance += Distance.Hamming(ad1.getDescription(), ad2.getDescription());
		
		return totalDistance;
		
	}
	
	protected Categories getItemCategory(AdEntity ad) {
		
		//todo return ad.getCategory()
		return null;
		
	}
	
}
