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
import java.util.Optional;

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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
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
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.user.User;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.appviews.assocstructures.AssociatedStructuresView;
import fr.utbm.ciad.labmanager.views.appviews.assocstructures.MyAssociatedStructuresView;
import fr.utbm.ciad.labmanager.views.appviews.conferences.ConferencesView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.IncomingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.MyIncomingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.MyOutgoingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.invitations.OutgoingInvitationsView;
import fr.utbm.ciad.labmanager.views.appviews.journals.JournalsView;
import fr.utbm.ciad.labmanager.views.appviews.jurys.JurysView;
import fr.utbm.ciad.labmanager.views.appviews.jurys.MyJurysView;
import fr.utbm.ciad.labmanager.views.appviews.organizations.AddressesView;
import fr.utbm.ciad.labmanager.views.appviews.organizations.OrganizationsView;
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

	private Avatar avatar;

	private Text username;

	/** Constructor.
	 *
	 * @param authenticatedUser the logged-in user.
	 * @param accessChecker the access checker.
	 * @param applicationName name of the application.
	 */
	public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker,
			@Value("${labmanager.application-name}") String applicationName) {
		this.authenticatedUser = authenticatedUser;
		this.accessChecker = accessChecker;
		this.applicationName = applicationName;
		setPrimarySection(Section.DRAWER);
		addDrawerContent();
		addHeaderContent();
	}

	private void addHeaderContent() {
		final DrawerToggle toggle = new DrawerToggle();
		toggle.setAriaLabel(getTranslation("views.menu_toggle")); //$NON-NLS-1$
		this.viewTitle = new H2();
		this.viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
		addToNavbar(true, toggle, this.viewTitle);
	}

	private void addDrawerContent() {
		final H1 appName = new H1(this.applicationName);
		appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
		final Header header = new Header(appName);
		final Scroller scroller = new Scroller(createNavigation());
		addToDrawer(header, scroller, createFooter());
	}

	/** Create the welcome items in the navigation bar.
	 *
	 * @param nav the navigation bar.
	 */
	protected void createWelcomeNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(WelcomeView.class)) {
			nav.addItem(new SideNavItem("Welcome", WelcomeView.class));
		}
	}

	/** Create the profile and career items in the navigation bar.
	 *
	 * @param nav the navigation bar.
	 */
	protected void createProfileNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyProfileView.class)
				|| this.accessChecker.hasAccess(MyMembershipsView.class)
				|| this.accessChecker.hasAccess(PersonsView.class)
				|| this.accessChecker.hasAccess(MembershipsView.class)) {
			final SideNavItem positions = new SideNavItem(getTranslation("views.navitem.position")); //$NON-NLS-1$
			if (this.accessChecker.hasAccess(MyProfileView.class)) {
				positions.addItem(new SideNavItem(getTranslation("views.navitem.my_profile"), MyProfileView.class, LineAwesomeIcon.USER_CIRCLE.create())); //$NON-NLS-1$
			}
			if (this.accessChecker.hasAccess(MyMembershipsView.class)) {
				positions.addItem(new SideNavItem("My Positions", MyMembershipsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(PersonsView.class)) {
				positions.addItem(new SideNavItem(getTranslation("views.navitem.all_persons"), PersonsView.class, LineAwesomeIcon.USERS_SOLID.create())); //$NON-NLS-1$
			}
			if (this.accessChecker.hasAccess(MembershipsView.class)) {
				positions.addItem(new SideNavItem("Positions", MembershipsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			nav.addItem(positions);
		}
	}

	/** Create the scientific activities items in the navigation bar.
	 *
	 * @param nav the navigation bar.
	 */
	protected void createScientificActivitiesNavigation(SideNav nav) {
		if (this.accessChecker.hasAccess(MyPublicationsView.class)
				|| this.accessChecker.hasAccess(MySupervisorsView.class)
				|| this.accessChecker.hasAccess(MySupervisionsView.class)
				|| this.accessChecker.hasAccess(MySupervisionsView.class)
				|| this.accessChecker.hasAccess(PublicationsView.class)
				|| this.accessChecker.hasAccess(SupervisionsView.class)
				|| this.accessChecker.hasAccess(JournalsView.class)
				|| this.accessChecker.hasAccess(ConferencesView.class)) {
			final SideNavItem science = new SideNavItem("Scientific Activities");
			if (this.accessChecker.hasAccess(MyPublicationsView.class)) {
				science.addItem(new SideNavItem("My Publications", MyPublicationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MySupervisorsView.class)) {
				science.addItem(new SideNavItem("My Supervisors", MySupervisorsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(MyPublicationsView.class)) {
				science.addItem(new SideNavItem("My Supervisions", MySupervisorsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(PublicationsView.class)) {
				science.addItem(new SideNavItem("Publications", PublicationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(SupervisionsView.class)) {
				science.addItem(new SideNavItem("Supervisions", SupervisionsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(JournalsView.class)) {
				science.addItem(new SideNavItem("Journals", JournalsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(ConferencesView.class)) {
				science.addItem(new SideNavItem("Conferences", ConferencesView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			nav.addItem(science);
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

	/** Create the scientific influence items in the navigation bar.
	 *
	 * @param nav the navigation bar.
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

	/** Create the teaching items in the navigation bar.
	 *
	 * @param nav the navigation bar.
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
		if (this.accessChecker.hasAccess(OrganizationsView.class)
				|| this.accessChecker.hasAccess(ScientificAxesView.class)
				|| this.accessChecker.hasAccess(AddressesView.class)) {
			final SideNavItem organizations = new SideNavItem("Organizations");
			if (this.accessChecker.hasAccess(OrganizationsView.class)) {
				organizations.addItem(new SideNavItem("Orgnizations", OrganizationsView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(ScientificAxesView.class)) {
				organizations.addItem(new SideNavItem("Scientific Axes", ScientificAxesView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			if (this.accessChecker.hasAccess(AddressesView.class)) {
				organizations.addItem(new SideNavItem("Addresses", AddressesView.class, LineAwesomeIcon.USER_TIE_SOLID.create()));
			}
			nav.addItem(organizations);
		}
	}

	/** Create the documentation items in the navigation bar.
	 *
	 * @param nav the navigation bar.
	 */
	protected void createDocumentationNavigation(SideNav nav) {
		final SideNavItem documentations = new SideNavItem(getTranslation("views.navitem.documentations")); //$NON-NLS-1$
		documentations.addItem(new SideNavItem(getTranslation("views.navitem.online_manuals"), ViewConstants.ONLINE_MANUAL_URL, LineAwesomeIcon.BOOK_SOLID.create())); //$NON-NLS-1$
		nav.addItem(documentations);
	}

	private SideNav createNavigation() {
		final SideNav nav = new SideNav();
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

	private Footer createFooter() {
		final Footer layout = new Footer();
		final Optional<User> maybeUser = this.authenticatedUser.get();
		if (maybeUser.isPresent()) {
			final User user = maybeUser.get();
			final Person person = user.getPerson();
			
			this.avatar = new Avatar(person.getFullName());
			final URL photo = person.getPhotoURL();
			if (photo != null) {
				this.avatar.setImage(photo.toExternalForm());
			} else {
				this.avatar.setImage(null);
			}
			this.avatar.setThemeName("xsmall"); //$NON-NLS-1$
			this.avatar.getElement().setAttribute("tabindex", "-1"); //$NON-NLS-1$//$NON-NLS-2$

			this.username = new Text(person.getFullName());

			final MenuBar userMenu = new MenuBar();
			userMenu.setThemeName("tertiary-inline contrast"); //$NON-NLS-1$

			final MenuItem userName = userMenu.addItem(""); //$NON-NLS-1$
			final Div div = new Div();
			div.add(this.avatar);
			div.add(this.username);
			div.add(new Icon("lumo", "dropdown")); //$NON-NLS-1$//$NON-NLS-2$
			div.getElement().getStyle().set("display", "flex"); //$NON-NLS-1$//$NON-NLS-2$
			div.getElement().getStyle().set("align-items", "center"); //$NON-NLS-1$//$NON-NLS-2$
			div.getElement().getStyle().set("gap", "var(--lumo-space-s)"); //$NON-NLS-1$//$NON-NLS-2$
			userName.add(div);
			this.logoutLink = userName.getSubMenu().addItem("", e -> { //$NON-NLS-1$
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

	private String getCurrentPageTitle() {
		final Object obj = getContent();
		if (obj == null) {
			return ""; //$NON-NLS-1$
		}
		if (obj instanceof HasDynamicTitle) {
			return ((HasDynamicTitle) obj).getPageTitle();
		}
		final PageTitle title = obj.getClass().getAnnotation(PageTitle.class);
		return title == null ? "" : getTranslation(title.value()); //$NON-NLS-1$
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.viewTitle.setText(getCurrentPageTitle());
		if (this.loginLink != null) {
			this.loginLink.setText(getTranslation("views.sign_in")); //$NON-NLS-1$
		}
		if (this.logoutLink != null) {
			this.logoutLink.setText(getTranslation("views.sign_out")); //$NON-NLS-1$
		}
	}

	@Override
	public void authenticatedUserIdentityChange() {
		final Optional<User> maybeUser = this.authenticatedUser.get();
		if (maybeUser.isPresent()) {
			final User user = maybeUser.get();
			final Person person = user.getPerson();
			final String fullname = person.getFullName();
			if (this.avatar != null) {
				this.avatar.setName(fullname);
				final URL photo = person.getPhotoURL();
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

}
