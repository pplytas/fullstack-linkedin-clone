package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.SkillEntity;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, Long> {

}
