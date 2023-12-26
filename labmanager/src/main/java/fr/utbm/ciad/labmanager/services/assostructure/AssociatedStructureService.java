/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.services.assostructure;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureHolder;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureRepository;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureType;
import fr.utbm.ciad.labmanager.data.assostructure.HolderRole;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationRepository;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.services.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service for the associated structures.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@Service
public class AssociatedStructureService extends AbstractService {

	private AssociatedStructureRepository structureRepository;

	private ResearchOrganizationRepository organizationRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param structureRepository the repository for the associated structures.
	 * @param organizationRepository the repository for the research organizations.
	 */
	public AssociatedStructureService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired AssociatedStructureRepository structureRepository,
			@Autowired ResearchOrganizationRepository organizationRepository) {
		super(messages, constants);
		this.structureRepository = structureRepository;
		this.organizationRepository = organizationRepository;
	}

	/** Replies the associated structure with the given identifier.
	 *
	 * @param id the identifier of the expected structure.
	 * @return the associated structure, or {@code null} if there is no associated structure with the given id.
	 */
	public AssociatedStructure getAssociatedStructureById(int id) {
		final var structureOpt = this.structureRepository.findById(Long.valueOf(id));
		if (structureOpt.isPresent()) {
			return structureOpt.get();
		}
		return null;
	}

	/** Remove the associated structure with the given identifier.
	 *
	 * @param identifier the identifier of the structure to remove.
	 */
	public void removeAssociatedStructure(long identifier) {
		final var id = Long.valueOf(identifier);
		final var structureOpt = this.structureRepository.findById(Long.valueOf(identifier));
		if (structureOpt.isPresent()) {
			final var structure = structureOpt.get();
			//
			structure.setFundingOrganization(null);
			structure.setHolders(null);
			structure.setType((AssociatedStructureType) null);
			this.structureRepository.deleteById(id);
		}
	}

	/** Create an associated structure.
	 *
	 * @param validated indicates if the structure is validated by a local authority.
	 * @param acronym the short name of acronym of the associated structure.
	 * @param name the name of the associated structure.
	 * @param type the name of the type of associated structure.
	 * @param creationDate the creation date of the associated structure in format {@code YYY-MM-DD}.
	 * @param creationDuration the duration of the creation of the associated structure in months.
	 * @param fundingOrganization the identifier of the research organization which is funding the associated structure.
	 * @param holders the list of the holding persons.
	 * @param description the public description of the associated structure (markdown syntax is accepted).
	 * @param budget the budget for creating the associated structure.
	 * @param projects list of projects that are related to the creation of the associated structure.
	 * @param confidential indicates if the project should be confidential or not.
	 * @return the reference to the created structure.
	 */
	public Optional<AssociatedStructure> createAssosiatedStructure(boolean validated, String acronym, String name,
			AssociatedStructureType type, LocalDate creationDate, int creationDuration, long fundingOrganization,
			Map<Long, HolderDescription> holders, String description, float budget,
			List<? extends Project> projects, boolean confidential) {
		final var structure = new AssociatedStructure();
		try {
			updateAssociatedStructure(structure, validated, acronym, name, type, creationDate, creationDuration,
					fundingOrganization, holders, description, budget, projects, confidential);
		} catch (Throwable ex) {
			// Delete created structure
			if (structure.getId() != 0) {
				try {
					removeAssociatedStructure(structure.getId());
				} catch (Throwable ex0) {
					// Silent
				}
			}
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
		return Optional.of(structure);
	}

	/** Update an associated structure.
	 *
	 * @param structureId the identifier of the associated structure to be updated.
	 * @param validated indicates if the structure is validated by a local authority.
	 * @param acronym the short name of acronym of the associated structure.
	 * @param name the name of the associated structure.
	 * @param type the name of the type of associated structure.
	 * @param creationDate the creation date of the associated structure in format {@code YYY-MM-DD}.
	 * @param creationDuration the duration of the creation of the associated structure in months.
	 * @param fundingOrganization the identifier of the research organization which is funding the associated structure.
	 * @param holders the list of the holding persons.
	 * @param description the public description of the associated structure (markdown syntax is accepted).
	 * @param budget the budget for creating the associated structure.
	 * @param projects list of projects that are related to the creation of the associated structure.
	 * @param confidential indicates if the project should be confidential or not.
	 * @return the reference to the created structure.
	 */
	public Optional<AssociatedStructure> updateAssociatedStructure(long structureId, boolean validated, String acronym, String name,
			AssociatedStructureType type, LocalDate creationDate, int creationDuration, long fundingOrganization,
			Map<Long, HolderDescription> holders, String description, float budget,
			List<? extends Project> projects, boolean confidential) {
		final Optional<AssociatedStructure> res;
		if (structureId >= 0) {
			res = this.structureRepository.findById(Long.valueOf(structureId));
		} else {
			res = Optional.empty();
		}
		if (res.isPresent()) {
			updateAssociatedStructure(res.get(), validated, acronym, name, type, creationDate, creationDuration,
					fundingOrganization, holders, description, budget, projects, confidential);
		}
		return res;
	}

	/** Update an associated structure.
	 *
	 * @param structure the associated structure to be updated.
	 * @param validated indicates if the structure is validated by a local authority.
	 * @param acronym the short name of acronym of the associated structure.
	 * @param name the name of the associated structure.
	 * @param type the name of the type of associated structure.
	 * @param creationDate the creation date of the associated structure in format {@code YYY-MM-DD}.
	 * @param creationDuration the duration of the creation of the associated structure in months.
	 * @param fundingOrganization the identifier of the research organization which is funding the associated structure.
	 * @param holders the list of the holding persons.
	 * @param description the public description of the associated structure (markdown syntax is accepted).
	 * @param budget the budget for creating the associated structure.
	 * @param projects list of projects that are related to the creation of the associated structure.
	 * @param confidential indicates if the structure should be confidential or not.
	 */
	protected void updateAssociatedStructure(AssociatedStructure structure, boolean validated, String acronym, String name,
			AssociatedStructureType type, LocalDate creationDate, int creationDuration, long fundingOrganization,
			Map<Long, HolderDescription> holders, String description, float budget,
			List<? extends Project> projects, boolean confidential) {
		structure.setValidated(validated);
		structure.setAcronym(acronym);
		structure.setName(name);
		structure.setType(type);
		structure.setCreationDate(creationDate);
		structure.setCreationDuration(creationDuration);
		structure.setDescription(description);
		structure.setBudget(budget);
		structure.setProjects(projects);
		structure.setConfidential(confidential);
		this.structureRepository.save(structure);

		// Link the organization
		final var fundingOrg = this.organizationRepository.findById(Long.valueOf(fundingOrganization));
		if (fundingOrg.isEmpty()) {
			throw new IllegalArgumentException("Funding organization not found with id " + fundingOrganization); //$NON-NLS-1$
		}
		structure.setFundingOrganization(fundingOrg.get());
		this.structureRepository.save(structure);

		// Link the holders
		final var structureHolders = new ArrayList<AssociatedStructureHolder>();
		if (holders != null && !holders.isEmpty() && holders.size() != structureHolders.size()) {
			holders.entrySet().stream().forEach(it -> {
				final var desc = it.getValue();
				final var holderObject = new AssociatedStructureHolder();
				holderObject.setPerson(desc.person);
				holderObject.setRole(desc.role);
				holderObject.setRoleDescription(desc.roleDescription);
				holderObject.setOrganization(desc.organization);
				holderObject.setSuperOrganization(desc.superOrganization);
				structureHolders.add(holderObject);
			});
		}
		structure.setHolders(structureHolders);
		this.structureRepository.save(structure);
	}

	/** Description of an associated structure holder.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.2
	 */
	public static class HolderDescription {

		/** Holding person.
		 */
		public final Person person;

		/** Holding person's role.
		 */
		public final HolderRole role;

		/** Description of the holding person's role.
		 */
		public final String roleDescription;

		/** Organization of the holding person.
		 */
		public final ResearchOrganization organization;

		/** Super-organization of the holding person.
		 */
		public final ResearchOrganization superOrganization;

		/** Constructor.
		 * 
		 * @param person the holding person.
		 * @param role the role of the person.
		 * @param roleDescription the description of the role.
		 * @param organization the organization of the person.
		 * @param superOrganization the super organization, or {@code null}.
		 */
		public HolderDescription(Person person, HolderRole role, String roleDescription,
				ResearchOrganization organization, ResearchOrganization superOrganization) {
			this.person = person;
			this.role = role;
			this.roleDescription = roleDescription;
			this.organization = organization;
			this.superOrganization = superOrganization;
		}

	}

	/** Replies the list of all the associated structures.
	 *
	 * @return the list of all the associated structures.
	 */
	public List<AssociatedStructure> getAllAssociatedStructures() {
		return this.structureRepository.findAll();
	}

	/** Replies the list of all the associated structures.
	 *
	 * @param filter the filter of structures.
	 * @return the list of all the associated structures.
	 * @since 4.0
	 */
	public List<AssociatedStructure> getAllAssociatedStructures(Specification<AssociatedStructure> filter) {
		return this.structureRepository.findAll(filter);
	}

	/** Replies the list of all the associated structures.
	 *
	 * @param filter the filter of structures.
	 * @param sortOrder the order specification to use for sorting the associated structures.
	 * @return the list of all the associated structures.
	 * @since 4.0
	 */
	public List<AssociatedStructure> getAllAssociatedStructures(Specification<AssociatedStructure> filter, Sort sortOrder) {
		return this.structureRepository.findAll(filter, sortOrder);
	}

	/** Replies the list of all the associated structures.
	 *
	 * @param sortOrder the order specification to use for sorting the associated structures.
	 * @return the list of all the associated structures.
	 * @since 4.0
	 */
	public List<AssociatedStructure> getAllAssociatedStructures(Sort sortOrder) {
		return this.structureRepository.findAll(sortOrder);
	}

	/** Replies the list of all the associated structures.
	 *
	 * @param pageable the manager of pages.
	 * @return the list of all the associated structures.
	 * @since 4.0
	 */
	public Page<AssociatedStructure> getAllAssociatedStructures(Pageable pageable) {
		return this.structureRepository.findAll(pageable);
	}

	/** Replies the list of all the associated structures.
	 *
	 * @param pageable the manager of pages.
	 * @param filter the filter of structures.
	 * @return the list of all the associated structures.
	 * @since 4.0
	 */
	public Page<AssociatedStructure> getAllAssociatedStructures(Pageable pageable, Specification<AssociatedStructure> filter) {
		return this.structureRepository.findAll(filter, pageable);
	}

	/** Replies the list of the associated structures that are associated to the organization with the given identifier.
	 *
	 * @param id the identifier of the organization.
	 * @return the list of associated structures.
	 */
	public List<AssociatedStructure> getAssociatedStructuresByOrganizationId(long id) {
		final var idObj = Long.valueOf(id);
		return this.structureRepository.findDistinctOrganizationAssociatedStructures(Boolean.FALSE, idObj);
	}

	/** Replies the list of the associated structures that are associated to the persons with the given identifier.
	 *
	 * @param id the identifier of the person.
	 * @return the list of associated structures.
	 */
	public List<AssociatedStructure> getAssociatedStructuresByPersonId(long id) {
		final var idObj = Long.valueOf(id);
		return this.structureRepository.findDistinctPersonAssociatedStructures(Boolean.FALSE, idObj);
	}

	/** Replies if the given identifier is for a person who is involved in an associated structure, whatever her/his role.
	 *
	 * @param id the identifier of the person.
	 * @return {@code true} if the person is involved in an associated structure.
	 * @since 3.6
	 */
	public boolean isInvolved(long id) {
		return !getAssociatedStructuresByPersonId(id).isEmpty();
	}

	/** Start the editing of the given associated structure.
	 *
	 * @param structure the associated structure to save.
	 * @return the editing context that enables to keep track of any information needed
	 *      for saving the associated structure and its related resources.
	 */
	public EditingContext startEditing(AssociatedStructure structure) {
		assert structure != null;
		return new EditingContext(structure);
	}

	/** Context for editing a {@link AssociatedStructure}.
	 * This context is usually defined when the entity is associated to
	 * external resources in the server file system.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public class EditingContext implements Serializable {

		private static final long serialVersionUID = -7800294119864189541L;

		private AssociatedStructure structure;

		/** Constructor.
		 *
		 * @param structure the edited associated structure.
		 */
		EditingContext(AssociatedStructure structure) {
			this.structure = structure;
		}

		/** Replies the associated structure.
		 *
		 * @return the associated structure.
		 */
		public AssociatedStructure getAssociatedStructure() {
			return this.structure;
		}

		/** Save the associated structure in the JPA database.
		 *
		 * <p>After calling this function, it is preferable to not use
		 * the associated structure object that was provided before the saving.
		 * Invoke {@link #getAssociatedStructure()} for obtaining the new structure
		 * instance, since the content of the saved object may have totally changed.
		 */
		@Transactional
		public void save() {
			this.structure = AssociatedStructureService.this.structureRepository.save(this.structure);
		}

	}

}
