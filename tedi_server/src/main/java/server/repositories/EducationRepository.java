package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.EducationEntity;
import server.entities.UserEntity;

@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, Long> {
	
	List<EducationEntity> findByUser(UserEntity user);

}
