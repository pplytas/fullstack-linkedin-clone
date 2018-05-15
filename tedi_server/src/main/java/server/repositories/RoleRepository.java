package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import server.entities.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	
}
