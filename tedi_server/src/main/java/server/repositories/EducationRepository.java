package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import server.entities.EducationEntity;

public interface EducationRepository extends JpaRepository<EducationEntity, Long> {

}
