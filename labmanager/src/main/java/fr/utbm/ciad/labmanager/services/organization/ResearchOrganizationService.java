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
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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

	private DownloadableFileManager fileManager;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param addressRepository the address repository.
	 * @param organizationRepository the organization repository.
	 * @param fileManager the manager of the uploaded and downloadable files.
	 */
	public ResearchOrganizationService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired OrganizationAddressRepository addressRepository,
			@Autowired ResearchOrganizationRepository organizationRepository,
			@Autowired DownloadableFileManager fileManager) {
		super(messages, constants);
		this.addressRepository = addressRepository;
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
	public Optional<ResearchOrganization> getResearchOrganizationById(int identifier) {
		return this.organizationRepository.findById(Integer.valueOf(identifier));
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
	 * @param superOrganization the identifier of the super organization, or {@code null} or {@code 0} if none.
	 * @param pathToLogo the uploaded logo of the organization, if any.
	 * @return the created organization in the database.
	 * @throws IOException if the logo cannot be uploaded or removed.
	 */
	public Optional<ResearchOrganization> createResearchOrganization(boolean validated, String acronym, String name,
			boolean isMajor,
			String rnsr, String nationalIdentifier, String description,
			ResearchOrganizationType type, String organizationURL, CountryCode country, List<Integer> addresses, Integer superOrganization,
			MultipartFile pathToLogo) throws IOException {
		final Optional<ResearchOrganization> sres;
		if (superOrganization != null && superOrganization.intValue() != 0) {
			sres = this.organizationRepository.findById(superOrganization);
			if (sres.isEmpty()) {
				throw new IllegalArgumentException("Research organization not found with id: " + superOrganization); //$NON-NLS-1$
			}
		} else {
			sres = Optional.empty();
		}
		//
		final Set<OrganizationAddress> adrs;
		if (addresses != null && !addresses.isEmpty()) {
			adrs = this.addressRepository.findAllByIdIn(addresses);
		} else {
			adrs = null;
		}
		//
		final ResearchOrganization res = new ResearchOrganization();
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
		if (sres.isPresent()) {
			res.setSuperOrganization(sres.get());
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
	public void removeResearchOrganization(int identifier) throws AttachedSubOrganizationException, AttachedMemberException {
		final Integer id = Integer.valueOf(identifier);
		final Optional<ResearchOrganization> res = this.organizationRepository.findById(id);
		if (res.isPresent()) {
			final ResearchOrganization organization = res.get();
			if (!organization.getSubOrganizations().isEmpty()) {
				throw new AttachedSubOrganizationException();
			}
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
	 * @param superOrganization the identifier of the super organization, or {@code null} or {@code 0} if none.
	 * @param pathToLogo the uploaded logo of the organization, if any.
	 * @param removePathToLogo indicates if the path to the logo in the database should be removed, possibly before saving a new logo.
	 * @return the organization object that was updated.
	 * @throws IOException if the logo cannot be uploaded or removed.
	 */
	public Optional<ResearchOrganization> updateResearchOrganization(int identifier, boolean validated, String acronym, String name,
			boolean isMajor,
			String rnsr, String nationalIdentifier, String description,
			ResearchOrganizationType type, String organizationURL, CountryCode country, List<Integer> addresses, Integer superOrganization,
			MultipartFile pathToLogo, boolean removePathToLogo) throws IOException  {
		final Optional<ResearchOrganization> res = this.organizationRepository.findById(Integer.valueOf(identifier));
		if (res.isPresent()) {
			final Optional<ResearchOrganization> sres;
			if (superOrganization != null && superOrganization.intValue() != 0) {
				sres = this.organizationRepository.findById(superOrganization);
				if (sres.isEmpty()) {
					throw new IllegalArgumentException("Research organization not found with id: " + superOrganization); //$NON-NLS-1$
				}
			} else {
				sres = Optional.empty();
			}
			//
			final Set<OrganizationAddress> adrs;
			if (addresses != null && !addresses.isEmpty()) {
				adrs = this.addressRepository.findAllByIdIn(addresses);
			} else {
				adrs = null;
			}
			//
			final ResearchOrganization organization = res.get();
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
			if (sres.isPresent()) {
				organization.setSuperOrganization(sres.get());
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
			final String filename = organization.getPathToLogo();
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
	public boolean linkSubOrganization(int superIdentifier, int subIdentifier) {
		if (superIdentifier != subIdentifier) {
			final Optional<ResearchOrganization> superOrg = this.organizationRepository.findById(
					Integer.valueOf(superIdentifier));
			if (superOrg.isPresent()) {
				final Optional<ResearchOrganization> subOrg = this.organizationRepository.findById(
						Integer.valueOf(subIdentifier));
				if (subOrg.isPresent()) {
					final ResearchOrganization superOrganization = superOrg.get();
					final ResearchOrganization subOrganization = subOrg.get();
					if (superOrganization.getSubOrganizations().add(subOrganization)) {
						subOrganization.setSuperOrganization(superOrganization);
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
	public boolean unlinkSubOrganization(int superIdentifier, int subIdentifier) {
		final Optional<ResearchOrganization> superOrg = this.organizationRepository.findById(
				Integer.valueOf(superIdentifier));
		if (superOrg.isPresent()) {
			final Optional<ResearchOrganization> subOrg = this.organizationRepository.findById(
					Integer.valueOf(subIdentifier));
			if (subOrg.isPresent()) {
				final ResearchOrganization superOrganization = superOrg.get();
				final ResearchOrganization subOrganization = subOrg.get();
				if (superOrganization.getSubOrganizations().remove(subOrganization)) {
					subOrganization.setSuperOrganization(null);
					this.organizationRepository.save(superOrganization);
					this.organizationRepository.save(subOrganization);
					return true;
				}
			}
		}
		return false;
	}

}
