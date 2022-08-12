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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.json.JsonObject;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.assertj.core.util.Arrays;
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
	public void writeField_serializable() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeField(generator, "fname", new FakeJsonExportable(951));
		verify(generator).writeFieldName(eq("fname"));
		verify(generator).writeStartObject();
		verify(generator).writeNumberField(eq("abc"), eq(951));
		verify(generator).writeStringField(eq("862"), eq("xyz"));
		verify(generator).writeEndObject();
		verify(generator).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeField_Map() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		Map<String, Object> map = new HashMap<>();
		map.put("nvalue", 456.852);
		map.put("svalue", "xyz");
		map.put("fobj", new FakeObject());
		map.put("jobj", new FakeJsonExportable(951));
		JsonUtils.writeField(generator, "fname", map);
		verify(generator).writeObjectFieldStart(eq("fname"));
		verify(generator).writeNumberField(eq("nvalue"), eq(456.852));
		verify(generator).writeStringField(eq("svalue"), eq("xyz"));
		verify(generator).writeStringField(eq("fobj"), eq("abc def"));
		verify(generator).writeFieldName(eq("jobj"));
		verify(generator).writeStartObject();
		verify(generator).writeNumberField(eq("abc"), eq(951));
		verify(generator).writeStringField(eq("862"), eq("xyz"));
		verify(generator, times(2)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeField_List() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		Object[] array = new Object[] {456.852, "xyz", new FakeObject(), new FakeJsonExportable(951)};
		JsonUtils.writeField(generator, "fname", Arrays.asList(array));
		verify(generator).writeArrayFieldStart(eq("fname"));
		verify(generator).writeNumber(eq(456.852));
		verify(generator).writeString(eq("xyz"));
		verify(generator).writeString(eq("abc def"));
		verify(generator).writeStartObject();
		verify(generator).writeNumberField(eq("abc"), eq(951));
		verify(generator).writeStringField(eq("862"), eq("xyz"));
		verify(generator).writeEndObject();
		verify(generator).writeEndArray();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeField_primitiveArray() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeField(generator, "fname", new Object[] {456.852, "xyz", new FakeObject(), new FakeJsonExportable(951)});
		verify(generator).writeArrayFieldStart(eq("fname"));
		verify(generator).writeNumber(eq(456.852));
		verify(generator).writeString(eq("xyz"));
		verify(generator).writeString(eq("abc def"));
		verify(generator).writeStartObject();
		verify(generator).writeNumberField(eq("abc"), eq(951));
		verify(generator).writeStringField(eq("862"), eq("xyz"));
		verify(generator).writeEndObject();
		verify(generator).writeEndArray();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeField_Boolean() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeField(generator, "fname", true);
		verify(generator, only()).writeBooleanField(eq("fname"), eq(true));
	}

	@Test
	public void writeField_Integer() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeField(generator, "fname", 456852);
		verify(generator, only()).writeNumberField(eq("fname"), eq(456852));
	}

	@Test
	public void writeField_Double() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeField(generator, "fname", 456.852);
		verify(generator, only()).writeNumberField(eq("fname"), eq(456.852));
	}

	@Test
	public void writeField_String() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeField(generator, "fname", "abc");
		verify(generator, only()).writeStringField(eq("fname"), eq("abc"));
	}

	@Test
	public void writeField_null() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeField(generator, "fname", null);
		verifyNoInteractions(generator);
	}

	@Test
	public void writeValue_serializable() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeValue(generator, new FakeJsonExportable(951));
		verify(generator, times(1)).writeStartObject();
		verify(generator).writeNumberField(eq("abc"), eq(951));
		verify(generator).writeStringField(eq("862"), eq("xyz"));
		verify(generator, times(1)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeValue_Map() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		Map<String, Object> map = new HashMap<>();
		map.put("nvalue", 456.852);
		map.put("svalue", "xyz");
		map.put("fobj", new FakeObject());
		map.put("jobj", new FakeJsonExportable(951));
		JsonUtils.writeValue(generator, map);
		verify(generator, times(2)).writeStartObject();
		verify(generator).writeNumberField(eq("nvalue"), eq(456.852));
		verify(generator).writeStringField(eq("svalue"), eq("xyz"));
		verify(generator).writeStringField(eq("fobj"), eq("abc def"));
		verify(generator).writeFieldName(eq("jobj"));
		verify(generator).writeNumberField(eq("abc"), eq(951));
		verify(generator).writeStringField(eq("862"), eq("xyz"));
		verify(generator, times(2)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeValue_List() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		Object[] array = new Object[] {456.852, "xyz", new FakeObject(), new FakeJsonExportable(951)};
		JsonUtils.writeValue(generator, Arrays.asList(array));
		verify(generator).writeStartArray();
		verify(generator).writeNumber(eq(456.852));
		verify(generator).writeString(eq("xyz"));
		verify(generator).writeString(eq("abc def"));
		verify(generator).writeStartObject();
		verify(generator).writeNumberField(eq("abc"), eq(951));
		verify(generator).writeStringField(eq("862"), eq("xyz"));
		verify(generator).writeEndObject();
		verify(generator).writeEndArray();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeValue_primitiveArray() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeValue(generator, new Object[] {456.852, "xyz", new FakeObject(), new FakeJsonExportable(951)});
		verify(generator).writeStartArray();
		verify(generator).writeNumber(eq(456.852));
		verify(generator).writeString(eq("xyz"));
		verify(generator).writeString(eq("abc def"));
		verify(generator).writeStartObject();
		verify(generator).writeNumberField(eq("abc"), eq(951));
		verify(generator).writeStringField(eq("862"), eq("xyz"));
		verify(generator).writeEndObject();
		verify(generator).writeEndArray();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeValue_Boolean() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeValue(generator, true);
		verify(generator, only()).writeBoolean(eq(true));
	}

	@Test
	public void writeValue_Integer() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeValue(generator, 456852);
		verify(generator, only()).writeNumber(eq(456852));
	}

	@Test
	public void writeValue_Double() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeValue(generator, 456.852);
		verify(generator, only()).writeNumber(eq(456.852));
	}

	@Test
	public void writeValue_String() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeValue(generator, "abc");
		verify(generator, only()).writeString(eq("abc"));
	}

	@Test
	public void writeValue_null() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeValue(generator, null);
		verifyNoInteractions(generator);
	}

	public static class FakeObject {
		@Override
		public String toString() {
			return "abc def";
		}
	}

	public static class FakeJsonExportable implements JsonSerializable {
		private final int id;
		public FakeJsonExportable(int id) {
			this.id = id;
		}
		@Override
		public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeStartObject();
			gen.writeNumberField("abc", this.id);
			gen.writeStringField("862", "xyz");
			gen.writeEndObject();
		}
		@Override
		public void serializeWithType(JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
				throws IOException {
			throw new UnsupportedOperationException();
		}
	}

}
