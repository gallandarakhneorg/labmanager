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

package fr.utbm.ciad.labmanager.views.appviews.persons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.views.components.addons.wizard.AbstractContextData;

/** Data in the wizard for updating the person ranking.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class PersonRankingUpdate extends AbstractContextData {

	private static final long serialVersionUID = -6857923954882458328L;

	private boolean googleScholarEnable = true;
	
	private boolean scopusEnable = true;

	private boolean wosEnable = true;

	private List<Person> persons = new ArrayList<>();

	private final Map<Long, PersonRankingInformation> googleScholar = new TreeMap<>();

	private final Map<Long, PersonRankingInformation> scopus = new TreeMap<>();

	private final Map<Long, PersonRankingInformation> wos = new TreeMap<>();

	/** Constructor.
	 */
	public PersonRankingUpdate() {
		//
	}

	/** Replies the list of persons.
	 * 
	 * @return the persons.
	 */
	public synchronized List<Person> getPersons() {
		return this.persons;
	}

	/** Change the list of persons.
	 * 
	 * @param persons the persons.
	 */
	public synchronized void setPersons(List<Person> persons) {
		assert persons != null;
		this.persons = persons;
	}

	/** Replies if WoS update is enabled.
	 *
	 * @return {@code true} if WoS is enabled.
	 */
	public synchronized boolean getWosEnable() {
		return this.wosEnable;
	}
	
	/** Change the flag that indicates if WoS update is enabled.
	 *
	 * @param enable {@code true} if WoS is enabled.
	 */
	public synchronized void setWosEnable(boolean enable) {
		this.wosEnable = enable;
	}

	/** Replies if Scopus update is enabled.
	 *
	 * @return {@code true} if Scopus is enabled.
	 */
	public synchronized boolean getScopusEnable() {
		return this.scopusEnable;
	}
	
	/** Change the flag that indicates if Scopus update is enabled.
	 *
	 * @param enable {@code true} if Scopus is enabled.
	 */
	public synchronized void setScopusEnable(boolean enable) {
		this.scopusEnable = enable;
	}

	/** Replies if Google Scholar update is enabled.
	 *
	 * @return {@code true} if Google Scholar is enabled.
	 */
	public synchronized boolean getGoogleScholarEnable() {
		return this.googleScholarEnable;
	}
	
	/** Change the flag that indicates if Google Scholar update is enabled.
	 *
	 * @param enable {@code true} if Google Scholar is enabled.
	 */
	public synchronized void setGoogleScholarEnable(boolean enable) {
		this.googleScholarEnable = enable;
	}

	/** Remove all the references to the WoS rankings.
	 */
	public synchronized void clearWosRankings() {
		this.wos.clear();
	}

	/** Add WoS ranking for the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @param knownHindex the H-index of the person that is already known. 
	 * @param newHindex the new H-index for the person. 
	 * @param knownCitations the number of citations for the person that is already known. 
	 * @param newCitations the new number of citations for the person. 
	 */
	public synchronized void addWosRanking(long personId, int knownHindex, int newHindex, int knownCitations, int newCitations) {
		this.wos.put(Long.valueOf(personId), new PersonRankingInformation(knownHindex, newHindex, knownCitations, newCitations));
	}

	/** Remove all the references to the Scopus rankings.
	 */
	public synchronized void clearScopusRankings() {
		this.scopus.clear();
	}

	/** Add Scopus ranking for the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @param knownHindex the H-index of the person that is already known. 
	 * @param newHindex the new H-index for the person. 
	 * @param knownCitations the number of citations for the person that is already known. 
	 * @param newCitations the new number of citations for the person. 
	 */
	public synchronized void addScopusRanking(long personId, int knownHindex, int newHindex, int knownCitations, int newCitations) {
		this.scopus.put(Long.valueOf(personId), new PersonRankingInformation(knownHindex, newHindex, knownCitations, newCitations));
	}

	/** Remove all the references to the Google Scholar rankings.
	 */
	public synchronized void clearGoogleScholarRankings() {
		this.googleScholar.clear();
	}

	/** Add Google Scholar ranking for the person with the given identifier.
	 *
	 * @param personId the identifier of the person.
	 * @param knownHindex the H-index of the person that is already known. 
	 * @param newHindex the new H-index for the person. 
	 * @param knownCitations the number of citations for the person that is already known. 
	 * @param newCitations the new number of citations for the person. 
	 */
	public synchronized void addGoogleScholarRanking(long personId, int knownHindex, int newHindex, int knownCitations, int newCitations) {
		this.googleScholar.put(Long.valueOf(personId), new PersonRankingInformation(knownHindex, newHindex, knownCitations, newCitations));
	}

	/** Replies all the update information about the persons.
	 *
	 * @return the stream of information, one entry per person.
	 */
	public Stream<PersonNewInformation> getPersonUpdates() {
		final var defaultRanking = new PersonRankingInformation(0, 0, 0, 0);
		return getPersons().stream().map(person -> {
			final var personId = Long.valueOf(person.getId());
			
			final var wos = this.wos.getOrDefault(personId, defaultRanking);
			final var wosHindex = extractValue(wos.knownHindex(), wos.newHindex());
			final var wosCitations = extractValue(wos.knownCitations(), wos.newCitations());

			final var scopus = this.scopus.getOrDefault(personId, defaultRanking);
			final var scopusHindex = extractValue(scopus.knownHindex(), scopus.newHindex());
			final var scopusCitations = extractValue(scopus.knownCitations(), scopus.newCitations());

			final var googleScholar = this.googleScholar.getOrDefault(personId, defaultRanking);
			final var googleScholarHindex = extractValue(googleScholar.knownHindex(), googleScholar.newHindex());
			final var googleScholarCitations = extractValue(googleScholar.knownCitations(), googleScholar.newCitations());

			if (wosHindex != null || wosCitations != null
					|| scopusHindex != null || scopusCitations != null
					|| googleScholarHindex != null || googleScholarCitations != null) {
				return new PersonNewInformation(person,
						extractKnownValue(wos.knownHindex(), wosHindex), wosHindex,
						extractKnownValue(wos.knownCitations(), wosCitations), wosCitations,
						extractKnownValue(scopus.knownHindex(), scopusHindex), scopusHindex,
						extractKnownValue(scopus.knownCitations(), scopusCitations), scopusCitations,
						extractKnownValue(googleScholar.knownHindex(), googleScholarHindex), googleScholarHindex,
						extractKnownValue(googleScholar.knownCitations(), googleScholarCitations), googleScholarCitations);
			}
			return null;
		}).filter(it -> it != null);
	}

	private static Integer extractKnownValue(int oldValue, Integer newValue) {
		if (newValue != null) {
			return Integer.valueOf(Math.max(0, oldValue));
		}
		return null;
	}

	private static Integer extractValue(int oldValue, int newValue) {
		final var ov = Math.max(0, oldValue);
		final var nv = Math.max(0, newValue);
		if (ov != nv) {
			return Integer.valueOf(nv);
		}
		return null;
	}

	/** Description of the ranking information for a person.
	 * 
	 * @param knownHindex the H-index of the person that is already known. 
	 * @param newHindex the new H-index for the person. 
	 * @param knownCitations the number of citations for the person that is already known. 
	 * @param newCitations the new number of citations for the person. 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public record PersonRankingInformation(int knownHindex, int newHindex, int knownCitations, int newCitations) {
		//
	}

	/** Description of the information for a person.
	 * 
	 * @param person the person.
	 * @param oldWosHindex the old WOS H-index, or {@code null}. 
	 * @param newWosHindex the new WOS H-index, or {@code null}. 
	 * @param oldWosCitations the old WOS citations, or {@code null}. 
	 * @param newWosCitations the new WOS citations, or {@code null}. 
	 * @param oldScopusHindex the old Scopus H-index, or {@code null}. 
	 * @param newScopusHindex the new Scopus H-index, or {@code null}. 
	 * @param oldScopusCitations the old Scopus citations, or {@code null}. 
	 * @param newScopusCitations the new Scopus citations, or {@code null}. 
	 * @param oldGoogleScholarHindex the old Google Scholar H-index, or {@code null}. 
	 * @param newGoogleScholarHindex the new Google Scholar H-index, or {@code null}. 
	 * @param oldGoogleScholarCitations the old Google Scholar citations, or {@code null}. 
	 * @param newGoogleScholarCitations the new Google Scholar citations, or {@code null}. 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public record PersonNewInformation(Person person,
			Integer oldWosHindex, Integer newWosHindex, Integer oldWosCitations, Integer newWosCitations,
			Integer oldScopusHindex, Integer newScopusHindex, Integer oldScopusCitations, Integer newScopusCitations,
			Integer oldGoogleScholarHindex, Integer newGoogleScholarHindex, Integer oldGoogleScholarCitations, Integer newGoogleScholarCitations) {
		//
	}

}
