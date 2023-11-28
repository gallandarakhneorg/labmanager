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

package fr.ciadlab.labmanager.service.publication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.member.Membership;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Production;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.scientificaxis.ScientificAxis;
import fr.ciadlab.labmanager.indicators.members.fte.PermanentResearcherFteIndicator;
import fr.ciadlab.labmanager.indicators.members.fte.PhdStudentFteIndicator;
import fr.ciadlab.labmanager.indicators.members.fte.PostdocFteIndicator;
import fr.ciadlab.labmanager.indicators.publication.count.ConferencePaperCountIndicator;
import fr.ciadlab.labmanager.indicators.publication.count.ScimagoJournalPaperCountIndicator;
import fr.ciadlab.labmanager.indicators.publication.count.UnrankedJournalPaperCountIndicator;
import fr.ciadlab.labmanager.indicators.publication.count.WosJournalPaperCountIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.ConferencePaperFteRatioIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.ConferencePaperPhdRatioIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.ConferencePaperPostdocRatioIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.ScimagoJournalPaperFteRatioIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.ScimagoJournalPaperPhdRatioIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.ScimagoJournalPaperPostdocRatioIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.WosJournalPaperFteRatioIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.WosJournalPaperPhdRatioIndicator;
import fr.ciadlab.labmanager.indicators.publication.fte.WosJournalPaperPostdocRatioIndicator;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.utils.country.CountryCode;
import fr.ciadlab.labmanager.utils.ranking.CoreRanking;
import fr.ciadlab.labmanager.utils.ranking.JournalRankingSystem;
import fr.ciadlab.labmanager.utils.ranking.QuartileRanking;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.util.MultiCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for computing stats on publications.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class PublicationStatService extends AbstractPublicationService {

	private static final String MESSAGE_PREFIX = "publicationStatService."; //$NON-NLS-1$

	private PermanentResearcherFteIndicator permanentResearcherCountIndicator;

	private PhdStudentFteIndicator phdStudentCountIndicator;

	private PostdocFteIndicator postdocCountIndicator;
	
	private ScimagoJournalPaperFteRatioIndicator scimagoPaperRateIndicator;
	
	private WosJournalPaperFteRatioIndicator wosRatePaperIndicator;

	private ScimagoJournalPaperCountIndicator scimagoJournalPaperCount;

	private UnrankedJournalPaperCountIndicator unrankedJournalPaperCount;
	
	private WosJournalPaperCountIndicator wosJournalPaperCount;

	private ConferencePaperCountIndicator conferencePaperCount;

	private ConferencePaperFteRatioIndicator conferenceRateIndicator;

	private ScimagoJournalPaperPhdRatioIndicator scimagoPhdPaperRateIndicator;
	
	private WosJournalPaperPhdRatioIndicator wosPhdPaperRateIndicator;

	private ConferencePaperPhdRatioIndicator conferencePhdRateIndicator;

	private ScimagoJournalPaperPostdocRatioIndicator scimagoPostdocPaperRateIndicator;
	
	private WosJournalPaperPostdocRatioIndicator wosPostdocPaperRateIndicator;

	private ConferencePaperPostdocRatioIndicator conferencePostdocRateIndicator;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param scimagoJournalPaperCount for computing the number of ACL per year with Scimago ranking system.
	 * @param wosJournalPaperCount for computing the number of ACL per year with WoS ranking system.
	 * @param unrankedJournalPaperCount for computing the number of papers in unranked journals.
	 * @param conferencePaperCount for computing the number of papers in conferences.
	 * @param permanentResearcherCountIndicator for computing the number of full-time researchers.
	 * @param phdStudentCountIndicator for computing the number of PhD students.
	 * @param postdocCountIndicator for computing the number of postdocs.
	 * @param scimagoRateIndicator for computing the publication ratios based on Scimago ranking system.
	 * @param wosRateIndicator for computing the publication ratios based on WoS ranking system.
	 * @param conferenceRateIndicator for computing the publication ratios for conference papers.
	 * @param scimagoPhdPaperRateIndicator for computing the publication ratios for PhD students for ranked journal papers in Scimago.
	 * @param wosPhdPaperRateIndicator for computing the publication ratios for PhD students for ranked journal papers in WoS.
	 * @param conferencePhdRateIndicator for computing the publication ratios for PhD students for conferences.
	 * @param scimagoPostdocPaperRateIndicator for computing the publication ratios for postdocs for ranked journal papers in Scimago.
	 * @param wosPostdocPaperRateIndicator for computing the publication ratios for postdocs for ranked journal papers in WoS.
	 * @param conferencePostdocRateIndicator for computing the publication ratios for postdocs for conferences.
	 */
	public PublicationStatService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ScimagoJournalPaperCountIndicator scimagoJournalPaperCount,
			@Autowired WosJournalPaperCountIndicator wosJournalPaperCount,
			@Autowired UnrankedJournalPaperCountIndicator unrankedJournalPaperCount,
			@Autowired ConferencePaperCountIndicator conferencePaperCount,
			@Autowired PermanentResearcherFteIndicator permanentResearcherCountIndicator,
			@Autowired PhdStudentFteIndicator phdStudentCountIndicator,
			@Autowired PostdocFteIndicator postdocCountIndicator,
			@Autowired ScimagoJournalPaperFteRatioIndicator scimagoRateIndicator,
			@Autowired WosJournalPaperFteRatioIndicator wosRateIndicator,
			@Autowired ConferencePaperFteRatioIndicator conferenceRateIndicator,
			@Autowired ScimagoJournalPaperPhdRatioIndicator scimagoPhdPaperRateIndicator,
			@Autowired WosJournalPaperPhdRatioIndicator wosPhdPaperRateIndicator,
			@Autowired ConferencePaperPhdRatioIndicator conferencePhdRateIndicator,
			@Autowired ScimagoJournalPaperPostdocRatioIndicator scimagoPostdocPaperRateIndicator,
			@Autowired WosJournalPaperPostdocRatioIndicator wosPostdocPaperRateIndicator,
			@Autowired ConferencePaperPostdocRatioIndicator conferencePostdocRateIndicator) {
		super(messages, constants);
		this.scimagoJournalPaperCount = scimagoJournalPaperCount;
		this.wosJournalPaperCount = wosJournalPaperCount;
		this.unrankedJournalPaperCount = unrankedJournalPaperCount;
		this.conferencePaperCount = conferencePaperCount;
		this.permanentResearcherCountIndicator = permanentResearcherCountIndicator;
		this.phdStudentCountIndicator = phdStudentCountIndicator;
		this.postdocCountIndicator = postdocCountIndicator;
		this.scimagoPaperRateIndicator = scimagoRateIndicator;
		this.wosRatePaperIndicator = wosRateIndicator;
		this.conferenceRateIndicator = conferenceRateIndicator;
		this.scimagoPhdPaperRateIndicator = scimagoPhdPaperRateIndicator;
		this.wosPhdPaperRateIndicator = wosPhdPaperRateIndicator;
		this.conferencePhdRateIndicator = conferencePhdRateIndicator;
		this.scimagoPostdocPaperRateIndicator = scimagoPostdocPaperRateIndicator;
		this.wosPostdocPaperRateIndicator = wosPostdocPaperRateIndicator;
		this.conferencePostdocRateIndicator = conferencePostdocRateIndicator;
	}

	/** Replies the string representation of the given quartile.
	 *
	 * @param ranking the quartile.
	 * @param showNotRanked indicates if the label for the not ranked indicator is replied. It it is
	 *      {@code false} the empty string is replied.
	 * @return the string representation of the given quartile.
	 */
	protected String toString(QuartileRanking ranking, boolean showNotRanked) {
		return ranking == null || ranking == QuartileRanking.NR
				? (showNotRanked ? getMessage(MESSAGE_PREFIX + "NotRanked") : "") //$NON-NLS-1$ //$NON-NLS-2$
				: ranking.name();
	}

	/** Replies the string representation of the given CORE ranking.
	 *
	 * @param ranking the CORE ranking.
	 * @param showNotRanked indicates if the label for the not ranked indicator is replied. It it is
	 *      {@code false} the empty string is replied.
	 * @return the string representation of the given CORE ranking.
	 */
	protected String toString(CoreRanking ranking, boolean showNotRanked) {
		return ranking == null || ranking == CoreRanking.NR
				? (showNotRanked ? getMessage(MESSAGE_PREFIX + "NotRanked") : "") //$NON-NLS-1$ //$NON-NLS-2$
				: ranking.toString();
	}

	/** Replies the string representation of the given publication category.
	 *
	 * @param category the publiction category.
	 * @return the string representation of the given category.
	 */
	protected String toString(PublicationCategory category) {
		return category == null ? getMessage(MESSAGE_PREFIX + "NoCategory") //$NON-NLS-1$
				: getMessage(MESSAGE_PREFIX + "Category", category.getAcronym(), category.getLabel()); //$NON-NLS-1$
	}

	/** Replies the category of a publication.
	 *
	 * @param publication the publication to read.
	 * @param rankingSystem the ranking system to be used.
	 * @return the category.
	 */
	protected static PublicationCategory getCategory(Production publication, JournalRankingSystem rankingSystem) {
		final PublicationCategory category;
		if (publication instanceof JournalBasedPublication) {
			category = ((JournalBasedPublication) publication).getCategory(rankingSystem);
		} else {
			category = publication.getCategory();
		}
		return category;
	}

	/** Replies the category of a publication.
	 *
	 * @param publication the publication to read.
	 * @param rankingSystem the ranking system to be used.
	 * @return the category.
	 */
	protected static QuartileRanking getQuartile(Production publication, JournalRankingSystem rankingSystem) {
		if (publication instanceof JournalBasedPublication) {
			final JournalBasedPublication pub = (JournalBasedPublication) publication;
			switch (rankingSystem) {
			case SCIMAGO:
				return pub.getScimagoQIndex();
			case WOS:
				return pub.getWosQIndex();
			default:
				//
			}
		}
		return QuartileRanking.NR;
	}

	/** Replies the numbers of publications per year for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param journalRankingSystem the system that is used for ranking the journal papers.
	 * @param enableAcl indicates if the count of ACL papers is enabled.
	 * @param enableAcln indicates if the count of ACLN/ACSL papers is enabled.
	 * @param enableCact indicates if the count of C-ACTI/C-ACTN papers is enabled.
	 * @param enableOtherConf indicates if the count of C-COM/C-AFF documents is enabled.
	 * @param enableBooks indicates if the count of OS/COS documents is enabled.
	 * @param enableOthers indicates if the count for the other types of documents is enabled.
	 * @return rows with: year, paper count for each enabled column.
	 */
	@SuppressWarnings("static-method")
	public List<List<Integer>> getNumberOfPublicationsPerYear(Collection<? extends Publication> publications, JournalRankingSystem journalRankingSystem,
			boolean enableAcl, boolean enableAcln, boolean enableCact, boolean enableOtherConf, boolean enableBooks,
			boolean enableOthers) {
		final JournalRankingSystem rankingSystem0 = journalRankingSystem == null ? JournalRankingSystem.getDefault() : journalRankingSystem;
		final Map<Integer, Collection<Publication>> publicationsPerYear = publications.stream()
				.collect(Collectors.toMap(
						it -> Integer.valueOf(it.getPublicationYear()),
						it -> Collections.singleton(it),
						(a, b) -> {
							final MultiCollection<Publication> multi = new MultiCollection<>();
							multi.addCollection(a);
							multi.addCollection(b);
							return multi;
						}));
		return publicationsPerYear.entrySet().stream()
				.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
				.map(it -> {
					int acl = 0;
					int acln = 0;
					int cact = 0;
					int otherConf = 0;
					int books = 0;
					int other = 0;
					for (final Publication pub : it.getValue()) {
						final PublicationCategory category = getCategory(pub, rankingSystem0);
						switch (category) {
						case ACL:
							++acl;
							break;
						case ACLN:
						case ASCL:
							++acln;
							break;
						case C_ACTI:
						case C_ACTN:
							++cact;
							break;
						case C_AFF:
						case C_COM:
							++otherConf;
							break;
						case COS:
						case OS:
							++books;
							break;
						case COV:
						case BRE:
						case AP:
						case C_INV:
						case DO:
						case OR:
						case OV:
						case PAT:
						case PT:
						case PV:
						case TH:
						default:
							++other;
							break;
						}
					}
					final List<Integer> columns = new ArrayList<>(3);
					columns.add(it.getKey());
					if (enableAcl) {
						columns.add(Integer.valueOf(acl));
					}
					if (enableAcln) {
						columns.add(Integer.valueOf(acln));
					}
					if (enableCact) {
						columns.add(Integer.valueOf(cact));
					}
					if (enableOtherConf) {
						columns.add(Integer.valueOf(otherConf));
					}
					if (enableBooks) {
						columns.add(Integer.valueOf(books));
					}
					if (enableOthers) {
						columns.add(Integer.valueOf(other));
					}
					return columns;
				})
				.collect(Collectors.toList());
	}

	/** Replies the numbers of publications per category for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param rankingSystem the ranking system to be used.
	 * @param lastYear the last year of the publications.
	 * @param excludeLastYear indicates if the value for the last year must be excluded.
	 * @return rows with: category, count.
	 */
	public List<List<Object>> getNumberOfPublicationsPerCategory(Collection<? extends Publication> publications, JournalRankingSystem rankingSystem,
			int lastYear, boolean excludeLastYear) {
		final JournalRankingSystem rankingSystem0 = rankingSystem == null ? JournalRankingSystem.getDefault() : rankingSystem;
		final Map<PublicationCategory, Integer> publicationsPerYear = publications.stream()
				.filter(it -> !excludeLastYear || it.getPublicationYear() != lastYear)
				.collect(Collectors.toMap(
						it -> getCategory(it, rankingSystem0),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		return publicationsPerYear.entrySet().stream()
				.filter(it -> it.getKey().isScientificEventPaper() || it.getKey().isScientificJournalPaper())
				.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
				.map(it -> {
					final List<Object> columns = new ArrayList<>(2);
					columns.add(toString(it.getKey()));
					columns.add(it.getValue());
					return columns;
				})
				.collect(Collectors.toList());
	}

	/** Replies the numbers of publications per quartile for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param rankingSystem the ranking system to be used.
	 * @param lastYear the last year of the publications.
	 * @param excludeLastYear indicates if the value for the last year must be excluded.
	 * @return rows with: quartile, count.
	 */
	public List<List<Object>> getNumberOfPublicationsPerQuartile(Collection<? extends Publication> publications, JournalRankingSystem rankingSystem,
			int lastYear, boolean excludeLastYear) {
		final JournalRankingSystem rankingSystem0 = rankingSystem == null ? JournalRankingSystem.getDefault() : rankingSystem;
		final Map<QuartileRanking, Integer> publicationsPerYear = publications.stream()
				.filter(it -> it instanceof JournalBasedPublication && (!excludeLastYear || it.getPublicationYear() != lastYear))
				.map(it -> (JournalBasedPublication) it)
				.filter(it -> getQuartile(it, rankingSystem0) != QuartileRanking.NR)
				.collect(Collectors.toMap(
						it -> getQuartile(it, rankingSystem0),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		return publicationsPerYear.entrySet().stream()
				.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
				.map(it -> {
					final List<Object> columns = new ArrayList<>(2);
					columns.add(toString(it.getKey(), true));
					columns.add(it.getValue());
					return columns;
				})
				.collect(Collectors.toList());
	}

	/** Replies the numbers of conference papers per CORE rank for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param lastYear the last year of the publications.
	 * @param excludeLastYear indicates if the value for the last year must be excluded.
	 * @return rows with: core, count.
	 */
	public List<List<Object>> getNumberOfPublicationsPerCoreRank(Collection<? extends Publication> publications,
			int lastYear, boolean excludeLastYear) {
		final Map<CoreRanking, Integer> publicationsPerYear = publications.stream()
				.filter(it -> (it.getCategory() == PublicationCategory.C_ACTI
					|| it.getCategory() == PublicationCategory.C_ACTN)
					&& it instanceof ConferenceBasedPublication
					&& (!excludeLastYear || it.getPublicationYear() != lastYear))
				.map(it -> (ConferenceBasedPublication) it)
				.collect(Collectors.toMap(
						it -> it.getCoreRanking(),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		return publicationsPerYear.entrySet().stream()
				.sorted((a, b) -> - a.getKey().compareTo(b.getKey()))
				.map(it -> {
					final List<Object> columns = new ArrayList<>(2);
					final CoreRanking ranking = it.getKey();
					columns.add(toString(ranking, true));
					columns.add(it.getValue());
					return columns;
				})
				.collect(Collectors.toList());
	}

	/** Replies the numbers of publications per research axis for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param lastYear the last year of the publications.
	 * @param excludeLastYear indicates if the value for the last year must be excluded.
	 * @return rows with: axis, count.
	 */
	public List<List<Object>> getNumberOfPublicationsPerScientificAxis(Collection<? extends Publication> publications,
			int lastYear, boolean excludeLastYear) {
		final Map<ScientificAxis, Integer> projectsPerAxis = new HashMap<>();
		final AtomicInteger outsideAxis = new AtomicInteger();
		publications.stream()
			.filter(it -> (!excludeLastYear || it.getPublicationYear() != lastYear)
					&& (it.getCategory().isScientificJournalPaper() || it.getCategory().isScientificEventPaper()))
			.forEach(it -> {
				if (!it.getScientificAxes().isEmpty()) {
					for (final ScientificAxis axis : it.getScientificAxes()) {
						final Integer oldValue = projectsPerAxis.get(axis);
						if (oldValue == null) {
							projectsPerAxis.put(axis, Integer.valueOf(1));
						} else {
							projectsPerAxis.put(axis, Integer.valueOf(oldValue.intValue() + 1));
						}
					}
				} else {
					outsideAxis.incrementAndGet();
				}
			});
		if (outsideAxis.intValue() > 0) {
			final ScientificAxis outAxis = new ScientificAxis();
			outAxis.setName(getMessage(MESSAGE_PREFIX + "OutsideScientificAxis")); //$NON-NLS-1$
			projectsPerAxis.put(outAxis, Integer.valueOf(outsideAxis.get()));
		}
		return projectsPerAxis.entrySet().stream()
				.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
				.map(it -> {
					final List<Object> columns = new ArrayList<>(2);
					final String name = Strings.isNullOrEmpty(it.getKey().getAcronym()) ? it.getKey().getName()
							: it.getKey().getAcronym() + " - " + it.getKey().getName(); //$NON-NLS-1$
					columns.add(name);
					columns.add(it.getValue());
					return columns;
				})
				.collect(Collectors.toList());
	}

	/** Replies the numbers of publications per journal for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param referenceYear the year of reference for computing the quartiles.
	 * @param excludeReferenceYear indicates if the value for the reference year must be excluded.
	 * @return rows with: journal name, publisher, Scimago, WoS, Impact factor, count.
	 */
	public List<List<Object>> getNumberOfPublicationsPerJournal(Collection<? extends Publication> publications,
			int referenceYear, boolean excludeReferenceYear) {
		final Map<Journal, Integer> publicationsPerYear = publications.stream()
				.filter(it -> it instanceof JournalBasedPublication
						&& (!excludeReferenceYear || it.getPublicationYear() != referenceYear))
				.map(it -> (JournalBasedPublication) it)
				.filter(it -> it.getJournal() != null)
				.collect(Collectors.toMap(
						it -> it.getJournal(),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		final Comparator<Journal> comparator = EntityUtils.getPreferredJournalComparator();
		return publicationsPerYear.entrySet().stream()
				.sorted((a, b) -> {
					int cmp = a.getValue().compareTo(b.getValue());
					if (cmp != 0) {
						return -cmp;
					}
					cmp = Float.compare(a.getKey().getImpactFactorByYear(referenceYear), b.getKey().getImpactFactorByYear(referenceYear));
					if (cmp != 0) {
						return -cmp;
					}
					return comparator.compare(a.getKey(), b.getKey());
				})
				.map(it -> {
					final Journal journal = it.getKey();
					final List<Object> columns = new ArrayList<>(2);
					columns.add(journal.getJournalName());
					columns.add(journal.getPublisher());
					columns.add(toString(journal.getScimagoQIndexByYear(referenceYear), false));
					columns.add(toString(journal.getWosQIndexByYear(referenceYear), false));
					columns.add(Float.valueOf(journal.getImpactFactorByYear(referenceYear)));
					columns.add(it.getValue());
					return columns;
				})
				.collect(Collectors.toList());
	}

	private static int compare(CoreRanking a, CoreRanking b) {
		return CoreRanking.normalize(a).compareTo(CoreRanking.normalize(b));
	}

	/** Replies the numbers of publications per conference for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param referenceYear the year of reference for computing the CORE ranks.
	 * @param excludeReferenceYear indicates if the value for the reference year must be excluded.
	 * @return rows with: journal name, publisher, Scimago, WoS, Impact factor, count.
	 */
	public List<List<Object>> getNumberOfPublicationsPerConference(Collection<? extends Publication> publications,
			int referenceYear, boolean excludeReferenceYear) {
		final Map<Conference, Integer> publicationsPerYear = publications.stream()
				.filter(it -> (it.getCategory() == PublicationCategory.C_ACTI
					|| it.getCategory() == PublicationCategory.C_ACTN)
					&& it instanceof ConferenceBasedPublication
					&& (!excludeReferenceYear || it.getPublicationYear() != referenceYear))
				.map(it -> (ConferenceBasedPublication) it)
				.filter(it -> it.getConference() != null)
				.collect(Collectors.toMap(
						it -> it.getConference(),
						it -> Integer.valueOf(1),
						(a, b) -> {
							return Integer.valueOf(a.intValue() + b.intValue());
						}));
		final Comparator<Conference> comparator = EntityUtils.getPreferredConferenceComparator();
		return publicationsPerYear.entrySet().stream()
				.sorted((a, b) -> {
					int cmp = a.getValue().compareTo(b.getValue());
					if (cmp != 0) {
						return -cmp;
					}
					cmp = compare(a.getKey().getCoreIndexByYear(referenceYear), b.getKey().getCoreIndexByYear(referenceYear));
					if (cmp != 0) {
						return -cmp;
					}
					return comparator.compare(a.getKey(), b.getKey());
				})
				.map(it -> {
					final Conference conference = it.getKey();
					final List<Object> columns = new ArrayList<>(2);
					columns.add(conference.getName() + " (" + conference.getAcronym() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
					columns.add(conference.getPublisher());
					columns.add(toString(conference.getCoreIndexByYear(referenceYear), false));
					columns.add(it.getValue());
					return columns;
				})
				.collect(Collectors.toList());
	}

	private static List<Membership> getLastMemberships(Person author, ResearchOrganization referenceOrganization, LocalDate tw0, LocalDate tw1) {
		final int refOrgId = referenceOrganization.getId();
		final List<Membership> lastMemberships = new ArrayList<>();
		final MutableInt year = new MutableInt(Integer.MIN_VALUE);
		author.getMemberships().stream()
			.filter(it -> it.getMemberSinceWhen() == null || it.getMemberSinceWhen().getYear() <= tw0.getYear())
			.forEach(it -> {
				if (it.isActiveIn(tw0, tw1)) {
					if (year.intValue() < tw0.getYear()) {
						year.setValue(tw0.getYear());
						lastMemberships.clear();
					}
					lastMemberships.add(it);
				} else {
					if (it.getMemberToWhen().getYear() > year.intValue()) {
						year.setValue(it.getMemberToWhen().getYear());
						lastMemberships.clear();
						lastMemberships.add(it);
					} else if (it.getMemberToWhen().getYear() == year.intValue()) {
						lastMemberships.add(it);
					}
				}
			});
		// Ignore the authors who are in the reference organization
		if (lastMemberships.stream().anyMatch(it -> refOrgId == it.getResearchOrganization().getId())) {
			return null;
		}
		return lastMemberships;
	}

	/** Replies the numbers of publications per country for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param referenceOrganization the organization for which the associated members are ignored in the counting.
	 * @param lastYear the last year of the publications.
	 * @param excludeLastYear indicates if the value for the last year must be excluded.
	 * @return rows with: country name, count.
	 */
	public List<List<Object>> getNumberOfPublicationsPerCountry(Collection<? extends Publication> publications,
			ResearchOrganization referenceOrganization, int lastYear, boolean excludeLastYear) {
		final Map<CountryCode, Integer> papersPerCountry = new TreeMap<>((a, b) -> {
			if (a == b) {
				return 0;
			}
			if (a == null) {
				return Integer.MIN_VALUE;
			}
			if (b == null) {
				return Integer.MAX_VALUE;
			}
			return a.compareTo(b);
		});
		final Map<Integer, List<Map<String, Object>>> anonymousPapers = new TreeMap<>();
		publications.stream()
			.filter(it -> (it.getCategory().isScientificEventPaper() || it.getCategory().isScientificJournalPaper())
					&& (!excludeLastYear || it.getPublicationYear() != lastYear))
			.forEach(it -> {
				final LocalDate tw0 = LocalDate.of(it.getPublicationYear(), 1, 1);
				final LocalDate tw1 = LocalDate.of(it.getPublicationYear(), 12, 31);
				final Set<CountryCode> countriesForPaper = new TreeSet<>();
				for (final Person author : it.getAuthors()) {
					final List<Membership> lastMemberships = getLastMemberships(author, referenceOrganization, tw0, tw1);
					// Test if the author is from the organization, or not.
					// Only the author from other organizations are considered.
					if (lastMemberships != null) {
						if (!lastMemberships.isEmpty()) {
							// The member is not member of the organization
							for (final Membership membership : lastMemberships) {
								final CountryCode country = membership.getResearchOrganization().getCountry();
								if (country != null) {
									countriesForPaper.add(country.normalize());
								}
							}
						} else {
							// The member is not member of the organization, but no membership for determining the country
							final List<Map<String, Object>> persons = anonymousPapers.computeIfAbsent(Integer.valueOf(it.getId()), it0 -> new ArrayList<>());
							final Map<String, Object> authorDesc = new HashMap<>();
							authorDesc.put("id", Integer.valueOf(author.getId())); //$NON-NLS-1$
							authorDesc.put("name", author.getFullName()); //$NON-NLS-1$
							persons.add(authorDesc);
						}
					}
				}
				for (final CountryCode country : countriesForPaper) {
					final Integer value = papersPerCountry.get(country);
					if (value == null) {
						papersPerCountry.put(country, Integer.valueOf(1));
					} else {
						papersPerCountry.put(country, Integer.valueOf(value.intValue() + 1));
					}
				}
			});
		if (!anonymousPapers.isEmpty()) {
			try {
				final ObjectMapper jsonMapper = JsonUtils.createMapper();
				getLogger().info("Papers with country-less author: " + jsonMapper.writeValueAsString(anonymousPapers)); //$NON-NLS-1$
			} catch (JsonProcessingException ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
			}
			papersPerCountry.put(null, Integer.valueOf(anonymousPapers.size()));
		}
		return papersPerCountry.entrySet().stream()
				.map(it -> {
					final List<Object> columns = new ArrayList<>(2);
					final CountryCode cc = it.getKey();
					if (cc == null) {
						columns.add("?"); //$NON-NLS-1$
					} else {
						columns.add(cc.getDisplayCountry());
					}
					columns.add(it.getValue());
					return columns;
				})
				// Sorted by quantity and country code
				.sorted((a, b) -> {
					final Integer na = (Integer) a.get(1);
					final Integer nb = (Integer) b.get(1);
					int cmp = nb.compareTo(na);
					if (cmp != 0) {
						return cmp;
					}
					final String la = (String) a.get(0);
					final String lb = (String) b.get(0);
					return la.compareToIgnoreCase(lb);
				})
				.collect(Collectors.toList());
	}

	/** Replies the numbers of publications per member for the given set of publications.
	 *
	 * @param publications the publications to analyze.
	 * @param referenceOrganization the organization for which the associated members are ignored in the counting.
	 * @param rankingSystem the ranking system to be used.
	 * @param minYear the minimum year of the publications.
	 * @param maxYear the maximum year of the publications.
	 * @param lastYear the last year of the publications.
	 * @return rows with: member, journal count, conference count.
	 */
	@SuppressWarnings("static-method")
	public List<List<Object>> getNumberOfPublicationsPerMember(Collection<? extends Publication> publications, ResearchOrganization referenceOrganization,
			JournalRankingSystem rankingSystem, int minYear, int maxYear) {
		final Map<Person, Map<PublicationCategory, Map<Integer, Integer>>> stats = new HashMap<>();
		publications.stream()
			.filter(it -> it.getCategory().isScientificEventPaper() || it.getCategory().isScientificJournalPaper())
			.forEach(it -> {
				final int year = it.getPublicationYear();
				final LocalDate tw0 = LocalDate.of(year, 1, 1);
				final LocalDate tw1 = LocalDate.of(year, 12, 31);
				final PublicationCategory category = getCategory(it, rankingSystem);
				for (final Person author : it.getAuthors()) {
					if (author.getMemberships().stream()
							.filter(it0 -> it0.isActiveIn(tw0, tw1) && it0.getResearchOrganization().getId() == referenceOrganization.getId()
							&& it0.isMainPosition() && !it0.getMemberStatus().isExternalPosition())
							.findAny().isPresent()) {
						final Map<PublicationCategory, Map<Integer, Integer>> stats0 = stats.computeIfAbsent(author, it0 -> new HashMap<>());
						final Map<Integer, Integer> stats1 = stats0.computeIfAbsent(category, it0 -> new HashMap<>());
						final Integer oldValue = stats1.get(Integer.valueOf(year));
						if (oldValue == null) {
							stats1.put(Integer.valueOf(year), Integer.valueOf(1));
						} else {
							stats1.put(Integer.valueOf(year), Integer.valueOf(oldValue.intValue() + 1));
						}
					}
				}
			});
		final List<List<Object>> table = new ArrayList<>();
		stats.entrySet().stream()
			.forEach(it -> {
				List<Object> columns;
	
				columns = new ArrayList<>();
				columns.add(it.getKey().getFullNameWithLastNameFirst());
				if (fillMemberTable(it.getValue(), columns, minYear, maxYear, PublicationCategory.ACL)) {
					table.add(columns);
				}
	
				columns = new ArrayList<>();
				columns.add(it.getKey().getFullNameWithLastNameFirst());
				if (fillMemberTable(it.getValue(), columns, minYear, maxYear, PublicationCategory.ACLN)) {
					table.add(columns);
				}
	
				columns = new ArrayList<>();
				columns.add(it.getKey().getFullNameWithLastNameFirst());
				if (fillMemberTable(it.getValue(), columns, minYear, maxYear, PublicationCategory.C_ACTI)) {
					table.add(columns);
				}
	
				columns = new ArrayList<>();
				columns.add(it.getKey().getFullNameWithLastNameFirst());
				if (fillMemberTable(it.getValue(), columns, minYear, maxYear, PublicationCategory.C_ACTN)) {
					table.add(columns);
				}
	
				columns = new ArrayList<>();
				columns.add(it.getKey().getFullNameWithLastNameFirst());
				if (fillMemberTable(it.getValue(), columns, minYear, maxYear, PublicationCategory.C_COM)) {
					table.add(columns);
				}
			});

		return table.stream().sorted((a, b) -> {
			int cmp = ((String) a.get(0)).compareToIgnoreCase((String) b.get(0));
			if (cmp != 0) {
				return cmp;
			}
			return ((String) a.get(1)).compareToIgnoreCase((String) b.get(1));
		})
				.collect(Collectors.toList());
	}

	private static boolean fillMemberTable(Map<PublicationCategory, Map<Integer, Integer>> input, List<Object> columns, int minYear, int maxYear, PublicationCategory category) {
		final Map<Integer, Integer> stats = input.get(category);
		columns.add(category.getAcronym());
		int total = 0;
		for (int y = minYear; y <= maxYear; ++y) {
			Integer value = null;
			if (stats != null) {
				value = stats.get(Integer.valueOf(y));
			}
			if (value == null) {
				value = Integer.valueOf(0);
			}
			columns.add(value);
			total += value.intValue();
		}
		columns.add(Integer.valueOf(total));
		return total > 0;
	}
	
	/** Replies the publication rates for the members of the given organization.
	 *
	 * @param publications the publications to analyze.
	 * @param referenceOrganization the organization for which the publication are considered.
	 * @param rankingSystem the ranking system to be used for the journals.
	 * @param minYear the minimum year of the publications.
	 * @param maxYear the maximum year of the publications.
	 * @param excludeLastYear indicates if the value for the last year must be excluded from the global values.
	 * @return rows with: category, rates per year, global rate.
	 * @since 3.6
	 */
	public List<List<Object>> getPublicationAnnualRates(Collection<? extends Publication> publications, ResearchOrganization referenceOrganization,
			JournalRankingSystem rankingSystem, int minYear, int maxYear, boolean excludeLastYear) {
		final List<List<Object>> table = new ArrayList<>();
		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "ETP"), //$NON-NLS-1$
				this.permanentResearcherCountIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear),
				minYear, maxYear, excludeLastYear);

		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "PhdStudents"), //$NON-NLS-1$
				this.phdStudentCountIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear),
				minYear, maxYear, excludeLastYear);

		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "Postdocs"), //$NON-NLS-1$
				this.postdocCountIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear),
				minYear, maxYear, excludeLastYear);
		
		Map<Integer, Number> values;
		switch (rankingSystem) {
		case SCIMAGO:
			values = this.scimagoJournalPaperCount.getValuesPerYear(referenceOrganization, minYear, maxYear);
			break;
		case WOS:
			values = this.wosJournalPaperCount.getValuesPerYear(referenceOrganization, minYear, maxYear);
			break;
		default:
			throw new IllegalArgumentException("Unsupported type of ranking system"); //$NON-NLS-1$
		}
		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "ACL"), //$NON-NLS-1$
				values, minYear, maxYear, excludeLastYear);

		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "ACLN"), //$NON-NLS-1$
				this.unrankedJournalPaperCount.getValuesPerYear(referenceOrganization, minYear, maxYear),
				minYear, maxYear, excludeLastYear);

		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "CACTIN"), //$NON-NLS-1$
				this.conferencePaperCount.getValuesPerYear(referenceOrganization, minYear, maxYear),
				minYear, maxYear, excludeLastYear);

		switch (rankingSystem) {
		case SCIMAGO:
			values = this.scimagoPaperRateIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear);
			break;
		case WOS:
			values = this.wosRatePaperIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear);
			break;
		default:
			throw new IllegalArgumentException("Unsupported type of ranking system"); //$NON-NLS-1$
		}
		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "PublicationRatioAclPerFTE"), //$NON-NLS-1$
				values,
				minYear, maxYear, excludeLastYear);

		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "PublicationRatioCactPerFTE"), //$NON-NLS-1$
				this.conferenceRateIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear),
				minYear, maxYear, excludeLastYear);

		switch (rankingSystem) {
		case SCIMAGO:
			values = this.scimagoPhdPaperRateIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear);
			break;
		case WOS:
			values = this.wosPhdPaperRateIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear);
			break;
		default:
			throw new IllegalArgumentException("Unsupported type of ranking system"); //$NON-NLS-1$
		}
		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "PublicationRatioAclPerPhd"), //$NON-NLS-1$
				values,
				minYear, maxYear, excludeLastYear);

		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "PublicationRatioCactPerPhd"), //$NON-NLS-1$
				this.conferencePhdRateIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear),
				minYear, maxYear, excludeLastYear);

		switch (rankingSystem) {
		case SCIMAGO:
			values = this.scimagoPostdocPaperRateIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear);
			break;
		case WOS:
			values = this.wosPostdocPaperRateIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear);
			break;
		default:
			throw new IllegalArgumentException("Unsupported type of ranking system"); //$NON-NLS-1$
		}
		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "PublicationRatioAclPerPostdoc"), //$NON-NLS-1$
				values,
				minYear, maxYear, excludeLastYear);

		addPublicationAnnualRates(table, getMessage(MESSAGE_PREFIX + "PublicationRatioCactPerPostdoc"), //$NON-NLS-1$
				this.conferencePostdocRateIndicator.getValuesPerYear(referenceOrganization, minYear, maxYear),
				minYear, maxYear, excludeLastYear);

		return table;
	}

	private static void addPublicationAnnualRates(List<List<Object>> table, String category,
			Map<Integer, ? extends Number> values, int minYear, int maxYear, boolean excludeLastYear) {
		final List<Object> row = new ArrayList<>();
		row.add(category);
		float average = 0f;
		int countAvg = 0;
		for (int y = minYear; y <= maxYear; ++y) {
			final Number value = values.get(Integer.valueOf(y));
			final float fvalue;
			if (value != null) {
				fvalue = value.floatValue();
			} else {
				fvalue = 0f;
			}
			row.add(Float.valueOf(fvalue));
			if (!excludeLastYear || y < maxYear) {
				average += fvalue;
				++countAvg;
			}
		}
		if (countAvg != 0) {
			row.add(Float.valueOf(average / countAvg));
		} else {
			row.add(Float.valueOf(0f));
		}
		table.add(row);
	}

}
