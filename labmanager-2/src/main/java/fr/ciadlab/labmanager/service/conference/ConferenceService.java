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

package fr.ciadlab.labmanager.service.conference;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.IdentifiableEntityComparator;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.conference.ConferenceQualityAnnualIndicators;
import fr.ciadlab.labmanager.io.core.CorePortal;
import fr.ciadlab.labmanager.io.core.CorePortal.CorePortalConference;
import fr.ciadlab.labmanager.repository.conference.ConferenceQualityAnnualIndicatorsRepository;
import fr.ciadlab.labmanager.repository.conference.ConferenceRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service related to the conferences.
 * 
 * @author $Author: sgalland$
 * @author $Author: anoubli$
 * @author $Author: bpdj$
 * @author $Author: pgoubet$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class ConferenceService extends AbstractService {

	private final ConferenceRepository conferenceRepository;

	private final ConferenceQualityAnnualIndicatorsRepository indicatorsRepository;

	private final CorePortal corePortal;

	private final SessionFactory sessionFactory;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param conferenceRepository the journal repository.
	 * @param indicatorsRepository the repository for accessing to the quality indicators.
	 * @param corePortal the accessor to the online CORE portal.
	 * @param sessionFactory the factory for hibernate session.
	 */
	public ConferenceService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ConferenceRepository conferenceRepository,
			@Autowired ConferenceQualityAnnualIndicatorsRepository indicatorsRepository,
			@Autowired CorePortal corePortal,
			@Autowired SessionFactory sessionFactory) {
		super(messages, constants);
		this.conferenceRepository = conferenceRepository;
		this.indicatorsRepository = indicatorsRepository;
		this.corePortal = corePortal;
		this.sessionFactory = sessionFactory;
	}

	/** Replies all the conferences for the database.
	 *
	 * @return the list of conferences.
	 */
	public List<Conference> getAllConferences() {
		return this.conferenceRepository.findAll();
	}

	/** Replies the conference with the given identifier.
	 *
	 * @param identifier the identifier of the conference.
	 * @return the conference or {@code null} if it has no conference with the given identifier.
	 */
	public Conference getConferenceById(int identifier) {
		final Optional<Conference> opt = this.conferenceRepository.findById(Integer.valueOf(identifier));
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	/** Create a conference.
	 *
	 * @param validated indicates if the conference is validated by a local authority.
	 * @param acronym the acronym of the conference.
	 * @param name the name of the conference.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param conferenceUrl the URL to the page of the conference.
	 * @param coreId the identifier of the conference on the CORE website.
	 * @return the updated conference.
	 */
	public Optional<Conference> createConference(boolean validated, String acronym, String name,
			String publisher, String isbn, String issn, Boolean openAccess, String conferenceUrl, String coreId) {
		final Conference conference = new Conference();
		try {
			updateConference(conference,
					validated, acronym, name, publisher, isbn, issn, openAccess, conferenceUrl, coreId);
		} catch (Throwable ex) {
			// Delete created conference
			if (conference.getId() != 0) {
				try {
					removeConference(conference.getId());
				} catch (Throwable ex0) {
					// Silent
				}
			}
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
		return Optional.of(conference);
	}

	/** Update the information of a conference.
	 *
	 * @param identifier the identifier of the conference for which information must be updated.
	 * @param validated indicates if the conference is validated by a local authority.
	 * @param acronym the acronym of the conference.
	 * @param name the name of the conference.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param conferenceUrl the URL to the page of the conference.
	 * @param coreId the identifier of the conference on the CORE website.
	 * @return the updated conference.
	 */
	public Optional<Conference> updateConference(int identifier, boolean validated, String acronym, String name,
			String publisher, String isbn, String issn, Boolean openAccess, String conferenceUrl, String coreId) {
		final Optional<Conference> res;
		if (identifier >= 0) {
			res = this.conferenceRepository.findById(Integer.valueOf(identifier));
		} else {
			res = Optional.empty();
		}
		if (res.isPresent()) {
			updateConference(res.get(),
					validated, acronym, name, publisher, isbn, issn, openAccess, conferenceUrl, coreId);
		}
		return res;
	}

	/** Update the information of a conference.
	 *
	 * @param identifier the identifier of the conference for which information must be updated.
	 * @param validated indicates if the conference is validated by a local authority.
	 * @param acronym the acronym of the conference.
	 * @param name the name of the conference.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param conferenceUrl the URL to the page of the conference.
	 * @param coreId the identifier of the conference on the CORE website.
	 */
	protected void updateConference(Conference conference, boolean validated, String acronym, String name,
			String publisher, String isbn, String issn, Boolean openAccess, String conferenceUrl, String coreId) {
		conference.setAcronym(acronym);
		conference.setConferenceURL(conferenceUrl);
		conference.setCoreId(coreId);
		conference.setISBN(isbn);
		conference.setISSN(issn);
		conference.setName(name);
		conference.setOpenAccess(openAccess);
		conference.setPublisher(publisher);
		conference.setValidated(validated);
		this.conferenceRepository.save(conference);
	}

	/** Delete the conference with the given identifier
	 * 
	 * @param identifier the identifier of the conference to be removed.
	 */
	public void removeConference(int identifier) {
		final Integer id = Integer.valueOf(identifier);
		final Optional<Conference> conferenceRef = this.conferenceRepository.findById(id);
		if (conferenceRef.isPresent()) {
			this.conferenceRepository.deleteById(id);
		}
	}

	/** Replies the conference with corresponds to the given name or acronym.
	 *
	 * @param conference the name or acronym of the conference.
	 * @return the conference or {@code null}.
	 */
	public Conference getConferenceByName(String conference) {
		if (!Strings.isNullOrEmpty(conference)) {
			final Optional<Conference> opt = this.conferenceRepository.findByAcronymOrName(conference);
			if (opt.isPresent()) {
				return opt.get();
			}
		}
		return null;
	}

	/** Replies the conferences with the given name.
	 *
	 * @param name the name to search for.
	 * @return the conferences.
	 */
	public Set<Conference> getConferencesByName(String name) {
		return this.conferenceRepository.findDistinctByAcronymOrName(name);
	}

	/** Save the given quality indicators for the conference.
	 *
	 * @param conference the conference.
	 * @param year the reference year of the quality indicators.
	 * @param coreIndex the CORE index.
	 * @return the indicators.
	 */
	public ConferenceQualityAnnualIndicators setQualityIndicators(Conference conference, int year, CoreRanking coreIndex) {
		final ConferenceQualityAnnualIndicators indicators = conference.setCoreIndexByYear(year, coreIndex);
		this.indicatorsRepository.save(indicators);
		this.conferenceRepository.save(conference);
		return indicators;
	}

	/** Delete the quality indicators for the given conference and year.
	 *
	 * @param conference the conference.
	 * @param year the reference year.
	 */
	public void deleteQualityIndicators(Conference conference, int year) {
		final ConferenceQualityAnnualIndicators indicators = conference.getQualityIndicators().remove(Integer.valueOf(year));
		if (indicators != null) {
			this.indicatorsRepository.delete(indicators);
			this.conferenceRepository.save(conference);
		}
	}

	/** Replies the URL of conference on CORE.
	 *
	 * @param id the identifier of the conference on CORE.
	 * @return the URL of the conference on the CORE portal website.
	 */
	public URL getCoreURLByConferenceId(String id) {
		try {
			return this.corePortal.getConferenceUrl(id);
		} catch (Throwable ex) {
			getLogger().warn(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	/** Compute the updates for the conference rankings.
	 *
	 * @param year the reference year.
	 * @param progression the progress indicator.
	 * @param callback the call back that is invoked on all the conferences with updates.
	 */
	public void computeConferenceRankingIndicatorUpdates(int year, DefaultProgression progression, BiConsumer<Conference, Map<String, Object>> callback) {
		final Set<Conference> treatedIdentifiers = new TreeSet<>(new IdentifiableEntityComparator());
		try (final Session session = this.sessionFactory.openSession()) {
			final List<Conference> conferences = this.conferenceRepository.findAll();
			final Progression progress = progression == null ? new DefaultProgression() : progression;
			progress.setProperties(0, 0, conferences.size(), false);
			for (final Conference conference: conferences) {
				if (treatedIdentifiers.add(conference)) {
					progress.setComment(getMessage("conferenceService.GetConferenceIndicatorUpdatesFor", conference.getNameOrAcronym())); //$NON-NLS-1$
					final Map<String, Object> newConferenceIndicators = new HashMap<>();
					readCorePortalIndicators(session, year, conference, newConferenceIndicators);
					if (!newConferenceIndicators.isEmpty()) {
						callback.accept(conference, newConferenceIndicators);
					}
				}
				progress.increment();
			}
			progress.end();
		}
	}

	private void readCorePortalIndicators(Session session, int year, Conference conference, Map<String, Object> newIndicators) {
		getLogger().info("Get CORE indicators for " + conference.getNameOrAcronym()); //$NON-NLS-1$
		session.beginTransaction();
		final String id = conference.getCoreId();
		if (!Strings.isNullOrEmpty(id)) {
			CorePortalConference indicators = null;
			try {
				indicators = this.corePortal.getConferenceRanking(year, id, null);
			} catch (Throwable ex) {
				getLogger().debug(ex.getLocalizedMessage(), ex);
				indicators = null;
			}
			if (indicators != null) {
				if (indicators.ranking != null) {
					newIndicators.put("coreRanking", indicators.ranking.name()); //$NON-NLS-1$
				}
			}
			final CoreRanking currentCore = conference.getCoreIndexByYear(year);
			newIndicators.put("currentCoreRanking", currentCore.name()); //$NON-NLS-1$
		}
		session.getTransaction().commit();
	}

	/** Save conference indicators. If a conference is not mentioned in the given map, its associated indicators will be not changed.
	 *
	 * @param year the reference year.
	 * @param changes the changes to apply.
	 */
	public void updateConferenceIndicators(int year, Map<Integer, CoreRanking> changes) {
		if (changes != null) {
			for (final Entry<Integer, CoreRanking> entry : changes.entrySet()) {
				final Optional<Conference> conference = this.conferenceRepository.findById(entry.getKey());
				if (conference.isEmpty()) {
					throw new IllegalArgumentException("Conference not found: " + entry.getKey()); //$NON-NLS-1$
				}
				final CoreRanking indicator = entry.getValue();
				if (indicator != null) {
					final Conference conf = conference.get();
					conf.setCoreIndexByYear(year, indicator);
					this.conferenceRepository.save(conf);
				}
			}
		}
	}

}
