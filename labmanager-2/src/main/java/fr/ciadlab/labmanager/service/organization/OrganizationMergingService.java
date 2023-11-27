/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.service.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructure;
import fr.ciadlab.labmanager.entities.assostructure.AssociatedStructureHolder;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganizationComparator;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.repository.assostructure.AssociatedStructureHolderRepository;
import fr.ciadlab.labmanager.repository.assostructure.AssociatedStructureRepository;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.organization.ResearchOrganizationRepository;
import fr.ciadlab.labmanager.repository.project.ProjectRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.utils.names.OrganizationNameComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the merging organizations.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@Service
public class OrganizationMergingService extends AbstractService {

	private final ResearchOrganizationService organizationService;

	private final ResearchOrganizationRepository organizationRepository;

	private final MembershipRepository membershipRepository;

	private final ProjectRepository projectRepository;

	private final AssociatedStructureRepository structureRepository;

	private final AssociatedStructureHolderRepository structureHolderRepository;

	private OrganizationNameComparator nameComparator;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param organizationService the organization service.
	 * @param organizationRepository the organization repository.
	 * @param membershipRepository the repository of the organization memberships.
	 * @param projectRepository the repository of the projects.
	 * @param structureRepository the repository of the associated structures.
	 * @param structureHolderRepository the repository of the associated structures' holders.
	 * @param nameComparator the comparator of organization names.
	 */
	public OrganizationMergingService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired ProjectRepository projectRepository,
			@Autowired AssociatedStructureRepository structureRepository,
			@Autowired AssociatedStructureHolderRepository structureHolderRepository,
			@Autowired OrganizationNameComparator nameComparator) {
		super(messages, constants);
		this.organizationService = organizationService;
		this.organizationRepository = organizationRepository;
		this.membershipRepository = membershipRepository;
		this.projectRepository = projectRepository;
		this.structureRepository = structureRepository;
		this.structureHolderRepository = structureHolderRepository;
		this.nameComparator = nameComparator;
	}

	/** Replies the duplicate organizations.
	 * The replied list contains groups of organizations who have similar acronym or names.
	 *
	 * @param comparator comparator of organizations that is used for sorting the groups of duplicates. If it is {@code null},
	 *      a {@link ResearchOrganizationComparator} is used.
	 * @param callback the callback invoked during the building.
	 * @return the duplicate persons that is finally computed.
	 * @throws Exception if a problem occurred during the building.
	 */
	public List<Set<ResearchOrganization>> getOrganizationDuplicates(Comparator<? super ResearchOrganization> comparator, OrganizationDuplicateCallback callback) throws Exception {
		// Each list represents a group of organizations that could be duplicate
		final List<Set<ResearchOrganization>> matchingOrganizations = new ArrayList<>();

		// Copy the list of organizations into another list in order to enable its
		// modification during the function's process
		final List<ResearchOrganization> organizationsList = new ArrayList<>(this.organizationService.getAllResearchOrganizations());

		final Comparator<? super ResearchOrganization> theComparator = comparator == null ? EntityUtils.getPreferredResearchOrganizationComparator() : comparator;

		final int total = organizationsList.size();
		// Notify the callback
		if (callback != null) {
			callback.onDuplicate(0, 0, total);
		}
		int duplicateCount = 0;
		
		for (int i = 0; i < organizationsList.size() - 1; ++i) {
			final ResearchOrganization referenceOrganization = organizationsList.get(i);

			final Set<ResearchOrganization> currentMatching = new TreeSet<>(theComparator);
			currentMatching.add(referenceOrganization);

			final ListIterator<ResearchOrganization> iterator2 = organizationsList.listIterator(i + 1);
			while (iterator2.hasNext()) {
				final ResearchOrganization otherOrganization = iterator2.next();
				if (this.nameComparator.isSimilar(
						referenceOrganization.getAcronym(), referenceOrganization.getName(),
						otherOrganization.getAcronym(), otherOrganization.getName())) {
					currentMatching.add(otherOrganization);
					++duplicateCount;
					// Consume the other person to avoid to be treated twice times
					iterator2.remove();
				}
			}
			if (currentMatching.size() > 1) {
				matchingOrganizations.add(currentMatching);
			}
			// Notify the callback
			if (callback != null) {
				callback.onDuplicate(i, duplicateCount, total);
			}
		}

		return matchingOrganizations;
	}

	/** Merge the entities by replacing those with an old organization by those with the new organization.
	 *
	 * @param source the list of the identifiers of the organizations to remove and replace by the target organization.
	 * @param target the identifier of the target organization which should replace the source organizations.
	 * @throws Exception if the merging cannot be completed.
	 */
	public void mergeOrganizationsById(Collection<Integer> source, Integer target) throws Exception {
		assert target != null;
		assert source != null;
		final Optional<ResearchOrganization> optTarget = this.organizationRepository.findById(target);
		if (optTarget.isPresent()) {
			final ResearchOrganization targetOrganization = optTarget.get();
			final List<ResearchOrganization> sourceOrganizations = this.organizationRepository.findAllById(source);
			if (sourceOrganizations.size() != source.size()) {
				for (final ResearchOrganization ro : sourceOrganizations) {
					if (!source.contains(Integer.valueOf(ro.getId()))) {
						throw new IllegalArgumentException("Source organization not found with identifier: " + ro.getId()); //$NON-NLS-1$
					}
				}
				throw new IllegalArgumentException("Source organization not found"); //$NON-NLS-1$
			}
			mergeOrganizations(sourceOrganizations, targetOrganization);
		} else {
			throw new IllegalArgumentException("Target organization not found with identifier: " + target); //$NON-NLS-1$
		}
	}

	/** Merge the entities by replacing those with an old organization by those with the new organization.
	 *
	 * @param sources the list of organizations to remove and replace by the target organization.
	 * @param target the target organization who should replace the source organizations.
	 * @throws Exception if the merging cannot be completed.
	 */
	public void mergeOrganizations(Iterable<ResearchOrganization> sources, ResearchOrganization target) throws Exception {
		assert target != null;
		assert sources != null;
		boolean changed = false;
		for (final ResearchOrganization source : sources) {
			if (source.getId() != target.getId()) {
				getLogger().info("Reassign to " + target.getAcronymOrName() + " the elements of " + source.getAcronymOrName()); //$NON-NLS-1$ //$NON-NLS-2$
				boolean lchange = reassignOrganizationMemberships(source, target);
				lchange = reassignProjects(source, target) || lchange;
				lchange = reassignAssociatedStructures(source, target) || lchange;
				this.organizationService.removeResearchOrganization(source.getId());
				changed = changed || lchange;
			}
		}
		if (changed) {
			this.organizationRepository.save(target);
		}
	}

	/** Re-assign the organization memberships attached to the source organization to the target organization.
	 * 
	 * @param sources the organization to remove and replace by the target organization.
	 * @param target the target organization which should replace the source organizations.
	 * @return {@code true} if organization membership has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignOrganizationMemberships(ResearchOrganization source, ResearchOrganization target) throws Exception {
		boolean changed = false;
		for (final Membership membership : this.membershipRepository.findDistinctByResearchOrganizationId(source.getId())) {
			membership.setResearchOrganization(target);
			this.membershipRepository.save(membership);
			changed = true;
		}
		return changed;
	}

	/** Re-assign the project organizations attached to the source organization to the target organization.
	 * 
	 * @param sources the organization to remove and replace by the target organization.
	 * @param target the target organization who should replace the source organizations.
	 * @return {@code true} if project has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignProjects(ResearchOrganization source, ResearchOrganization target) throws Exception {
		final Set<Project> changed = new TreeSet<>(EntityUtils.getPreferredProjectComparator());
		
		final int sourceId = source.getId();
		
		for (final Project project : this.projectRepository.findAll()) {
			if (project.getCoordinator() != null && project.getCoordinator().getId() == sourceId) {
				project.setCoordinator(target);
				changed.add(project);
			}
			if (project.getLocalOrganization() != null && project.getLocalOrganization().getId() == sourceId) {
				project.setLocalOrganization(target);
				changed.add(project);
			}
			if (project.getSuperOrganization() != null && project.getSuperOrganization().getId() == sourceId) {
				project.setSuperOrganization(target);
				changed.add(project);
			}
			if (project.getLearOrganization() != null && project.getLearOrganization().getId() == sourceId) {
				project.setLearOrganization(target);
				changed.add(project);
			}
			final Set<ResearchOrganization> otherPartners = new TreeSet<>(project.getOtherPartnersRaw());
			boolean found = false;
			final Iterator<ResearchOrganization> iterator = otherPartners.iterator();
			while (iterator.hasNext()) {
				final ResearchOrganization orga = iterator.next();
				if (orga != null && orga.getId() == sourceId) {
					found = true;
					iterator.remove();
				}
			}
			if (found) {
				otherPartners.add(target);
				project.setOtherPartners(otherPartners);
				changed.add(project);
			}
		}
		//
		if (changed.isEmpty()) {
			return false;
		}
		for (final Project project : changed) {
			this.projectRepository.save(project);
		}
		return true;
	}

	/** Re-assign the associated structure's attached to the source organization to the target organization.
	 * 
	 * @param source the organization to remove and replace by the target organization.
	 * @param target the target organization who should replace the source organizations.
	 * @return {@code true} if associated structure has changed.
	 * @throws Exception if the change cannot be completed.
	 */
	protected boolean reassignAssociatedStructures(ResearchOrganization source, ResearchOrganization target) throws Exception {
		final Set<AssociatedStructure> changed = new TreeSet<>(EntityUtils.getPreferredAssociatedStructureComparator());
		final int sourceId = source.getId();
		for (final AssociatedStructure structure : this.structureRepository.findAll()) {
			if (structure.getFundingOrganization() != null && structure.getFundingOrganization().getId() == sourceId) {
				structure.setFundingOrganization(target);
				changed.add(structure);
			}
		}
		final boolean structureChanged = !changed.isEmpty();
		if (structureChanged) {
			for (final AssociatedStructure structure : changed) {
				this.structureRepository.save(structure);
			}
		}
		//
		final Set<AssociatedStructureHolder> changed1 = new TreeSet<>(EntityUtils.getPreferredAssociatedStructureHolderComparator());
		for (final AssociatedStructureHolder holder : this.structureHolderRepository.findAll()) {
			if (holder.getOrganization() != null && holder.getOrganization().getId() == sourceId) {
				holder.setOrganization(target);
				changed1.add(holder);
			}
			if (holder.getSuperOrganization() != null && holder.getSuperOrganization().getId() == sourceId) {
				holder.setSuperOrganization(target);
				changed1.add(holder);
			}
		}
		final boolean holderChanged = !changed1.isEmpty();
		if (holderChanged) {
			for (final AssociatedStructureHolder holder : changed1) {
				this.structureHolderRepository.save(holder);
			}
		}
		//
		return structureChanged || holderChanged;
	}

	/** Callback that is invoked when building the list of duplicate organizations.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.2
	 */
	@FunctionalInterface
	public interface OrganizationDuplicateCallback {

		/** Invoked for each organization.
		 *
		 * @param index the position of the reference organization in the list of organizations. It represents the progress of the treatment
		 *     of each organization.
		 * @param duplicateCount the count of discovered duplicates.
		 * @param total the total number of organizations in the list.
		 * @throws Exception if there is an error during the callback treatment. This exception is forwarded to the
		 *     caller of the function that has invoked this callback.
		 */
		void onDuplicate(int index, int duplicateCount, int total) throws Exception;

	}

}
