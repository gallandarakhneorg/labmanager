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

package fr.utbm.ciad.labmanager.utils.io.coreportal;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.microsoft.playwright.ElementHandle;
import fr.utbm.ciad.labmanager.utils.io.AbstractWebScraper;
import fr.utbm.ciad.labmanager.utils.ranking.CoreRanking;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.progress.Progression;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/** Accessor to the online CORE Portal.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 * @see "http://portal.core.edu.au/"
 */
@Component
@Primary
public class OnlineCorePortal extends AbstractWebScraper implements CorePortal {

	private static final String SCHEME = "http"; //$NON-NLS-1$

	private static final String HOST = "portal.core.edu.au"; //$NON-NLS-1$

	private static final String CONFERENCE_PATH = "conf-ranks/"; //$NON-NLS-1$

	private static final Pattern SOURCE_PATTERN = Pattern.compile("^Source:.*?([0-9]+)$", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

	private static final Pattern RANK_PATTERN = Pattern.compile("^Rank:.*?([^\\s]+)$", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

	/** Factory of URI builder.
	 */
	protected final UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();

	@Override
	public URL getConferenceUrl(String conferenceId) {
		if (!Strings.isNullOrEmpty(conferenceId)) {
			try {
				UriBuilder builder = this.uriBuilderFactory.builder();
				builder = builder.scheme(SCHEME);
				builder = builder.host(HOST);
				builder = builder.path(CONFERENCE_PATH).path(conferenceId);
				final URI uri = builder.build();
				return uri.toURL();
			} catch (Exception ex) {
				//
			}
		}
		return null;
	}

	private static boolean parseConferenceRankingBox(AtomicReference<CorePortalConference> output,
			AtomicInteger outputYear, ElementHandle box) {
		final List<ElementHandle> rows = box.querySelectorAll("div"); //$NON-NLS-1$
		String foundRank = null;
		Integer foundYear = null;
		for (final ElementHandle row : rows) {
			final String text = row.innerText().trim();
			if (!Strings.isNullOrEmpty(text)) {
				final Matcher matcher0 = SOURCE_PATTERN.matcher(text);
				if (matcher0.find()) {
					foundYear = Integer.valueOf(matcher0.group(1));
				}
				final Matcher matcher1 = RANK_PATTERN.matcher(text);
				if (matcher1.find()) {
					foundRank = matcher1.group(1);
				}
				if (foundRank != null && foundYear != null) {
					break;
				}
			}
		}
		if (foundRank != null && foundYear != null) {
			if (!Strings.isNullOrEmpty(foundRank)) {
				try {
					final CoreRanking ranking = CoreRanking.valueOfCaseInsensitive(foundRank);
					output.set(new CorePortalConference(ranking));
					outputYear.set(foundYear.intValue());
					return true;
				} catch (Throwable ex) {
					//
				}
			}
		}
		return false;
	}
	
	@Override
	public CorePortalConference getConferenceRanking(int year, String identifier, Progression progress) throws Exception {
		final Progression prog = ensureProgress(progress);
		if (!Strings.isNullOrEmpty(identifier)) {
			final URL url = getConferenceUrl(identifier);
			if (url != null) {
				final AtomicReference<CorePortalConference> output = new AtomicReference<>();
				final AtomicInteger outputYear = new AtomicInteger(Integer.MIN_VALUE);
				loadHtmlPage(
						DEFAULT_DEVELOPER,
						url,
						prog,
						"div[id=detail]", //$NON-NLS-1$
						0,
						(page, element0) -> {
							final List<ElementHandle> boxes = element0.querySelectorAll("div[class=detail]"); //$NON-NLS-1$
							for (final ElementHandle box : boxes) {
								final AtomicReference<CorePortalConference> output0 = new AtomicReference<>();
								final AtomicInteger outputYear0 = new AtomicInteger(0);
								if (parseConferenceRankingBox(output0, outputYear0, box)) {
									final int y = outputYear0.get();
									if (y <= year && (outputYear.get() < y)) {
										output.set(output0.get());
										outputYear.set(y);
									}
								}
							}
						});
				final CorePortalConference conference = output.get();
				if (conference != null) {
					return conference;
				}
			}
		}
		throw new IllegalArgumentException("Invalid CORE identifier or no valid access: " + identifier); //$NON-NLS-1$
	}

}
