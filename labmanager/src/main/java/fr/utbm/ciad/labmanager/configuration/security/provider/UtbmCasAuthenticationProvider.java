package fr.utbm.ciad.labmanager.configuration.security.provider;

import org.apereo.cas.client.validation.TicketValidator;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.userdetails.UserDetailsService;

public class UtbmCasAuthenticationProvider extends AbstractCasAuthenticationProvider {
    public UtbmCasAuthenticationProvider(UserDetailsService userDetailsService, ServiceProperties serviceProperties, TicketValidator ticket, String key) {
        super(userDetailsService, serviceProperties, ticket, key);
    }
}
