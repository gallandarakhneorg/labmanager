/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.controller.api.teaching;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.teaching.StudentType;
import fr.ciadlab.labmanager.entities.teaching.TeacherRole;
import fr.ciadlab.labmanager.entities.teaching.TeachingActivity;
import fr.ciadlab.labmanager.entities.teaching.TeachingActivityLevel;
import fr.ciadlab.labmanager.entities.teaching.TeachingActivityType;
import fr.ciadlab.labmanager.io.json.JsonUtils;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.teaching.TeachingService;
import fr.ciadlab.labmanager.utils.country.CountryCode;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** REST Controller for teaching API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.4
 */
@RestController
@CrossOrigin
public class TeachingApiController extends AbstractApiController {

	private TeachingService teachingService;

	private PersonService personService;

	private ResearchOrganizationService organizationService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param teachingService the service for managing the teaching activities.
	 * @param personService the service for accessing the persons.
	 * @param organizationService the service for accessing to the organizations.
	 * @param nameParser the parser for person names.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public TeachingApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired TeachingService teachingService,
			@Autowired PersonService personService,
			@Autowired ResearchOrganizationService organizationService,
			@Autowired PersonNameParser nameParser,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.teachingService = teachingService;
		this.personService = personService;
		this.organizationService = organizationService;
		this.nameParser = nameParser;
	}

	/** Saving information of a teaching activity. 
	 *
	 * @param person the identifier of the person associated to this activity.
	 * @param activity the identifier of the activity. If the identifier is not provided, this endpoint is supposed to create
	 *     an activity in the database.
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
	 * @param activityUrl is a link to an external website that is dedicated to the activity. 
	 * @param sourceUrl is a link to an external website that provides source code for the activity.
	 * @param annualWorkPerType Json array that is a list of maps. Each map has the keys {@code type} and
	 *     {@code hours} for the type of activity and the number of hours respectively.
	 * @param pathToSlides are the uploaded slides, or {@code null} if no upload. If there is no uploaded,
	 *     any previously uploaded slides are kept.
	 * @param removePathToSlides indicates if previously uploaded slides must be removed.
	 * @param username the name of the logged-in user.
	 * @throws Exception if the publication cannot be saved.
	 */
	@PutMapping(value = "/" + Constants.TEACHING_ACTIVITY_SAVING_ENDPOINT)
	public void saveTeachingActivity(
			@RequestParam(required = true) Integer person,
			@RequestParam(required = false) Integer activity,
			@RequestParam(required = true) String code,
			@RequestParam(required = true) String title,
			@RequestParam(required = true) String level,
			@RequestParam(required = true) String studentType,
			@RequestParam(required = false) String language,
			@RequestParam(required = false) String degree,
			@RequestParam(required = true) Integer university,
			@RequestParam(required = true) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = true) String role,
			@RequestParam(required = false, defaultValue = "false") boolean differentHetdForTdTp,
			@RequestParam(required = false, defaultValue = "0") int numberOfStudents,
			@RequestParam(required = false) String explanation,
			@RequestParam(required = false) String activityUrl,
			@RequestParam(required = false) String sourceUrl,
			@RequestParam(required = false) String annualWorkPerType,
			@RequestParam(required = false) MultipartFile pathToSlides,
			@RequestParam(required = false, defaultValue = "false", name = "@fileUpload_removed_pathToSlides") boolean removePathToSlides,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.TEACHING_ACTIVITY_SAVING_ENDPOINT, person, activity);
		// Analyze parameters
		final String inCode = inString(code);
		final String inTitle = inString(title);
		final TeachingActivityLevel inLevel = TeachingActivityLevel.valueOfCaseInsensitive(inString(level));
		final StudentType inStudentType = StudentType.valueOfCaseInsensitive(inString(studentType));
		final String inLanguageStr = inString(language);
		final String inDegree = inString(degree);
		final CountryCode inLanguage = Strings.isNullOrEmpty(inLanguageStr) ? null : CountryCode.valueOfCaseInsensitive(inLanguageStr);
		final LocalDate inStartDate = LocalDate.parse(inString(startDate));
		final String inEndDateStr = inString(endDate);
		final LocalDate inEndDate = Strings.isNullOrEmpty(inEndDateStr) ? null : LocalDate.parse(inEndDateStr);
		final TeacherRole inRole = TeacherRole.valueOfCaseInsensitive(inString(role));
		final String inExplanation = inString(explanation);
		final URL inActivityUrl = inURL(activityUrl);
		final URL inSourceUrl = inURL(sourceUrl);
		//
		final Map<TeachingActivityType, Float> hours = new HashMap<>();
		if (!Strings.isNullOrEmpty(annualWorkPerType)) {
			final ObjectMapper mapper = JsonUtils.createMapper();
			try {
				@SuppressWarnings("unchecked")
				List<Map<?, ?>> rawHours = mapper.readValue(annualWorkPerType, List.class);
				if (rawHours != null && !rawHours.isEmpty()) {
					for (final Map<?,?> declaration : rawHours) {
						if (declaration != null && !declaration.isEmpty()) {
							final String typeStr = inString(declaration.get("type")); //$NON-NLS-1$
							final TeachingActivityType type = TeachingActivityType.valueOfCaseInsensitive(typeStr);
							final Float hourFlt = inFloat(declaration.get("hours")); //$NON-NLS-1$
							if (hourFlt.floatValue() > 0f) {
								final Float oldHours = hours.get(type);
								if (oldHours != null) {
									hours.put(type, Float.valueOf(oldHours.floatValue() + hourFlt.floatValue()));
								} else {
									hours.put(type, hourFlt);
								}
							}
						}
					}
				}
			} catch (Throwable ex) {
				throw new IllegalArgumentException("Invalid format for the argument 'annualWorkPerType'. " + ex.getLocalizedMessage(), ex); //$NON-NLS-1$
			}
		}
		//
		final Person personObj = getPersonWith(person, null, null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found: " + person); //$NON-NLS-1$
		}
		//
		if (university == null) {
			throw new IllegalArgumentException("University not found: " + university); //$NON-NLS-1$
		}
		final Optional<ResearchOrganization> inUniversity = this.organizationService.getResearchOrganizationById(university.intValue());
		if (inUniversity.isEmpty()) {
			throw new IllegalArgumentException("University not found: " + university); //$NON-NLS-1$
		}
		// Create or update the activity
		Optional<TeachingActivity> activityOpt = Optional.empty();
		if (activity == null) {
			activityOpt = this.teachingService.createTeachingActivity(
					personObj, inCode, inTitle, inLevel, inStudentType, inLanguage, inDegree, inUniversity.get(),
					inStartDate, inEndDate, inRole, differentHetdForTdTp, numberOfStudents,
					inExplanation, inActivityUrl, inSourceUrl, hours, pathToSlides, removePathToSlides);
		} else {
			activityOpt = this.teachingService.updateTeachingActivity(activity.intValue(),
					personObj, inCode, inTitle, inLevel, inStudentType, inLanguage, inDegree, inUniversity.get(),
					inStartDate, inEndDate, inRole, differentHetdForTdTp, numberOfStudents,
					inExplanation, inActivityUrl, inSourceUrl, hours, pathToSlides, removePathToSlides);
		}
		if (activityOpt.isEmpty()) {
			throw new IllegalStateException("Teaching activity not found"); //$NON-NLS-1$
		}
	}

	/** Delete a teaching activity from the database.
	 *
	 * @param activity the identifier of the activity.
	 * @param username the name of the logged-in user.
	 * @throws Exception in case of error.
	 */
	@DeleteMapping("/" + Constants.TEACHING_ACTIVITY_DELETING_ENDPOINT)
	public void deleteTeachingActivity(
			@RequestParam(name = Constants.ACTIVITY_ENDPOINT_PARAMETER) Integer activity,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		ensureCredentials(username, Constants.TEACHING_ACTIVITY_DELETING_ENDPOINT, activity);
		if (activity == null || activity.intValue() == 0) {
			throw new IllegalStateException("Activity not found"); //$NON-NLS-1$
		}
		this.teachingService.removeTeachingActivity(activity.intValue(), true);
	}

}
