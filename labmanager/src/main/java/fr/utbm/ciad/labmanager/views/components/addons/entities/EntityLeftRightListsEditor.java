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

package fr.utbm.ciad.labmanager.views.components.addons.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableComparator;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import org.vaadin.lineawesome.LineAwesomeIcon;

/** Component that enables to select a list of entities from a given list of available entities.
 * 
 * @param <T> the type of the edited entity.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public class EntityLeftRightListsEditor<T> extends CustomField<Set<T>> {

	private static final long serialVersionUID = 7855575001699179094L;

	private static final float LIST_HEIGHT = 200f;

	private Collection<T> originalAvailableEntities = new ArrayList<>();

	private final EmptySetMultiSelectListBox<T> availableEntities;

	private final EmptySetMultiSelectListBox<T> selectedEntities;

	private final Span availableEntityLabel;

	private final Span selectedEntityLabel;

	private final Button insertButton;

	private final Button deleteButton;

	private final Button additionButton;

	/** Constructor without the button for adding a new entity.
	 *
	 * @param comparator the comparator to be used for sorting the entities in the list.
	 */
	public EntityLeftRightListsEditor(Comparator<T> comparator) {
		this(comparator, null);
	}

	/** Constructor.
	 *
	 * @param comparator the comparator to be used for sorting the entities in the list.
	 * @param entityCreationCallback the callback that is invoked for creating a new entity and add it inside
	 *     the list of selected entities. Argument is a lambda function that must be invoked for
	 *     saving the new entity. The argument of this lambda function is the new entity itself.
	 */
	public EntityLeftRightListsEditor(Comparator<T> comparator, Consumer<Consumer<T>> entityCreationCallback) {
		this.availableEntityLabel = new Span(""); //$NON-NLS-1$
		this.availableEntityLabel.getStyle().set("font-size", "var(--lumo-font-size-xs)"); //$NON-NLS-1$ //$NON-NLS-2$
		
		this.availableEntities = new EmptySetMultiSelectListBox<>(comparator);

		this.selectedEntityLabel = new Span(""); //$NON-NLS-1$
		this.selectedEntityLabel.getStyle().set("font-size", "var(--lumo-font-size-xs)"); //$NON-NLS-1$ //$NON-NLS-2$

		this.selectedEntities = new EmptySetMultiSelectListBox<>(comparator);

		final var controlPanel = new VerticalLayout();
		controlPanel.setAlignItems(Alignment.CENTER);
		controlPanel.setMaxWidth(40f, Unit.PIXELS);
		controlPanel.setSpacing(false);
		controlPanel.setPadding(false);

		this.insertButton = new Button();
		this.insertButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
		this.insertButton.setIcon(LineAwesomeIcon.ARROW_ALT_CIRCLE_RIGHT_SOLID.create());
		this.insertButton.setEnabled(false);
		controlPanel.add(this.insertButton);

		this.deleteButton = new Button();
		this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
		this.deleteButton.setIcon(LineAwesomeIcon.ARROW_ALT_CIRCLE_LEFT_SOLID.create());
		this.deleteButton.setEnabled(false);
		controlPanel.add(this.deleteButton);

		if (entityCreationCallback != null) {
			this.additionButton = new Button();
			this.additionButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
			final var icon = LineAwesomeIcon.PLUS_CIRCLE_SOLID.create();
			icon.setColor("var(--lumo-error-color)"); //$NON-NLS-1$
			this.additionButton.setIcon(icon);
			controlPanel.add(this.additionButton);
		} else {
			this.additionButton = null;
		}

		final var availablePanel = new VerticalLayout(this.availableEntityLabel, this.availableEntities);
		availablePanel.setAlignItems(Alignment.START);
		availablePanel.setDefaultHorizontalComponentAlignment(Alignment.START);
		availablePanel.setSpacing(false);
		availablePanel.setPadding(false);
		availablePanel.getStyle().setBackground("var(--lumo-shade-5pct)"); //$NON-NLS-1$

		final var selectedPanel = new VerticalLayout(this.selectedEntityLabel, this.selectedEntities);
		selectedPanel.setAlignItems(Alignment.START);
		selectedPanel.setDefaultHorizontalComponentAlignment(Alignment.START);
		selectedPanel.setSpacing(false);
		selectedPanel.setPadding(false);
		selectedPanel.getStyle().setBackground("var(--lumo-shade-5pct)"); //$NON-NLS-1$
		
		final var horizontalLayout = new HorizontalLayout();
		horizontalLayout.add(availablePanel, controlPanel, selectedPanel);
		horizontalLayout.setAlignItems(Alignment.CENTER);
		horizontalLayout.setSpacing(false);
		horizontalLayout.setPadding(false);
		horizontalLayout.setFlexGrow(0f, controlPanel);
		add(horizontalLayout);

		this.availableEntities.addSelectionListener(it -> {
			final var hasValue = !it.getAllSelectedItems().isEmpty();
			this.insertButton.setEnabled(hasValue);
		});
		this.selectedEntities.addSelectionListener(it -> {
			final var hasValue = !it.getAllSelectedItems().isEmpty();
			this.deleteButton.setEnabled(hasValue);
		});
		this.insertButton.addClickListener(it -> doInsertion(true));
		this.deleteButton.addClickListener(it -> doDeletion(true));
		if (this.additionButton != null) {
			final var userCallback = entityCreationCallback;
			if (userCallback != null) {
				final Consumer<T> finalCallback = it -> {
					if (it != null) {
						addNewEntity(it, true);
					}
				};
				this.additionButton.addClickListener(it -> userCallback.accept(finalCallback));
			}
		}
	}

	/** Do the insertion of the selected items in the list of available
	 * entities into the list of selected entities.
	 *
	 * @param updateFieldValue indicates if the field component is notified
	 *     about a value change. In other words, the function
	 *     {@link #updateValue()} is invoked if this argument is evaluated
	 *     to {@code true} and it is not invoked otherwise.
	 */
	protected void doInsertion(boolean updateFieldValue) {
		final var currentSelection = this.availableEntities.getSelectedItems();
		if (currentSelection != null && !currentSelection.isEmpty()) {
			addSelectedEntities(currentSelection);
			this.selectedEntities.select(currentSelection);
			if (updateFieldValue) {
				updateValue();
			}
		}
	}

	/** Do the insertion of the given item. This function add the given item
	 * in the list of available entities and in the list of selected entities.
	 *
	 * @param item the new item.
	 * @param updateFieldValue indicates if the field component is notified
	 *     about a value change. In other words, the function
	 *     {@link #updateValue()} is invoked if this argument is evaluated
	 *     to {@code true} and it is not invoked otherwise.
	 */
	@SuppressWarnings("unchecked")
	protected void addNewEntity(T newEntity, boolean updateFieldValue) {
		this.originalAvailableEntities.add(newEntity);
		final var se = this.selectedEntities.getListDataView();
		se.addItem(newEntity);
		this.selectedEntities.select(newEntity);
		if (updateFieldValue) {
			updateValue();
		}
	}

	/** Do the deletion of the selected items in the list of selected
	 * entities and put them back in the list of available entities.
	 *
	 * @param updateFieldValue indicates if the field component is notified
	 *     about a value change. In other words, the function
	 *     {@link #updateValue()} is invoked if this argument is evaluated
	 *     to {@code true} and it is not invoked otherwise.
	 */
	protected void doDeletion(boolean updateFieldValue) {
		final var currentSelection = this.selectedEntities.getSelectedItems();
		if (currentSelection != null && !currentSelection.isEmpty()) {
			removeSelectedEntities(currentSelection);
			this.availableEntities.select(currentSelection);
			if (updateFieldValue) {
				updateValue();
			}
		}
	}

	/** Change the label for the list of available entities.
	 *
	 * @param text the text.
	 */
	public void setAvailableEntityLabel(String text) {
		this.availableEntityLabel.setText(text);
	}

	/** Change the label for the list of selected entities.
	 *
	 * @param text the text.
	 */
	public void setSelectedEntityLabel(String text) {
		this.selectedEntityLabel.setText(text);
	}

	/** Change the tooltip of the insertion button.
	 *
	 * @param text the text.
	 */
	public void setAdditionTooltip(String text) {
		this.insertButton.setTooltipText(text);
	}

	/** Change the tooltip of the deletion button.
	 *
	 * @param text the text.
	 */
	public void setDeletionTooltip(String text) {
		this.deleteButton.setTooltipText(text);
	}

	/** Change the tooltip of the creation button.
	 *
	 * @param text the text.
	 */
	public void setCreationTooltip(String text) {
		if (this.additionButton != null) {
			this.additionButton.setTooltipText(text);
		}
	}

	/** Change the renderer of the entities in the list of available entities
	 * and in the list of selected entities too.
	 *
	 * @param labelGenerator the generator of labels for the entities.
	 */
	public void setEntityLabelGenerator(ItemLabelGenerator<T> labelGenerator) {
		this.availableEntities.setItemLabelGenerator(labelGenerator);
		this.selectedEntities.setItemLabelGenerator(labelGenerator);
	}

	/** Change the renderer of the entities in the list of available entities
	 * and in the list of selected entities too.
	 *
	 * @param renderer the generator of labels for the entities.
	 */
	public void setEntityRenderer(ComponentRenderer<? extends Component, T> renderer) {
		this.availableEntities.setRenderer(renderer);
		this.selectedEntities.setRenderer(renderer);
	}

	/** Change the list of items that are proposed as available.
	 * The provided list is copied in a in-memory collection.
	 *
	 * @param items the list of items.
	 */
	public void setAvailableEntities(Collection<T> items) {
		if (items == null) {
			this.originalAvailableEntities = new ArrayList<>();
		} else {
			this.originalAvailableEntities = new ArrayList<>(items);
		}
		this.availableEntities.setEntities(items);
	}

	/** Clear the selected entities and put them back in the available entities.
	 */
	protected void clearSelectedEntities() {
		this.availableEntities.deselectAll();
		this.selectedEntities.deselectAll();
		//
		this.selectedEntities.clear();
		this.availableEntities.setEntities(this.originalAvailableEntities);
	}

	/** Add the given items in the list of selected items.
	 * This function adds only the entities that are inside
	 * the list of available entities.
	 *
	 * @param items the list of items to add to the list of selected items.
	 */
	protected void addSelectedEntities(Set<T> items) {
		final var ae = this.availableEntities.getListDataView();
		final var se = this.selectedEntities.getListDataView();
		for (var selectedItem : items) {
			if (ae.contains(selectedItem)) {
				ae.removeItem(selectedItem);
				se.addItem(selectedItem);
			}
		}
	}

	/** rmeove the given items from the list of selected items.
	 *
	 * @param items the list of items to remove from the list of selected items.
	 */
	protected void removeSelectedEntities(Set<T> items) {
		final var ae = this.availableEntities.getListDataView();
		final var se = this.selectedEntities.getListDataView();
		for (var selectedItem : items) {
			if (se.contains(selectedItem)) {
				se.removeItem(selectedItem);
				ae.addItem(selectedItem);
			}
		}
	}

	/** Change the list of selected items.
	 * The list of available items is updated for reflecting the selection.
	 *
	 * @param items the list of items.
	 */
	public void setSelectedEntities(Set<T> items) {
		clearSelectedEntities();
		addSelectedEntities(items);
		updateValue();
	}

	/** Replies the list of selected items.
	 *
	 * @return the collection of selected items.
	 */
	public Collection<T> getSelectedEntities() {
		return this.selectedEntities.getEntities();
	}

	@Override
	protected final Set<T> generateModelValue() {
		return new HashSet<>(getSelectedEntities());
	}

	@Override
	protected final void setPresentationValue(Set<T> newPresentationValue) {
		clearSelectedEntities();
		addSelectedEntities(newPresentationValue);
	}

	/** Multi-selection list with empty set as empty value.
	 * 
	 * @param <T> the type of the edited entity.
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private static class EmptySetMultiSelectListBox<T> extends MultiSelectListBox<T> {

		private static final long serialVersionUID = -5030533782286666677L;

		private final SerializableComparator<T> comparator;

		private ListDataProvider<T> dataProvider;

		/** Constructor.
		 *
		 * @param comparator the comparator to be used for sorting the entities in the list.
		 */
		EmptySetMultiSelectListBox(Comparator<T> comparator) {
			this.comparator = ComponentFactory.toSerializableComparator(comparator);
			setMinHeight(LIST_HEIGHT, Unit.PIXELS);
			setWidthFull();
			getStyle().setBackground("var(--lumo-shade-5pct)"); //$NON-NLS-1$
			setEntities(getEmptyValue());
		}

		/** Change the entities that are inside the list.
		 *
		 * @param entities the new collection of entities.
		 */
		void setEntities(Collection<T> entities) {
			this.dataProvider = new ListDataProvider<>(createStorageFor(entities));
			this.dataProvider.setSortComparator(this.comparator);
			setItems(this.dataProvider);
		}

		/** Replies the entities that are inside the list.
		 *
		 * @return the collection of entities in the list.
		 */
		Collection<T> getEntities() {
			return this.dataProvider.getItems();
		}

		/** Create a storage data structure that may be used for the internal lists for
		 * containing the given entities.
		 *
		 * @param entities the entities to put in the created data structure.
		 * @return the created data structure.
		 */
		Set<T> createStorageFor(Collection<T> entities) {
			final var newCollection = new HashSet<T>();
			if (entities != null && !entities.isEmpty()) {
				newCollection.addAll(entities);
			}
			return newCollection;
		}
		
		@Override
	    public Set<T> getEmptyValue() {
	        return new HashSet<>();
	    }

	}

}
