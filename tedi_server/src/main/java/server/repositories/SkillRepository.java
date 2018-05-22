package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import server.entities.SkillEntity;

public interface SkillRepository extends JpaRepository<SkillEntity, Long> {

}
