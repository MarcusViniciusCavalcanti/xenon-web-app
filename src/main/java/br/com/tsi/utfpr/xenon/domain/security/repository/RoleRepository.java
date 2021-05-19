package br.com.tsi.utfpr.xenon.domain.security.repository;

import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
