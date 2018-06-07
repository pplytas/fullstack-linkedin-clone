package server.classification;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.entities.SkillEntity;
import server.entities.UserEntity;
import server.entities.UserSkillEntity;
import server.repositories.UserRepository;

@Component
public class UserClassifier extends Classifier<UserEntity> {
	
	@Autowired
	private UserRepository userRepo;
	
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
			user1Skills.add(s.getName().toLowerCase());
		}
		List<String> user2Skills = new ArrayList<>();
		for (SkillEntity s : user2.getSkills()) {
			user2Skills.add(s.getName().toLowerCase());
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
		return user.getCategory();
	}

	//to disable logins in this user, all we need to do is to set email to something non permitted (to avoid conflicts on new registered users)
	//and not save a role for the user, thus not saving him in the SpringSecurity table
	@PostConstruct
	protected void generateInitialDataset() {
		//creating 3 test users for each category, to help in data training
		for (int i=1; i<=3; i++) {
			if (userRepo.findByEmail("software" + i) == null) {
				UserEntity test = new UserEntity.UserBuilder("software" + i, null).build();
				test.setCategories(Categories.SOFTWARE);
				test.addSkill(new UserSkillEntity("software", test));
				test.addSkill(new UserSkillEntity("programming", test));
				test.addSkill(new UserSkillEntity("C++", test));
				test.addSkill(new UserSkillEntity("C", test));
				test.addSkill(new UserSkillEntity("C#", test));
				test.addSkill(new UserSkillEntity("java", test));
				test.addSkill(new UserSkillEntity("python", test));
				test.addSkill(new UserSkillEntity("sql", test));
				test.addSkill(new UserSkillEntity("mysql", test));
				test.addSkill(new UserSkillEntity("mongodb", test));
				test.addSkill(new UserSkillEntity("postgre", test));
				test.addSkill(new UserSkillEntity("database", test));
				test.addSkill(new UserSkillEntity("prolog", test));
				test.addSkill(new UserSkillEntity("haskell", test));
				test.addSkill(new UserSkillEntity("development", test));
				test.addSkill(new UserSkillEntity("web", test));
				test.addSkill(new UserSkillEntity("javascript", test));
				userRepo.save(test);
			}
		}
		for (int i=1; i<=3; i++) {
			if (userRepo.findByEmail("telecom" + i) == null) {
				UserEntity test = new UserEntity.UserBuilder("telecom" + i, null).build();
				test.setCategories(Categories.TELECOMMUNICATIONS);
				test.addSkill(new UserSkillEntity("networks", test));
				test.addSkill(new UserSkillEntity("networking", test));
				test.addSkill(new UserSkillEntity("signals", test));
				test.addSkill(new UserSkillEntity("telecommunication", test));
				test.addSkill(new UserSkillEntity("wireless", test));
				test.addSkill(new UserSkillEntity("mobile", test));
				userRepo.save(test);
			}
		}
		for (int i=1; i<=3; i++) {
			if (userRepo.findByEmail("hr" + i) == null) {
				UserEntity test = new UserEntity.UserBuilder("hr" + i, null).build();
				test.setCategories(Categories.HR);
				test.addSkill(new UserSkillEntity("hr", test));
				test.addSkill(new UserSkillEntity("motivation", test));
				test.addSkill(new UserSkillEntity("leading", test));
				test.addSkill(new UserSkillEntity("innovation", test));
				test.addSkill(new UserSkillEntity("communication", test));
				test.addSkill(new UserSkillEntity("openminded", test));
				userRepo.save(test);
			}
		}
	}
	
}
