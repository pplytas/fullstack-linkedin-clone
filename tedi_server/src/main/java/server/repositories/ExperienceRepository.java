package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import server.entities.ExperienceEntity;

public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {

}
