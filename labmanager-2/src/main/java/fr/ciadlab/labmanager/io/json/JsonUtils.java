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

package fr.ciadlab.labmanager.io.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;

/** Utility class for JSON exporters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public final class JsonUtils {

	private JsonUtils() {
		//
	}

	/** Write a field and its value in the given generator.
	 *
	 * @param generator the JSO generator.
	 * @param name the name of the field.
	 * @param value the value of the field.
	 * @throws IOException if the field cannot be written.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void writeField(JsonGenerator generator, String name, Object value) throws IOException {
		if (value instanceof JsonNode) {
			generator.writeFieldName(name);
			generator.writeTree((JsonNode) value);
		} else if (value instanceof Byte) {
			generator.writeNumberField(name, ((Byte) value).byteValue());
		} else if (value instanceof Short) {
			generator.writeNumberField(name, ((Short) value).shortValue());
		} else if (value instanceof Integer) {
			generator.writeNumberField(name, ((Integer) value).intValue());
		} else if (value instanceof Long) {
			generator.writeNumberField(name, ((Long) value).longValue());
		} else if (value instanceof Float) {
			generator.writeNumberField(name, ((Float) value).floatValue());
		} else if (value instanceof Double) {
			generator.writeNumberField(name, ((Double) value).doubleValue());
		} else if (value instanceof BigInteger) {
			generator.writeNumberField(name, (BigInteger) value);
		} else if (value instanceof BigDecimal) {
			generator.writeNumberField(name, (BigDecimal) value);
		} else if (value instanceof Number) {
			generator.writeNumberField(name, ((Number) value).doubleValue());
		} else if (value instanceof Boolean) {
			generator.writeBooleanField(name, ((Boolean) value).booleanValue());
		} else if (value instanceof Character) {
			generator.writeStringField(name, value.toString());
		} else if (value instanceof LocalDate || value instanceof java.sql.Date) {
			generator.writeStringField(name, value.toString());
		} else if (value != null
				&& (value.getClass().isEnum() || value instanceof Enum)) {
			generator.writeStringField(name, ((Enum<?>) value).name());
		} else if (value instanceof Map) {
			final Map iter = (Map) value;
			final Iterator<Entry> iterator = iter.entrySet().iterator();
			generator.writeObjectFieldStart(name);
			while (iterator.hasNext()) {
				final Entry elt = iterator.next();
				if (elt.getKey() != null) {
					writeField(generator, elt.getKey().toString(), elt.getValue());
				}
			}
			generator.writeEndObject();
		} else if (value instanceof Iterable) {
			final Iterable<?> iter = (Iterable<?>) value;
			final Iterator<?> iterator = iter.iterator();
			generator.writeArrayFieldStart(name);
			while (iterator.hasNext()) {
				final Object elt = iterator.next();
				writeValue(generator, elt);
			}
			generator.writeEndArray();
		} else if (value !=null && value.getClass().isArray()) {
			generator.writeArrayFieldStart(name);
			for (int i = 0; i < Array.getLength(value); ++i) {
				writeValue(generator, Array.get(value, i));
			}
			generator.writeEndArray();
		} else if (value instanceof JsonSerializable) {
			final JsonSerializable serializable = (JsonSerializable) value;
			generator.writeFieldName(name);
			serializable.serialize(generator, null);
		} else if (value != null) {
			generator.writeStringField(name, value.toString());
		}
	}

	/** Write a value in the given generator.
	 *
	 * @param generator the JSO generator.
	 * @param value the value of the field.
	 * @throws IOException if the field cannot be written.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void writeValue(JsonGenerator generator, Object value) throws IOException {
		if (value instanceof JsonNode) {
			generator.writeTree((JsonNode) value);
		} else if (value instanceof Byte) {
			generator.writeNumber(((Byte) value).byteValue());
		} else if (value instanceof Short) {
			generator.writeNumber(((Short) value).shortValue());
		} else if (value instanceof Integer) {
			generator.writeNumber(((Integer) value).intValue());
		} else if (value instanceof Long) {
			generator.writeNumber(((Long) value).longValue());
		} else if (value instanceof Float) {
			generator.writeNumber(((Number) value).floatValue());
		} else if (value instanceof Double) {
			generator.writeNumber(((Number) value).doubleValue());
		} else if (value instanceof BigInteger) {
			generator.writeNumber((BigInteger) value);
		} else if (value instanceof BigDecimal) {
			generator.writeNumber((BigDecimal) value);
		} else if (value instanceof Number) {
			generator.writeNumber(((Number) value).doubleValue());
		} else if (value instanceof Boolean) {
			generator.writeBoolean(((Boolean) value).booleanValue());
		} else if (value instanceof Character) {
			generator.writeString(value.toString());
		} else if (value instanceof LocalDate || value instanceof java.sql.Date) {
			generator.writeString(value.toString());
		} else if (value != null && value.getClass().isEnum()) {
			generator.writeString(((Enum<?>) value).name());
		} else if (value instanceof Map) {
			final Map iter = (Map) value;
			final Iterator<Entry> iterator = iter.entrySet().iterator();
			generator.writeStartObject();
			while (iterator.hasNext()) {
				final Entry elt = iterator.next();
				if (elt.getKey() != null) {
					writeField(generator, elt.getKey().toString(), elt.getValue());
				}
			}
			generator.writeEndObject();
		} else if (value instanceof Iterable) {
			final Iterable<?> iter = (Iterable<?>) value;
			final Iterator<?> iterator = iter.iterator();
			generator.writeStartArray();
			while (iterator.hasNext()) {
				final Object elt = iterator.next();
				writeValue(generator, elt);
			}
			generator.writeEndArray();
		} else if (value != null && value.getClass().isArray()) {
			generator.writeStartArray();
			for (int i = 0; i < Array.getLength(value); ++i) {
				writeValue(generator, Array.get(value, i));
			}
			generator.writeEndArray();
		} else if (value instanceof JsonSerializable) {
			final JsonSerializable serializable = (JsonSerializable) value;
			serializable.serialize(generator, null);
		} else if (value != null) {
			generator.writeString(value.toString());
		}
	}

}
