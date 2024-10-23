package fr.utbm.ciad.labmanager.views.components.dashboard;

import com.vaadin.componentfactory.PopupVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.data.publication.Authorship;
import fr.utbm.ciad.labmanager.data.publication.AuthorshipRepository;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.componentfactory.Popup;

import java.util.List;

public class Reporting extends HorizontalLayout {

    private static final long serialVersionUID = 1L;

    public static final String TEST_TARGET_ELEMENT_ID = "show-notification-popup";

    private final PersonService personService;
    private final AuthorshipRepository authorshipRepository;
    private final AuthenticatedUser authenticatedUser;
    private final boolean hasOrcid;
    private final List<Publication> publicationsWithoutDoi;

    VerticalLayout orcidLayout = new VerticalLayout();
    VerticalLayout publicationLayout = new VerticalLayout();

    private final Icon icon = new Icon(VaadinIcon.BELL);
    private final Popup popup = new Popup();

    public Reporting(PersonService personService,
                     AuthorshipRepository authorshipRepository,
                     AuthenticatedUser authenticatedUser) {
        this.personService = personService;
        this.authorshipRepository = authorshipRepository;
        this.authenticatedUser = authenticatedUser;

        addClassNames(LumoUtility.JustifyContent.END,
                LumoUtility.Margin.Right.SMALL, LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL);

        hasOrcid = checkOrcid();
        publicationsWithoutDoi = checkPublicationWithoutDoi();
        generateLayout();
        initPopup();

        icon.setId(TEST_TARGET_ELEMENT_ID);
        icon.addClickListener(event -> {
            popup.setOpened(!popup.isOpened());
        });
        popup.setFor(TEST_TARGET_ELEMENT_ID);
        popup.setCloseOnScroll(true);
        popup.setModeless(true);
        popup.setIgnoreTargetClick(true);
        popup.addThemeVariants(PopupVariant.LUMO_POINTER_ARROW);
        popup.setCloseOnClick(true);
        Div d = new Div(icon);
        d.addClassName("tagged");
        add(d);
        d.getElement().setAttribute("notifications", "10+");
        add(popup);

    }

    private void initPopup() {
        popup.add(orcidLayout, publicationLayout);
    }

    private void generateLayout() {
        if (!hasOrcid) {
            orcidLayout.add("• You have not set your ORCID identifier. Please set it in your profile.");
        }
        if (publicationsWithoutDoi != null && !publicationsWithoutDoi.isEmpty()) {
            publicationLayout.add("• You have publications without DOI. Please set the DOI in the publication details.");
            Grid<String> publicationGrid = new Grid<>();
            publicationGrid.setItems(publicationsWithoutDoi.stream().map(Publication::getTitle).toList().subList(0, Math.min(publicationsWithoutDoi.size(), 4)));
            publicationGrid.addColumn(String::toString);
            publicationLayout.add(publicationGrid);
            Button seeMoreButton = new Button("See More", event -> {
                if (popup.isOpened())
                {
                    popup.setOpened(false);
                }
                final var session = VaadinService.getCurrentRequest().getWrappedSession();
                session.setAttribute(new StringBuilder().append(ViewConstants.MISSING_DOI_FILTER_ROOT).toString(), true);
                getUI().ifPresent(ui -> ui.navigate("publications"));
            });
            publicationLayout.add(seeMoreButton);
        }
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
            List<Authorship> authorships = authorshipRepository.findAuthorshipsByPersonId(authenticatedUser.get().get().getPerson().getId());
            List<Publication> publications = authorships.stream().map(Authorship::getPublication).toList();
            return publications.stream().filter(publication -> publication.getDOI() == null).toList();
        } else {
            return null;
        }
    }
}
