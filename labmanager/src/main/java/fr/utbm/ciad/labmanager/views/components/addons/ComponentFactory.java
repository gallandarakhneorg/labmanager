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

package fr.utbm.ciad.labmanager.views.components.addons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Locale;
import java.util.function.BiConsumer;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog.ConfirmEvent;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.IconFactory;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.server.StreamResource;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.vaadin.lineawesome.LineAwesomeIcon;

/** Factory of Vaadin components.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@org.springframework.stereotype.Component
public final class ComponentFactory {

	private ComponentFactory() {
		//
	}

	/** Create a form layout with multiple columns.
	 *
	 * @param columns the number of columns between 1 and 2.
	 * @return the layout.
	 */
	public static FormLayout newColumnForm(int columns) {
		assert columns >= 1 && columns <= 2;
		final var content = new FormLayout();
		switch (columns) {
		case 1:
			// No specific configuration for a single column
			break;
		case 2:
			content.setResponsiveSteps(
					new FormLayout.ResponsiveStep("0", 1), //$NON-NLS-1$
					new FormLayout.ResponsiveStep("20em", 2)); //$NON-NLS-1$
			break;
		default:
			throw new IllegalArgumentException();
		}
		return content;
	}

	/** Create a text field with a clickable 16x16 icon as suffix.
	 *
	 * @param href the URL of the target page.
	 * @param iconPath the path of the icon in the JAva resources, starting with a slash character.
	 * @return the field.
	 * @see #newClickableIconTextField(String, String, int)
	 */
	public static TextField newClickableIconTextField(String href, String iconPath) {
		return newClickableIconTextField(href, iconPath, 16);
	}

	/** Create a text field with a clickable icon as suffix.
	 *
	 * @param href the URL of the target page.
	 * @param iconPath the path of the icon in the JAva resources, starting with a slash character.
	 * @param iconSize the size of the icon in points.
	 * @return the field.
	 * @see #newClickableIconTextField(String, String)
	 */
	public static TextField newClickableIconTextField(String href, String iconPath, int iconSize) {
		assert !Strings.isNullOrEmpty(href);
		final var content = new TextField();
		final var imageResource = newStreamImage(iconPath);
		final var image = new Image(imageResource, href);
		image.setMinHeight(iconSize, Unit.POINTS);
		image.setMaxHeight(iconSize, Unit.POINTS);
		image.setMinWidth(iconSize, Unit.POINTS);
		image.setMaxWidth(iconSize, Unit.POINTS);
		final var anchor = new Anchor(href, image);
		anchor.setTitle(href);
		anchor.setTarget(AnchorTarget.BLANK);
		anchor.setTabIndex(-1);
		content.setSuffixComponent(anchor);
		return content;
	}

	/** Create a image stream with the image representing an empty background.
	 *
	 * @return the stream.
	 */
	public static StreamResource newEmptyBackgroundStreamImage() {
		return newStreamImage("/images/empty_background.png"); //$NON-NLS-1$
	}

	/** Create a image stream from an URL pointing a Java resource.
	 *
	 * @param iconPath the path of the icon in the Java resources, starting with a slash character.
	 * @return the stream.
	 */
	public static StreamResource newStreamImage(String iconPath) {
		assert !Strings.isNullOrEmpty(iconPath);
		final URL url;
		try {
			url = FileSystem.convertStringToURL(iconPath, false);
		} catch (Throwable ex) {
			throw new IllegalArgumentException(ex);
		}
		return new StreamResource(FileSystem.largeBasename(url),
				() -> ComponentFactory.class.getResourceAsStream(iconPath));
	}

	/** Create a resource stream from an URL pointing a Java resource.
	 *
	 * @param resourceFile the path to the server-side file.
	 * @return the resource to the server-side file.
	 */
	public static StreamResource newStreamImage(File resourceFile) {
		return new StreamResource(resourceFile.getName(), () -> {
			try {
				return new FileInputStream(resourceFile);
			} catch (IOException ex) {
				throw new IOError(ex);
			}
		});
	}

	/** Create a menu item with an icon and text and add it into the given receiver.
	 *
	 * @param menu the receiver of the new item.
	 * @param icon the icon of the item..
	 * @param label the label of the item.
	 * @param ariaLabel the aria label of the item. It is recommended to have this aria label not {@code null} or empty when
	 *     the {@code label} is {@code null} or empty, to enable disabled persons to have information on the feature of
	 *     the menu item.
	 * @param clickListener the listener on clicks on the item.
	 * @return the created item.
	 * @see #setIconItemText(MenuItem, String)
	 */
	public static MenuItem addIconItem(MenuBar menu, IconFactory icon, String label, String ariaLabel,
			ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
		return addIconItem(menu, icon, label, ariaLabel, false, clickListener);
	}

	/** Create a menu item with an icon and text and add it into the given receiver.
	 *
	 * @param menu the receiver of the new item.
	 * @param icon the icon of the item..
	 * @param label the label of the item.
	 * @param ariaLabel the aria label of the item. It is recommended to have this aria label not {@code null} or empty when
	 *     the {@code label} is {@code null} or empty, to enable disabled persons to have information on the feature of
	 *     the menu item.
	 * @return the created item.
	 * @see #setIconItemText(MenuItem, String)
	 */
	public static MenuItem addIconItem(MenuBar menu, IconFactory icon, String label, String ariaLabel) {
		return addIconItem(menu, icon, label, ariaLabel, false, null);
	}

	/** Create a menu item with an icon and text and add it into the given receiver.
	 *
	 * @param receiver the receiver of the new item.
	 * @param icon the icon of the item, never {@code null}.
	 * @param label the label of the item. It may be {@code null} for creating an item without text.
	 * @param ariaLabel the aria label of the item. It is recommended to have this aria label not {@code null} or empty when
	 *     the {@code label} is {@code null} or empty, to enable disabled persons to have information on the feature of
	 *     the menu item.
	 * @param isChild indicates if the item is for a sub-menu, when {@code true}; or the root item, when {@code false}.
	 * @return the created item.
	 * @see #setIconItemText(MenuItem, String)
	 */
	public static MenuItem addIconItem(HasMenuItems receiver, IconFactory icon, String label, String ariaLabel, boolean isChild) {
		return addIconItem(receiver, icon, label, ariaLabel, isChild, null);
	}

	/** Create a menu item with an icon and text and add it into the given receiver.
	 *
	 * @param receiver the receiver of the new item.
	 * @param icon the icon of the item, never {@code null}.
	 * @param label the label of the item. It may be {@code null} for creating an item without text.
	 * @param ariaLabel the aria label of the item. It is recommended to have this aria label not {@code null} or empty when
	 *     the {@code label} is {@code null} or empty, to enable disabled persons to have information on the feature of
	 *     the menu item.
	 * @param isChild indicates if the item is for a sub-menu, when {@code true}; or the root item, when {@code false}.
	 * @param clickListener the listener on clicks on the item.
	 * @return the created item.
	 * @see #setIconItemText(MenuItem, String)
	 */
	public static MenuItem addIconItem(HasMenuItems receiver, IconFactory icon, String label, String ariaLabel, boolean isChild,
			ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
		assert icon != null; 
		return addIconItem(receiver, icon.create(), label, ariaLabel, isChild, clickListener);
	}

	/** Create a menu item with an icon and text and add it into the given receiver.
	 *
	 * @param menu the receiver of the new item.
	 * @param icon the icon of the item..
	 * @param label the label of the item.
	 * @param ariaLabel the aria label of the item. It is recommended to have this aria label not {@code null} or empty when
	 *     the {@code label} is {@code null} or empty, to enable disabled persons to have information on the feature of
	 *     the menu item.
	 * @param clickListener the listener on clicks on the item.
	 * @return the created item.
	 * @see #setIconItemText(MenuItem, String)
	 */
	public static MenuItem addIconItem(MenuBar menu, LineAwesomeIcon icon, String label, String ariaLabel,
			ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
		return addIconItem(menu, icon, label, ariaLabel, false, clickListener);
	}

	/** Create a menu item with an icon and text and add it into the given receiver.
	 *
	 * @param menu the receiver of the new item.
	 * @param icon the icon of the item..
	 * @param label the label of the item.
	 * @param ariaLabel the aria label of the item. It is recommended to have this aria label not {@code null} or empty when
	 *     the {@code label} is {@code null} or empty, to enable disabled persons to have information on the feature of
	 *     the menu item.
	 * @return the created item.
	 * @see #setIconItemText(MenuItem, String)
	 */
	public static MenuItem addIconItem(MenuBar menu, LineAwesomeIcon icon, String label, String ariaLabel) {
		return addIconItem(menu, icon, label, ariaLabel, false, null);
	}

	/** Create a menu item with an icon and text and add it into the given receiver.
	 *
	 * @param receiver the receiver of the new item.
	 * @param icon the icon of the item, never {@code null}.
	 * @param label the label of the item. It may be {@code null} for creating an item without text.
	 * @param ariaLabel the aria label of the item. It is recommended to have this aria label not {@code null} or empty when
	 *     the {@code label} is {@code null} or empty, to enable disabled persons to have information on the feature of
	 *     the menu item.
	 * @param isChild indicates if the item is for a sub-menu, when {@code true}; or the root item, when {@code false}.
	 * @return the created item.
	 * @see #setIconItemText(MenuItem, String)
	 */
	public static MenuItem addIconItem(HasMenuItems receiver, LineAwesomeIcon icon, String label, String ariaLabel, boolean isChild) {
		return addIconItem(receiver, icon, label, ariaLabel, isChild, null);
	}

	/** Create a menu item with an icon and text and add it into the given receiver.
	 *
	 * @param receiver the receiver of the new item.
	 * @param icon the icon of the item, never {@code null}.
	 * @param label the label of the item. It may be {@code null} for creating an item without text.
	 * @param ariaLabel the aria label of the item. It is recommended to have this aria label not {@code null} or empty when
	 *     the {@code label} is {@code null} or empty, to enable disabled persons to have information on the feature of
	 *     the menu item.
	 * @param isChild indicates if the item is for a sub-menu, when {@code true}; or the root item, when {@code false}.
	 * @param clickListener the listener on clicks on the item.
	 * @return the created item.
	 * @see #setIconItemText(MenuItem, String)
	 */
	public static MenuItem addIconItem(HasMenuItems receiver, LineAwesomeIcon icon, String label, String ariaLabel, boolean isChild,
			ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
		assert icon != null; 
		return addIconItem(receiver, icon.create(), label, ariaLabel, isChild, clickListener);
	}

	private static <T extends com.vaadin.flow.component.Component & HasStyle> MenuItem addIconItem(
			HasMenuItems receiver, T iconInstance, String label, String ariaLabel, boolean isChild,
			ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
		assert iconInstance != null; 
		if (isChild) {
			iconInstance.getStyle().setWidth("var(--lumo-icon-size-s)"); //$NON-NLS-1$
			iconInstance.getStyle().setHeight("var(--lumo-icon-size-s)"); //$NON-NLS-1$
			iconInstance.getStyle().set("marginRight", "var(--lumo-space-s)"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		var item = receiver.addItem(iconInstance, clickListener);
		if (!Strings.isNullOrEmpty(ariaLabel)) {
			item.setAriaLabel(ariaLabel);
		}
		if (!Strings.isNullOrEmpty(label)) {
			item.add(new Text(label));
		}
		return item;
	}

	/** Change te text of the given menu item assuming it was build with an icon.
	 *
	 * @param item the item to change..
	 * @param text the new text.
	 * @return the item.
	 * @see #addIconItem(MenuBar, LineAwesomeIcon, String, String)
	 * @see #addIconItem(MenuBar, IconFactory, String, String)
	 * @see #addIconItem(HasMenuItems, LineAwesomeIcon, String, String, boolean, ComponentEventListener)
	 * @see #addIconItem(HasMenuItems, IconFactory, String, String, boolean, ComponentEventListener)
	 */
	public static MenuItem setIconItemText(MenuItem item, String text) {
		final var eltOpt = item.getElement().getChildren()
				.filter(it -> it.getComponent().isPresent() && it.getComponent().get() instanceof Text)
				.map(it -> (Text) it.getComponent().get())
				.findAny();
		if (Strings.isNullOrEmpty(text)) {
			if (eltOpt.isPresent()) {
				final var elt = eltOpt.get();
				elt.removeFromParent();
			}
		} else if (eltOpt.isPresent()) {
			final var elt = eltOpt.get();
			elt.setText(text);
		} else {
			item.add(new Text(text));
		}
		return item;
	}

	/** Create a dialog that asks for a critical question and that is modal.
	 * This function does not attach an event handler to the confirm and cancel buttons.
	 *
	 * @param parent the parent component; mostly used for obtaining the translation of text.
	 * @param title the title of the box.
	 * @param message the message in the box.
	 * @return the dialog.
	 */
	public static ConfirmDialog createDeletionDialog(com.vaadin.flow.component.Component parent, String title, String message) {
		return createDeletionDialog(parent, title, message, null);
	}

	/** Create a dialog that asks for a critical question and that is modal.
	 *
	 * @param parent the parent component; mostly used for obtaining the translation of text.
	 * @param title the title of the box.
	 * @param message the message in the box.
	 * @param confirmHandler the handler invoked when the confirm button is pushed.
	 * @return the dialog.
	 */
	public static ConfirmDialog createDeletionDialog(com.vaadin.flow.component.Component parent, String title, String message,
			ComponentEventListener<ConfirmEvent> confirmHandler) {
		return createCriticalQuestionDialog(
				title, message,
				parent.getTranslation("views.delete"), //$NON-NLS-1$
				confirmHandler);
	}

	/** Create a dialog that asks for a critical question and that is modal.
	 * This function does not attach an event handler to the confirm and cancel buttons.
	 *
	 * @param title the title of the box.
	 * @param message the message in the box.
	 * @param confirmText the text of the confirm button.
	 * @param confirmHandler the handler invoked when the confirm button is pushed.
	 * @return the dialog.
	 */
	public static ConfirmDialog createCriticalQuestionDialog(String title, String message, String confirmText) {
		return createCriticalQuestionDialog(title, message, confirmText, null);
	}

	/** Create a dialog that asks for a critical question and that is modal.
	 *
	 * @param title the title of the box.
	 * @param message the message in the box.
	 * @param confirmText the text of the confirm button.
	 * @param confirmHandler the handler invoked when the confirm button is pushed.
	 * @return the dialog.
	 */
	public static ConfirmDialog createCriticalQuestionDialog(String title, String message, String confirmText,
			ComponentEventListener<ConfirmEvent> confirmHandler) {
		final var dialog = new ConfirmDialog();
		dialog.setConfirmButtonTheme("error primary"); //$NON-NLS-1$
		dialog.setHeader(title);
		dialog.setText(message);
		dialog.setConfirmText(confirmText);
		dialog.setCancelable(true);
		dialog.setCloseOnEsc(true);
		if (confirmHandler != null) {
			dialog.addConfirmListener(confirmHandler);
		}
		return dialog;
	}

	/** Add an hidden column in the grid (usually the last column) for supporting the hover menu bar.
	 *
	 * @param <T> the type of data in the grid.
	 * @param grid the grid to update.
	 * @param generator the callback function for creating the buttons of the menu bar.
	 */
	public static <T> void addGridCellHoverMenuBar(Grid<T> grid, BiConsumer<T, MenuBar> generator) {
		grid.addComponentColumn(it -> {
			final var menuBar = new MenuBar();
			menuBar.addClassName("hoverGridHlHide"); //$NON-NLS-1$
			menuBar.addThemeVariants(MenuBarVariant.LUMO_ICON, MenuBarVariant.LUMO_SMALL, MenuBarVariant.LUMO_TERTIARY_INLINE);
			generator.accept(it, menuBar);
			return menuBar;
		}).setTextAlign(ColumnTextAlign.END).setKey("controls").setAutoWidth(true).setFlexGrow(0).setWidth("0px"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Open a standard dialog box for editing an information.
	 * This function provides three callbacks. One is the standard saving callback that is verifying the validity of
	 * the input data. Second, it is the standard validation callback that is saving the data after
	 * turning on the validation flag by the local authority. Third is the standard deletion callback that invoked
	 * after querying the user for accepting the deletion. 
	 *
	 * @param title the title of the dialog.
	 * @param content the content of the dialog, where the editing fields are located.
	 * @param mapEnterKeyToSave if {@code true}, the "save" button is activated when the {@code Enter}
	 *     is pushed. If {@code false}, the {@code Enter} is not mapped to a component.
	 * @param enableValidationButton indicates if the 'Validate' button is enabled or not.
	 * @param saveDoneCallback the callback that is invoked after saving
	 * @param deleteDoneCallback the callback that is invoked after deleting
	 */
	public static void openEditionModalDialog(String title, AbstractEntityEditor<?> content, boolean mapEnterKeyToSave,
			SerializableConsumer<Dialog> saveDoneCallback, SerializableConsumer<Dialog> deleteDoneCallback) {
		final SerializableConsumer<Dialog> validateCallback;
		if (content.isBaseAdmin()) {
			validateCallback = dialog -> {
				content.validateByOrganizationalStructureManager();
				if (content.isValidData()) {
					content.save();
				} else {
					content.notifyInvalidity();
				}
			};
		} else {
			validateCallback = null;
		}
		openEditionModalDialog(title, content, mapEnterKeyToSave,
				saveDoneCallback,
				validateCallback,
				deleteDoneCallback);
	}

	/** Open a standard dialog box for editing an information.
	 * This function provides a standard saving callback that is verifying the validity of
	 * the input data.
	 *
	 * @param title the title of the dialog.
	 * @param content the content of the dialog, where the editing fields are located.
	 * @param mapEnterKeyToSave if {@code true}, the "save" button is activated when the {@code Enter}
	 *     is pushed. If {@code false}, the {@code Enter} is not mapped to a component.
	 * @param enableValidationButton indicates if the 'Validate' button is enabled or not.
	 * @param saveDoneCallback the callback that is invoked after saving
	 * @param validateCallback the callback for validating the information.
	 * @param deleteDoneCallback the callback that is invoked after deleting
	 */
	public static void openEditionModalDialog(String title, AbstractEntityEditor<?> content, boolean mapEnterKeyToSave,
			SerializableConsumer<Dialog> saveDoneCallback,
			SerializableConsumer<Dialog> validateCallback,
			SerializableConsumer<Dialog> deleteDoneCallback) {
		final SerializableConsumer<Dialog> deleteCallback;
		if (deleteDoneCallback != null) {
			deleteCallback = dialog -> {
				final var confirmDialog = new ConfirmDialog();
				confirmDialog.setHeader(content.getTranslation("views.delete.entity")); //$NON-NLS-1$
				confirmDialog.setText(content.getTranslation("views.delete.entity.text")); //$NON-NLS-1$
				confirmDialog.setCancelable(true);
				confirmDialog.setConfirmText(content.getTranslation("views.delete")); //$NON-NLS-1$
				confirmDialog.setConfirmButtonTheme("error primary"); //$NON-NLS-1$
				confirmDialog.addConfirmListener(event -> {
					if (content.delete()) {
						dialog.close();
						deleteDoneCallback.accept(dialog);
					}
				});
				confirmDialog.open();
			};
		} else {
			deleteCallback = null;
		}
		doOpenEditionModalDialog(title, content, mapEnterKeyToSave,
				dialog -> {
					if (content.isValidData()) {
						if (content.save()) {
							dialog.close();
							if (saveDoneCallback != null) {
								saveDoneCallback.accept(dialog);
							}
						}
					} else {
						content.notifyInvalidity();
					}
				},
				validateCallback,
				deleteCallback);
	}

	/** Open a standard dialog box for editing an information.
	 *
	 * @param title the title of the dialog.
	 * @param content the content of the dialog, where the editing fields are located.
	 * @param mapEnterKeyToSave if {@code true}, the "save" button is activated when the {@code Enter}
	 *     is pushed. If {@code false}, the {@code Enter} is not mapped to a component.
	 * @param saveCallback the callback for saving the information.
	 * @param validateCallback the callback for validating the information.
	 * @param deleteCallback the callback for deleting the information.
	 */
	public static void doOpenEditionModalDialog(String title, Component content, boolean mapEnterKeyToSave,
			SerializableConsumer<Dialog> saveCallback,
			SerializableConsumer<Dialog> validateCallback,
			SerializableConsumer<Dialog> deleteCallback) {
		final var dialog = new Dialog();
		dialog.setModal(true);
		dialog.setCloseOnEsc(true);
		dialog.setCloseOnOutsideClick(true);
		dialog.setDraggable(true);
		dialog.setResizable(true);			
		dialog.setWidthFull();
		dialog.setHeaderTitle(title);
		dialog.add(content);

		final var saveButton = new Button(content.getTranslation("views.save"), e -> saveCallback.accept(dialog)); //$NON-NLS-1$
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		if (mapEnterKeyToSave) {
			saveButton.addClickShortcut(Key.ENTER);
		}
		saveButton.setIcon(LineAwesomeIcon.SAVE_SOLID.create());

		final var cancelButton = new Button(content.getTranslation("views.cancel"), e -> dialog.close()); //$NON-NLS-1$

		final Button deletionButton;
		if (deleteCallback != null) {
			deletionButton = new Button(content.getTranslation("views.delete"), //$NON-NLS-1$
					event -> deleteCallback.accept(dialog));
			deletionButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
			deletionButton.setIcon(LineAwesomeIcon.TRASH_SOLID.create());
		} else {
			deletionButton = null;
		}

		final Button validateButton;
		if (validateCallback != null) {
			validateButton = new Button(content.getTranslation("views.validate"), //$NON-NLS-1$
					event -> validateCallback.accept(dialog));
			validateButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
			validateButton.setIcon(LineAwesomeIcon.CHECK_DOUBLE_SOLID.create());
		} else {
			validateButton = null;
		}

		if (deletionButton != null) {
			if (validateButton != null) {
				dialog.getFooter().add(validateButton, cancelButton, deletionButton, saveButton);
			} else {
				dialog.getFooter().add(cancelButton, deletionButton, saveButton);
			}
		} else if (validateButton != null) {
			dialog.getFooter().add(validateButton, cancelButton, saveButton);
		} else {
			dialog.getFooter().add(cancelButton, saveButton);
		}
		
		dialog.open();
	}

	/** Create a combo box that contains the countries.
	 *
	 * @param locale the locale for rendering the country names.
	 * @return the combo box.
	 */
	public static ComboBox<CountryCode> newCountryComboBox(Locale locale) {
		final var combo = new ComboBox<CountryCode>();
		combo.setItems(CountryCode.getAllDisplayCountries(locale));
		combo.setItemLabelGenerator(it -> getCountryLabelForCombo(it, locale));
		combo.setValue(CountryCode.getDefault());
		return combo;
	}

	/** Update the items of the given combo box for displaying the country names according to the locale.
	 *
	 * @param combo the combo box to update.
	 * @param locale the locale for rendering the country names.
	 */
	public static void updateCountryComboBoxItems(ComboBox<CountryCode> combo, Locale locale) {
		combo.setItemLabelGenerator(it -> getCountryLabelForCombo(it, locale));
	}

	private static String getCountryLabelForCombo(CountryCode country, Locale locale) {
		return country.getDisplayCountry(locale);
	}

	/** Create a combo box that contains the languages.
	 *
	 * @param locale the locale for rendering the language names.
	 * @return the combo box.
	 */
	public static ComboBox<CountryCode> newLanguageComboBox(Locale locale) {
		final var combo = new ComboBox<CountryCode>();
		combo.setItems(CountryCode.getAllDisplayLanguages(locale));
		combo.setItemLabelGenerator(it -> getLanguageLabelForCombo(it, locale));
		combo.setValue(CountryCode.getDefault());
		return combo;
	}

	/** Update the items of the given combo box for displaying the country names according to the locale.
	 *
	 * @param combo the combo box to update.
	 * @param locale the locale for rendering the country names.
	 */
	public static void updateLanguageComboBoxItems(ComboBox<CountryCode> combo, Locale locale) {
		combo.setItemLabelGenerator(it -> getLanguageLabelForCombo(it, locale));
	}

	private static String getLanguageLabelForCombo(CountryCode country, Locale locale) {
		return StringUtils.capitalize(country.getDisplayLanguage(locale));
	}

	/** Convert the given comparator to a serializable comparator.
	 *
	 * @param <T> the type of data that is compared.
	 * @param comparator the comparator to convert.
	 * @return the serializable comparator.
	 */
	public static <T> SerializableComparator<T> toSerializableComparator(Comparator<T> comparator) {
		if (comparator == null) {
			return null;
		}
		if (comparator instanceof SerializableComparator<T> cmp) {
			return cmp;
		}
		return (a, b) -> comparator.compare(a, b);
	}

}
