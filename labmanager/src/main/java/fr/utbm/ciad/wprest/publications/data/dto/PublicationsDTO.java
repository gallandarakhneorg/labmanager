package fr.utbm.ciad.wprest.publications.data.dto;

import fr.utbm.ciad.labmanager.data.publication.PublicationLanguage;
import fr.utbm.ciad.labmanager.data.publication.PublicationType;
import fr.utbm.ciad.wprest.data.PersonOnWebsite;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a publication's information.
 *
 * @param title           the title of the publication
 * @param doi             the doi of the publication
 * @param issn            the issn of the publication
 * @param publicationDate the date the publication was released
 * @param publicationType the type of the publication (e.g., journal article, book, etc.)
 * @param persons         a list of names of persons associated with the publication
 * @param abstractText    a brief summary of the publication's content
 * @param pdfUrl          the URL link to the PDF version of the publication
 * @param language        the language in which the publication is written
 * @param keywords        a list of keywords related to the publication
 */
public record PublicationsDTO(String title,
                              String doi,
                              String issn,
                              LocalDate publicationDate,
                              PublicationType publicationType,
                              List<PersonOnWebsite> persons,
                              String abstractText,
                              String pdfUrl,
                              PublicationLanguage language,
                              List<String> keywords) {
}
