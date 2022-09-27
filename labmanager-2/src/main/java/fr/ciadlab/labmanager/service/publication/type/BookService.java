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

package fr.ciadlab.labmanager.service.publication.type;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;
import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.Book;
import fr.ciadlab.labmanager.io.filemanager.DownloadableFileManager;
import fr.ciadlab.labmanager.repository.publication.type.BookRepository;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service for managing books.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class BookService extends AbstractPublicationTypeService {

	private BookRepository repository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param downloadableFileManager downloadable file manager.
	 * @param repository the repository for this service.
	 */
	public BookService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired BookRepository repository) {
		super(messages, constants, downloadableFileManager);
		this.repository = repository;
	}

	/** Replies all the books.
	 *
	 * @return the books.
	 */
	public List<Book> getAllBooks() {
		return this.repository.findAll();
	}

	/** Replies the book with the given identifier.
	 *
	 * @param identifier the identifier of the book.
	 * @return the book or {@code null}.
	 */
	public Book getBook(int identifier) {
		final Optional<Book> byId = this.repository.findById(Integer.valueOf(identifier));
		return byId.orElse(null);
	}

	/** Create a book.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param edition the edition number of the book.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param publisher the name of the publisher of the book.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @return the created book chapter.
	 */
	public Book createBook(Publication publication, String volume, String number, String pages, String edition, String editors,
			String series, String publisher, String address) {
		return createBook(publication, volume, number, pages, edition, editors, series, publisher, address, true);
	}

	/** Create a book.
	 *
	 * @param publication the publication to copy.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param edition the edition number of the book.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param publisher the name of the publisher of the book.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @param saveInDb {@code true} for saving the publication in the database.
	 * @return the created book chapter.
	 */
	public Book createBook(Publication publication, String volume, String number, String pages, String edition, String editors,
			String series, String publisher, String address, boolean saveInDb) {
		final Book res = new Book(publication, volume, number, pages, editors, address, series, publisher, edition);
		if (saveInDb) {
			this.repository.save(res);
		}
		return res;
	}

	/** Update the book with the given identifier.
	 * <p>This function does not check the associated between the given publication type and the book class. 
	 *
	 * @param pubId identifier of the book to change.
	 * @param title the new title of the publication, never {@code null} or empty.
	 * @param type the new type of publication, never {@code null}.
	 * @param date the new date of publication. It may be {@code null}. In this case only the year should be considered.
	 * @param year the new year of the publication. 
	 * @param abstractText the new text of the abstract.
	 * @param keywords the new list of keywords.
	 * @param doi the new DOI number.
	 * @param isbn the new ISBN number.
	 * @param issn the new ISSN number.
	 * @param dblpUrl the new URL to the DBLP page of the publication.
	 * @param extraUrl the new URL to the page of the publication.
	 * @param language the new major language of the publication.
	 * @param pdfContent the content of the publication PDF that is encoded in {@link Base64}. The content will be saved into
	 *     the dedicated folder for PDF files.
	 * @param awardContent the content of the publication award certificate that is encoded in {@link Base64}. The content will be saved into
	 *     the dedicated folder for PDF files.
	 * @param pathToVideo the path that allows to download the video of the publication.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param edition the edition number of the book.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param publisher the name of the publisher of the book.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @return the updated book.
	 */
	public Optional<Book> updateBook(int pubId,
			String title, PublicationType type, LocalDate date, int year, String abstractText, String keywords,
			String doi, String isbn, String issn, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String volume, String number, String pages, String edition, String editors,
			String series, String publisher, String address) {
		final Optional<Book> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final Book book = res.get();

			updatePublicationNoSave(book, title, type, date, year,
					abstractText, keywords, doi, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			book.setVolume(Strings.emptyToNull(volume));
			book.setNumber(Strings.emptyToNull(number));
			book.setPages(Strings.emptyToNull(pages));
			book.setEdition(Strings.emptyToNull(edition));
			book.setEditors(Strings.emptyToNull(editors));
			book.setSeries(Strings.emptyToNull(series));
			book.setPublisher(Strings.emptyToNull(publisher));
			book.setAddress(Strings.emptyToNull(address));
			
			this.repository.save(res.get());
		}
		return res;
	}

	/** Remove the book from the database.
	 *
	 * @param identifier the identifier of the book to be removed.
	 */
	public void removeBook(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}
