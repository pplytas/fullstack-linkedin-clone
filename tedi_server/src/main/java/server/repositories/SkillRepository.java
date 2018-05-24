package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.SkillEntity;
import server.entities.UserEntity;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, Long> {

	List<SkillEntity> findByUser(UserEntity user);
	
}
