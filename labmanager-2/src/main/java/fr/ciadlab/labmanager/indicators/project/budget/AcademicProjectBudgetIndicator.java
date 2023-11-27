/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.ciadlab.labmanager.indicators.project.budget;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.project.Project;
import fr.ciadlab.labmanager.entities.project.ProjectCategory;
import fr.ciadlab.labmanager.service.project.ProjectService;
import fr.ciadlab.labmanager.utils.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

/** Sum the budgets of academic projects.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.2
 */
@Component
public class AcademicProjectBudgetIndicator extends AbstractProjectBudgetIndicator {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 * @param projectService the service for accessing the projects.
	 */
	public AcademicProjectBudgetIndicator(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired ProjectService projectService) {
		super(messages, constants, projectService);
	}

	@Override
	public String getName() {
		return getMessage("academicProjectBudgetIndicator.name"); //$NON-NLS-1$
	}

	@Override
	public String getLabel(Unit unit) {
		return getLabelWithYears("academicProjectBudgetIndicator.label", unit.getLabel()); //$NON-NLS-1$
	}

	@Override
	public boolean isSelectableProject(Project project) {
		if (project != null) {
			final ProjectCategory cat = project.getCategory();
			return cat == ProjectCategory.COMPETITIVE_CALL_PROJECT || cat == ProjectCategory.AUTO_FUNDING;
		}
		return false;
	}

}
