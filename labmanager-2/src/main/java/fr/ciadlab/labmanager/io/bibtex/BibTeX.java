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

package fr.ciadlab.labmanager.io.bibtex;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.PublicationExporter;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Utilities for BibTeX. BibTeX is reference management software for formatting lists of bibliography references.
 * The BibTeX tool is typically used together with the LaTeX document preparation system.
 * The purpose of BibTeX is to make it easy to cite sources in a consistent manner, by separating bibliographic
 * information from the presentation of this information, similarly to the separation of content and
 * presentation/style supported by LaTeX itself.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 * @see "https://en.wikipedia.org/wiki/BibTeX"
 */
public interface BibTeX extends PublicationExporter<String> {
	
	/** Convert any special macro from a TeX string into its equivalent in the current character encoding.
	 * For example, the macros {@code \'e} is translated to {@code é}.
	 * <p>
	 * The conversion in the opposite direction is supported by {@link #toTeXString(String)}.
	 *
	 * @param texString the TeX data.
	 * @return the Java string that corresponds to the given TeX data.
	 * @throws Exception if the TeX string cannot be parsed.
	 * @see #toTeXString(String)
	 */
	String parseTeXString(String texString) throws Exception;

	/** Extract the publications from a BibTeX source.
	 * This function does not save the publication in the database, as well as the authors.
	 *
	 * @param bibtex the BibTeX data
	 * @return the list of publications that are detected in the BibTeX data.
	 * @throws Exception if the BibTeX source cannot be processed.
	 * @see #extractPublications(String)
	 */
	default List<Publication> extractPublications(String bibtex) throws Exception {
		return getPublicationStreamFrom(bibtex).collect(Collectors.toList());
	}

	/** Extract the publications from a BibTeX source.
	 * This function does not save the publication in the database.
	 *
	 * @param bibtex the BibTeX data
	 * @return the list of publications that are detected in the BibTeX data.
	 * @throws Exception if the BibTeX source cannot be processed.
	 * @see #extractPublications(String)
	 */
	default List<Publication> extractPublications(Reader bibtex) throws Exception {
		return getPublicationStreamFrom(bibtex).collect(Collectors.toList());
	}

	/** Extract the publications from a BibTeX source.
	 * This function does not save the publication in the database.
	 *
	 * @param bibtex the BibTeX data
	 * @return the stream of publications that are detected in the BibTeX data.
	 * @throws Exception if the BibTeX source cannot be processed.
	 * @see #getPublicationStreamFrom(Reader)
	 * @see #extractPublications(Reader)
	 */
	default Stream<Publication> getPublicationStreamFrom(String bibtex) throws Exception {
		if (!Strings.isNullOrEmpty(bibtex)) {
			try (final Reader reader = new StringReader(bibtex)) {
				return getPublicationStreamFrom(reader);
			}
		}
		return Collections.<Publication>emptySet().stream();
	}

	/** Extract the publications from a BibTeX source.
	 * This function does not save the publication in the database.
	 *
	 * @param bibtex the BibTeX data
	 * @return the stream of publications that are detected in the BibTeX data.
	 * @throws Exception if the BibTeX source cannot be processed.
	 * @see #getPublicationStreamFrom(String)
	 * @see #extractPublications(String)
	 */
	Stream<Publication> getPublicationStreamFrom(Reader bibtex) throws Exception;

	@Override
	default String exportPublications(Iterable<? extends Publication> publications, ExporterConfigurator configurator) {
		try (final StringWriter writer = new StringWriter()) {
			exportPublications(writer, publications, configurator);
			return Strings.emptyToNull(writer.toString());
		} catch (IOException ex) {
			return null;
		}
	}

	/** Export the given the publications to a BibTeX source.
	 *
	 * @param output the writer of the BibTeX for saving its content.
	 * @param publications the publications to export.
	 * @param configurator the configuration of the exporter.
	 * @throws IOException if any problem occurred when writing the BibTeX content.
	 */
	void exportPublications(Writer output, Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws IOException;

}
