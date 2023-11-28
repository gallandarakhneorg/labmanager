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

package fr.ciadlab.labmanager.controller.api.indicator;

import java.util.List;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.controller.api.AbstractApiController;
import fr.ciadlab.labmanager.service.indicator.GlobalIndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST Controller for manipulating general indicators.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@RestController
@CrossOrigin
public class GlobalIndicatorApiController extends AbstractApiController {

	private GlobalIndicatorsService indicatorService;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the accessor to the localized messages.
	 * @param constants the constants of the app.
	 * @param indicatorService the service for accessing the global indicators.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public GlobalIndicatorApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired GlobalIndicatorsService indicatorService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.indicatorService = indicatorService;
	}

	/** Save or create the global indicators.
	 *
	 * @param visibleIndicators the list of the keys of the visible indicators.
	 * @param username the name of the logged-in user.
	 */
	@PutMapping("/" + Constants.GLOBAL_INDICATORS_SAVING_ENDPOINT)
	public void saveGlobalIndicators(
			@RequestParam(required = false) List<String> visibleIndicators,
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.GLOBAL_INDICATORS_SAVING_ENDPOINT);
		this.indicatorService.setVisibleIndicators(visibleIndicators);
	}

	/** Reset the cached global indicators.
	 *
	 * @param username the name of the logged-in user.
	 */
	@PutMapping("/" + Constants.RESET_INDICATOR_CACHE_ENDPOINT)
	public void resetIndicatorCache(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) {
		ensureCredentials(username, Constants.RESET_INDICATOR_CACHE_ENDPOINT);
		this.indicatorService.clearCache();
	}

}
