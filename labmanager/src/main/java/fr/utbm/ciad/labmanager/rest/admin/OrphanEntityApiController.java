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

package fr.utbm.ciad.labmanager.rest.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.rest.AbstractApiController;
import fr.utbm.ciad.labmanager.services.OrphanEntityBuilder;
import fr.utbm.ciad.labmanager.services.admin.OrphanEntityService;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import org.arakhne.afc.progress.ProgressionEvent;
import org.arakhne.afc.progress.ProgressionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

/** This controller provides a tool for detecting and managing orphan entities
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@RestController
@CrossOrigin
public class OrphanEntityApiController extends AbstractApiController {

	private List<? extends OrphanEntityBuilder<?>> builders;

	private OrphanEntityService orphanService;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the constants of the app.
	 * @param builders the list of the builders that are albeo to compute orphan entities.
	 * @param orphanService the service for manupulating the orphan entities.
	 * @param usernameKey the key string for encrypting the usernames.
	 */
	public OrphanEntityApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired List<? extends OrphanEntityBuilder<?>> builders,
			@Autowired OrphanEntityService orphanService,
			@Value("${labmanager.security.username-key}") String usernameKey) {
		super(messages, constants, usernameKey);
		this.builders = builders;
		this.orphanService = orphanService;
	}

	/** Detect and replies the orphan values.
	 *
	 * @param response the HTTP response.
	 * @param username the name of the logged-in user.
	 * @return the progress indicator.
	 * @throws Exception in case of error.
	 */
	@GetMapping(value = "/" + Constants.COMPUTE_ORPHAN_ENTITIES_ENDPOINT)
	public SseEmitter computeOrphanEntities(
			@CookieValue(name = "labmanager-user-id", defaultValue = Constants.ANONYMOUS) byte[] username) throws Exception {
		//TODO ensureCredentials(username, Constants.COMPUTE_ORPHAN_ENTITIES_ENDPOINT);
		//
		final ExecutorService service = Executors.newSingleThreadExecutor();
		final SseEmitter emitter = new SseEmitter(Long.valueOf(Constants.SSE_TIMEOUT));
		//
		final ProgressionListener progressListener = new ProgressionListener() {
			@Override
			public void onProgressionValueChanged(ProgressionEvent event) {
				final Map<String, Object> content = new HashMap<>();
				content.put("percent", Integer.valueOf((int) event.getPercent())); //$NON-NLS-1$
				content.put("terminated", Boolean.FALSE); //$NON-NLS-1$
				try {
					final ObjectMapper mapper = JsonUtils.createMapper();
					final SseEventBuilder sseevent = SseEmitter.event().data(mapper.writeValueAsString(content), MediaType.APPLICATION_JSON);
					emitter.send(sseevent);
				} catch (RuntimeException ex) {
					throw ex;
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		};
		//
		service.execute(() -> {
			try {
				final ObjectNode root = this.orphanService.detectOrphanEntities(this.builders, progressListener);
				//
				try {
					final ObjectMapper mapper = JsonUtils.createMapper();
					final ObjectNode content = mapper.createObjectNode();
					content.put("percent", Integer.valueOf(Constants.HUNDRED)); //$NON-NLS-1$
					content.put("terminated", Boolean.TRUE); //$NON-NLS-1$
					content.set("data", root); //$NON-NLS-1$
					final SseEventBuilder sseevent = SseEmitter.event().data(mapper.writeValueAsString(content), MediaType.APPLICATION_JSON);
					emitter.send(sseevent);
				} catch (RuntimeException ex) {
					throw ex;
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			} catch (Throwable ex) {
				getLogger().error(ex.getLocalizedMessage(), ex);
			}
		});
		return emitter;
	}

}
