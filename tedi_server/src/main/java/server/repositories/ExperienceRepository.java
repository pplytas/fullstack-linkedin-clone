package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.ExperienceEntity;

@Repository
public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {

}
