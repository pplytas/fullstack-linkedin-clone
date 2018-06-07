package server.classification;

import java.util.ArrayList;
import java.util.List;

import server.entities.SkillEntity;
import server.entities.UserEntity;

public class UserClassifier extends Classifier<UserEntity> {
	
	public UserClassifier() {
		super();
	}
	
	public UserClassifier(int k) {
		super(k);
	}
	
	//calculates distance based on how many common skills the users have
	//TODO change it to something better
	protected int calculateDistance(UserEntity user1, UserEntity user2) {
		
		int totalDistance = 0;
		List<String> user1Skills = new ArrayList<>();
		for (SkillEntity s : user1.getSkills()) {
			user1Skills.add(s.getName());
		}
		List<String> user2Skills = new ArrayList<>();
		for (SkillEntity s : user2.getSkills()) {
			user2Skills.add(s.getName());
		}
		if (user1Skills.size() < user2Skills.size()) {
			for (String str : user1Skills) {
				if (!user2Skills.contains(str)) {
					totalDistance++;
				}
			}
		}
		else {
			for (String str : user2Skills) {
				if (!user1Skills.contains(str)) {
					totalDistance++;
				}
			}
		}
		
		return totalDistance;
		
	}
	
	protected Categories getItemCategory(UserEntity user) {
		
		//todo return user.getCategory()
		return null;
		
	}

}
