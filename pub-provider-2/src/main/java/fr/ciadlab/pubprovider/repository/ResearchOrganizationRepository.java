package fr.ciadlab.pubprovider.repository;

import fr.ciadlab.pubprovider.entities.ResearchOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ResearchOrganizationRepository extends JpaRepository<ResearchOrganization, Integer> {

    Set<ResearchOrganization> findDistinctByOrgAutsAutAutId(int autId);

    Set<ResearchOrganization> findDistinctByOrgSupResOrgId(int resOrgId);

    Set<ResearchOrganization> findDistinctByOrgSubsResOrgId(int resOrgId);


}