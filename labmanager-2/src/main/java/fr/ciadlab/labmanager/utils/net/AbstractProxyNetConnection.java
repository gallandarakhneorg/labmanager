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
 * you entered into with the SeT.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.utils.net;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

/** A wrapper for the network connection that assumes a connection through a proxy.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0
 */
public abstract class AbstractProxyNetConnection implements NetConnection {

	@Override
	public BufferedImage getImageFromURL(URL url) throws IOException {
		final Proxy proxy = new Proxy(getProxyType(), new InetSocketAddress(getProxyAddress(), getProxyPort()));
		final URLConnection connection = url.openConnection(proxy);
		connection.connect();
		try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			try (final InputStream in = connection.getInputStream()) {
				final byte[] buf = new byte[1024];
				int n = 0;
				while (-1 != (n = in.read(buf))) {
					out.write(buf, 0, n);
				}
			}
			final byte[] response = out.toByteArray();
			try (final ByteArrayInputStream bis = new ByteArrayInputStream(response)) {
				return ImageIO.read(bis);
			}
		}
	}

	/** Replies the type of the proxy server to be used for the connexion.
	 *
	 * @return the proxy server type.
	 */
	protected abstract Proxy.Type getProxyType();

	/** Replies the address of the proxy server to be used for the connexion.
	 *
	 * @return the proxy server address.
	 */
	protected abstract String getProxyAddress();

	/** Replies the port of the proxy server to be used for the connexion.
	 *
	 * @return the proxy port.
	 */
	protected abstract int getProxyPort();

}

