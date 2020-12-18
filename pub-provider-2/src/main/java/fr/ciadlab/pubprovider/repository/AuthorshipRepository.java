package fr.ciadlab.pubprovider.repository;

import fr.ciadlab.pubprovider.entities.Authorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface AuthorshipRepository extends JpaRepository<Authorship, Integer> {

    void deleteByAutAutIdAndPubPubId(int autAutId, int pubPubId);

    Optional<Authorship> findDistinctByAutAutIdAndPubPubId(int autAutId, int pubPubId);

    Set<Authorship> findByPubPubId(int pubPubId);
}
