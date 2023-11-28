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

package fr.ciadlab.labmanager.controller.view.publication;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fr.ciadlab.labmanager.entities.publication.PublicationCategory;
import fr.ciadlab.labmanager.entities.publication.PublicationType;

/** Contains a description of statstics related to publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PublicationsStat implements Serializable {

	private static final long serialVersionUID = -4411393590862116868L;

	private final int year;

	private final Map<PublicationType, Integer> countsPerType = new HashMap<>();

	private final Map<PublicationCategory, Integer> countsPerCategory = new HashMap<>();

	private Integer total;

	/** Constructor.
	 *
	 * @param year the year for which the statistics are computed.
	 */
	public PublicationsStat(int year) {
		this.year = year;
	}

	/** Replies the year for which the statistics are computed.
	 *
	 * @return the year.
	 */
	public int getYear() {
		return this.year;
	}

	/** Increment the number of publications of a given type.
	 *
	 * @param type the type of publication, never {@code null}.
	 * @param isRanked indicated if the count is for ranked publication.
	 * @param count the number of publications for the given type to add to the total number. Only positive values are
	 *     accepted. If you provide a negative value, it is ignored.
	 */
	public void increment(PublicationType type, boolean isRanked, int count) {
		assert type != null;
		if (count > 0) {
			Integer total = this.countsPerType.get(type);
			if (total == null) {
				this.countsPerType.put(type, Integer.valueOf(count));
			} else {
				this.countsPerType.put(type, Integer.valueOf(total.intValue() + count));
			}
			final PublicationCategory category = type.getCategory(isRanked);
			total = this.countsPerCategory.get(category);
			if (total == null) {
				this.countsPerCategory.put(category, Integer.valueOf(count));
			} else {
				this.countsPerCategory.put(category, Integer.valueOf(total.intValue() + count));
			}
			this.total = null;
		}
	}

	/** Replies the total number of publications, including all the types of publications.
	 *
	 * @return the total number of publications.
	 */
	public int getTotal() {
		if (this.total == null) {
			int tot = 0;
			for (final Integer count : this.countsPerType.values()) {
				tot += count.intValue();
			}
			this.total = Integer.valueOf(tot);
		}
		return this.total.intValue(); 
	}

	/** Replies the number of publications for the given type.
	 *
	 * @param type the type of publication.
	 * @return the number of publications of the given type.
	 */
	public int count(PublicationType type) {
		assert type != null;
		Integer count = this.countsPerType.get(type);
		if (count != null) {
			return count.intValue();
		}
		return 0;
	}

	/** Replies the number of publications for the given category.
	 *
	 * @param category the category of publication.
	 * @return the number of publications of the given category.
	 */
	public int count(PublicationCategory category) {
		assert category != null;
		Integer count = this.countsPerCategory.get(category);
		if (count != null) {
			return count.intValue();
		}
		return 0;
	}

	/** Replies the number of publications for the given categories.
	 *
	 * @param categories the categories of publication.
	 * @return the number of publications of the given categories.
	 */
	public int count(PublicationCategory... categories) {
		if (categories != null) {
			int total = 0;
			for (final PublicationCategory category : categories) {
				total += count(category);
			}
			return total;
		}
		return 0;
	}

}
