package com.spring.rest.controller;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rest.entities.MemberStatus;
import com.spring.rest.entities.Membership;
import com.spring.rest.entities.ResearchOrganization;
import com.spring.rest.services.ResearchOrganizationServ;

@RestController
@CrossOrigin
public class ResearchOrganizationCtrl {
	
	@Autowired
	private ResearchOrganizationServ resOrgServ;

	//Get all entities
	@RequestMapping(value="/getgetResearchOrganizations", method=RequestMethod.GET, headers="Accept=application/json")
	public List<ResearchOrganization> getgetAllResearchOrganizations() {
		return resOrgServ.getAllResearchOrganizations();
	}
	
	//Get all entities
	@RequestMapping(value="/getResearchOrganizations", method=RequestMethod.POST, headers="Accept=application/json")
	public List<ResearchOrganization> getAllResearchOrganizations() {
		return resOrgServ.getAllResearchOrganizations();
	}

	//Get one specific entity based on its Id
	@RequestMapping(value="/getResearchOrganization", method=RequestMethod.POST, headers="Accept=application/json")
	public List<ResearchOrganization> getResearchOrganization(int index) {
		return resOrgServ.getResearchOrganization(index);
	}

	//Remove one specific entity based on its Id
	@RequestMapping(value="/removeResearchOrganization", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeResearchOrganization(int index) {
		resOrgServ.removeResearchOrganization(index);
	}

	//Creates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/createResearchOrganization", method=RequestMethod.POST, headers="Accept=application/json")
	public int createResearchOrganization(String resOrgName, String resOrgDesc) {
		return resOrgServ.createResearchOrganization(resOrgName, resOrgDesc);
	}

	//Updates one specific entity based on its fields (minus its relationship fields)
	@RequestMapping(value="/updateResearchOrganization", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateResearchOrganization(int index, String resOrgName, String resOrgDesc) {
		resOrgServ.updateResearchOrganization(index, resOrgName, resOrgDesc);
	}

	//Creates a membership relation between 2 entities based on the relationship's fields and the concerned entities' Ids
	@RequestMapping(value="/addMembership", method=RequestMethod.POST, headers="Accept=application/json")
	public void addMembership(int index, int autId, Date memSinceWhen, Date memToWhen, MemberStatus memStatus) {
		resOrgServ.addMembership(index, autId, memSinceWhen, memToWhen, memStatus);
	}

	//Updates a membership relation between 2 entities based on the relationship's fields and the concerned entities' Ids
	@RequestMapping(value="/updateMembership", method=RequestMethod.POST, headers="Accept=application/json")
	public void updateMembership(int index, int autId, Date memSinceWhen, Date memToWhen, MemberStatus memStatus) {
		resOrgServ.updateMembership(index, autId, memSinceWhen, memToWhen, memStatus);
	}

	//Removes a membership relation between 2 entities based on the concerned entities' Ids
	@RequestMapping(value="/removeMembership", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeMembership(int index, int autId) {
		resOrgServ.removeMembership(index, autId);
	}

	//Creates a hierarchy between 2 organization, the organization with Id "index" will become the superOrganization of the organization with Id "resOrgId"
	@RequestMapping(value="/addSubOrganization", method=RequestMethod.POST, headers="Accept=application/json")
	public void addLinkedOrganization(int index, int resOrgId) {
		resOrgServ.addLinkedOrganization(index, resOrgId);
	}
	
	//No additional field other than sub & sup so no need for an update method for subOrganizations

	//Removes a hierarchy relation between 2 organizations based on the sup and sub Ids
	@RequestMapping(value="/removeSubOrganization", method=RequestMethod.POST, headers="Accept=application/json")
	public void removeSubOrganization(int index, int resOrgId) {
		resOrgServ.removeSubOrganization(index, resOrgId);
	}

	//Get all subOrganizations of this organization (only goes down one level in the hierarchy)
	@RequestMapping(value="/getSubOrganizations", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<ResearchOrganization> getSubOrganizations(int index) {
		return resOrgServ.getSubOrganizations(index);
	}

	//Get the supOrganization of this organization (only goes up one level in the hierarchy)
	@RequestMapping(value="/getSupOrganization", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<ResearchOrganization> getSupOrganization(int index) {
		return resOrgServ.getSupOrganization(index);
	}

	//Get the supOrganizations of this organization (goes up all levels)
	@RequestMapping(value="/getAllSubOrganizations", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<ResearchOrganization> getAllSubOrganizations(int index) {
		return resOrgServ.getAllSubOrganizations(index);
	}

	//Get the supOrganizations of this organization (goes up all levels)
	@RequestMapping(value="/getAllSupOrganizations", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<ResearchOrganization> getAllSupOrganizations(int index) {
		return resOrgServ.getAllSupOrganizations(index);
	}
	
	//Get all entities in relation with one specific entity
	@RequestMapping(value="/getLinkedOrganizations", method=RequestMethod.POST, headers="Accept=application/json")
	public Set<ResearchOrganization> getLinkedOrganizations(int index) {
		return resOrgServ.getLinkedOrganizations(index);
	}
	
	//Get one specific entity based on its Id
	@RequestMapping(value="/getMembership", method=RequestMethod.POST, headers="Accept=application/json")
	public List<Membership> getMembership(int orgId, int autId) {
		return resOrgServ.getMembership(orgId, autId);
	}
}

// Legacy //

/*
	@GetMapping(value="/getFirstResearchOrganization")
	public ResearchOrganization getFirstResearchOrganization() {
		return resOrgServ.getResearchOrganization(1);
	  }
*/