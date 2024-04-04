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

import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import com.helger.commons.io.stream.StringInputStream;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.addons.download.DownloadExtension;
import fr.utbm.ciad.labmanager.views.components.addons.localization.LanguageSelect;
import fr.utbm.ciad.labmanager.views.components.addons.progress.ProgressExtension;
import jakarta.annotation.security.PermitAll;
import org.arakhne.afc.progress.DefaultProgression;
import org.springframework.util.FastByteArrayOutputStream;
import org.vaadin.lineawesome.LineAwesomeIcon;

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

        final var saveIcon = LineAwesomeIcon.SAVE_SOLID.create();
    	Button taskButton = new Button();
    	taskButton.setIcon(saveIcon);
    	taskButton.setText("Test async task progress");
    	taskButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        layoutColumn2.add(taskButton);
        ProgressExtension.extend(taskButton)
        	.withAsyncTask(progress -> {
    			final var pg = new DefaultProgression(0, 12);
    			pg.addProgressionListener(progress);
   				for (var i = 0; i < 12; i++) {
           			try {
       					Thread.sleep(1000);
					} catch (InterruptedException ex) {
						throw new RuntimeException(ex);
       				}
           			pg.increment("Step " + (i+1) + "/12");
				}
   				pg.end();
       			return "Hello";
        	})
        	.withProgressIcon(LineAwesomeIcon.SAVE_SOLID.create())
        	.withProgressTitle("Do an async task")
        	.withSuccessListener(it -> {
        		System.err.println("RES:" + it);
        	})
        	.withFailureListener(it -> {
        		System.err.println("ERR:" + it);
        	});


        final var saveIcon2 = LineAwesomeIcon.SAVE_SOLID.create();
    	Button dlButton = new Button();
    	dlButton.setIcon(saveIcon2);
    	dlButton.setText("Test async download");
    	dlButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_CONTRAST);
        layoutColumn2.add(dlButton);
        DownloadExtension.extend(dlButton)
	    	.withProgressIcon(LineAwesomeIcon.DOWNLOAD_SOLID.create())
        	//.withProgressIcon(VaadinIcon.ABACUS.create(), 128f)
    		//.withProgressIcon(LumoIcon.ARROW_DOWN.create(), 128f)
	    	.withProgressTitle("Download data")
        	.withFilename(() -> "mydownload.txt")
        	.withMimeType(() -> "plain/text")
	    	.withSuccessListener(it -> {
	    		System.err.println("DNWLD:" + it);
	    	})
	    	.withFailureListener(it -> {
	    		System.err.println("ERR DL:" + it);
	    	})
        	.withInputStreamFactory(progress -> {
        		progress.setProperties(0, 0, 12, false);
        		final var buffer = new StringBuffer();
   				for (var i = 0; i < 12; i++) {
           			try {
           				buffer.append(i).append(' ');
       					Thread.sleep(1000);
					} catch (InterruptedException ex) {
						throw new RuntimeException(ex);
       				}
           			progress.increment("Step " + (i+1) + "/12");
				}
   				return new StringInputStream(buffer.toString(), Charset.defaultCharset());
        	});

        final var menuBar = new MenuBar();
        layoutColumn2.add(menuBar);
        
        final var menuItem0 = menuBar.addItem("Download data");
        DownloadExtension.extend(menuItem0)
	    	.withProgressIcon(LineAwesomeIcon.DOWNLOAD_SOLID.create())
        	//.withProgressIcon(VaadinIcon.ABACUS.create(), 128f)
    		//.withProgressIcon(LumoIcon.ARROW_DOWN.create(), 128f)
	    	.withProgressTitle("Download data from menu bar")
        	.withFilename(() -> "mydownloadmenu.txt")
        	.withMimeType(() -> "plain/text")
	    	.withSuccessListener(it -> {
	    		System.err.println("DNWLD:" + it);
	    	})
	    	.withFailureListener(it -> {
	    		System.err.println("ERR DL:" + it);
	    	})
        	.withInputStreamFactory(progress -> {
        		progress.setProperties(0, 0, 12, false);
        		final var buffer = new StringBuffer();
   				for (var i = 0; i < 12; i++) {
           			try {
           				buffer.append(i).append(' ');
       					Thread.sleep(1000);
					} catch (InterruptedException ex) {
						throw new RuntimeException(ex);
       				}
           			progress.increment("Step " + (i+1) + "/12");
				}
   				return new StringInputStream(buffer.toString(), Charset.defaultCharset());
        	});

        final var subMenuItem = menuBar.addItem("Sub menu");
        final var subMenu = subMenuItem.getSubMenu();
        final var menuItem1 = subMenu.addItem("Download data");
        DownloadExtension.extend(menuItem1)
	    	.withProgressIcon(LineAwesomeIcon.DOWNLOAD_SOLID.create())
        	//.withProgressIcon(VaadinIcon.ABACUS.create(), 128f)
    		//.withProgressIcon(LumoIcon.ARROW_DOWN.create(), 128f)
	    	.withProgressTitle("Download data from sub menu")
        	.withFilename(() -> "mydownloadsubmenu.txt")
        	.withMimeType(() -> "plain/text")
	    	.withSuccessListener(it -> {
	    		System.err.println("DNWLD:" + it);
	    	})
	    	.withFailureListener(it -> {
	    		System.err.println("ERR DL:" + it);
	    	})
        	.withInputStreamFactory(progress -> {
        		progress.setProperties(0, 0, 12, false);
        		final var buffer = new StringBuffer();
   				for (var i = 0; i < 12; i++) {
           			try {
           				buffer.append(i).append(' ');
       					Thread.sleep(1000);
					} catch (InterruptedException ex) {
						throw new RuntimeException(ex);
       				}
           			progress.increment("Step " + (i+1) + "/12");
				}
   				return new StringInputStream(buffer.toString(), Charset.defaultCharset());
        	});
	}
}
