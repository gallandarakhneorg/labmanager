package fr.ciadlab.pubprovider.repository;

import fr.ciadlab.pubprovider.entities.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {

    //List<Publication> findAllByResOrgId(int resOrgId);

    List<Publication> findAllByPubAutsAutAutId(int autAutId);

    Optional<Publication> findByPubId(int pubId);

}