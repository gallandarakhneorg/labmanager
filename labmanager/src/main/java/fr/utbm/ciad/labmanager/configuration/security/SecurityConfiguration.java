/*
 * $Id$
 *
 * Copyright (c) 2019-2024, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fr.utbm.ciad.labmanager.configuration.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

/** Configuration of the security for API and Vaadin.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.0
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${labmanager.cas-servers.ub.base}")
    private String casServerUbBaseUrl;

    @Value("${labmanager.cas-servers.ub.login}")
    private String casServerUbLoginUrl;

	private static final String API_URL = "/api/v" + Constants.MANAGER_MAJOR_VERSION + "/**/*"; //$NON-NLS-1$ //$NON-NLS-2$

	/** The default password encoder when using local security system.
	 *
	 * @return the encoder.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public PasswordEncoder passwordEncoder() {
		/* TODO
	   		return new BCryptPasswordEncoder();
		 */
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Access to API is allowed and anonymous
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers(new AntPathRequestMatcher(API_URL)).anonymous());
		http.csrf(cfg -> cfg.ignoringRequestMatchers(new AntPathRequestMatcher(API_URL)));
		
        // Access to images is allowed
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/images/**/*")).permitAll()); //$NON-NLS-1$

		// Access to icons from the line-awesome addon
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()); //$NON-NLS-1$

		super.configure(http);

		setLoginView(http, AdaptiveLoginView.class);
	}

    @Value("${labmanager.cas-servers.ub.service}")
    private String casServerUbServiceUrl;

    @Value("${labmanager.cas-servers.ub.key}")
    private String casServerUbKey;


    @Value("${labmanager.cas-servers.utbm.base}")
    private String casServerUtbmBaseUrl;

    @Value("${labmanager.cas-servers.utbm.login}")
    private String casServerUtbmLoginUrl;

    @Value("${labmanager.cas-servers.utbm.logout}")
    private String casServerUtbmLogoutUrl;

    @Value("${labmanager.cas-servers.utbm.service}")
    private String casServerUtbmServiceUrl;

    @Value("${labmanager.cas-servers.utbm.key}")
    private String casServerUtbmKey;

    /**
     * Configure the security.
     * Override the default configuration to permit access to the login page bypass the vaadin configure.
     *
     * @param http the http security.
     * @throws Exception if an error occurs.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (authorize)
                        -> {
                    authorize
                            .requestMatchers(new AntPathRequestMatcher("/images/**/*")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/VAADIN/**/")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/themes/**")).permitAll()
                            .requestMatchers("/login").permitAll()
                            .requestMatchers("/").permitAll();
                });

        http.csrf(AbstractHttpConfigurer::disable);
    }

    /**
     * Configure the security filter chain.
     * Override the default configuration to add the CAS authentication.
     *
     * @param http the http security.
     * @return the security filter chain.
     * @throws Exception if an error occurs.
     */
    @Override
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        this.configure(http);

        http
                .addFilter(commonFilter())
                .addFilterBefore(new SingleSignOutFilter(), CasAuthenticationFilter.class)
                .authorizeHttpRequests(
                        (authorize)
                                -> authorize
                                .anyRequest().authenticated())
                .exceptionHandling(
                        (exceptions)
                                -> exceptions
                                .authenticationEntryPoint(commonAuthenticationEntryPoint()));

        http
                .logout(
                        (logout)
                                -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .addLogoutHandler(new SecurityContextLogoutHandler())
                                .logoutSuccessUrl("/").permitAll());

        return http.build();
    }

    /**
     * Create the common filter.
     * This filter is used to redirect the user to the correct CAS server based on the organization parameter or to the login page.
     *
     * @return the common filter.
     */
    @Bean
    public CommonAuthenticationFilter commonFilter() {
        UtbmCasAuthenticationProvider utbmProvider
                = new UtbmCasAuthenticationProvider
                (
                        this.userDetailsService,
                        serviceProperties(this.casServerUtbmServiceUrl),
                        ticketValidator(this.casServerUtbmBaseUrl),
                        casServerUtbmKey
                );
        UbCasAuthenticationProvider ubProvider
                = new UbCasAuthenticationProvider
                (
                        this.userDetailsService,
                        serviceProperties(this.casServerUbServiceUrl),
                        ticketValidator(this.casServerUbBaseUrl),
                        casServerUbKey
                );

        return new CommonAuthenticationFilter
                (
                        new UtbmCasAuthenticationFilter(new ProviderManager(utbmProvider)),
                        new UbCasAuthenticationFilter(new ProviderManager(ubProvider))
                );
    }

    /**
     * First step of the CAS authentication process.
     * Create the common authentication entry point that will redirect to the correct CAS server.
     *
     * @return the authentication entry point.
     */
    @Bean
    public CommonAuthenticationEntryPoint commonAuthenticationEntryPoint() {
        UtbmCasAuthenticationEntryPoint utbmEntryPoint = new UtbmCasAuthenticationEntryPoint(this.casServerUtbmLoginUrl, serviceProperties(this.casServerUtbmServiceUrl));
        UbCasAuthenticationEntryPoint ubEntryPoint = new UbCasAuthenticationEntryPoint(this.casServerUbLoginUrl, serviceProperties(this.casServerUbServiceUrl));
        return new CommonAuthenticationEntryPoint(
                utbmEntryPoint,
                ubEntryPoint);
    }

    /**
     * Create a ticket validator.
     *
     * @param url the URL of the CAS server.
     * @return the ticket validator.
     */
    private TicketValidator ticketValidator(String url) {
        return new Cas30ServiceTicketValidator(url);
    }

    /**
     * Create the service properties.
     * This is used to configure the CAS service.
     *
     * @param url the URL of the service.
     * @return the service properties.
     */
    public ServiceProperties serviceProperties(String url) {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(url);
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        return new SingleSignOutFilter();
    }

    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(this.casServerUtbmLogoutUrl, new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/logout");
        return logoutFilter;
    }
}
