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

package fr.ciadlab.labmanager.controller.view.teaching;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.view.AbstractViewController;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.entities.teaching.StudentType;
import fr.ciadlab.labmanager.entities.teaching.TeacherRole;
import fr.ciadlab.labmanager.entities.teaching.TeachingActivity;
import fr.ciadlab.labmanager.entities.teaching.TeachingActivityLevel;
import fr.ciadlab.labmanager.entities.teaching.TeachingActivityType;
import fr.ciadlab.labmanager.service.member.PersonService;
import fr.ciadlab.labmanager.service.organization.ResearchOrganizationService;
import fr.ciadlab.labmanager.service.teaching.TeachingService;
import fr.ciadlab.labmanager.utils.CountryCodeUtils;
import fr.ciadlab.labmanager.utils.names.PersonNameParser;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.arakhne.afc.vmutil.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/** REST Controller for teaching views.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.4
 */
@RestController
@CrossOrigin
public class TeachingViewController extends AbstractViewController {

	private TeachingService teachingService;

	private PersonService personService;

	private ResearchOrganizationService organizationService;

	private PersonNameParser nameParser;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param teachingService the service for accessing the teaching activities.
	 * @param personService the service for accessing the persons.
	 * @param organizationService the service for accessing to the organizations.
	 * @param nameParser the parser for person names.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public TeachingViewController(
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

	/** Replies the model-view component for managing the teaching activities.
	 *
	 * @param dbId the database identifier of the person for who the projects must be exported.
	 * @param webId the webpage identifier of the person for who the projects must be exported.
	 * @param username the name of the logged-in user.
	 * @return the model-view component.
	 * @see #showProjects(Integer, String, Integer, String, boolean, byte[])
	 */
	@GetMapping("/" + Constants.TEACHING_ACTIVITY_LIST_ENDPOINT)
	public ModelAndView teachingActivityList(
			@RequestParam(required = false, name = Constants.DBID_ENDPOINT_PARAMETER) Integer dbId,
			@RequestParam(required = false, name = Constants.WEBID_ENDPOINT_PARAMETER) String webId,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		ensureCredentials(username, Constants.TEACHING_ACTIVITY_LIST_ENDPOINT, dbId, inWebId);
		final ModelAndView modelAndView = new ModelAndView(Constants.TEACHING_ACTIVITY_LIST_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Person personObj = getPersonWith(dbId, inWebId, null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found with id " + dbId + " or web-id " + webId); //$NON-NLS-1$ //$NON-NLS-2$
		}
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		//
		initAdminTableButtons(modelAndView, endpoint(
				Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT,
				Constants.PERSON_ENDPOINT_PARAMETER, Integer.valueOf(personObj.getId()),
				Constants.ACTIVITY_ENDPOINT_PARAMETER));
		//
		final List<TeachingActivity> activities = extractActivityListWithoutFilter(dbId, inWebId);
		modelAndView.addObject("activities", activities); //$NON-NLS-1$
		//
		modelAndView.addObject("additionUrl", endpoint(Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER)); //$NON-NLS-1$
		return modelAndView;
	}

	private List<TeachingActivity> extractActivityListWithoutFilter(Integer dbId, String webId) {
		final List<TeachingActivity> activities;
		if (dbId != null && dbId.intValue() != 0) {
			activities = this.teachingService.getActivitiesByPersonId(dbId.intValue());
		} else if (!Strings.isNullOrEmpty(webId)) {
			final Person person = this.personService.getPersonByWebPageId(webId);
			if (person == null) {
				throw new IllegalArgumentException("Person not found with web identifier: " + webId); //$NON-NLS-1$
			}
			activities = this.teachingService.getActivitiesByPersonId(person.getId());
		} else {
			activities = this.teachingService.getAllActivities();
		}
		return activities;
	}

	/** Show the editor for a teaching activity. This editor permits to create or to edit an activity.
	 *
	 * @param person the identifier of the person associated to this activity.
	 * @param activity the identifier of the activity to edit. If it is {@code null}, the endpoint
	 *     is dedicated to the creation of an activity.
	 * @param username the name of the logged-in user.
	 * @return the model-view object.
	 * @throws IOException if there is some internal IO error when building the form's data.
	 */
	@GetMapping(value = "/" + Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT)
	public ModelAndView teachingActivityEditor(
			@RequestParam(required = false, name = Constants.PERSON_ENDPOINT_PARAMETER) Integer person,
			@RequestParam(required = false, name = Constants.ACTIVITY_ENDPOINT_PARAMETER) Integer activity,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws IOException {
		ensureCredentials(username, Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT, person, activity);
		final ModelAndView modelAndView = new ModelAndView(Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT);
		initModelViewWithInternalProperties(modelAndView, false);
		//
		final Person personObj = getPersonWith(person, null, null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new IllegalArgumentException("Person not found: " + person); //$NON-NLS-1$
		}
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		//
		final TeachingActivity activityObj;
		if (activity != null && activity.intValue() != 0) {
			activityObj = this.teachingService.getActivityById(activity.intValue());
			if (activityObj == null) {
				throw new IllegalArgumentException("Teaching activity not found: " + activity); //$NON-NLS-1$
			}

			// Provide more information about uploaded slides
			final String slidePath = activityObj.getPathToSlides();
			if (!Strings.isNullOrEmpty(slidePath)) {
				modelAndView.addObject("pathToSlides_basename", FileSystem.largeBasename(slidePath)); //$NON-NLS-1$
				modelAndView.addObject("pathToSlides_picture", rootedThumbnail(slidePath, false)); //$NON-NLS-1$
			}
			
			// Provide a YEAR-MONTH dates
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd"); //$NON-NLS-1$
			if (activityObj.getStartDate() != null) {
				modelAndView.addObject("formattedStartDate", activityObj.getStartDate().format(formatter)); //$NON-NLS-1$
			}
			if (activityObj.getEndDate() != null) {
				modelAndView.addObject("formattedEndDate", activityObj.getEndDate().format(formatter)); //$NON-NLS-1$
			}
		} else {
			activityObj = null;
		}
		modelAndView.addObject("activity", activityObj); //$NON-NLS-1$
		//
		final List<TeachingActivityLevel> sortedLevels = Arrays.asList(TeachingActivityLevel.values());
		modelAndView.addObject("sortedLevels", sortedLevels); //$NON-NLS-1$
		//
		final List<StudentType> sortedStudentTypes = Arrays.asList(StudentType.values());
		modelAndView.addObject("sortedStudentTypes", sortedStudentTypes); //$NON-NLS-1$
		//
		final List<TeacherRole> sortedRoles = Arrays.asList(TeacherRole.values());
		modelAndView.addObject("sortedRoles", sortedRoles); //$NON-NLS-1$
		//
		final List<TeachingActivityType> sortedActivityTypes = Arrays.asList(TeachingActivityType.values());
		modelAndView.addObject("sortedActivityTypes", sortedActivityTypes); //$NON-NLS-1$
		//
		final List<ResearchOrganization> organizations = this.organizationService.getAllResearchOrganizations().stream()
				.sorted((a, b) -> a.getAcronymOrName().compareToIgnoreCase(b.getAcronymOrName()))
				.collect(Collectors.toList());
		modelAndView.addObject("organizations", organizations); //$NON-NLS-1$
		//
		modelAndView.addObject("formActionUrl", endpoint(Constants.TEACHING_ACTIVITY_SAVING_ENDPOINT, Constants.PERSON_ENDPOINT_PARAMETER, person)); //$NON-NLS-1$
		modelAndView.addObject("formRedirectUrl", endpoint(Constants.TEACHING_ACTIVITY_LIST_ENDPOINT, Constants.DBID_ENDPOINT_PARAMETER, person)); //$NON-NLS-1$
		modelAndView.addObject("countryLabels", CountryCodeUtils.getAllDisplayCountries()); //$NON-NLS-1$
		modelAndView.addObject("defaultLanguage", TeachingActivity.DEFAULT_LANGUAGE); //$NON-NLS-1$
		//
		return modelAndView;
	}

	/** Show the list of the teaching activities for the given person.
	 *
	 * @param dbId the database identifier of the person who is supervisor. If it is not provided, the webId should be provided.
	 * @param webId the web-page identifier of the person who is supervisor. If it is not provided, the dbId should be provided.
	 * @param activeOnly indicates if the inactive activities (old activities) are displayed. By default, they are hidden.
	 *     If this argument is {@code true}, only active activities are shown. If it is {@code false}, all the activities
	 *     are shown.
	 * @param embedded indicates if the view will be embedded into a larger page, e.g., WordPress page. 
	 * @param username the name of the logged-in user.
	 * @return the model-view.
	 */
	@GetMapping("/showTeaching")
	public ModelAndView showTeaching(
			@RequestParam(required = false, name = Constants.DBID_ENDPOINT_PARAMETER) Integer dbId,
			@RequestParam(required = false, name = Constants.WEBID_ENDPOINT_PARAMETER) String webId,
			@RequestParam(required = false, defaultValue = "false") boolean activeOnly,
			@RequestParam(required = false, defaultValue = "false") boolean embedded,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		final String inWebId = inString(webId);
		readCredentials(username, "showTeaching", dbId, inWebId); //$NON-NLS-1$
		final ModelAndView modelAndView = new ModelAndView("showTeaching"); //$NON-NLS-1$
		initModelViewWithInternalProperties(modelAndView, embedded);
		//
		final Person personObj = getPersonWith(dbId, inWebId, null, this.personService, this.nameParser);
		if (personObj == null) {
			throw new RuntimeException("Person not found"); //$NON-NLS-1$
		}
		final List<TeachingActivity> activities = this.teachingService.getActivitiesByPersonId(personObj.getId());
		final boolean showAll = !activeOnly;
		final List<TeachingActivity> sortedActivities = activities.stream()
				.filter(it -> {
					if (showAll || it.isActive()) {
						return true;
					}
					return false;
				})
				.sorted(EntityUtils.getPreferredTeachingActivityComparator())
				.collect(Collectors.toList()); 
		modelAndView.addObject("person", personObj); //$NON-NLS-1$
		modelAndView.addObject("activities", sortedActivities); //$NON-NLS-1$
		if (isLoggedIn()) {
			modelAndView.addObject("editionUrl", endpoint(Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT, //$NON-NLS-1$
					Constants.PERSON_ENDPOINT_PARAMETER, Integer.valueOf(personObj.getId()),
					Constants.ACTIVITY_ENDPOINT_PARAMETER));
			modelAndView.addObject("additionUrl", endpoint(Constants.TEACHING_ACTIVITY_EDITING_ENDPOINT, //$NON-NLS-1$
					Constants.PERSON_ENDPOINT_PARAMETER, Integer.valueOf(personObj.getId())));
		}
		return modelAndView;
	}

}
