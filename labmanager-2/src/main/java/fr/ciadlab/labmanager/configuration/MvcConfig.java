/*
 * $Id$
 * 
 * Copyright (c) 2019-22, CIAD Laboratory, Universite de Technologie de Belfort Montbeliard
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the CIAD laboratory and the Université de Technologie
 * de Belfort-Montbéliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the CIAD-UTBM.
 * 
 * http://www.ciad-lab.fr/
 */

package fr.ciadlab.labmanager.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** Callbacks for MVC architecture.
 * 
 * @author $Author: sgalland$
 * @author $Author: tmartine$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	/** Bean for converting the HTTP message.
	 *
	 * @return the converter.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		return new StringHttpMessageConverter(Charset.forName("UTF-8")); //$NON-NLS-1$
	}

	/** Bean for resolving the templates.
	 *
	 * @return the resolving.
	 */
	@SuppressWarnings("static-method")
	@Bean
	public ITemplateResolver templateResolver() {
		final ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		resolver.setPrefix("/templates/"); //$NON-NLS-1$
		resolver.setSuffix(".html"); //$NON-NLS-1$
		resolver.setTemplateMode("HTML5"); //$NON-NLS-1$
		resolver.setCharacterEncoding("UTF-8"); //$NON-NLS-1$
		resolver.setCacheable(true);
		return resolver;
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {
		//
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {
		//
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {
		//
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {
		//
	}

	@Override
	public void addFormatters(FormatterRegistry formatterRegistry) {
		//
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS));  //$NON-NLS-1$//$NON-NLS-2$
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCacheControl(CacheControl.maxAge(15, TimeUnit.DAYS)); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		//
	}

	@Override
	public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
		//
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {
		//
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {
		//
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {
		//
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
		//
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> list) {
		//
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
		//
	}

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
		//
	}

	@Override
	public Validator getValidator() {
		return null;
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		return null;
	}

}

