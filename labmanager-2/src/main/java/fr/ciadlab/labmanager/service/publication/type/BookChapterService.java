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
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.entities.publication.PublicationLanguage;
import fr.ciadlab.labmanager.entities.publication.PublicationType;
import fr.ciadlab.labmanager.entities.publication.type.BookChapter;
import fr.ciadlab.labmanager.repository.publication.type.BookChapterRepository;
import fr.ciadlab.labmanager.service.publication.AbstractPublicationTypeService;
import fr.ciadlab.labmanager.utils.files.DownloadableFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service for managing book chapters.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Service
public class BookChapterService extends AbstractPublicationTypeService {

	private BookChapterRepository repository;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param downloadableFileManager downloadable file manager.
	 * @param repository the repository for this service.
	 */
	public BookChapterService(@Autowired DownloadableFileManager downloadableFileManager,
			@Autowired BookChapterRepository repository) {
		super(downloadableFileManager);
		this.repository = repository;
	}

	/** Replies all the book chapters.
	 *
	 * @return the book chapters.
	 */
	public List<BookChapter> getAllBookChapters() {
		return this.repository.findAll();
	}

	/** Replies the book chapter with the given identifier.
	 *
	 * @param identifier the identifier of the book chapter.
	 * @return the book chapter, or {@code null}.
	 */
	public BookChapter getBookChapter(int identifier) {
		final Optional<BookChapter> res = this.repository.findById(Integer.valueOf(identifier));
		return res.orElse(null);
	}

	/** Create a book chapter.
	 *
	 * @param publication the publication to copy.
	 * @param bookTitle the title of the book in which the chapter is.
	 * @param chapterNumber the number of the chapter in the book.
	 * @param edition the edition number of the book.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param publisher the name of the publisher of the book.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @return the created book chapter.
	 */
	public BookChapter createBookChapter(Publication publication, String bookTitle, String chapterNumber, String edition,
			String volume, String number, String pages, String editors, String series,
			String publisher, String address) {
		return createBookChapter(publication, bookTitle, chapterNumber, edition, volume, number, pages,
				editors, series, publisher, address, true);
	}

	/** Create a book chapter.
	 *
	 * @param publication the publication to copy.
	 * @param bookTitle the title of the book in which the chapter is.
	 * @param chapterNumber the number of the chapter in the book.
	 * @param edition the edition number of the book.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param publisher the name of the publisher of the book.
	 * @param address the geographical location of the event, usually a city and a country.
	 * @param saveInDb {@code true} for saving the publication in the database.
	 * @return the created book chapter.
	 */
	public BookChapter createBookChapter(Publication publication, String bookTitle, String chapterNumber, String edition,
			String volume, String number, String pages, String editors, String series,
			String publisher, String address, boolean saveInDb) {
		final BookChapter res = new BookChapter(publication, volume, number, pages, editors,
				address, series, publisher, edition, bookTitle, chapterNumber);
		if (saveInDb) {
			this.repository.save(res);
		}
		return res;
	}

	/** Update the book chapter with the given identifier.
	 *
	 * @param pubId identifier of the chapter to change.
	 * @param title the new title of the publication, never {@code null} or empty.
	 * @param type the new type of publication, never {@code null}.
	 * @param date the new date of publication, never {@code null}.
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
	 * @param bookTitle the title of the book in which the chapter is.
	 * @param chapterNumber the number of the chapter in the book.
	 * @param edition the edition number of the book.
	 * @param volume the volume of the journal.
	 * @param number the number of the journal.
	 * @param pages the pages in the journal.
	 * @param editors the list of the names of the editors. Each name may have the format {@code LAST, VON, FIRST} and the names may be separated
	 *     with {@code AND}.
	 * @param series the number or the name of the series for the conference proceedings.
	 * @param publisher the name of the publisher of the book.
	 * @param address the geographical location of the event, usually a city and a country.
	 */
	public void updateBookChapter(int pubId, 
			String title, PublicationType type, LocalDate date, String abstractText, String keywords,
			String doi, String isbn, String issn, String dblpUrl, String extraUrl,
			PublicationLanguage language, String pdfContent, String awardContent, String pathToVideo,
			String bookTitle, String chapterNumber, String edition,
			String volume, String number, String pages, String editors, String series,
			String publisher, String address) {
		final Optional<BookChapter> res = this.repository.findById(Integer.valueOf(pubId));
		if (res.isPresent()) {
			final BookChapter chapter = res.get();

			updatePublicationNoSave(chapter, title, type, date,
					abstractText, keywords, doi, isbn, issn, dblpUrl,
					extraUrl, language, pdfContent, awardContent,
					pathToVideo);

			chapter.setBookTitle(Strings.emptyToNull(bookTitle));
			chapter.setChapterNumber(Strings.emptyToNull(chapterNumber));
			chapter.setEdition(Strings.emptyToNull(edition));
			chapter.setVolume(Strings.emptyToNull(volume));
			chapter.setNumber(Strings.emptyToNull(number));
			chapter.setPages(Strings.emptyToNull(pages));
			chapter.setEditors(Strings.emptyToNull(editors));
			chapter.setSeries(Strings.emptyToNull(series));
			chapter.setPublisher(Strings.emptyToNull(publisher));
			chapter.setAddress(Strings.emptyToNull(address));

			this.repository.save(res.get());
		}
	}

	/** Remove the book chapter with the given identifier.
	 *
	 * @param identifier the identifier if the chapter to remove.
	 */
	public void removeBookChapter(int identifier) {
		this.repository.deleteById(Integer.valueOf(identifier));
	}

}
