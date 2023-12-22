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

package fr.utbm.ciad.labmanager.utils.io.json;

import java.io.IOException;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.utbm.ciad.labmanager.data.IdentifiableEntity;
import fr.utbm.ciad.labmanager.data.publication.JournalBasedPublication;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.utils.io.ExporterConfigurator;
import fr.utbm.ciad.labmanager.utils.io.html.HtmlPageExporter;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
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
		final var root = exportPublicationsAsTreeWithRootKeys(publications, configurator, null, rootKeys);
		final var mapper = JsonUtils.createMapper();
		return mapper.writer().writeValueAsString(root);
	}

	@Override
	public JsonNode exportPublicationsAsTreeWithRootKeys(Iterable<? extends Publication> publications, ExporterConfigurator configurator,
			Procedure2<Publication, ObjectNode> callback, String... rootKeys) throws Exception {
		final var mapper = JsonUtils.createMapper();
		final var node = exportPublicationsAsTree(publications, configurator, callback, mapper, configurator.getLocale());
		var root = node;
		if (rootKeys != null) {
			for (var i = rootKeys.length - 1; i >= 0; --i) {
				final var rkey = rootKeys[i];
				final var onode = mapper.createObjectNode();
				onode.set(rkey, root);
				root = onode; 
			}
		}
		return root;
	}

	/** Export the publications into a JSON tree.
	 * 
	 * @param publications the publications to export.
	 * @param configurator the configurator for the export, never {@code null}.
	 * @param callback a function that is invoked for each publication for giving the opportunity
	 *     to fill up the Json node of the publication.
	 * @param mapper the JSON object creator and mapper.
	 * @param locale the locale to use.
	 * @return the representation of the publications.
	 * @throws Exception if the publication cannot be converted.
	 */
	public JsonNode exportPublicationsAsTree(Iterable<? extends Publication> publications, ExporterConfigurator configurator,
			Procedure2<Publication, ObjectNode> callback, ObjectMapper mapper, Locale locale) throws Exception {
		if (publications == null) {
			return mapper.nullNode();
		}
		final var array = mapper.createArrayNode();
		final var extraButtons = configurator.isDownloadButtons() || configurator.isExportButtons() || configurator.isEditButtons()
				|| configurator.isDeleteButtons() || configurator.isFormattedAuthorList() || configurator.isFormattedPublicationDetails()
				|| configurator.isFormattedLinks() || configurator.isTypeAndCategoryLabels();
		for (final var publication : publications) {
			final var entryNode = exportPublication(publication, configurator, mapper);
			// Add additional fields by the callback function
			if (callback != null) {
				callback.apply(publication, entryNode);
			}
			// Make aliasing of the year
			if (entryNode.has("publicationYear")) { //$NON-NLS-1$
				entryNode.set("year", entryNode.get("publicationYear").deepCopy()); //$NON-NLS-1$ //$NON-NLS-2$
			}
			// Make aliasing for the Scimago Quartiles
			if (entryNode.has("scimagoQIndex") && publication instanceof JournalBasedPublication jbp) { //$NON-NLS-1$
				final var url = configurator.getJournalService().getScimagoQuartileImageURLByJournal(jbp.getJournal());
				if (url != null) {
					entryNode.set("scimagoQIndex_imageUrl", entryNode.textNode(url.toExternalForm())); //$NON-NLS-1$
				}
			}
			// Add labels for type and category
			if (configurator.isTypeAndCategoryLabels()) {
				if (entryNode.has("type")) { //$NON-NLS-1$
					entryNode.set("htmlTypeLabel", mapper.valueToTree(publication.getType().getLabel(getMessageSourceAccessor(), locale))); //$NON-NLS-1$
				}
				if (entryNode.has("category")) { //$NON-NLS-1$
					entryNode.set("htmlCategoryLabel", mapper.valueToTree(publication.getCategory().getLabel(getMessageSourceAccessor(), locale))); //$NON-NLS-1$
				}
			}
			//
			if (extraButtons) {
				if (configurator.isDownloadButtons()) {
					final var button0 = this.htmlPageExporter.getButtonToDownloadPublicationPDF(publication.getPathToDownloadablePDF());
					final var button1 = this.htmlPageExporter.getButtonToDownloadPublicationAwardCertificate(publication.getPathToDownloadableAwardCertificate());
					final var array0 = mapper.createArrayNode();
					if (!Strings.isNullOrEmpty(button0)) {
						array0.add(mapper.valueToTree(button0));
					}
					if (!Strings.isNullOrEmpty(button1)) {
						array0.add(mapper.valueToTree(button1));
					}
					entryNode.set("htmlDownloads", array0); //$NON-NLS-1$
				}
				if (configurator.isExportButtons()) {
					final var button0 = this.htmlPageExporter.getButtonToExportPublicationToBibTeX(publication.getId(), configurator);
					final var button3 = this.htmlPageExporter.getButtonToExportPublicationToRIS(publication.getId(), configurator);
					final var button1 = this.htmlPageExporter.getButtonToExportPublicationToOpenDocument(publication.getId(), configurator);
					final var button2 = this.htmlPageExporter.getButtonToExportPublicationToHtml(publication.getId(), configurator);
					final var array0 = mapper.createArrayNode();
					if (!Strings.isNullOrEmpty(button0)) {
						array0.add(mapper.valueToTree(button0));
					}
					if (!Strings.isNullOrEmpty(button3)) {
						array0.add(mapper.valueToTree(button3));
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
					final var editButton = this.htmlPageExporter.getButtonToEditPublication(publication.getId(), locale);
					if (!Strings.isNullOrEmpty(editButton)) {
						final var editNode = mapper.valueToTree(editButton);
						entryNode.set("htmlEdit", editNode); //$NON-NLS-1$
					}
				}
				if (configurator.isDeleteButtons()) {
					final var deleteButton = this.htmlPageExporter.getButtonToDeletePublication(publication.getId(), locale);
					if (!Strings.isNullOrEmpty(deleteButton)) {
						final var deleteNode = mapper.valueToTree(deleteButton);
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
