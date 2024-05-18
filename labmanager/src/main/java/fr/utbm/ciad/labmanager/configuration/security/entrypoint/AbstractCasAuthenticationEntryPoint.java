package fr.utbm.ciad.labmanager.configuration.security.entrypoint;

import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;

/**
 * Abstract class for CAS authentication entry point.
 *
 * @author $Author: jferlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.0
 */
public abstract class AbstractCasAuthenticationEntryPoint extends CasAuthenticationEntryPoint {
    public AbstractCasAuthenticationEntryPoint(String loginUrl, ServiceProperties serviceProperties) {
        super();
        setLoginUrl(loginUrl);
        setServiceProperties(serviceProperties);
    }
}
