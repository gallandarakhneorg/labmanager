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

package fr.ciadlab.labmanager.io.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.ciadlab.labmanager.entities.AttributeProvider;
import fr.ciadlab.labmanager.entities.IdentifiableEntity;
import fr.ciadlab.labmanager.entities.conference.Conference;
import fr.ciadlab.labmanager.entities.journal.Journal;
import fr.ciadlab.labmanager.entities.publication.ConferenceBasedPublication;
import fr.ciadlab.labmanager.entities.publication.JournalBasedPublication;
import fr.ciadlab.labmanager.entities.publication.Publication;

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
		} else if (value != null && (value.getClass().isEnum() || value instanceof Enum)) {
			generator.writeObjectField(name, value);
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
		} else if (value != null && (value.getClass().isEnum() || value instanceof Enum)) {
			generator.writeObject(value);
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
		} else {
			// A value is expected to be written. So that a null node is output.
			generator.writeNull();
		}
	}

	/** Write a reference to the given object.
	 *
	 * @param generator the JSON generator.
	 * @param ref the object to reference. 
	 * @throws IOException if the reference cannot be written.
	 */
	public static void writeObjectRef(JsonGenerator generator, IdentifiableEntity ref) throws IOException {
		if (ref != null && ref.getId() != 0) {
			generator.writeStartObject();
			generator.writeNumberField("@id", ref.getId()); //$NON-NLS-1$
			generator.writeStringField("@type@", ref.getClass().getSimpleName()); //$NON-NLS-1$
			generator.writeEndObject();
		} else {
			generator.writeNull();
		}
	}

	/** Write a field with a reference to the given object.
	 *
	 * @param generator the JSON generator.
	 * @param fieldName the name of the field.
	 * @param ref the object to reference. 
	 * @throws IOException if the reference cannot be written.
	 */
	public static void writeObjectRefField(JsonGenerator generator, String fieldName, IdentifiableEntity ref) throws IOException {
		if (ref != null && ref.getId() != 0) {
			generator.writeObjectFieldStart(fieldName);
			generator.writeNumberField("@id", ref.getId()); //$NON-NLS-1$
			generator.writeStringField("@type@", ref.getClass().getSimpleName()); //$NON-NLS-1$
			generator.writeEndObject();
		} else {
			generator.writeNullField(fieldName);
		}
	}

	/** Write an object and its attributes.
	 *
	 * @param generator the JSON generator.
	 * @param obj the object to write. 
	 * @throws IOException if the reference cannot be written.
	 */
	public static void writeObjectAndAttributes(JsonGenerator generator, AttributeProvider obj) throws IOException {
		if (obj != null) {
			generator.writeStartObject();
			obj.forEachAttribute((attrName, attrValue) -> {
				writeField(generator, attrName, attrValue);
			});
			generator.writeEndObject();
		} else {
			generator.writeNull();
		}
	}

	/** Create a generator with cached entities.
	 *
	 * @param generator the JSON generator.
	 * @return the generator.
	 */
	public static CachedGenerator cache(JsonGenerator generator) {
		return new CachedGenerator(generator);
	}

	/** Create a JSON object mapper with the proper configuration.
	 * This mapper enables to serialize the {@link LocalDate}.
	 *
	 * @return the configured mapper.
	 */
	public static ObjectMapper createMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper;
	}
	
	
	/** Create the JSON node for the entity.
	 *
	 * @param entity the entity.
	 * @return the JSON node, never {@code null}.
	 * @throws JsonProcessingException if the nodes cannot be created
	 */
	public static JsonNode getJsonNode(Object entity) throws JsonProcessingException {
		final ObjectMapper mapper = JsonUtils.createMapper();
		final String temporaryContent = mapper.writeValueAsString(entity);
		return mapper.readTree(temporaryContent);
	}

	/** Generator of JSON with cached entities.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 2.0.0
	 */
	public static class CachedGenerator {

		private final JsonGenerator generator;

		private final Set<Integer> cache = new TreeSet<>();

		private CachedGenerator(JsonGenerator generator) {
			this.generator = generator;
		}

		/** Write the reference to the given object, or the object itself if it is not cached.
		 * Save the object in the cache.
		 *
		 * @param obj the object to write.
		 * @param creator the creator of the entity as JSON.
		 * @throws IOException if the object cannot be written.
		 */
		public void writeReferenceOrObject(IdentifiableEntity obj, CachedGeneratorCreator creator) throws IOException {
			if (obj == null) {
				this.generator.writeNull();
			} else if (this.cache.add(Integer.valueOf(obj.getId()))) {
				creator.create();
			} else {
				writeObjectRef(this.generator, obj);
			}
		}

		/** Write in a field the reference to the given object, or the object itself if it is not cached.
		 * Save the object in the cache.
		 *
		 * @param fieldName the name of the field.
		 * @param obj the object to write.
		 * @param creator the creator of the entity as JSON.
		 * @throws IOException if the object cannot be written.
		 */
		public void writeReferenceOrObjectField(String fieldName, IdentifiableEntity obj, CachedGeneratorCreator creator) throws IOException {
			if (obj != null) {
				this.generator.writeFieldName(fieldName);
				writeReferenceOrObject(obj, creator);
			}
		}

		/** Write a publication and its attributes. This function has a dedicated behavior for references to the
		 * publication's receiver, e.g., the journals.
		 *
		 * @param generator the JSON generator.
		 * @param publication the publication to write. 
		 * @param journalCreator the creator of the journal entity as JSON.
		 * @param conferenceCreator the creator of the conference entity as JSON.
		 * @throws IOException if the reference cannot be written.
		 */
		public void writePublicationAndAttributes(Publication publication, CachedGeneratorCreator1<Journal> journalCreator,
				CachedGeneratorCreator1<Conference> conferenceCreator) throws IOException {
			if (publication != null) {
				this.generator.writeStartObject();
				publication.forEachAttribute((attrName, attrValue) -> {
					writeField(this.generator, attrName, attrValue);
				});
				//
				if (publication instanceof JournalBasedPublication) {
					final JournalBasedPublication jpublication = (JournalBasedPublication) publication;
					final Journal journal = jpublication.getJournal();
					writeReferenceOrObjectField("journal", journal, () -> journalCreator.create(journal)); //$NON-NLS-1$
				} else if (publication instanceof ConferenceBasedPublication) {
					final ConferenceBasedPublication cpublication = (ConferenceBasedPublication) publication;
					final Conference conference = cpublication.getConference();
					writeReferenceOrObjectField("conference", conference, () -> conferenceCreator.create(conference)); //$NON-NLS-1$
				}
				//
				this.generator.writeEndObject();
			} else {
				this.generator.writeNull();
			}
		}

		/** Creator for JSON.
		 * 
		 * @author $Author: sgalland$
		 * @version $Name$ $Revision$ $Date$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 * @since 2.0.0
		 */
		public interface CachedGeneratorCreator {

			/** Create the JSON.
			 *
			 * @throws IOException if the object cannot be written.
			 */
			void create() throws IOException;

		}

		/** Creator for JSON.
		 * 
		 * @param <T> type of object to serialize.
		 * @author $Author: sgalland$
		 * @version $Name$ $Revision$ $Date$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 * @since 2.0.0
		 */
		public interface CachedGeneratorCreator1<T> {

			/** Create the JSON.
			 *
			 * @param obj object to serialize.
			 * @throws IOException if the object cannot be written.
			 */
			void create(T obj) throws IOException;

		}

	}

}