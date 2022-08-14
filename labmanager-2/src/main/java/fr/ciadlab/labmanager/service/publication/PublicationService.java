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

package fr.ciadlab.labmanager.service.publication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.entities.publication.Authorship;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.io.ExporterConfigurator;
import fr.ciadlab.labmanager.io.bibtex.BibTeX;
import fr.ciadlab.labmanager.io.html.HtmlDocumentExporter;
import fr.ciadlab.labmanager.io.od.OpenDocumentTextExporter;
import fr.ciadlab.labmanager.repository.journal.JournalRepository;
import fr.ciadlab.labmanager.repository.member.PersonRepository;
import fr.ciadlab.labmanager.repository.publication.AuthorshipRepository;
import fr.ciadlab.labmanager.repository.publication.PublicationRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import fr.ciadlab.labmanager.service.member.PersonService;
import org.apache.jena.ext.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service for managing the publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class PublicationService extends AbstractService {

	private PublicationRepository publicationRepository;

	private AuthorshipService authorshipService;

	private AuthorshipRepository authorshipRepository;

	private JournalRepository journalRepository;

	private PersonRepository personRepository;

	private PersonService personService;

	private BibTeX bibtex;

	private HtmlDocumentExporter html;

	private OpenDocumentTextExporter odt;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param publicationRepository the publication repository.
	 * @param authorshipService the service for managing the authorships.
	 * @param authorshipRepository authorshipRepository the repository of authorships.
	 * @param personService the service for managing the persons.
	 * @param personRepository the repository of the persons.
	 * @param journalRepository the repository of the journals.
	 * @param bibtex the tool for managing BibTeX source.
	 * @param html the tool for exporting to HTML.
	 * @param odt the tool for exporting to Open Document Text.
	 */
	public PublicationService(@Autowired PublicationRepository publicationRepository,
			@Autowired AuthorshipService authorshipService,
			@Autowired AuthorshipRepository authorshipRepository,
			@Autowired PersonService personService, @Autowired PersonRepository personRepository,
			@Autowired JournalRepository journalRepository,
			@Autowired BibTeX bibtex, @Autowired HtmlDocumentExporter html,
			@Autowired OpenDocumentTextExporter odt) {
		this.publicationRepository = publicationRepository;
		this.authorshipService = authorshipService;
		this.authorshipRepository = authorshipRepository;
		this.personRepository = personRepository;
		this.personService = personService;
		this.journalRepository = journalRepository;
		this.bibtex = bibtex;
		this.html = html;
		this.odt = odt;
	}

	/** Replies all the publications from the database.
	 *
	 * @return the publications.
	 */
	public List<Publication> getAllPublications() {
		return this.publicationRepository.findAll();
	}

	/** Replies all the publications from the database that are attached to the given person.
	 *
	 * @param identifier the identifier of the person.
	 * @return the publications.
	 */
	public List<Publication> getPublicationsByPersonId(int identifier) {
		return this.publicationRepository.findAllByAuthorshipsPersonId(identifier);
	}

	/** Replies all the publications from the database that are attached to the given organization.
	 *
	 * @param identifier the identifier of the organization.
	 * @return the publications.
	 */
	public List<Publication> getPublicationsByOrganizationId(int identifier) {
		return this.publicationRepository.findAllByAuthorshipsPersonResearchOrganizationsResearchOrganizationId(identifier);
	}

	/** Replies the publication with the given identifier.
	 *
	 * @param identifier the identifier of the publication.
	 * @return the publication, or {@code null} if not found.
	 */
	public Publication getPublicationById(int identifier) {
		final Optional<Publication> byId = this.publicationRepository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Replies the publications with the given identifiers.
	 *
	 * @param identifiers the identifiers of the publications.
	 * @return the publications.
	 */
	public List<Publication> getPublicationsByIds(List<Integer> identifiers) {
		return this.publicationRepository.findAllById(identifiers);
	}

	/** Replies the publications with the given title.
	 *
	 * @param title the title of the publications.
	 * @return the publications.
	 */
	public List<Publication> getPublicationsByTitle(String title) {
		if (Strings.isNullOrEmpty(title)) {
			return Collections.emptyList();
		}
		return this.publicationRepository.findAllByTitle(title);
	}

	/** Remove the publication with the given identifier.
	 *
	 * @param identifier the identifier of the publication to remove.
	 */
	public void removePublication(int identifier) {
		final Integer id = Integer.valueOf(identifier);
		final Optional<Publication> res = this.publicationRepository.findById(id);
		if (res.isPresent()) {
			final Publication publication = res.get();
			this.publicationRepository.save(publication);
			this.publicationRepository.deleteById(id);
		}
	}

	/** Save the given publications into the database.
	 * If one publication has a temporary list of authors, the corresponding authors are
	 * explicitly created into the database.
	 *
	 * @param publications the list of publications to save in the database.
	 */
	public void save(Publication... publications) {
		save(Arrays.asList(publications));
	}

	/** Save the given publications into the database.
	 * If one publication has a temporary list of authors, the corresponding authors are
	 * explicitly created into the database.
	 *
	 * @param publications the list of publications to save in the database.
	 */
	public void save(List<? extends Publication> publications) {
		for (final Publication publication : publications) {
			final List<Person> authors = publication.getTemporaryAuthors();
			publication.setTemporaryAuthors(null);
			this.publicationRepository.save(publication);
			if (publication instanceof JournalBasedPublication) {
				this.journalRepository.save(((JournalBasedPublication) publication).getJournal());
			}
			if (authors != null) {
				for (final Person author : authors) {
					this.personRepository.save(author);
					this.authorshipService.addAuthorship(author.getId(), publication.getId());
				}
			}
		}
	}

	/** Import publications from a BibTeX string. The format of the BibTeX is a standard that is briefly described
	 * on {@link "https://en.wikipedia.org/wiki/BibTeX"}.
	 * If multiple BibTeX entries are defined into the given input string, each of them is subject
	 * of an importation tentative. If the import process is successful, the database identifier of the publication
	 * is replied.
	 *
	 * @param bibtex the string that contains the BibTeX description of the publications.
	 * @return the list of the identifiers of the publications that are successfully imported.
	 * @throws Exception if it is impossible to parse the given BibTeX source.
	 * @see BibTeX
	 * @see "https://en.wikipedia.org/wiki/BibTeX"
	 */
	public List<Integer> importPublications(String bibtex) throws Exception {
		// Holds the publications that we are trying to import.
		// The publications are not yet imported into the database.
		final List<Publication> importablePublications = this.bibtex.extractPublications(bibtex);

		//Holds the IDs of the successfully imported IDs. We'll need it for type differenciation later.
		final List<Integer> importedPublicationIdentifiers = new ArrayList<>();

		//We are going to try to import every publication in the list
		for (final Publication publication : importablePublications) {
			try {
				// Add the publication to the database and get the new assigned identifier
				this.publicationRepository.save(publication);
				final int publicationId = publication.getId();
				final Integer publicationIdObj = Integer.valueOf(publicationId);

				// Adding the id of the current publication to the list
				importedPublicationIdentifiers.add(publicationIdObj);

				// For every authors assigned to this publication, save them into the database
				final List<Person> authors = publication.getAuthors();
				publication.setTemporaryAuthors(null);
				for (final Person author : authors) {
					try {
						// Search for a person with a "similar name"
						int personId = this.personService.getPersonIdBySimilarName(
								author.getFirstName(), author.getLastName());
						// Create new author if is not inside the database.
						// If we've already got the author with the abbreviated first name in DB, 
						// but the one parsed have the full version, it creates a new author
						if (personId == 0) {
							this.personRepository.save(author);
							personId = author.getId();
						}
						// Assigning authorship
						this.authorshipService.addAuthorship(personId, publicationId);
						this.publicationRepository.save(publication);

						// Check if the newly imported pub has at least one authorship.
						// If not, it's a bad case and the pub have to be removed and marked as failed
						if (this.authorshipService.getAuthorsFor(publicationId).isEmpty()) {
							throw new IllegalArgumentException("No author for publication id=" + publicationId); //$NON-NLS-1$
						}
					} catch (Exception ex) {
						// Even if a larger try catch for exceptions exists, we need to delete
						// first the imported publication and linked authorship
						importedPublicationIdentifiers.remove(publicationIdObj);
						for (final Authorship toRemove : this.authorshipRepository.findByPublicationId(publicationId)) {
							this.authorshipRepository.deleteById(Integer.valueOf(toRemove.getId()));
						}
						this.publicationRepository.deleteById(publicationIdObj);
						throw ex;
					}
				}	
			} catch(Exception ex) {
				getLogger().error("Error while importing Bibtext publication\nData :\n" + publication + "\nException :", ex); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return importedPublicationIdentifiers;
	}

	/**
	 * Export function for BibTeX using a list of publication identifiers.
	 *
	/**
	 * Export function for HTML using a list of publication identifiers.
	 *
	 * @param configurator the configurator of the exporter.
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @return the HTML description of the publications with the given identifiers.
	 * @throws Exception if it is impossible to generate the HTML for the publications.
	 */
	public String exportHtml(ExporterConfigurator configurator, int... identifiers) throws Exception {
		if (identifiers == null) {
			return null;
		}
		return exportHtml(Arrays.stream(identifiers).mapToObj(it -> Integer.valueOf(it)), configurator);
	}

	/**
	 * Export function for HTML using a list of publication identifiers.
	 *
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @param configurator the configurator of the exporter.
	 * @return the HTML description of the publications with the given identifiers.
	 * @throws Exception if it is impossible to generate the HTML for the publications.
	 */
	public String exportHtml(Collection<Integer> identifiers, ExporterConfigurator configurator) throws Exception {
		if (identifiers == null) {
			return null;
		}
		return exportHtml(identifiers.stream(), configurator);
	}

	/**
	 * Export function for HTML using a list of publication identifiers.
	 *
	 * @param identifiers the array of publication identifiers that should be exported.
	 * @param configurator the configurator of the exporter.
	 * @return the HTML description of the publications with the given identifiers, or {@code null}
	 *      if there is no publication to export.
	 * @throws Exception if it is impossible to generate the HTML for the publications.
	 */
	public String exportHtml(Stream<Integer> identifiers, ExporterConfigurator configurator) throws Exception {
		if (identifiers == null) {
			return null;
		}
		final List<Publication> publications = new ArrayList<>();
		identifiers.forEach(it -> {
			if (it != null) {
				final Optional<Publication> optPublication = this.publicationRepository.findById(it);
				if (optPublication.isPresent()) {
					publications.add(optPublication.get());
				}
			}
		});
		return this.html.exportPublications(publications, configurator);
	}

	/**
	 * Export function for Open Document Text using a list of publication identifiers.
	 *
	 * @param publications the array of publications that should be exported.
	 * @param configurator the configurator of the exporter.
	 * @return the ODT description of the publications with the given identifiers.
	 * @throws Exception if it is impossible to generate the ODT for the publications.
	 */
	public byte[] exportOdt(Iterable<? extends Publication> publications, ExporterConfigurator configurator) throws Exception {
		if (publications == null) {
			return null;
		}
		return this.odt.exportPublications(publications, configurator);
	}

}
