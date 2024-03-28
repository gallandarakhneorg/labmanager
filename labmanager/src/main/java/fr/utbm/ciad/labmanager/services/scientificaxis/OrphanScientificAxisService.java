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

package fr.utbm.ciad.labmanager.services.scientificaxis;

import java.util.Locale;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxis;
import fr.utbm.ciad.labmanager.data.scientificaxis.ScientificAxisRepository;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for orphan scientific axes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanScientificAxisService extends AbstractOrphanService<ScientificAxis> {

	private static final String MESSAGE_PREFIX = "orphanScientificAxisService."; //$NON-NLS-1$

	private final ScientificAxisRepository scientificAxisRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param scientificAxisRepository the repository for accessing the scientific axes.
	 */
	public OrphanScientificAxisService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ScientificAxisRepository scientificAxisRepository) {
		super(messages, constants);
		this.scientificAxisRepository = scientificAxisRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Locale locale, Progression progress) {
		computeOrphansInJson(receiver, this.scientificAxisRepository, this,
				Constants.SCIENTIFIC_AXIS_EDITING_ENDPOINT, Constants.AXIS_ENDPOINT_PARAMETER,
				Constants.SCIENTIFIC_AXIS_DELETING_ENDPOINT, Constants.AXIS_ENDPOINT_PARAMETER,
				locale, progress);
	}

	@Override
	public String getOrphanCriteria(ScientificAxis axis, Locale locale) {
		if (!axis.getProjects().isEmpty()) {
			return null;
		}
		if (!axis.getPublications().isEmpty()) {
			return null;
		}
		if (!axis.getMemberships().isEmpty()) {
			return null;
		}
		return getMessage(locale, MESSAGE_PREFIX + "NoLink"); //$NON-NLS-1$
	}

	@Override
	public String getOrphanEntityLabel(ScientificAxis entity, Locale locale) {
		return entity.getAcronymOrName();
	}

	@Override
	public String getOrphanTypeLabel(Locale locale) {
		return getMessage(locale, MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
