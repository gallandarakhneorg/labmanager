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

package fr.utbm.ciad.labmanager.services.journal;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.data.journal.JournalQualityAnnualIndicators;
import fr.utbm.ciad.labmanager.data.journal.JournalQualityAnnualIndicatorsRepository;
import fr.utbm.ciad.labmanager.data.journal.JournalRepository;
import fr.utbm.ciad.labmanager.data.publication.type.JournalPaperRepository;
import fr.utbm.ciad.labmanager.services.AbstractService;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import fr.utbm.ciad.labmanager.utils.io.network.NetConnection;
import fr.utbm.ciad.labmanager.utils.io.scimago.ScimagoPlatform;
import fr.utbm.ciad.labmanager.utils.io.wos.WebOfSciencePlatform;
import fr.utbm.ciad.labmanager.utils.ranking.QuartileRanking;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Service related to the journals.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class JournalService extends AbstractService {

	private static final String NOT_RANKED_STR = "--"; //$NON-NLS-1$
	
	private final JournalRepository journalRepository;

	private final JournalQualityAnnualIndicatorsRepository indicatorRepository;

	private final JournalPaperRepository publicationRepository;

	private final NetConnection netConnection;

	private final ScimagoPlatform scimago;

	private final WebOfSciencePlatform wos;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param journalRepository the journal repository.
	 * @param indicatorRepository the repository for journal indicators.
	 * @param publicationRepository the publication repository.
	 * @param scimago the reference to the tool for accessing to the Scimago platform.
	 * @param wos the reference to the tool for accessing to the Web-of-Science platform.
	 * @param netConnection the tools for accessing the network.
	 */
	public JournalService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired JournalRepository journalRepository,
			@Autowired JournalQualityAnnualIndicatorsRepository indicatorRepository,
			@Autowired JournalPaperRepository publicationRepository,
			@Autowired ScimagoPlatform scimago,
			@Autowired WebOfSciencePlatform wos,
			@Autowired NetConnection netConnection) {
		super(messages, constants);
		this.journalRepository = journalRepository;
		this.indicatorRepository = indicatorRepository;
		this.publicationRepository = publicationRepository;
		this.scimago = scimago;
		this.wos = wos;
		this.netConnection = netConnection;
	}

	/** Replies all the journals for the database.
	 *
	 * @return the list of journals.
	 */
	public List<Journal> getAllJournals() {
		return this.journalRepository.findAll();
	}

	/** Replies all the journals for the database.
	 *
	 * @param filter the filter of journals.
	 * @return the list of journals.
	 * @since 4.0
	 */
	public List<Journal> getAllJournals(Specification<Journal> filter) {
		return this.journalRepository.findAll(filter);
	}

	/** Replies all the journals for the database.
	 *
	 * @param filter the filter of journals.
	 * @param sortOrder the order specification to use for sorting the journals.
	 * @return the list of journals.
	 * @since 4.0
	 */
	public List<Journal> getAllJournals(Specification<Journal> filter, Sort sortOrder) {
		return this.journalRepository.findAll(filter, sortOrder);
	}

	/** Replies all the journals for the database.
	 *
	 * @param sortOrder the order specification to use for sorting the journals.
	 * @return the list of journals.
	 * @since 4.0
	 */
	public List<Journal> getAllJournals(Sort sortOrder) {
		return this.journalRepository.findAll(sortOrder);
	}

	/** Replies all the journals for the database.
	 *
	 * @param pageable the manager of pages.
	 * @return the list of journals.
	 * @since 4.0
	 */
	public Page<Journal> getAllJournals(Pageable pageable) {
		return this.journalRepository.findAll(pageable);
	}

	/** Replies all the journals for the database.
	 *
	 * @param pageable the manager of pages.
	 * @param filter the filter of journals.
	 * @return the list of journals.
	 * @since 4.0
	 */
	public Page<Journal> getAllJournals(Pageable pageable, Specification<Journal> filter) {
		return this.journalRepository.findAll(filter, pageable);
	}

	/** Replies the journal with the given identifier.
	 *
	 * @param identifier the identifier to search for.
	 * @return the journal, or {@code null} if none is defined.
	 */
	public Journal getJournalById(int identifier) {
		final var res = this.journalRepository.findById(Long.valueOf(identifier));
		if (res.isPresent()) {
			return res.get();
		}
		return null;
	}

	/** Replies a journal with the given name. If multiple journals have the same name, one is replied (usually the first replied by the iterator).
	 *
	 * @param name the name to search for.
	 * @return the journal, or {@code null} if none is defined.
	 */
	public Journal getJournalByName(String name) {
		final var res = getJournalsByName(name);
		if (res.isEmpty()) {
			return null;
		}
		return res.iterator().next();
	}

	/** Replies the journals with the given name.
	 *
	 * @param name the name to search for.
	 * @return the journals.
	 */
	public Set<Journal> getJournalsByName(String name) {
		return this.journalRepository.findDistinctByJournalName(name);
	}

	/** Replies the identifier of a journal with the given name. If multiple journals have the same name, one is selected (usually the first replied by the iterator).
	 *
	 * @param name the name to search for.
	 * @return the journal identifier, or {@code 0} if none is defined.
	 */
	public long getJournalIdByName(String name) {
		final var res = this.journalRepository.findDistinctByJournalName(name);
		if (!res.isEmpty()) {
			final var journal = res.iterator().next();
			if (journal != null) {
				return journal.getId();
			}
		}
		return 0;
	}

	/** Create a journal into the database.
	 *
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param name the name of the journal.
	 * @param address the address of the publisher of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param journalUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	 * @param scimagoCategory the name of the scientific category on Scimago for obtaining Q-index.
	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	 * @param wosCategory the name of the scientific category on WoS for obtaining Q-index.
	 * @return the created journal.
	 */
	public Journal createJournal(boolean validated, String name, String address, String publisher, String isbn, String issn,
			Boolean openAccess, String journalUrl, String scimagoId, String scimagoCategory, String wosId, String wosCategory) {
		final var res = new Journal();
		res.setJournalName(name);
		res.setAddress(address);
		res.setPublisher(publisher);
		res.setISBN(isbn);
		res.setISSN(issn);
		res.setOpenAccess(openAccess);
		res.setJournalURL(journalUrl);
		res.setScimagoId(scimagoId);
		res.setScimagoCategory(scimagoCategory);
		res.setWosId(wosId);
		res.setWosCategory(wosCategory);
		res.setValidated(validated);
		this.journalRepository.save(res);
		return res;
	}

	/** Remove the journal with the given identifier.
	 * A journal cannot be removed if it has attached papers.
	 *
	 * @param identifier the identifier of the journal to remove.
	 * @throws AttachedJournalPaperException when the journal has attached papers.
	 */
	@Transactional
	public void removeJournal(long identifier) throws AttachedJournalPaperException {
		final var id = Long.valueOf(identifier);
		final var journalRef = this.journalRepository.findById(id);
		if (journalRef.isPresent()) {
			final var journal = journalRef.get();
			if (journal.hasPublishedPaper()) {
				throw new AttachedJournalPaperException();
			}
			this.journalRepository.deleteById(id);
		}
	}

	/** Update the information of a journal.
	 *
	 * @param identifier the identifier of the journal for which information must be updated.
	 * @param validated indicates if the journal is validated by a local authority.
	 * @param name the name of the journal.
	 * @param address the address of the publisher of the journal.
	 * @param publisher the name of the publisher of the journal.
	 * @param isbn the ISBN number for the journal.
	 * @param issn the ISSN number for the journal.
	 * @param openAccess indicates if the journal is open access or not.
	 * @param journalUrl the URL to the page of the journal on the publisher website.
	 * @param scimagoId the identifier to the page of the journal on the Scimago website.
	 * @param scimagoCategory the name of the scientific category on Scimago for obtaining Q-index.
	 * @param wosId the identifier to the page of the journal on the Web-Of-Science website.
	 * @param wosCategory the name of the scientific category on WoS for obtaining Q-index.
	 * @return the updated journal.
	 */
	public Journal updateJournal(long identifier, boolean validated, String name, String address, String publisher, String isbn, String issn,
			Boolean openAccess, String journalUrl, String scimagoId, String scimagoCategory, String wosId, String wosCategory) {
		final var res = this.journalRepository.findById(Long.valueOf(identifier));
		if (res.isPresent()) {
			final var journal = res.get();
			if (journal != null) {
				if (!Strings.isNullOrEmpty(name)) {
					journal.setJournalName(name);
				}
				journal.setJournalName(Strings.emptyToNull(name));
				journal.setAddress(Strings.emptyToNull(address));
				journal.setPublisher(Strings.emptyToNull(publisher));
				journal.setISBN(Strings.emptyToNull(isbn));
				journal.setISSN(Strings.emptyToNull(issn));
				journal.setOpenAccess(openAccess);
				journal.setJournalURL(Strings.emptyToNull(journalUrl));
				journal.setScimagoId(Strings.emptyToNull(scimagoId));
				journal.setScimagoCategory(Strings.emptyToNull(scimagoCategory));
				journal.setWosId(Strings.emptyToNull(wosId));
				journal.setWosCategory(Strings.emptyToNull(wosCategory));
				journal.setValidated(validated);
				this.journalRepository.save(journal);
				return journal; 
			}
		}
		return null;
	}

	/** Create a link between a journal and a paper.
	 * If the paper is linked to another journal, this previous link is lost and the new link is set up.
	 *
	 * @param journalId the identifier of the journal.
	 * @param paperId the identifier of the paper.
	 * @return {@code true} if the link is created; {@code false} if the link cannot be created.
	 */
	public boolean linkPaper(long journalId, long paperId) {
		final var idPaper = Long.valueOf(paperId);
		final var optPaper = this.publicationRepository.findById(idPaper);
		if (optPaper.isPresent()) {
			final var optJournal = this.journalRepository.findById(Long.valueOf(journalId));
			if (optJournal.isPresent()) {
				final var paper = optPaper.get();
				final var journal = optJournal.get();
				unlinkPaper(paperId, false);
				journal.getPublishedPapers().add(paper);
				paper.setJournal(journal);
				this.journalRepository.save(journal);
				this.publicationRepository.save(paper);
				return true;
			}
		}
		return false;
	}

	/** Unlink the paper with the given identifier from its journal.
	 *
	 * @param paperId the identifier of the paper. 
	 * @param applySave indicates if the changes must be committed into the database by this function.
	 * @return {@code true} if the link is deleted; {@code false} if the link cannot be deleted.
	 */
	public boolean unlinkPaper(long paperId, boolean applySave) {
		final var optPaper = this.publicationRepository.findById(Long.valueOf(paperId));
		if (optPaper.isPresent()) {
			final var paper = optPaper.get();
			final var linkedJournal = paper.getJournal();
			if (linkedJournal != null) {
				if (linkedJournal.getPublishedPapers().remove(paper)) {
					paper.setJournal(null);
					if (applySave) {
						this.journalRepository.save(linkedJournal);
						this.publicationRepository.save(paper);
					}
					return true;
				}
			}
		}
		return false;
	}

	/** Unlink the paper with the given identifier from its journal.
	 *
	 * @param paperId the identifier of the paper. 
	 * @return {@code true} if the link is deleted; {@code false} if the link cannot be deleted.
	 */
	public boolean unlinkPaper(long paperId) {
		return unlinkPaper(paperId, true);
	}

	/** Download the quartile information from the Scimago website.
	 *
	 * @param journalId the identifier of the journal.
	 * @return the quartile or {@code null} if none cannot be found.
	 */
	public QuartileRanking downloadScimagoQuartileByJournalId(long journalId) {
		final var res = this.journalRepository.findById(Long.valueOf(journalId));
		if (res.isPresent()) {
			return downloadScimagoQuartileByJournal(res.get());
		}
		return null;
	}

	/** Replies the URL of the image that shows up the scimago quartile.
	 *
	 * @param journal the journal for which the quartile should be replied.
	 * @return the URL of the image.
	 */
	public URL getScimagoQuartileImageURLByJournal(Journal journal) {
		try {
			return this.scimago.getJournalPictureUrl(journal.getScimagoId());
		} catch (Throwable ex) {
			getLogger().warn(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	/** Replies the URL of journal on Scimago.
	 *
	 * @param id the identifier of the journal on Scimago.
	 * @return the URL of the journal on the Scimago website.
	 */
	public URL getScimagoURLByJournalId(String id) {
		try {
			return this.scimago.getJournalUrl(id);
		} catch (Throwable ex) {
			getLogger().warn(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

	/** Download the quartile information from the Scimago website.
	 *
	 * @param journal the journal for which the quartile must be downloaded.
	 * @return the quartile or {@code null} if none cannot be found.
	 */
	public QuartileRanking downloadScimagoQuartileByJournal(Journal journal) {
		if (journal != null) {
			final var scimagoId = journal.getScimagoId();
			if (!Strings.isNullOrEmpty(scimagoId)) {
				try {
					final var url = getScimagoQuartileImageURLByJournal(journal);
					if (url != null) {
						final var image = this.netConnection.getImageFromURL(url);
						final var rgba = image.getRGB(5, 55);
						final var red = (rgba >> 16) & 0xff;
						switch (red) {
						case 164:
							return QuartileRanking.Q1;
						case 232:
							return QuartileRanking.Q2;
						case 251:
							return QuartileRanking.Q3;
						case 221:
							return QuartileRanking.Q4;
						default:
							//
						}
					}
				} catch (Throwable ex) {
					getLogger().warn(ex.getLocalizedMessage(), ex);
				}
			}
		}
		return null;
	}

	/** Save the given quality indicators for the journal.
	 *
	 * @param journal the journal.
	 * @param year the reference year of the quality indicators.
	 * @param impactFactor the impact factor.
	 * @param scimagoQIndex the Scimago Q-Index.
	 * @param wosQIndex  the WoS Q-Index.
	 * @return the indicators.
	 */
	public JournalQualityAnnualIndicators setQualityIndicators(Journal journal, int year, float impactFactor,
			QuartileRanking scimagoQIndex, QuartileRanking wosQIndex) {
		final JournalQualityAnnualIndicators indicators = journal.setImpactFactorByYear(year, impactFactor);
		indicators.setScimagoQIndex(scimagoQIndex);
		indicators.setWosQIndex(wosQIndex);
		this.indicatorRepository.save(indicators);
		this.journalRepository.save(journal);
		return indicators;
	}

	/** Delete the quality indicators for the given journal and year.
	 *
	 * @param journal the journal.
	 * @param year the reference year.
	 */
	public void deleteQualityIndicators(Journal journal, int year) {
		final JournalQualityAnnualIndicators indicators = journal.getQualityIndicators().remove(Integer.valueOf(year));
		if (indicators != null) {
			this.indicatorRepository.delete(indicators);
			this.journalRepository.save(journal);
		}
	}

	/** Replies Json that describes an update of the journal indicators for the given reference year.
	 * This function read the given CSV stream as it is the CSV file provided by Web-of-Science.
	 * It also uses the {@link ScimagoPlatform} tool for downloading the CSV file from the Scimago
	 * website.
	 *
	 * @param referenceYear the reference year.
	 * @param wosCsv the CSV file from web-of-science, or {@code null} if none.
	 * @param progress the progression monitor.
	 * @return the JSON.
	 * @throws Exception if an error occurred when reading the CSV streams.
	 * @since 2.5
	 */
	public JsonNode getJournalIndicatorUpdates(int referenceYear, InputStream wosCsv, Progression progress) throws Exception {
		final var progress0 = progress == null ? new DefaultProgression() : progress; 
		final var journals = this.journalRepository.findAll();
		progress0.setProperties(0, 0, journals.size(), false);
		final var mapper = JsonUtils.createMapper();
		final var root = new ObjectNode(mapper.getNodeFactory());
		final var dataNode = root.putArray("data"); //$NON-NLS-1$
		for (final var journal : journals) {
			final var journalNode = dataNode.objectNode();
			final var lastScimagoQuartile = journal.getScimagoQIndexByYear(referenceYear);
			final var oldDataNode = journalNode.putObject("previous"); //$NON-NLS-1$
			oldDataNode.put("scimagoQindex", lastScimagoQuartile.name()); //$NON-NLS-1$
			if (!Strings.isNullOrEmpty(journal.getScimagoCategory())) {
				oldDataNode.put("scimagoCategory", journal.getScimagoCategory()); //$NON-NLS-1$
			}
			final var lastWosQuartile = journal.getWosQIndexByYear(referenceYear);
			oldDataNode.put("wosQindex", lastWosQuartile.name()); //$NON-NLS-1$
			if (!Strings.isNullOrEmpty(journal.getWosCategory())) {
				oldDataNode.put("wosCategory", journal.getWosCategory()); //$NON-NLS-1$
			}
			final var lastImpactFactor = journal.getImpactFactorByYear(referenceYear);
			if (lastImpactFactor > 0f) {
				oldDataNode.put("impactFactor", Float.toString(lastImpactFactor)); //$NON-NLS-1$
			}
			// Read updates from the Scimago provider
			if (!Strings.isNullOrEmpty(journal.getScimagoId())) {
				final var scimagoNode = journalNode.objectNode();
				final var rankings = this.scimago.getJournalRanking(referenceYear, journal.getScimagoId(), null);
				if (rankings != null) {
					QuartileRanking q = null;
					if (!Strings.isNullOrEmpty(journal.getScimagoCategory())) {
						q = rankings.get(journal.getScimagoCategory());
					}
					if (q == null) {
						final var categories = scimagoNode.putArray("categories"); //$NON-NLS-1$
						for (final var category : rankings.entrySet()) {
							if (!ScimagoPlatform.BEST.equals(category.getKey())) {
								final var categoryNode = categories.addObject();
								categoryNode.put("name", category.getKey()); //$NON-NLS-1$
								categoryNode.put("qindex", category.getValue().name()); //$NON-NLS-1$
							}
						}
					} else {
						scimagoNode.put("qindex", q.name()); //$NON-NLS-1$
					}
				} else {
					scimagoNode.put("qindex", QuartileRanking.NR.name()); //$NON-NLS-1$
				}
				if (!scimagoNode.isEmpty()) {
					journalNode.set("scimago", scimagoNode); //$NON-NLS-1$
				}
			}
			// Read updates from the Web-of-Science provider
			if (wosCsv != null && !Strings.isNullOrEmpty(journal.getISSN())) {
				final var wosNode = journalNode.objectNode();
				final var rankings = this.wos.getJournalRanking(referenceYear, wosCsv, journal.getISSN(), null);
				if (rankings != null) {
					if (rankings.impactFactor > 0f) {
						journalNode.put("impactFactor", rankings.impactFactor); //$NON-NLS-1$
					}
					QuartileRanking q = null;
					if (!Strings.isNullOrEmpty(journal.getWosCategory())) {
						q = rankings.quartiles.get(journal.getWosCategory());
					}
					if (q == null) {
						final var categories = wosNode.putArray("categories"); //$NON-NLS-1$
						for (final var category : rankings.quartiles.entrySet()) {
							final var categoryNode = categories.addObject();
							categoryNode.put("name", category.getKey()); //$NON-NLS-1$
							categoryNode.put("qindex", category.getValue().name()); //$NON-NLS-1$
						}
					} else {
						wosNode.put("qindex", q.name()); //$NON-NLS-1$
					}
				} else {
					wosNode.put("qindex", QuartileRanking.NR.name()); //$NON-NLS-1$
				}
				if (!wosNode.isEmpty()) {
					journalNode.set("wos", wosNode); //$NON-NLS-1$
				}
			}
			//
			if (!journalNode.isEmpty()) {
				journalNode.put("id", journal.getId()); //$NON-NLS-1$
				journalNode.put("name", journal.getJournalName()); //$NON-NLS-1$
				journalNode.put("publisher", journal.getPublisher()); //$NON-NLS-1$
				journalNode.put("issn", journal.getISSN()); //$NON-NLS-1$
				dataNode.add(journalNode);
			}
			progress0.increment();
		}
		return root;
	}

	/** Update the journal quality indicators according to the changes that are provided.
	 *
	 * @param referenceYear the reference year.
	 * @param changes the changes. Keys are the journal database id; and values are maps that map attribute names to
	 *     values. Attribute names are: {@code id}, {@code impactFactor}, {@code scimagoQindex}, {@code scimagoCategory},
	 *     {@code wosQindex}, {@code wosCategory}.
	 */
	public void updateJournalIndicators(int referenceYear, Map<String, Map<String, ?>> changes) {
		if (changes != null && !changes.isEmpty()) {
			for (final var journalUpdate : changes.values()) {
				final var id = ensureInt(journalUpdate, "id"); //$NON-NLS-1$
				final var impactFactor = optionalFloat(journalUpdate, "impactFactor"); //$NON-NLS-1$
				var scimagoCategory = optionalStringWithUnsetConstant(journalUpdate, "scimagoCategory"); //$NON-NLS-1$
				final QuartileRanking scimagoQindex;
				if (NOT_RANKED_STR.equals(scimagoCategory)) {
					scimagoCategory = null;
					scimagoQindex = QuartileRanking.NR;
				} else {
					scimagoQindex = optionalEnum(journalUpdate, "scimagoQindex", QuartileRanking.class); //$NON-NLS-1$
				}
				var wosCategory = optionalStringWithUnsetConstant(journalUpdate, "wosCategory"); //$NON-NLS-1$
				final QuartileRanking wosQindex;
				if (NOT_RANKED_STR.equals(wosCategory)) {
					wosCategory = null;
					wosQindex = QuartileRanking.NR;
				} else {
					wosQindex = optionalEnum(journalUpdate, "wosQindex", QuartileRanking.class); //$NON-NLS-1$
				}
				updateJournalIndicators(id, referenceYear, impactFactor, scimagoQindex, scimagoCategory, wosQindex, wosCategory);
			}
		}
	}

	/** Update the journal quality indicators according to the changes that are provided.
	 *
	 * @param id the identifier of the journal.
	 * @param referenceYear the reference year.
	 * @param scimagoQindex the Q-index that is provided by Scimago, or {@code null} if none.
	 * @param scimagoCategory the name of the scientific category that is used on Scimago for retrieving the Q-index.
	 * @param wosQindex the Q-index that is provided by WoS, or {@code null} if none.
	 * @param wosCategory the name of the scientific category that is used on WoS for retrieving the Q-index.
	 */
	protected void updateJournalIndicators(long id, int referenceYear, float impactFactor, QuartileRanking scimagoQindex,
			String scimagoCategory, QuartileRanking wosQindex, String wosCategory) {
		final var optJournal = this.journalRepository.findById(Long.valueOf(id));
		if (optJournal.isEmpty()) {
			throw new IllegalArgumentException("Journal with id " + id + " not found"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		//
		final var journal = optJournal.get();
		final var indicators = new HashSet<JournalQualityAnnualIndicators>();
		var changed = false;
		//
		if (!Float.isNaN(impactFactor) && impactFactor > 0f) {
			final var lastImpactFactor = journal.getImpactFactorByYear(referenceYear);
			if (Float.isNaN(lastImpactFactor) || lastImpactFactor <= 0f || (lastImpactFactor > 0f && lastImpactFactor != impactFactor)) {
				final var indicator = journal.setImpactFactorByYear(referenceYear, impactFactor);
				indicators.add(indicator);
			}
		}
		//
		if (!Objects.equals(scimagoCategory, journal.getScimagoCategory())) {
			journal.setScimagoCategory(scimagoCategory);
			changed = true;
		}
		if (!Objects.equals(wosCategory, journal.getWosCategory())) {
			journal.setWosCategory(wosCategory);
			changed = true;
		}
		//
		final var normalizedScimagoQindex = QuartileRanking.normalize(scimagoQindex);
		final var lastScimagoQindex = journal.getScimagoQIndexByYear(referenceYear);
		if (lastScimagoQindex != normalizedScimagoQindex) {
			final var indicator = journal.setScimagoQIndexByYear(referenceYear, normalizedScimagoQindex);
			indicators.add(indicator);
		}
		//
		final var normalizedWosQindex = QuartileRanking.normalize(wosQindex);
		final var lastWosQindex = journal.getWosQIndexByYear(referenceYear);
		if (lastWosQindex != normalizedWosQindex) {
			final var indicator = journal.setWosQIndexByYear(referenceYear, normalizedWosQindex);
			indicators.add(indicator);
		}
		//
		if (!indicators.isEmpty()) {
			for (final var indicator : indicators) {
				this.indicatorRepository.save(indicator);
			}
			this.journalRepository.save(journal);
		} else if (changed) {
			this.journalRepository.save(journal);
		}
	}

	/** Start the editing of the given journal.
	 *
	 * @param journal the journal to save.
	 * @return the editing context that enables to keep track of any information needed
	 *      for saving the journal and its related resources.
	 */
	public EditingContext startEditing(Journal journal) {
		assert journal != null;
		return new EditingContext(journal);
	}

	/** Context for editing a {@link Journal}.
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
		
		private static final long serialVersionUID = 2080175605368803970L;

		private Journal journal;

		/** Constructor.
		 *
		 * @param journal the edited journal.
		 */
		EditingContext(Journal journal) {
			this.journal = journal;
		}

		/** Replies the journal.
		 *
		 * @return the journal.
		 */
		public Journal getJournal() {
			return this.journal;
		}

		/** Save the journal in the JPA database.
		 *
		 * <p>After calling this function, it is preferable to not use
		 * the journal object that was provided before the saving.
		 * Invoke {@link #getJournal()} for obtaining the new journal
		 * instance, since the content of the saved object may have totally changed.
		 */
		@Transactional
		public void save() {
			this.journal = JournalService.this.journalRepository.save(this.journal);
		}

	}

}
