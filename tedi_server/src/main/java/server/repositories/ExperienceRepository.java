package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.ExperienceEntity;
import server.entities.UserEntity;

@Repository
public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {

	List<ExperienceEntity> findByUser(UserEntity user);
	
}
