package fr.utbm.ciad.labmanager.configuration.security.filter;

import org.springframework.security.authentication.AuthenticationManager;

public class UbCasAuthenticationFilter extends AbstractCasAuthenticationFilter {
    public UbCasAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
}
