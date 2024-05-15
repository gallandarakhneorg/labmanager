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

package fr.utbm.ciad.labmanager.views.appviews.journals;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.dom.Style.AlignSelf;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.Route;
import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.data.user.UserRole;
import fr.utbm.ciad.labmanager.services.journal.JournalService;
import fr.utbm.ciad.labmanager.views.ViewConstants;
import fr.utbm.ciad.labmanager.views.appviews.MainLayout;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.progress.ProgressExtension;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullValueValidator;
import fr.utbm.ciad.labmanager.views.components.addons.wizard.AbstractLabManagerProgressionWizardStep;
import fr.utbm.ciad.labmanager.views.components.addons.wizard.AbstractLabManagerWizard;
import io.overcoded.vaadin.wizard.AbstractFormWizardStep;
import io.overcoded.vaadin.wizard.ExceptionRunnable;
import io.overcoded.vaadin.wizard.WizardStep;
import io.overcoded.vaadin.wizard.config.WizardConfigurationProperties;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.criteria.Predicate;
import org.arakhne.afc.progress.Progression;
import org.springframework.beans.factory.annotation.Autowired;

/** Wizard for updating the journal rankings.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Route(value = "updatejournalranking", layout = MainLayout.class)
@RolesAllowed({UserRole.RESPONSIBLE_GRANT, UserRole.ADMIN_GRANT})
public class JournalRankingUpdaterWizard extends AbstractLabManagerWizard<JournalRankingUpdate> {

	private static final long serialVersionUID = -3604347983449875564L;

	/** Constructor.
	 *
	 * @param journalService the service for accessing the journal entities.
	 */
	public JournalRankingUpdaterWizard(@Autowired JournalService journalService) {
		this(	journalService,
				defaultWizardConfiguration(null, false),
				new JournalRankingUpdate());
	}

	/** Constructor.
	 *
	 * @param journalService the service for accessing the journal entities.
	 * @param properties the properties of the wizard.
	 * @param context the data context.
	 */
	protected JournalRankingUpdaterWizard(JournalService journalService,  WizardConfigurationProperties properties, JournalRankingUpdate context) {
		this(properties, context, Arrays.asList(
				new JournalInputWizardStep(context),
				new JournalRankLoadingWizardStep(context, journalService),
				new JournalRankDownloadWizardStep(context, journalService)));
	}

	private JournalRankingUpdaterWizard(WizardConfigurationProperties properties, JournalRankingUpdate context, List<WizardStep<JournalRankingUpdate>> steps) {
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
	protected static class JournalInputWizardStep extends AbstractFormWizardStep<JournalRankingUpdate> {

		private static final long serialVersionUID = 5165672963156038933L;

		private ComboBox<Integer> yearPicker;

		private ToggleButton wosEnable;

		private ToggleButton scimagoEnable;

		private ToggleButton impactFactorsEnable;

		/** Constructor.
		 *
		 * @param context the data context.
		 */
		public JournalInputWizardStep(JournalRankingUpdate context) {
			super(context, ComponentFactory.getTranslation("views.journals.updateRankings.step1.title"), 1); //$NON-NLS-1$
		}

		@Override
		protected Html getInformationMessage() {
			return null;
		}

		@Override
		public boolean isValid() {
			if (super.isValid()) {
				return isEnabled(this.wosEnable) || isEnabled(this.scimagoEnable) || isEnabled(this.impactFactorsEnable);
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

			this.scimagoEnable = new ToggleButton();
			this.scimagoEnable.setWidthFull();
			this.scimagoEnable.addValueChangeListener(it -> updateButtonStateForNextStep());
			form.add(this.scimagoEnable, 2);

			this.impactFactorsEnable = new ToggleButton();
			this.impactFactorsEnable.setWidthFull();
			this.impactFactorsEnable.addValueChangeListener(it -> updateButtonStateForNextStep());
			form.add(this.impactFactorsEnable, 2);

			final var now = LocalDate.now();
			final var firstYear = now.getYear() - 10;
			final var lastYear = now.getYear() - 1;
			final var selectableYears = new ArrayList<Integer>();
			for (var i = lastYear; i >= firstYear; --i) {
				selectableYears.add(Integer.valueOf(i));
			}
			this.yearPicker = new ComboBox<>("", selectableYears); //$NON-NLS-1$
			this.yearPicker.setRequired(true);
			this.yearPicker.setWidth(6, Unit.EM);
			form.add(this.yearPicker, 1);

			this.binder.forField(this.yearPicker)
				.withValidator(new NotNullValueValidator<>(ComponentFactory.getTranslation("views.journals.updateRankings.step1.year.error"))) //$NON-NLS-1$
				.bind(JournalRankingUpdate::getYear, JournalRankingUpdate::setYear);
			this.binder.forField(this.wosEnable).bind(JournalRankingUpdate::getWosEnable, JournalRankingUpdate::setWosEnable);
			this.binder.forField(this.scimagoEnable).bind(JournalRankingUpdate::getScimagoEnable, JournalRankingUpdate::setScimagoEnable);
			this.binder.forField(this.impactFactorsEnable).bind(JournalRankingUpdate::getImpactFactorsEnable, JournalRankingUpdate::setImpactFactorsEnable);
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			if (this.yearPicker != null) {
				this.yearPicker.setLabel(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step1.year")); //$NON-NLS-1$
				this.yearPicker.setHelperText(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step1.year.help")); //$NON-NLS-1$
			}
			if (this.wosEnable != null) {
				this.wosEnable.setLabel(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step1.enable_wos")); //$NON-NLS-1$
			}
			if (this.scimagoEnable != null) {
				this.scimagoEnable.setLabel(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step1.enable_scimago")); //$NON-NLS-1$
			}
			if (this.impactFactorsEnable != null) {
				this.impactFactorsEnable.setLabel(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step1.enable_impact_factors")); //$NON-NLS-1$
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
	protected static class JournalRankLoadingWizardStep extends AbstractLabManagerProgressionWizardStep<JournalRankingUpdate> {

		private static final long serialVersionUID = 4363978455050034273L;

		private final JournalService journalService;
		
		/** Constructor.
		 *
		 * @param context the data to be shared between the wizard steps.
		 * @param journalService the service for accessing to the journals' JPA entities.
		 */
		public JournalRankLoadingWizardStep(JournalRankingUpdate context, JournalService journalService) {
			super(context, ComponentFactory.getTranslation("views.journals.updateRankings.step2.title"), 2, 1, true);//$NON-NLS-1$
			this.journalService = journalService;
		}

		@Override
		public BiFunction<Component, String, String> getBackActionMessageSupplier() {
			return (cmp, tlt) -> ComponentFactory.getTranslation("views.journals.updateRankings.step2.back.message", tlt); //$NON-NLS-1$
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			getMajorText().ifPresent(it -> it.setText(ComponentFactory.getTranslation("views.journals.updateRankings.step2.comment"))); //$NON-NLS-1$
		}

		@Override
		protected ExceptionRunnable createAsynchronousTask(int taskNo, Progression progression) {
			return () -> {
				final var identifiers = getContext().getEntityIdentifiers();
				progression.increment(5);
				final Consumer<Journal> callback = it -> {
					// Force loading of quality indicators for the journals
					it.getQualityIndicators();
				};
				final List<Journal> journals;
				if (identifiers == null || identifiers.isEmpty()) {
					journals = this.journalService.getAllJournals(callback);
				} else {
					journals = this.journalService.getAllJournals((root, query, criteriaBuilder) -> {
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
					}, callback);
				}
				progression.increment(90);
				getContext().setJournals(journals);
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
	protected static class JournalRankDownloadWizardStep extends AbstractLabManagerProgressionWizardStep<JournalRankingUpdate> {

		private static final long serialVersionUID = 2565280216469508988L;

		private final JournalService journalService;

		/** Constructor.
		 *
		 * @param context the data to be shared between the wizard steps.
		 * @param journalService the service for accessing to the journals' JPA entities.
		 */
		public JournalRankDownloadWizardStep(JournalRankingUpdate context, JournalService journalService) {
			super(context, ComponentFactory.getTranslation("views.journals.updateRankings.step3.title"), 3, 1, false);//$NON-NLS-1$
			this.journalService = journalService;
		}

		@Override
		public BiFunction<Component, String, String> getBackActionMessageSupplier() {
			return (cmp, tlt) -> ComponentFactory.getTranslation("views.journals.updateRankings.step3.back.message", tlt); //$NON-NLS-1$
		}

		@Override
		public void localeChange(LocaleChangeEvent event) {
			super.localeChange(event);
			getMajorText().ifPresent(it -> it.setText(ComponentFactory.getTranslation("views.journals.updateRankings.step3.comment"))); //$NON-NLS-1$
		}

		@Override
		protected Component getProgressIcon(int taskNo) {
			Image image;
			switch (taskNo) {
			case 0:
				image = new Image(ComponentFactory.newStreamImage(ViewConstants.SCIMAGO_ICON), ViewConstants.SCIMAGO_BASE_URL);
				break;
			case 1:
				image = new Image(ComponentFactory.newStreamImage(ViewConstants.WOS_ICON), null);
				break;
			default:
				throw new IllegalArgumentException();
			}
			image.setMinHeight(ViewConstants.ICON_SIZE, Unit.POINTS);
			image.setMaxHeight(ViewConstants.MAX_ICON_SIZE, Unit.POINTS);
			image.setMinWidth(ViewConstants.ICON_SIZE, Unit.POINTS);
			image.setMaxWidth(ViewConstants.MAX_ICON_SIZE, Unit.POINTS);
			image.getStyle().setAlignSelf(AlignSelf.CENTER);
			return image;
		}

		@Override
		protected ExceptionRunnable createAsynchronousTask(int taskNo, Progression progression) {
			switch (taskNo) {
			case 0:
				final var pattern0 = getWizard().orElseThrow().getTranslation("views.journals.updateRankings.step3.download_scimago"); //$NON-NLS-1$
				final var extendedProgression0 = ProgressExtension.withCommentFormatter(progression, it -> MessageFormat.format(pattern0, it));
				return () -> {
					if (getContext().getScimagoEnable()) {
						getContext().clearScimagoRankings();
						this.journalService.downloadJournalIndicatorsFromScimago(getContext().getYear(), getContext().getJournals(), extendedProgression0,
								(referenceYear, journalId, scientificField, oldQuartile, newQuartiles) -> getContext().addScimagoRanking(journalId, oldQuartile, newQuartiles));
					} else {
						progression.end();
					}
				};
			case 1:
				final var pattern1 = getWizard().orElseThrow().getTranslation("views.journals.updateRankings.step3.download_wos"); //$NON-NLS-1$
				final var extendedProgression1 = ProgressExtension.withCommentFormatter(progression, it -> MessageFormat.format(pattern1, it));
				return () -> {
					if (getContext().getWosEnable()) {
						getContext().clearScimagoRankings();
						this.journalService.downloadJournalIndicatorsFromWoS(getContext().getYear(), getContext().getJournals(), extendedProgression1,
								(referenceYear, journalId, scientificField, oldQuartile, newQuartiles) -> getContext().addWosRanking(journalId, oldQuartile, newQuartiles));
					} else {
						progression.end();
					}
				};
			default:
				throw new IllegalArgumentException();
			}
		}

	}

	//	/** Wizard step for journal rank loading.
	//	 * 
	//	 * @author $Author: sgalland$
	//	 * @version $Name$ $Revision$ $Date$
	//	 * @mavengroupid $GroupId$
	//	 * @mavenartifactid $ArtifactId$
	//	 * @since 4.0
	//	 */
	//	protected static class JournalRankLoadingWizardStep extends AbstractLocaleWizardStep<JournalRankingUpdate> {
	//
	//		private static final long serialVersionUID = 4125384926627194410L;
	//
	//		private final JournalService journalService;
	//
	//		private Span textLoadingJournals;
	//
	//		private ProgressAdapter progressLoadingJournals;
	//		
	//		private Span textLoadingWos;
	//
	//		private ProgressAdapter progressLoadingWos;
	//
	//		private Span textLoadingScimago;
	//
	//		private ProgressAdapter progressLoadingScimago;
	//
	//		private Span textLoadingImpactFactors;
	//
	//		private ProgressAdapter progressLoadingImpactFactors;
	//
	//		/** Constructor.
	//		 *
	//		 * @param journalService the service for accessing the journal entities.
	//		 * @param context the data context.
	//		 */
	//		public JournalRankLoadingWizardStep(JournalService journalService, JournalRankingUpdate context) {
	//			super(context, ComponentFactory.getTranslation("views.journals.updateRankings.step2.title"), 2); //$NON-NLS-1$, 1);
	//			this.journalService = journalService;
	//		}
	//
	//		@Override
	//		public Div getLayout() {
	//			final var layout = new VerticalLayout();
	//			
	//			this.textLoadingJournals = new Span(""); //$NON-NLS-1$
	//			final var div0 = new Div(this.textLoadingJournals);
	//			layout.add(div0);
	//			
	//			final var progressLoadingJournals = new ProgressBar();
	//			progressLoadingJournals.setWidthFull();
	//			progressLoadingJournals.setIndeterminate(true);
	//			layout.add(progressLoadingJournals);
	//			this.progressLoadingJournals = new ProgressAdapter(progressLoadingJournals);
	//
	//			this.textLoadingWos = new Span(""); //$NON-NLS-1$
	//			final var div1 = new Div(this.textLoadingWos);
	//			layout.add(div1);
	//
	//			final var progressLoadingWos = new ProgressBar();
	//			progressLoadingWos.setWidthFull();
	//			progressLoadingWos.setIndeterminate(true);
	//			layout.add(progressLoadingWos);
	//			this.progressLoadingWos = new ProgressAdapter(progressLoadingWos);
	//
	//			this.textLoadingScimago = new Span(""); //$NON-NLS-1$
	//			final var div2 = new Div(this.textLoadingScimago);
	//			layout.add(div2);
	//
	//			final var progressLoadingScimago = new ProgressBar();
	//			progressLoadingScimago.setWidthFull();
	//			progressLoadingScimago.setIndeterminate(true);
	//			layout.add(progressLoadingScimago);
	//			this.progressLoadingScimago = new ProgressAdapter(progressLoadingScimago);
	//
	//			this.textLoadingImpactFactors = new Span(""); //$NON-NLS-1$
	//			final var div3 = new Div(this.textLoadingImpactFactors);
	//			layout.add(div3);
	//
	//			final var progressLoadingImpactFactors = new ProgressBar();
	//			progressLoadingImpactFactors.setWidthFull();
	//			progressLoadingImpactFactors.setIndeterminate(true);
	//			layout.add(progressLoadingImpactFactors);
	//			this.progressLoadingImpactFactors = new ProgressAdapter(progressLoadingImpactFactors);
	//
	//			return new Div(layout);
	//		}
	//
	//		@Override
	//		public void setStatus(StepStatus status) {
	//			super.setStatus(status);
	//			if (status == StepStatus.ACTIVE) {
	//				startLoadingTasks();
	//			}
	//		}
	//
	//		/** Start the loading tasks accotrding to the internal sequence for loading.
	//		 */
	//		protected void startLoadingTasks() {
	//			if (this.progressLoadingImpactFactors.isIndeterminate()) {
	//				startJournalInformationLoading();
	//			}
	//		}
	//
	//		/** Invoked when an error has been encountered.
	//		 * 
	//		 * @param error the error description.
	//		 */
	//		private void onTaskError(Throwable error) {
	//			// FIXME
	//			error.printStackTrace();
	//		}
	//
	//		private Void onTaskErrorInternal(Throwable error) {
	//			onTaskError(error);
	//			return null;
	//		}
	//
	//		/** Invoked when the journal informations are loaded.
	//		 */
	//		protected void onJournalInformationsLoaded() {
	//			if (this.progressLoadingWos.isIndeterminate()) {
	//				startWosLoading();
	//			}
	//			if (this.progressLoadingScimago.isIndeterminate()) {
	//				startScimagoLoading();
	//			}
	//			if (this.progressLoadingImpactFactors.isIndeterminate()) {
	//				startImpactFactorsLoading();
	//			}
	//		}
	//
	//		private void onJournalInformationsLoadedInternal(Void data) {
	//			onJournalInformationsLoaded();
	//		}
	//
	//		/** Start the loading of the journal informations in a parallel thread.
	//		 */
	//		protected void startJournalInformationLoading() {
	//			final var task = CompletableFuture.runAsync(() -> {
	//				final var progression = new DefaultProgression(0, 100);
	//				progression.addProgressionListener(this.progressLoadingJournals);
	//				final var identifiers = getContext().getEntityIdentifiers();
	//				progression.increment(10);
	//				final List<Journal> journals;
	//				if (identifiers == null || identifiers.isEmpty()) {
	//					journals = this.journalService.getAllJournals();
	//				} else {
	//					journals = this.journalService.getAllJournals((root, query, criteriaBuilder) -> {
	//						Predicate pred = null;
	//						for (final var id : identifiers) {
	//							final var p = criteriaBuilder.equal(root.get("id"), Long.valueOf(id)); //$NON-NLS-1$
	//							if (pred == null) {
	//								pred = p;
	//							} else {
	//								pred = criteriaBuilder.or(pred, p);
	//							}
	//						}
	//						return pred;
	//					});
	//				}
	//				progression.increment(80);
	//				getContext().setJournals(journals);
	//				progression.end();
	//			});
	//			if (task != null) {
	//				task
	//					.exceptionally(this::onTaskErrorInternal)
	//					.thenAccept(this::onJournalInformationsLoadedInternal);
	//			} else {
	//				onTaskError(new IllegalStateException());
	//			}
	//		}
	//
	//		/** Invoked when the WoS informations are loaded.
	//		 */
	//		protected void onWosLoaded() {
	//		}
	//
	//		private void onWosLoadedInternal(Void data) {
	//			onWosLoaded();
	//		}
	//
	//		/** Start the loading of the journal informations in a parallel thread.
	//		 */
	//		protected void startWosLoading() {
	//			final var task = CompletableFuture.runAsync(() -> {
	//				final var progression = new DefaultProgression(0, 100);
	//				progression.addProgressionListener(this.progressLoadingWos);
	//				
	//				progression.end();
	//			});
	//			if (task != null) {
	//				task
	//					.exceptionally(this::onTaskErrorInternal)
	//					.thenAccept(this::onWosLoadedInternal);
	//			} else {
	//				onTaskError(new IllegalStateException());
	//			}
	//		}
	//
	//		/** Invoked when the Scimago informations are loaded.
	//		 */
	//		protected void onScimagoLoaded() {
	//		}
	//
	//		private void onScimagoLoadedInternal(Void data) {
	//			onScimagoLoaded();
	//		}
	//
	//		/** Start the loading of the journal informations in a parallel thread.
	//		 */
	//		protected void startScimagoLoading() {
	//			final var task = CompletableFuture.runAsync(() -> {
	//				try {
	//					final var context = getContext();
	//					final var year = context.getYear();
	//					final var journals = context.getJournals();
	//					final var progression = new DefaultProgression(0, journals.size());
	//					progression.addProgressionListener(this.progressLoadingScimago);
	//					
	//					for (final var journal : journals) {
	//						final var subTask = progression.subTask(1);
	//						final var quartiles = this.scimago.getJournalRanking(year, journal.getScimagoId(), subTask);
	//						progression.ensureNoSubTask();
	//					}
	//				} catch (Throwable ex) {
	//					onTaskError(ex);
	//					
	//				} finally {
	//					progression.end();
	//				}
	//			});
	//			if (task != null) {
	//				task
	//					.exceptionally(this::onTaskErrorInternal)
	//					.thenAccept(this::onScimagoLoadedInternal);
	//			} else {
	//				onTaskError(new IllegalStateException());
	//			}
	//		}
	//
	//		/** Invoked when the impact factors are loaded.
	//		 */
	//		protected void onImpactFactorsLoaded() {
	//		}
	//
	//		private void onImpactFactorsLoadedInternal(Void data) {
	//			onScimagoLoaded();
	//		}
	//
	//		/** Start the loading of the journal impact factors in a parallel thread.
	//		 */
	//		protected void startImpactFactorsLoading() {
	//			final var task = CompletableFuture.runAsync(() -> {
	//				final var progression = new DefaultProgression(0, 100);
	//				progression.addProgressionListener(this.progressLoadingImpactFactors);
	//				
	//				progression.end();
	//			});
	//			if (task != null) {
	//				task
	//					.exceptionally(this::onTaskErrorInternal)
	//					.thenAccept(this::onImpactFactorsLoadedInternal);
	//			} else {
	//				onTaskError(new IllegalStateException());
	//			}
	//		}
	//
	//		@Override
	//		public boolean isValid() {
	//			return false;
	//		}
	//
	//		@Override
	//		public boolean commit() {
	//			return false;
	//		}
	//
	//		@Override
	//		public void localeChange(LocaleChangeEvent event) {
	//			super.localeChange(event);
	//			setName(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step2.title")); //$NON-NLS-1$
	//			this.textLoadingJournals.setText(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step2.loading_journals")); //$NON-NLS-1$
	//			this.textLoadingWos.setText(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step2.loading_wos")); //$NON-NLS-1$
	//			this.textLoadingScimago.setText(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step2.loading_scimago")); //$NON-NLS-1$
	//			this.textLoadingImpactFactors.setText(ComponentFactory.getTranslation(event.getLocale(), "views.journals.updateRankings.step2.loading_impact_factors")); //$NON-NLS-1$
	//		}
	//		
	//	}

}
