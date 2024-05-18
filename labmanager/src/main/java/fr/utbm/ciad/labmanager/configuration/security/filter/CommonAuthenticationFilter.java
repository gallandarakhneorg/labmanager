package fr.utbm.ciad.labmanager.configuration.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.cas.web.CasAuthenticationFilter;

import java.io.IOException;

public class CommonAuthenticationFilter extends CasAuthenticationFilter implements Filter {

    private final UtbmCasAuthenticationFilter utbmCasAuthenticationFilter;
    private final UbCasAuthenticationFilter ubCasAuthenticationFilter;

    public CommonAuthenticationFilter(UtbmCasAuthenticationFilter utbmCasAuthenticationFilter, UbCasAuthenticationFilter ubCasAuthenticationFilter) {
        setFilterProcessesUrl("/login/cas");
        this.utbmCasAuthenticationFilter = utbmCasAuthenticationFilter;
        this.ubCasAuthenticationFilter = ubCasAuthenticationFilter;
    }

    @Override
    public void afterPropertiesSet() {
        this.utbmCasAuthenticationFilter.afterPropertiesSet();
        this.ubCasAuthenticationFilter.afterPropertiesSet();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var organizationParam = request.getParameter("organization");
        boolean requiresAuthentication = super.requiresAuthentication(request, response);
        if (!requiresAuthentication) {
            //servletResponse.getWriter().write("cas parameter is missing");
            this.logger.warn("Do not require authentication");
            chain.doFilter(request, response);
        } else {
            this.logger.warn("Do require authentication");
            switch (organizationParam) {
                case "UTBM":
                    this.logger.warn("UTBM");
                    utbmCasAuthenticationFilter.doFilter(request, response, chain);
                    break;
                case "UB":
                    this.logger.warn("UB");
                    ubCasAuthenticationFilter.doFilter(request, response, chain);
                    break;
                default:
                    this.logger.warn("Default");
                    break;
            }
        }
    }
}