/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
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

package fr.utbm.ciad.labmanager.tests.utils.io.json;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import fr.utbm.ciad.labmanager.data.AttributeProvider;
import fr.utbm.ciad.labmanager.data.IdentifiableEntity;
import fr.utbm.ciad.labmanager.data.conference.Conference;
import fr.utbm.ciad.labmanager.data.journal.Journal;
import fr.utbm.ciad.labmanager.data.publication.JournalBasedPublication;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils.CachedGenerator;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils.CachedGenerator.CachedGeneratorCreator;
import fr.utbm.ciad.labmanager.utils.io.json.JsonUtils.CachedGenerator.CachedGeneratorCreator1;
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
		verify(generator, times(1)).writeNull();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeObjectRef_null() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeObjectRef(generator, null);
		verify(generator, times(1)).writeNull();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeObjectRef_obj() throws IOException {
		IdentifiableEntity obj = mock(IdentifiableEntity.class);
		when(obj.getId()).thenReturn(14567);
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeObjectRef(generator, obj);
		verify(generator, times(1)).writeStartObject();
		verify(generator, times(1)).writeNumberField(eq("@id"), eq(14567));
		verify(generator, times(1)).writeStringField(eq("@type@"), any());
		verify(generator, times(1)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeObjectRefField_null() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeObjectRefField(generator, "xyz", null);
		verify(generator, times(1)).writeNullField(eq("xyz"));
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeObjectRefField_obj() throws IOException {
		IdentifiableEntity obj = mock(IdentifiableEntity.class);
		when(obj.getId()).thenReturn(14567);
		JsonGenerator generator = mock(JsonGenerator.class);
		JsonUtils.writeObjectRefField(generator, "xyz", obj);
		verify(generator, times(1)).writeObjectFieldStart(eq("xyz"));
		verify(generator, times(1)).writeNumberField(eq("@id"), eq(14567));
		verify(generator, times(1)).writeStringField(eq("@type@"), any());
		verify(generator, times(1)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void writeObjectAndAttributes() throws Exception {
		AttributeProvider obj = mock(AttributeProvider.class);
		JsonGenerator generator = mock(JsonGenerator.class);

		JsonUtils.writeObjectAndAttributes(generator, obj);

		verify(obj, times(1)).forEachAttribute(isNotNull());
		verifyNoMoreInteractions(obj);

		verify(generator, times(1)).writeStartObject();
		verify(generator, times(1)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void cache() {
		assertNotNull(JsonUtils.cache(mock(JsonGenerator.class)));
	}

	@Test
	public void cache_writeReferenceOrObject_firstCall() throws Exception {
		IdentifiableEntity obj = mock(IdentifiableEntity.class);
		when(obj.getId()).thenReturn(14567);
		CachedGeneratorCreator creator = mock(CachedGeneratorCreator.class);
		JsonGenerator generator = mock(JsonGenerator.class);
		CachedGenerator cache = JsonUtils.cache(generator);

		cache.writeReferenceOrObject(obj, creator);
	
		verify(creator, times(1)).create();
		verifyNoInteractions(generator);
	}

	@Test
	public void cache_writeReferenceOrObject_secondCall() throws Exception {
		IdentifiableEntity obj = mock(IdentifiableEntity.class);
		when(obj.getId()).thenReturn(14567);
		CachedGeneratorCreator creator = mock(CachedGeneratorCreator.class);
		JsonGenerator generator = mock(JsonGenerator.class);
		CachedGenerator cache = JsonUtils.cache(generator);

		cache.writeReferenceOrObject(obj, creator);
		reset(creator);

		cache.writeReferenceOrObject(obj, creator);
	
		verifyNoInteractions(creator);

		verify(generator, times(1)).writeStartObject();
		verify(generator, times(1)).writeNumberField(eq("@id"), eq(14567)); //$NON-NLS-1$
		verify(generator, times(1)).writeStringField(eq("@type@"), any()); //$NON-NLS-1$
		verify(generator, times(1)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void cache_writeReferenceOrObjectField_firstCall() throws Exception {
		IdentifiableEntity obj = mock(IdentifiableEntity.class);
		when(obj.getId()).thenReturn(14567);
		CachedGeneratorCreator creator = mock(CachedGeneratorCreator.class);
		JsonGenerator generator = mock(JsonGenerator.class);
		CachedGenerator cache = JsonUtils.cache(generator);

		cache.writeReferenceOrObjectField("xyz", obj, creator);
	
		verify(creator, times(1)).create();
		
		verify(generator, times(1)).writeFieldName(eq("xyz"));
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void cache_writeReferenceOrObjectField_secondCall() throws Exception {
		IdentifiableEntity obj = mock(IdentifiableEntity.class);
		when(obj.getId()).thenReturn(14567);
		CachedGeneratorCreator creator = mock(CachedGeneratorCreator.class);
		JsonGenerator generator = mock(JsonGenerator.class);
		CachedGenerator cache = JsonUtils.cache(generator);

		cache.writeReferenceOrObjectField("abc", obj, creator);
		reset(creator);
		reset(generator);

		cache.writeReferenceOrObjectField("xyz", obj, creator);
	
		verifyNoInteractions(creator);

		verify(generator, times(1)).writeFieldName(eq("xyz"));
		verify(generator, times(1)).writeStartObject();
		verify(generator, times(1)).writeNumberField(eq("@id"), eq(14567)); //$NON-NLS-1$
		verify(generator, times(1)).writeStringField(eq("@type@"), any()); //$NON-NLS-1$
		verify(generator, times(1)).writeEndObject();
		verifyNoMoreInteractions(generator);
	}

	@Test
	public void cache_writePublicationAndAttributes() throws Exception {
		Publication publication = mock(Publication.class);
		CachedGeneratorCreator1<Journal> journalCcreator = mock(CachedGeneratorCreator1.class);
		CachedGeneratorCreator1<Conference> conferenceCreator = mock(CachedGeneratorCreator1.class);
		JsonGenerator generator = mock(JsonGenerator.class);
		CachedGenerator cache = JsonUtils.cache(generator);

		cache.writePublicationAndAttributes(publication, journalCcreator, conferenceCreator);

		verify(publication, times(1)).forEachAttribute(isNotNull());
		verifyNoMoreInteractions(publication);

		verify(generator, times(1)).writeStartObject();
		verify(generator, times(1)).writeEndObject();
		verifyNoMoreInteractions(generator);
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
