package fr.utbm.ciad.labmanager.configuration.security.entrypoint;

import org.springframework.security.cas.ServiceProperties;

/**
 * Create a new instance of the {@link AbstractCasAuthenticationEntryPoint} with the given login URL and service properties for the CAS service of UB.
 *
 * @author $Author: jferlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.0
 */
public class UtbmCasAuthenticationEntryPoint extends AbstractCasAuthenticationEntryPoint {
    public UtbmCasAuthenticationEntryPoint(String loginUrl, ServiceProperties serviceProperties) {
        super(loginUrl, serviceProperties);
    }
}
