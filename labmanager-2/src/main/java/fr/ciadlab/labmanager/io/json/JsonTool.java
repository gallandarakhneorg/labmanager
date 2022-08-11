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

import java.lang.reflect.Method;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.ciadlab.labmanager.entities.journal.JournalQualityAnnualIndicators;
import fr.ciadlab.labmanager.entities.publication.Publication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Definition of constants for the JSON tools.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.0.0
 */
public abstract class JsonTool {

	/** Prefix for hidden JSON fields.
	 */
	protected static final String HIDDEN_FIELD_PREFIX = "_"; //$NON-NLS-1$

	/** Prefix for special JSON fields.
	 */
	protected  static final String SPECIAL_FIELD_PREFIX = "@"; //$NON-NLS-1$

	/** Prefix for research organization identifiers.
	 */
	protected  static final String RESEARCHORGANIZATION_ID_PREFIX = "/ro#"; //$NON-NLS-1$

	/** Prefix for person identifiers.
	 */
	protected  static final String PERSON_ID_PREFIX = "/pers#"; //$NON-NLS-1$

	/** Prefix for journal identifiers.
	 */
	protected  static final String JOURNAL_ID_PREFIX = "/jour#"; //$NON-NLS-1$

	/** Prefix for publication identifiers.
	 */
	protected  static final String PUBLICATION_ID_PREFIX = "/publi#"; //$NON-NLS-1$

	/** The name of the field that is an JSON identifier.
	 */
	protected static final String ID_FIELDNAME = SPECIAL_FIELD_PREFIX + "id"; //$NON-NLS-1$

	/** The name of the field that is the date of the last modification of the file.
	 */
	protected static final String LAST_CHANGE_FIELDNAME = HIDDEN_FIELD_PREFIX + "last-update-date"; //$NON-NLS-1$

	/** Prefix for for the setter functions.
	 */
	protected static final String SETTER_FUNCTION_PREFIX = "set"; //$NON-NLS-1$

	/** Prefix for for the getter functions.
	 */
	protected static final String GETTER_FUNCTION_PREFIX = "get"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to research organizations.
	 */
	protected static final String RESEARCHORGANIZATIONS_SECTION = "researchOrganizations"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to persons.
	 */
	protected static final String PERSONS_SECTION = "persons"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to memberships.
	 */
	protected static final String MEMBERSHIPS_SECTION = "memberships"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to journals.
	 */
	protected static final String JOURNALS_SECTION = "journals"; //$NON-NLS-1$

	/** Main section of the JSON that is dedicated to publications.
	 */
	protected static final String PUBLICATIONS_SECTION = "publications"; //$NON-NLS-1$

	/** Name of the field for the super organization reference.
	 */
	protected static final String SUPERORGANIZATION_KEY = "superOrganization"; //$NON-NLS-1$

	/** Name of the field for the person reference.
	 */
	protected static final String PERSON_KEY = "person"; //$NON-NLS-1$

	/** Name of the field for the research organization reference.
	 */
	protected static final String RESEARCHORGANIZATION_KEY = "researchOrganization"; //$NON-NLS-1$

	/** Name of the field for the definition of the journals' annual quality indicators.
	 */
	protected static final String QUALITYINDICATORSHISTORY_KEY = "qualityIndicatorsHistory"; //$NON-NLS-1$

	/** Name of the field for the Scimago Q-Index.
	 */
	protected static final String SCIMAGOQINDEX_KEY = "scimagoQIndex"; //$NON-NLS-1$

	/** Name of the field for the Web-of-Science Q-Index.
	 */
	protected static final String WOSQINDEX_KEY = "wosQIndex"; //$NON-NLS-1$

	/** Name of the field for the journal impact factor.
	 */
	protected static final String IMPACTFACTOR_KEY = "impactFactor"; //$NON-NLS-1$

	/** Name of the field for the types.
	 */
	protected static final String TYPE_KEY = "type"; //$NON-NLS-1$

	/** Name of the field for the authors of a publication.
	 */
	protected static final String AUTHORS_KEY = "authors"; //$NON-NLS-1$

	/** Name of the field for the journal of a journal paper.
	 */
	protected static final String JOURNAL_KEY = "journal"; //$NON-NLS-1$

	/** Name of the field for the year. This field is usually for {@link Publication}.
	 *
	 * @see #PUBLICATIONYEAR_KEY
	 * @see #REFERENCEYEAR_KEY
	 */
	protected static final String YEAR_KEY = "year"; //$NON-NLS-1$

	/** Name of the field for the year. This field is usually for {@link Publication}.
	 *
	 * @see #YEAR_KEY
	 * @see #REFERENCEYEAR_KEY
	 */
	protected static final String PUBLICATIONYEAR_KEY = "publicationYear"; //$NON-NLS-1$

	/** Name of the field for the year. This field is usually for {@link JournalQualityAnnualIndicators}.
	 *
	 * @see #YEAR_KEY
	 * @see #PUBLICATIONYEAR_KEY
	 */
	protected static final String REFERENCEYEAR_KEY = "referenceYear"; //$NON-NLS-1$

	/** Name of the field for the major language of a publication.
	 *
	 * @see #MAJORLANGUAGE_KEY
	 */
	protected static final String LANGUAGE_KEY = "language"; //$NON-NLS-1$

	/** Name of the field for the major language of a publication.
	 *
	 * @see #LANGUAGE_KEY
	 */
	protected static final String MAJORLANGUAGE_KEY = "majorLanguage"; //$NON-NLS-1$

	/** Name of the field for the month of a publication.
	 */
	protected static final String MONTH_KEY = "month"; //$NON-NLS-1$

	/** Name of the field for the URL of the publication.
	 *
	 * @see #EXTRAURL_KEY
	 */
	protected static final String URL_KEY = "url"; //$NON-NLS-1$

	/** Name of the field for the URL of the publication.
	 *
	 * @see #URL_KEY
	 */
	protected static final String EXTRAURL_KEY = "extraURL"; //$NON-NLS-1$

	/** Logger of the service. It is lazy loaded.
	 */
	private Logger logger;

	/** Replies the logger of this service.
	 *
	 * @return the logger.
	 */
	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = createLogger();
		}
		return this.logger;
	}

	/** Change the logger of this service.
	 *
	 * @param logger the logger.
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/** Factory method for creating the service logger.
	 *
	 * @return the logger.
	 */
	protected Logger createLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	/** Find the setter methods that matches the given names.
	 *
	 * @param type the type of the object on which the setter function must be invoked.
	 * @param names the set of lower-cased names of the setter functions that could serve as candidates for
	 *     finding the method.
	 * @param value the value to give to the setter function. The value is used for checking the formal parameter's type.
	 * @return the method that could be served as setter, or {@code null} if none was found.
	 */
	protected static Method findSetterMethod(Class<?> type, Set<String> names, Object value) {
		assert value != null;
		try {
			final Collection<Method> methods = Arrays.asList(type.getMethods()).parallelStream().filter(it -> {
				if (it.getParameterCount() == 1 && (names.contains(it.getName().toLowerCase()))) {
					final Class<?> receiver = it.getParameterTypes()[0];
					final Class<?> provider = value.getClass();
					return receiver.isAssignableFrom(provider);
				}
				return false;
			}) .collect(Collectors.toSet());
			final int size = methods.size();
			if (size > 1) {
				throw new IllegalArgumentException("Too many setter function candidates (case insensitive) in type '" //$NON-NLS-1$
						+ type.getName() + "' with names: " + names.toString()); //$NON-NLS-1$
			} else if (size == 1) {
				return methods.iterator().next();
			}
		} catch (Throwable ex) {
			//
		}
		return null;
	}

	/** Replies all the getter functions of the given type that have a matching setter function.
	 *
	 * @param source the source type.
	 * @return the map of the methods. Keys are the names of the attributes, and the values are the setter functions.
	 */
	protected static Map<String, Method> findGetterMethods(Class<?> source) {
		final Set<String> setters = new TreeSet<>();
		final Map<String, Method> getters = new HashMap<>();
		for (final Method meth : source.getMethods()) {
			final String name = meth.getName().toLowerCase();
			if (meth.getParameterCount() == 1
					&& (Number.class.equals(meth.getParameterTypes()[0])
							|| String.class.equals(meth.getParameterTypes()[0])
							|| Boolean.class.equals(meth.getParameterTypes()[0]))
					&& name.startsWith(SETTER_FUNCTION_PREFIX)) {
				final String attrName = toLowerFirst(meth.getName().substring(SETTER_FUNCTION_PREFIX.length()));
				setters.add(attrName);
			} else 	if (meth.getParameterCount() == 0
					&& (meth.getReturnType().isPrimitive()
							|| meth.getReturnType().isEnum()
							|| Number.class.isAssignableFrom(meth.getReturnType())
							|| String.class.isAssignableFrom(meth.getReturnType())
							|| Boolean.class.isAssignableFrom(meth.getReturnType())
							|| Date.class.isAssignableFrom(meth.getReturnType())
							|| LocalDate.class.isAssignableFrom(meth.getReturnType())
							|| Character.class.isAssignableFrom(meth.getReturnType()))
					&& name.startsWith(GETTER_FUNCTION_PREFIX)) {
				final String attrName = toLowerFirst(meth.getName().substring(GETTER_FUNCTION_PREFIX.length()));
				getters.put(attrName, meth);
			}
		}
		//
		final Iterator<Entry<String, Method>> iterator = getters.entrySet().iterator();
		while (iterator.hasNext()) {
			final Entry<String, Method> entry = iterator.next();
			if (!setters.contains(entry.getKey())) {
				iterator.remove();
			}
		}
		//
		return getters;
	}

	private static String toLowerFirst(String name) {
		if (name.length() <= 0) {
			return name.toLowerCase();
		}
		final Pattern pattern = Pattern.compile("^[A-Z]+$"); //$NON-NLS-1$
		final Matcher matcher = pattern.matcher(name);
		if (matcher.matches()) {
			return name.toLowerCase();
		}
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	/** Convert the given value into an equivalent value that could be saved into a JSON.
	 *
	 * @param value the value to convert.
	 * @return the JSON compliant value, or {@code null} if the value cannot be saved into JSON.
	 */
	protected static Object convertValue(Object value) {
		if (value != null) {
			final Class<?> type = value.getClass();
			if (value instanceof CharSequence) {
				return ((CharSequence) value).toString();
			}
			if (type.isPrimitive() || value instanceof Number || value instanceof Boolean) {
				return value;
			}
			if (value instanceof Enum) {
				return ((Enum<?>) value).name().toLowerCase();
			}
			if (value instanceof Date) {
				return ((Date) value).toString();
			}
			if (value instanceof LocalDate) {
				return ((LocalDate) value).toString();
			}
			if (value instanceof Character) {
				return ((Character) value).toString();
			}
		}
		return null;
	}

	/** Create a reference to another entity.
	 *
	 * @param id the identifier.
	 */
	protected static JsonElement createReference(String id) {
		final JsonObject ref = new JsonObject();
		ref.addProperty(ID_FIELDNAME, id);
		return ref;
	}

	/** Add entity reference to the given receiver.
	 *
	 * @param receiver the receiver of JSON.
	 * @param key the key that should receive the reference.
	 * @param id the identifier.
	 */
	protected static void addReference(JsonObject receiver, String key, String id) {
		final JsonElement ref = createReference(id);
		receiver.add(key, ref);
	}

}
