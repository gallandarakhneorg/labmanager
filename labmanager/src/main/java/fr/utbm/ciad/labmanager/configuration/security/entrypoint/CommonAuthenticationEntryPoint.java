package fr.utbm.ciad.labmanager.configuration.security.entrypoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * CommonAuthenticationEntryPoint
 * <p>
 * This class is used to redirect the user to the correct CAS server based on the organization parameter or to the login page.
 *
 * @see UtbmCasAuthenticationEntryPoint
 * @see UbCasAuthenticationEntryPoint
 * @see AbstractCasAuthenticationEntryPoint
 * @author $Author: jferlin$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 5.0
 */
public class CommonAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

    private final UtbmCasAuthenticationEntryPoint utbmCasAuthenticationEntryPoint;
    private final UbCasAuthenticationEntryPoint ubCasAuthenticationEntryPoint;

    public CommonAuthenticationEntryPoint(
            UtbmCasAuthenticationEntryPoint utbmCasAuthenticationEntryPoint,
            UbCasAuthenticationEntryPoint ubCasAuthenticationEntryPoint) {
        this.utbmCasAuthenticationEntryPoint = utbmCasAuthenticationEntryPoint;
        this.ubCasAuthenticationEntryPoint = ubCasAuthenticationEntryPoint;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        utbmCasAuthenticationEntryPoint.afterPropertiesSet();
        ubCasAuthenticationEntryPoint.afterPropertiesSet();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        var param = request.getParameter("organization");
        if (param == null) {
            response.sendRedirect("/LabManager/login");
        } else {
            switch (param) {
                case "UTBM":
                    utbmCasAuthenticationEntryPoint.commence(request, response, authException);
                    break;
                case "UB":
                    ubCasAuthenticationEntryPoint.commence(request, response, authException);
                    break;
                default:
                    response.sendRedirect("/LabManager/login");
                    break;
            }
        }

    }
}