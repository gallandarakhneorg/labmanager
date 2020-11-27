package com.spring.rest.repository;

import com.spring.rest.entities.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    void deleteByResOrgResOrgIdAndAutAutId(int orgId, int autId);

    Optional<Membership> findDistinctByResOrgResOrgIdAndAutAutId(int orgId, int autId);
}
