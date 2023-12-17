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

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.internal.LocaleUtil;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import jakarta.servlet.http.Cookie;

/** A select component that is able to change the current language of the Vaadin UI.
 * 
 * <h3>important note</h3>
 * 
 * <p>If you put multiple {@link LanguageSelect} on the same UI page, they will be not
 * listening about the language changes that may be applied by the other selectors on the same page.
 * Consequently, if you change the language with one selector, the other selectors will not change
 * their own selected language dynamically. <strong>As a consequence, it is recommended to put
 * only one {@link LanguageSelect} per page.</strong>
 *
 * <h3>Language Flags</h3>
 *
 * <p>You need to provide a flag image for each Locale that you will add as item.
 * Place these images in {@code /icons/languageflags/} folder in the Java resources
 * {@code src/main/resources}, and name them [language_code].svg.
 * Only SVG files are used by this language selector by default.
 * If you want to change the flag folder, call {@link #setFlagPath(String)}.
 * If you want to change the default extension, call {@link #setFlagExtension(String)}.
 *
 * <h3>Translation of language names</h3>
 * 
 * <p>In the ResourceBundle that your I18NProvider implementation uses, add translations for each Locale that you will use.
 * The keys must be named {@code views.language_select.[language_code]}
 * If you want to change the key prefix, call {@link #setFlagTranslationKeyPrefix(String)}.
 * 
 * <p>This component is a re-implementation and an extension of
 * <a href="https://github.com/KasparScherrer/language-select">LanguageSelect</a>
 * written by Karspar Scherrer under Apache 2 license.
 *
 * @author $Author: sgalland$
 * @author $Author: kscherrer$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@CssImport(value = "./themes/labmanager/components/language-select.css", themeFor = "vaadin-select")
public class LanguageSelect extends Select<Locale> implements LocaleChangeObserver {

	private static final long serialVersionUID = -9160658766711844756L;

	/** Default path of the flag images.
	 */
	public static final String DEFAULT_FLAG_PATH = "/icons/languageselect/"; //$NON-NLS-1$

	/** Default filename extension for the flag images.
	 */
	public static final String DEFAULT_FLAG_EXTENSION = ".svg"; //$NON-NLS-1$

	// 6 months
	private static final int LANGUAGE_COOKIE_DURATION = 262800;

	private static final String LANGUAGE_COOKIE_NAME = "VaadinPreferredLanguage"; //$NON-NLS-1$


	private static final String DEFAULT_TRANSLATION_KEY_PREFIX = "views.language_select."; //$NON-NLS-1$

	private final ComponentRenderer<? extends Component, Locale> defaultRenderer = new ComponentRenderer<>(new LanguageSelectItemRenderer(this));

	private String flagPath;

	private String flagExtension;

	private String flagTranslationKeyPrefix;

	/** Constructor with the provided languages. This constructor does not use the language cookie.
	 * To use the language cookie, invoke {@link #LanguageSelect(boolean, Locale...)}.
	 *
	 * @param items the supported languages.
	 * @see #LanguageSelect(boolean, Locale...)
	 */
	public LanguageSelect(Locale... items) {
		this(false, items);
	}

	/** Constructor with the provided languages and that is ready the language cookie.
	 *
	 * @param useLanguageCookie indicates if the language cookie is read, or not.
	 * @param items the supported languages.
	 * @see #LanguageSelect(Locale...)
	 */
	public LanguageSelect(boolean useLanguageCookie, Locale... items) {
		this(UI.getCurrent().getLocale(), useLanguageCookie, items);
	}

	/** Constructor with the provided languages and that is ready the language cookie.
	 *
	 * @param currentLocale the initial locale to be selected.
	 * @param useLanguageCookie indicates if the language cookie is read, or not.
	 * @param items the supported languages.
	 * @see #LanguageSelect(Locale...)
	 */
	public LanguageSelect(Locale currentLocale, boolean useLanguageCookie, Locale... items){
		setItems(items);
		addClassName("language-select"); //$NON-NLS-1$
		setEmptySelectionAllowed(false);
		
		setRenderer(this.defaultRenderer);

		setValue(currentLocale);

		// Important that valuechangeListener is defined after setValue
		addValueChangeListener(change -> {
			changeUILocale(change.getValue());
			if (useLanguageCookie) {
				changeLanguageCookie(change.getValue());
			}
		});
	}
	
	/** Replies the default resource corresponding to the language flag.
	 *
	 * @param locale the locale for which the flag must be replied.
	 * @return the flag icon resource.
	 */
	public static StreamResource getDefaultLanguageFlag(Locale locale) {
		return ComponentFactory.newStreamImage(
				new StringBuilder().append(DEFAULT_FLAG_PATH)
				.append(locale.getLanguage().toLowerCase())
				.append(DEFAULT_FLAG_EXTENSION).toString());
	}

	@Override
	public void setRenderer(ComponentRenderer<? extends Component, Locale> renderer) {
		super.setRenderer(renderer == null ? this.defaultRenderer : renderer);
	}

	/** Change the renderer of the items.
	 *
	 * @param renderer the new renderer.
	 */
	public void setRenderer(SerializableFunction<Locale, Component> renderer) {
		setRenderer(renderer == null ? null : new ComponentRenderer<>(renderer));
	}

	/** Factory method for creating the language select from the current
	 * {@link I18NProvider} provider.
	 *
	 * @return the language select.
	 */
	public static LanguageSelect newStandardLanguageSelect() {
		final Locale[] locales = getAvailableLocales();
		final LanguageSelect select = new LanguageSelect(true, locales);
		return select;
	}

	/** Factory method for creating the language select from the current
	 * {@link I18NProvider} provider and displaying only the flags.
	 *
	 * @return the language select.
	 */
	public static LanguageSelect newFlagOnlyLanguageSelect() {
		final Locale[] locales = getAvailableLocales();
		final LanguageSelect select = new LanguageSelect(true, locales);
		select.setRenderer(new LanguageSelectFlagItemRenderer(select));
		select.setMaxWidth(52, Unit.POINTS);
		return select;
	}

	/** Replies the available locales that are provided by the i18n provider.
	 *
	 * @return the locales, never {@code null} or empty array
	 * @throws IllegalStateException if there is no i18n provider defined.
	 */
	public static Locale[] getAvailableLocales() {
		final Optional<I18NProvider> i18NProvider = LocaleUtil.getI18NProvider();
		if (i18NProvider.isPresent()) {
			final Locale[] locales = i18NProvider.get().getProvidedLocales().toArray(size -> new Locale[size]);
			if (locales.length > 0) {
				return locales;
			}
		}
		throw new IllegalStateException("i18nProvider provider not found; or no language support provided"); //$NON-NLS-1$
	}

	/** Change the locale of the UI.
	 *
	 * @param locale the new UI locale.
	 */
	public static void setUILocale(Locale locale) {
		UI.getCurrent().getSession().setLocale(locale);
	}

	/** Change the locale of the UI. This function is provided for subclass overriding.
	 *
	 * @param locale the new UI locale.
	 * @see #setUILocale(Locale)
	 */
	@SuppressWarnings("static-method")
	protected void changeUILocale(Locale locale) {
		setUILocale(locale);
	}

	/** Change the language cookie.
	 *
	 * @param locale the new UI locale.
	 */
	@SuppressWarnings("static-method")
	protected void changeLanguageCookie(Locale locale) {
		final Cookie languageCookie = new Cookie(LANGUAGE_COOKIE_NAME, locale.getLanguage());
		languageCookie.setMaxAge(LANGUAGE_COOKIE_DURATION);
		languageCookie.setPath("/"); //$NON-NLS-1$
		VaadinService.getCurrentResponse().addCookie(languageCookie);
	}

	/** Change the path where the flags are located.
	 * It is the path in the {@code src/main/resources} folder, with
	 * a slash character at the beginning and at the end.
	 *
	 * @param path the new path.
	 */
	public void setFlagPath(String path) {
		this.flagPath = Strings.emptyToNull(path);
	}

	/** Replies the path where the flags are located.
	 * It is the path in the {@code src/main/resources} folder, with
	 * a slash character at the beginning and at the end.
	 *
	 * @return the path.
	 */
	public String getFlagPath() {
		return this.flagPath == null ? DEFAULT_FLAG_PATH : this.flagPath;
	}

	/** Change the file extension of the flag filenames.
	 *
	 * @param ext the filename extension.
	 */
	public void setFlagExtension(String ext) {
		this.flagExtension = Strings.emptyToNull(ext);
	}

	/** Replies the file extension of the flag filenames.
	 *
	 * @return the filename extension.
	 */
	public String getFlagExtension() {
		return this.flagExtension == null ? DEFAULT_FLAG_EXTENSION : this.flagExtension;
	}

	/** Change the prefix of the keys that are in the translation files for the language names.
	 *
	 * @param prefix the prefix of the keys in the language translation files.
	 */
	public void setFlagTranslationKeyPrefix(String prefix) {
		this.flagTranslationKeyPrefix = Strings.emptyToNull(prefix);
	}

	/** Replies the prefix of the keys that are in the translation files for the language names.
	 *
	 * @return the prefix of the keys in the language translation files.
	 */
	public String getFlagTranslationKeyPrefix() {
		return this.flagTranslationKeyPrefix == null ? DEFAULT_TRANSLATION_KEY_PREFIX : this.flagTranslationKeyPrefix;
	}

	/**
	 * This will refresh all items and use the names of each language, translated in the new Locale
	 */
	public void refresh(){
		// As soon as Select::refreshItems is public, use that!
		//  see https://github.com/vaadin/flow/issues/6337
		setRenderer(getItemRenderer());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		refresh();
	}

	/** Read the language cookie and initialize the Vaadin session with the language.
	 *
	 * @param serviceInitEvent the initialization event from Vaadin framework.
	 */
	public static void readLanguageCookies(ServiceInitEvent serviceInitEvent){
		serviceInitEvent.getSource().addSessionInitListener(sessionEvent -> {
			final VaadinRequest request = sessionEvent.getRequest();
			final VaadinSession session = sessionEvent.getSession();

			final Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				final Optional<Cookie> localeCookie = Arrays.stream(cookies).filter(c -> c.getName().equals(LANGUAGE_COOKIE_NAME)).findFirst();
				localeCookie.ifPresent(cookie -> session.setLocale(new Locale(cookie.getValue())));
			}
		});
	}

}
