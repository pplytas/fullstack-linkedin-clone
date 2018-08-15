package server.classification;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.entities.AdEntity;
import server.entities.AdSkillEntity;
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
	protected int calculateDistance(AdEntity ad1, AdEntity ad2) {
		
		int totalDistance = 0;
		Set<String> ad1Skills = new HashSet<>();
		if (ad1.getSkills() == null || ad2.getSkills() == null) {
			return 0;
		}
		for (SkillEntity s : ad1.getSkills()) {
			ad1Skills.add(s.getName().toLowerCase());
		}
		Set<String> ad2Skills = new HashSet<>();
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
		
		totalDistance += Distance.Levenshtein(ad1.getTitle(), ad2.getTitle());
		totalDistance += Distance.Levenshtein(ad1.getDescription(), ad2.getDescription());
		
		return totalDistance;
		
	}
	
	protected Categories getItemCategory(AdEntity ad) {
		return ad.getCategories();		
	}
	
	//to disable viewing these ads, all we need to do is to leave them without user and handle that from elsewhere (ignore ones with null user)
	@PostConstruct
	protected void generateInitialDataset() {
		//creating 3 test ads for each category, to help in data training
		for (int i=1; i<=3; i++) {
			if (adRepo.findByTitleAndPublisherIsNull("software" + i) == null) {
				AdEntity test = new AdEntity();
				test.setTitle("software" + i);
				//not adding a description means ignoring description check for initial data set
				test.addSkill(new AdSkillEntity("software", test));
				test.addSkill(new AdSkillEntity("junion", test));
				test.addSkill(new AdSkillEntity("developer", test));
				test.addSkill(new AdSkillEntity("development", test));
				test.addSkill(new AdSkillEntity("senior", test));
				test.addSkill(new AdSkillEntity("programming", test));
				test.addSkill(new AdSkillEntity("maintenance", test));
				test.setCategories(Categories.SOFTWARE);
				adRepo.save(test);
			}
		}
		for (int i=1; i<=3; i++) {
			if (adRepo.findByTitleAndPublisherIsNull("telecommunications"+i) == null) {
				AdEntity test = new AdEntity();
				test.setTitle("telecommunications" + i);
				test.addSkill(new AdSkillEntity("network", test));
				test.addSkill(new AdSkillEntity("maintenance", test));
				test.addSkill(new AdSkillEntity("networks", test));
				test.addSkill(new AdSkillEntity("wireless", test));
				test.addSkill(new AdSkillEntity("adaptable", test));
				test.addSkill(new AdSkillEntity("mobile", test));
				test.addSkill(new AdSkillEntity("telecommunication", test));
				test.setCategories(Categories.TELECOMMUNICATIONS);
				adRepo.save(test);
			}
		}
		for (int i=1; i<=3; i++) {
			if (adRepo.findByTitleAndPublisherIsNull("hr"+i) == null) {
				AdEntity test = new AdEntity();
				test.setTitle("hr" + i);
				test.addSkill(new AdSkillEntity("adaptable", test));
				test.addSkill(new AdSkillEntity("motivation", test));
				test.addSkill(new AdSkillEntity("innovative", test));
				test.addSkill(new AdSkillEntity("openminded", test));
				test.addSkill(new AdSkillEntity("adaptable", test));
				test.setCategories(Categories.HR);
				adRepo.save(test);
			}
		}
	}
	
}
