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

package fr.ciadlab.labmanager.service.indicator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.ciadlab.labmanager.configuration.Constants;
import fr.ciadlab.labmanager.entities.indicator.GlobalIndicators;
import fr.ciadlab.labmanager.entities.organization.ResearchOrganization;
import fr.ciadlab.labmanager.indicators.Indicator;
import fr.ciadlab.labmanager.repository.indicator.GlobalIndicatorsRepository;
import fr.ciadlab.labmanager.service.AbstractService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/** Service related to the global indicators.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 2.2
 */
@Service
public class GlobalIndicatorsService extends AbstractService {

	private static final int MAX_CACHE_AGE = 7;
	
	private final GlobalIndicatorsRepository indicatorRepository;

	private final List<? extends Indicator> allIndicators;

	private final Map<String, Indicator> allIndicatorsPerKey;

	/** Constructor for injector.
	 * This constructor is defined for being invoked by the IOC injector.
	 *
	 * @param messages the provider of localized messages.
	 * @param constants the accessor to the live constants.
	 * @param indicatorRepository the global indicator repository.
	 * @param allIndicators the list of all the indicators that were install in the app.
	 */
	public GlobalIndicatorsService(
			@Autowired MessageSourceAccessor messages,
			@Autowired Constants constants,
			@Autowired GlobalIndicatorsRepository indicatorRepository,
			@Autowired List<? extends Indicator> allIndicators) {
		super(messages, constants);
		this.indicatorRepository = indicatorRepository;
		this.allIndicators = allIndicators;
		this.allIndicatorsPerKey = this.allIndicators.stream().collect(
				Collectors.toMap(Indicator::getKey, Function.identity()));
	}

	/** Ensure that the global indicators' object is created.
	 *
	 * @return the global indicators' object.
	 */
	public GlobalIndicators getGlobalIndicatorsNeverNull() {
		final Optional<GlobalIndicators> opt = this.indicatorRepository.findAll().stream().findFirst();
		GlobalIndicators gi;
		if (opt.isEmpty()) {
			gi = createGlobalIndicators();
		} else {
			gi = opt.get();
		}
		return gi;
	}

	/** Replies the global indicators.
	 *
	 * @return the indicators or {@code null} if there is no global indicators yet.
	 */
	public GlobalIndicators getGlobalIndicatorsOrNull() {
		final Optional<GlobalIndicators> opt = this.indicatorRepository.findAll().stream().findFirst();
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	/** Create the global indicators' object.
	 *
	 * @return a new global indicators' object.
	 */
	protected GlobalIndicators createGlobalIndicators() {
		final GlobalIndicators gi = new GlobalIndicators();
		this.indicatorRepository.save(gi);
		return gi;
	}

	/** Replies all the visible global indicators in the order that they should be displayed.
	 *
	 * @return the visible indicators.
	 */
	public List<? extends Indicator> getVisibleIndicators() {
		return getVisibleIndicatorStream().collect(Collectors.toList());
	}

	/** Replies in a stream all the visible global indicators in the order that they should be displayed.
	 *
	 * @return the visible indicators in a stream.
	 */
	public Stream<? extends Indicator> getVisibleIndicatorStream() {
		final List<String> indicatorKeys = getGlobalIndicatorsNeverNull().getVisibleIndicatorKeyList();		
		return indicatorKeys.stream().map(it -> this.allIndicatorsPerKey.get(it)).filter(it -> it != null);
	}

	/** Replies all the invisible global indicators.
	 *
	 * @return the invisible indicators.
	 */
	public List<? extends Indicator> getInvisibleIndicators() {
		final Set<String> visibles = new TreeSet<>(getGlobalIndicatorsNeverNull().getVisibleIndicatorKeyList());
		if (visibles.isEmpty()) {
			return this.allIndicators;
		}
		return this.allIndicators.stream().filter(it -> {
			return !visibles.contains(it.getKey());
		}).collect(Collectors.toList());
	}

	/** Save or create the global indicators.
	 *
	 * @param visibleIndicators the list of th keys of the visible indicators.
	 */
	public void setVisibleIndicators(List<String> visibleIndicators) {
		final StringBuilder keys = new StringBuilder();
		for (final String key : visibleIndicators) {
			if (keys.length() > 0) {
				keys.append(","); //$NON-NLS-1$
			}
			keys.append(key);
		}
		setVisibleIndicators(keys.toString());
	}

	/** Save or create the global indicators.
	 *
	 * @param visibleIndicators the list of th keys of the visible indicators.
	 */
	public void setVisibleIndicators(String visibleIndicators) {
		final GlobalIndicators gi = getGlobalIndicatorsNeverNull();
		gi.setVisibleIndicatorKeys(visibleIndicators);
		this.indicatorRepository.save(gi);
	}

	/** Replies the values of the visibles indicators, without reading the cache.
	 * The values are computed on-the-fly without reading any cache system.
	 *
	 * @param organization the organization for which the indicators must be computed.
	 * @return the map from the indicator keys to the values.
	 */
	public Map<String, Number> getVisibleIndicatorsValues(ResearchOrganization organization) {
		return getVisibleIndicatorStream().parallel().collect(Collectors.toConcurrentMap(
				it -> it.getKey(),
				it -> it.getNumericValue(organization)));
	}

	/** Replies the indicators and their associated values of the visibles indicators, without reading the cache.
	 * The values are computed on-the-fly without reading any cache system.
	 *
	 * @param organization the organization for which the indicators must be computed.
	 * @param useCache indicates if the value cache must be used or not. If this flag is {@code false}, the values are neither
	 *     read from the cache system nor written back in the cache system.
	 * @return the map from the indicator keys to the values.
	 */
	public List<Pair<? extends Indicator, Number>> getVisibleIndicatorsWithValues(ResearchOrganization organization, boolean useCache) {
		final GlobalIndicators gi = getGlobalIndicatorsNeverNull();
		if (useCache) {
			final Map<String, Number> cache;
			if (gi.getCacheAge() > MAX_CACHE_AGE) {
				gi.resetCachedValues();
				cache = Collections.emptyMap();
			} else {
				cache = gi.getCachedValues();
			}
			assert cache != null;
			return getVisibleIndicatorStream()
				.map(it -> {
					Number value = cache.get(it.getKey());
					if (value == null) {
						value = it.getNumericValue(organization);
						gi.setCachedValues(it.getKey(), value);
						this.indicatorRepository.save(gi);
					}
					return Pair.of(it, value);
				})
				.collect(Collectors.toList());
		}
		return getVisibleIndicatorStream()
				.map(it -> Pair.of(it, it.getNumericValue(organization)))
				.collect(Collectors.toList());
	}

	/** Clear the cache content.
	 */
	public void clearCache() {
		final GlobalIndicators gi = getGlobalIndicatorsOrNull();
		if (gi != null) {
			gi.resetCachedValues();
			this.indicatorRepository.save(gi);
		}
	}

}
