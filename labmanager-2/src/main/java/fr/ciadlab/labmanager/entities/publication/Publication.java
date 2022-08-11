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

package fr.ciadlab.labmanager.entities.publication;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.transaction.Transactional;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.EntityUtils;
import fr.ciadlab.labmanager.entities.member.Person;
import fr.ciadlab.labmanager.utils.AttributeProvider;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import fr.ciadlab.labmanager.utils.JsonExportable;
import fr.ciadlab.labmanager.utils.JsonUtils;

/** Abstract representation of a research publication.
 * This class contains the fields that are usually shared between many of the different types of research publications.
 * These types of research publications are specifically defined in subtypes.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Publications")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Publication implements Serializable, Comparable<Publication>, JsonExportable, AttributeProvider {

	private static final long serialVersionUID = -5980560007123809890L;

	/** Identifier of the publication.
	 * The generated value type is set to {@link GenerationType#AUTO} instead of {@link GenerationType#IDENTITY}
	 * because it allows to use an inheritance strategy of type {@link InheritanceType.JOINED} and {@link InheritanceType.TABLE_PER_CLASS}.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private int id;

	/** Type of the publication. This type of publication may enable/disable several fields of the publication (see sub types of {@link Publications}).
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private PublicationType type;

	/** Title of a publication.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String title;

	/** Text that is the abstract of the publication.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	@Lob
	private String abstractText;

	/** List of keywords. The separator of the keywords may be a coma ({@code ,}) or a column (@code ;}).
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	@Lob
	private String keywords;

	/** Date of publication. Only the year is really mandatory and significant. The day and month are usually ignored.
	 * 
	 * @see #publicationYear
	 */
	@Column
	private Date publicationDate;

	/** Year of publication.
	 */
	@Column
	private int publicationYear = getDefaultPublicationYear();

	/** ISBN number if the publication has one.
	 */
	@Column
	private String isbn;

	/** ISSN number if the publication has one.
	 */
	@Column
	private String issn;

	/** DOI identifier if the publication has one.
	 */
	@Column
	private String doi;

	/** identifier on HAL if the publication has one.
	 */
	@Column
	private String halId;

	/** Extra URL if the publication has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String extraUrl;

	/** URL of a associated video if the publication has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String videoUrl;

	/** URL of the publication page on the DBLP if the publication has one.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String dblpUrl;

	/** URL of a associated video if the publication has one. It is preferred to have a local URL, i.e. without the host name.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToDownloadablePDF;

	/** URL of a associated award certificate if the publication has one. It is preferred to have a local URL, i.e. without the host name.
	 */
	@Column(length = EntityUtils.LARGE_TEXT_SIZE)
	private String pathToDownloadableAwardCertificate;

	/** The major language used for writing the publication. The default language depends on the definition in {@link PublicationLanguage}.
	 */
	@Column
	@Enumerated(EnumType.STRING)
	private PublicationLanguage majorLanguage = PublicationLanguage.getDefault();

	/** Authorships specify the authors of the publication and their position in the list of authors.
	 */
	@OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Authorship> authorships = new HashSet<>();

	@Transient
	private List<Person> temporaryAuthors = null;

	/** Constructor by copy.
	 *
	 * @param publication is the publication to copy.
	 */
	protected Publication(Publication publication) {
		assert publication != null;
		this.id = publication.getId();
		this.authorships.addAll(publication.getAuthorships());
		this.title = publication.getTitle();
		this.abstractText = publication.getAbstractText();
		this.keywords = publication.getKeywords();
		this.publicationDate = publication.getPublicationDate();
		this.publicationYear = publication.getPublicationYear();
		this.isbn = publication.getISBN();
		this.issn = publication.getISSN();
		this.doi = publication.getDOI();
		this.extraUrl = publication.getExtraURL();
		this.videoUrl = publication.getVideoURL();
		this.dblpUrl = publication.getDblpURL();
		this.pathToDownloadablePDF = publication.getPathToDownloadablePDF();
		this.majorLanguage = publication.getMajorLanguage();
		this.pathToDownloadableAwardCertificate = publication.getPathToDownloadableAwardCertificate();
		this.type = publication.getType();
	}

	/** Create a publication with the given field values.
	 *
	 * @param type the type of the publication. It cannot be {@code null}.
	 * @param title the title of the publication.
	 * @param abstractText the text of the abstract for the publication.
	 * @param keywords the keywords, seperated by coma or column characters 
	 * @param date the date of publication.
	 * @param isbn the ISBN number if any.
	 * @param issn the ISSN number if any.
	 * @param doi the DOI reference number if any.
	 * @param halId the HAL reference number if any.
	 * @param extraUrl an URL to a page associated to the publication.
	 * @param videoUrl an URL to a video associated to the publication.
	 * @param dblpUrl the URL to the DBLP page of the publication if any.
	 * @param pdfPath the path (may be an URL, but preferably a simple path) to a downloadable PDF file for the publication.
	 * @param awardPath the path (may be an URL, but preferably a simple path) to a downloadable PDF file that is a award certificate associated to the publication.
	 * @param language the major language used for writing the publication. It cannot be {@code null}.
	 */
	public Publication(PublicationType type, String title, String abstractText, String keywords,
			Date date, String isbn, String issn,
			String doi, String halId, String extraUrl, String videoUrl, String dblpUrl, String pdfPath,
			String awardPath, PublicationLanguage language) {
		assert type != null;
		assert language != null;
		this.type = type;
		this.title = title;
		this.abstractText = abstractText;
		this.keywords = keywords;
		this.publicationDate = date;
		this.isbn = isbn;
		this.issn = issn;
		this.doi = doi;
		this.halId = halId;
		this.extraUrl = extraUrl;
		this.videoUrl = videoUrl;
		this.dblpUrl = dblpUrl;
		this.pathToDownloadablePDF = pdfPath;
		this.pathToDownloadableAwardCertificate = awardPath;
		this.majorLanguage = language;
		resetPublicationYear();
	}

	/** Default constructor that generate a publication with "empty" fields.
	 */
	protected Publication() {
		//
	}

	@Override
	public String toString() {
		return "{" + getTitle() + "}:" + this.id; //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public int hashCode() {
		int h = HashCodeUtils.start();
		h = HashCodeUtils.add(h, this.id);
		h = HashCodeUtils.add(h, this.type);
		h = HashCodeUtils.add(h, this.title);
		h = HashCodeUtils.add(h, this.abstractText);
		h = HashCodeUtils.add(h, this.keywords);
		h = HashCodeUtils.add(h, this.publicationDate);
		h = HashCodeUtils.add(h, this.publicationYear);
		h = HashCodeUtils.add(h, this.doi);
		h = HashCodeUtils.add(h, this.halId);
		h = HashCodeUtils.add(h, this.dblpUrl);
		h = HashCodeUtils.add(h, this.isbn);
		h = HashCodeUtils.add(h, this.issn);
		h = HashCodeUtils.add(h, this.extraUrl);
		h = HashCodeUtils.add(h, this.pathToDownloadablePDF);
		h = HashCodeUtils.add(h, this.pathToDownloadableAwardCertificate);
		h = HashCodeUtils.add(h, this.majorLanguage);
		// result = prime * result + ((pubAuts == null) ? 0 : pubAuts.hashCode());
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Publication other = (Publication) obj;
		if (this.id != other.id) {
			return false;
		}
		if (!Objects.equals(this.type, other.type)) {
			return false;
		}
		if (!Objects.equals(this.title, other.title)) {
			return false;
		}
		if (!Objects.equals(this.abstractText, other.abstractText)) {
			return false;
		}
		if (!Objects.equals(this.keywords, other.keywords)) {
			return false;
		}
		if (!Objects.equals(this.publicationDate, other.publicationDate)) {
			return false;
		}
		if (this.publicationYear != other.publicationYear) {
			return false;
		}
		if (!Objects.equals(this.doi, other.doi)) {
			return false;
		}
		if (!Objects.equals(this.halId, other.halId)) {
			return false;
		}
		if (!Objects.equals(this.dblpUrl, other.dblpUrl)) {
			return false;
		}
		if (!Objects.equals(this.isbn, other.isbn)) {
			return false;
		}
		if (!Objects.equals(this.issn, other.issn)) {
			return false;
		}
		if (!Objects.equals(this.extraUrl, other.extraUrl)) {
			return false;
		}
		if (!Objects.equals(this.pathToDownloadablePDF, other.pathToDownloadablePDF)) {
			return false;
		}
		if (!Objects.equals(this.pathToDownloadableAwardCertificate, other.pathToDownloadableAwardCertificate)) {
			return false;
		}
		if (!Objects.equals(this.majorLanguage, other.majorLanguage)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Publication o) {
		return EntityUtils.getPreferredPublicationComparator().compare(this, o);
	}

	/** {@inheritDoc}
	 * <p>The attributes that are not considered by this function are:<ul>
	 * <li>{@code id}</li>
	 * <li>{@code type}</li>
	 * <li>{@code authorships}</li>
	 * <li>{@code authors}</li>
	 * <li>{@code publicationYear}</li>
	 * </ul>
	 */
	@Override
	public void forEachAttribute(BiConsumer<String, Object> consumer) {
		assert consumer != null : "How to consume an attribute if the consumer is null?"; //$NON-NLS-1$
		if (!Strings.isNullOrEmpty(getTitle())) {
			consumer.accept("title", getTitle()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAbstractText())) {
			consumer.accept("abstractText", getAbstractText()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getKeywords())) {
			consumer.accept("keywords", getKeywords()); //$NON-NLS-1$
		}
		final Date dt = getPublicationDate();
		if (dt != null) {
			consumer.accept("publicationDate", dt.toString()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDOI())) {
			consumer.accept("doi", getDOI()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getHalId())) {
			consumer.accept("halId", getHalId()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getDblpURL())) {
			consumer.accept("dblpURL", getDblpURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISBN())) {
			consumer.accept("isbn", getISBN()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getISSN())) {
			consumer.accept("issn", getISSN()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getExtraURL())) {
			consumer.accept("extraURL", getExtraURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getVideoURL())) {
			consumer.accept("videoURL", getVideoURL()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPathToDownloadablePDF())) {
			consumer.accept("pathToDownloadablePDF", getPathToDownloadablePDF()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getPathToDownloadableAwardCertificate())) {
			consumer.accept("pathToDownloadableAwardCertificate", getPathToDownloadableAwardCertificate()); //$NON-NLS-1$
		}
		if (getMajorLanguage() != null) {
			consumer.accept("majorLanguage", getMajorLanguage()); //$NON-NLS-1$
		}
	}

	@Override
	public final void toJson(JsonObject json) {
		json.addProperty("id", Integer.valueOf(getId())); //$NON-NLS-1$
		if (getType() != null) {
			json.addProperty("type", getType().name()); //$NON-NLS-1$
		}
		final JsonArray authors = new JsonArray();
		for (final Person author : getAuthors()) {
			final JsonObject personJson = new JsonObject();
			author.toJson(personJson);
			if (personJson.size() > 0) {
				authors.add(personJson);
			}
		}
		if (authors.size() > 0) {
			json.add("authors", authors); //$NON-NLS-1$
		}
		json.addProperty("publicationYear", Integer.valueOf(getPublicationYear())); //$NON-NLS-1$
		//
		forEachAttribute((name, value) -> JsonUtils.defaultBehavior(json, name, value));
	}

	/** Replies the set of authorships.
	 *
	 * @return the authoships.
	 */
	public List<Authorship> getAuthorships() {
		return this.authorships.stream().sorted(AuthorshipComparator.DEFAULT).collect(Collectors.toList());
	}

	/** Replies the ordered list of authors.
	 *
	 * @return the list of authors.
	 * @see #getTemporaryAuthors()
	 */
	public List<Person> getAuthors() {
		if (this.temporaryAuthors != null) {
			return this.temporaryAuthors;
		}
		return getAuthorships().stream().map(it -> it.getPerson()).collect(Collectors.toList());
	}

	/** Change the ordered list of authors that is stored into a temporary memory space.
	 *
	 * @param authors the list of authors.
	 */
	public void setTemporaryAuthors(List<Person> authors) {
		this.temporaryAuthors = authors;
	}

	/** Replies the ordered list of authors that is stored into a temporary memory space.
	 *
	 * @return the list of authors.
	 * @see #getAuthors()
	 */
	public List<Person> getTemporaryAuthors() {
		return this.temporaryAuthors;
	}

	/** Add the given authorship.
	 *
	 * @param authorship the authorship to delete.
	 */
	@Transactional
	public void addAuthorship(Authorship authorship) {
		this.authorships.add(authorship);
	}

	/** Delete the given authorship.
	 *
	 * @param authorship the authorship to delete.
	 */
	@Transactional
	public void deleteAuthorship(Authorship authorship) {
		this.authorships.remove(authorship);
	}

	/** Replies the identifier of the publication. This identifier is useful only from the database point of view. Do not use this
	 * identifier in your Java code.
	 *
	 * @return the identifier
	 */
	public int getId() {
		return this.id;
	}

	/** Change the identifier of the publication. This identifier is useful only from the database point of view. Do not use this
	 * identifier in your Java code.
	 *
	 * @param id the new identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** Replies the preferred value for a string-based ID for this publication.
	 *
	 * @return the identifier
	 */
	public String getPreferredStringId() {
		final StringBuilder b = new StringBuilder();
		b.append(getClass().getSimpleName());
		b.append('_');
		b.append(getId());
		return b.toString();
	}

	/** Replies the type of publication.
	 *
	 * @return the type, never {@code null}.
	 */
	public PublicationType getType() {
		return this.type;
	}

	/** Change the type of publication.
	 *
	 * @param type the type, never {@code null}.
	 */
	public void setType(PublicationType type) {
		this.type = type;
	}

	/** Change the type of publication.
	 *
	 * @param type the type.
	 */
	public final void setType(String type) {
		if (Strings.isNullOrEmpty(type)) {
			setType((PublicationType) null);
		} else {
			setType(PublicationType.valueOfCaseInsensitive(type));
		}
	}

	/** Replies the category of publication.
	 *
	 * @return the category, never {@code null}.
	 */
	public PublicationCategory getCategory() {
		final PublicationType type = getType();
		if (type == null) {
			return null;
		}
		final Set<PublicationCategory> categories = type.getCategories();
		final int size = categories.size();
		if (size <= 0) {
			return PublicationCategory.AP;
		} else if (size == 1) {
			return categories.iterator().next();
		}
		// Multiple categories are available. The selection is done on ranking.
		return type.getCategory(isRanked());
	}

	/** Replies if the publication is published into a ranked support (journal or conference).
	 *
	 * @return {@code true} if the publication is in a ranked support.
	 */
	public abstract boolean isRanked();

	/** Replies the title of the publication.
	 *
	 * @return the title.
	 */
	public String getTitle() {
		return this.title;
	}

	/** Change the title of the publication.
	 *
	 * @param title the title.
	 */
	public void setTitle(String title) {
		this.title = Strings.emptyToNull(title);
	}

	/** Replies the text that is an abstract/summary of the publication.
	 *
	 * @return the text.
	 */
	public String getAbstractText() {
		return this.abstractText;
	}

	/** Change the text that is an abstract/summary of the publication.
	 *
	 * @param text the text.
	 */
	public void setAbstractText(String text) {
		this.abstractText = Strings.emptyToNull(text);
	}

	/** Replies a list of keywords that describe the contribution of the research publication.
	 * The keywords are usually separated by coma or column characters.
	 *
	 * @return the list of keywords.
	 */
	public String getKeywords() {
		return this.keywords;
	}

	/** Replies a list of keywords that describe the contribution of the research publication.
	 * The keywords are usually separated by coma or column characters.
	 *
	 * @param keywords the list of keywords.
	 */
	public void setKeywords(String keywords) {
		this.keywords = Strings.emptyToNull(keywords);
	}

	/** Replies the date of publication.
	 *
	 * @return the date, or {@code null}.
	 */
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	/** Change the date of publication. This function changes also the field that contains the publication year. 
	 *
	 * @param date the date, or {@code null}.
	 */
	public void setPublicationDate(Date date) {
		this.publicationDate = date;
		resetPublicationYear();
	}

	/** Change the date of publication. This function changes also the field that contains the publication year. 
	 *
	 * @param date the date, or {@code null}.
	 */
	public final void setPublicationDate(String date) {
		setPublicationDate(Date.valueOf(date));
	}

	private void resetPublicationYear() {
		if (this.publicationDate != null) {
			this.publicationYear = this.publicationDate.toLocalDate().getYear();
		} else {
			this.publicationYear = getDefaultPublicationYear();
		}
	}

	/** Replies the year of publication.
	 * If the publication date is specified, the year is extracted from this date.
	 * If the publication date is not specified, the current year is replied.
	 *
	 * @return the year.
	 */
	public int getPublicationYear() {
		return this.publicationYear;
	}

	/** Replies the default publication year. By default is the current year.
	 *
	 * @return the default publication year.
	 */
	public static int getDefaultPublicationYear() {
		return LocalDate.now().getYear();
	}

	/** Change the publication year.
	 *
	 * @param year is the year of publication.
	 */
	public void setPublicationYear(int year) {
		this.publicationYear = year;
	}

	/** Change the publication year.
	 *
	 * @param year is the year of publication.
	 */
	public final void setPublicationYear(Number year) {
		if (year == null) {
			setPublicationYear(0);
		} else {
			setPublicationYear(year.intValue());
		}
	}

	/** Replies the ISBN number that is associated to this publication.
	 * Some implementation of this function delegates to the container of the publication, e.g., the journal.
	 *
	 * @return the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 */
	public String getISBN() {
		return this.isbn;
	}

	/** Change the ISBN number that is associated to this publication.
	 * Some implementation of this function delegates to the container of the publication, e.g., the journal.
	 *
	 * @param isbn the ISBN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/ISBN"
	 */
	public void setISBN(String isbn) {
		this.isbn = Strings.emptyToNull(isbn);
	}

	/** Replies the ISSN number that is associated to this publication.
	 * Some implementation of this function delegates to the container of the publication, e.g., the journal.
	 *
	 * @return the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 */
	public String getISSN() {
		return this.issn;
	}

	/** Change the ISSN number that is associated to this publication.
	 * Some implementation of this function delegates to the container of the publication, e.g., the journal.
	 *
	 * @param issn the ISSN number or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/International_Standard_Serial_Number"
	 */
	public void setISSN(String issn) {
		this.issn = Strings.emptyToNull(issn);
	}

	/** Replies the DOI reference number that is associated to this publication.
	 * Usually, the DOI number should not be prefixed by the {@code http://doi.org} prefix.
	 *
	 * @return the DOI reference or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/Digital_object_identifier"
	 */
	public String getDOI() {
		return this.doi;
	}

	/** Change the DOI reference number that is associated to this publication.
	 * Usually, the DOI number should not be prefixed by the {@code http://doi.org} prefix.
	 *
	 * @param doi the DOI reference or {@code null}.
	 * @see "https://en.wikipedia.org/wiki/Digital_object_identifier"
	 */
	public void setDOI(String doi) {
		this.doi = Strings.emptyToNull(doi);
	}

	/** Replies the HAL reference number that is associated to this publication.
	 * Usually, the HAL number should not be prefixed by the {@code http://hal.archives-ouvertes.fr} prefix.
	 *
	 * @return the HAL reference or {@code null}.
	 */
	public String getHalId() {
		return this.halId;
	}

	/** Change the HAL reference number that is associated to this publication.
	 * Usually, the HAL number should not be prefixed by the {@code http://hal.archives-ouvertes.fr} prefix.
	 *
	 * @param hal the HAL reference or {@code null}.
	 */
	public void setHalId(String hal) {
		this.halId = Strings.emptyToNull(hal);
	}

	/** Replies any extra URL that is associated to the publication.
	 *
	 * @return the URL or {@code null}.
	 * @see #getExtraURL()
	 */
	public final URL getExtraURLObject() {
		try {
			return new URL(getExtraURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Replies the string representation of any extra URL that is associated to the publication.
	 *
	 * @return the string representation of the extra URL or {@code null}.
	 * @see #getExtraURLObject()
	 */
	public String getExtraURL() {
		return this.extraUrl;
	}

	/** Change the extra URL that is associated to the publication.
	 *
	 * @param url the URL or {@code null}.
	 */
	public final void setExtraURL(URL url) {
		final String value;
		if (url != null) {
			value = url.toExternalForm();
		} else {
			value = null;
		}
		setExtraURL(value);
	}

	/** Change the extra URL that is associated to the publication.
	 *
	 * @param url the URL or {@code null}.
	 */
	public void setExtraURL(String url) {
		this.extraUrl = Strings.emptyToNull(url);
	}

	/** Replies any URL of a video that is associated to the publication.
	 *
	 * @return the URL or {@code null}.
	 * @see #getVideoURL()
	 */
	public final URL getVideoURLObject() {
		try {
			return new URL(getVideoURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Replies the string representation of any URL to a video that is associated to the publication.
	 *
	 * @return the string representation of the video URL or {@code null}.
	 * @see #getVideoURLObject()
	 */
	public String getVideoURL() {
		return this.videoUrl;
	}

	/** Change the URL of the video that is associated to the publication.
	 *
	 * @param url the URL or {@code null}.
	 */
	public final void setVideoURL(URL url) {
		final String value;
		if (url != null) {
			value = url.toExternalForm();
		} else {
			value = null;
		}
		setVideoURL(value);
	}

	/** Change the URL of the video that is associated to the publication.
	 *
	 * @param url the URL or {@code null}.
	 */
	public void setVideoURL(String url) {
		this.videoUrl = Strings.emptyToNull(url);
	}

	/** Replies URL of a DBLP page that is associated to the publication.
	 *
	 * @return the URL or {@code null}.
	 * @see #getDblpURL()
	 * @see "https://dblp.org/"
	 */
	public final URL getDblpURLObject() {
		try {
			return new URL(getDblpURL());
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/** Replies the string representation of the URL of a DBLP page that is associated to the publication.
	 *
	 * @return the string representation of the DBLP URL or {@code null}.
	 * @see #getDblpURLObject()
	 * @see "https://dblp.org/"
	 */
	public String getDblpURL() {
		return this.dblpUrl;
	}

	/** Change the URL of the DBLP page that is associated to the publication.
	 *
	 * @param url the URL or {@code null}.
	 * @see "https://dblp.org/"
	 */
	public final void setDblpURL(URL url) {
		final String value;
		if (url != null) {
			value = url.toExternalForm();
		} else {
			value = null;
		}
		setDblpURL(value);
	}

	/** Change the URL of the DBLP page that is associated to the publication.
	 *
	 * @param url the URL or {@code null}.
	 * @see "https://dblp.org/"
	 */
	public void setDblpURL(String url) {
		this.dblpUrl = Strings.emptyToNull(url);
	}

	/** Replies a path to a downloadable PDF for the publication.
	 *
	 * @return the path or {@code null}.
	 */
	public String getPathToDownloadablePDF() {
		return this.pathToDownloadablePDF;
	}

	/** Change the path to a downloadable PDF for the publication.
	 *
	 * @param path the path or {@code null}.
	 */
	public void setPathToDownloadablePDF(String path) {
		this.pathToDownloadablePDF = Strings.emptyToNull(path);
	}

	/** Replies a path to a downloadable certificate of award for the publication.
	 *
	 * @return the path or {@code null}.
	 */
	public String getPathToDownloadableAwardCertificate() {
		return this.pathToDownloadableAwardCertificate;
	}

	/** Change the path to a downloadable certificate of award for the publication.
	 *
	 * @param path the path or {@code null}.
	 */
	public void setPathToDownloadableAwardCertificate(String path) {
		this.pathToDownloadableAwardCertificate = Strings.emptyToNull(path);
	}

	/** Replies the major language that is used for writing the publication.
	 *
	 * @return the language, never {@code null}.
	 */
	public PublicationLanguage getMajorLanguage() {
		return this.majorLanguage;
	}

	/** Change the major language that is used for writing the publication.
	 *
	 * @param language the language. If {@code null}, the default language will be assigned.
	 */
	public void setMajorLanguage(PublicationLanguage language) {
		if (language == null) {
			this.majorLanguage = PublicationLanguage.getDefault();
		} else {
			this.majorLanguage = language;
		}
	}

	/** Change the major language that is used for writing the publication.
	 *
	 * @param language the language.
	 */
	public final void setMajorLanguage(String language) {
		if (Strings.isNullOrEmpty(language)) {
			setMajorLanguage((PublicationLanguage) null);
		} else {
			setMajorLanguage(PublicationLanguage.valueOfCaseInsensitive(language));
		}
	}

	/** Replies a short description of where the publication is published.
	 * This function is a general place-holder for obtaining the place where publication
	 * was published. The returned value depends strongly of the type of the publication,
	 * and therefore of the function implementation in the sub-types.
	 *
	 * @return the description.
	 */
	public abstract String getWherePublishedShortDescription();

}
