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

import java.io.ByteArrayOutputStream;

import org.apache.jena.ext.com.google.common.base.Strings;
import org.odftoolkit.odfdom.doc.OdfDocument.OdfMediaType;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.type.Color;
import org.springframework.http.MediaType;

/** Utility class for creating ODF spreadsheet.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 3.6
 */
public class OdfSpreadsheetHelper implements AutoCloseable {

	private static final String HORIZONTAL_CENTER = "center"; //$NON-NLS-1$

	private static final String VERTICAL_CENTER = "middle"; //$NON-NLS-1$

	private static final String HEADER_BACKGROUND = "#003195"; //$NON-NLS-1$

	private static final String CURRENCY = "€"; //$NON-NLS-1$

	private static final String CURRENCY_FORMAT = "#0.0 €"; //$NON-NLS-1$

	private static final String LONG_FORMAT = "#0"; //$NON-NLS-1$

	private static final String DOUBLE_FORMAT = "#0.0"; //$NON-NLS-1$

	private final OdfSpreadsheetDocument ods;
	
	/** Constructor.
	 *
	 * @throws Exception if it is impossible to create the ODF document.
	 */
	public OdfSpreadsheetHelper() throws Exception {
		this.ods = OdfSpreadsheetDocument.newSpreadsheetDocument();
	}
	
	@Override
	public void close() throws Exception {
		this.ods.close();
	}

	/** Create a new table in the document, with the given title.
	 *
	 * @param title the title of the table.
	 * @return the table.
	 * @throws Exception if it is impossible to create the ODF table.
	 */
	public TableHelper newTable(String title) throws Exception {
		final OdfTable source = this.ods.getSpreadsheetTables().get(0);
		final OdfTable target = OdfTable.getInstance((TableTableElement) source.getOdfElement().cloneElement());
		this.ods.getContentRoot().appendChild(target.getOdfElement());
		target.setTableName(title);
		return new TableHelper(target);
	}

	/** Replies the MIME type of the spreadsheet.
	 *
	 * @return the MIME type.
	 */
	public MediaType getMediaType() {
		return MediaType.valueOf(this.ods.getMediaTypeString());
	}

	/** Replies the file extension for the spreadsheet.
	 *
	 * @return the filename extension.
	 */
	@SuppressWarnings("static-method")
	public String getFileExtension() {
		return OdfMediaType.SPREADSHEET.getSuffix();
	}

	/** Save the spreadsheet in a byte array.
	 * 
	 * @return the byte array of the spreadsheet.
	 * @throws Exception if the byte array cannot be created.
	 */
	public byte[] toByteArray() throws Exception {
		// Remove the first table that is used as a template for the other tables.
		this.ods.getSpreadsheetTables().get(0).remove();
		// Serialize the spreadsheet
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			this.ods.save(output);
			output.flush();
			return output.toByteArray();
		}
	}

	/** Utility class for an ODF spreadsheet table.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.6
	 */
	public static class TableHelper {

		private final OdfTable table;
		
		/** Constructor.
		 *
		 * @param table the wrapped table.
		 */
		protected TableHelper(OdfTable table) {
			this.table = table;
		}
		
		/** Replies the row that is the header of the table.
		 *
		 * @return the header row.
		 */
		public TableHeaderHelper getHeader() {
			final OdfTableRow row = this.table.getRowByIndex(0);
			row.setUseOptimalHeight(true);
			return new TableHeaderHelper(row);
		}

		/** Replies the content of the table.
		 *
		 * @return the content helper.
		 */
		public TableContentHelper getContent() {
			return new TableContentHelper(this.table);
		}

	}

	/** Utility class for header row of an ODF spreadsheet table.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.6
	 */
	public static class TableHeaderHelper {

		private final OdfTableRow row;

		private int index;
		
		/** Constructor.
		 *
		 * @param row the wrapped row.
		 */
		protected TableHeaderHelper(OdfTableRow row) {
			this.row = row;
		}

		/** Append a column in the header.
		 *
		 * @param title the title of the column.
		 */
		public void appendColumn(String title) {
			final OdfTableCell cell = this.row.getCellByIndex(this.index);
			++this.index;
			cell.setStringValue(Strings.nullToEmpty(title));
			cell.setHorizontalAlignment(HORIZONTAL_CENTER);
			cell.setVerticalAlignment(VERTICAL_CENTER);
			cell.setCellBackgroundColor(HEADER_BACKGROUND);
			cell.setTextWrapped(true);
		}

	}

	/** Utility class for content rows of an ODF spreadsheet table.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.6
	 */
	public static class TableContentHelper {

		private final OdfTable table;

		private int rowIndex = 1;
		
		/** Constructor.
		 *
		 * @param table the wrapped table.
		 */
		protected TableContentHelper(OdfTable table) {
			this.table = table;
		}

		/** Append a row in the content.
		 *
		 * @return the row.
		 */
		public TableContentRowHelper appendRow() {
			final OdfTableRow row = this.table.getRowByIndex(this.rowIndex);
			++this.rowIndex;
			return new TableContentRowHelper(row);
		}

	}

	/** Utility class for content row of an ODF spreadsheet table.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 3.6
	 */
	public static class TableContentRowHelper {

		private final OdfTableRow row;

		private int index;
		
		/** Constructor.
		 *
		 * @param row the wrapped row.
		 */
		protected TableContentRowHelper(OdfTableRow row) {
			this.row = row;
		}

		private static void defaultContentCellStyle(OdfTableCell cell) {
			cell.setVerticalAlignment(VERTICAL_CENTER);
			cell.setCellBackgroundColor((Color) null);
			cell.setTextWrapped(true);
		}

		/** Append a cell with a text value.
		 *
		 * @param value the content of the cell.
		 */
		public void append(String value) {
			final OdfTableCell cell = this.row.getCellByIndex(this.index);
			++this.index;
			cell.setStringValue(Strings.nullToEmpty(value));
			defaultContentCellStyle(cell);
		}

		/** Append a cell with a currency value.
		 *
		 * @param value the content of the cell.
		 */
		public void appendCurrency(Double value) {
			final OdfTableCell cell = this.row.getCellByIndex(this.index);
			++this.index;
			cell.setCurrencyValue(value == null ? Double.valueOf(Double.NaN) : value, CURRENCY);
			cell.setCurrencyFormat(CURRENCY, CURRENCY_FORMAT);
			defaultContentCellStyle(cell);
		}

		/** Append a cell with an integer value.
		 *
		 * @param value the content of the cell.
		 */
		public void append(Long value) {
			final OdfTableCell cell = this.row.getCellByIndex(this.index);
			++this.index;
			cell.setDoubleValue(Double.valueOf(value == null ? 0l : value.doubleValue()));
			cell.setFormatString(LONG_FORMAT);
			defaultContentCellStyle(cell);
		}
	
		/** Append a cell with a floating-point value.
		 *
		 * @param value the content of the cell.
		 */
		public void append(Double value) {
			final OdfTableCell cell = this.row.getCellByIndex(this.index);
			++this.index;
			cell.setDoubleValue(value == null ? Double.valueOf(Double.NaN) : value);
			cell.setFormatString(DOUBLE_FORMAT);
			defaultContentCellStyle(cell);
		}

		/** Append a cell with a boolean value.
		 *
		 * @param value the content of the cell.
		 */
		public void append(Boolean value) {
			final OdfTableCell cell = this.row.getCellByIndex(this.index);
			++this.index;
			cell.setBooleanValue(value == null ? Boolean.FALSE : value);
			defaultContentCellStyle(cell);
		}

	}

}
