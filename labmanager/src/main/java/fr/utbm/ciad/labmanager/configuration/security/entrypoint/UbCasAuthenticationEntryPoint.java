package fr.utbm.ciad.labmanager.configuration.security.entrypoint;

import org.springframework.security.cas.ServiceProperties;

public class UbCasAuthenticationEntryPoint extends AbstractCasAuthenticationEntryPoint {
    public UbCasAuthenticationEntryPoint(String loginUrl, ServiceProperties serviceProperties) {
        super(loginUrl, serviceProperties);
    }
}
