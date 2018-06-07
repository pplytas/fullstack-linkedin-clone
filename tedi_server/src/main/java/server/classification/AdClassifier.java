package server.classification;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.entities.AdEntity;
import server.entities.SkillEntity;
import server.repositories.AdRepository;
import server.utilities.Distance;

@Component
public class AdClassifier extends Classifier<AdEntity> {

	@Autowired
	private AdRepository adRepo;
	
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
			ad1Skills.add(s.getName().toLowerCase());
		}
		List<String> ad2Skills = new ArrayList<>();
		for (SkillEntity s : ad2.getSkills()) {
			ad2Skills.add(s.getName().toLowerCase());
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
		return ad.getCategories();		
	}
	
	//to disable viewing these ads, all we need to do is to leave them without user and handle that from the endpoint (ignore ones with null user)
	@PostConstruct
	protected void generateInitialDataset() {
		//creating 3 test ads for each category, to help in data training
		//TODO
	}
	
}
