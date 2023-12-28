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

package fr.utbm.ciad.labmanager.views.appviews;

import java.util.ArrayList;
import java.util.Locale;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
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
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.appviews.about.AboutView;
import fr.utbm.ciad.labmanager.views.appviews.assocstructures.AssociatedStructuresListView;
import fr.utbm.ciad.labmanager.views.appviews.assocstructures.MyAssociatedStructuresView;
import fr.utbm.ciad.labmanager.views.appviews.conferences.ConferencesListView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.IncomingInvitationsListView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.MyIncomingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.MyOutgoingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.OutgoingInvitationsListView;
import fr.utbm.ciad.labmanager.views.appviews.journals.JournalsListView;
import fr.utbm.ciad.labmanager.views.appviews.jurys.JuryMembershipsListView;
import fr.utbm.ciad.labmanager.views.appviews.jurys.MyJurysView;
import fr.utbm.ciad.labmanager.views.appviews.memberships.MembershipsListView;
import fr.utbm.ciad.labmanager.views.appviews.memberships.MyMembershipsView;
import fr.utbm.ciad.labmanager.views.appviews.organizations.AddressesListView;
import fr.utbm.ciad.labmanager.views.appviews.organizations.OrganizationsListView;
import fr.utbm.ciad.labmanager.views.appviews.persons.MyProfileView;
import fr.utbm.ciad.labmanager.views.appviews.persons.PersonsListView;
import fr.utbm.ciad.labmanager.views.appviews.projects.MyProjectsView;
import fr.utbm.ciad.labmanager.views.appviews.projects.ProjectsListView;
import fr.utbm.ciad.labmanager.views.appviews.publications.MyPublicationsView;
import fr.utbm.ciad.labmanager.views.appviews.publications.PublicationsView;
import fr.utbm.ciad.labmanager.views.appviews.scientificaxes.ScientificAxesListView;
import fr.utbm.ciad.labmanager.views.appviews.supervisions.MySupervisionsView;
import fr.utbm.ciad.labmanager.views.appviews.supervisions.MySupervisorsView;
import fr.utbm.ciad.labmanager.views.appviews.supervisions.SupervisionsListView;
import fr.utbm.ciad.labmanager.views.appviews.teaching.MyTeachingActivitiesView;
import fr.utbm.ciad.labmanager.views.appviews.teaching.TeachingActivitiesListView;
import fr.utbm.ciad.labmanager.views.appviews.welcome.WelcomeView;
import fr.utbm.ciad.labmanager.views.components.addons.countryflag.CountryFlag;
import fr.utbm.ciad.labmanager.views.components.addons.localization.LanguageSelect;
import fr.utbm.ciad.labmanager.views.components.users.UserIdentityChangedObserver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
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

	private final MessageSourceAccessor messages;

	private Anchor loginLink;

	private MenuItem logoutLink;

	private MenuItem languagesLink;

	private LanguageRecord[] languageLinks;

	private Avatar avatar;

	private Text username;

	private Span roleInUserMenu;

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

	private SideNavItem teachingSection;

	private SideNavItem teachingActivites;

	private SideNavItem projectSection;

	private SideNavItem projects;

	private SideNavItem associatedStructures;

	private SideNavItem diffusionSection;

	private SideNavItem juryMemberships;

	private SideNavItem outgoingInvitations;

	private SideNavItem incomingInvitations;

	private SideNavItem supervisions;

	private SideNavItem positions;

	/** Constructor.
	 *
	 * @param authenticatedUser the logged-in user.
	 * @param accessChecker the access checker.
	 * @param messages the accessor to the localized messages in the Spring framework.
	 * @param applicationName name of the application.
	 */
	public MainLayout(@Autowired AuthenticatedUser authenticatedUser, @Autowired AccessAnnotationChecker accessChecker,
			@Autowired MessageSourceAccessor messages, @Value("${labmanager.application-name}") String applicationName) {
		this.messages = messages;
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
				|| this.accessChecker.hasAccess(PersonsListView.class)
				|| this.accessChecker.hasAccess(MembershipsListView.class)) {
			this.positionSection = new SideNavItem(null);
			if (this.accessChecker.hasAccess(MyProfileView.class)) {
				this.myprofile = new SideNavItem(null, MyProfileView.class, LineAwesomeIcon.USER_CIRCLE.create());
				this.positionSection.addItem(this.myprofile);
			}
			if (this.accessChecker.hasAccess(MyMembershipsView.class)) {
				this.positionSection.addItem(new SideNavItem("My Positions", MyMembershipsView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(PersonsListView.class)) {
				this.allpersons = new SideNavItem(null, PersonsListView.class, LineAwesomeIcon.USERS_SOLID.create());
				this.positionSection.addItem(this.allpersons);
			}
			if (this.accessChecker.hasAccess(MembershipsListView.class)) {
				this.positions = new SideNavItem("", MembershipsListView.class, LineAwesomeIcon.ID_CARD_ALT_SOLID.create()); //$NON-NLS-1$
				this.positionSection.addItem(this.positions);
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
				|| this.accessChecker.hasAccess(SupervisionsListView.class)
				|| this.accessChecker.hasAccess(JournalsListView.class)
				|| this.accessChecker.hasAccess(ConferencesListView.class)) {
			this.scientificActivitySection = new SideNavItem(null);
			if (this.accessChecker.hasAccess(MyPublicationsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("My Publications", MyPublicationsView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MySupervisorsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("My Supervisors", MySupervisorsView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyPublicationsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("My Supervisions", MySupervisorsView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(PublicationsView.class)) {
				this.scientificActivitySection.addItem(new SideNavItem("Publications", PublicationsView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(SupervisionsListView.class)) {
				this.supervisions = new SideNavItem("", SupervisionsListView.class, LineAwesomeIcon.GRADUATION_CAP_SOLID.create()); //$NON-NLS-1$
				this.scientificActivitySection.addItem(this.supervisions);
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
				|| this.accessChecker.hasAccess(ProjectsListView.class)
				|| this.accessChecker.hasAccess(MyAssociatedStructuresView.class)
				|| this.accessChecker.hasAccess(AssociatedStructuresListView.class)) {
			this.projectSection = new SideNavItem(""); //$NON-NLS-1$
			if (this.accessChecker.hasAccess(MyProjectsView.class)) {
				this.projectSection.addItem(new SideNavItem("My Projects", MyAssociatedStructuresView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyAssociatedStructuresView.class)) {
				this.projectSection.addItem(new SideNavItem("My Associated Structures", MyAssociatedStructuresView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(ProjectsListView.class)) {
				this.projects = new SideNavItem("", ProjectsListView.class, LineAwesomeIcon.CUBES_SOLID.create()); //$NON-NLS-1$
				this.projectSection.addItem(this.projects);
			}
			if (this.accessChecker.hasAccess(AssociatedStructuresListView.class)) {
				this.associatedStructures = new SideNavItem("", AssociatedStructuresListView.class, LineAwesomeIcon.LINK_SOLID.create()); //$NON-NLS-1$
				this.projectSection.addItem(this.associatedStructures);
			}
			nav.addItem(this.projectSection);
		}
	}

	/** Create the scientific influence items in the navigation panel.
	 *
	 * @param nav the navigation panel.
	 */
	protected void createScientificInfluenceNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyIncomingInvitationsView.class)
				|| this.accessChecker.hasAccess(MyOutgoingInvitationsView.class)
				|| this.accessChecker.hasAccess(IncomingInvitationsListView.class)
				|| this.accessChecker.hasAccess(OutgoingInvitationsListView.class)
				|| this.accessChecker.hasAccess(MyJurysView.class)
				|| this.accessChecker.hasAccess(JuryMembershipsListView.class)) {
			this.diffusionSection = new SideNavItem(""); //$NON-NLS-1$
			if (this.accessChecker.hasAccess(MyIncomingInvitationsView.class)) {
				this.diffusionSection.addItem(new SideNavItem("My Incoming Invitations", MyIncomingInvitationsView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyOutgoingInvitationsView.class)) {
				this.diffusionSection.addItem(new SideNavItem("My Outgoing Invitations", MyOutgoingInvitationsView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyJurysView.class)) {
				this.diffusionSection.addItem(new SideNavItem("My Jurys", MyJurysView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(IncomingInvitationsListView.class)) {
				this.incomingInvitations = new SideNavItem("", IncomingInvitationsListView.class, LineAwesomeIcon.SIGN_IN_ALT_SOLID.create()); //$NON-NLS-1$
				this.diffusionSection.addItem(this.incomingInvitations);
			}
			if (this.accessChecker.hasAccess(OutgoingInvitationsListView.class)) {
				this.outgoingInvitations = new SideNavItem("", OutgoingInvitationsListView.class, LineAwesomeIcon.SIGN_OUT_ALT_SOLID.create()); //$NON-NLS-1$
				this.diffusionSection.addItem(this.outgoingInvitations);
			}
			if (this.accessChecker.hasAccess(JuryMembershipsListView.class)) {
				this.juryMemberships = new SideNavItem("", JuryMembershipsListView.class, LineAwesomeIcon.USER_GRADUATE_SOLID.create()); //$NON-NLS-1$
				this.diffusionSection.addItem(this.juryMemberships);
			}
			nav.addItem(this.diffusionSection);
		}
	}

	/** Create the teaching items in the navigation panel.
	 *
	 * @param nav the navigation panel.
	 */
	protected void createTeachingNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyTeachingActivitiesView.class)
				|| this.accessChecker.hasAccess(TeachingActivitiesListView.class)) {
			this.teachingSection = new SideNavItem(""); //$NON-NLS-1$
			if (this.accessChecker.hasAccess(MyTeachingActivitiesView.class)) {
				this.teachingSection.addItem(new SideNavItem("My Activities", MyTeachingActivitiesView.class, LineAwesomeIcon.QUESTION_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(TeachingActivitiesListView.class)) {
				this.teachingActivites = new SideNavItem("", TeachingActivitiesListView.class, LineAwesomeIcon.CHALKBOARD_TEACHER_SOLID.create()); //$NON-NLS-1$
				this.teachingSection.addItem(this.teachingActivites);
			}
			nav.addItem(this.teachingSection);
		}
	}

	/** Create the structure management items in the navigation bar.
	 *
	 * @param nav the navigation bar.
	 */
	protected void createStructureManagementNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(OrganizationsListView.class)
				|| this.accessChecker.hasAccess(ScientificAxesListView.class)
				|| this.accessChecker.hasAccess(AddressesListView.class)) {
			this.organizationsSection = new SideNavItem(""); //$NON-NLS-1$
			if (this.accessChecker.hasAccess(OrganizationsListView.class)) {
				this.organizations = new SideNavItem("", OrganizationsListView.class, LineAwesomeIcon.UNIVERSITY_SOLID.create()); //$NON-NLS-1$
				this.organizationsSection.addItem(this.organizations);
			}
			if (this.accessChecker.hasAccess(ScientificAxesListView.class)) {
				this.scientificAxes = new SideNavItem("", ScientificAxesListView.class, LineAwesomeIcon.LAYER_GROUP_SOLID.create()); //$NON-NLS-1$
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

			createUserMenu(userName.getSubMenu());

			layout.add(userMenu);
		} else {
			this.loginLink = new Anchor("login", "");  //$NON-NLS-1$ //$NON-NLS-2$
			layout.add(this.loginLink);
		}
		return layout;
	}

	/** Invoked for creating the user menu.
	 *
	 * @param mainMenu the user menu to fill up.
	 */
	protected void createUserMenu(SubMenu mainMenu) {
		//
		// User role
		//
		this.roleInUserMenu = new Span();
		mainMenu.add(this.roleInUserMenu);

		//
		// Language selection
		//
		this.languagesLink = mainMenu.addItem(""); //$NON-NLS-1$s
		final var locales = LanguageSelect.getAvailableLocales();
		this.languageLinks = new LanguageRecord[locales.length];
		final var languageSubMenu = this.languagesLink.getSubMenu();
		final var currentLang = getLocale().getLanguage();
		final var langItems = new ArrayList<MenuItem>(locales.length);
		MenuItem currentLanguageItem = null;
		int i = 0;
		for (final var locale : locales) {
			final var hl = new HorizontalLayout();
			hl.setSpacing(false);
			hl.setAlignItems(Alignment.CENTER);

			final var countryCode = CountryCode.fromLocale(locale);
			final var flag = new CountryFlag(countryCode);
			flag.setSizeFromHeight(2, Unit.EX);
			flag.getStyle().set("margin-right", "var(--lumo-space-s)"); //$NON-NLS-1$ //$NON-NLS-2$

			final var text = new Span(""); //$NON-NLS-1$
			this.languageLinks[i] = new LanguageRecord(locale, text);
			hl.add(flag, text);
			final var menuItem = languageSubMenu.addItem(hl, e -> {
				final var current = e.getSource();
				langItems.stream().filter(it -> it != current).forEach(it -> it.setChecked(false));
				LanguageSelect.setUILocale(locale);
			});
			menuItem.setCheckable(true);
			langItems.add(menuItem);
			if (currentLanguageItem == null && currentLang.equalsIgnoreCase(locale.getLanguage())) {
				currentLanguageItem = menuItem;
			}
			++i;
		}
		if (currentLanguageItem != null) {
			currentLanguageItem.setChecked(true);
		}
		
		//
		// Logout
		//
		this.logoutLink = mainMenu.addItem("", e -> { //$NON-NLS-1$
			this.authenticatedUser.logout();
		});
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
			updateUserRoleInMenu(user.getRole());
		}
	}

	private void updateUserRoleInMenu(UserRole role) {
		if (role != null && this.roleInUserMenu != null) {
			final var roleName = role.getLabel(this.messages, getLocale());
			this.roleInUserMenu.setText(getTranslation("views.roleInUserMenu", roleName)); //$NON-NLS-1$
			String color = null;
			switch (role) {
			case USER:
				color = "var(--lumo-primary-text-color)"; //$NON-NLS-1$
				break;
			case RESPONSIBLE:
				color = "var(--lumo-success-text-color)"; //$NON-NLS-1$
				break;
			case ADMIN:
				color = "var(--lumo-error-text-color)"; //$NON-NLS-1$
				break;
			}
			if (color != null) {
				this.roleInUserMenu.getStyle().setColor(color);
			}
		}
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		if (this.authenticatedUser != null && this.authenticatedUser.get().isPresent()) {
			updateUserRoleInMenu(this.authenticatedUser.get().get().getRole());
		}
		
		this.viewTitle.setText(getCurrentPageTitle());

		if (this.positionSection != null) {
			this.positionSection.setLabel(getTranslation("views.navitem.positionSection")); //$NON-NLS-1$
		}
		if (this.myprofile != null) {
			this.myprofile.setLabel(this.getTranslation("views.navitem.my_profile")); //$NON-NLS-1$
		}
		if (this.allpersons != null) {
			this.allpersons.setLabel(getTranslation("views.navitem.all_persons")); //$NON-NLS-1$
		}
		if (this.positions != null) {
			this.positions.setLabel(getTranslation("views.navitem.positions")); //$NON-NLS-1$
		}
		
		if (this.scientificActivitySection != null) {
			this.scientificActivitySection.setLabel(getTranslation("views.navitem.scientificactivitiesSection")); //$NON-NLS-1$
		}
		if (this.journals != null) {
			this.journals.setLabel(getTranslation("views.navitem.journals")); //$NON-NLS-1$
		}
		if (this.conferences != null) {
			this.conferences.setLabel(getTranslation("views.navitem.conferences")); //$NON-NLS-1$
		}

		if (this.projectSection != null) {
			this.projectSection.setLabel(getTranslation("views.navitem.projectSection")); //$NON-NLS-1$
		}
		if (this.projects != null) {
			this.projects.setLabel(getTranslation("views.navitem.projects")); //$NON-NLS-1$
		}
		if (this.associatedStructures != null) {
			this.associatedStructures.setLabel(getTranslation("views.navitem.associated_structures")); //$NON-NLS-1$
		}

		if (this.teachingSection != null) {
			this.teachingSection.setLabel(getTranslation("views.navitem.teachingSection")); //$NON-NLS-1$
		}
		if (this.teachingActivites != null) {
			this.teachingActivites.setLabel(getTranslation("views.navitem.teaching_activities")); //$NON-NLS-1$
		}

		if (this.organizationsSection != null) {
			this.organizationsSection.setLabel(getTranslation("views.navitem.organizationsSection")); //$NON-NLS-1$
		}
		if (this.organizations != null) {
			this.organizations.setLabel(getTranslation("views.navitem.organizations")); //$NON-NLS-1$
		}
		if (this.scientificAxes != null) {
			this.scientificAxes.setLabel(getTranslation("views.navitem.scientific_axes")); //$NON-NLS-1$
		}
		if (this.addresses != null) {
			this.addresses.setLabel(getTranslation("views.navitem.addresses")); //$NON-NLS-1$
		}

		if (this.documentations != null) {
			this.documentations.setLabel(getTranslation("views.navitem.documentations")); //$NON-NLS-1$
		}
		if (this.documentation != null) {
			this.documentation.setLabel(getTranslation("views.navitem.online_manuals")); //$NON-NLS-1$
		}
		if (this.about != null) {
			this.about.setLabel(getTranslation("views.navitem.about_app")); //$NON-NLS-1$
		}

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

		if (this.diffusionSection != null) {
			this.diffusionSection.setLabel(getTranslation("views.navitem.diffusionSection")); //$NON-NLS-1$
		}
		if (this.incomingInvitations != null) {
			this.incomingInvitations.setLabel(getTranslation("views.navitem.incoming_invitations")); //$NON-NLS-1$
		}
		if (this.outgoingInvitations != null) {
			this.outgoingInvitations.setLabel(getTranslation("views.navitem.outgoing_invitations")); //$NON-NLS-1$
		}
		if (this.juryMemberships != null) {
			this.juryMemberships.setLabel(getTranslation("views.navitem.jury_memberships")); //$NON-NLS-1$
		}
		if (this.supervisions != null) {
			this.supervisions.setLabel(getTranslation("views.navitem.supervisions")); //$NON-NLS-1$
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
