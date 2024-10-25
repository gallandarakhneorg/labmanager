package fr.utbm.ciad.labmanager.views.components.dashboard;

import com.vaadin.componentfactory.Popup;
import com.vaadin.componentfactory.PopupVariant;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.data.publication.Authorship;
import fr.utbm.ciad.labmanager.data.publication.AuthorshipRepository;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.views.ViewConstants;

import java.util.LinkedHashMap;
import java.util.List;

@StyleSheet("themes/labmanager/addons/badge/css/notify-badge.css")
public class Reporting extends HorizontalLayout {

    private static final long serialVersionUID = 1L;

    public static final String TEST_TARGET_ELEMENT_ID = "show-notification-popup";

    private final PersonService personService;
    private final AuthorshipRepository authorshipRepository;
    private final AuthenticatedUser authenticatedUser;

    VerticalLayout orcidLayout = null;
    VerticalLayout publicationLayout = null;

    private final Div orcidDiv = new Div();
    private final Span badge = new Span();
    private final Icon icon = new Icon(LumoIcon.BELL.create().getIcon());
    private final Popup popup = new Popup();

    private int notificationCount = 0;

    public Reporting(PersonService personService,
                     AuthorshipRepository authorshipRepository,
                     AuthenticatedUser authenticatedUser) {
        this.personService = personService;
        this.authorshipRepository = authorshipRepository;
        this.authenticatedUser = authenticatedUser;

        initOrcidLayout();
        initPublicationLayout();
        initPopupContent();

        configureIconAndPopup();

        addClassNames(LumoUtility.JustifyContent.END,
                LumoUtility.Margin.Right.SMALL, LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL);


        add(orcidDiv);
        add(popup);
    }

    private void configureIconAndPopup() {
        popup.setFor(TEST_TARGET_ELEMENT_ID);
        popup.setCloseOnScroll(true);
        popup.setModeless(true);
        popup.setIgnoreTargetClick(true);
        popup.addThemeVariants(PopupVariant.LUMO_POINTER_ARROW);
        popup.setCloseOnClick(true);

        icon.getStyle().setScale("1.5");

        orcidDiv.addClassNames("item");
        orcidDiv.setId(TEST_TARGET_ELEMENT_ID);
        orcidDiv.addClickListener(event -> {
            popup.setOpened(!popup.isOpened());
        });

        badge.setText(String.valueOf(notificationCount));
        badge.addClassNames("notify-badge");

        orcidDiv.add(icon);
        orcidDiv.add(badge);
    }

    private void initOrcidLayout() {
        if (!checkOrcid()) {
            Text orcidText = new Text("• You have not set your ORCID identifier.");
            Button orcidButton = new Button("Set ORCID", event -> {
                closedPopup();
                redirectOrcid();
            });
            orcidLayout = new VerticalLayout();
            orcidLayout.add(orcidText, orcidButton);
            orcidLayout.setClassName(LumoUtility.Padding.Left.MEDIUM);
            orcidLayout.setClassName(LumoUtility.Padding.SMALL);
            notificationCount++;
        }
    }

    private void initPublicationLayout() {
        List<Publication> publicationsWithoutDoi = checkPublicationWithoutDoi();

        if (publicationsWithoutDoi != null && !publicationsWithoutDoi.isEmpty()) {
            notificationCount += publicationsWithoutDoi.size();
            Grid<String> publicationGrid = new Grid<>();
            int remainingPublications = publicationsWithoutDoi.size() - 4;

            LinkedHashMap<String, Publication> publicationMap = new LinkedHashMap<>();

            int i = 0;
            for (Publication publication : publicationsWithoutDoi) {
                if (i < 4) {
                    publicationMap.put(publication.getTitle(), publication);
                } else {
                    break;
                }
                i++;
            }
            if (remainingPublications > 0) {
                publicationMap.put("See more " + remainingPublications, null);
            }

            publicationGrid.setItems(publicationMap.keySet().stream().toList());
            publicationGrid.addColumn(String::toString).setSortable(false);
            publicationGrid.addItemDoubleClickListener(event -> {
                closedPopup();
                redirectDoi(publicationMap.get(event.getItem()));
            });

            publicationGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
            publicationGrid.getStyle().set("width", "300px").set("height", "180px")
                    .set("margin-left", "0.5rem").set("margin-top", "0.5rem")
                    .set("align-self", "unset");

            publicationLayout = new VerticalLayout();
            publicationLayout.add("• You have publications without DOI.");
            publicationLayout.add(publicationGrid);

            publicationLayout.setSpacing(true);
            publicationLayout.setPadding(true);
            publicationLayout.setWidthFull();
            publicationLayout.setClassName(LumoUtility.Padding.Left.MEDIUM);
            publicationLayout.setClassName(LumoUtility.Padding.SMALL);
        }
    }

    private void closedPopup() {
        if (popup.isOpened()) {
            popup.setOpened(false);
        }
    }

    private void initPopupContent() {
        if (orcidLayout != null) {
            popup.add(orcidLayout);
        }
        if (publicationLayout != null) {
            popup.add(publicationLayout);
        }
    }

    private void redirectDoi(Publication publication) {
        if (publication != null) {
            redirectDoiSingle(publication);
        } else {
            redirectDoiALl();
        }
    }

    private void redirectDoiALl() {
        final var session = VaadinService.getCurrentRequest().getWrappedSession();
        session.setAttribute(new StringBuilder().append(ViewConstants.MISSING_DOI_FILTER).toString(), true);
        getUI().ifPresent(ui -> ui.navigate("publications"));
    }

    private void redirectDoiSingle(Publication publication) {
        final var session = VaadinService.getCurrentRequest().getWrappedSession();
        session.setAttribute(new StringBuilder().append(ViewConstants.EDIT_DOI_FILTER).toString(), publication.getId());
        redirectDoiALl();
    }

    private void redirectOrcid() {
        final var session = VaadinService.getCurrentRequest().getWrappedSession();
        session.setAttribute(new StringBuilder().append(ViewConstants.EDIT_ORCID_FILTER).toString(), true);
        getUI().ifPresent(ui -> ui.navigate("myprofile"));
    }

    private Boolean checkOrcid() {
        if (authenticatedUser.get().isPresent()) {
            return personService.hasOrcid(authenticatedUser.get().get().getPerson());
        } else {
            return true;
        }
    }

    private List<Publication> checkPublicationWithoutDoi() {
        if (authenticatedUser.get().isPresent()) {
            List<Authorship> authorships = authorshipRepository.findAuthorshipsByPersonIdOrderByPublicationDesc(authenticatedUser.get().get().getPerson().getId());
            List<Publication> publications = authorships.stream().map(Authorship::getPublication).toList();
            return publications.stream().filter(publication -> publication.getDOI() == null).toList();
        } else {
            return null;
        }
    }


}
