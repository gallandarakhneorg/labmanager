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

package fr.ciadlab.labmanager.utils.markdown;

import java.util.Arrays;

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Utilities for manupilating markdown with the Flexmark API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
@Component
@Primary
public class FlexmarkMarkdownTools implements MarkdownTools {

	private final MutableDataSet options = new MutableDataSet();

	private final Parser parser;

	private final HtmlRenderer renderer;

	/** Constructor.
	 */
	public FlexmarkMarkdownTools() {
		// uncomment to set optional extensions
        this.options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
		// uncomment to convert soft-breaks to hard breaks
        //options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        this.parser = Parser.builder(this.options).build();
        this.renderer = HtmlRenderer.builder(this.options).build();
	}
	
	@Override
	public String markdownToHTML(String markdown) {
        final Node document = this.parser.parse(markdown);
        final String html = this.renderer.render(document);
		return html;
	}

}
