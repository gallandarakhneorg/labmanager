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

package fr.ciadlab.labmanager.io.gscholar;

import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.microsoft.playwright.ElementHandle;
import fr.ciadlab.labmanager.io.AbstractWebScraper;
import org.arakhne.afc.progress.Progression;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Accessor to the online Web-of-Science platform.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.5
 * @see "https://www.webofscience.com"
 */
@Component
@Primary
public class OnlineGoogleScholarPlatform extends AbstractWebScraper implements GoogleScholarPlatform {

	@Override
	public GoogleScholarPerson getPersonRanking(URL gsProfile, Progression progress) throws Exception {
		final Progression prog = ensureProgress(progress);
		if (gsProfile != null) {
			final AtomicReference<GoogleScholarPerson> output = new AtomicReference<>();
			//"onetrust-reject-all-handler"
			loadHtmlPage(
					DEFAULT_DEVELOPER,
					gsProfile,
					prog,
					"table[id='gsc_rsb_st']", //$NON-NLS-1$
					0,
					(page, element0) -> {
						final List<ElementHandle> elements = element0.asElement().querySelectorAll("td[class='gsc_rsb_std']"); //$NON-NLS-1$
						if (elements != null) {
							int ihindex = -1;
							if (elements.size() > 2) {
								final Integer hindex = readInt(elements.get(2));
								ihindex = positiveInt(hindex);
							}
							int icitations = -1;
							if (elements.size() > 0) {
								final Integer citations = readInt(elements.get(0));
								icitations = positiveInt(citations); 
							}
							output.set(new GoogleScholarPerson(ihindex, icitations));
						}
					});
			final GoogleScholarPerson person = output.get();
			if (person != null) {
				return person;
			}
		}
		throw new IllegalArgumentException("Invalid Google Scholar URL or no valid access: " + gsProfile); //$NON-NLS-1$
	}

}
