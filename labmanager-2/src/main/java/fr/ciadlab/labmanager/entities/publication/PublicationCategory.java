/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.entities.publication;

import com.google.common.base.Strings;
import org.arakhne.afc.vmutil.locale.Locale;

/** A publication category defines a family of research papers in France.
 * The publication categories group several {@link PublicationType publications types}.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see PublicationType
 */
public enum PublicationCategory {
	/** Patents.
	 */
	BRE {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return true;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Articles in international or national journals with selection commitee and ranked in international databases.
	 */
	ACL {
		@Override
		public boolean isScientificJournalPaper() {
			return true;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Articles in international or national journals with selection committee and not ranked in international databases.
	 */
	ACLN {
		@Override
		public boolean isScientificJournalPaper() {
			return true;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Articles in international or national journals without selection committee.
	 */
	ASCL {
		@Override
		public boolean isScientificJournalPaper() {
			return true;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Papers in the proceedings of an international conference.
	 */
	C_ACTI {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return true;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Papers in the proceedings of a national conference.
	 */
	C_ACTN {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return true;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Oral Communications without proceeding in international or national conference.
	 */
	C_COM {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return true;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Posters in international or national conference.
	 */
	C_AFF {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return true;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Editor of books or journals.
	 */
	DO {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Scientific books.
	 */
	OS {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Chapters in scientific books.
	 */
	COS {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Keynotes in international or national conference.
	 */
	C_INV {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return true;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** PhD or Master theses.
	 */
	TH {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Publications for research transfer.
	 */
	PT {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Research tools (not software).
	 */
	OR {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	},
	/** Books for scientific culture dissemination.
	 */
	OV {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return true;
		}
	},
	/** Chapters in a book for scientific culture dissemination.
	 */
	COV {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return true;
		}
	},
	/** Papers for scientific culture dissemination.
	 */
	PV {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return true;
		}
	},
	/** Artistic research productions.
	 */
	PAT {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return true;
		}
	},
	/** Other productions (registered software, reports...)
	 */
	AP {
		@Override
		public boolean isScientificJournalPaper() {
			return false;
		}
		@Override
		public boolean isPatent() {
			return false;
		}
		@Override
		public boolean isScientificEventPaper() {
			return false;
		}
		@Override
		public boolean isScientificCultureDissemination() {
			return false;
		}
	};

	/** Replies the label for the publication category.
	 *
	 * @return the label of the status in the current language.
	 */
	public String getLabel() {
		final String label = Locale.getString(PublicationCategory.class, name());
		return Strings.nullToEmpty(label);
	}

	/** Replies if the category is for the scientific papers in journals.
	 *
	 * @return {@code true} if the category is for scientific papers.
	 */
	public abstract boolean isScientificJournalPaper();

	/** Replies if the category is for the patents.
	 *
	 * @return {@code true} if the category is for patents.
	 */
	public abstract boolean isPatent();

	/** Replies if the category is for the scientific events.
	 *
	 * @return {@code true} if the category is for scientific events.
	 */
	public abstract boolean isScientificEventPaper();

	/** Replies if the category is for the dissemination of scientific culture.
	 *
	 * @return {@code true} if the category is for scientific culture dissemination.
	 */
	public abstract boolean isScientificCultureDissemination();

}
