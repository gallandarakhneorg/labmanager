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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

/** Tests for {@link JsonUtils}.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class JsonUtilsTest {

	@Test
	public void defaultBehavior_integer() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", 123);
		assertEquals("{\"xyz\":123}", json.toString());
	}

	@Test
	public void defaultBehavior_boolean() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", true);
		assertEquals("{\"xyz\":true}", json.toString());
	}

	@Test
	public void defaultBehavior_float() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", 123.456f);
		assertEquals("{\"xyz\":123.456}", json.toString());
	}

	@Test
	public void defaultBehavior_character() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", 't');
		assertEquals("{\"xyz\":\"t\"}", json.toString());
	}

	@Test
	public void defaultBehavior_string() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", "abc");
		assertEquals("{\"xyz\":\"abc\"}", json.toString());
	}

	@Test
	public void defaultBehavior_array() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", new long[] { 123l, 456l, 789l, 147l });
		assertEquals("{\"xyz\":[123,456,789,147]}", json.toString());
	}

	@Test
	public void defaultBehavior_map() {
		Map<Integer, String> map = new TreeMap<>();
		map.put(123, "abc");
		map.put(456, "def");
		map.put(789, "ghi");
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", map);
		assertEquals("{\"xyz\":{\"123\":\"abc\",\"456\":\"def\",\"789\":\"ghi\"}}", json.toString());
	}

	@Test
	public void defaultBehavior_list() {
		List<Integer> list = new ArrayList<>();
		list.add(123);
		list.add(456);
		list.add(789);
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", list);
		assertEquals("{\"xyz\":[123,456,789]}", json.toString());
	}

	@Test
	public void defaultBehavior_set() {
		Set<Integer> list = new TreeSet<>();
		list.add(123);
		list.add(456);
		list.add(789);
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", list);
		assertEquals("{\"xyz\":[123,456,789]}", json.toString());
	}

	@Test
	public void defaultBehavior_object() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", new FakeObject());
		assertEquals("{\"xyz\":\"abc def\"}", json.toString());
	}

	@Test
	public void defaultBehavior_jsonable() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", new FakeJsonExportable(145));
		assertEquals("{\"xyz\":{\"abc\":145,\"862\":\"xyz\"}}", json.toString());
	}

	@Test
	public void defaultBehavior_arrayOfJsonable() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz",
				new JsonExportable[] {
						new FakeJsonExportable(145),
						new FakeJsonExportable(368),
						new FakeJsonExportable(745),
		});
		assertEquals("{\"xyz\":[{\"abc\":145,\"862\":\"xyz\"},{\"abc\":368,\"862\":\"xyz\"},{\"abc\":745,\"862\":\"xyz\"}]}", json.toString());
	}

	@Test
	public void defaultBehavior_arrayOfListOfJsonsable() {
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz",
				new List[] {
						Collections.singletonList(new FakeJsonExportable(145)),
						Collections.singletonList(new FakeJsonExportable(368)),
		});
		assertEquals("{\"xyz\":[[{\"abc\":145,\"862\":\"xyz\"}],[{\"abc\":368,\"862\":\"xyz\"}]]}", json.toString());
	}

	@Test
	public void defaultBehavior_mapOfJsonsable() {
		Map map = new TreeMap<>();
		map.put(123, new FakeJsonExportable(159));
		map.put(789, new FakeJsonExportable(753));
		JsonObject json = new JsonObject();
		JsonUtils.defaultBehavior(json, "xyz", map);
		assertEquals("{\"xyz\":{\"123\":{\"abc\":159,\"862\":\"xyz\"},\"789\":{\"abc\":753,\"862\":\"xyz\"}}}", json.toString());
	}

	public static class FakeObject {
		@Override
		public String toString() {
			return "abc def";
		}
	}

	public static class FakeJsonExportable implements JsonExportable {
		private final int id;
		public FakeJsonExportable(int id) {
			this.id = id;
		}
		@Override
		public void toJson(JsonObject json) {
			json.addProperty("abc", this.id);
			json.addProperty("862", "xyz");
		}
	}

}
