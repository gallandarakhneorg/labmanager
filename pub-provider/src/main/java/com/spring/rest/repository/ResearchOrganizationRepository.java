package com.spring.rest.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import com.spring.rest.entities.ResearchOrganization;

public interface ResearchOrganizationRepository extends JpaRepository<ResearchOrganization, Integer> {

	Set<ResearchOrganization> findDistinctByOrgAutsAutAutId(int autId);

	Set<ResearchOrganization> findDistinctByOrgSupResOrgId(int resOrgId);

	Set<ResearchOrganization> findDistinctByOrgSubsResOrgId(int resOrgId);
	
	
	
}