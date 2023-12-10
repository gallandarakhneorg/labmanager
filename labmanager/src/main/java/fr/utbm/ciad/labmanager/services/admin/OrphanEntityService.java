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

package fr.utbm.ciad.labmanager.services.admin;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.services.AbstractService;
import fr.utbm.ciad.labmanager.services.OrphanEntityBuilder;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import jakarta.transaction.Transactional;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.progress.DefaultProgression;
import org.arakhne.afc.progress.Progression;
import org.arakhne.afc.progress.ProgressionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** Service for managing orphan entities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Service
public class OrphanEntityService extends AbstractService {

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 */
	public OrphanEntityService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants) {
		super(messages, constants);
	}

	/** Detect the  orphan entities.
	 *
	 * @param builders the orphan detectors.
	 * @param progressListener the listener to be notified about progression of the analysis.
	 * @return the JSON node that contains the orphan entities.
	 */
	@SuppressWarnings("static-method")
	@Async
	@Transactional
	public ObjectNode detectOrphanEntities(List<? extends OrphanEntityBuilder<?>> builders, ProgressionListener progressListener) {
		final DefaultProgression progress = new DefaultProgression(0, 0, builders.size() * Constants.HUNDRED, false);
		if (progressListener != null) {
			progress.addProgressionListener(progressListener);
		}
		//
		final ObjectMapper mapper = JsonUtils.createMapper();
		final ObjectNode root = new ObjectNode(mapper.getNodeFactory());
		for (final OrphanEntityBuilder<?> builder : builders) {
			final Progression prog = progress.subTask(Constants.HUNDRED);
			detectOrphanEntities(root, builder, prog);
			prog.end();
		}
		return root;
	}

	private static void detectOrphanEntities(ObjectNode root, OrphanEntityBuilder<?> builder, Progression progress) {
		final String name = builder.getOrphanTypeLabel();
		if (!Strings.isNullOrEmpty(name)) {
			final ArrayNode orphans = root.arrayNode();
			builder.computeOrphans(orphans, progress);
			if (!orphans.isEmpty()) {
				root.set(name, orphans);
			}
		}
	}

}