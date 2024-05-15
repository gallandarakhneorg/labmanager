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

package fr.utbm.ciad.labmanager.views.appviews.journals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.utils.ranking.QuartileRanking;
import fr.utbm.ciad.labmanager.views.components.addons.wizard.AbstractContextData;

/** Data in the wizard for updating the journal ranking
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class JournalRankingUpdate extends AbstractContextData {
			
	private static final long serialVersionUID = -7689528428844273761L;

	private int year = LocalDate.now().getYear() - 1;

	private boolean wosEnable = true;
	
	private boolean scimagoEnable = true;

	private boolean impactFactorsEnable = true;

	private List<Journal> journals = new ArrayList<>();
	
	private final Map<Long, JournalRankingInformation> scimago = new TreeMap<>();

	private final Map<Long, JournalRankingInformation> wos = new TreeMap<>();

	/** Constructor.
	 */
	public JournalRankingUpdate() {
		//
	}

	/** Replies the year for the ranking updates.
	 *
	 * @return the year.
	 */
	public synchronized int getYear() {
		return this.year;
	}

	/** Set the year for the ranking updates.
	 *
	 * @param year the year.
	 */
	public synchronized void setYear(int year) {
		this.year = year;
	}

	/** Replies the list of journals.
	 * 
	 * @return the journals.
	 */
	public synchronized List<Journal> getJournals() {
		return this.journals;
	}

	/** Change the list of journals.
	 * 
	 * @param journals the journals.
	 */
	public synchronized void setJournals(List<Journal> journals) {
		assert journals != null;
		this.journals = journals;
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

	/** Replies if Scimago update is enabled.
	 *
	 * @return {@code true} if Scimago is enabled.
	 */
	public synchronized boolean getScimagoEnable() {
		return this.scimagoEnable;
	}
	
	/** Change the flag that indicates if Scimago update is enabled.
	 *
	 * @param enable {@code true} if Scimago is enabled.
	 */
	public synchronized void setScimagoEnable(boolean enable) {
		this.scimagoEnable = enable;
	}

	/** Replies if impact factor update is enabled.
	 *
	 * @return {@code true} if impact factor update is enabled.
	 */
	public synchronized boolean getImpactFactorsEnable() {
		return this.impactFactorsEnable;
	}
	
	/** Change the flag that indicates if impact factor update is enabled.
	 *
	 * @param enable {@code true} if impact factor update is enabled.
	 */
	public synchronized void setImpactFactorsEnable(boolean enable) {
		this.impactFactorsEnable = enable;
	}

	/** Add Scimago ranking for the journal with the given identifier.
	 *
	 * @param journalId the identifier of the journal.
	 * @param kwownQuartile the quartile that is already known for the journal
	 * @param choices the rankings per scientific field.
	 */
	public synchronized void addScimagoRanking(long journalId, QuartileRanking kwownQuartile, Map<String, QuartileRanking> choices) {
		this.scimago.put(Long.valueOf(journalId), new JournalRankingInformation(kwownQuartile, choices));
	}

	/** Remove all the references to the Scimago rankings.
	 */
	public synchronized void clearScimagoRankings() {
		this.scimago.clear();
	}

	/** Add WoS ranking for the journal with the given identifier.
	 *
	 * @param journalId the identifier of the journal.
	 * @param kwownQuartile the quartile that is already known for the journal
	 * @param choices the rankings per scientific field.
	 */
	public synchronized void addWosRanking(long journalId, QuartileRanking kwownQuartile, Map<String, QuartileRanking> choices) {
		this.wos.put(Long.valueOf(journalId), new JournalRankingInformation(kwownQuartile, choices));
	}

	/** Remove all the references to the WoS rankings.
	 */
	public synchronized void clearWosRankings() {
		this.wos.clear();
	}

	private record JournalRankingInformation(QuartileRanking knownQuartile, Map<String, QuartileRanking> rankings) {
		//
	}

}
