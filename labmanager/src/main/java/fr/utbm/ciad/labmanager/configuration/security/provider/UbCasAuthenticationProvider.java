package fr.utbm.ciad.labmanager.configuration.security.provider;

import org.apereo.cas.client.validation.TicketValidator;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.userdetails.UserDetailsService;

public class UbCasAuthenticationProvider extends AbstractCasAuthenticationProvider {
    public UbCasAuthenticationProvider(UserDetailsService userDetailsService, ServiceProperties serviceProperties, TicketValidator ticket, String key) {
        super(userDetailsService, serviceProperties, ticket, key);
    }
}
