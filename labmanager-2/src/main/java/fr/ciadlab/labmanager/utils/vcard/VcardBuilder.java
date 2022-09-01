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

package fr.ciadlab.labmanager.utils.vcard;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import org.springframework.http.MediaType;

/** A builder of a Virtual Card File (VCF).
 * <p>Virtual Card Format (VCF) or vCard is a digital file format for storing contact information.
 * The format is widely used for data interchange among popular information exchange applications.
 * A single VCF file can contain contact information for one or multiple contacts.
 * A VCF file usually contains information such as contact’s name, address, phone number, email,
 * birthday, photographs and audio in addition to a number of other fields.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public interface VcardBuilder {

	/** Mime type for a Vcard.
	 *
	 * @see #VCARD_MIME_TYPE
	 */
	String VCARD_MIME_TYPE_VALUE = "text/vcard"; //$NON-NLS-1$
	
	/** Mime type for a Vcard.
	 *
	 * @see #VCARD_MIME_TYPE_VALUE
	 */
	MediaType VCARD_MIME_TYPE = MediaType.parseMediaType(VCARD_MIME_TYPE_VALUE + ";charset=utf-8"); //$NON-NLS-1$

	/** Build the Vcard for the given person.
	 *
	 * @param person the person for who the Vcard should be built.
	 * @param organization the organization to consider for generating the card. If it not provided, the first available organization
	 *     will be used from the active memberships.
	 * @return the content of the Vcard.
	 */
	String build(Person person, ResearchOrganization organization);

}
