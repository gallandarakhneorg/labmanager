package fr.utbm.ciad.labmanager.views.appviews.login;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "login")
@AnonymousAllowed
public class LoginView extends Composite<HorizontalLayout> implements BeforeEnterObserver {

    private static final long serialVersionUID = 4873621745334362590L;


    public LoginView() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        final VerticalLayout layoutColumn2 = new VerticalLayout();
        final Icon icon = new Icon();
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);
        icon.getElement().setAttribute("icon", "lumo:user");
        //layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, loginForm);
        getContent().add(layoutColumn2);
        layoutColumn2.add(icon);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    }
}
