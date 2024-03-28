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

package fr.utbm.ciad.labmanager.views.appviews.welcome;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.addons.localization.LanguageSelect;
import jakarta.annotation.security.PermitAll;

/** The default view of the labmanager application.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@PageTitle("views.iddle")
@Route(value = "", layout = MainLayout.class)
@PermitAll
@Uses(Icon.class)
public class WelcomeView extends Composite<VerticalLayout> {

    private static final long serialVersionUID = -3953932858588108431L;

    /** Constructor.
     */
	public WelcomeView() {
        final VerticalLayout layoutColumn2 = new VerticalLayout();
        final Icon icon = new Icon();
        //final LoginForm loginForm = new LoginForm();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn2.setAlignItems(Alignment.CENTER);
        icon.getElement().setAttribute("icon", "lumo:user");
        //layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, loginForm);
        getContent().add(layoutColumn2);
        layoutColumn2.add(icon);
        //layoutColumn2.add(loginForm);
        
        LanguageSelect select0 = LanguageSelect.newStandardLanguageSelect(getLocale());
        select0.addThemeVariants(SelectVariant.LUMO_SMALL);
        layoutColumn2.add(select0);

        LanguageSelect select1 = LanguageSelect.newFlagOnlyLanguageSelect(getLocale());
        select1.addThemeVariants(SelectVariant.LUMO_SMALL);
        layoutColumn2.add(select1);
	}
}
