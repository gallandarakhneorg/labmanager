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

package fr.utbm.ciad.labmanager.views.components.teaching;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import fr.utbm.ciad.labmanager.components.security.AuthenticatedUser;
import fr.utbm.ciad.labmanager.data.teaching.PedagogicalPracticeType;
import fr.utbm.ciad.labmanager.data.teaching.StudentType;
import fr.utbm.ciad.labmanager.data.teaching.TeacherRole;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivity;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityLevel;
import fr.utbm.ciad.labmanager.services.AbstractEntityService.EntityEditingContext;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import fr.utbm.ciad.labmanager.views.components.addons.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.addons.converters.StringTrimer;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMark;
import fr.utbm.ciad.labmanager.views.components.addons.details.DetailsWithErrorMarkStatusHandler;
import fr.utbm.ciad.labmanager.views.components.addons.entities.AbstractEntityEditor;
import fr.utbm.ciad.labmanager.views.components.addons.uploads.pdf.ServerSideUploadablePdfField;
import fr.utbm.ciad.labmanager.views.components.addons.validators.LanguageValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotEmptyStringValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.NotNullEnumerationValidator;
import fr.utbm.ciad.labmanager.views.components.addons.validators.UrlValidator;
import org.slf4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation for the editor of the information related to a teaching activity.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Uses(Icon.class)
public abstract class AbstractTeachingActivityEditor extends AbstractEntityEditor<TeachingActivity> {

	private static final long serialVersionUID = -4154950918131057693L;

	private DetailsWithErrorMark descriptionDetails;

	private TextField activityCode;

	private TextField activityTitle;

	private ComboBox<CountryCode> language;

	private DatePicker startDate;

	private DatePicker endDate;

	private DetailsWithErrorMark studentsDetails;

	private TextField degree;

	private ComboBox<TeachingActivityLevel> level;

	private ComboBox<StudentType> studentType;

	private IntegerField numberOfStudents;

	private DetailsWithErrorMark teacherDetails;

	private ComboBox<TeacherRole> teacherRole;

	private ComboBox<PedagogicalPracticeType> pedagogicalPractices;
	
	private Checkbox labworkTutorialEtpDifference;

	private DetailsWithErrorMark documentsDetails;

	private ServerSideUploadablePdfField slides;

	private TextField sourceUrl;

	private TextField activityUrl;

	private final DownloadableFileManager fileManager;

	/** Constructor.
	 *
	 * @param context the context for editing the teaching activity.
	 * @param fileManager the manager of the downloadable files.
	 * @param authenticatedUser the connected user.
	 * @param messages the accessor to the localized messages (Spring layer).
	 * @param logger the logger to be used by this view.
	 */
	public AbstractTeachingActivityEditor(EntityEditingContext<TeachingActivity> context, DownloadableFileManager fileManager,
			AuthenticatedUser authenticatedUser, MessageSourceAccessor messages, Logger logger) {
		super(TeachingActivity.class, authenticatedUser, messages, logger,
				"views.teaching_activity.administration_details", //$NON-NLS-1$
				"views.teaching_activity.administration.validated_address", //$NON-NLS-1$
				context);
		this.fileManager = fileManager;
	}

	@Override
	protected void createEditorContent(VerticalLayout rootContainer) {
		createDescriptionDetails(rootContainer);
		createStudentDetails(rootContainer);
		createTeacherDetails(rootContainer);
		createDocumentDetails(rootContainer);
	}

	/** Create the section for editing the description of the teaching activity.
	 *
	 * @param rootContainer the container.
	 */
	protected void createDescriptionDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);
		
		this.activityCode = new TextField();
		this.activityCode.setPrefixComponent(VaadinIcon.HASH.create());
		this.activityCode.setRequired(true);
		this.activityCode.setClearButtonVisible(true);
		content.add(this.activityCode, 1);

		this.activityTitle = new TextField();
		this.activityTitle.setPrefixComponent(VaadinIcon.HASH.create());
		this.activityTitle.setRequired(true);
		this.activityTitle.setClearButtonVisible(true);
		content.add(this.activityTitle, 1);

		this.language = ComponentFactory.newLanguageComboBox(getLocale());
		this.language.setPrefixComponent(VaadinIcon.COMMENT_ELLIPSIS_O.create());
		content.add(this.language, 2);

		this.startDate = new DatePicker();
		this.startDate.setPrefixComponent(VaadinIcon.SIGN_IN_ALT.create());
		this.startDate.setClearButtonVisible(true);
		content.add(this.startDate, 1);

		this.endDate = new DatePicker();
		this.endDate.setPrefixComponent(VaadinIcon.SIGN_OUT_ALT.create());
		this.endDate.setClearButtonVisible(true);
		content.add(this.endDate, 1);

		this.descriptionDetails = new DetailsWithErrorMark(content);
		this.descriptionDetails.setOpened(false);
		rootContainer.add(this.descriptionDetails);

		getEntityDataBinder().forField(this.activityCode)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.teaching_activities.code.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.activityCode, this.descriptionDetails))
			.bind(TeachingActivity::getCode, TeachingActivity::setCode);
		getEntityDataBinder().forField(this.activityTitle)
			.withConverter(new StringTrimer())
			.withValidator(new NotEmptyStringValidator(getTranslation("views.teaching_activities.title.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.activityTitle, this.descriptionDetails))
			.bind(TeachingActivity::getTitle, TeachingActivity::setTitle);
		getEntityDataBinder().forField(this.language)
			.withValidator(new LanguageValidator(getTranslation("views.teaching_activities.language.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.language, this.descriptionDetails))
			.bind(TeachingActivity::getLanguage, TeachingActivity::setLanguage);
		getEntityDataBinder().forField(this.startDate)
			.bind(TeachingActivity::getStartDate, TeachingActivity::setStartDate);
		getEntityDataBinder().forField(this.endDate)
			.bind(TeachingActivity::getEndDate, TeachingActivity::setEndDate);
	}

	/** Create the section for editing the description of the students.
	 *
	 * @param rootContainer the container.
	 */
	protected void createStudentDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.degree = new TextField();
		this.degree.setPrefixComponent(VaadinIcon.DIPLOMA_SCROLL.create());
		this.degree.setClearButtonVisible(true);
		content.add(this.degree, 2);

		this.level = new ComboBox<>();
		this.level.setItems(TeachingActivityLevel.values());
		this.level.setItemLabelGenerator(this::getTeachingActivityLevelLabel);
		this.level.setPrefixComponent(VaadinIcon.FORM.create());
		content.add(this.level, 1);

		this.studentType = new ComboBox<>();
		this.studentType.setItems(StudentType.getAllDisplayTypes(getMessageSourceAccessor(), getLocale()));
		this.studentType.setItemLabelGenerator(this::getStudentTypeLabel);
		this.studentType.setPrefixComponent(VaadinIcon.ACADEMY_CAP.create());
		content.add(this.studentType, 1);

		this.numberOfStudents = new IntegerField();
		this.numberOfStudents.setMin(0);
		this.numberOfStudents.setPrefixComponent(VaadinIcon.COINS.create());
		this.numberOfStudents.setClearButtonVisible(true);
		content.add(this.numberOfStudents, 1);

		this.studentsDetails = new DetailsWithErrorMark(content);
		this.studentsDetails.setOpened(false);
		rootContainer.add(this.studentsDetails);

		getEntityDataBinder().forField(this.degree)
			.withConverter(new StringTrimer())
			.bind(TeachingActivity::getDegree, TeachingActivity::setDegree);
		getEntityDataBinder().forField(this.level)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.teaching_activities.level.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.level, this.studentsDetails))
			.bind(TeachingActivity::getLevel, TeachingActivity::setLevel);
		getEntityDataBinder().forField(this.studentType)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.teaching_activities.student_type.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.studentType, this.studentsDetails))
			.bind(TeachingActivity::getStudentType, TeachingActivity::setStudentType);
		getEntityDataBinder().forField(this.numberOfStudents)
			.withValidator(new IntegerRangeValidator(getTranslation("views.teaching_activities.student_count.error"), Integer.valueOf(0), null)) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.numberOfStudents, this.studentsDetails))
			.bind(TeachingActivity::getNumberOfStudents, TeachingActivity::setNumberOfStudents);
	}

	private String getTeachingActivityLevelLabel(TeachingActivityLevel level) {
		return level.getLabel(getMessageSourceAccessor(), getLocale());
	}

	private String getStudentTypeLabel(StudentType type) {
		return type.getLabel(getMessageSourceAccessor(), getLocale());
	}

	/** Create the section for editing the description of the teachers.
	 *
	 * @param rootContainer the container.
	 */
	protected void createTeacherDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.teacherRole = new ComboBox<>();
		this.teacherRole.setItems(TeacherRole.values());
		this.teacherRole.setItemLabelGenerator(this::getTeacherRoleLabel);
		this.teacherRole.setPrefixComponent(VaadinIcon.FORM.create());
		content.add(this.teacherRole, 2);

//		this.pedagogicalPractices = new ComboBox<>();
//		this.pedagogicalPractices.setItems(PedagogicalPracticeType.getAllDisplayTypes(getMessageSourceAccessor(), getLocale()));
//		this.pedagogicalPractices.setItemLabelGenerator(this::getPedagogicalPracticeLabel);
//		this.pedagogicalPractices.setPrefixComponent(VaadinIcon.FORM.create());
//		content.add(this.pedagogicalPractices, 1);

		this.labworkTutorialEtpDifference = new Checkbox();
		content.add(this.labworkTutorialEtpDifference, 2);

		this.teacherDetails = new DetailsWithErrorMark(content);
		this.teacherDetails.setOpened(false);
		rootContainer.add(this.teacherDetails);

		getEntityDataBinder().forField(this.teacherRole)
			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.teaching_activities.teacher_role.error"))) //$NON-NLS-1$
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.teacherRole, this.teacherDetails))
			.bind(TeachingActivity::getRole, TeachingActivity::setRole);
//		getEntityDataBinder().forField(this.pedagogicalPractices)
//			.withValidator(new NotNullEnumerationValidator<>(getTranslation("views.teaching_activities.pedagogical_practices.error"))) //$NON-NLS-1$
//			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.pedagogicalPractices, this.teacherDetails))
//			.bind(TeachingActivity::getPedagogicalPracticeTypes, TeachingActivity::setPedagogicalPracticeTypes);
		getEntityDataBinder().forField(this.labworkTutorialEtpDifference)
			.bind(TeachingActivity::isDifferentHetdForTdTp, TeachingActivity::setDifferentHetdForTdTp);
	}

	private String getTeacherRoleLabel(TeacherRole role) {
		return role.getLabel(getMessageSourceAccessor(), getLocale());
	}

	private String getPedagogicalPracticeLabel(PedagogicalPracticeType type) {
		return type.getLabel(getMessageSourceAccessor(), getLocale());
	}

	/** Create the section for editing the description of the documents.
	 *
	 * @param rootContainer the container.
	 */
	protected void createDocumentDetails(VerticalLayout rootContainer) {
		final var content = ComponentFactory.newColumnForm(2);

		this.activityUrl = new TextField();
		this.activityUrl.setPrefixComponent(VaadinIcon.GLOBE_WIRE.create());
		content.add(this.activityUrl, 1);

		this.sourceUrl = new TextField();
		this.sourceUrl.setPrefixComponent(VaadinIcon.GLOBE_WIRE.create());
		content.add(this.sourceUrl, 1);

		this.slides = new ServerSideUploadablePdfField(this.fileManager,
				ext -> this.fileManager.makeTeachingActivitySlidesFilename(getEditedEntity().getId()));
		this.slides.setClearButtonVisible(true);
		content.add(this.slides, 2);

		this.documentsDetails = new DetailsWithErrorMark(content);
		this.documentsDetails.setOpened(false);
		rootContainer.add(this.documentsDetails);

		final var invalidUrl = getTranslation("views.urls.invalid_format"); //$NON-NLS-1$

		getEntityDataBinder().forField(this.activityUrl)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(invalidUrl, true))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.activityUrl, this.documentsDetails))
			.bind(TeachingActivity::getActivityUrl, TeachingActivity::setActivityUrl);
		getEntityDataBinder().forField(this.sourceUrl)
			.withConverter(new StringTrimer())
			.withValidator(new UrlValidator(invalidUrl, true))
			.withValidationStatusHandler(new DetailsWithErrorMarkStatusHandler(this.sourceUrl, this.documentsDetails))
			.bind(TeachingActivity::getSourceUrl, TeachingActivity::setSourceUrl);
		getEntityDataBinder().forField(this.slides)
			.withConverter(new StringTrimer())
			.bind(TeachingActivity::getPathToSlides, TeachingActivity::setPathToSlides);
	}

	@Override
	protected void doSave() throws Exception {
		getEditingContext().save(this.slides);
	}

	@Override
	protected String computeSavingSuccessMessage() {
		return getTranslation("views.teaching_activities.save_success", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeValidationSuccessMessage() {
		return getTranslation("views.teaching_activities.validation_success", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeDeletionSuccessMessage() {
		return getTranslation("views.teaching_activities.delete_success2", //$NON-NLS-1$
				getEditedEntity().getTitle());
	}

	@Override
	protected String computeSavingErrorMessage(Throwable error) {
		return getTranslation("views.teaching_activities.save_error", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeValidationErrorMessage(Throwable error) {
		return getTranslation("views.teaching_activities.validation_error", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	protected String computeDeletionErrorMessage(Throwable error) {
		return getTranslation("views.teaching_activities.delete_error2", //$NON-NLS-1$ 
				getEditedEntity().getTitle(), error.getLocalizedMessage());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		super.localeChange(event);
		this.descriptionDetails.setSummaryText(getTranslation("views.teaching_activities.description_details")); //$NON-NLS-1$
		this.activityCode.setLabel(getTranslation("views.teaching_activities.code")); //$NON-NLS-1$
		this.activityCode.setHelperText(getTranslation("views.teaching_activities.code.help")); //$NON-NLS-1$
		this.activityTitle.setLabel(getTranslation("views.teaching_activities.title")); //$NON-NLS-1$
		this.activityTitle.setHelperText(getTranslation("views.teaching_activities.title.help")); //$NON-NLS-1$
		this.language.setLabel(getTranslation("views.teaching_activities.language")); //$NON-NLS-1$
		this.language.setHelperText(getTranslation("views.teaching_activities.language.help")); //$NON-NLS-1$
		ComponentFactory.updateLanguageComboBoxItems(this.language, getLocale());
		this.startDate.setLocale(event.getLocale());
		this.startDate.setLabel(getTranslation("views.teaching_activities.start_date")); //$NON-NLS-1$
		this.endDate.setLocale(event.getLocale());
		this.endDate.setLabel(getTranslation("views.teaching_activities.end_date")); //$NON-NLS-1$
		this.endDate.setHelperText(getTranslation("views.teaching_activities.end_date.help")); //$NON-NLS-1$

		this.studentsDetails.setSummaryText(getTranslation("views.teaching_activities.student_details")); //$NON-NLS-1$
		this.degree.setLabel(getTranslation("views.teaching_activities.degree")); //$NON-NLS-1$
		this.degree.setHelperText(getTranslation("views.teaching_activities.degree.help")); //$NON-NLS-1$
		this.level.setLabel(getTranslation("views.teaching_activities.level")); //$NON-NLS-1$
		this.level.setItemLabelGenerator(this::getTeachingActivityLevelLabel);
		this.studentType.setLabel(getTranslation("views.teaching_activities.student_type")); //$NON-NLS-1$
		this.studentType.setItemLabelGenerator(this::getStudentTypeLabel);
		this.numberOfStudents.setLabel(getTranslation("views.teaching_activities.student_count")); //$NON-NLS-1$
		this.numberOfStudents.setHelperText(getTranslation("views.teaching_activities.student_count.help")); //$NON-NLS-1$

		this.teacherDetails.setSummaryText(getTranslation("views.teaching_activities.teacher_details")); //$NON-NLS-1$
		this.teacherRole.setLabel(getTranslation("views.teaching_activities.teacher_role")); //$NON-NLS-1$
		this.teacherRole.setItemLabelGenerator(this::getTeacherRoleLabel);
//		this.pedagogicalPractices.setLabel(getTranslation("views.teaching_activities.pedagogical_practices")); //$NON-NLS-1$
//		this.pedagogicalPractices.setItemLabelGenerator(this::getPedagogicalPracticeLabel);
		this.labworkTutorialEtpDifference.setLabel(getTranslation("views.teaching_activities.different_etp")); //$NON-NLS-1$

		this.documentsDetails.setSummaryText(getTranslation("views.teaching_activities.document_details")); //$NON-NLS-1$
		this.activityUrl.setLabel(getTranslation("views.teaching_activities.activity_url")); //$NON-NLS-1$
		this.sourceUrl.setLabel(getTranslation("views.teaching_activities.source_url")); //$NON-NLS-1$
		this.slides.setLabel(getTranslation("views.teaching_activities.slides")); //$NON-NLS-1$
	}

}
