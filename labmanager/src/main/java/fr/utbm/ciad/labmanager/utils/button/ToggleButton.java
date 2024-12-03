package fr.utbm.ciad.labmanager.utils.button;

import com.vaadin.flow.component.button.Button;

/**
 * A custom button that toggles between two modes, executing specific actions when the mode is changed.
 *
 * @author $Author: sgalland$
 * @author $Author: pschneiderlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class ToggleButton extends Button {

    private boolean mode = false;
    private final String textFirstMode;
    private final String textSecondMode;
    private final Runnable firstMode;
    private final Runnable secondMode;

    /**
     * Default Constructor
     */
    public ToggleButton(){
        this("Mode 1", null, "Mode 2", null);
    }

    /**
     * Constructs a ToggleButton with custom labels and actions for two modes.
     *
     * @param textFirstMode  the text to display on the button to switch to the first mode.
     * @param firstMode      the action to execute when switching to the first mode.
     * @param textSecondMode the text to display on the button to switch to the second mode.
     * @param secondMode     the action to execute when switching to the second mode.
     */
    public ToggleButton(String textFirstMode, Runnable firstMode, String textSecondMode, Runnable secondMode) {
        this.textFirstMode = textFirstMode;
        this.firstMode = firstMode;
        this.textSecondMode = textSecondMode;
        this.secondMode = secondMode;
        setText(textFirstMode);
        addClickListener(event -> changeMode());
    }

    /**
     * Toggles the button's mode and updates the text and behavior accordingly.
     *
     * @param mode true to first mode; false to second one.
     */
    private void changeMode(boolean mode) {
        if (mode) {
            setText(textSecondMode);
            firstMode.run();
        } else {
            setText(textFirstMode);
            secondMode.run();
        }
        if (this.mode != mode) {
            this.mode = mode;
        }
    }

    /**
     * Toggles the current mode.
     */
    private void changeMode() {
        changeMode(!mode);
    }

    /**
     * Manually sets the mode state without triggering any associated actions.
     *
     * @param mode true to firstMode mode; false to second one.
     */
    public void setMode(boolean mode) {
        this.mode = mode;
    }

    /**
     * Checks whether the button is currently in first mode.
     *
     * @return true if in first mode; false otherwise.
     */
    public boolean isInFirstMode() {
        return mode;
    }
}