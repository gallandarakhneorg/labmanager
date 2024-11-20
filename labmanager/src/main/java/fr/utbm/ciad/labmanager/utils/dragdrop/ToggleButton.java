package fr.utbm.ciad.labmanager.utils.dragdrop;

import com.vaadin.flow.component.button.Button;

public class ToggleButton extends Button {

    private boolean editionMode = false;

    private final Runnable setMode;
    private final Runnable removeMode;

    /**
     * Constructor that initializes the toggle button with the provided actions for setting and removing edition mode.
     *
     * @param setMode          the action to be performed when entering edition mode.
     * @param removeEditionMode the action to be performed when leaving edition mode.
     */
    public ToggleButton(Runnable setMode, Runnable removeEditionMode) {
        this.setMode = setMode;
        this.removeMode = removeEditionMode;
        setText(getTranslation("views.edit"));
        addClickListener(event -> changeMode());
    }

    /**
     * Changes the mode between editing and non-editing, updating the button text and running the appropriate action.
     *
     * @param editionMode boolean value representing whether to enter or leave edition mode.
     */
    private void changeMode(boolean editionMode) {
        if (this.isInTrueMode() != editionMode) {
            this.editionMode = editionMode;
        }
        if (editionMode) {
            setText(getTranslation("views.stop_edit"));
            setMode.run();
        } else {
            setText(getTranslation("views.edit"));
            removeMode.run();
        }
    }

    /**
     * Toggles the edition mode between true and false.
     * This method is triggered when the button is clicked.
     */
    private void changeMode() {
        changeMode(!editionMode);
    }

    /**
     * Sets the edition mode directly without toggling.
     *
     * @param editionMode boolean value representing the edition mode state.
     */
    public void editMode(boolean editionMode) {
        this.editionMode = editionMode;
    }

    /**
     * Checks if the button is currently in edition mode.
     *
     * @return true if the button is in edition mode, false otherwise.
     */
    public boolean isInTrueMode() {
        return editionMode;
    }
}
