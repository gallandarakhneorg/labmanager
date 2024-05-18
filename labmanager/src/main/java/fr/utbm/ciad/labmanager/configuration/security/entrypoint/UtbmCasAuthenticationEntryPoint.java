package fr.utbm.ciad.labmanager.configuration.security.entrypoint;

import org.springframework.security.cas.ServiceProperties;

public class UtbmCasAuthenticationEntryPoint extends AbstractCasAuthenticationEntryPoint {
    public UtbmCasAuthenticationEntryPoint(String loginUrl, ServiceProperties serviceProperties) {
        super(loginUrl, serviceProperties);
    }
}
