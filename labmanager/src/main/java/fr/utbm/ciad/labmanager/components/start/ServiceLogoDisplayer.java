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

package fr.utbm.ciad.labmanager.components.start;

import com.google.common.base.Strings;
import fr.utbm.ciad.labmanager.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** This listener displays the service logo on the log console at start-up.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.8
 */
@Component
@Order(0)
public class ServiceLogoDisplayer implements ApplicationListener<ApplicationReadyEvent> {

	/** Logger of the service. It is lazy loaded.
	 */
	private Logger logger;

	private final String logo;

	/** Constructor.
	 * 
	 * @param logo the logo to display.
	 */
	public ServiceLogoDisplayer(
			@Value("${labmanager.splash-screen}") String logo) {
		this.logo = Strings.emptyToNull(logo);
	}

	/** Replies the logo to be displayed.
	 *
	 * @return the logo.
	 */
	public String getLogo() {
		return this.logo;
	}

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

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		final var lg = getLogo();
		if (!Strings.isNullOrEmpty(lg)) {
			final var lines = lg.split("\\n"); //$NON-NLS-1$
			final var logger = getLogger();
			for (final var line : lines) {
				logger.info(line);
			}
			logger.info(" . version " + Constants.MANAGER_VERSION);//$NON-NLS-1$
			logger.info(" . ready");//$NON-NLS-1$
		}
	}

}
