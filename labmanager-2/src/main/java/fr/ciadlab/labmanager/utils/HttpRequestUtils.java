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

package fr.ciadlab.labmanager.utils;

import java.io.StringWriter;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONWriter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** Utilities for HTTP request.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
public class HttpRequestUtils {

	private static final String[] IP_HEADER_CANDIDATES = {
			"X-Forwarded-For", //$NON-NLS-1$
			"Proxy-Client-IP", //$NON-NLS-1$
			"WL-Proxy-Client-IP", //$NON-NLS-1$
			"HTTP_X_FORWARDED_FOR", //$NON-NLS-1$
			"HTTP_X_FORWARDED", //$NON-NLS-1$
			"HTTP_X_CLUSTER_CLIENT_IP", //$NON-NLS-1$
			"HTTP_CLIENT_IP", //$NON-NLS-1$
			"HTTP_FORWARDED_FOR", //$NON-NLS-1$
			"HTTP_FORWARDED", //$NON-NLS-1$
			"HTTP_VIA", //$NON-NLS-1$
			"REMOTE_ADDR" //$NON-NLS-1$
	};

	private HttpRequestUtils() {
		//
	}

	/** Replie the Client IP of a Servlet request is ongoing.
	 *
	 * @return the client address.
	 */
	public static String getClientIpAddressIfServletRequestExist() {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return "0.0.0.0"; //$NON-NLS-1$
		}
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		final StringWriter swriter = new StringWriter();
		JSONWriter writer = new JSONWriter(swriter);
		writer.object();
		for (String header: IP_HEADER_CANDIDATES) {
			final String ipList = request.getHeader(header);
			if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) { //$NON-NLS-1$
				writer = writer.key(header);
				writer = writer.value(ipList);
			}
		}
		writer.key("_REMOTE_ADDR"); //$NON-NLS-1$
		writer.value(request.getRemoteAddr());
		writer = writer.endObject();
		return swriter.toString();
	}
}
