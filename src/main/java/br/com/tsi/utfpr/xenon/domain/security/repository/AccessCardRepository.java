package br.com.tsi.utfpr.xenon.domain.security.repository;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessCardRepository extends JpaRepository<AccessCard, Long> {

    Optional<AccessCard> findByUsername(String username);
}
