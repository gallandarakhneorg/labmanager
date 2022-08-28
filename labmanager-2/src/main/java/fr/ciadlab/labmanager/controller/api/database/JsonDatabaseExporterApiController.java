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

package fr.ciadlab.labmanager.controller.api.database;

import java.util.Map;

import fr.ciadlab.labmanager.AbstractComponent;
import fr.ciadlab.labmanager.io.json.DatabaseToJsonExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** This controller provides a tool for exporting the content of the database according
 * to a specific JSON format that is independent of any database engine.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@RestController
@CrossOrigin
public class JsonDatabaseExporterApiController extends AbstractComponent {

	private DatabaseToJsonExporter exporter;

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param exporter the exporter.
	 */
	public JsonDatabaseExporterApiController(
			@Autowired MessageSourceAccessor messages,
			@Autowired DatabaseToJsonExporter exporter) {
		super(messages);
		this.exporter = exporter;
	}

	/** Export the JSON.
	 *
	 * @param model the model.
	 * @return not used.
	 */
	@GetMapping("/exportDatabaseToJson")
	public @ResponseBody Map<String, Object> exportDatabaseToJson() {
		try {
			return this.exporter.exportFromDatabase();
		} catch (Exception ex) {
			getLogger().error(ex.getLocalizedMessage(), ex);
			return null;
		}
	}

}
