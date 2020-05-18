package com.neu.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.httpBasic().and().authorizeRequests().antMatchers("/v1/user/self").authenticated();
        httpSecurity.httpBasic().and().authorizeRequests().antMatchers("/v1/recipe").authenticated();
        httpSecurity.httpBasic().and().authorizeRequests().antMatchers("/v1/recipe/{id}").authenticated();


        httpSecurity.csrf().disable();
    }

    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers(HttpMethod.POST, "/v1/user")
                .antMatchers(HttpMethod.GET,"/v1/recipes")
                .antMatchers(HttpMethod.GET,"/v1/recipe")
                .antMatchers(HttpMethod.GET,"/v1/recipe/{id}")
                .antMatchers(HttpMethod.GET,"/v1/health")
                .antMatchers("/actautor/*")
                .antMatchers("/actuator/metrics/*")
                .antMatchers("/actuator/prometheus/*")
                .antMatchers(HttpMethod.GET, "/v1/recipe/{id}/image/{imageId}");
    }

    @Autowired
    CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }

}