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

import java.util.Locale;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.appviews.about.AboutView;
import fr.utbm.ciad.labmanager.views.appviews.assocstructures.AssociatedStructuresView;
import fr.utbm.ciad.labmanager.views.appviews.assocstructures.MyAssociatedStructuresView;
import fr.utbm.ciad.labmanager.views.appviews.conferences.ConferencesListView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.IncomingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.MyIncomingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.MyOutgoingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.OutgoingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.journals.JournalsListView;
import fr.utbm.ciad.labmanager.views.appviews.jurys.JurysView;
import fr.utbm.ciad.labmanager.views.appviews.jurys.MyJurysView;
import fr.utbm.ciad.labmanager.views.appviews.organizations.AddressesListView;
import fr.utbm.ciad.labmanager.views.appviews.organizations.OrganizationsListView;
import fr.utbm.ciad.labmanager.views.appviews.persons.MembershipsView;
import fr.utbm.ciad.labmanager.views.appviews.persons.MyMembershipsView;
import fr.utbm.ciad.labmanager.views.appviews.persons.MyProfileView;
import fr.utbm.ciad.labmanager.views.appviews.persons.PersonsView;
import fr.utbm.ciad.labmanager.views.appviews.projects.MyProjectsView;
import fr.utbm.ciad.labmanager.views.appviews.projects.ProjectsView;
import fr.utbm.ciad.labmanager.views.appviews.publications.MyPublicationsView;
import fr.utbm.ciad.labmanager.views.appviews.publications.PublicationsView;
import fr.utbm.ciad.labmanager.views.appviews.scientificaxes.ScientificAxesView;
import fr.utbm.ciad.labmanager.views.appviews.supervisions.MySupervisionsView;
import fr.utbm.ciad.labmanager.views.appviews.supervisions.MySupervisorsView;
import fr.utbm.ciad.labmanager.views.appviews.supervisions.SupervisionsView;
import fr.utbm.ciad.labmanager.views.appviews.teaching.MyTeachingActivitiesView;
import fr.utbm.ciad.labmanager.views.appviews.teaching.TeachingActivitiesView;
import fr.utbm.ciad.labmanager.views.appviews.welcome.WelcomeView;
import fr.utbm.ciad.labmanager.views.components.messages.LanguageSelect;
import fr.utbm.ciad.labmanager.views.components.users.UserIdentityChangedObserver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.vaadin.lineawesome.LineAwesomeIcon;

/** The main view is a top-level placeholder for other views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class MainLayout extends AppLayout implements LocaleChangeObserver, UserIdentityChangedObserver {

	private static final long serialVersionUID = 4459306874348372506L;

	private final String applicationName;

	private H2 viewTitle;

	private final AuthenticatedUser authenticatedUser;

	private final AccessAnnotationChecker accessChecker;

	private Anchor loginLink;

	private MenuItem logoutLink;

	private MenuItem languagesLink;

	private LanguageRecord[] languageLinks;

	private Avatar avatar;

	private Text username;

	private SideNavItem positionSection;

	private SideNavItem myprofile;

	private SideNavItem allpersons;

	private SideNavItem documentations;

	private SideNavItem documentation;

	private SideNavItem about;

	private SideNavItem organizationsSection;

	private SideNavItem organizations;

	private SideNavItem scientificAxes;

	private SideNavItem addresses;

	private SideNavItem scientificActivitySection;

	private SideNavItem journals;

	private SideNavItem conferences;

	/** Constructor.
	 *
	 * @param authenticatedUser the logged-in user.
	 * @param accessChecker the access checker.
	 * @param applicationName name of the application.
	 */
	public MainLayout(@Autowired AuthenticatedUser authenticatedUser, @Autowired AccessAnnotationChecker accessChecker,
			@Value("${labmanager.application-name}") String applicationName) {
		this.authenticatedUser = authenticatedUser;
		this.accessChecker = accessChecker;
		this.applicationName = applicationName;
		setPrimarySection(Section.DRAWER);
		addNavigationPanel();
		addViewHeader();
	}

	/** Add the title of the view, with the menu toggle button and the view title.
	 */
	protected void addViewHeader() {
		final var toggle = new DrawerToggle();
		toggle.setAriaLabel(getTranslation("views.menu_toggle")); //$NON-NLS-1$
		this.viewTitle = new H2();
		this.viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
		addToNavbar(true, toggle, this.viewTitle);
	}

	/** Add the navigation panel, with the application header, the menus, and the navigation footer.
	 */
	protected void addNavigationPanel() {
		final var scroller = new Scroller(createNavigationPanelContent());
		addToDrawer(createNavigationPanelHeader(), scroller, createNavigationPanelFooter());
	}

	/** Create the welcome items in the navigation panel.
	 *
	 * @param nav the navigation panel.
	 */
	protected void createWelcomeNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(WelcomeView.class)) {
			nav.addItem(new SideNavItem("Welcome", WelcomeView.class));
		}
	}

	/** Create the profile and career items in the navigation panel.
	 *
	 * @param nav the navigation panel.
	 */
	protected void createProfileNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyProfileView.class)
				|| this.accessChecker.hasAccess(MyMembershipsView.class)
				|| this.accessChecker.hasAccess(PersonsView.class)
				|| this.accessChecker.hasAccess(MembershipsView.class)) {
			this.positionSection = new SideNavItem(null);
			if (this.accessChecker.hasAccess(MyProfileView.class)) {
				this.myprofile = new SideNavItem(null, MyProfileView.class, LineAwesomeIcon.USER_CIRCLE.create());
				this.positionSection.addItem(this.myprofile);
			}
			if (this.accessChecker.hasAccess(MyMembershipsView.class)) {
				this.positionSection.addItem(new SideNavItem("My Positions", MyMembershipsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(PersonsView.class)) {
				this.allpersons = new SideNavItem(null, PersonsView.class, LineAwesomeIcon.USERS_SOLID.create());
				this.positionSection.addItem(this.allpersons);
			}
			if (this.accessChecker.hasAccess(MembershipsView.class)) {
				this.positionSection.addItem(new SideNavItem("Positions", MembershipsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			nav.addItem(this.positionSection);
		}
	}

	/** Create the scientific activities items in the navigation panel.
	 *
	 * @param nav the navigation panel.
	 */
	protected void createScientificActivitiesNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyPublicationsView.class)
				|| this.accessChecker.hasAccess(MySupervisorsView.class)
				|| this.accessChecker.hasAccess(MySupervisionsView.class)
				|| this.accessChecker.hasAccess(MySupervisionsView.class)
				|| this.accessChecker.hasAccess(PublicationsView.class)
				|| this.accessChecker.hasAccess(SupervisionsView.class)
				|| this.accessChecker.hasAccess(JournalsListView.class)
				|| this.accessChecker.hasAccess(ConferencesListView.class)) {
			this.scientificActivitySection = new SideNavItem(null);
			if (this.accessChecker.hasAccess(MyPublicationsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("My Publications", MyPublicationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MySupervisorsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("My Supervisors", MySupervisorsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyPublicationsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("My Supervisions", MySupervisorsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(PublicationsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("Publications", PublicationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(SupervisionsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("Supervisions", SupervisionsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(JournalsListView.class)) {
				this.journals = new SideNavItem("", JournalsListView.class, LineAwesomeIcon.NEWSPAPER_SOLID.create()); //$NON-NLS-1$
				this.scientificActivitySection.addItem(this.journals);
			}
			if (this.accessChecker.hasAccess(ConferencesListView.class)) {
				this.conferences = new SideNavItem("", ConferencesListView.class, LineAwesomeIcon.CHALKBOARD_TEACHER_SOLID.create()); //$NON-NLS-1$
				this.scientificActivitySection.addItem(this.conferences);
			}
			nav.addItem(this.scientificActivitySection);
		}
	}

	/** Create the project and innovation items in the navigation bar.
	 *
	 * @param nav the navigation bar.
	 */
	protected void createProjectInnovationNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyProjectsView.class)
				|| this.accessChecker.hasAccess(ProjectsView.class)
				|| this.accessChecker.hasAccess(MyAssociatedStructuresView.class)
				|| this.accessChecker.hasAccess(AssociatedStructuresView.class)) {
			final SideNavItem projects = new SideNavItem("Projects and Innovation");
			if (this.accessChecker.hasAccess(MyProjectsView.class)) {
				projects.addItem(new SideNavItem("My Projects", MyAssociatedStructuresView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyAssociatedStructuresView.class)) {
				projects.addItem(new SideNavItem("My Associated Structures", MyAssociatedStructuresView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(ProjectsView.class)) {
				projects.addItem(new SideNavItem("Projects", ProjectsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(AssociatedStructuresView.class)) {
				projects.addItem(new SideNavItem("Associated Structures", AssociatedStructuresView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			nav.addItem(projects);
		}
	}

	/** Create the scientific influence items in the navigation panel.
	 *
	 * @param nav the navigation panel.
	 */
	protected void createScientificInfluenceNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyIncomingInvitationsView.class)
				|| this.accessChecker.hasAccess(MyOutgoingInvitationsView.class)
				|| this.accessChecker.hasAccess(IncomingInvitationsView.class)
				|| this.accessChecker.hasAccess(OutgoingInvitationsView.class)
				|| this.accessChecker.hasAccess(MyJurysView.class)
				|| this.accessChecker.hasAccess(JurysView.class)) {
			final SideNavItem diffusion = new SideNavItem("Rayonnement");
			if (this.accessChecker.hasAccess(MyIncomingInvitationsView.class)) {
				diffusion.addItem(new SideNavItem("My Incoming Invitations", MyIncomingInvitationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyOutgoingInvitationsView.class)) {
				diffusion.addItem(new SideNavItem("My Outgoing Invitations", MyOutgoingInvitationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(IncomingInvitationsView.class)) {
				diffusion.addItem(new SideNavItem("Incoming Invitations", IncomingInvitationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyOutgoingInvitationsView.class)) {
				diffusion.addItem(new SideNavItem("Outgoing Invitations", OutgoingInvitationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyJurysView.class)) {
				diffusion.addItem(new SideNavItem("My Jurys", MyJurysView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(JurysView.class)) {
				diffusion.addItem(new SideNavItem("Jurys", JurysView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			nav.addItem(diffusion);
		}
	}

	/** Create the teaching items in the navigation panel.
	 *
	 * @param nav the navigation panel.
	 */
	protected void createTeachingNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyTeachingActivitiesView.class)
				|| this.accessChecker.hasAccess(TeachingActivitiesView.class)) {
			final SideNavItem teaching = new SideNavItem("Teaching");
			if (this.accessChecker.hasAccess(MyTeachingActivitiesView.class)) {
				teaching.addItem(new SideNavItem("My activities", MyTeachingActivitiesView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(TeachingActivitiesView.class)) {
				teaching.addItem(new SideNavItem("Activities", TeachingActivitiesView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			nav.addItem(teaching);
		}
	}

	/** Create the structure management items in the navigation bar.
	 *
	 * @param nav the navigation bar.
	 */
	protected void createStructureManagementNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(OrganizationsListView.class)
				|| this.accessChecker.hasAccess(ScientificAxesView.class)
				|| this.accessChecker.hasAccess(AddressesListView.class)) {
			this.organizationsSection = new SideNavItem(""); //$NON-NLS-1$
			if (this.accessChecker.hasAccess(OrganizationsListView.class)) {
				this.organizations = new SideNavItem("", OrganizationsListView.class, LineAwesomeIcon.UNIVERSITY_SOLID.create()); //$NON-NLS-1$
				this.organizationsSection.addItem(this.organizations);
			}
			if (this.accessChecker.hasAccess(ScientificAxesView.class)) {
				this.scientificAxes = new SideNavItem("", ScientificAxesView.class, LineAwesomeIcon.LAYER_GROUP_SOLID.create()); //$NON-NLS-1$
				this.organizationsSection.addItem(this.scientificAxes);
			}
			if (this.accessChecker.hasAccess(AddressesListView.class)) {
				this.addresses = new SideNavItem("", AddressesListView.class, LineAwesomeIcon.MAP_MARKER_SOLID.create()); //$NON-NLS-1$
				this.organizationsSection.addItem(this.addresses);
			}
			nav.addItem(this.organizationsSection);
		}
	}

	/** Create the documentation items in the navigation panel.
	 *
	 * @param nav the navigation panel.
	 */
	protected void createDocumentationNavigation(SideNav nav) {
		this.documentations = new SideNavItem(""); //$NON-NLS-1$
		this.documentation = new SideNavItem("", ViewConstants.ONLINE_MANUAL_URL, LineAwesomeIcon.BOOK_SOLID.create()); //$NON-NLS-1$
		this.documentations.addItem(this.documentation);
		this.about = new SideNavItem("", AboutView.class, LineAwesomeIcon.COPYRIGHT_SOLID.create()); //$NON-NLS-1$
		this.documentations.addItem(this.about);
		nav.addItem(this.documentations);
	}

	/** Create the menus in the navigation panel.
	 *
	 * @return the created navigation panel.
	 */
	protected SideNav createNavigationPanelContent() {
		final var nav = new SideNav();
		createWelcomeNavigation(nav);
		createProfileNavigation(nav);
		createScientificActivitiesNavigation(nav);
		createProjectInnovationNavigation(nav);
		createScientificInfluenceNavigation(nav);
		createTeachingNavigation(nav);
		createStructureManagementNavigation(nav);
		createDocumentationNavigation(nav);
		return nav;
	}

	/** Create the header of the navigation panel.
	 *
	 * @return the header of the navigation panel.
	 */
	protected Header createNavigationPanelHeader() {
		final var appName = new H1(this.applicationName);
		appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
		return new Header(appName);
	}

	/** Create the footer of the navigation panel.
	 *
	 * @return the footer of the navigation panel.
	 */
	protected Footer createNavigationPanelFooter() {
		this.avatar = null;
		this.username = null;
		this.loginLink = null;
		this.logoutLink = null;
		this.languageLinks = null;
		this.languageLinks = null;
		
		final var layout = new Footer();
		final var maybeUser = this.authenticatedUser.get();
		if (maybeUser.isPresent()) {
			final var user = maybeUser.get();
			final var person = user.getPerson();
			
			this.avatar = new Avatar(person.getFullName());
			final var photo = person.getPhotoURL();
			if (photo != null) {
				this.avatar.setImage(photo.toExternalForm());
			} else {
				this.avatar.setImage(null);
			}
			this.avatar.setThemeName("xsmall"); //$NON-NLS-1$
			this.avatar.getElement().setAttribute("tabindex", "-1"); //$NON-NLS-1$//$NON-NLS-2$

			this.username = new Text(person.getFullName());

			final var userMenu = new MenuBar();
			userMenu.setThemeName("tertiary-inline contrast"); //$NON-NLS-1$

			final var userName = userMenu.addItem(""); //$NON-NLS-1$
			final var div = new Div();
			div.add(this.avatar);
			div.add(this.username);
			div.add(new Icon("lumo", "dropdown")); //$NON-NLS-1$//$NON-NLS-2$
			div.getElement().getStyle().set("display", "flex"); //$NON-NLS-1$//$NON-NLS-2$
			div.getElement().getStyle().set("align-items", "center"); //$NON-NLS-1$//$NON-NLS-2$
			div.getElement().getStyle().set("gap", "var(--lumo-space-s)"); //$NON-NLS-1$//$NON-NLS-2$
			userName.add(div);

			final var mainMenu = userName.getSubMenu();

			this.languagesLink = mainMenu.addItem(""); //$NON-NLS-1$s
			final var locales = LanguageSelect.getAvailableLocales();
			this.languageLinks = new LanguageRecord[locales.length];
			final var languageSubMenu = this.languagesLink.getSubMenu();
			int i = 0;
			for (final var locale : LanguageSelect.getAvailableLocales()) {
				final var hl = new HorizontalLayout();
				hl.setSpacing(false);
				hl.setAlignItems(Alignment.CENTER);
				final var img = new Image(LanguageSelect.getDefaultLanguageFlag(locale), ""); //$NON-NLS-1$
				img.setMaxHeight("var(--lumo-icon-size-s)"); //$NON-NLS-1$
				img.getStyle().set("margin-right", "var(--lumo-space-s)"); //$NON-NLS-1$ //$NON-NLS-2$
				final var text = new Span(""); //$NON-NLS-1$
				this.languageLinks[i] = new LanguageRecord(locale, text);
				hl.add(img, text);
				languageSubMenu.addItem(hl, e -> {
					LanguageSelect.setUILocale(locale);
				});
				++i;
			}
			
			this.logoutLink = mainMenu.addItem("", e -> { //$NON-NLS-1$
				this.authenticatedUser.logout();
			});

			layout.add(userMenu);
		} else {
			this.loginLink = new Anchor("login", "");  //$NON-NLS-1$ //$NON-NLS-2$
			layout.add(this.loginLink);
		}
		return layout;
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		this.viewTitle.setText(getCurrentPageTitle());
	}

	/** Replies the title of the current page.
	 *
	 * @return the current page title.
	 */
	protected String getCurrentPageTitle() {
		final var obj = getContent();
		if (obj == null) {
			return ""; //$NON-NLS-1$
		}
		if (obj instanceof HasDynamicTitle castValue) {
			return castValue.getPageTitle();
		}
		final var title = obj.getClass().getAnnotation(PageTitle.class);
		return title == null ? "" : getTranslation(title.value()); //$NON-NLS-1$
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.viewTitle.setText(getCurrentPageTitle());

		this.positionSection.setLabel(getTranslation("views.navitem.positionSection")); //$NON-NLS-1$
		this.myprofile.setLabel(this.getTranslation("views.navitem.my_profile")); //$NON-NLS-1$
		this.allpersons.setLabel(getTranslation("views.navitem.all_persons")); //$NON-NLS-1$
		
		this.scientificActivitySection.setLabel(getTranslation("views.navitem.scientificactivitiesSection")); //$NON-NLS-1$
		this.journals.setLabel(getTranslation("views.navitem.journals")); //$NON-NLS-1$
		this.conferences.setLabel(getTranslation("views.navitem.conferences")); //$NON-NLS-1$

		this.organizationsSection.setLabel(getTranslation("views.navitem.organizationsSection")); //$NON-NLS-1$
		this.organizations.setLabel(getTranslation("views.navitem.organizations")); //$NON-NLS-1$
		this.scientificAxes.setLabel(getTranslation("views.navitem.scientific_axes")); //$NON-NLS-1$
		this.addresses.setLabel(getTranslation("views.navitem.addresses")); //$NON-NLS-1$

		this.documentations.setLabel(getTranslation("views.navitem.documentations")); //$NON-NLS-1$
		this.documentation.setLabel(getTranslation("views.navitem.online_manuals")); //$NON-NLS-1$
		this.about.setLabel(getTranslation("views.navitem.about_app")); //$NON-NLS-1$

		if (this.loginLink != null) {
			this.loginLink.setText(getTranslation("views.sign_in")); //$NON-NLS-1$
		}
		if (this.logoutLink != null) {
			this.logoutLink.setText(getTranslation("views.sign_out")); //$NON-NLS-1$
		}
		if (this.languagesLink != null) {
			this.languagesLink.setText(getTranslation("views.languages")); //$NON-NLS-1$
		}
		if (this.languageLinks != null) {
			for (final LanguageRecord menuItem : this.languageLinks) {
				menuItem.menuText.setText(StringUtils.capitalize(menuItem.locale.getDisplayLanguage(getLocale())));
			}
		}
	}

	@Override
	public void authenticatedUserIdentityChange() {
		final var maybeUser = this.authenticatedUser.get();
		if (maybeUser.isPresent()) {
			final var user = maybeUser.get();
			final var person = user.getPerson();
			final var fullname = person.getFullName();
			if (this.avatar != null) {
				this.avatar.setName(fullname);
				final var photo = person.getPhotoURL();
				if (photo != null) {
					this.avatar.setImage(photo.toExternalForm());
				} else {
					this.avatar.setImage(null);
				}
			}
			if (this.username != null) {
				this.username.setText(fullname);
			}
		}
	}

	/** Description of a language menu item.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private record LanguageRecord(Locale locale, Span menuText) {
		//
	}

}
