package fr.ciadlab.pubprovider.repository;

import fr.ciadlab.pubprovider.entities.Author;
import fr.ciadlab.pubprovider.entities.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Set<Author> findDistinctByAutPubsPubPubId(int pubPubId);

    Set<Author> findDistinctByAutOrgsResOrgResOrgId(int resOrgId);

    Set<Author> findByAutFirstNameAndAutLastName(String autFirstName, String autlastName);

    Set<Author> findDistinctByAutOrgsResOrgResOrgNameAndAutOrgsMemStatus(String resOrgName, MemberStatus status);
}
