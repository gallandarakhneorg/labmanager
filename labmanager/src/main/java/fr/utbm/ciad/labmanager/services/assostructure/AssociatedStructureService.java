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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import fr.utbm.ciad.labmanager.configuration.ConfigurationConstants;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureHolder;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureRepository;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureType;
import fr.utbm.ciad.labmanager.data.assostructure.HolderRole;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationRepository;
import fr.utbm.ciad.labmanager.data.project.Project;
import fr.utbm.ciad.labmanager.services.AbstractEntityService;
import fr.utbm.ciad.labmanager.utils.HasAsynchronousUploadService;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/** Service for the associated structures.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@Service
public class AssociatedStructureService extends AbstractEntityService<AssociatedStructure> {

	private AssociatedStructureRepository structureRepository;

	private ResearchOrganizationRepository organizationRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param structureRepository the repository for the associated structures.
	 * @param organizationRepository the repository for the research organizations.
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param sessionFactory the Hibernate session factory.
	 */
	public AssociatedStructureService(
			@Autowired AssociatedStructureRepository structureRepository,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired MessageSourceAccessor messages,
			@Autowired ConfigurationConstants constants,
			@Autowired SessionFactory sessionFactory) {
		super(messages, constants, sessionFactory);
		this.structureRepository = structureRepository;
		this.organizationRepository = organizationRepository;
	}

	/** Replies the associated structure with the given identifier.
	 *
	 * @param id the identifier of the expected structure.
	 * @return the associated structure, or {@code null} if there is no associated structure with the given id.
	 * @Deprecated no replacement.
	 */
	@Deprecated(since = "4.0", forRemoval = true)
	public AssociatedStructure getAssociatedStructureById(int id) {
		final var structureOpt = this.structureRepository.findById(Long.valueOf(id));
		if (structureOpt.isPresent()) {
			return structureOpt.get();
		}
		return null;
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
	 * @Deprecated no replacement.
	 */
	@Deprecated(since = "4.0", forRemoval = true)
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
	 * @Deprecated no replacement.
	 */
	@Deprecated(since = "4.0", forRemoval = true)
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
	 * @Deprecated no replacement.
	 */
	@Deprecated(since = "4.0", forRemoval = true)
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
	 * @Deprecated no replacement.
	 */
	@Deprecated(since = "4.0", forRemoval = true)
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
	 * @Deprecated no replacement.
	 */
	@Deprecated(since = "4.0", forRemoval = true)
	public List<AssociatedStructure> getAllAssociatedStructures() {
		return this.structureRepository.findAll();
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
	 * @Deprecated no replacement.
	 */
	@Deprecated(since = "4.0", forRemoval = true)
	public List<AssociatedStructure> getAssociatedStructuresByPersonId(long id) {
		final var idObj = Long.valueOf(id);
		return this.structureRepository.findDistinctPersonAssociatedStructures(Boolean.FALSE, idObj);
	}

	/** Replies if the given identifier is for a person who is involved in an associated structure, whatever her/his role.
	 *
	 * @param id the identifier of the person.
	 * @return {@code true} if the person is involved in an associated structure.
	 * @since 3.6
	 * @Deprecated no replacement.
	 */
	@Deprecated(since = "4.0", forRemoval = true)
	public boolean isInvolved(long id) {
		return !getAssociatedStructuresByPersonId(id).isEmpty();
	}

	@Override
	public EntityEditingContext<AssociatedStructure> startEditing(AssociatedStructure structure) {
		assert structure != null;
		// Force loading of the entities that may be edited at the same time as the rest of the structure properties
		inSession(session -> {
			if (structure.getId() != 0l) {
				session.load(structure, Long.valueOf(structure.getId()));
				Hibernate.initialize(structure.getFundingOrganization());
				Hibernate.initialize(structure.getHoldersRaw());
				for (final var holder : structure.getHoldersRaw()) {
					Hibernate.initialize(holder.getPerson());
					Hibernate.initialize(holder.getOrganization());
					Hibernate.initialize(holder.getSuperOrganization());
				}
				Hibernate.initialize(structure.getProjects());
				for (final var project : structure.getProjects()) {
					Hibernate.initialize(project.getCoordinator());
					Hibernate.initialize(project.getSuperOrganization());
				}
			}
		});
		return new EditingContext(structure);
	}

	@Override
	public EntityDeletingContext<AssociatedStructure> startDeletion(Set<AssociatedStructure> structures) {
		assert structures != null && !structures.isEmpty();
		return new DeletingContext(structures);
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
	protected class EditingContext extends AbstractEntityEditingContext<AssociatedStructure> {

		private static final long serialVersionUID = -7800294119864189541L;

		/** Constructor.
		 *
		 * @param structure the edited associated structure.
		 */
		protected EditingContext(AssociatedStructure structure) {
			super(structure);
		}

		@Override
		public void save(HasAsynchronousUploadService... components) throws IOException {
			this.entity = AssociatedStructureService.this.structureRepository.save(this.entity);
		}

		@Override
		public EntityDeletingContext<AssociatedStructure> createDeletionContext() {
			return AssociatedStructureService.this.startDeletion(Collections.singleton(this.entity));
		}

	}

	/** Context for deleting a {@link AssociatedStructure}.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected class DeletingContext extends AbstractEntityDeletingContext<AssociatedStructure> {

		private static final long serialVersionUID = -6135693755795081567L;

		/** Constructor.
		 *
		 * @param structures the associated structures to delete.
		 */
		protected DeletingContext(Set<AssociatedStructure> structures) {
			super(structures);
		}

		@Override
		protected void deleteEntities(Collection<Long> identifiers) throws Exception {
			AssociatedStructureService.this.structureRepository.deleteAllById(identifiers);
		}

	}

}
