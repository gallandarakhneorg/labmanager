/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.io.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Definition of constants for the JSON tools.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class JsonTool {

	/** Prefix for hidden JSON fields.
	 */
	protected static final String HIDDEN_FIELD_PREFIX = "_"; //$NON-NLS-1$

	/** Prefix for special JSON fields.
	 */
	protected  static final String SPECIAL_FIELD_PREFIX = "@"; //$NON-NLS-1$

	/** Prefix for research organization identifiers.
	 */
	protected  static final String RESEARCHORGANIZATION_ID_PREFIX = "/ro#"; //$NON-NLS-1$

	/** Prefix for person identifiers.
	 */
	protected  static final String PERSON_ID_PREFIX = "/pers#"; //$NON-NLS-1$

	/** Prefix for journal identifiers.
	 */
	protected  static final String JOURNAL_ID_PREFIX = "/jour#"; //$NON-NLS-1$

	/** The name of the field that is an JSON identifier.
	 */
	protected static final String ID_FIELDNAME = SPECIAL_FIELD_PREFIX + "id"; //$NON-NLS-1$

	/** The name of the field that is the date of the last modification of the file.
	 */
	protected static final String LAST_CHANGE_FIELDNAME = HIDDEN_FIELD_PREFIX + "last-update-date"; //$NON-NLS-1$

	/** Prefix for for the setter functions.
	 */
	protected static final String SETTER_FUNCTION_PREFIX = "set"; //$NON-NLS-1$

	/** Prefix for for the getter functions.
	 */
	protected static final String GETTER_FUNCTION_PREFIX = "get"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to research organizations.
	 */
	protected static final String RESEARCHORGANIZATIONS_SECTION = "researchOrganizations"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to persons.
	 */
	protected static final String PERSONS_SECTION = "persons"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to memberships.
	 */
	protected static final String MEMBERSHIPS_SECTION = "memberships"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to journals.
	 */
	protected static final String JOURNALS_SECTION = "journals"; //$NON-NLS-1$

	/** Name of the field for the super organization reference.
	 */
	protected static final String SUPERORGANIZATION_KEY = "superOrganization"; //$NON-NLS-1$

	/** Name of the field for the person reference.
	 */
	protected static final String PERSON_KEY = "person"; //$NON-NLS-1$

	/** Name of the field for the research organization reference.
	 */
	protected static final String RESEARCHORGANIZATION_KEY = "researchOrganization"; //$NON-NLS-1$

	/** Name of the field for the definition of the journals' annual quality indicators.
	 */
	protected static final String QUALITYINDICATORSHISTORY_KEY = "qualityIndicatorsHistory"; //$NON-NLS-1$

	/** Name of the field for the Scimago Q-Index.
	 */
	protected static final String SCIMAGOQINDEX_KEY = "scimagoQIndex"; //$NON-NLS-1$

	/** Name of the field for the Web-of-Science Q-Index.
	 */
	protected static final String WOSQINDEX_KEY = "wosQIndex"; //$NON-NLS-1$

	/** Name of the field for the journal impact factor.
	 */
	protected static final String IMPACTFACTOR_KEY = "impactFactor"; //$NON-NLS-1$

	/** Logger of the service. It is lazy loaded.
	 */
	private Logger logger;

	/** Replies the logger of this service.
	 *
	 * @return the logger.
	 */
	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = createLogger();
		}
		return this.logger;
	}

	/** Change the logger of this service.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/** Factory method for creating the service logger.
	 *
	 * @return the logger.
	 */
	protected Logger createLogger() {
		return LoggerFactory.getLogger(getClass());
	}

}
