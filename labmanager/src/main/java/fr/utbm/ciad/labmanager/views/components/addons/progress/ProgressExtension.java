/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * Copyright (c) 2019 Kaspar Scherrer
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

package fr.utbm.ciad.labmanager.views.components.addons.progress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.base.Strings;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.AbstractIcon;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;
import org.arakhne.afc.progress.ProgressionListener;

/** Extension of a component that is clickable in order to start asynchronous a task and show its progress in a modal dialog.
 *
 * @param <T> the type of the result computed by the asynchronous task.
 * @param <C> the type of the extended component that must be a valid Vaadin component and a received of click event.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class ProgressExtension<T, C extends Component & ClickNotifier<C>> implements Serializable {

	private static final long serialVersionUID = 7038667406237972422L;

	private static final String DEFAULT_ICON_WIDTH = "256px"; //$NON-NLS-1$

	private final C component;

	private final List<SerializableConsumer<T>> successListeners = new ArrayList<>();

	private final List<SerializableConsumer<Throwable>> failureListeners = new ArrayList<>();

	private final List<SerializableConsumer<Throwable>> cancellationListeners = new ArrayList<>();

	private Component progressIcon;

	private String progressTitle;

	private SerializableFunction<ProgressionListener, CompletableFuture<T>> taskProvider;
	
	private ProgressDialog<T> dialog;

	private Registration clickListener;

	/** Constructor.
	 * 
	 * @param component the component to extend.
	 */
	protected ProgressExtension(C component) {
		this.component = component;
		registerClickListener(false);
		
		// Reset the dialog reference for enabling the starting of another task
		this.successListeners.add(it -> onTaskFinished());
		this.failureListeners.add(it -> onTaskFinished());
		this.cancellationListeners.add(it -> onTaskFinished());
	}

	private void registerClickListener(boolean inUi) {
		if (this.clickListener == null) {
			if (inUi) {
				final var ui = this.component.getUI().orElseThrow();
				ui.access(() -> this.clickListener = this.component.addClickListener(it -> startTaskAsynchronously()));
			} else {
				this.clickListener = this.component.addClickListener(it -> startTaskAsynchronously());
			}
		}
	}

	/** Replies the linked component.
	 *
	 * @return the linked component.
	 */
	public C getComponent() {
		return this.component;
	}
	
	/** Replies the progress dialog.
	 *
	 * @return the progress dialog.
	 */
	public synchronized ProgressDialog<T> getDialog() {
		return this.dialog;
	}

	/** Change the task creator. The provided task is not run on a parallel thread.
	 * 
	 * @param taskProvider the provider of the task. The argument is the listener on progression, and the returned value must be a completable future for the task.
	 * @return {@code this}.
	 * @see #withAsyncTask(SerializableFunction)
	 */
	public ProgressExtension<T, C> withTask(SerializableFunction<ProgressionListener, CompletableFuture<T>> taskProvider) {
		this.taskProvider = taskProvider;
		return this;
	}

	/** Change the task creator. The provided task is run on a parallel thread.
	 * 
	 * @param task the provider of the asynchronous task. The argument is the listener on progression.
	 * @return {@code this}.
	 * @see #withTask(SerializableFunction)
	 */
	public ProgressExtension<T, C> withAsyncTask(SerializableFunction<ProgressionListener, T> task) {
		return withTask(it -> CompletableFuture.supplyAsync(() -> task.apply(it)));
	}

	/** Invoked when the asynchronous task is finished.
	 */
	protected synchronized void onTaskFinished() {
		if (this.dialog != null) {
			this.dialog.removeFromParent();
			this.dialog = null;
		}
		registerClickListener(true);
	}

	/** Extends the given component to enables asynchronous run of a task.
	 *
	 * @param <T> the type of the result computed by the asynchronous task.
	 * @param <C> the type of the component that must be a valid Vaadin component and a received of click event.
	 * @param component the component to extend.
	 * @return the extension.
	 */
	public static <T, C extends Component & ClickNotifier<C>> ProgressExtension<T, C> extend(C component) {
		return new ProgressExtension<>(component);
	}

	private static String toSizeWidth(float size, Unit unit) {
		return new StringBuilder().append(size).append(unit.getSymbol()).toString();
	}

	private static String ensureWidth(String width) {
		if (Strings.isNullOrEmpty(width)) {
			return DEFAULT_ICON_WIDTH;
		}
		return width;
	}

	/** Change the icon of the progress window.
	 *
	 * @param icon the icon component.
	 * @return {@code this}.
	 */
	public <CC extends Component & HasSize> ProgressExtension<T, C> withProgressIcon(CC icon) {
		return withProgressIcon(icon, null);
	}

	/** Change the icon of the progress window.
	 *
	 * @param icon the icon component.
	 * @param minWidth the minimal width of the icon.
	 * @param unit the unit of the given {@code minWidth}.
	 * @return {@code this}.
	 */
	public <CC extends Component & HasSize> ProgressExtension<T, C> withProgressIcon(CC icon, float minWidth, Unit unit) {
		return withProgressIcon(icon, toSizeWidth(minWidth, unit));
	}

	/** Change the icon of the progress window.
	 *
	 * @param icon the icon component.
	 * @param minWidth the minimal width of the icon.
	 * @return {@code this}.
	 */
	public <CC extends Component & HasSize> ProgressExtension<T, C> withProgressIcon(CC icon, String minWidth) {
		this.progressIcon = icon;
		if (icon != null) {
			icon.setMinWidth(ensureWidth(minWidth));
		}
		return this;
	}

	/** Change the icon of the progress window.
	 *
	 * @param icon the icon component.
	 * @return {@code this}.
	 */
	public ProgressExtension<T, C> withProgressIcon(AbstractIcon<?> icon) {
		return withProgressIcon(icon, null);
	}

	/** Change the icon of the progress window.
	 *
	 * @param icon the icon component.
	 * @param minWidth the minimal width of the icon.
	 * @param unit the unit of the given {@code minWidth}.
	 * @return {@code this}.
	 */
	public ProgressExtension<T, C> withProgressIcon(AbstractIcon<?> icon, float minWidth, Unit unit) {
		return withProgressIcon(icon, toSizeWidth(minWidth, unit));
	}

	/** Change the icon of the progress window.
	 *
	 * @param icon the icon component.
	 * @param minWidth the minimal width of the icon.
	 * @return {@code this}.
	 */
	public ProgressExtension<T, C> withProgressIcon(AbstractIcon<?> icon, String minWidth) {
		this.progressIcon = icon;
		if (icon != null) {
			icon.setSize(ensureWidth(minWidth));
		}
		return this;
	}

	/** Change the title of the progress window.
	 *
	 * @param title the title.
	 * @return {@code this}.
	 */
	public ProgressExtension<T, C> withProgressTitle(String title) {
		this.progressTitle = title;
		return this;
	}

	/** Add a listener on the success completion of the task.
	 *
	 * @param listener the lambda function that must be invoked when the task is successfully terminated.
	 * @return {@code this}.
	 */
	public ProgressExtension<T, C> withSuccessListener(SerializableConsumer<T> listener) {
		this.successListeners.add(listener);
		return this;
	}

	/** Add a listener on the error completion of the task.
	 *
	 * @param listener the lambda function that must be invoked when the task has failed.
	 * @return {@code this}.
	 */
	public ProgressExtension<T, C> withFailureListener(SerializableConsumer<Throwable> listener) {
		this.failureListeners.add(listener);
		return this;
	}

	/** Add a listener on the cancellation of the task.
	 *
	 * @param listener the lambda function that must be invoked when the task has failed.
	 * @return {@code this}.
	 */
	public ProgressExtension<T, C> withCancellationListener(SerializableConsumer<Throwable> listener) {
		this.cancellationListeners.add(listener);
		return this;
	}

	/** Start the asynchronous task.
	 */
	protected synchronized void startTaskAsynchronously() {
		if (this.clickListener != null) {
			this.clickListener.remove();
			this.clickListener = null;
		}
		if (this.dialog == null) {
			this.dialog = new ProgressDialog<>(this.progressIcon, this.progressTitle, null, this.taskProvider);
			for (final var listener : this.successListeners) {
				this.dialog.addSuccessListener(listener);
			}
			for (final var listener : this.failureListeners) {
				this.dialog.addFailureListener(listener);
			}
			for (final var listener : this.cancellationListeners) {
				this.dialog.addCancellationListener(listener);
			}
			this.dialog.openAndRun();
		}
	}

}
