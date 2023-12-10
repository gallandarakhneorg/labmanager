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

package fr.utbm.ciad.labmanager.views.persons;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import fr.utbm.ciad.labmanager.data.member.Gender;
import fr.utbm.ciad.labmanager.utils.country.CountryCode;
import fr.utbm.ciad.labmanager.views.components.ComponentFactory;
import fr.utbm.ciad.labmanager.views.components.DetailsWithErrorMark;

/** Abstract implementation for the information related to a person.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
public abstract class AbstractPersonEditorView extends Composite<VerticalLayout> implements LocaleChangeObserver {

	private static final long serialVersionUID = 6088942935576032576L;

	private DetailsWithErrorMark personalInformationDetails;

	private TextField lastname;

	private TextField firstname;

	private ComboBox<Gender> gender;

	private DetailsWithErrorMark contactInformationDetails;

	private EmailField email;

	private TextField officePhone;

	private TextField mobilePhone;

	private TextField officeRoom;

	/** Constructor.
	 */
	public AbstractPersonEditorView() {
		final VerticalLayout rootContainer = new VerticalLayout();
		rootContainer.setSpacing(true);

		createPersonalInformationComponents(rootContainer);
		createContactInformationComponents(rootContainer);

		getContent().add(rootContainer);
		/**

Gravatar identifier

ORCID
Identifier on Scopus (Elsevier)
Identifier on ResearcherID, WoS or Publon
Identifier for Google Scholar

H-index on Google Scholar
H-index on Web-of-Science (WoS)
H-index on Scopus
Number of paper citations on Google Scholar
Number of paper citations on Web-of-Science (WoS)
Number of paper citations on Scopus

Identifier on Research Gate
Identifier on AD Scientific Index
Internet address of the DBLP page
Internet address of the Academia.edu
Internet address of the EU CORDIS page

Identifier on Linked-In
Github Identifier
Facebook Identifier

Naming convention for the person's page on the institution website

Is the person information validated by a local authority?          */
	}

	/** Create the components for entering the personal informations.
	 * The personal informations include:<ul>
	 * <li>Last Name</li>
	 * <li>First Name</li>
	 * <li>Gender</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	protected void createPersonalInformationComponents(VerticalLayout receiver) {
		final FormLayout content = ComponentFactory.newColumnForm(2);

		this.lastname = new TextField();
		this.lastname.setRequired(true);
		content.add(this.lastname);

		this.firstname = new TextField();
		this.firstname.setRequired(true);
		content.add(this.firstname);

		this.gender = new ComboBox<>();
		this.gender.setItems(Gender.values());
		this.gender.setItemLabelGenerator(Gender::getLabel);
		this.gender.setHelperText(getTranslation("views.persons.gender.helper")); //$NON-NLS-1$);
		this.gender.setValue(Gender.NOT_SPECIFIED);
		content.add(this.gender);

		this.personalInformationDetails = new DetailsWithErrorMark(content);
		this.personalInformationDetails.setOpened(true);
		receiver.add(this.personalInformationDetails);

		//FIXME
		this.lastname.setValue("Galland");
		this.firstname.setValue("Stéphane");
		this.gender.setValue(Gender.NOT_SPECIFIED);
	}

	/** Create the components for entering the contact informations.
	 * The contact informations include:<ul>
	 * <li>Email</li>
	 * <li>Office phone</li>
	 * <li>Mobile phone</li>
	 * <li>Office room</li>
	 * </ul>
	 *
	 * @param receiver the receiver of the component
	 */
	protected void createContactInformationComponents(VerticalLayout receiver) {
		final FormLayout content = ComponentFactory.newColumnForm(2);

		this.email = new EmailField();
		this.email.getElement().setAttribute("name", "email"); //$NON-NLS-1$ //$NON-NLS-2$
		this.email.setErrorMessage(getTranslation("views.persons.email.error")); //$NON-NLS-1$
		this.email.setRequired(true);
		this.email.setClearButtonVisible(true);
		content.add(this.email, 2);

		this.officePhone = ComponentFactory.newPhoneNumberField(CountryCode.FRANCE);
		content.add(this.officePhone);

		this.mobilePhone = ComponentFactory.newPhoneNumberField(CountryCode.FRANCE);
		content.add(this.mobilePhone);

		this.contactInformationDetails = new DetailsWithErrorMark(content);
		this.contactInformationDetails.setOpened(true);
		receiver.add(this.contactInformationDetails);

		this.officeRoom = new TextField();
		this.officeRoom.setHelperText(getTranslation("views.persons.officeroom.helper")); //$NON-NLS-1$
		content.add(this.officeRoom);

		//FIXME
		this.email.setValue("stephane.galland@utbm.fr");
		this.officePhone.setValue("+33 384 583 418");
		this.mobilePhone.setValue("+33 662 274 442");
		this.officeRoom.setValue("D209");
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.personalInformationDetails.setLabel(getTranslation("views.persons.personal_informations")); //$NON-NLS-1$
		this.lastname.setLabel(getTranslation("views.persons.last_name")); //$NON-NLS-1$
		this.firstname.setLabel(getTranslation("views.persons.last_name")); //$NON-NLS-1$
		this.gender.setLabel(getTranslation("views.persons.gender")); //$NON-NLS-1$

		this.contactInformationDetails.setLabel(getTranslation("views.persons.contact_informations")); //$NON-NLS-1$
		this.email.setLabel(getTranslation("views.persons.email")); //$NON-NLS-1$
		this.officePhone.setLabel(getTranslation("views.persons.officephone")); //$NON-NLS-1$
		this.mobilePhone.setLabel(getTranslation("views.persons.mobilephone")); //$NON-NLS-1$
		this.officeRoom.setLabel(getTranslation("views.persons.officeroom")); //$NON-NLS-1$
	}

}
