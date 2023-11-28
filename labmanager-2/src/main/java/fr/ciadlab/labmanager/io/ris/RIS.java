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

package fr.ciadlab.labmanager.io.ris;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.PublicationExporter;
import org.apache.jena.ext.com.google.common.base.Strings;

/** Utilities for RIS.
 * RIS is a standardized tag format developed by Research Information Systems, Incorporated to enable citation programs
 * to exchange data.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.7
 * @see "https://en.wikipedia.org/wiki/RIS_(file_format)"
 */
public interface RIS extends PublicationExporter<String> {
	
//	/** Extract the publications from a BibTeX source.
//	 * This function does not save the publication in the database, as well as the authors.
//	 *
//	 * @param bibtex the BibTeX data
//	 * @param keepBibTeXId indicates if the BibTeX keys should be used as the
//	 *     {@link Publication#getPreferredStringId() preferred string-based ID} of the publication.
//	 *     If this argument is {@code true}, the BibTeX keys are provided to the publication.
//	 *     If this argument is {@code false}, the BibTeX keys are ignored.
//	 * @param assignRandomId indicates if a random identifier will be assigned to the created entities.
//	 *     If this argument is {@code true}, a numeric id will be computed and assign to all the JPA entities.
//	 *     If this argument is {@code false}, the ids of the JPA entities will be the default values, i.e., {@code 0}.
//	 * @param ensureAtLeastOneMember if {@code true}, at least one member of a research organization is required from the
//	 *     the list of the persons. If {@code false}, the list of persons could contain no organization member.
//	 * @param createMissedJournal if {@code true} the missed journals from the JPA database will be automatically the subject
//	 *     of the creation of a {@link JournalFake journal fake} for the caller. If {@code false}, an exception is thown when
//	 *     a journal is missed from the JPA database.
//	 * @param createMissedConference if {@code true} the missed conferences from the JPA database will be automatically the subject
//	 *     of the creation of a {@link ConferenceFake conference fake} for the caller. If {@code false}, an exception is thrown when
//	 *     a conference is missed from the JPA database.
//	 * @return the list of publications that are detected in the BibTeX data.
//	 * @throws Exception if the BibTeX source cannot be processed.
//	 * @see #extractPublications(String)
//	 */
//	default List<Publication> extractPublications(String bibtex, boolean keepBibTeXId, boolean assignRandomId, boolean ensureAtLeastOneMember,
//			boolean createMissedJournal, boolean createMissedConference) throws Exception {
//		return getPublicationStreamFrom(bibtex, keepBibTeXId, assignRandomId, ensureAtLeastOneMember,
//				createMissedJournal, createMissedConference).collect(Collectors.toList());
//	}
//
//	/** Extract the publications from a BibTeX source.
//	 * This function does not save the publication in the database.
//	 *
//	 * @param bibtex the BibTeX data
//	 * @param keepBibTeXId indicates if the BibTeX keys should be used as the
//	 *     {@link Publication#getPreferredStringId() preferred string-based ID} of the publication.
//	 *     If this argument is {@code true}, the BibTeX keys are provided to the publication.
//	 *     If this argument is {@code false}, the BibTeX keys are ignored.
//	 * @param assignRandomId indicates if a random identifier will be assigned to the created entities.
//	 *     If this argument is {@code true}, a numeric id will be computed and assign to all the JPA entities.
//	 *     If this argument is {@code false}, the ids of the JPA entities will be the default values, i.e., {@code 0}.
//	 * @param ensureAtLeastOneMember if {@code true}, at least one member of a research organization is required from the
//	 *     the list of the persons. If {@code false}, the list of persons could contain no organization member.
//	 * @param createMissedJournal if {@code true} the missed journals from the JPA database will be automatically the subject
//	 *     of the creation of a {@link JournalFake journal fake} for the caller. If {@code false}, an exception is thown when
//	 *     a journal is missed from the JPA database.
//	 * @param createMissedConference if {@code true} the missed conferences from the JPA database will be automatically the subject
//	 *     of the creation of a {@link ConferenceFake conference fake} for the caller. If {@code false}, an exception is thrown when
//	 *     a conference is missed from the JPA database.
//	 * @return the list of publications that are detected in the BibTeX data.
//	 * @throws Exception if the BibTeX source cannot be processed.
//	 * @see #extractPublications(String)
//	 */
//	default List<Publication> extractPublications(Reader bibtex, boolean keepBibTeXId, boolean assignRandomId, boolean ensureAtLeastOneMember,
//			boolean createMissedJournal, boolean createMissedConference) throws Exception {
//		return getPublicationStreamFrom(bibtex, keepBibTeXId, assignRandomId, ensureAtLeastOneMember,
//				createMissedJournal, createMissedConference).collect(Collectors.toList());
//	}
//
//	/** Extract the publications from a BibTeX source.
//	 * This function does not save the publication in the database.
//	 *
//	 * @param bibtex the BibTeX data
//	 * @param keepBibTeXId indicates if the BibTeX keys should be used as the
//	 *     {@link Publication#getPreferredStringId() preferred string-based ID} of the publication.
//	 *     If this argument is {@code true}, the BibTeX keys are provided to the publication.
//	 *     If this argument is {@code false}, the BibTeX keys are ignored.
//	 * @param assignRandomId indicates if a random identifier will be assigned to the created entities.
//	 *     If this argument is {@code true}, a numeric id will be computed and assign to all the JPA entities.
//	 *     If this argument is {@code false}, the ids of the JPA entities will be the default values, i.e., {@code 0}.
//	 * @param ensureAtLeastOneMember if {@code true}, at least one member of a research organization is required from the
//	 *     the list of the persons. If {@code false}, the list of persons could contain no organization member.
//	 * @param createMissedJournal if {@code true} the missed journals from the JPA database will be automatically the subject
//	 *     of the creation of a {@link JournalFake journal fake} for the caller. If {@code false}, an exception is thown when
//	 *     a journal is missed from the JPA database.
//	 * @param createMissedConference if {@code true} the missed conferences from the JPA database will be automatically the subject
//	 *     of the creation of a {@link ConferenceFake conference fake} for the caller. If {@code false}, an exception is thrown when
//	 *     a conference is missed from the JPA database.
//	 * @return the stream of publications that are detected in the BibTeX data.
//	 * @throws Exception if the BibTeX source cannot be processed.
//	 * @see #getPublicationStreamFrom(Reader)
//	 * @see #extractPublications(Reader)
//	 */
//	default Stream<Publication> getPublicationStreamFrom(String bibtex, boolean keepBibTeXId, boolean assignRandomId,
//			boolean ensureAtLeastOneMember, boolean createMissedJournal, boolean createMissedConference) throws Exception {
//		if (!Strings.isNullOrEmpty(bibtex)) {
//			try (final Reader reader = new StringReader(bibtex)) {
//				return getPublicationStreamFrom(reader, keepBibTeXId, assignRandomId, ensureAtLeastOneMember, createMissedJournal,
//						createMissedConference);
//			}
//		}
//		return Collections.<Publication>emptySet().stream();
//	}
//
//	/** Extract the publications from a BibTeX source.
//	 * This function does not save the publication in the database.
//	 *
//	 * @param bibtex the BibTeX data
//	 * @param keepBibTeXId indicates if the BibTeX keys should be used as the
//	 *     {@link Publication#getPreferredStringId() preferred string-based ID} of the publication.
//	 *     If this argument is {@code true}, the BibTeX keys are provided to the publication.
//	 *     If this argument is {@code false}, the BibTeX keys are ignored.
//	 * @param assignRandomId indicates if a random identifier will be assigned to the created entities.
//	 *     If this argument is {@code true}, a numeric id will be computed and assign to all the JPA entities.
//	 *     If this argument is {@code false}, the ids of the JPA entities will be the default values, i.e., {@code 0}.
//	 * @param ensureAtLeastOneMember if {@code true}, at least one member of a research organization is required from the
//	 *     the list of the persons. If {@code false}, the list of persons could contain no organization member.
//	 * @param createMissedJournal if {@code true} the missed journals from the JPA database will be automatically the subject
//	 *     of the creation of a {@link JournalFake journal fake} for the caller. If {@code false}, an exception is thrown when
//	 *     a journal is missed from the JPA database.
//	 * @param createMissedConference if {@code true} the missed conferences from the JPA database will be automatically the subject
//	 *     of the creation of a {@link ConferenceFake conference fake} for the caller. If {@code false}, an exception is thrown when
//	 *     a conference is missed from the JPA database.
//	 * @return the stream of publications that are detected in the BibTeX data.
//	 * @throws Exception if the BibTeX source cannot be processed.
//	 * @see #getPublicationStreamFrom(String)
//	 * @see #extractPublications(String)
//	 */
//	Stream<Publication> getPublicationStreamFrom(Reader bibtex, boolean keepBibTeXId, boolean assignRandomId, boolean ensureAtLeastOneMember,
//			boolean createMissedJournal, boolean createMissedConference) throws Exception;

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
