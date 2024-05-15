/*
 * $Id$
 * 
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.views.components.addons.wizard;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.google.common.base.Strings;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.QueryParameters;
import fr.utbm.ciad.labmanager.data.IdentifiableEntity;
import io.overcoded.vaadin.wizard.Wizard;
import io.overcoded.vaadin.wizard.WizardStep;
import io.overcoded.vaadin.wizard.config.WizardConfigurationProperties;

/** Abstract implementation of a Wizard with localized steps.
 * 
 * @param <T> the type of the context data.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@Tag("overcoded-wizard")
public abstract class AbstractLabManagerWizard<T extends AbstractContextData> extends Wizard<T> implements HasUrlParameter<String> {

	private static final long serialVersionUID = 8982560094831020617L;

	private static final String IDENTIFIERS_KEY = "ids"; //$NON-NLS-1$

	/** Constructor.
	 *
	 * @param properties the properties that are used by the wizard.
	 * @param context the context data.
	 * @param steps the wizard steps.
	 */
	protected AbstractLabManagerWizard(WizardConfigurationProperties properties, T context, List<WizardStep<T>> steps) {
		super(properties, context, steps,
				backConditionPagerFactory(),
				defaultStartEndPointFactory(),
				defaulyFinishEndPointFactory());
	}

	/** Build route parameters for this wizard that contains the list of the entity identifiers to update.
	 *
	 * @param entities the list of the entities.
	 * @return the route parameters.
	 */
	public static QueryParameters buildQueryParameters(Collection<? extends IdentifiableEntity> entities) {
		final var identifiers = entities.stream().map(it -> Long.toString(it.getId())).toList();
		final var map = Collections.singletonMap(IDENTIFIERS_KEY, identifiers);
		return new QueryParameters(map);
	}

	/** Extract the entity identifiers from the route parameters or the query parameters.
	 *
	 * @param routeParameters the parameters that have been provided in the route.
	 * @param queryParameters the parameters that have been provided in URL query.
	 * @return the entity identifiers.
	 */
	public static Stream<Long> extractParameters(String routeParameters, QueryParameters queryParameters) {
		if (!Strings.isNullOrEmpty(routeParameters)) {
			final var identifiersStr = routeParameters.split("[^0-9]*:[^0-9]*"); //$NON-NLS-1$
			return Arrays.asList(identifiersStr).stream().filter(it -> Pattern.matches("^[0-9]+$", it)).map(it -> Long.valueOf(it)); //$NON-NLS-1$
		}
		if (queryParameters != null) {
			final var list = queryParameters.getParameters(IDENTIFIERS_KEY);
			if (list != null) {
				return list.stream().filter(it -> Pattern.matches("^[0-9]+$", it)).map(it -> Long.valueOf(it)); //$NON-NLS-1$
			}
		}
		return Collections.<Long>emptyList().stream();
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		this.context.setEntityIdentifiers(extractParameters(parameter, event.getLocation().getQueryParameters()).toList());
	}

}
