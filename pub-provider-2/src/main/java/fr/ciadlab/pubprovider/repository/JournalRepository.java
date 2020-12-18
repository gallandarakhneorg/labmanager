package fr.ciadlab.pubprovider.repository;

import fr.ciadlab.pubprovider.entities.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface JournalRepository extends JpaRepository<Journal, Integer> {
    Optional<Journal> findByJourName(String jourName);
}
