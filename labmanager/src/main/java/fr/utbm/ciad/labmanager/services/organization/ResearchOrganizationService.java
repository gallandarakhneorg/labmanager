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

package fr.utbm.ciad.labmanager.services.organization;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddress;
import fr.utbm.ciad.labmanager.data.organization.OrganizationAddressRepository;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationRepository;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganizationType;
import fr.utbm.ciad.labmanager.services.AbstractService;
import fr.utbm.ciad.labmanager.utils.HasAsynchronousUploadService;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import org.arakhne.afc.vmutil.FileSystem;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/** Service for research organizations.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class ResearchOrganizationService extends AbstractService {

	private final OrganizationAddressRepository addressRepository;

	private final ResearchOrganizationRepository organizationRepository;

	private final SessionFactory sessionFactory;
	
	private DownloadableFileManager fileManager;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param addressRepository the address repository.
	 * @param sessionFactory the Hibernate session factory.
	 * @param organizationRepository the organization repository.
	 * @param fileManager the manager of the uploaded and downloadable files.
	 */
	public ResearchOrganizationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired SessionFactory sessionFactory,
			@Autowired OrganizationAddressRepository addressRepository,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired DownloadableFileManager fileManager) {
		super(messages, constants);
		this.addressRepository = addressRepository;
		this.sessionFactory = sessionFactory;
		this.organizationRepository = organizationRepository;
		this.fileManager = fileManager;
	}

	/** Replies all the research organizations.
	 *
	 * @return the research organizations.
	 */
	public List<ResearchOrganization> getAllResearchOrganizations() {
		return this.organizationRepository.findAll();
	}

	/** Replies all the research organizations.
	 *
	 * @param filter the filter of organizations.
	 * @return the research organizations.
	 * @since 4.0
	 */
	public List<ResearchOrganization> getAllResearchOrganizations(Specification<ResearchOrganization> filter) {
		return this.organizationRepository.findAll(filter);
	}

	/** Replies all the research organizations.
	 *
	 * @param filter the filter of organizations.
	 * @param sortOrder the order specification to use for sorting the organizations.
	 * @return the research organizations.
	 * @since 4.0
	 */
	public List<ResearchOrganization> getAllResearchOrganizations(Specification<ResearchOrganization> filter, Sort sortOrder) {
		return this.organizationRepository.findAll(filter, sortOrder);
	}

	/** Replies all the research organizations.
	 *
	 * @param sortOrder the order specification to use for sorting the organizations.
	 * @return the research organizations.
	 * @since 4.0
	 */
	public List<ResearchOrganization> getAllResearchOrganizations(Sort sortOrder) {
		return this.organizationRepository.findAll(sortOrder);
	}

	/** Replies all the research organizations.
	 *
	 * @param pageable the manager of the pages.
	 * @return the research organizations.
	 * @since 4.0
	 */
	public Page<ResearchOrganization> getAllResearchOrganizations(Pageable pageable) {
		return this.organizationRepository.findAll(pageable);
	}

	/** Replies all the research organizations.
	 *
	 * @param pageable the manager of the pages.
	 * @param filter the filter of organizations.
	 * @return the research organizations.
	 * @since 4.0
	 */
	public Page<ResearchOrganization> getAllResearchOrganizations(Pageable pageable, Specification<ResearchOrganization> filter) {
		return this.organizationRepository.findAll(filter, pageable);
	}

	/** Replies the research organization with the given identifier.
	 *
	 * @param identifier the identifier to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationById(long identifier) {
		return this.organizationRepository.findById(Long.valueOf(identifier));
	}

	/** Replies the research organization with the given acronym.
	 *
	 * @param acronym the acronym to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationByAcronym(String acronym) {
		return this.organizationRepository.findDistinctByAcronym(acronym);
	}

	/** Replies the research organization with the given name.
	 *
	 * @param name the name to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationByName(String name) {
		return this.organizationRepository.findDistinctByName(name);
	}

	/** Replies the research organization with the given acronym or name.
	 *
	 * @param text the text to search for.
	 * @return the research organization.
	 */
	public Optional<ResearchOrganization> getResearchOrganizationByAcronymOrName(String text) {
		return this.organizationRepository.findDistinctByAcronymOrName(text, text);
	}

	/** Create a research organization.
	 *
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param acronym the new acronym for the research organization.
	 * @param name the new name for the research organization.
	 * @param isMajor indicates if the organization is a major organization.
	 * @param rnsr the number of the organization in the RNSR.
	 * @param nationalIdentifier the number of the organization for the national minitry of research.
	 * @param description the new description for the research organization.
	 * @param type the type of the research organization.
	 * @param organizationURL the web-site URL of the research organization.
	 * @param country the country of the research organization.
	 * @param addresses the identifiers of the addresses of the organization.
	 * @param superOrganizations the identifiers of the super organizations.
	 * @param pathToLogo the uploaded logo of the organization, if any.
	 * @return the created organization in the database.
	 * @throws IOException if the logo cannot be uploaded or removed.
	 */
	public Optional<ResearchOrganization> createResearchOrganization(boolean validated, String acronym, String name,
			boolean isMajor,
			String rnsr, String nationalIdentifier, String description,
			ResearchOrganizationType type, String organizationURL, CountryCode country, List<Long> addresses,
			Set<Long> superOrganizations,
			MultipartFile pathToLogo) throws IOException {
		final var sres = new HashSet<ResearchOrganization>();
		if (superOrganizations != null && !superOrganizations.isEmpty()) {
			for (final var sorgId : superOrganizations) {
				final var sorg = this.organizationRepository.findById(sorgId);
				if (sres.isEmpty()) {
					throw new IllegalArgumentException("Research organization not found with id: " + sorgId); //$NON-NLS-1$
				}
				sres.add(sorg.get());
			}
		}
		//
		final Set<OrganizationAddress> adrs;
		if (addresses != null && !addresses.isEmpty()) {
			adrs = this.addressRepository.findAllByIdIn(addresses);
		} else {
			adrs = null;
		}
		//
		final var res = new ResearchOrganization();
		res.setAcronym(Strings.emptyToNull(acronym));
		res.setName(Strings.emptyToNull(name));
		res.setMajorOrganization(isMajor);
		res.setRnsr(Strings.emptyToNull(rnsr));
		res.setNationalIdentifier(Strings.emptyToNull(nationalIdentifier));
		res.setDescription(Strings.emptyToNull(description));
		res.setType(type);
		res.setOrganizationURL(Strings.emptyToNull(organizationURL));
		res.setCountry(country);
		res.setValidated(validated);
		if (adrs != null && !adrs.isEmpty()) {
			res.setAddresses(adrs);
		}
		if (!sres.isEmpty()) {
			res.setSuperOrganizations(sres);
		}
		this.organizationRepository.save(res);
		//
		if (updateLogo(res, false, pathToLogo)) {
			this.organizationRepository.save(res);
		}
		return Optional.of(res);
	}

	/** Remove a research organization from the database.
	 * An research organization cannot be removed if it contains a suborganization.
	 *
	 * @param identifier the identifier of the organization to remove.
	 * @throws AttachedSubOrganizationException when the organization contains suborganizations.
	 * @throws AttachedMemberException when the organization contains members.
	 */
	@Transactional
	public void removeResearchOrganization(long identifier) throws AttachedSubOrganizationException, AttachedMemberException {
		final var id = Long.valueOf(identifier);
		final var res = this.organizationRepository.findById(id);
		if (res.isPresent()) {
			final var organization = res.get();
			if (!organization.getMemberships().isEmpty()) {
				throw new AttachedMemberException();
			}
			//
			this.organizationRepository.deleteById(id);
		}
	}

	/** Change the information associated to a research organization.
	 *
	 * @param identifier the identifier of the research organization to be updated.
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param acronym the new acronym for the research organization.
	 * @param name the new name for the research organization.
	 * @param isMajor indicates if the organization is a major organization.
	 * @param rnsr the number of the organization in the RNSR.
	 * @param nationalIdentifier the identifier of the organization for the national ministry of research.
	 * @param description the new description for the research organization.
	 * @param type the type of the research organization.
	 * @param organizationURL the web-site URL of the research organization.
	 * @param country the country of the research organization.
	 * @param addresses the identifiers of the addresses of the organization.
	 * @param superOrganizations the identifiers of the super organizations.
	 * @param pathToLogo the uploaded logo of the organization, if any.
	 * @param removePathToLogo indicates if the path to the logo in the database should be removed, possibly before saving a new logo.
	 * @return the organization object that was updated.
	 * @throws IOException if the logo cannot be uploaded or removed.
	 */
	public Optional<ResearchOrganization> updateResearchOrganization(long identifier, boolean validated, String acronym,
			String name, boolean isMajor, String rnsr, String nationalIdentifier, String description,
			ResearchOrganizationType type, String organizationURL, CountryCode country, List<Long> addresses,
			Set<Long> superOrganizations, MultipartFile pathToLogo, boolean removePathToLogo) throws IOException  {
		final var res = this.organizationRepository.findById(Long.valueOf(identifier));
		if (res.isPresent()) {
			final var sres = new HashSet<ResearchOrganization>();
			for (final var orgaId : superOrganizations) {
				final var sorg = this.organizationRepository.findById(orgaId);
				if (sorg.isEmpty()) {
					throw new IllegalArgumentException("Research organization not found with id: " + orgaId); //$NON-NLS-1$
				}
				sres.add(sorg.get());
			}
			//
			final Set<OrganizationAddress> adrs;
			if (addresses != null && !addresses.isEmpty()) {
				adrs = this.addressRepository.findAllByIdIn(addresses);
			} else {
				adrs = null;
			}
			//
			final var organization = res.get();
			if (!Strings.isNullOrEmpty(acronym)) {
				organization.setAcronym(acronym);
			}
			if (!Strings.isNullOrEmpty(name)) {
				organization.setName(name);
			}
			organization.setMajorOrganization(isMajor);
			organization.setRnsr(Strings.emptyToNull(rnsr));
			organization.setNationalIdentifier(Strings.emptyToNull(nationalIdentifier));
			organization.setDescription(Strings.emptyToNull(description));
			organization.setType(type);
			organization.setOrganizationURL(Strings.emptyToNull(organizationURL));
			organization.setCountry(country);
			organization.setAddresses(adrs);
			organization.setValidated(validated);
			if (!sres.isEmpty()) {
				organization.setSuperOrganizations(sres);
			}
			//
			this.organizationRepository.save(organization);
			//
			if (updateLogo(organization, removePathToLogo, pathToLogo)) {
				this.organizationRepository.save(organization);
			}
		}
		return res;
	}

	private boolean updateLogo(ResearchOrganization organization, boolean explicitRemove, MultipartFile uploadedFile) throws IOException {
		final String ext;
		if (uploadedFile != null) {
			ext = FileSystem.extension(uploadedFile.getOriginalFilename());
		} else {
			final var filename = organization.getPathToLogo();
			if (!Strings.isNullOrEmpty(filename)) {
				ext = FileSystem.extension(filename);
			} else {
				ext = null;
			}
		}
		return updateUploadedFile(explicitRemove, uploadedFile,
				"Organization logo uploaded at: ", //$NON-NLS-1$
				it -> organization.setPathToLogo(it),
				() -> this.fileManager.makeOrganizationLogoFilename(organization.getId(), ext),
				() -> this.fileManager.deleteOrganizationLogo(organization.getId(), ext),
				(fn, th) -> this.fileManager.saveImage(fn, uploadedFile));
	}

	/** Link a suborganization to a super organization.
	 * 
	 * @param superIdentifier the identifier of the super organization.
	 * @param subIdentifier the identifier of the suborganization.
	 * @return {@code true} if the link is created; {@code false} if the link cannot be created.
	 */
	public boolean linkSubOrganization(long superIdentifier, long subIdentifier) {
		if (superIdentifier != subIdentifier) {
			final var superOrg = this.organizationRepository.findById(Long.valueOf(superIdentifier));
			if (superOrg.isPresent()) {
				final var subOrg = this.organizationRepository.findById(Long.valueOf(subIdentifier));
				if (subOrg.isPresent()) {
					final var superOrganization = superOrg.get();
					final var subOrganization = subOrg.get();
					if (subOrganization.getSuperOrganizations().add(superOrganization)
						&& superOrganization.getSubOrganizations().add(subOrganization)) {
						this.organizationRepository.save(subOrganization);
						this.organizationRepository.save(superOrganization);
						return true;
					}
				}
			}
		}
		return false;
	}

	/** Unlink a suborganization from a super organization.
	 * 
	 * @param superIdentifier the identifier of the super organization.
	 * @param subIdentifier the identifier of the suborganization.
	 * @return {@code true} if the link is deleted; {@code false} if the link cannot be deleted.
	 */
	public boolean unlinkSubOrganization(long superIdentifier, long subIdentifier) {
		final var superOrg = this.organizationRepository.findById(Long.valueOf(superIdentifier));
		if (superOrg.isPresent()) {
			final var subOrg = this.organizationRepository.findById(Long.valueOf(subIdentifier));
			if (subOrg.isPresent()) {
				final var superOrganization = superOrg.get();
				final var subOrganization = subOrg.get();
				if (superOrganization.getSubOrganizations().remove(subOrganization)) {
					subOrganization.getSuperOrganizations().remove(superOrganization);
					this.organizationRepository.save(superOrganization);
					this.organizationRepository.save(subOrganization);
					return true;
				}
			}
		}
		return false;
	}

	/** Start the editing of the given organization.
	 *
	 * @param organization the organization to save.
	 * @return the editing context that enables to keep track of any information needed
	 *      for saving the organization and its related resources.
	 */
	public EditingContext startEditing(ResearchOrganization organization) {
		assert organization != null;
		// Force initialization of the internal properties that are needed for editing
		if (organization.getId() != 0l) {
			try (final var session = this.sessionFactory.openSession()) {
				session.load(organization, Long.valueOf(organization.getId()));
				Hibernate.initialize(organization.getAddresses());
			}
		}
		return new EditingContext(organization);
	}

	/** Context for editing a {@link ResearchOrganization}.
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
		
		private static final long serialVersionUID = 23311671802716077L;

		private String pathToLogo;
		
		private long id;

		private ResearchOrganization organization;

		/** Constructor.
		 *
		 * @param organization the edited organization.
		 */
		EditingContext(ResearchOrganization organization) {
			this.id = organization.getId();
			this.pathToLogo = organization.getPathToLogo();
			this.organization = organization;
		}

		/** Replies the organization.
		 *
		 * @return the organization.
		 */
		public ResearchOrganization getOrganization() {
			return this.organization;
		}

		/** Save the organization in the JPA database.
		 *
		 * <p>After calling this function, it is preferable to not use
		 * the organization object that was provided before the saving.
		 * Invoke {@link #getOrganization()} for obtaining the new organization
		 * instance, since the content of the saved object may have totally changed.
		 *
		 * @param components list of components to update if the service detects an inconsistent value.
		 * @throws IOException if files cannot be saved on the server.
		 */
		@Transactional
		public void save(HasAsynchronousUploadService... components) throws IOException {
			this.organization = ResearchOrganizationService.this.organizationRepository.save(this.organization);
			// Save the uploaded file if needed.
			if (this.id != this.organization.getId()) {
				// Special case where the field value does not corresponds to the correct path
				deleteLogo(this.id);
				for (final var component : components) {
					component.updateValue();
				}
				this.organization = ResearchOrganizationService.this.organizationRepository.save(this.organization);
			}
			if (Strings.isNullOrEmpty(this.organization.getPathToLogo())) {
				deleteLogo(this.organization.getId());
			} else {
				for (final var component : components) {
					component.saveUploadedFileOnServer();
				}
			}
			this.id = this.organization.getId();
			this.pathToLogo = this.organization.getPathToLogo();
		}

		private void deleteLogo(long id) {
			if (!Strings.isNullOrEmpty(this.pathToLogo)) {
				final var ext = FileSystem.extension(this.pathToLogo);
				if (!Strings.isNullOrEmpty(ext)) {
					ResearchOrganizationService.this.fileManager.deleteOrganizationLogo(id, ext);
				}
			}
		}

	}

}
