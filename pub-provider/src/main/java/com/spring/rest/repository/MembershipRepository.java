package com.spring.rest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.rest.entities.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
	void deleteByResOrgResOrgIdAndAutAutId(int orgId, int autId);

	Optional<Membership> findDistinctByResOrgResOrgIdAndAutAutId(int orgId, int autId);
}
