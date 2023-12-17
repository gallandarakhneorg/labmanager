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

package fr.utbm.ciad.labmanager.services.assostructure;

import java.util.Locale;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructure;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureHolder;
import fr.utbm.ciad.labmanager.data.assostructure.AssociatedStructureRepository;
import fr.utbm.ciad.labmanager.services.AbstractOrphanService;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for the managing the orphan associated structures.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanAssociatedStructureService extends AbstractOrphanService<AssociatedStructure> {

	private static final String MESSAGE_PREFIX = "orphanAssociatedStructureService."; //$NON-NLS-1$
	
	private AssociatedStructureRepository structureRepository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param structureRepository the repository for the associated structures.
	 */
	public OrphanAssociatedStructureService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired AssociatedStructureRepository structureRepository) {
		super(messages, constants);
		this.structureRepository = structureRepository;
	}

	@Override
	public void computeOrphans(ArrayNode receiver, Locale locale, Progression progress) {
		computeOrphansInJson(receiver, this.structureRepository, this,
				Constants.ASSOCIATED_STRUCTURE_EDITING_ENDPOINT, Constants.STRUCTURE_ENDPOINT_PARAMETER,
				Constants.ASSOCIATED_STRUCTURE_DELETING_ENDPOINT, Constants.STRUCTURE_ENDPOINT_PARAMETER,
				locale,
				progress);
	}

	@Override
	public String getOrphanCriteria(AssociatedStructure structure, Locale locale) {
		if (structure.getHolders().isEmpty()) {
			return getMessage(locale, MESSAGE_PREFIX + "EmptyHolderList"); //$NON-NLS-1$
		}
		if (structure.getCreationDate() == null) {
			return getMessage(locale, MESSAGE_PREFIX + "NoCreationDate"); //$NON-NLS-1$
		}
		for (final AssociatedStructureHolder holder : structure.getHolders()) {
			if (holder.getPerson() == null) {
				return getMessage(locale, MESSAGE_PREFIX + "NoPersonInHolder"); //$NON-NLS-1$
			}
			if (holder.getOrganization() == null) {
				return getMessage(locale, MESSAGE_PREFIX + "NoOrganizationInHolder"); //$NON-NLS-1$
			}
		}
		return null;
	}

	@Override
	public String getOrphanEntityLabel(AssociatedStructure entity, Locale locale) {
		return entity.getAcronym() + " - " + entity.getName(); //$NON-NLS-1$
	}

	@Override
	public String getOrphanTypeLabel(Locale locale) {
		return getMessage(locale, MESSAGE_PREFIX + "Name"); //$NON-NLS-1$
	}

}
