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

package fr.ciadlab.labmanager.utils.net;

import java.net.Proxy.Type;

import org.springframework.stereotype.Component;

/** A wrapper for the network connection that assumes a connection from the UTBM network.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0
 */
@Component
public class UtbmNetConnection extends AbstractProxyNetConnection {

    private static final String PROXY_URL = "proxy.utbm.fr"; //$NON-NLS-1$

    private static final int PROXY_PORT = 3128;

	@Override
	protected int getProxyPort() {
		return PROXY_PORT;
	}

	@Override
	protected Type getProxyType() {
		return Type.HTTP;
	}

	@Override
	protected String getProxyAddress() {
		return PROXY_URL;
	}

}

