package fr.utbm.ciad.labmanager.utils.contextMenu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.ContextMenu;

/**
 * The factory of a ContextMenu
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class ContextMenuFactory {

    /**
     * Default Constructor
     */
    public ContextMenuFactory(){

    }

    /**
     * Creates a context menu based on the specified menu type and associates it with the given target component.
     *
     * @param type            the type of context menu to create; can be BASIC, MANAGED, or CONDITIONAL.
     * @param targetComponent the component to which the context menu will be associated.
     * @return the created context menu instance.
     */
    public static ContextMenu createContextMenu(MenuType type, Component targetComponent) {
        ContextMenu menu = switch (type) {
            case CONDITIONAL -> new ConditionalManagedContextMenu();
            case MANAGED -> new ManagedContextMenu();
            default -> new ContextMenu();
        };
        menu.setTarget(targetComponent);
        return menu;
    }

    /**
     * Enumeration representing the types of context menus that can be created.
     */
    public enum MenuType {
        BASIC,
        MANAGED,
        CONDITIONAL
    }
}
