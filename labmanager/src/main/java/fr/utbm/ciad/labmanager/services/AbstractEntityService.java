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

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.IdentifiableEntity;
import fr.utbm.ciad.labmanager.utils.HasAsynchronousUploadService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.transaction.annotation.Transactional;

/** Abstract implementation of a Spring boot service.
 * 
 * @param <T> the type of the entity to be deleted.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractEntityService<T extends IdentifiableEntity> extends AbstractService {


	private final SessionFactory sessionFactory;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param sessionFactory the factory of JPA session.
	 */
	public AbstractEntityService(MessageSourceAccessor messages, Constants constants, SessionFactory sessionFactory) {
		super(messages, constants);
		this.sessionFactory = sessionFactory;
	}

	/** Run the provided code in a JPA session.
	 *
	 * @param code the code to run. It takes the session as argument.
	 * @since 4.0
	 */
	public void inSession(Consumer<Session> code) {
		try (final var session = this.sessionFactory.openSession()) {
			code.accept(session);
		}
	}

	/** Start the editing of the given entity.
	 *
	 * @param entity the entity to save.
	 * @return the editing context that enables to keep track of any information needed
	 *      for saving the entity and its related resources.
	 */
	public abstract EntityEditingContext<T> startEditing(T entity);

	/** Start the deletion of the given entities.
	 *
	 * @param entities the entities to delete.
	 * @return the deletion context that enables to keep track of any information needed
	 *      for deleting the entity and its related resources.
	 */
	public abstract EntityDeletingContext<T> startDeletion(Set<T> entities);

	/** Context for editing an entity.
	 *
	 * @param <T> the type of the entity to be edited.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public interface EntityEditingContext<T extends IdentifiableEntity> extends Serializable {

		/** Replies the edited entity.
		 *
		 * @return the edited entity.
		 */
		T getEntity();

		/** Save the entity in the JPA database.
		 *
		 * <p>After calling this function, it is preferable to not use
		 * the entity object that was provided before the saving.
		 * Invoke {@link #getEntity()} for obtaining the new entity
		 * instance, since the content of the saved object may have totally changed.
		 *
		 * @param components list of components to update if the service detects an inconsistent value.
		 * @throws IOException if files cannot be saved on the server.
		 */
		@Transactional
		void save(HasAsynchronousUploadService... components) throws IOException;

		/** Create a deletion context from the entity that is edited. This context is usually used for deleting the entity that is currently edited.
		 *
		 * @return the deletion context.
		 */
		EntityDeletingContext<T> startDeletion();
		
		/** Create a deletion context from the entity that is edited. This context is usually used for deleting the entity that is currently edited.
		 *
		 * @return the deletion context, never {@code null}.
		 */
		EntityDeletingContext<T> createDeletionContext();

	}

	/** Context for editing an entity.
	 *
	 * @param <T> the type of the entity to be edited.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public abstract static class AbstractEntityEditingContext<T extends IdentifiableEntity> implements EntityEditingContext<T> {

		private static final long serialVersionUID = -7350966100644929293L;

		/** The edited entity.
		 */
		protected T entity;

		/** Constructor.
		 *
		 * @param entity the entity to be edited.
		 */
		protected AbstractEntityEditingContext(T entity) {
			this.entity = entity;
		}

		@Override
		public T getEntity() {
			return this.entity;
		}

		@Override
		@Transactional
		public abstract void save(HasAsynchronousUploadService... components) throws IOException;

		@Override
		public final EntityDeletingContext<T> startDeletion() {
			final var entity = this.entity;
			if (entity != null && entity.getId() != 0l) {
				return createDeletionContext();
			}
			return new AbstractEntityDeletingContext<>(Collections.singleton(entity)) {

				private static final long serialVersionUID = 2393794876950796791L;

				@Override
				protected void deleteEntities() throws Exception {
					// Nothing to do because the entity was not saved in the JPA infrastructure.
				}
				
			};
		}

	}

	/** Context for editing an entity that has also associated files on the server file system.
	 *
	 * @param <T> the type of the entity to be edited.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public abstract static class AbstractEntityWithServerFilesEditingContext<T extends IdentifiableEntity> extends AbstractEntityEditingContext<T> {

		private static final long serialVersionUID = -3495386730939748318L;

		private long id;

		/** Constructor.
		 *
		 * @param entity the entity to be edited.
		 */
		protected AbstractEntityWithServerFilesEditingContext(T entity) {
			super(entity);
			this.id = entity.getId();
		}

		@Override
		public final void save(HasAsynchronousUploadService... components) throws IOException {
			this.entity = writeInJPA(this.entity, true);
			// Save the uploaded file if needed.
			if (this.id != this.entity.getId()) {
				// Special case where the field value does not corresponds to the correct path
				deleteAssociatedFiles(this.id);
				for (final var component : components) {
					component.updateValue();
				}
				this.entity = writeInJPA(this.entity, false);
			}
			// Standard saving process
			final var hasUploadedFile = prepareAssociatedFileUpload();
			if (hasUploadedFile) {
				for (final var component : components) {
					component.saveUploadedFileOnServer();
				}
			}
			this.id = this.entity.getId();
			postProcessAssociatedFiles();
		}

		/** Invoked to save the given entity in the JPA infrastructure.
		 *
		 * @param entity the entity to save.
		 * @param initialSaving indicates if the call to this function is the function in the global processing of saving. This function mayt be called multiple times.
		 * @return the new instance of the saved entity.
		 */
		protected abstract T writeInJPA(T entity, boolean initialSaving);

		/** Delete all the associated files to the entity with the given identifier.
		 *
		 * @param id the identifier of the entity.
		 */
		protected abstract void deleteAssociatedFiles(long id);

		/** Invoked just before the associated files are uploaded for preparing the server
		 * to receive them. For example, the old files are removed when their is no file to
		 * be uploaded and the associated path is empty.
		 *
		 * @return {@code true} if at least one file should be uploaded from the client to the server.
		 */
		protected abstract boolean prepareAssociatedFileUpload();

		/** Invoked just after the associated files are uploaded.
		 */
		protected abstract void postProcessAssociatedFiles();

	}

	/** Context for deleting an entity.
	 *
	 * @param <T> the type of the entity to be deleted.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public interface EntityDeletingContext<T extends IdentifiableEntity> extends Serializable {

		/** Replies the entities to be deleted.
		 *
		 * @return the entities.
		 */
		Set<T> getEntities();

		/** Indicates if the deletion is possible without error.
		 * If this function replies {@code false}, then a call to
		 * {@code #delete(HasAsynchronousUploadService...)} should throw
		 * an acception, and the reason of the deletion rejection could
		 * be obtained by calling {@link #getDeletionStatus()}.
		 *
		 * @return {@code true} if deletion is possible; {@code false}
		 *      if deletion is not possible.
		 * @see #delete(HasAsynchronousUploadService...)
		 * @see #getDeletionStatus()
		 */
		boolean isDeletionPossible();

		/** Replies the reason of the deletion rejection.
		 * See the documentation of {@link #delete(HasAsynchronousUploadService...)}
		 * for obtaining the possible constraints for deletion of an organization.
		 *
		 * @return the reason of the deletion rejection, or {@code null} if deletion is possible.
		 * @see #isDeletionPossible()
		 * @see #delete(HasAsynchronousUploadService...)
		 */
		DeletionStatus getDeletionStatus();

		/** Delete the organization in the JPA database.
		 * Conditions for deletion are: <ol>
		 * <li>Organization has no linked suborganization;</li>
		 * <li>Organization has no linked membership.</li>
		 * </ol>
		 *
		 * <p>After calling this function, it is preferable to not use
		 * the organization object any more.
		 *
		 * @throws Exception if organization or associated files cannot be deleted from the server.
		 * @see #isDeletionPossible()
		 * @see #getDeletionStatus()
		 */
		@Transactional
		void delete() throws Exception;

	}

	/** Context for deleting an entity.
	 *
	 * @param <T> the type of the entity to be deleted.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public abstract static class AbstractEntityDeletingContext<T extends IdentifiableEntity> implements EntityDeletingContext<T> {

		private static final long serialVersionUID = -7909548992030372794L;

		private Set<T> entities;

		private DeletionStatus deletionStatus;

		/** Constructor.
		 *
		 * @param entities the entities to delete.
		 */
		protected AbstractEntityDeletingContext(Set<T> entities) {
			this.entities = entities;
		}

		@Override
		public Set<T> getEntities() {
			return this.entities;
		}

		/** Replies the list of the identifiers that shoçuld be deleted.
		 *
		 * @return the list of identifiers for deletable entities.
		 */
		protected Iterable<Long> getDeletableEntityIdentifiers() {
			return this.entities.stream().map(it -> Long.valueOf(it.getId())).toList();
		}

		@Override
		public boolean isDeletionPossible() {
			checkDeletionStatus();
			return this.deletionStatus.isOk(); 
		}

		@Override
		public DeletionStatus getDeletionStatus() {
			checkDeletionStatus();
			return this.deletionStatus;
		}

		private void checkDeletionStatus() {
			if (this.deletionStatus == null) {
				this.deletionStatus = computeDeletionStatus();
			}
		}

		/** Compute the deletion status for the given entities to be deleted.
		 *
		 * @return the new deletion status.
		 */
		@SuppressWarnings("static-method")
		protected DeletionStatus computeDeletionStatus() {
			return DeletionStatus.OK;
		}

		@Override
		@Transactional
		public void delete() throws Exception {
			if (!isDeletionPossible()) {
				throw new IllegalStateException();
			}
			deleteEntities();
		}

		/** Do the deletion of the entities.
		 *
		 * @param components list of components to update if the service detects an inconsistent value.
		 * @throws Exception if organization or associated files cannot be deleted from the server.
		 */
		protected abstract void deleteEntities() throws Exception;

	}

}
