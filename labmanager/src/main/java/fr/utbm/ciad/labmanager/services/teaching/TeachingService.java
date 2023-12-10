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

package fr.utbm.ciad.labmanager.services.teaching;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.organization.ResearchOrganization;
import fr.utbm.ciad.labmanager.data.teaching.PedagogicalPracticeType;
import fr.utbm.ciad.labmanager.data.teaching.StudentType;
import fr.utbm.ciad.labmanager.data.teaching.TeacherRole;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivity;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityLevel;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityRepository;
import fr.utbm.ciad.labmanager.data.teaching.TeachingActivityType;
import fr.utbm.ciad.labmanager.services.AbstractService;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.utils.io.filemanager.DownloadableFileManager;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Service for the teaching activities.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.4
 */
@Service
public class TeachingService extends AbstractService {

	private TeachingActivityRepository teachingActivityRepository;

	private DownloadableFileManager fileManager;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param teachingActivityRepository the repository for the teaching activities.
	 * @param fileManager the manager of the uploaded and downloadable files.
	 */
	public TeachingService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired TeachingActivityRepository teachingActivityRepository,
			@Autowired DownloadableFileManager fileManager) {
		super(messages, constants);
		this.teachingActivityRepository = teachingActivityRepository;
		this.fileManager = fileManager;
	}

	/** Replies the teaching activities for the person with the given identifier.
	 *
	 * @param id the identifier of the person.
	 * @return the list of teaching activities.
	 */
	public List<TeachingActivity> getActivitiesByPersonId(int id) {
		return this.teachingActivityRepository.findDistinctByPersonId(Integer.valueOf(id));
	}

	/** Replies all the known teaching activities.
	 *
	 * @return all the activities, never {@code null}.
	 */
	public List<TeachingActivity> getAllActivities() {
		return this.teachingActivityRepository.findAll();
	}

	/** Replies all the known teaching activities.
	 *
	 * @param pageable a manager of pages.
	 * @return all the activities, never {@code null}.
	 * @since 4.0
	 */
	public Page<TeachingActivity> getAllActivities(Pageable pageable) {
		return this.teachingActivityRepository.findAll(pageable);
	}

	/** Replies all the known teaching activities.
	 *
	 * @param pageable a manager of pages.
	 * @param filter the fitler to apply to the users.
	 * @return all the activities, never {@code null}.
	 * @since 4.0
	 */
	public Page<TeachingActivity> getAllActivities(Pageable pageable, Specification<TeachingActivity> filter) {
		return this.teachingActivityRepository.findAll(filter, pageable);
	}

	/** Replies the teaching activity that has the given identifier.
	 *
	 * @param id the identifier.
	 * @return the activity or {@code null} if the activity cannot be found.
	 */
	public TeachingActivity getActivityById(int id) {
		final Optional<TeachingActivity> opt = this.teachingActivityRepository.findById(Integer.valueOf(id));
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	/** Delete the teaching activity with the given identifier.
	 *
	 * @param id the identifier.
	 * @param removeAssociatedFiles indicates if the uploaded files that are associated to the activity should be deleted also.
	 */
	public void removeTeachingActivity(int id, boolean removeAssociatedFiles) {
		final Integer identifier = Integer.valueOf(id);
		final Optional<TeachingActivity> activityOpt = this.teachingActivityRepository.findById(identifier);
		if (activityOpt.isPresent()) {
			final TeachingActivity activity = activityOpt.get();
			//
			final String pathToSlides = activity.getPathToSlides();
			//
			activity.setPerson(null);
			activity.setUniversity(null);
			activity.setAnnualWorkPerType(null);
			activity.setPathToSlides(null);
			this.teachingActivityRepository.deleteById(identifier);
			if (removeAssociatedFiles) {
				try {
					if (!Strings.isNullOrEmpty(pathToSlides)) {
						this.fileManager.deleteTeachingActivitySlides(id);
					}
				} catch (Throwable ex) {
					// Silent
				}
			}
		}
	}

	/** Create a teaching activity with the given attributes.
	 *
	 * @param person the person that is associated to the teaching activity.
	 * @param code the code or acronym of the teaching activity.
	 * @param title the title of the teaching activity.
	 * @param level the level of teaching.
	 * @param studentType the type of the targeted students.
	 * @param language the language of the activity. If it not provided, the default value is applied.
	 * @param degree the name of the degree.
	 * @param university the university in which the teaching activity is realized.
	 * @param startDate the start date of the activity.
	 * @param endDate the end date of the activity, or {@code null} if it is still active.
	 * @param role the role of the person in the activity.
	 * @param differentHetdForTdTp indicates if the person has different factors for her/his hours of tutorials
	 *      and practice works for computing the number of hETD.
	 * @param numberOfStudents is an estimation of the number of students per year in the activity.
	 * @param explanation is a text that describes the activity.
	 * @param pedagogicalPracticeTypes the types of pedagogical practices that are applied in the teaching activity.
	 * @param activityUrl is a link to an external website that is dedicated to the activity. 
	 * @param sourceUrl is a link to an external website that provides source code for the activity.
	 * @param annualWorkPerType maps the types of teaching activity to the annual number of hours.
	 * @param pathToSlides are the uploaded slides, or {@code null} if no upload. If there is no uploaded,
	 *     any previously uploaded slides are kept.
	 * @param removePathToSlides indicates if previously uploaded slides must be removed.
	 * @return the created activity, or nothing if the activity cannot be created.
	 * @throws Exception if the slides cannot be uploaded.
	 */
	public Optional<TeachingActivity> createTeachingActivity(Person person, String code, String title,
			TeachingActivityLevel level, StudentType studentType,
			CountryCode language, String degree, ResearchOrganization university,
			LocalDate startDate, LocalDate endDate, TeacherRole role, boolean differentHetdForTdTp,
			int numberOfStudents, String explanation, Set<PedagogicalPracticeType> pedagogicalPracticeTypes,
			URL activityUrl, URL sourceUrl, Map<TeachingActivityType, Float> annualWorkPerType,
			MultipartFile pathToSlides, boolean removePathToSlides) throws Exception {
		final TeachingActivity activity = new TeachingActivity();
		try {
			updateTeachingActivity(activity,
					person, code, title, level, studentType, language, degree, university, startDate, endDate,
					role, differentHetdForTdTp, numberOfStudents, explanation, pedagogicalPracticeTypes,
					activityUrl, sourceUrl, annualWorkPerType, pathToSlides, removePathToSlides);
		} catch (Throwable ex) {
			// Delete created activity
			if (activity.getId() != 0) {
				try {
					removeTeachingActivity(activity.getId(), true);
				} catch (Throwable ex0) {
					// Silent
				}
			}
			getLogger().error(ex.getLocalizedMessage(), ex);
			throw ex;
		}
		return Optional.of(activity);
	}

	/** Update an existing teaching activity with the given attributes.
	 *
	 * @param activity the identifier of the activity to be updated.
	 * @param person the person that is associated to the teaching activity.
	 * @param code the code or acronym of the teaching activity.
	 * @param title the title of the teaching activity.
	 * @param level the level of teaching.
	 * @param studentType the type of the targeted students.
	 * @param language the language of the activity. If it not provided, the default value is applied.
	 * @param degree the name of the degree.
	 * @param university the university in which the teaching activity is realized.
	 * @param startDate the start date of the activity.
	 * @param endDate the end date of the activity, or {@code null} if it is still active.
	 * @param role the role of the person in the activity.
	 * @param differentHetdForTdTp indicates if the person has different factors for her/his hours of tutorials
	 *      and practice works for computing the number of hETD.
	 * @param numberOfStudents is an estimation of the number of students per year in the activity.
	 * @param explanation is a text that describes the activity.
	 * @param pedagogicalPracticeTypes the types of pedagogical practices that are applied in the teaching activity.
	 * @param activityUrl is a link to an external website that is dedicated to the activity. 
	 * @param sourceUrl is a link to an external website that provides source code for the activity.
	 * @param annualWorkPerType maps the types of teaching activity to the annual number of hours.
	 * @param pathToSlides are the uploaded slides, or {@code null} if no upload. If there is no uploaded,
	 *     any previously uploaded slides are kept.
	 * @param removePathToSlides indicates if previously uploaded slides must be removed.
	 * @return the updated activity, or nothing if the activity cannot be created.
	 * @throws Exception if the slides cannot be uploaded.
	 */
	public Optional<TeachingActivity> updateTeachingActivity(
			int activity, Person person, String code, String title,
			TeachingActivityLevel level, StudentType studentType,
			CountryCode language, String degree, ResearchOrganization university,
			LocalDate startDate, LocalDate endDate, TeacherRole role, boolean differentHetdForTdTp,
			int numberOfStudents, String explanation, Set<PedagogicalPracticeType> pedagogicalPracticeTypes,
			URL activityUrl, URL sourceUrl, Map<TeachingActivityType, Float> annualWorkPerType,
			MultipartFile pathToSlides, boolean removePathToSlides) throws Exception {
		final Optional<TeachingActivity> res;
		if (activity >= 0) {
			res = this.teachingActivityRepository.findById(Integer.valueOf(activity));
		} else {
			res = Optional.empty();
		}
		if (res.isPresent()) {
			updateTeachingActivity(res.get(),
					person, code, title, level, studentType, language, degree, university, startDate, endDate,
					role, differentHetdForTdTp, numberOfStudents, explanation, pedagogicalPracticeTypes,
					activityUrl, sourceUrl, annualWorkPerType, pathToSlides, removePathToSlides);
		}
		return res;
	}

	/** Update an existing teaching activity with the given attributes.
	 *
	 * @param activity the activity to be updated.
	 * @param person the person that is associated to the teaching activity.
	 * @param code the code or acronym of the teaching activity.
	 * @param title the title of the teaching activity.
	 * @param level the level of teaching.
	 * @param studentType the type of the targeted students.
	 * @param language the language of the activity. If it not provided, the default value is applied.
	 * @param degree the name of the degree.
	 * @param university the university in which the teaching activity is realized.
	 * @param startDate the start date of the activity.
	 * @param endDate the end date of the activity, or {@code null} if it is still active.
	 * @param role the role of the person in the activity.
	 * @param differentHetdForTdTp indicates if the person has different factors for her/his hours of tutorials
	 *      and practice works for computing the number of hETD.
	 * @param numberOfStudents is an estimation of the number of students per year in the activity.
	 * @param explanation is a text that describes the activity.
	 * @param pedagogicalPracticeTypes the types of pedagogical practices that are applied in the teaching activity.
	 * @param activityUrl is a link to an external website that is dedicated to the activity. 
	 * @param sourceUrl is a link to an external website that provides source code for the activity.
	 * @param annualWorkPerType maps the types of teaching activity to the annual number of hours.
	 * @param pathToSlides are the uploaded slides, or {@code null} if no upload. If there is no uploaded,
	 *     any previously uploaded slides are kept.
	 * @param removePathToSlides indicates if previously uploaded slides must be removed.
	 * @throws Exception if the slides cannot be uploaded.
	 */
	protected void updateTeachingActivity(
			TeachingActivity activity, Person person, String code, String title,
			TeachingActivityLevel level, StudentType studentType,
			CountryCode language, String degree, ResearchOrganization university,
			LocalDate startDate, LocalDate endDate, TeacherRole role, boolean differentHetdForTdTp,
			int numberOfStudents, String explanation, Set<PedagogicalPracticeType> pedagogicalPracticeTypes,
			URL activityUrl, URL sourceUrl, Map<TeachingActivityType, Float> annualWorkPerType,
			MultipartFile pathToSlides, boolean removePathToSlides) throws Exception {
		activity.setActivityUrl(activityUrl);
		activity.setCode(code);
		activity.setDifferentHetdForTdTp(differentHetdForTdTp);
		activity.setStartDate(startDate);
		activity.setEndDate(endDate);
		activity.setExplanation(explanation);
		activity.setPedagogicalPracticeTypes(pedagogicalPracticeTypes);
		activity.setLanguage(language);
		activity.setLevel(level);
		activity.setNumberOfStudents(numberOfStudents);
		activity.setRole(role);
		activity.setSourceUrl(sourceUrl);
		activity.setStudentType(studentType);
		activity.setTitle(title);
		activity.setPerson(person);
		activity.setDegree(degree);
		activity.setUniversity(university);
		activity.setAnnualWorkPerType(annualWorkPerType);
		this.teachingActivityRepository.save(activity);

		if (updateSlides(activity, removePathToSlides, pathToSlides)) {
			this.teachingActivityRepository.save(activity);
		}
	}

	private boolean updateSlides(TeachingActivity activity, boolean explicitRemove, MultipartFile uploadedFile) throws IOException {
		return updateUploadedFile(explicitRemove, uploadedFile,
				"Teachnig activity's slides uploaded at: ", //$NON-NLS-1$
				it -> activity.setPathToSlides(it),
				() -> this.fileManager.makeTeachingActivitySlidesFilename(activity.getId()),
				() -> this.fileManager.deleteTeachingActivitySlides(activity.getId()),
				(fn, th) -> this.fileManager.savePdfAndThumbnailFiles(fn, th, uploadedFile));
	}

	/** Replies if the given identifier is for a teacher.
	 *
	 * @param id the identifier of the person.
	 * @return {@code true} if the person is a teacher.
	 * @since 3.6
	 */
	public boolean isTeacher(int id) {
		return !this.teachingActivityRepository.findDistinctByPersonId(Integer.valueOf(id)).isEmpty();
	}

}