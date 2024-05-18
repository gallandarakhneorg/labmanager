package fr.utbm.ciad.labmanager.configuration.security.provider;

import org.apereo.cas.client.validation.TicketValidator;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;

public abstract class AbstractCasAuthenticationProvider extends CasAuthenticationProvider {
    public AbstractCasAuthenticationProvider(UserDetailsService userDetailsService, ServiceProperties serviceProperties, TicketValidator ticket, String key) {
        super();
        setUserDetailsService(userDetailsService);
        setAuthenticationUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
        setServiceProperties(serviceProperties);
        setTicketValidator(ticket);
        setKey(key);
    }
}
