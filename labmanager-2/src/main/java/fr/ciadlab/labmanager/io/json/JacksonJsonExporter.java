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

package fr.ciadlab.labmanager.io.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.html.HtmlPageExporter;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Exporter of publications to JSON using the Jackson API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
@Component
@Primary
public class JacksonJsonExporter implements JsonExporter {

	private HtmlPageExporter htmlPageExporter;

	/** Constructor.
	 * 
	 * @param htmlPageExporter the exporter for HTML pages.
	 */
	public JacksonJsonExporter(@Autowired HtmlPageExporter htmlPageExporter) {
		this.htmlPageExporter = htmlPageExporter;
	}

	@Override
	public String exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode node = exportPublicationsAsTree(publications, configurator, mapper);
		return mapper.writer().writeValueAsString(node);
	}

	/** Export the publications into a JSON tree.
	 * 
	 * @param publications the publications to export.
	 * @param configurator the configurator for the export, never {@code null}.
	 * @param mapper the JSON object creator and mapper.
	 * @return the representation of the publications.
	 * @throws Exception if the publication cannot be converted.
	 */
	public JsonNode exportPublicationsAsTree(Iterable<? extends Publication> publications, ExporterConfigurator configurator,
			ObjectMapper mapper) throws Exception {
		if (publications == null) {
			return mapper.nullNode();
		}
		final ArrayNode array = mapper.createArrayNode();
		for (final Publication publication : publications) {
			ObjectNode entryNode = null;
			final JsonNode node = exportPublication(publication, configurator, mapper);
			if (node != null) {
				entryNode = mapper.createObjectNode();
				entryNode.set("data", node); //$NON-NLS-1$
			}
			if (configurator.isDownloadButtons() || configurator.isExportButtons() || configurator.isEditButtons()
					|| configurator.isDeleteButtons()) {
				final ObjectNode htmlNodes = mapper.createObjectNode();
				if (configurator.isDownloadButtons()) {
					final String button0 = this.htmlPageExporter.getButtonToDownloadPublicationPDF(publication.getPathToDownloadablePDF());
					final String button1 = this.htmlPageExporter.getButtonToDownloadPublicationAwardCertificate(publication.getPathToDownloadableAwardCertificate());
					final ArrayNode array0 = mapper.createArrayNode();
					if (!Strings.isNullOrEmpty(button0)) {
						array0.add(mapper.valueToTree(button0));
					}
					if (!Strings.isNullOrEmpty(button1)) {
						array0.add(mapper.valueToTree(button1));
					}
					if (!array0.isEmpty()) {
						htmlNodes.set("download", array0); //$NON-NLS-1$
					}
				}
				if (configurator.isExportButtons()) {
					final String button0 = this.htmlPageExporter.getButtonToExportPublicationToBibTeX(publication.getId());
					final String button1 = this.htmlPageExporter.getButtonToExportPublicationToOpenDocument(publication.getId());
					final String button2 = this.htmlPageExporter.getButtonToExportPublicationToHtml(publication.getId());
					final ArrayNode array0 = mapper.createArrayNode();
					if (!Strings.isNullOrEmpty(button0)) {
						array0.add(mapper.valueToTree(button0));
					}
					if (!Strings.isNullOrEmpty(button1)) {
						array0.add(mapper.valueToTree(button1));
					}
					if (!Strings.isNullOrEmpty(button2)) {
						array0.add(mapper.valueToTree(button2));
					}
					if (!array0.isEmpty()) {
						htmlNodes.set("export", array0); //$NON-NLS-1$
					}
				}
				if (configurator.isEditButtons()) {
					final String button = this.htmlPageExporter.getButtonToEditPublication(publication.getId());
					if (!Strings.isNullOrEmpty(button)) {
						htmlNodes.set("edit", mapper.valueToTree(button)); //$NON-NLS-1$
					}
				}
				if (configurator.isDeleteButtons()) {
					final String button = this.htmlPageExporter.getButtonToDeletePublication(publication.getId());
					if (!Strings.isNullOrEmpty(button)) {
						htmlNodes.set("delete", mapper.valueToTree(button)); //$NON-NLS-1$
					}
				}
				if (!htmlNodes.isEmpty()) {
					if (entryNode == null) {
						entryNode = mapper.createObjectNode();
					}
					entryNode.set("html", htmlNodes); //$NON-NLS-1$
				}
			}
			if (entryNode != null) {
				array.add(entryNode);
			}
		}
		return array;
	}

	/** Export in JSON a single publication.
	 *
	 * @param publication the publication, never {@code null}.
	 * @param configurator the configurator for the exporter.
	 * @param mapper the JSON object creator and mapper.
	 * @return the representation of the publication.
	 * @throws Exception if the publication cannot be converted.
	 */
	@SuppressWarnings("static-method")
	public JsonNode exportPublication(Publication publication, ExporterConfigurator configurator, ObjectMapper mapper) throws Exception  {
		assert publication != null;
		return mapper.valueToTree(publication);
	}

}
