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

package fr.ciadlab.labmanager.io.od;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.springframework.stereotype.Component;

/** Utility class for creating ODF text document.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.7
 */
@Component
public class OdfTextDocumentHelper {

	/** Constructor without initial template.
	 */
	public OdfTextDocumentHelper() {
		//
	}

	/** Append the given values, with the given separator to the receiver.
	 *
	 * @param receiver the receiver.
	 * @param separator the separator of values.
	 * @param values the values to add.
	 * @return {@code true} if a value was added.
	 */
	@SuppressWarnings("static-method")
	public boolean append(TextPElement receiver, String separator, String... values) {
		assert receiver != null;
		assert separator != null;
		assert values != null;
		boolean first = true;
		boolean added = false;
		for (final String value : values) {
			if (!Strings.isNullOrEmpty(value)) {
				if (first) {
					first = false;
				} else {
					receiver.newTextNode(separator);
				}
				receiver.newTextNode(value);
				added = true;
			}
		}
		return added;
	}

	/** Append the given value if it is not empty.
	 *
	 * @param receiver the receiver.
	 * @param value the values to add.
	 * @return {@code true} if a value was added.
	 */
	public boolean append(TextPElement receiver, String value) {
		return append(receiver, "", value); //$NON-NLS-1$
	}


	/** Replies a text that is underlined.
	 * 
	 * @param odtText the container.
	 * @return the formating element.
	 */
	@SuppressWarnings("static-method")
	public TextSpanElement newTextUnderline(TextPElement odtText) {
		final TextSpanElement odtSpan = odtText.newTextSpanElement();
		odtSpan.setProperty(StyleTextPropertiesElement.TextUnderlineType, "single"); //$NON-NLS-1$
		odtSpan.setProperty(StyleTextPropertiesElement.TextUnderlineStyle, "solid"); //$NON-NLS-1$
		return odtSpan;
	}

	/** Replies a text that is italic.
	 * 
	 * @param odtText the container.
	 * @return the formating element.
	 */
	@SuppressWarnings("static-method")
	public TextSpanElement newTextItalic(TextPElement odtText) {
		final TextSpanElement odtSpan = odtText.newTextSpanElement();
		odtSpan.setProperty(StyleTextPropertiesElement.FontStyle, "italic"); //$NON-NLS-1$
		return odtSpan;
	}

	/** Replies a text that is bold.
	 * 
	 * @param odtText the container.
	 * @return the formating element.
	 */
	@SuppressWarnings("static-method")
	public TextSpanElement newTextBold(TextPElement odtText) {
		final TextSpanElement odtSpan = odtText.newTextSpanElement();
		odtSpan.setProperty(StyleTextPropertiesElement.FontWeight, "bold"); //$NON-NLS-1$
		return odtSpan;
	}

	/** Replies a text that is bold and underlined.
	 * 
	 * @param odtText the container.
	 * @return the formating element.
	 */
	@SuppressWarnings("static-method")
	public TextSpanElement newTextBoldUnderline(TextPElement odtText) {
		final TextSpanElement odtSpan = odtText.newTextSpanElement();
		odtSpan.setProperty(StyleTextPropertiesElement.FontWeight, "bold"); //$NON-NLS-1$
		odtSpan.setProperty(StyleTextPropertiesElement.TextUnderlineType, "single"); //$NON-NLS-1$
		odtSpan.setProperty(StyleTextPropertiesElement.TextUnderlineStyle, "solid"); //$NON-NLS-1$
		return odtSpan;
	}

	/** Replies the value preceded by its decoration if the value is not {@code null} or empty.
	 *
	 * @param value the value to decorate.
	 * @param decorator the text to append before the value.
	 * @return the decorated value.
	 */
	@SuppressWarnings("static-method")
	public String decorateBefore(String value, String decorator) {
		assert decorator != null;
		if (!Strings.isNullOrEmpty(value)) {
			return decorator + value;
		}
		return ""; //$NON-NLS-1$
	}


	/** Replies the string representation of the given number if it is strictly positive.
	 * 
	 * @param number the number.
	 * @return the formatted number or {@code null} if the number is negative or nul.
	 */
	@SuppressWarnings("static-method")
	public String formatNumberIfStrictlyPositive(float number) {
		if (number > 0f) {
			final NumberFormat format = new DecimalFormat("#0.000"); //$NON-NLS-1$
			return format.format(number);
		}
		return null;
	}

}
