package fr.utbm.ciad.labmanager.views.components.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.server.VaadinService;
import fr.utbm.ciad.labmanager.data.publication.Authorship;
import fr.utbm.ciad.labmanager.data.publication.AuthorshipRepository;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.views.ViewConstants;

import java.util.List;

public class Reporting extends Button {

    private static final long serialVersionUID = 1L;

    private final PersonService personService;
    private final AuthorshipRepository authorshipRepository;
    private final AuthenticatedUser authenticatedUser;
    private final boolean hasOrcid;
    private final List<Publication> publicationsWithoutDoi;

    private Dialog infoDialog;

    VerticalLayout orcidLayout = new VerticalLayout();
    VerticalLayout publicationLayout = new VerticalLayout();

    public Reporting(PersonService personService,
                     AuthorshipRepository authorshipRepository,
                     AuthenticatedUser authenticatedUser) {
        this.personService = personService;
        this.authorshipRepository = authorshipRepository;
        this.authenticatedUser = authenticatedUser;
        hasOrcid = checkOrcid();
        publicationsWithoutDoi = checkPublicationWithoutDoi();
        generateLayout();

        this.setIcon(VaadinIcon.INFO.create());
        this.addClickListener(e -> {
            infoDialog = new Dialog();
            infoDialog.add("Reporting");
            SplitLayout layout = new SplitLayout(orcidLayout, publicationLayout);
            layout.setOrientation(SplitLayout.Orientation.VERTICAL);
            layout.setSplitterPosition(50);
            layout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
            infoDialog.add(layout);
            infoDialog.open();
        });
    }

    private void generateLayout() {
        if (!hasOrcid) {
            orcidLayout.add("You have not set your ORCID identifier. Please set it in your profile.");
        }
        if (publicationsWithoutDoi != null && !publicationsWithoutDoi.isEmpty()) {
            publicationLayout.add("You have publications without DOI. Please set the DOI in the publication details.");
            Grid<String> publicationGrid = new Grid<>();
            publicationGrid.setItems(publicationsWithoutDoi.stream().map(Publication::getTitle).toList().subList(0, Math.min(publicationsWithoutDoi.size(), 4)));
            publicationGrid.addColumn(String::toString);
            publicationLayout.add(publicationGrid);
            Button seeMoreButton = new Button("See More", event -> {
                infoDialog.close();
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
