/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.pubprovider.service;

import fr.ciadlab.pubprovider.entities.Author;
import fr.ciadlab.pubprovider.entities.MemberStatus;
import fr.ciadlab.pubprovider.entities.Membership;
import fr.ciadlab.pubprovider.entities.ResearchOrganization;
import fr.ciadlab.pubprovider.repository.AuthorRepository;
import fr.ciadlab.pubprovider.repository.MembershipRepository;
import fr.ciadlab.pubprovider.repository.ResearchOrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResearchOrganizationService {
    @Autowired
    private ResearchOrganizationRepository repo;

    @Autowired
    private MembershipRepository memRepo;

    @Autowired
    private AuthorRepository autRepo;

    public List<ResearchOrganization> getAllResearchOrganizations() {
        return repo.findAll();
    }

    public Optional<ResearchOrganization> getResearchOrganization(int index) {
        return repo.findById(index);
    }

    public Optional<ResearchOrganization> getResearchOrganizationByName(String name) {
        final List<ResearchOrganization> allResearchOrganizations = getAllResearchOrganizations();
        Optional<ResearchOrganization> ciadResearchOrganization = allResearchOrganizations.parallelStream().filter(o -> o.getResOrgName().equals(name)).findFirst();
        return ciadResearchOrganization;
    }


    public List<Membership> getOrganizationMembers(ResearchOrganization organization, Integer active, String status) {

        final List<Membership> members = new ArrayList<>();
        // Fill members
        members.addAll(organization.getOrgAuts());

        // Now filter members with parameters
        if (active != null) {
            if (active == 1) {
                // Keep only active members : remove year > 1970 && < date.now
                members.removeIf(m -> m.getMemToWhen().getYear() + 1900 > 1970 && m.getMemToWhen().before(new java.util.Date()));
            } else {
                // Keep only non active members : remove year == 1970 && > date.now
                members.removeIf(m -> m.getMemToWhen().getYear() + 1900 == 1970 || m.getMemToWhen().after(new java.util.Date()));
            }
        } else {
            // Keep all members
        }

        if (status != null) {
            // Filter by status
            if (status.equals("Teacher-Researchers")) {
                // Keep only following status
                members.removeIf(m ->
                        m.getMemStatus() != MemberStatus.MCF &&
                                m.getMemStatus() != MemberStatus.MCF_HDR &&
                                m.getMemStatus() != MemberStatus.LRU &&
                                m.getMemStatus() != MemberStatus.ECC &&
                                m.getMemStatus() != MemberStatus.PR
                );
            } else {
                members.removeIf(m -> m.getMemStatus() != MemberStatus.valueOf(status));
            }
        } else {
            // Keep all members
        }
        return members;
    }

    /**
     * Gets the list of other organizations for members
     * @param members the members to find orgs for
     * @param currentOrganizationName the current org name in order to find *others* orgs
     * @return map of members id and other organizations in list
     */
    public Map<Integer, List<ResearchOrganization>> getOtherOrganizationsForMembers(List<Membership> members, String currentOrganizationName) {
        final List<ResearchOrganization> allResearchOrganizations = getAllResearchOrganizations();
        Map<Integer, List<ResearchOrganization>> otherOrganisationsForMembers = new HashMap<>();
        for (Membership member : members) {
            List<ResearchOrganization> otherOrgs = allResearchOrganizations.parallelStream().filter(o ->
                    o.getOrgAuts().stream().anyMatch(m -> m.getAut().getAutId() == member.getAut().getAutId()) && !o.getResOrgName().equals(currentOrganizationName)
            ).collect(Collectors.toList());
            otherOrganisationsForMembers.put(member.getMemId(), otherOrgs);
        }
        return otherOrganisationsForMembers;
    }

    public void removeResearchOrganization(int index) {
        final Optional<ResearchOrganization> res = repo.findById(index);
        if (res.isPresent()) {
            if (res.get().getOrgSubs().isEmpty()) {
                repo.deleteById(index);
            }
        }
    }

    public int createResearchOrganization(String resOrgName, String resOrgDesc) {
        final ResearchOrganization res = new ResearchOrganization();
        //ResearchOrganization fields
        res.setResOrgName(resOrgName);
        res.setResOrgDesc(resOrgDesc);
        this.repo.save(res);
        return res.getResOrgId();
    }

    public void updateResearchOrganization(int resOrgId, String resOrgName, String resOrgDesc) {
        final Optional<ResearchOrganization> res = this.repo.findById(resOrgId);
        if (res.isPresent()) {
            //Generic pub fields
            if (!resOrgName.isEmpty())
                res.get().setResOrgName(resOrgName);
            if (!resOrgDesc.isEmpty())
                res.get().setResOrgDesc(resOrgDesc);
            this.repo.save(res.get());
        }
    }

    //Here we assume we dont need further than the aut & the org associated
    public List<Membership> getMembership(int orgId, int autId) {
        final Optional<Membership> res = memRepo.findDistinctByResOrgResOrgIdAndAutAutId(orgId, autId);
        List<Membership> memL = new LinkedList<Membership>();
        if (res.isPresent()) {
            res.get().getAut().setAutPubs(new HashSet<>());
            res.get().getAut().setAutOrgs(new HashSet<>());
            res.get().getResOrg().setOrgSup(null);
            res.get().getResOrg().setOrgSubs(new HashSet<>());
            res.get().getResOrg().setOrgAuts(new HashSet<>());
            memL.add(res.get());
        }
        return memL;
    }

    public void addMembership(int index, int autId, java.sql.Date memSinceWhen, java.sql.Date memToWhen, MemberStatus memStatus) {
        final Optional<ResearchOrganization> org = repo.findById(index);
        final Optional<Author> aut = autRepo.findById(autId);
        if (org.isPresent() && aut.isPresent()) {
            //disjoint checks if they already dont have an existing membership they share
            if (Collections.disjoint(org.get().getOrgAuts(), aut.get().getAutOrgs())) {
                final Membership mem = new Membership();
                mem.setAut(aut.get());
                mem.setResOrg(org.get());
                mem.setMemSinceWhen(memSinceWhen);
                mem.setMemToWhen(memToWhen);
                mem.setMemStatus(memStatus);
                this.memRepo.save(mem);
            }
        }
    }

    public void updateMembership(int index, int autId, java.sql.Date memSinceWhen, Date memToWhen, MemberStatus memStatus) {
        final Optional<Membership> res = memRepo.findDistinctByResOrgResOrgIdAndAutAutId(index, autId);
        if (res.isPresent()) {
            //Generic pub fields
            if (memSinceWhen != null)
                res.get().setMemSinceWhen(memSinceWhen);
            if (memToWhen != null)
                res.get().setMemToWhen(memToWhen);
            if (memStatus != null)
                res.get().setMemStatus(memStatus);
            this.memRepo.save(res.get());
        }
    }

    @Transactional
    //Dunno why but its specifically needed there
    public void removeMembership(int index, int autId) {
        memRepo.deleteByResOrgResOrgIdAndAutAutId(index, autId);
    }

    public void addLinkedOrganization(int index, int resOrgId) {
        final Optional<ResearchOrganization> supOrg = repo.findById(index);
        final Optional<ResearchOrganization> subOrg = repo.findById(resOrgId);

        if (supOrg.isPresent() && subOrg.isPresent()) {
            subOrg.get().getOrgSubs().add(subOrg.get());
            subOrg.get().setOrgSup(supOrg.get());
            repo.save(subOrg.get());
            repo.save(supOrg.get());
        }
    }

    public void removeSubOrganization(int index, int resOrgId) {
        final Optional<ResearchOrganization> supOrg = repo.findById(index);
        final Optional<ResearchOrganization> subOrg = repo.findById(resOrgId);

        if (supOrg.isPresent() && subOrg.isPresent()) {
            subOrg.get().getOrgSubs().remove(subOrg.get());
            subOrg.get().setOrgSup(null);
            repo.save(subOrg.get());
            repo.save(supOrg.get());
        }
    }

}
