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

package fr.ciadlab.labmanager.service;

import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
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
		final ObjectNode orphanNode = receiver.addObject();
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
		/*try {
			final JsonNode node = JsonUtils.getJsonNode(entity);
			orphanNode.set("entity", node); //$NON-NLS-1$
		} catch (Throwable ex) {
			getLogger().warn(ex.getLocalizedMessage(), ex);
		}*/
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
	 * @param progress the progress indicator.
	 */
	protected void computeOrphansInJson(ArrayNode receiver, JpaRepository<T, Integer> repository,
			OrphanEntityBuilder<T> builder, String editionEndpoint, String editionParameter,
			String deletionEndpoint, String deletionParameter, Progression progress) {
		computeOrphansInJson(receiver, repository, builder, editionEndpoint, editionParameter, null, null,
				deletionEndpoint, deletionParameter, progress);
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
	 * @param progress the progress indicator.
	 */
	protected void computeOrphansInJson(ArrayNode receiver, JpaRepository<T, Integer> repository,
			OrphanEntityBuilder<T> builder, String editionEndpoint, String editionParameter0,
			String editionParameter1, Function<T, String> editionValue1,
			String deletionEndpoint, String deletionParameter, Progression progress) {
		List<T> list = repository.findAll();
		progress.setProperties(0, 0, list.size(), false);
		for (final T entity : list) {
			final String reason = builder.getOrphanCriteria(entity);
			if (!Strings.isNullOrEmpty(reason)) {
				final String label = builder.getOrphanEntityLabel(entity);
				createOrphanJsonNode(receiver,
						editionEndpoint, editionParameter0,
						editionParameter1, editionValue1 == null ? null : editionValue1.apply(entity),
						deletionEndpoint, deletionParameter, entity, reason, label);
			}
			progress.increment();
		}
	}

}
