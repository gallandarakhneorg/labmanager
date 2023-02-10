/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.service.scientificaxis;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
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

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param scientificAxisRepository the repository for accessing the scientific axes.
	 */
	public ScientificAxisService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ScientificAxisRepository scientificAxisRepository) {
		super(messages, constants);
		this.scientificAxisRepository = scientificAxisRepository;
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
		axis.setPublications(publications);
		axis.setMemberships(memberships);
		this.scientificAxisRepository.save(axis);
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
