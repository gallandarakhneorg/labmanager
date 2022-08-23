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

package fr.ciadlab.labmanager.utils;

import java.util.Collection;
import java.util.Collections;

import org.apache.jena.ext.com.google.common.base.Strings;

/** An exception that contains multiple causes.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public class ComposedException extends Exception {

	private static final long serialVersionUID = 1077595052229473266L;

	private final Collection<? extends Throwable> errors;

	/** Constructor.
	 * 
	 * @param errors the causes.
	 */
	public ComposedException(Collection<? extends Throwable> errors) {
		super();
		this.errors = errors;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		final String msg = super.getMessage();
		if (Strings.isNullOrEmpty(msg)) {
			final StringBuilder b = new StringBuilder();
			for (final Throwable ex : getCauses()) {
				if (!Strings.isNullOrEmpty(ex.getLocalizedMessage())) {
					if (b.length() > 0) {
						b.append("\n"); //$NON-NLS-1$
					}
					b.append(ex.getLocalizedMessage());
				}
			}
			return b.toString();
		}
		return msg;
	}

	/** Replies the causes of this exception.
	 *
	 * @return the causes.
	 */
	public synchronized Collection<? extends Throwable> getCauses() {
		return this.errors == null ? Collections.emptyList() : this.errors;
	}

}
