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

package fr.utbm.ciad.labmanager.views.components.addons.download;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

/** Extension of a component that is clickable in order to start asynchronous download of a file from the client browser.
 *
 * <p>This class is inspired from the {@code lazy-download-button} maven plugin from {@code org.vaadin.stefan} group, that is adapted to be an extension for any clickable component.
 *
 * @param <C> the type of the extended component that must be a valid Vaadin component and a received of click event.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class LazyDownloadExtension<C extends Component & ClickNotifier<C>> implements Serializable {

	private static final long serialVersionUID = -1776991760046757518L;

    private static final String DEFAULT_FILE_NAME = "download"; //$NON-NLS-1$

    private static final Supplier<String> DEFAULT_FILE_NAME_SUPPLIER = () -> DEFAULT_FILE_NAME;

    private final C component;

    private Anchor anchor;

	private Function<C, Optional<Component>> anchorReceiverSupplier;

	private Supplier<String> fileNameSupplier = DEFAULT_FILE_NAME_SUPPLIER;
	
	private InputStreamFactory inputStreamFactory;

	private Supplier<StreamResource> streamResourceSupplier;

	private Consumer<Throwable> errorNotifier;

	/** Constructor.
	 * 
	 * @param component the component to extend.
	 */
	protected LazyDownloadExtension(C component) {
		this.component = component;
	}
	
	/** Extends the given component to enables asynchronous download of a file.
	 *
	 * @param <C> the type of the component that must be a valid Vaadin component and a received of click event.
	 * @param component the component to extend.
	 * @return the extension.
	 */
	public static <C extends Component & ClickNotifier<C>> LazyDownloadExtension<C> extend(C component) {
		return new LazyDownloadExtension<>(component);
	}

	/** Change the supplier of stream resrouce. A call to this function will cancel any previous call to {@link #withFilenameSupplier(Supplier)} and
	 * {@link #withInputStreamFactory(Supplier)}.
	 *
	 * @param supplier the supplier. A lambda that replies the stream resource of the content of the downloadable file.
	 * @return {@code this}
	 */
	public LazyDownloadExtension<C> withStreamResource(Supplier<StreamResource> supplier) {
		this.streamResourceSupplier = supplier;
		return this;
	}

	/** Change the supplier of filename for the downloadable file.
	 *
	 * @param supplier the supplier. A lambda that replies the filename. If this argument is {@code null}, the default supplier is used.
	 * @return {@code this}
	 */
	public LazyDownloadExtension<C> withFilename(Supplier<String> supplier) {
		this.fileNameSupplier = supplier == null ? DEFAULT_FILE_NAME_SUPPLIER : supplier;
		return this;
	}

	/** Change the error notifier that should be used by the extension.
	 *
	 * @param notifier the error notifier. A lambda that takes the error as argument.
	 * @return {@code this}
	 */
	public LazyDownloadExtension<C> withErrorNotifier(Consumer<Throwable> notifier) {
		this.errorNotifier = notifier;
		return this;
	}

	/** Change the factory for the input stream of the content of the downloadable file.
	 *
	 * @param factory the object that is able to create the input stream to the client.
	 * @return {@code this}
	 */
	public LazyDownloadExtension<C> withInputStreamFactory(InputStreamFactory2 factory) {
		this.inputStreamFactory = () -> {
			try {
				return factory.createInputStream();
			} catch (Throwable ex) {
				if (ex instanceof RuntimeException ex2) {
					throw ex2;
				}
				throw new RuntimeException(ex);
			}
		};
		return this;
	}

	/** Change the supplier of the component that will receive the anchor component for supporting the download process.
	 * By default, the receiver of the anchor is obtaining with {@code getParent()} on the extended component.
	 *
	 * @param supplier the supplier of the anchor receiver. It is a lambda with the extended component as argument and that should reply the receiver of the anchor.
	 * @return {@code this}
	 */
	public LazyDownloadExtension<C> withAnchorReceiver(Function<C, Optional<Component>> supplier) {
		this.anchorReceiverSupplier = supplier;
		return this;
	}
	
	/** Replies the receiver of the anchor.
	 *
	 * @return the receiver component.
	 */
	public Optional<Component> getAnchorReceiver() {
		if (this.anchorReceiverSupplier != null) {
			final var opt = this.anchorReceiverSupplier.apply(this.component);
			if (opt != null) {
				return opt;
			}
		}
		return this.component.getParent();
	}

	/** Invoked when the extended component is detached and for removing the extension.
	 */
	protected void onDetachExtendedComponent() {
		final Anchor anch;
		synchronized (this) {
			anch = this.anchor;
		}
        if (anch != null) {
        	anch.removeFromParent();
        }
	}

	/** Start the download process
	 */
	@SuppressWarnings("resource")
	protected void doFileDownload() {
		final var parent = getAnchorReceiver();
		if (parent.isPresent()) {
			final var parentComponent = parent.get();
            final var optionalUI = parentComponent.getUI();
            if (optionalUI.isPresent()) {
            	final var ui = optionalUI.get();
                try {
                	final StreamResource href;
                    if (this.streamResourceSupplier != null) {
                        href = this.streamResourceSupplier.get();
                    } else {
						final var inputStream = this.inputStreamFactory.createInputStream();
						if (inputStream == null) {
							// Do nothing because the stream factory does not create a stream. The error is logged by the stream factory if there is some error.
							return;
						}
						href = new StreamResource(this.fileNameSupplier.get(), () -> inputStream);
                    }
                    href.setCacheTime(0);
                    final Anchor anch;
    				synchronized (this) {
    		            if (this.anchor == null) {
    		            	this.anchor = new Anchor();
    		                final var anchorElement = this.anchor.getElement();
    		                anchorElement.setAttribute("download", true); //$NON-NLS-1$
    		                anchorElement.getStyle().set("display", "none"); //$NON-NLS-1$ //$NON-NLS-2$
    		                parentComponent.getElement().appendChild(anchorElement);
    		            }
                    	anch = this.anchor;
    				}
                    ui.access(() -> {
                        anch.setHref(href);
                        anch.getElement().callJsFunction("click"); //$NON-NLS-1$
                    });
                } catch (Throwable ex) {
                	if (this.errorNotifier == null) {
                		if (ex instanceof RuntimeException rex) {
                			throw rex;
                		}
                		throw new RuntimeException(ex);
                	}
                	this.errorNotifier.accept(ex);
                }
            }
		}
	}

	/** Do the binding to the extended component.
	 */
	public void bind() {
		this.component.addDetachListener(it -> onDetachExtendedComponent());
		this.component.addClickListener(it -> doFileDownload());
	}

}
