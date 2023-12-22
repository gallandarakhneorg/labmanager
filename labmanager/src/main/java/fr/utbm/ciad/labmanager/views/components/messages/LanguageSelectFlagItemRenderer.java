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

package fr.utbm.ciad.labmanager.views.components.messages;

import java.lang.ref.WeakReference;
import java.util.Locale;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.server.StreamResource;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;

/** Standard item renderer for the language select.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class LanguageSelectFlagItemRenderer implements SerializableFunction<Locale, Component> {

	private static final long serialVersionUID = -2304075478884549020L;

	private final WeakReference<LanguageSelect> parent;

	/** Constructor.
	 *
	 * @param parent the associated component.
	 */
	public LanguageSelectFlagItemRenderer(LanguageSelect parent) {
		this.parent = new WeakReference<>(parent);
	}

	/** Replies the resource corresponding to the language flag.
	 *
	 * @param parent the parent component.
	 * @param lang the code of the language.
	 * @return the flag icon resource.
	 */
	@SuppressWarnings("static-method")
	protected StreamResource getLanguageFlag(LanguageSelect parent, String lang) {
		return ComponentFactory.newStreamImage(
				new StringBuilder().append(parent.getFlagPath())
				.append(lang)
				.append(parent.getFlagExtension()).toString());
	}
	
	/** Replies the name of the language.
	 *
	 * @param parent the parent component.
	 * @param lang the code of the language.
	 * @return the language name.
	 */
	@SuppressWarnings("static-method")
	protected String getLanguageName(LanguageSelect parent, String lang) {
		final var key = new StringBuilder().append(parent.getFlagTranslationKeyPrefix()).append(lang).toString();
		return parent.getTranslation(key);
	}

	@Override
	public Component apply(Locale item) {
		final var wrapper = new FlexLayout();
	    wrapper.setAlignItems(Alignment.CENTER);
		final var parent = this.parent.get();
		if (parent != null) {
			final var lang = item.getLanguage().toLowerCase();
			final var flag = new Image(getLanguageFlag(parent, lang), ""); //$NON-NLS-1$
			flag.addClassName("language-select-flag"); //$NON-NLS-1$
			flag.setWidth("var(--lumo-size-xs)"); //$NON-NLS-1$
			wrapper.add(flag	);
		}
	    return wrapper;
	}

}
