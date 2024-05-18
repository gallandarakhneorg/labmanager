package fr.utbm.ciad.labmanager.configuration.security.filter;

import org.springframework.security.authentication.AuthenticationManager;

public class UtbmCasAuthenticationFilter extends AbstractCasAuthenticationFilter {
    public UtbmCasAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
}
