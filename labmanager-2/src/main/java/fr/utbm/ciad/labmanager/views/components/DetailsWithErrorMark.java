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

import com.google.common.base.Strings;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/** A Details with an error marker on the side of the title.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public class DetailsWithErrorMark extends Details {

	private static final long serialVersionUID = -7845592866965170183L;

	private final HorizontalLayout errorBadge;

	private final Span errorText = new Span();

	private final Text title;

	/** Constructor without initial title.
	 *
	 * @param content the content of the details component.
	 */
	public DetailsWithErrorMark(Component content) {
		this(null, content);
	}

	/** Constructor.
	 *
	 * @param title the title of the details.
	 * @param content the content of the details component.
	 */
	public DetailsWithErrorMark(String title, Component content) {
		final HorizontalLayout summary = new HorizontalLayout();
		summary.setSpacing(false);

		final Icon icon = VaadinIcon.EXCLAMATION_CIRCLE.create();
		icon.getStyle().set("width", "var(--lumo-icon-size-s)");  //$NON-NLS-1$//$NON-NLS-2$
		icon.getStyle().set("height", "var(--lumo-icon-size-s)");  //$NON-NLS-1$//$NON-NLS-2$

		this.errorBadge = new HorizontalLayout(icon, this.errorText);
		this.errorBadge.setSpacing(false);
		this.errorBadge.getStyle().set("color", "var(--lumo-error-text-color)");  //$NON-NLS-1$//$NON-NLS-2$
		this.errorBadge.getStyle().set("margin-left", "var(--lumo-space-s)");  //$NON-NLS-1$//$NON-NLS-2$
		this.errorBadge.setVisible(false);

		this.title = new Text(title);
		summary.add(this.title);
		summary.add(this.errorBadge);

		setSummary(summary);
		add(content);
	}

	/** Change the title of the details.
	 *
	 * @param title the new title.
	 */
	public void setLabel(String title) {
		this.title.setText(title);
	}

	/** Change the error message. This function causes the error mark to be displayed or hidden.
	 *
	 * @param message the error message. If it is {@code null} or empty, the error mark is hidden.
	 */
	public void setError(String message) {
		final String normedText = Strings.emptyToNull(message);
		this.errorText.setText(normedText);
		this.errorBadge.setVisible(normedText != null);
	}

}
