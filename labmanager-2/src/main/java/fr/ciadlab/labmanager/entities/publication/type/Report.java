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

package fr.ciadlab.labmanager.entities.publication.type;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ciadlab.labmanager.entities.publication.Publication;
import fr.ciadlab.labmanager.utils.HashCodeUtils;
import org.apache.jena.ext.com.google.common.base.Strings;

/** A report (scientific, technical, manual).
 *
 * <p>This type is equivalent to the BibTeX types: {@code manual}, {@code techreport}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Entity
@Table(name = "Reports")
@PrimaryKeyJoinColumn(name = "id")
public class Report extends Publication {

	private static final long serialVersionUID = 2108690602460073396L;

	/** Name of the university, school or institution that published the report.
	 */
	@Column
	private String institution;

	/** Geographical address of the institution that published the report. It is usually a city and a country.
	 */
	@Column
	private String address;

	/** Type of report.
	 */
	@Column
	private String reportType;

	/** Number that is assigned by the institution to the report.
	 */
	@Column
	private String reportNumber;

	/** Construct a report with the given values.
	 *
	 * @param publication the publication to copy.
	 * @param institution the name of the institution in which the report was published.
	 * @param address the geographical address of the institution. Usually a city and a country.
	 * @param type the type of report.
	 * @param number the number of the report.
	 */
	public Report(Publication publication, String institution, String address, String type, String number) {
		super(publication);
		this.institution = institution;
		this.address = address;
		this.reportType = type;
		this.reportNumber = number;
	}

	/** Construct an empty report.
	 */
	public Report() {
		//
	}

	@Override
	public int hashCode() {
		int h = super.hashCode();
		h = HashCodeUtils.add(h, this.institution);
		h = HashCodeUtils.add(h, this.address);
		h = HashCodeUtils.add(h, this.reportType);
		h = HashCodeUtils.add(h, this.reportNumber);
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		final Report other = (Report) obj;
		if (!Objects.equals(this.institution, other.institution)) {
			return false;
		}
		if (!Objects.equals(this.address, other.address)) {
			return false;
		}
		if (!Objects.equals(this.reportType, other.reportType)) {
			return false;
		}
		if (!Objects.equals(this.reportNumber, other.reportNumber)) {
			return false;
		}
		return true;
	}

	@Override
	public void forEachAttribute(AttributeConsumer consumer) throws IOException {
		super.forEachAttribute(consumer);
		if (!Strings.isNullOrEmpty(getInstitution())) {
			consumer.accept("institution", getInstitution()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			consumer.accept("address", getAddress()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getReportNumber())) {
			consumer.accept("reportNumber", getReportNumber()); //$NON-NLS-1$
		}
		if (!Strings.isNullOrEmpty(getReportType())) {
			consumer.accept("reportType", getReportType()); //$NON-NLS-1$
		}
	}

	@Override
	@JsonIgnore
	public String getWherePublishedShortDescription() {
		final StringBuilder buf = new StringBuilder();
		buf.append(getInstitution());
		final boolean b0 = !Strings.isNullOrEmpty(getReportNumber());
		final boolean b1 = !Strings.isNullOrEmpty(getReportType());
		if (b0 || b1) {
			buf.append(", "); //$NON-NLS-1$
			if (b0 && b1) {
				buf.append(getReportNumber());
				buf.append("/"); //$NON-NLS-1$
				buf.append(getReportType());
			} else if (b0) {
				buf.append(getReportNumber());
			} else {
				buf.append(getReportType());
			}
		}
		if (!Strings.isNullOrEmpty(getAddress())) {
			buf.append(", "); //$NON-NLS-1$
			buf.append(getAddress());
		}
		if (!Strings.isNullOrEmpty(getISBN())) {
			buf.append(", ISBN "); //$NON-NLS-1$
			buf.append(getISBN());
		}
		if (!Strings.isNullOrEmpty(getISSN())) {
			buf.append(", ISSN "); //$NON-NLS-1$
			buf.append(getISSN());
		}
		return buf.toString();
	}

	/** Replies the name of the institution in which the report was published.
	 *
	 * @return the name of the institution.
	 */
	public String getInstitution() {
		return this.institution;
	}

	/** Chage the name of the institution in which the report was published.
	 *
	 * @param name the name of the institution.
	 */
	public void setInstitution(String name) {
		this.institution = Strings.emptyToNull(name);
	}

	/** Replies the geographical address where the report was published. It is usually a city and a country.
	 *
	 * @return the address.
	 */
	public String getAddress() {
		return this.address;
	}

	/** Change the geographical address where the report was published. It is usually a city and a country.
	 *
	 * @param address the address.
	 */
	public void setAddress(String address) {
		this.address = Strings.emptyToNull(address);
	}

	/** Replies the type of report.
	 *
	 * @return the type description.
	 */
	public String getReportType() {
		return this.reportType;
	}

	/** Change the type of report.
	 *
	 * @param type the type description.
	 */
	public void setReportType(String type) {
		this.reportType = Strings.emptyToNull(type);
	}

	/** Replies the number that was assigned by the institution to the report.
	 *
	 * @return the report number.
	 */
	public String getReportNumber() {
		return this.reportNumber;
	}

	/** Replies the number that was assigned by the institution to the report.
	 *
	 * @param number the report number.
	 */
	public void setReportNumber(String number) {
		this.reportNumber = Strings.emptyToNull(number);
	}

	@Override
	public boolean isRanked() {
		return false;
	}

}
