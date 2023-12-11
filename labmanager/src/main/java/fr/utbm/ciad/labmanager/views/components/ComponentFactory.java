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

package fr.utbm.ciad.labmanager.views.components;

import java.net.URL;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.stereotype.Component;

/** Factory of Vaadin components.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Component
public final class ComponentFactory {

	private ComponentFactory() {
		//
	}

	/** Create a form layout with multiple columns.
	 *
	 * @param columns the number of columns between 1 and 2.
	 * @return the layout.
	 */
	public static FormLayout newColumnForm(int columns) {
		assert columns >= 1 && columns <= 2;
		final FormLayout content = new FormLayout();
		switch (columns) {
		case 1:
			// No specific configuration for a single column
			break;
		case 2:
			content.setResponsiveSteps(
					new FormLayout.ResponsiveStep("0", 1), //$NON-NLS-1$
					new FormLayout.ResponsiveStep("20em", 2)); //$NON-NLS-1$
			break;
		default:
			throw new IllegalArgumentException();
		}
		return content;
	}

	/** Create a text field for phone numbers, with international prefix.
	 *
	 * @param region the code of the region; never {@code null}.
	 * @return the field.
	 * @see #newPhoneNumberField(CountryCode)
	 */
	public static TextField newPhoneNumberField(String region) {
		assert !Strings.isNullOrEmpty(region);
		final TextField content = new TextField();
		//final PhoneI18nFieldFormatter formatter = new PhoneI18nFieldFormatter(region);
		//formatter.extend(content);
		return content;
	}

	/** Create a text field for phone numbers, with international prefix.
	 *
	 * @param region the code of the region; never {@code null}.
	 * @return the field.
	 * @see #newPhoneNumberField(String)
	 */
	public static TextField newPhoneNumberField(CountryCode region) {
		assert region != null;
		return newPhoneNumberField(region.getCode());
	}

	/** Create a text field with a clickable 16x16 icon as suffix.
	 *
	 * @param href the URL of the target page.
	 * @param iconPath the path of the icon in the JAva resources, starting with a slash character.
	 * @return the field.
	 * @see #newClickableIconTextField(String, String, int)
	 */
	public static TextField newClickableIconTextField(String href, String iconPath) {
		return newClickableIconTextField(href, iconPath, 16);
	}

	/** Create a text field with a clickable icon as suffix.
	 *
	 * @param href the URL of the target page.
	 * @param iconPath the path of the icon in the JAva resources, starting with a slash character.
	 * @param iconSize the size of the icon in points.
	 * @return the field.
	 * @see #newClickableIconTextField(String, String)
	 */
	public static TextField newClickableIconTextField(String href, String iconPath, int iconSize) {
		assert !Strings.isNullOrEmpty(href);
		final TextField content = new TextField();
		final StreamResource imageResource = newStreamImage(iconPath);
		final Image image = new Image(imageResource, href);
		image.setAlt(imageResource.getName());
		image.setMinHeight(iconSize, Unit.POINTS);
		image.setMaxHeight(iconSize, Unit.POINTS);
		image.setMinWidth(iconSize, Unit.POINTS);
		image.setMaxWidth(iconSize, Unit.POINTS);
		final Anchor anchor = new Anchor(href, image);
		anchor.setTitle(href);
		anchor.setTarget(AnchorTarget.BLANK);
		anchor.setTabIndex(-1);
		content.setSuffixComponent(anchor);
		return content;
	}

	/** Create a image stream from an URL pointing a Java resource.
	 *
	 * @param iconPath the path of the icon in the Java resources, starting with a slash character.
	 * @return the stream.
	 */
	public static StreamResource newStreamImage(String iconPath) {
		assert !Strings.isNullOrEmpty(iconPath);
		final URL url;
		try {
			url = FileSystem.convertStringToURL(iconPath, false);
		} catch (Throwable ex) {
			throw new IllegalArgumentException(ex);
		}
		final StreamResource imageResource = new StreamResource(FileSystem.largeBasename(url),
		        () -> ComponentFactory.class.getResourceAsStream(iconPath));
		return imageResource;
	}

}
