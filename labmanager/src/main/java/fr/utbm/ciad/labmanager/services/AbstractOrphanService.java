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

package fr.utbm.ciad.labmanager.services;

import java.util.Locale;
import java.util.function.Function;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.IdentifiableEntity;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.progress.Progression;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.jpa.repository.JpaRepository;

/** Abstract implementation of a Spring service for orphan entities.
 * 
 * @param <T> the type of the orphan entities.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public abstract class AbstractOrphanService<T extends IdentifiableEntity> extends AbstractService implements OrphanEntityBuilder<T> {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractOrphanService(MessageSourceAccessor messages, Constants constants) {
		super(messages, constants);
	}

	/** Create a JSON node into the receiver that describes an orphan entity.
	 *
	 * @param receiver the JSON receiver.
	 * @param editionEndpoint the name of the endpoint for editing the entity. 
	 * @param editionParameter0 the argument to pass to the edition endpoint for specifying the entity's identifier. 
	 * @param editionParameter1 a second name of argument to pass to the edition endpoint. 
	 * @param editionValue1 the value of the second name of argument to pass to the edition endpoint. 
	 * @param deletionEndpoint the endpoint for deleting the orphan entities.
	 * @param deletionParameter the parameter for specifying the entity to be deleted to the deletion endpoint.
	 * @param entity the entity to remove.
	 * @param reason the reason why the given entity is considered as orphan.
	 * @since 3.6
	 */
	protected void createOrphanJsonNode(ArrayNode receiver, String editionEndpoint, String editionParameter0,
			String editionParameter1, String editionValue1,  String deletionEndpoint, String deletionParameter,
			T entity, String reason, String label) {
		final var orphanNode = receiver.addObject();
		orphanNode.put("id", entity.getId()); //$NON-NLS-1$
		orphanNode.put("reason", reason); //$NON-NLS-1$
		orphanNode.put("label", label); //$NON-NLS-1$
		final String eendpoint;
		if (Strings.isNullOrEmpty(editionParameter1)) {
			eendpoint = endpoint(editionEndpoint, editionParameter0, getEditionParameterValue(entity));
		} else {
			eendpoint = endpoint(editionEndpoint, editionParameter0, getEditionParameterValue(entity), editionParameter1, editionValue1);
		}
		orphanNode.put("edition", eendpoint); //$NON-NLS-1$
		orphanNode.put("deletion", endpoint(deletionEndpoint, deletionParameter, getDeletionParameterValue(entity))); //$NON-NLS-1$
	}

	/** Replies the identifier that is associated to the given entity and that should be passed in the edition link.
	 *
	 * @param entity the entity for which the edition identifier should be provided.
	 * @return the edition identifier of the entity.
	 */
	protected Object getEditionParameterValue(T entity) {
		return Integer.valueOf(entity.getId());
	}

	/** Replies the identifier that is associated to the given entity and that should be passed in the deletion link.
	 *
	 * @param entity the entity for which the deletion identifier should be provided.
	 * @return the deletion identifier of the entity.
	 */
	protected Object getDeletionParameterValue(T entity) {
		return Integer.valueOf(entity.getId());
	}

	/** Compute the orphans and put them in a Json node.
	 *
	 * @param receiver the Json receiver.
	 * @param repository the repository to read.
	 * @param builder the builder of orphan entities.
	 * @param editionEndpoint the name of the endpoint for editing the entity. 
	 * @param editionArgument the argument to pass to the edition endpoint for specifying the entity's identifier. 
	 * @param deletionEndpoint the endpoint for deleting the orphan entities.
	 * @param deletionParameter the parameter for specifying the entity to be deleted to the deletion endpoint.
	 * @param locale the locale to use.
	 * @param progress the progress indicator.
	 */
	protected void computeOrphansInJson(ArrayNode receiver, JpaRepository<T, Integer> repository,
			OrphanEntityBuilder<T> builder, String editionEndpoint, String editionParameter,
			String deletionEndpoint, String deletionParameter, Locale locale, Progression progress) {
		computeOrphansInJson(receiver, repository, builder, editionEndpoint, editionParameter, null, null,
				deletionEndpoint, deletionParameter, locale, progress);
	}

	/** Compute the orphans and put them in a Json node.
	 *
	 * @param receiver the Json receiver.
	 * @param repository the repository to read.
	 * @param builder the builder of orphan entities.
	 * @param editionEndpoint the name of the endpoint for editing the entity. 
	 * @param editionParameter0 the argument to pass to the edition endpoint for specifying the entity's identifier. 
	 * @param editionParameter1 a second name of argument to pass to the edition endpoint. 
	 * @param editionValue1 the value of the second name of argument to pass to the edition endpoint. 
	 * @param deletionEndpoint the endpoint for deleting the orphan entities.
	 * @param deletionParameter the parameter for specifying the entity to be deleted to the deletion endpoint.
	 * @param locale the locale to use.
	 * @param progress the progress indicator.
	 */
	protected void computeOrphansInJson(ArrayNode receiver, JpaRepository<T, Integer> repository,
			OrphanEntityBuilder<T> builder, String editionEndpoint, String editionParameter0,
			String editionParameter1, Function<T, String> editionValue1,
			String deletionEndpoint, String deletionParameter, Locale locale, Progression progress) {
		var list = repository.findAll();
		progress.setProperties(0, 0, list.size(), false);
		for (final var entity : list) {
			final var reason = builder.getOrphanCriteria(entity, locale);
			if (!Strings.isNullOrEmpty(reason)) {
				final var label = builder.getOrphanEntityLabel(entity, locale);
				createOrphanJsonNode(receiver,
						editionEndpoint, editionParameter0,
						editionParameter1, editionValue1 == null ? null : editionValue1.apply(entity),
						deletionEndpoint, deletionParameter, entity, reason, label);
			}
			progress.increment();
		}
	}

}
