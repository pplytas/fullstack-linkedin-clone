package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.AdSkillEntity;

@Repository
public interface AdSkillRepository extends JpaRepository<AdSkillEntity, Long> {

}
