package fr.utbm.ciad.labmanager.configuration.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.web.CasAuthenticationFilter;

public abstract class AbstractCasAuthenticationFilter extends CasAuthenticationFilter {
    public AbstractCasAuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        setFilterProcessesUrl("/login/cas");
        setAuthenticationManager(authenticationManager);
    }
}
