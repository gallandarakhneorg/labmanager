/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.io.ris;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ciadlab.labmanager.utils.IntegerRange;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Utilities for RIS.
 * RIS is a standardized tag format developed by Research Information Systems, Incorporated to enable citation programs
 * to exchange data.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.7
 * @see "https://en.wikipedia.org/wiki/RIS_(file_format)"
 */
public abstract class AbstractRIS implements RIS {

	private static final Pattern PAGES_PATTERN = Pattern.compile("^\\s*([0-9]+)(?:\\s*\\-+\\s*([0-9]+)\\s*)?$"); //$NON-NLS-1$

	/** Parse the string that represents a page range.
	 *
	 * @param pages the string to parse.
	 * @return the page range, or {@code null} if the given argument cannot be parsed.
	 */
	protected static IntegerRange parsePages(String pages) {
		if (!Strings.isNullOrEmpty(pages)) {
			final Matcher matcher = PAGES_PATTERN.matcher(pages);
			if (matcher.find()) {
				try {
					final String p0 = matcher.group(1);
					if (matcher.groupCount() > 1) {
						final String p1 = matcher.group(2);
						final int page0 = Integer.parseUnsignedInt(p0);
						final int page1 = Integer.parseUnsignedInt(p1);
						return new IntegerRange(page0, page1);
					}
					final int page = Integer.parseUnsignedInt(p0);
					return new IntegerRange(page, page);
				} catch (Throwable ex) {
					//
				}
			}
		}
		return null;
	}

}
