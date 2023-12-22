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

package fr.utbm.ciad.labmanager.services.publication;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.utbm.ciad.labmanager.configuration.Constants;
import fr.utbm.ciad.labmanager.data.member.Membership;
import fr.utbm.ciad.labmanager.data.member.Person;
import fr.utbm.ciad.labmanager.data.publication.Publication;
import fr.utbm.ciad.labmanager.services.AbstractService;
import org.springframework.context.support.MessageSourceAccessor;

/** Abstract implementation of a service for managing the publications.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPublicationService extends AbstractService {

	/** Constructor.
	 *
	 * @param messages the provider of messages.
	 * @param constants the accessor to the constants.
	 */
	public AbstractPublicationService(MessageSourceAccessor messages, Constants constants) {
		super(messages, constants);
	}

	/** Filter the given publication for returning only those that have authors with an active membership.
	 *
	 * @param <P> the type of the publications to be filtered.
	 * @param publications the publications to filter.
	 * @param organizationId the identifier of the organization for which the publications are accepted.
	 * @param includeSubOrganizations indicates if the members of the suborganizations are considered.
	 * @return the filtered publications.
	 */
	protected static <P extends Publication> Set<P> filterPublicationsWithMemberships(Set<P> publications,
			long organizationId, boolean includeSubOrganizations) {
		final Function<Person, Stream<Membership>> streamBuilder;
		if (includeSubOrganizations) {
			streamBuilder = it -> buildStream(it, organizationId);
		} else {
			streamBuilder = it -> buildStreamStrict(it, organizationId);
		}
		return publications.stream().filter(it -> hasActiveAuthor(it, streamBuilder))
					.collect(Collectors.toUnmodifiableSet());
	}

	private static boolean isOrganizationOf(Membership membership, long organizationId) {
		var ro = membership.getResearchOrganization();
		while (ro != null) {
			if (ro.getId() == organizationId) {
				return true;
			}
			ro = ro.getSuperOrganization();
		}
		return false;
	}
	
	private static Stream<Membership> buildStream(Person author, long organizationId) {
		return author.getMemberships().stream().filter(it -> isOrganizationOf(it, organizationId));
	}
	
	private static Stream<Membership> buildStreamStrict(Person author, long organizationId) {
		return author.getMemberships().stream().filter(it -> it.getId() == organizationId);
	}

	private static boolean hasActiveAuthor(Publication publication, Function<Person, Stream<Membership>> streamBuilder) {
		final var pubDate = publication.getPublicationDate();
		if (pubDate != null) {
			for (final var author : publication.getAuthors()) {
				final var stream = streamBuilder.apply(author).filter(it -> it.isActiveAt(pubDate));
				if (stream.count() > 0) {
					return true;
				}
			}
		} else {
			final var year = publication.getPublicationYear();
			final var start = LocalDate.of(year, 1, 1);
			final var end = LocalDate.of(year, 12, 31);
			for (final var author : publication.getAuthors()) {
				final var stream = streamBuilder.apply(author).filter(it -> it.isActiveIn(start, end));
				if (stream.count() > 0) {
					return true;
				}
			}
		}
		return false;
	}

}
