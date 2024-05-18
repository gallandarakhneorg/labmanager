package fr.utbm.ciad.labmanager.configuration.security.entrypoint;

import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;

public abstract class AbstractCasAuthenticationEntryPoint extends CasAuthenticationEntryPoint {
    public AbstractCasAuthenticationEntryPoint(String loginUrl, ServiceProperties serviceProperties) {
        super();
        setLoginUrl(loginUrl);
        setServiceProperties(serviceProperties);
    }
}
