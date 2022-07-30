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

package fr.ciadlab.labmanager.utils;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	
	/** Implementation of a standard behavior for exporting an attribute to JSON.
	 *
	 * @param output the JSON data structure to fill up.
	 * @param name the name of the attribute.
	 * @param value the value of the attribute.
	 */
	@SuppressWarnings({"unchecked","rawtypes"})
	public static void defaultBehavior(JsonObject output, String name, Object value) {
		assert output != null;
		assert name != null;
		if (value != null) {
			if (value instanceof Number) {
				output.addProperty(name, (Number) value);
			} else if (value instanceof Boolean) {
				output.addProperty(name, (Boolean) value);
			} else if (value instanceof Character) {
				output.addProperty(name, (Character) value);
			} else if (value.getClass().isArray()) {
				final JsonArray sub = new JsonArray();
				for (int i = 0; i < Array.getLength(value); ++i) {
					addToArray(sub, Array.get(value, i));
				}
				output.add(name, sub);
			} else if (value instanceof Map) {
				final Map iter = (Map) value;
				final JsonObject sub = new JsonObject();
				final Iterator<Entry> iterator = iter.entrySet().iterator();
				while (iterator.hasNext()) {
					final Entry elt = iterator.next();
					if (elt.getKey() != null) {
						defaultBehavior(sub, elt.getKey().toString(), elt.getValue());
					}
				}
				output.add(name, sub);
			} else if (value instanceof Iterable) {
				final Iterable<?> iter = (Iterable<?>) value;
				final JsonArray sub = new JsonArray();
				final Iterator<?> iterator = iter.iterator();
				while (iterator.hasNext()) {
					final Object elt = iterator.next();
					addToArray(sub, elt);
				}
				output.add(name, sub);
			} else if (value instanceof JsonExportable) {
				final JsonObject sub = new JsonObject();
				((JsonExportable) value).toJson(sub);
				output.add(name, sub);
			} else {
				output.addProperty(name, value.toString());
			}
		}
	}

	@SuppressWarnings({"unchecked","rawtypes"})
	private static void addToArray(JsonArray array, Object value) {
		if (value instanceof JsonElement) {
			array.add((JsonElement) value);
		} else if (value instanceof Number) {
			array.add((Number) value);
		} else if (value instanceof Boolean) {
			array.add((Boolean) value);
		} else if (value instanceof Character) {
			array.add((Character) value);
		} else if (value instanceof Array) {
			final Array arr = (Array) value;
			final JsonArray sub = new JsonArray();
			for (int i = 0; i < Array.getLength(arr); ++i) {
				addToArray(sub, Array.get(arr, i));
			}
			array.add(sub);
		} else if (value instanceof Map) {
			final Map iter = (Map) value;
			final JsonObject sub = new JsonObject();
			final Iterator<Entry> iterator = iter.entrySet().iterator();
			while (iterator.hasNext()) {
				final Entry elt = iterator.next();
				if (elt.getKey() != null) {
					defaultBehavior(sub, elt.getKey().toString(), elt.getValue());
				}
			}
			array.add(sub);
		} else if (value instanceof Iterable) {
			final Iterable<?> iter = (Iterable<?>) value;
			final JsonArray sub = new JsonArray();
			final Iterator<?> iterator = iter.iterator();
			while (iterator.hasNext()) {
				final Object elt = iterator.next();
				addToArray(sub, elt);
			}
			array.add(sub);
		} else if (value instanceof JsonExportable) {
			final JsonObject sub = new JsonObject();
			((JsonExportable) value).toJson(sub);
			array.add(sub);
		} else if (value != null) {
			array.add(value.toString());
		}
	}

}
