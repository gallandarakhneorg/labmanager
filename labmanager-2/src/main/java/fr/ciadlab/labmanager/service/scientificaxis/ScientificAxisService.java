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

package fr.ciadlab.labmanager.service.scientificaxis;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import fr.ciadlab.labmanager.repository.member.MembershipRepository;
import fr.ciadlab.labmanager.repository.project.ProjectRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.repository.scientificaxis.ScientificAxisRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for scientific axes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.5
 */
@Service
public class ScientificAxisService extends AbstractService {

	private final ScientificAxisRepository scientificAxisRepository;

	private final PublicationRepository publicationRepository;

	private final MembershipRepository membershipRepository;

	private final ProjectRepository projectRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param scientificAxisRepository the repository for accessing the scientific axes.
	 * @param publicationRepository the repository for accessing the publications.
	 * @param membershipRepository the repository for accessing the memberships.
	 * @param projectRepository the repository for accessing the projects.
	 */
	public ScientificAxisService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ScientificAxisRepository scientificAxisRepository,
			@Autowired PublicationRepository publicationRepository,
			@Autowired MembershipRepository membershipRepository,
			@Autowired ProjectRepository projectRepository) {
		super(messages, constants);
		this.scientificAxisRepository = scientificAxisRepository;
		this.publicationRepository = publicationRepository;
		this.membershipRepository = membershipRepository;
		this.projectRepository = projectRepository;
	}

	/** Replies the list of all the scientific axes.
	 *
	 * @return the list of all the scientific axes.
	 */
	public List<ScientificAxis> getAllScientificAxes() {
		return this.scientificAxisRepository.findAll();
	}

	/** Replies the scientific axis that has the given identifier.
	 *
	 * @param id the identifier of the axis.
	 * @return the axis with the gien identifier, or {@code null} if there is no axis with this id.
	 */
	public ScientificAxis getScientificAxisById(int id) {
		final Optional<ScientificAxis> axis = this.scientificAxisRepository.findById(Integer.valueOf(id));
		if (axis.isPresent()) {
			return axis.get();
		}
		return null;
	}

	/** Delete the scientific axis with the given identifier.
	 *
	 * @param identifier the identifier of the axis to be deleted.
	 */
	public void removeScientificAxis(int identifier) {
		final Integer id = Integer.valueOf(identifier);
		final Optional<ScientificAxis> axisOpt = this.scientificAxisRepository.findById(id);
		if (axisOpt.isPresent()) {
			final ScientificAxis axis = axisOpt.get();
			//
			axis.setProjects(null);
			this.scientificAxisRepository.deleteById(id);
		}
	}

	/** Create a scientific axis.
	 *
	 * @param validated indicates if the axis is validated by a local authority.
	 * @param acronym the short name of acronym of the scientific axis.
	 * @param name the name of the scientific axis.
	 * @param startDate the start date of the scientific axis in format {@code YYY-MM-DD}.
	 * @param endDate the start date of the scientific axis in format {@code YYY-MM-DD}, or {@code null} if none.
	 * @param projects the list of associated projects.
	 * @param publications the list of associated publications.
	 * @param memberships the list of associated memberships.
	 * @return the reference to the created axis.
	 */
	public Optional<ScientificAxis> createScientificAxis(boolean validated, String acronym, String name,
			LocalDate startDate, LocalDate endDate, List<Project> projects, List<Publication> publications,
			List<Membership> memberships) {
		final ScientificAxis axis = new ScientificAxis();
		try {
			updateScientificAxis(axis, validated, acronym, name, startDate, endDate, projects, publications, memberships);
		} catch (Throwable ex) {
			// Delete created axis
			if (axis.getId() != 0) {
				try {
					removeScientificAxis(axis.getId());
				} catch (Throwable ex0) {
					// Silent
				}
			}
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
		return Optional.of(axis);
	}

	/** Update a scientific axis.
	 *
	 * @param axisId the identifier of the scientific axis to be updated.
	 * @param validated indicates if the axis is validated by a local authority.
	 * @param acronym the short name of acronym of the scientific axis.
	 * @param name the name of the scientific axis.
	 * @param startDate the start date of the scientific axis in format {@code YYY-MM-DD}.
	 * @param endDate the start date of the scientific axis in format {@code YYY-MM-DD}, or {@code null} if none.
	 * @param projects the list of associated projects.
	 * @param publications the list of associated publications.
	 * @param memberships the list of associated memberships.
	 * @return the reference to the created axis.
	 */
	public Optional<ScientificAxis> updateScientificAxis(int axisId, boolean validated, String acronym,
			String name, LocalDate startDate, LocalDate endDate, List<Project> projects,
			List<Publication> publications, List<Membership> memberships) {
		final Optional<ScientificAxis> res;
		if (axisId >= 0) {
			res = this.scientificAxisRepository.findById(Integer.valueOf(axisId));
		} else {
			res = Optional.empty();
		}
		if (res.isPresent()) {
			updateScientificAxis(res.get(), validated, acronym, name, startDate, endDate, projects, publications, memberships);
		}
		return res;
	}

	/** Update a scientific axis.
	 *
	 * @param axis the scientific axis to be updated.
	 * @param validated indicates if the axis is validated by a local authority.
	 * @param acronym the short name of acronym of the scientific axis.
	 * @param name the name of the scientific axis.
	 * @param startDate the start date of the scientific axis in format {@code YYY-MM-DD}.
	 * @param endDate the start date of the scientific axis in format {@code YYY-MM-DD}, or {@code null} if none.
	 * @param projects the list of associated projects.
	 * @param publications the list of associated publications.
	 * @param memberships the list of associated memberships.
	 */
	protected void updateScientificAxis(ScientificAxis axis, boolean validated, String acronym,
			String name, LocalDate startDate, LocalDate endDate, List<Project> projects,
			List<Publication> publications, List<Membership> memberships) {
		axis.setValidated(validated);
		axis.setAcronym(acronym);
		axis.setName(name);
		axis.setStartDate(startDate);
		axis.setEndDate(endDate);
		this.scientificAxisRepository.save(axis);

		axis.setProjects(projects);
		this.scientificAxisRepository.save(axis);
		
		updateMemberships(axis, memberships);
		updatePublications(axis, publications);
		updateProjects(axis, projects);
	}

	private void updateMemberships(ScientificAxis axis, List<Membership> memberships) {
		axis.setMemberships(memberships);
		for (final Membership mbr : memberships) {
			mbr.getScientificAxes().add(axis);
		}
		this.membershipRepository.saveAll(memberships);
	}

	private void updatePublications(ScientificAxis axis, List<Publication> publications) {
		axis.setPublications(publications);
		for (final Publication pub : publications) {
			pub.getScientificAxes().add(axis);
		}
		this.publicationRepository.saveAll(publications);
	}
	
	private void updateProjects(ScientificAxis axis, List<Project> projects) {
		axis.setProjects(projects);
		for (final Project prj : projects) {
			prj.getScientificAxes().add(axis);
		}
		this.projectRepository.saveAll(projects);
	}

	/** Replies the scientific axes that have the given identifiers.
	 *
	 * @param identifiers the identifiers.
	 * @return the axes.
	 */
	public List<ScientificAxis> getScientificAxesFor(List<Integer> identifiers) {
		final List<ScientificAxis> axes = this.scientificAxisRepository.findAllById(identifiers);
		if (axes.size() != identifiers.size()) {
			throw new IllegalArgumentException("Scientific axis not found"); //$NON-NLS-1$
		}
		return axes;
	}

}
