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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.IdentifiableEntity;
import fr.utbm.ciad.labmanager.utils.HasAsynchronousUploadService;
import org.apache.commons.io.function.IOBiConsumer;
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
	@SuppressWarnings("resource")
	public void inSession(Consumer<Session> code) {
		Session currentSession = null;
		try {
			currentSession = this.sessionFactory.getCurrentSession();
		} catch (Throwable ex) {
			//
		}
		if (currentSession == null) {
			try (final var session = this.sessionFactory.openSession()) {
				code.accept(session);
			}
		} else {
			code.accept(currentSession);
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

		/** Replies the edited entity. This entity could be the original (first) edited entity as
		 * it is replied by {@link #getOriginalEntity()} or the entity that is provided as
		 * argument of {@link #setEntity(IdentifiableEntity)}.
		 *
		 * @return the edited entity.
		 * @see #getOriginalEntity()
		 * @see #setEntity(IdentifiableEntity)
		 */
		T getEntity();

		/** Replies the original edited entity. It is the entity that is initially provided as editable.
		 * The value replied by this function is not changed by a call to {@link #setEntity(IdentifiableEntity)}.
		 * To obtain the argument of this call, you must invoke {@link #getEntity()}.
		 *
		 * @return the original edited entity.
		 * @see #getEntity()
		 * @see #setEntity(IdentifiableEntity)
		 */
		T getOriginalEntity();

		/** Change the edited entity. This function changes the value replied by {@link #getEntity()}, but not
		 * those replied by {@link #getOriginalEntity()}.
		 *
		 * @param entity the new edited entity.
		 * @see #getEntity()
		 * @see #getOriginalEntity()
		 */
		void setEntity(T entity);

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
		private final T originalEntity;

		/** The edited entity.
		 */
		protected T entity;

		/** Constructor.
		 *
		 * @param entity the entity to be edited.
		 */
		protected AbstractEntityEditingContext(T entity) {
			this.originalEntity = entity;
			this.entity = entity;
		}

		@Override
		public T getEntity() {
			return this.entity;
		}
	
		@Override
		public T getOriginalEntity() {
			return this.originalEntity;
		}

		@Override
		public void setEntity(T entity) {
			this.entity = entity;
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
				protected void deleteEntities(Collection<Long> identifiers) throws Exception {
					// Nothing to do because the entity was not saved in the JPA infrastructure.
				}
				
			};
		}

	}
	
	/** Representation of an uploaded file during the edition process.
	 *
	 * @param <T> the type of the publication.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class UploadedFileTracker<T extends IdentifiableEntity> implements Serializable {

		private static final long serialVersionUID = 1872347201559398676L;

		private final Function<T, String> reader;

		private final IOBiConsumer<Long, String> deleter;

		private final IOBiConsumer<Long, Long> renamer;

		private String savedPath;
		
		/** Construction.
		 *
		 * @param publication publication to be associated to.
		 * @param reader the reading lambda for obtaining the filename.
		 * @param deleter the lambda for deleting the associated file.
		 * @param renamer the lambda for renaming the associated file.
		 */
		public UploadedFileTracker(T publication, Function<T, String> reader, IOBiConsumer<Long, String> deleter, IOBiConsumer<Long, Long> renamer) {
			this.reader = reader;
			this.deleter = deleter;
			this.renamer = renamer;
			this.savedPath = reader.apply(publication);
		}

		/** Reset the path memory to keep track of changes.
		 *
		 * @param entity entity to read for reseting.
		 */
		public void resetPathMemory(T entity) {
			this.savedPath = this.reader.apply(entity);
		}

		/** Replies if the path to the file has changed since the last saving.
		 *
		 * @param entity entity to read for reseting.
		 * @return {@code true} if the path has changed.
		 */
		public boolean isPathChanged(T entity) {
			return !Objects.equals(this.savedPath, this.reader.apply(entity));
		}

		/** Delete the associated file.
		 *
		 * @param entity entity to read for deletion.
		 * @return {@code true} if the file is deleted, or {@code false} if no file is deleted.
		 * @throws IOException is thrown when the associated file cannot be deleted.
		 */
		public boolean deleteFile(T entity) throws IOException {
			final var currentPath = this.reader.apply(entity);
			if (Strings.isNullOrEmpty(currentPath)) {
				if (this.deleter != null) {
					this.deleter.accept(Long.valueOf(entity.getId()), this.savedPath);
				}
				return true;
			}
			return false;
		}

		/** Delete or rename the associated file to be consistznt with the
		 * new identifier of the associated entity.
		 *
		 * @param oldId the previous value of the entity's identifier. It it is {@code 0} then the entity was never created in the database. 
		 * @param entity the JPE entity to be tracked.
		 * @throws IOException is thrown when the associated file cannot be deleted or renamed.
		 */
		public void deleteOrRenameFile(long oldId, T entity) throws IOException {
			assert oldId != entity.getId();
			if (oldId != 0) {
				final var currentPath = this.reader.apply(entity);
				if (Strings.isNullOrEmpty(currentPath)) {
					if (this.deleter != null) {
						this.deleter.accept(Long.valueOf(oldId), this.savedPath);
					}
				} else if (this.renamer != null) {
					this.renamer.accept(Long.valueOf(oldId), Long.valueOf(entity.getId()));
				}
			}
		}
		
	}

	/** Representation of a list of uploaded files during the edition process.
	 *
	 * @param <T> the type of the publication.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class UploadedFilesTracker<T extends IdentifiableEntity> implements Serializable {

		private static final long serialVersionUID = -775887678637997587L;

		private final Function<T, List<String>> reader;

		private final IOBiConsumer<Long, List<String>> deleter;

		private final IOBiConsumer<Long, Long> renamer;

		private List<String> savedPaths;
		
		/** Construction.
		 *
		 * @param publication publication to be associated to.
		 * @param reader the reading lambda for obtaining the filenames.
		 * @param deleter the lambda for deleting the associated files.
		 * @param renamer the lambda for renaming the associated files.
		 */
		public UploadedFilesTracker(T publication, Function<T, List<String>> reader, IOBiConsumer<Long, List<String>> deleter, IOBiConsumer<Long, Long> renamer) {
			this.reader = reader;
			this.deleter = deleter;
			this.renamer = renamer;
			this.savedPaths = reader.apply(publication);
		}

		/** Reset the path memory to keep track of changes.
		 *
		 * @param entity entity to read for reseting.
		 */
		public void resetPathMemory(T entity) {
			this.savedPaths = this.reader.apply(entity);
		}

		/** Replies if the path to the file has changed since the last saving.
		 *
		 * @param entity entity to read for reseting.
		 * @return {@code true} if the path has changed.
		 */
		public boolean isPathChanged(T entity) {
			return !Objects.equals(this.savedPaths, this.reader.apply(entity));
		}

		/** Delete the associated files.
		 *
		 * @param entity entity to read for deletion.
		 * @return {@code true} if the files are deleted, or {@code false} if no file is deleted.
		 * @throws IOException is thrown when an associated file cannot be deleted.
		 */
		public boolean deleteFiles(T entity) throws IOException {
			final var currentPaths = this.reader.apply(entity);
			if (currentPaths == null || currentPaths.isEmpty()) {
				if (this.deleter != null) {
					this.deleter.accept(Long.valueOf(entity.getId()), this.savedPaths);
				}
				return true;
			}
			return false;
		}

		/** Delete or rename the associated files to be consistent with the
		 * new identifier of the associated entity.
		 *
		 * @param oldId the previous value of the entity's identifier. It it is {@code 0} then the entity was never created in the database. 
		 * @param entity the JPE entity to be tracked.
		 * @throws IOException is thrown when an associated file cannot be deleted or renamed.
		 */
		public void deleteOrRenameFiles(long oldId, T entity) throws IOException {
			assert oldId != entity.getId();
			if (oldId != 0) {
				final var currentPaths = this.reader.apply(entity);
				if (currentPaths == null || currentPaths.isEmpty()) {
					if (this.deleter != null) {
						this.deleter.accept(Long.valueOf(oldId), this.savedPaths);
					}
				} else if (this.renamer != null) {
					this.renamer.accept(Long.valueOf(oldId), Long.valueOf(entity.getId()));
				}
			}
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
		
		/** Create a tracker for an uploaded file.
		 *
		 * @param entity the entity to track.
		 * @param reader the reading lambda for obtaining the filename. 
		 * @param deleter the lambda for deleting the associated file.
		 * @param renamer the lambda for renaming the associated file.
		 * @return the tracker. 
		 */
		protected UploadedFileTracker<T> newUploadedFileTracker(T entity, Function<T, String> reader, IOBiConsumer<Long, String> deleter, IOBiConsumer<Long, Long> renamer) {
			return new UploadedFileTracker<>(entity, reader, deleter, renamer);
		}

		/** Create a tracker for an uploaded file.
		 *
		 * @param entity the entity to track.
		 * @param reader the reading lambda for obtaining the filename. 
		 * @param deleter the lambda for deleting the associated file.
		 * @param renamer the lambda for renaming the associated file.
		 * @return the tracker. 
		 */
		protected UploadedFilesTracker<T> newUploadedFilesTracker(T entity, Function<T, List<String>> reader, IOBiConsumer<Long, List<String>> deleter, IOBiConsumer<Long, Long> renamer) {
			return new UploadedFilesTracker<>(entity, reader, deleter, renamer);
		}

		@Override
		public final void save(HasAsynchronousUploadService... components) throws IOException {
			this.entity = writeInJPA(this.entity, true);

			if (this.id != this.entity.getId()) {
				// The id of the entity has changed.
				
				// Therefore, the associated files may be not correctly named
				deleteOrRenameAssociatedFiles(this.id);
				
				// If there is any uploaded file (new file content), they should be updated also.
				var changed = false;
				for (final var component : components) {
					component.updateValue();
					changed = true;
				}

				// Save the changes of the attributes
				if (changed) {
					this.entity = writeInJPA(this.entity, false);
				}
			}

			// Upload the new file on the server
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

		/** Delete or move all the associated files to the entity with the given identifier.
		 * The files are deleted when the new entity not has the path in the fields.
		 * The files are moved when the new entity has the path in the fields.
		 *
		 * @param oldId the identifier of the entity before change.
		 * @throws IOException the error if an associated file cannot be deleted or renamed.
		 */
		protected abstract void deleteOrRenameAssociatedFiles(long oldId) throws IOException;

		/** Invoked just before the associated files are uploaded for preparing the server
		 * to receive them. For example, the old files are removed when their is no file to
		 * be uploaded and the associated path is empty.
		 *
		 * @return {@code true} if at least one file should be uploaded from the client to the server.
		 * @throws IOException the error if an associated file cannot be updated.
		 */
		protected abstract boolean prepareAssociatedFileUpload() throws IOException;

		/** Invoked just after the associated files are uploaded.
		 *
		 * @throws IOException the error if an associated file cannot be updated.
		 */
		protected abstract void postProcessAssociatedFiles() throws IOException;

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
		protected List<Long> getDeletableEntityIdentifiers() {
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
			final var identifiers = getDeletableEntityIdentifiers();
			deleteEntities(identifiers);
		}

		/** Do the deletion of the entities.
		 *
		 * @param identifiers the identifiers of the entities to be deleted.
		 * @throws Exception if organization or associated files cannot be deleted from the server.
		 */
		protected abstract void deleteEntities(Collection<Long> identifiers) throws Exception;

	}

}
