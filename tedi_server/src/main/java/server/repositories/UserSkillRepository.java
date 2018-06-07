package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.UserSkillEntity;
import server.entities.UserEntity;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkillEntity, Long> {

	List<UserSkillEntity> findByUser(UserEntity user);
	
}
