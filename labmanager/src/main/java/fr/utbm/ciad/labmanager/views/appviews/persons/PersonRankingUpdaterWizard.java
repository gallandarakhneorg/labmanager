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

package fr.utbm.ciad.labmanager.views.appviews.persons;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.dom.Style.AlignSelf;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.member.PersonService;
import fr.utbm.ciad.labmanager.services.member.PersonService.PersonRankingUpdateInformation;
import fr.utbm.ciad.labmanager.utils.SerializableExceptionProvider;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.appviews.persons.PersonRankingUpdate.PersonNewInformation;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.progress.ProgressExtension;
import fr.utbm.ciad.labmanager.views.components.addons.wizard.AbstractLabManagerProgressionWizardStep;
import fr.utbm.ciad.labmanager.views.components.addons.wizard.AbstractLabManagerWizard;
import io.overcoded.vaadin.wizard.AbstractFormWizardStep;
import io.overcoded.vaadin.wizard.WizardStep;
import io.overcoded.vaadin.wizard.config.WizardConfigurationProperties;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.criteria.Predicate;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

/** Wizard for updating the person rankings.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "updatepersonranking", layout = MainLayout.class)
@RolesAllowed({UserRole.RESPONSIBLE_GRANT, UserRole.ADMIN_GRANT})
public class PersonRankingUpdaterWizard extends AbstractLabManagerWizard<PersonRankingUpdate> {

	private static final long serialVersionUID = 753864429342217682L;

	/** Constructor.
	 *
	 * @param personService the service for accessing the person entities.
	 */
	public PersonRankingUpdaterWizard(@Autowired PersonService personService) {
		this(	personService,
				defaultWizardConfiguration(null, false),
				new PersonRankingUpdate());
	}

	/** Constructor.
	 *
	 * @param personService the service for accessing the person entities.
	 * @param properties the properties of the wizard.
	 * @param context the data context.
	 */
	protected PersonRankingUpdaterWizard(PersonService personService,  WizardConfigurationProperties properties, PersonRankingUpdate context) {
		this(properties, context, Arrays.asList(
				new PersonInputWizardStep(context),
				new PersonRankLoadingWizardStep(context, personService),
				new PersonRankDownloadWizardStep(context, personService),
				new PersonRankingSummaryWizardStep(context),
				new PersonRankSavingWizardStep(context, personService)));
	}

	private PersonRankingUpdaterWizard(WizardConfigurationProperties properties, PersonRankingUpdate context, List<WizardStep<PersonRankingUpdate>> steps) {
		super(properties, context, steps);
	}

	private static boolean isEnabled(ToggleButton button) {
		assert button != null;
		final var value = button.getValue();
		return value == null || value.booleanValue();
	}

	/** Wizard step to input configuration parameters.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class PersonInputWizardStep extends AbstractFormWizardStep<PersonRankingUpdate> {

		private static final long serialVersionUID = 5165672963156038933L;

		private ToggleButton wosEnable;

		private ToggleButton scopusEnable;

		private ToggleButton googleScolarEnable;

		/** Constructor.
		 *
		 * @param context the data context.
		 */
		public PersonInputWizardStep(PersonRankingUpdate context) {
			super(context, ComponentFactory.getTranslation("views.persons.updateRankings.step1.title"), 1); //$NON-NLS-1$
		}

		@Override
		protected Html getInformationMessage() {
			return null;
		}

		@Override
		public boolean isValid() {
			if (super.isValid()) {
				return isEnabled(this.wosEnable) || isEnabled(this.scopusEnable) || isEnabled(this.googleScolarEnable);
			}
			return false;
		}

		@Override
		protected boolean commitAfterContextUpdated() {
			return true;
		}

		@Override
		protected void createForm(FormLayout form) {
			this.wosEnable = new ToggleButton();
			this.wosEnable.setWidthFull();
			this.wosEnable.addValueChangeListener(it -> updateButtonStateForNextStep());
			form.add(this.wosEnable, 2);

			this.scopusEnable = new ToggleButton();
			this.scopusEnable.setWidthFull();
			this.scopusEnable.addValueChangeListener(it -> updateButtonStateForNextStep());
			form.add(this.scopusEnable, 2);

			this.googleScolarEnable = new ToggleButton();
			this.googleScolarEnable.setWidthFull();
			this.googleScolarEnable.addValueChangeListener(it -> updateButtonStateForNextStep());
			form.add(this.googleScolarEnable, 2);

			this.binder.forField(this.wosEnable).bind(PersonRankingUpdate::getWosEnable, PersonRankingUpdate::setWosEnable);
			this.binder.forField(this.scopusEnable).bind(PersonRankingUpdate::getScopusEnable, PersonRankingUpdate::setScopusEnable);
			this.binder.forField(this.googleScolarEnable).bind(PersonRankingUpdate::getGoogleScholarEnable, PersonRankingUpdate::setGoogleScholarEnable);
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			if (this.wosEnable != null) {
				this.wosEnable.setLabel(ComponentFactory.getTranslation(event.getLocale(), "views.persons.updateRankings.step1.enable_wos")); //$NON-NLS-1$
			}
			if (this.scopusEnable != null) {
				this.scopusEnable.setLabel(ComponentFactory.getTranslation(event.getLocale(), "views.persons.updateRankings.step1.enable_scopus")); //$NON-NLS-1$
			}
			if (this.googleScolarEnable != null) {
				this.googleScolarEnable.setLabel(ComponentFactory.getTranslation(event.getLocale(), "views.persons.updateRankings.step1.enable_googlescholar")); //$NON-NLS-1$
			}
		}

	}


	/** Wizard step for journal rank loading from the database.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class PersonRankLoadingWizardStep extends AbstractLabManagerProgressionWizardStep<PersonRankingUpdate> {

		private static final long serialVersionUID = 4363978455050034273L;

		private final PersonService personService;

		/** Constructor.
		 *
		 * @param context the data to be shared between the wizard steps.
		 * @param personService the service for accessing to the persons' JPA entities.
		 */
		public PersonRankLoadingWizardStep(PersonRankingUpdate context, PersonService personService) {
			super(context, ComponentFactory.getTranslation("views.persons.updateRankings.step2.title"), 2, 1, true, false);//$NON-NLS-1$
			this.personService = personService;
		}

		@Override
		public BiFunction<Component, String, String> getBackActionMessageSupplier() {
			return (cmp, tlt) -> ComponentFactory.getTranslation("views.persons.updateRankings.step2.back.message", tlt); //$NON-NLS-1$
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			getMajorText().ifPresent(it -> it.setText(ComponentFactory.getTranslation("views.persons.updateRankings.step2.comment"))); //$NON-NLS-1$
		}

		@Override
		protected SerializableExceptionProvider<String> createAsynchronousTask(int taskNo, Progression progression) {
			final var terminationMessage = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step2.person_read"); //$NON-NLS-1$
			return () -> {
				final var identifiers = getContext().getEntityIdentifiers();
				progression.increment(5);
				final List<Person> persons;
				if (identifiers == null || identifiers.isEmpty()) {
					persons = this.personService.getAllPersons();
				} else {
					persons = this.personService.getAllPersons((root, query, criteriaBuilder) -> {
						Predicate pred = null;
						for (final var id : identifiers) {
							final var p = criteriaBuilder.equal(root.get("id"), id); //$NON-NLS-1$
							if (pred == null) {
								pred = p;
							} else {
								pred = criteriaBuilder.or(pred, p);
							}
						}
						return pred;
					});
				}
				progression.increment(90);
				getContext().setPersons(persons);
				return terminationMessage;
			};
		}

	}

	/** Wizard step for downloading the ranking updates.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class PersonRankDownloadWizardStep extends AbstractLabManagerProgressionWizardStep<PersonRankingUpdate> {

		private static final long serialVersionUID = 8508847291993319473L;

		private final PersonService personService;

		/** Constructor.
		 *
		 * @param context the data to be shared between the wizard steps.
		 * @param personService the service for accessing to the persons' JPA entities.
		 */
		public PersonRankDownloadWizardStep(PersonRankingUpdate context, PersonService personService) {
			super(context, ComponentFactory.getTranslation("views.persons.updateRankings.step3.title"), 3, 3, true, false);//$NON-NLS-1$
			this.personService = personService;
		}

		@Override
		public BiFunction<Component, String, String> getBackActionMessageSupplier() {
			return (cmp, tlt) -> ComponentFactory.getTranslation("views.persons.updateRankings.step3.back.message", tlt); //$NON-NLS-1$
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			getMajorText().ifPresent(it -> it.setText(ComponentFactory.getTranslation("views.persons.updateRankings.step3.comment"))); //$NON-NLS-1$
		}

		@Override
		protected Component getProgressIcon(int taskNo) {
			Image image;
			switch (taskNo) {
			case 0:
				image = new Image(ComponentFactory.newStreamImage(ViewConstants.WOS_ICON), Person.WOS_BASE_URL);
				break;
			case 1:
				image = new Image(ComponentFactory.newStreamImage(ViewConstants.SCOPUS_ICON), Person.SCOPUS_BASE_URL);
				break;
			case 2:
				image = new Image(ComponentFactory.newStreamImage(ViewConstants.GSCHOLAR_ICON), Person.GSCHOLAR_BASE_URL);
				break;
			default:
				throw new IllegalArgumentException();
			}
			image.setMinHeight(ViewConstants.ICON_SIZE, Unit.POINTS);
			image.setMaxHeight(ViewConstants.MAX_ICON_SIZE, Unit.POINTS);
			image.setMinWidth(ViewConstants.ICON_SIZE, Unit.POINTS);
			image.setMaxWidth(ViewConstants.MAX_ICON_SIZE, Unit.POINTS);
			final var style = image.getStyle();
			style.setAlignSelf(AlignSelf.BASELINE);
			style.setMarginRight("var(--lumo-space-s)"); //$NON-NLS-1$
			return image;
		}

		@Override
		protected SerializableExceptionProvider<String> createAsynchronousTask(int taskNo, Progression progression) {
			switch (taskNo) {
			case 0:
				final var pattern0 = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step3.download_wos"); //$NON-NLS-1$
				final var extendedProgression0 = ProgressExtension.withCommentFormatter(progression, it -> MessageFormat.format(pattern0, it));
				final var terminationMessage0 = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step3.wos_downloaded"); //$NON-NLS-1$
				return () -> {
					final var ewos = getContext().getWosEnable();
					if (ewos) {
						getContext().clearWosRankings();
						this.personService.downloadPersonIndicatorsFromWoS(getContext().getPersons(), extendedProgression0,
								(personId, oldHindex, newHindex, oldCitations, newCitations) -> {
									getContext().addWosRanking(personId, oldHindex, newHindex, oldCitations, newCitations);
								});
					} else {
						progression.end();
					}
					return terminationMessage0;
				};
			case 1:
				final var pattern1 = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step3.download_scopus"); //$NON-NLS-1$
				final var extendedProgression1 = ProgressExtension.withCommentFormatter(progression, it -> MessageFormat.format(pattern1, it));
				final var terminationMessage1 = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step3.scopus_downloaded"); //$NON-NLS-1$
				return () -> {
					final var escopus = getContext().getScopusEnable();
					if (escopus) {
						getContext().clearScopusRankings();
						this.personService.downloadPersonIndicatorsFromScopus(getContext().getPersons(), extendedProgression1,
								(personId, oldHindex, newHindex, oldCitations, newCitations) -> {
									getContext().addScopusRanking(personId, oldHindex, newHindex, oldCitations, newCitations);
								});
					} else {
						progression.end();
					}
					return terminationMessage1;
				};
			case 2:
				final var pattern2 = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step3.download_googlescholar"); //$NON-NLS-1$
				final var extendedProgression2 = ProgressExtension.withCommentFormatter(progression, it -> MessageFormat.format(pattern2, it));
				final var terminationMessage2 = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step3.googlescholar_downloaded"); //$NON-NLS-1$
				return () -> {
					final var egs = getContext().getGoogleScholarEnable();
					if (egs) {
						getContext().clearGoogleScholarRankings();
						this.personService.downloadPersonIndicatorsFromGoogleScholar(getContext().getPersons(), extendedProgression2,
								(personId, oldHindex, newHindex, oldCitations, newCitations) -> {
									getContext().addGoogleScholarRanking(personId, oldHindex, newHindex, oldCitations, newCitations);
								});
					} else {
						progression.end();
					}
					return terminationMessage2;
				};
			default:
				throw new IllegalArgumentException();
			}
		}

	}

	/** Wizard step to summarize the updates of the person ranking informations.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class PersonRankingSummaryWizardStep extends WizardStep<PersonRankingUpdate> {

		private static final long serialVersionUID = 7506569906063637966L;

		private Grid<PersonNewInformation> grid;

		private Column<PersonNewInformation> nameColumn;

		private Column<PersonNewInformation> wosHindexColumn;

		private Column<PersonNewInformation> wosCitationColumn;

		private Column<PersonNewInformation> scopusHindexColumn;

		private Column<PersonNewInformation> scopusCitationColumn;

		private Column<PersonNewInformation> googleScholarHindexColumn;

		private Column<PersonNewInformation> googleScholarCitationColumn;

		/** Constructor.
		 *
		 * @param context the data context.
		 */
		public PersonRankingSummaryWizardStep(PersonRankingUpdate context) {
			super(context, ComponentFactory.getTranslation("views.persons.updateRankings.step4.title"), 4); //$NON-NLS-1$
		}

		@Override
		public Div getLayout() {
			this.grid = new Grid<>(PersonNewInformation.class, false);
			this.grid.setPageSize(ViewConstants.GRID_PAGE_SIZE);
			this.grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
			this.grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
			this.grid.setSelectionMode(SelectionMode.NONE);

			this.nameColumn = this.grid.addColumn(info -> info.person().getFullNameWithLastNameFirst())
					.setAutoWidth(true).setSortable(true);
			if (getContext().getWosEnable()) {
				this.wosHindexColumn = this.grid.addColumn(info -> toString(info.oldWosHindex(), info.newWosHindex()))
						.setAutoWidth(true).setSortable(true);
				this.wosCitationColumn = this.grid.addColumn(info -> toString(info.oldWosCitations(), info.newWosCitations()))
						.setAutoWidth(true).setSortable(true);
			} else {
				this.wosHindexColumn = null;
				this.wosCitationColumn = null;
			}
			if (getContext().getScopusEnable()) {
				this.scopusHindexColumn = this.grid.addColumn(info -> toString(info.oldScopusHindex(), info.newScopusHindex()))
						.setAutoWidth(true).setSortable(true);
				this.scopusCitationColumn = this.grid.addColumn(info -> toString(info.oldScopusCitations(), info.newScopusCitations()))
						.setAutoWidth(true).setSortable(true);
			} else {
				this.scopusHindexColumn = null;
				this.scopusCitationColumn = null;
			}
			if (getContext().getGoogleScholarEnable()) {
				this.googleScholarHindexColumn = this.grid.addColumn(info -> toString(info.oldGoogleScholarHindex(), info.newGoogleScholarHindex()))
						.setAutoWidth(true).setSortable(true);
				this.googleScholarCitationColumn = this.grid.addColumn(info -> toString(info.oldGoogleScholarCitations(), info.newGoogleScholarCitations()))
						.setAutoWidth(true).setSortable(true);
			} else {
				this.googleScholarHindexColumn = null;
				this.googleScholarCitationColumn = null;
			}

			this.grid.sort(GridSortOrder.asc(this.nameColumn).build());

			this.grid.setItems(new ListDataProvider<>(getContext().getPersonUpdates().toList()));

			return new Div(this.grid);
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			if (this.nameColumn != null) {
				this.nameColumn.setHeader(ComponentFactory.getTranslation("views.persons.updateRankings.step4.person_name")); //$NON-NLS-1$
			}
			if (this.wosHindexColumn != null) {
				this.wosHindexColumn.setHeader(ComponentFactory.getTranslation("views.persons.updateRankings.step4.wos_hindex")); //$NON-NLS-1$
			}
			if (this.wosCitationColumn != null) {
				this.wosCitationColumn.setHeader(ComponentFactory.getTranslation("views.persons.updateRankings.step4.wos_citations")); //$NON-NLS-1$
			}
			if (this.scopusHindexColumn != null) {
				this.scopusHindexColumn.setHeader(ComponentFactory.getTranslation("views.persons.updateRankings.step4.scopus_hindex")); //$NON-NLS-1$
			}
			if (this.scopusCitationColumn != null) {
				this.scopusCitationColumn.setHeader(ComponentFactory.getTranslation("views.persons.updateRankings.step4.scopus_citations")); //$NON-NLS-1$
			}
			if (this.googleScholarHindexColumn != null) {
				this.googleScholarHindexColumn.setHeader(ComponentFactory.getTranslation("views.persons.updateRankings.step4.googlescholar_hindex")); //$NON-NLS-1$
			}
			if (this.googleScholarCitationColumn != null) {
				this.googleScholarCitationColumn.setHeader(ComponentFactory.getTranslation("views.persons.updateRankings.step4.googlescholar_citations")); //$NON-NLS-1$
			}
		}

		private static String toString(Integer oldValue, Integer newValue) {
			if (oldValue != null && newValue != null) {
				final var ov = Math.max(0, oldValue.intValue());
				final var nv = Math.max(0, newValue.intValue());
				if (ov != nv) {
					return new StringBuilder()
							.append(ov)
							.append(" \u2B95 ") //$NON-NLS-1$
							.append(nv)
							.toString();
				}
			}
			return ""; //$NON-NLS-1$
		}

		@Override
		public boolean isValid() {
			return this.grid.getListDataView().getItemCount() > 0;
		}

		@Override
		public boolean commit() {
			return true;
		}

	}

	/** Wizard step for saving the ranking updates.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	protected static class PersonRankSavingWizardStep extends AbstractLabManagerProgressionWizardStep<PersonRankingUpdate> {

		private static final long serialVersionUID = -4974486467719332268L;

		private final PersonService personService;

		/** Constructor.
		 *
		 * @param context the data to be shared between the wizard steps.
		 * @param personService the service for accessing to the persons' JPA entities.
		 */
		public PersonRankSavingWizardStep(PersonRankingUpdate context, PersonService personService) {
			super(context, ComponentFactory.getTranslation("views.journals.updateRankings.step5.title"), 5, 1, false, true);//$NON-NLS-1$
			this.personService = personService;
		}

		@Override
		public BiFunction<Component, String, String> getBackActionMessageSupplier() {
			return (cmp, tlt) -> ComponentFactory.getTranslation("views.persons.updateRankings.step5.back.message", tlt); //$NON-NLS-1$
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			getMajorText().ifPresent(it -> it.setText(ComponentFactory.getTranslation("views.persons.updateRankings.step5.comment"))); //$NON-NLS-1$
		}

		@Override
		protected Component getProgressIcon(int taskNo) {
			final var image = VaadinIcon.COG.create();
			final var style = image.getStyle();
			style.setAlignSelf(AlignSelf.BASELINE);
			style.setMarginRight("var(--lumo-space-s)"); //$NON-NLS-1$
			return image;
		}

		@Override
		protected SerializableExceptionProvider<String> createAsynchronousTask(int taskNo, Progression progression) {
			final var pattern0 = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step5.saving"); //$NON-NLS-1$
			final var extendedProgression0 = ProgressExtension.withCommentFormatter(progression, it -> MessageFormat.format(pattern0, it));
			final var terminationMessage0 = getWizard().orElseThrow().getTranslation("views.persons.updateRankings.step5.end"); //$NON-NLS-1$
			return () -> {
				final var context = getContext();
				this.personService.updatePersonIndicators(
						context.getPersonUpdates().map(it -> {
							final var person = it.person();
							final var wosHindex = it.newWosHindex();
							final var wosCitations = it.newWosCitations();
							final var scopusHindex = it.newScopusHindex();
							final var scopusCitations = it.newScopusCitations();
							final var googleScholarHindex = it.newGoogleScholarHindex();
							final var googleScholarCitations = it.newGoogleScholarCitations();
							return new PersonRankingUpdateInformation(person,
									wosHindex, wosCitations, scopusHindex, scopusCitations, googleScholarHindex, googleScholarCitations);
						}).toList(),
						context.getWosEnable(), context.getScopusEnable(), context.getGoogleScholarEnable(), extendedProgression0);
				return terminationMessage0;
			};
		}

	}

}
