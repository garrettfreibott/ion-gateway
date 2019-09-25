/*
 * Copyright (c) 2019 Connexta, LLC
 *
 * Released under the GNU Lesser General Public License version 3; see
 * https://www.gnu.org/licenses/lgpl-3.0.html
 */
package com.connexta.gateway;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterChainProxy;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;

@Configuration
@ConditionalOnClass({EnableWebFluxSecurity.class, WebFilterChainProxy.class})
@ConditionalOnMissingBean({SecurityWebFilterChain.class, WebFilterChainProxy.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@AutoConfigureBefore(ReactiveSecurityAutoConfiguration.class)
@AutoConfigureAfter({
  HealthEndpointAutoConfiguration.class,
  InfoEndpointAutoConfiguration.class,
  WebEndpointAutoConfiguration.class,
  ReactiveOAuth2ClientAutoConfiguration.class
})
@EnableWebFluxSecurity
class WebSecurityConfig {

  // NOTE: This code is modeled after ReactiveManagementWebSecurityAutoConfiguration but tailored
  // for OAuth2 browser
  // and bearer token logins

  @Bean
  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    return http.csrf()
        .disable()
        .authorizeExchange()
        .anyExchange()
        .authenticated()
        .and()
        .oauth2ResourceServer() // handles system-to-system bearer tokens
        .jwt()
        .and()
        .and()
        .oauth2Login() // handles browser redirect logins
        .and()
        .exceptionHandling() // Auto redirects to keycloak login if no auth header is present
        .authenticationEntryPoint(
            new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/master"))
        .and()
        .build();
  }
}
