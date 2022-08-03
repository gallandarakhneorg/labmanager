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

package fr.ciadlab.labmanager.controller.publication;

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
	public void incrementCountForType(PublicationType type, boolean isRanked, int count) {
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
	public int getCountForType(PublicationType type) {
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
	public int getCountForCategory(PublicationCategory category) {
		assert category != null;
		Integer count = this.countsPerCategory.get(category);
		if (count != null) {
			return count.intValue();
		}
		return 0;
	}

}
