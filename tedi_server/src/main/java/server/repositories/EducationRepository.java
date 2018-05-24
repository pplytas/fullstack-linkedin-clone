package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.EducationEntity;

@Repository
public interface EducationRepository extends JpaRepository<EducationEntity, Long> {

}
