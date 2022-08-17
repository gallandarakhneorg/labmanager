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

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.html.HtmlPageExporter;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;
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
public class JacksonJsonExporter extends AbstractJsonExporter {

	private HtmlPageExporter htmlPageExporter;

	/** Constructor.
	 * 
	 * @param messages the accessors to the localized messages.
	 * @param htmlPageExporter the exporter for HTML pages.
	 * @param doiTools the tools for managing the DOI.
	 */
	public JacksonJsonExporter(@Autowired MessageSourceAccessor messages, @Autowired HtmlPageExporter htmlPageExporter) {
		super(messages);
		this.htmlPageExporter = htmlPageExporter;
	}

	@Override
	public String exportPublicationsWithRootKeys(Iterable<? extends Publication> publications, ExporterConfigurator configurator,
			String... rootKeys) throws Exception {
		final ObjectMapper mapper = new ObjectMapper();
		final JsonNode node = exportPublicationsAsTree(publications, configurator, mapper);
		JsonNode root = node;
		if (rootKeys != null) {
			for (int i = rootKeys.length - 1; i >= 0; --i) {
				final String rkey = rootKeys[i];
				final ObjectNode onode = mapper.createObjectNode();
				onode.set(rkey, root);
				root = onode; 
			}
		}
		return mapper.writer().writeValueAsString(root);
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
		final boolean extraButtons = configurator.isDownloadButtons() || configurator.isExportButtons() || configurator.isEditButtons()
				|| configurator.isDeleteButtons() || configurator.isFormattedAuthorList() || configurator.isFormattedPublicationDetails()
				|| configurator.isFormattedLinks() || configurator.isTypeAndCategoryLabels();
		for (final Publication publication : publications) {
			final ObjectNode entryNode = exportPublication(publication, configurator, mapper);
			// Make aliasing of the year
			if (entryNode.has("publicationYear")) { //$NON-NLS-1$
				entryNode.set("year", entryNode.get("publicationYear").deepCopy()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			// Add labels for type and category
			if (configurator.isTypeAndCategoryLabels()) {
				if (entryNode.has("type")) { //$NON-NLS-1$
					entryNode.set("htmlTypeLabel", mapper.valueToTree(publication.getType().getLabel())); //$NON-NLS-1$
				}
				if (entryNode.has("category")) { //$NON-NLS-1$
					entryNode.set("htmlCategoryLabel", mapper.valueToTree(publication.getType().getCategory(publication.isRanked()).getLabel())); //$NON-NLS-1$
				}
			}
			//
			if (extraButtons) {
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
					entryNode.set("htmlDownloads", array0); //$NON-NLS-1$
				}
				if (configurator.isExportButtons()) {
					final String button0 = this.htmlPageExporter.getButtonToExportPublicationToBibTeX(publication.getId(), configurator);
					final String button1 = this.htmlPageExporter.getButtonToExportPublicationToOpenDocument(publication.getId(), configurator);
					final String button2 = this.htmlPageExporter.getButtonToExportPublicationToHtml(publication.getId(), configurator);
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
					entryNode.set("htmlExports", array0); //$NON-NLS-1$
				}
				if (configurator.isEditButtons()) {
					final String editButton = this.htmlPageExporter.getButtonToEditPublication(publication.getId());
					if (!Strings.isNullOrEmpty(editButton)) {
						final JsonNode editNode = mapper.valueToTree(editButton);
						entryNode.set("htmlEdit", editNode); //$NON-NLS-1$
					}
				}
				if (configurator.isDeleteButtons()) {
					final String deleteButton = this.htmlPageExporter.getButtonToDeletePublication(publication.getId());
					if (!Strings.isNullOrEmpty(deleteButton)) {
						final JsonNode deleteNode = mapper.valueToTree(deleteButton);
						entryNode.set("htmlDelete", deleteNode); //$NON-NLS-1$
					}
				}
				if (configurator.isFormattedAuthorList()) {
					entryNode.set("htmlAuthors", mapper.valueToTree(this.htmlPageExporter.generateHtmlAuthors(publication, configurator))); //$NON-NLS-1$
				}
				if (configurator.isFormattedPublicationDetails()) {
					entryNode.set("htmlPublicationDetails", mapper.valueToTree(this.htmlPageExporter.generateHtmlPublicationDetails(publication, configurator))); //$NON-NLS-1$
				}
				if (configurator.isFormattedLinks()) {
					entryNode.set("htmlLinks", mapper.valueToTree(this.htmlPageExporter.generateHtmlLinks(publication, configurator))); //$NON-NLS-1$
				}
			}
			array.add(entryNode);
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
	public ObjectNode exportPublication(Publication publication, ExporterConfigurator configurator, ObjectMapper mapper) throws Exception  {
		assert publication != null;
		return mapper.valueToTree(publication);
	}

	/** Serializer of {@code IdentifiableEntity} that serializes the identifiers.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	protected static class IdentifierSerializer extends JsonSerializer<IdentifiableEntity> {

		@Override
		public void serialize(IdentifiableEntity value, JsonGenerator generator, SerializerProvider serializers)
				throws IOException {
			generator.writeNumber(value.getId());
		}

	}

}
